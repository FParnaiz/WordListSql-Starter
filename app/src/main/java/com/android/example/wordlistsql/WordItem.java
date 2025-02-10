package com.android.example.wordlistsql;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class WordItem {

    private int mId;
    private String mWord;

    public WordItem() {}
    public int getId() {return this.mId;}
    public String getWord() {return this.mWord;}
    public void setId(int id) {this.mId = id;}
    public void setWord(String word) {this.mWord = word;}
}