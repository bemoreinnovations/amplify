package com.github.stkent.amplify.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import com.github.stkent.amplify.ILogger;
import com.github.stkent.amplify.helpers.BaseTest;
import com.github.stkent.amplify.tracking.interfaces.IAppFeedbackDataProvider;
import com.github.stkent.amplify.tracking.interfaces.IEnvironmentCapabilitiesProvider;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class FeedbackUtilTest extends BaseTest {

    private FeedbackUtil feedbackUtil;

    @Mock
    private IAppFeedbackDataProvider mockAppFeedbackDataProvider;

    @Mock
    private IEnvironmentCapabilitiesProvider mockEnvironmentCapabilitiesProvider;

    @Mock
    private ILogger mockLogger;

    @Mock
    private Activity mockActivity;

    @Captor
    private ArgumentCaptor<Intent> intentCaptor;

    @Test
    public void testThatFeedbackEmailIntentContainsCorrectInformation() {
        // Arrange
        final String fakeAppName = "fake-app-name";
        final String fakeDeviceName = "fake-device-name";
        final String fakeVersionDisplayString = "fake-version-display-string";
        final String fakeEmailAddress = "someone@example.com";

        when(mockAppFeedbackDataProvider.getAppNameString()).thenReturn(fakeAppName);
        when(mockAppFeedbackDataProvider.getDeviceName()).thenReturn(fakeDeviceName);
        when(mockAppFeedbackDataProvider.getVersionDisplayString())
                .thenReturn(fakeVersionDisplayString);

        when(mockEnvironmentCapabilitiesProvider.canHandleIntent(any(Intent.class)))
                .thenReturn(true);

        feedbackUtil = new FeedbackUtil(
                mockAppFeedbackDataProvider,
                mockEnvironmentCapabilitiesProvider,
                fakeEmailAddress,
                mockLogger);

        // Act
        feedbackUtil.showFeedbackEmailChooser(mockActivity);

        // Assert
        verify(mockActivity).startActivity(intentCaptor.capture());

        final Intent capturedIntent = intentCaptor.getValue();
        assertEquals(capturedIntent.getAction(), Intent.ACTION_CHOOSER);

        final Intent targetIntent = capturedIntent.getParcelableExtra(Intent.EXTRA_INTENT);
        assertEquals(targetIntent.getAction(), Intent.ACTION_SENDTO);
        assertEquals(targetIntent.getData(), Uri.parse("mailto:"));

        assertArrayEquals(
                targetIntent.getStringArrayExtra(Intent.EXTRA_EMAIL),
                new String[]{fakeEmailAddress});

        final String expectedEmailSubjectLine = fakeAppName + " Android App Feedback";
        assertEquals(targetIntent.getStringExtra(Intent.EXTRA_SUBJECT), expectedEmailSubjectLine);

        final String expectedEmailBody = "";
        assertEquals(targetIntent.getStringExtra(Intent.EXTRA_TEXT), expectedEmailBody);
    }

    @Test
    public void testThatChooserActivityIsNotStartedIfDeviceCannotHandleEmailIntents() {
        // Arrange
        final String fakeEmailAddress = "someone@example.com";

        when(mockEnvironmentCapabilitiesProvider.canHandleIntent(any(Intent.class)))
                .thenReturn(false);

        feedbackUtil = new FeedbackUtil(
                mockAppFeedbackDataProvider,
                mockEnvironmentCapabilitiesProvider,
                fakeEmailAddress,
                mockLogger);

        // Act
        feedbackUtil.showFeedbackEmailChooser(mockActivity);

        // Assert
        verifyNoMoreInteractions(mockActivity);
    }

}
