package com.kakao.adfit.publisher.sample;

import android.app.TabActivity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;

import com.kakao.adfit.ads.AdListener;
import com.kakao.adfit.ads.ba.BannerAdView;

public class BannerTypeXML2 extends TabActivity implements OnTabChangeListener {
    private static final String LOGTAG = "BannerTypeXML2";
    private BannerAdView adView = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.banner_type_2);

        // initialize AdFit
        initAdFit();

        // UI
        TabHost mTabHost = getTabHost();
        mTabHost.addTab(mTabHost.newTabSpec("tab1").setIndicator("광고가 있는 탭").setContent(R.id.tab1));
        mTabHost.addTab(mTabHost.newTabSpec("tab2").setIndicator("광고가 없는 탭").setContent(R.id.tab2));

        mTabHost.setOnTabChangedListener(this);

        Button pauseBtn = (Button) findViewById(R.id.pause);
        Button resumeBtn = (Button) findViewById(R.id.resume);

        pauseBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if ( adView != null ) {
                    adView.setRequestInterval(0);
                }
            }
        });

        resumeBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if ( adView != null ) {
                    adView.setRequestInterval(30);
                    adView.loadAd();
                }

            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        if (adView != null) {
            adView.resume();
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        if (adView != null) {
            adView.pause();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (adView != null) {
            adView.destroy();
            adView = null;
        }
    }

    private void initAdFit() {

        // AdFit sdk 초기화 시작
        adView = (BannerAdView) findViewById(R.id.adview);
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                Log.d(LOGTAG, "onAdLoaded");
            }

            @Override
            public void onAdFailed(int code) {
                Log.d(LOGTAG, "onAdFailed : " + code);
            }

            @Override
            public void onAdClicked() {
                Log.d(LOGTAG, "onAdClicked");
            }
        });

        // 할당 받은 clientId 설정
        //adView.setClientId("DAN-s164c5nwco54");
        // 광고 갱신 시간 : 기본 60초
        //adView.setRequestInterval(120);

        // 광고 사이즈 설정
        adView.setAdUnitSize("320x50");

        adView.loadAd();
    }

    public void onTabChanged(String tabId) {

        // 광고 View가 보이지 않을 때는 내부적으로 서버에 광고 요청을 하지 않는다.
        if (tabId.equals("tab1")) {
            adView.setRequestInterval(30);
            adView.loadAd();
        } else if (tabId.equals("tab2")) {
            adView.setRequestInterval(0);
        }

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

}