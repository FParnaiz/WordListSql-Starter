/*
 * Copyright (C) 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.example.wordlistsql;

import android.view.View;

import androidx.appcompat.app.AlertDialog;

/**
 * Instantiated for the Edit and Delete buttons in WordListAdapter.
 */
public class MyButtonOnClickListener implements View.OnClickListener {
    private static final String TAG = View.OnClickListener.class.getSimpleName();

    int id;
    String word;
    WordListOpenHelper mDB;
    WordListAdapter mAdapter;


    public MyButtonOnClickListener(int id, String word, WordListOpenHelper db, WordListAdapter adapter) {
        this.id = id;
        this.word = word;
        this.mDB = db;
        this.mAdapter = adapter;
    }

    public void onClick(View v) {

        new AlertDialog.Builder(this.mAdapter.mContext)
                .setTitle("Quieres eliminar la palabra " + word + "?")
                .setPositiveButton("Sí", (dialog, which) -> deleteWord())
                .setNegativeButton("No", null)
                .show();

    }

    public void deleteWord(){
        mDB.delete(id);
        mAdapter.notifyDataSetChanged();
    }
}
