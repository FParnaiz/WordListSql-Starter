package com.android.example.wordlistsql;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SearchActivity extends AppCompatActivity {
    private static final String TAG = EditWordActivity.class.getSimpleName();
    private TextView mTextView;
    private EditText mEditWordView;
    private WordListOpenHelper mDB;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mEditWordView = ((EditText)findViewById(R.id.search_word));
        mTextView = ((TextView) findViewById(R.id.search_result));
        mDB = new WordListOpenHelper(this);
    }

    public void showResult(View view) {
        String word = mEditWordView.getText().toString();
        mTextView.setText("Result for " + word + ":\n\n");

        // Search for the word in the database.
        Cursor cursor = mDB.search(word);

        // Only process a non-null cursor with rows.
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            int index;
            String result;

            // Iterate over the cursor, while there are entries.
            do {
                index = cursor.getColumnIndex(WordListOpenHelper.KEY_WORD);
                result = cursor.getString(index);
                mTextView.append(result + "\n");
            } while (cursor.moveToNext());

            cursor.close();
        } else {
            // Add handling for no results found
            mTextView.append("No matches found");
        }
    }
}