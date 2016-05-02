/*
 * Copyright (C) 2016 Michał Łuszczuk.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.propaneapps.sample.presenter;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.propaneapps.sample.domain.api.LocalApi;
import com.propaneapps.sample.domain.model.User;
import com.propaneapps.sample.ui.SampleDownloadResultView;
import com.propaneapps.tomorrow.presenter.BasePresenter;
import com.propaneapps.tomorrow.presenter.Presenter;

import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class SampleDownloadTaskPresenter extends BasePresenter<SampleDownloadResultView<List<User>>>
        implements Presenter<SampleDownloadResultView<List<User>>> {

    public static final String BUNDLE_DATA = "data";
    public static final String BUNDLE_LOAD_FINISHED = "loading_finished";
    public static final String BUNDLE_ERROR_OCCURRED = "error_occurred";

    private ArrayList<User> loadedData;
    private boolean loadFinished;
    private boolean loadErrorOccurred;

    @Override
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);

        if (bundle != null) {
            // if bundle is not empty we know that process or activity is recreated, so loader state need to be restored
            loadedData = bundle.getParcelableArrayList(BUNDLE_DATA);
            loadFinished = bundle.getBoolean(BUNDLE_LOAD_FINISHED);
            loadErrorOccurred = bundle.getBoolean(BUNDLE_ERROR_OCCURRED);

            if (!loadFinished) {
                //load was in progress while whole presenter and task was killed, so we need to re-run it
                downloadData();
            }

        } else if (!loadFinished) {
            downloadData();
        }
    }

    private void downloadData() {
        LocalApi.fetchUsers().subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<User>>() {
                    @Override
                    public void call(List<User> listOfUsers) {
                        loadFinished = true;
                        loadedData = new ArrayList<>(listOfUsers);
                        setViewStateBaseOnCurrentState();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        loadFinished = true;
                        loadErrorOccurred = true;
                        setViewStateBaseOnCurrentState();
                    }
                });
    }

    @Override
    public void bindView(SampleDownloadResultView<List<User>> view) {
        super.bindView(view);
        setViewStateBaseOnCurrentState();
    }

    private void setViewStateBaseOnCurrentState() {
        SampleDownloadResultView<List<User>> view = getView();
        if (view != null) {

            if (loadedData != null) {
                view.showContent(loadedData);
            } else if (loadErrorOccurred) {
                view.showConnectionError();
            } else {
                view.showDataLoading();
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        // this method will help us to save some snapshot state of presenter in case of activity or app process kill

        bundle.putParcelableArrayList(BUNDLE_DATA, loadedData);
        bundle.putBoolean(BUNDLE_LOAD_FINISHED, loadFinished);
        bundle.putBoolean(BUNDLE_ERROR_OCCURRED, loadErrorOccurred);
    }
}
