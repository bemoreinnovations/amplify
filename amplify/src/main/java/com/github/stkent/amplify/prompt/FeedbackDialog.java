package com.github.stkent.amplify.prompt;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatDialog;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.stkent.amplify.R;

/**
 * Created by ahulr on 24-10-2016.
 */

public class FeedbackDialog extends AppCompatDialog implements View.OnClickListener {

    private Context context;
    private Builder builder;
    private TextView tvFeedback, tvSubmit, tvCancel;
    private EditText etFeedback;
    private LinearLayout feedbackButtons;

    private String defaultFormTitle = "Feedback";
    private String defaultSubmitText = "Submit";
    private String defaultCancelText = "Cancel";
    private String defaultHint = "Suggest us what went wrong and \nwe'll work on it.";

    public FeedbackDialog(Context context, Builder builder) {
        super(context);
        this.context = context;
        this.builder = builder;
    }

    public interface FeedbackFormListener {
        void onFormSubmitted(String feedback);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.default_feedback_view);

        tvFeedback = (TextView) findViewById(R.id.dialog_rating_feedback_title);
        tvSubmit = (TextView) findViewById(R.id.dialog_rating_button_feedback_submit);
        tvCancel = (TextView) findViewById(R.id.dialog_rating_button_feedback_cancel);
        etFeedback = (EditText) findViewById(R.id.dialog_rating_feedback);
        feedbackButtons = (LinearLayout) findViewById(R.id.dialog_rating_feedback_buttons);

        init();
    }

    private void init() {

        tvFeedback.setText(TextUtils.isEmpty(builder.formTitle) ? defaultFormTitle : builder.formTitle);
        tvSubmit.setText(TextUtils.isEmpty(builder.submitText) ? defaultSubmitText : builder.submitText);
        tvCancel.setText(TextUtils.isEmpty(builder.cancelText) ? defaultCancelText : builder.cancelText);
        etFeedback.setHint(TextUtils.isEmpty(builder.feedbackFormHint) ? defaultHint : builder.feedbackFormHint);

        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.colorAccent, typedValue, true);
        int color = typedValue.data;

        tvFeedback.setTextColor(ContextCompat.getColor(context, builder.titleTextColor != 0 ? builder.titleTextColor : R.color.black));
        //noinspection ResourceAsColor
        tvSubmit.setTextColor(builder.positiveTextColor != 0 ? ContextCompat.getColor(context, builder.positiveTextColor) : color);
        tvCancel.setTextColor(ContextCompat.getColor(context, builder.negativeTextColor != 0 ? builder.negativeTextColor : R.color.grey_500));

        if (builder.positiveBackgroundColor != 0) {
            tvSubmit.setBackgroundResource(builder.positiveBackgroundColor);

        }
        if (builder.negativeBackgroundColor != 0) {
            tvCancel.setBackgroundResource(builder.negativeBackgroundColor);
        }

        Drawable d = context.getPackageManager().getApplicationIcon(context.getApplicationInfo());

        tvSubmit.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.dialog_rating_button_feedback_submit) {

            String feedback = etFeedback.getText().toString().trim();
            if (TextUtils.isEmpty(feedback)) {

                Animation shake = AnimationUtils.loadAnimation(context, R.anim.shake);
                etFeedback.startAnimation(shake);
                return;
            }

            if (builder.feedbackFormListener != null) {
                builder.feedbackFormListener.onFormSubmitted(feedback);
            }

            dismiss();

        } else if (view.getId() == R.id.dialog_rating_button_feedback_cancel) {

            dismiss();
        }

    }

    private void openForm() {
        tvFeedback.setVisibility(View.VISIBLE);
        etFeedback.setVisibility(View.VISIBLE);
        feedbackButtons.setVisibility(View.VISIBLE);
    }

    private void openPlaystore(Context context) {
        final Uri marketUri = Uri.parse("market://details?id=" + context.getPackageName());
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, marketUri));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(context, "Coudn't find PlayStore on this device", Toast.LENGTH_SHORT).show();
        }
    }

    public TextView getFormTitleTextView() {
        return tvFeedback;
    }

    public TextView getFormSumbitTextView() {
        return tvSubmit;
    }

    public TextView getFormCancelTextView() {
        return tvCancel;
    }

    public static class Builder {

        private final Context context;
        private String formTitle, submitText, cancelText, feedbackFormHint;
        private int positiveTextColor, negativeTextColor, titleTextColor;
        private int positiveBackgroundColor, negativeBackgroundColor;
        private FeedbackFormListener feedbackFormListener;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder titleTextColor(int titleTextColor) {
            this.titleTextColor = titleTextColor;
            return this;
        }

        public Builder positiveButtonTextColor(int positiveTextColor) {
            this.positiveTextColor = positiveTextColor;
            return this;
        }

        public Builder negativeButtonTextColor(int negativeTextColor) {
            this.negativeTextColor = negativeTextColor;
            return this;
        }

        public Builder positiveButtonBackgroundColor(int positiveBackgroundColor) {
            this.positiveBackgroundColor = positiveBackgroundColor;
            return this;
        }

        public Builder negativeButtonBackgroundColor(int negativeBackgroundColor) {
            this.negativeBackgroundColor = negativeBackgroundColor;
            return this;
        }

        public Builder onRatingBarFormSumbit(FeedbackFormListener feedbackFormListener) {
            this.feedbackFormListener = feedbackFormListener;
            return this;
        }

        public Builder formTitle(String formTitle) {
            this.formTitle = formTitle;
            return this;
        }

        public Builder formHint(String formHint) {
            this.feedbackFormHint = formHint;
            return this;
        }

        public Builder formSubmitText(String submitText) {
            this.submitText = submitText;
            return this;
        }

        public Builder formCancelText(String cancelText) {
            this.cancelText = cancelText;
            return this;
        }

        public FeedbackDialog build() {
            return new FeedbackDialog(context, this);
        }
    }
}
