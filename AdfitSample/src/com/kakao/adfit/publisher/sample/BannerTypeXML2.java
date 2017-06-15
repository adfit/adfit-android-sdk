package com.kakao.adfit.publisher.sample;

import android.app.TabActivity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import com.kakao.adfit.publisher.AdView;
import com.kakao.adfit.publisher.AdView.*;
import com.kakao.adfit.publisher.impl.AdError;

public class BannerTypeXML2 extends TabActivity implements OnTabChangeListener {
    private static final String LOGTAG = "BannerTypeXML2";
    private AdView adView = null;

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
                    // 서버로부터 광고 요청 중단
                    adView.pause();
                }
            }
        });

        resumeBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if ( adView != null ) {
                    // 서버로부터 광고 요청 재개
                    adView.resume();
                }

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if ( adView != null ) {
            adView.destroy();
            adView = null;
        }
    }

    private void initAdFit() {

        // AdFit sdk 초기화 시작
        adView = (AdView) findViewById(R.id.adview);

        // 광고 클릭시 실행할 리스너
        adView.setOnAdClickedListener(new OnAdClickedListener() {
            public void OnAdClicked() {
                Log.i(LOGTAG, "광고를 클릭했습니다.");
            }
        });

        // 광고 내려받기 실패했을 경우에 실행할 리스너
        adView.setOnAdFailedListener(new OnAdFailedListener() {
            public void OnAdFailed(AdError arg0, String arg1) {
                Log.w(LOGTAG, arg1);
            }
        });

        // 광고를 정상적으로 내려받았을 경우에 실행할 리스너
        adView.setOnAdLoadedListener(new OnAdLoadedListener() {
            public void OnAdLoaded() {
                Log.i(LOGTAG, "광고가 정상적으로 로딩되었습니다.");
            }
        });

        // 광고를 불러올때 실행할 리스너
        adView.setOnAdWillLoadListener(new OnAdWillLoadListener() {
            public void OnAdWillLoad(String arg1) {
                Log.i(LOGTAG, "광고를 불러옵니다. : " + arg1);
            }
        });

        // 광고를 닫았을때 실행할 리스너
        adView.setOnAdClosedListener(new OnAdClosedListener() {
            public void OnAdClosed() {
                Log.i(LOGTAG, "광고를 닫았습니다.");
            }
        });

        // 할당 받은 clientId 설정
        adView.setClientId("DAN-s164c5nwco54");

        // 광고 갱신 시간 : 기본 60초
        adView.setRequestInterval(12);

        // Animation 효과 : 기본 값은 AnimationType.NONE
        adView.setAnimationType(AnimationType.FLIP_HORIZONTAL);

        adView.setVisibility(View.VISIBLE);
    }

    public void onTabChanged(String tabId) {

        // 광고 View가 보이지 않을 때는 내부적으로 서버에 광고 요청을 하지 않는다.
        if ( tabId.equals("tab1") ) {
            adView.setVisibility(View.VISIBLE);
        } else if ( tabId.equals("tab2") ) {
            adView.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

}