package me.maximpestryakov.yamblzweather.presentation;

import android.content.Context;

import com.arellomobile.mvp.MvpAppCompatActivity;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class BaseActivity extends MvpAppCompatActivity {
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
