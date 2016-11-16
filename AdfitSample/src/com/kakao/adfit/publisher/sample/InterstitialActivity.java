package com.kakao.adfit.publisher.sample;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import com.kakao.adfit.publisher.AdInterstitial;
import com.kakao.adfit.publisher.AdView.OnAdFailedListener;
import com.kakao.adfit.publisher.AdView.OnAdLoadedListener;
import com.kakao.adfit.publisher.impl.AdError;

public class InterstitialActivity extends Activity {
    AdInterstitial mAdInterstitial = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.interstitial);

        // Interstitial 광고 초기화
        initInterstitial();

        final Button requestBtn = (Button) findViewById(R.id.requestBtn);

        requestBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                mAdInterstitial.loadAd();
            }
        });
    }

    public void initInterstitial() {

        // Interstitial(전면형) 광고 객체 생성
        mAdInterstitial = new AdInterstitial(this);

        // Interstitial(전면형) 광고 클라이언트 아이디 설정
        mAdInterstitial.setClientId("DAN-tocwl5lu39dc");

        // Interstitial(전면형) 광고 로딩시 사용할 Callback 설정
        mAdInterstitial.setOnAdLoadedListener(new OnAdLoadedListener() {
            public void OnAdLoaded() {
                Log.i("InterstitialTab", "광고가 로딩되었습니다.");
            }
        });

        // Interstitial(전면형) 광고 로딩 실패시 사용할 Callback 설정
        mAdInterstitial.setOnAdFailedListener(new OnAdFailedListener() {
            public void OnAdFailed(AdError error, String errorMessage) {
                Toast.makeText(InterstitialActivity.this, errorMessage, Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if ( mAdInterstitial != null ) {
            mAdInterstitial = null;
        }
    }
}