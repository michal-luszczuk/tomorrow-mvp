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
package com.propaneapps.sample.ui;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.propaneapps.sample.R;
import com.propaneapps.sample.domain.model.User;
import com.propaneapps.sample.ui.list.UsersRecyclerViewAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BaseSampleDownloadResultView implements SampleDownloadResultView<List<User>> {

    @BindView(R.id.progress)
    TextView progress;

    @BindView(R.id.content)
    RecyclerView content;

    @BindView(R.id.error)
    TextView error;

    public BaseSampleDownloadResultView(View rootView) {
        ButterKnife.bind(this, rootView);
    }

    // SampleDownloadResultView methods

    @Override
    public void showContent(List<User> data) {
        hideViews(progress, error);
        showViews(content);

        Context context = content.getContext();
        content.setLayoutManager(new LinearLayoutManager(context));
        content.setAdapter(new UsersRecyclerViewAdapter(content.getContext(), data));
    }

    @Override
    public void showConnectionError() {
        hideViews(progress, content);
        showViews(error);

    }

    @Override
    public void showDataLoading() {
        hideViews(content, error);
        showViews(progress);
    }

    private void hideViews(View... views) {
        for (View view : views) {
            view.setVisibility(View.GONE);
        }
    }

    private void showViews(View... views) {
        for (View view : views) {
            view.setVisibility(View.VISIBLE);
        }
    }
}
