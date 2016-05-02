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
package com.propaneapps.sample.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.propaneapps.sample.R;
import com.propaneapps.sample.domain.model.User;
import com.propaneapps.sample.presenter.SampleDownloadTaskPresenter;
import com.propaneapps.sample.ui.BaseSampleDownloadResultView;
import com.propaneapps.sample.ui.SampleDownloadResultView;
import com.propaneapps.tomorrow.base.BasePresenterFragment;
import com.propaneapps.tomorrow.common.FactoryWithType;

import java.util.List;

public class SampleMvpLoaderFragment extends BasePresenterFragment<SampleDownloadResultView<List<User>>, SampleDownloadTaskPresenter> {

    private SampleDownloadResultView<List<User>> view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_sample_mvp_loader, container, false);
        view = new BaseSampleDownloadResultView(root);
        return root;
    }

    @Override
    public FactoryWithType<SampleDownloadTaskPresenter> getPresenterFactory() {
        return new FactoryWithType<SampleDownloadTaskPresenter>() {
            @Override
            public Class<? extends SampleDownloadTaskPresenter> getTypeClazz() {
                return SampleDownloadTaskPresenter.class;
            }

            @Override
            public SampleDownloadTaskPresenter create() {
                return new SampleDownloadTaskPresenter();
            }
        };
    }

    @Override
    public SampleDownloadResultView<List<User>> getViewLayer() {
        return view;
    }


}
