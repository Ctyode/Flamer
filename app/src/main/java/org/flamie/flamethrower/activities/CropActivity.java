package org.flamie.flamethrower.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.flamie.flamethrower.ui.CropObjects;

public class CropActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new CropObjects(getApplicationContext(), this));
        if(getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
    }

}
