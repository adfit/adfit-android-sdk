package com.kakao.adfit.publisher.sample;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import com.kakao.adfit.publisher.AdView;
import com.kakao.adfit.publisher.AdView.*;
import com.kakao.adfit.publisher.impl.AdError;

public class BannerTypeXML1 extends Activity {
    private static final String LOGTAG = "BannerTypeXML1";

    private LinearLayout adWrapper = null;
    private AdView adView = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.banner_type_1);

        adWrapper = (LinearLayout) findViewById(R.id.adWrapper);
        initAdFit();

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
        adView.setRequestInterval(5);

        // 광고 클릭시 실행할 리스너
        adView.setOnAdClickedListener(new OnAdClickedListener() {
            public void OnAdClicked() {
                Log.i(LOGTAG, "광고를 클릭했습니다.");
            }
        });

        // 광고 내려받기 실패했을 경우에 실행할 리스너
        adView.setOnAdFailedListener(new OnAdFailedListener() {
            public void OnAdFailed(AdError arg0, String arg1) {
                adWrapper.setVisibility(View.GONE);
                Log.w(LOGTAG, arg1);
            }
        });

        // 광고를 정상적으로 내려받았을 경우에 실행할 리스너
        adView.setOnAdLoadedListener(new OnAdLoadedListener() {
            public void OnAdLoaded() {
                adWrapper.setVisibility(View.VISIBLE);
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

        // 광고 사이즈 설정
        adView.setAdUnitSize("320x50");

        // Animation 효과 : 기본 값은 AnimationType.NONE
        adView.setAnimationType(AnimationType.FLIP_HORIZONTAL);

        adView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
