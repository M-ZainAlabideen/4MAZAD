package app.mazad.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import app.mazad.R;
import app.mazad.classes.GlobalFunctions;
import app.mazad.classes.LocaleHelper;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DISPLAY_LENGTH = 3000;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(LocaleHelper.onAttach(newBase)));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //make the screen without statusBar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        GlobalFunctions.setDefaultLanguage(this);
        GlobalFunctions.setUpFont(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
