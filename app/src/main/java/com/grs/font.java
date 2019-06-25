package com.grs;

import android.app.Application;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class font extends Application {
    @Override
    public void onCreate(){
        super.onCreate();
        initFont();
    }

    private void initFont() {
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/LamoonRegular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }
}
