package org.flamie.flamethrower.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.flamie.flamethrower.ui.MainObjects;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new MainObjects(getApplicationContext(), this));
        getSupportActionBar().hide();
    }
}
