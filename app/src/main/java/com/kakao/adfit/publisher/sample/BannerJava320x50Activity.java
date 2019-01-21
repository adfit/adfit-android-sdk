package com.kakao.adfit.publisher.sample;

import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.kakao.adfit.ads.AdListener;
import com.kakao.adfit.ads.ba.BannerAdView;
//import androidx.lifecycle.Lifecycle;
//import androidx.lifecycle.LifecycleObserver;
//import androidx.lifecycle.OnLifecycleEvent;

public class BannerJava320x50Activity extends AppCompatActivity {

    private BannerAdView adView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_banner_sample);

        adView = findViewById(R.id.adView);  // 배너 광고 뷰
        adView.setClientId("DAN-1h82js7czjqsj");  // 할당 받은 광고 단위(clientId) 설정
        adView.setAdListener(new AdListener() {  // 광고 수신 리스너 설정

            @Override
            public void onAdLoaded() {
                toast("Banner is loaded");
            }

            @Override
            public void onAdFailed(int errorCode) {
                toast("Failed to load banner :: errorCode = " + errorCode);
            }

            @Override
            public void onAdClicked() {
                toast("Banner is clicked");
            }

        });

        // lifecycle 사용 가능한 경우
        // 참조 :: https://developer.android.com/topic/libraries/architecture/lifecycle
//        getLifecycle().addObserver(new LifecycleObserver() {
//
//            @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
//            public void onResume() {
//                if (adView == null) return;
//                adView.resume();
//            }
//
//            @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
//            public void onPause() {
//                if (adView == null) return;
//                adView.pause();
//            }
//
//            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
//            public void onDestroy() {
//                if (adView == null) return;
//                adView.destroy();
//                adView = null;
//            }
//
//        });

        adView.loadAd();  // 광고 요청
    }

    @Override
    public void onResume() {
        super.onResume();

        // lifecycle 사용이 불가능한 경우
        if (adView == null) return;
        adView.resume();
    }

    @Override
    public void onPause() {
        super.onPause();

        // lifecycle 사용이 불가능한 경우
        if (adView == null) return;
        adView.pause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // lifecycle 사용이 불가능한 경우
        if (adView == null) return;
        adView.destroy();
    }

    private void toast(String message) {
        if (adView == null) return;
        Toast.makeText(adView.getContext(), message, Toast.LENGTH_SHORT).show();
    }

}
