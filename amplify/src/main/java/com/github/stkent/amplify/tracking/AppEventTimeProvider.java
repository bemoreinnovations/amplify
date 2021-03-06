/**
 * Copyright 2015 Stuart Kent
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.github.stkent.amplify.tracking;

import android.support.annotation.NonNull;

import com.github.stkent.amplify.tracking.interfaces.IAppEventTimeProvider;
import com.github.stkent.amplify.utils.appinfo.IAppInfoProvider;

public final class AppEventTimeProvider implements IAppEventTimeProvider {

    @NonNull
    private final IAppInfoProvider appInfoProvider;

    public AppEventTimeProvider(@NonNull final IAppInfoProvider appInfoProvider) {
        this.appInfoProvider = appInfoProvider;
    }

    @Override
    public long getInstallTime() {
        return appInfoProvider.getPackageInfo().firstInstallTime;
    }

    @Override
    public long getLastUpdateTime() {
        return appInfoProvider.getPackageInfo().lastUpdateTime;
    }

}
