package com.kakao.adfit.publisher.sample;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.kakao.adfit.AdfitSdk;

public class AboutActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);

        TextView version = (TextView) findViewById(R.id.version);
        version.setText("Publisher SDK v" + AdfitSdk.SDK_VERSION);

    }
}
