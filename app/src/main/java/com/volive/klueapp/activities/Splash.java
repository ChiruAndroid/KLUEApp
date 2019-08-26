package com.volive.klueapp.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.volive.klueapp.R;
import com.volive.klueapp.utils.Constants;
import com.volive.klueapp.utils.PreferenceUtils;

import java.util.Locale;


public class Splash extends AppCompatActivity {
    PreferenceUtils preferenceUtils;
    String strSelectLanguage, checkcurrency;

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash_screen);

        preferenceUtils = new PreferenceUtils(this);
        preferenceUtils.saveString(PreferenceUtils.API_KEY, Constants.API_KEY);
        strSelectLanguage = preferenceUtils.getStringFromPreference(PreferenceUtils.Language, "");
        checkcurrency = preferenceUtils.getStringFromPreference(PreferenceUtils.Currency, "");

        if (checkcurrency.trim().length() > 0) {

        } else {
            preferenceUtils.setCurrency(Constants.SAR);
        }

        if (strSelectLanguage == null || strSelectLanguage.isEmpty()) {
            String languageToLoad = "ar"; // your language
            Locale locale = new Locale(languageToLoad);
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config,
                    getBaseContext().getResources().getDisplayMetrics());
            preferenceUtils.setLanguage("ar");
        } else if (strSelectLanguage.equalsIgnoreCase("en")) {
            String languageToLoad = "en"; // your language
            Locale locale = new Locale(languageToLoad);
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config,
                    getBaseContext().getResources().getDisplayMetrics());
            preferenceUtils.setLanguage("en");
        } else if (strSelectLanguage.equalsIgnoreCase("ar")) {
            String languageToLoad = "ar"; // your language
            Locale locale = new Locale(languageToLoad);
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config,
                    getBaseContext().getResources().getDisplayMetrics());
            preferenceUtils.setLanguage("ar");
        } else {
            String languageToLoad = "ar"; // your language
            Locale locale = new Locale(languageToLoad);
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config,
                    getBaseContext().getResources().getDisplayMetrics());
            preferenceUtils.setLanguage("ar");
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                if (!preferenceUtils.getbooleanFromPreference(preferenceUtils.LOG_OUT, true)) {
                    Intent intent = new Intent(Splash.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(Splash.this, Splash2.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 3000);
    }

}