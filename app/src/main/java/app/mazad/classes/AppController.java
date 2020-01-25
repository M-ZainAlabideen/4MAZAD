package app.mazad.classes;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

import androidx.multidex.MultiDex;

import java.util.Locale;

import app.mazad.R;
import app.mazad.activities.MainActivity;
import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;

public class AppController extends Application {
    private Locale locale = null;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base, Constants.EN));
        MultiDex.install(this);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (locale != null) {
            newConfig.locale = locale;
            Locale.setDefault(locale);
            getBaseContext().getResources().updateConfiguration(newConfig, getBaseContext().getResources().getDisplayMetrics());
        }
    }


}
