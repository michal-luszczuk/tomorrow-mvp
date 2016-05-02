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

package com.propaneapps.sample.ui.list;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.propaneapps.sample.R;
import com.propaneapps.sample.domain.model.User;

import java.util.List;

public class UsersRecyclerViewAdapter extends RecyclerView.Adapter<UserViewHolder> {

    private List<User> users;
    private LayoutInflater inflater;

    public UsersRecyclerViewAdapter(Context context, List<User> users) {
        this.inflater = LayoutInflater.from(context);
        this.users = users;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = inflater.inflate(R.layout.list_item_user, parent, false);
        return new UserViewHolder(root);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        holder.name.setText(users.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}
