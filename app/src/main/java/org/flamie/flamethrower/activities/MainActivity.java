package org.flamie.flamethrower.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.flamie.flamethrower.ui.MainObjects;
import org.flamie.flamethrower.util.DimenUtils;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DimenUtils.init(getApplicationContext());
        setContentView(new MainObjects(getApplicationContext(), this));
        if(getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
    }

}
