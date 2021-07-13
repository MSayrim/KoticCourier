package com.example.koticcourier.Activities;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
public class BaseActivity extends AppCompatActivity {
    protected Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this.getApplicationContext();
    }
}
