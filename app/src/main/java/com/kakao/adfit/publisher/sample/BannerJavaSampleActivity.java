package com.kakao.adfit.publisher.sample;

import android.os.Bundle;
import android.widget.Toast;

import com.kakao.adfit.ads.AdListener;
import com.kakao.adfit.ads.ba.BannerAdView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.lifecycle.Lifecycle.Event;
import androidx.lifecycle.LifecycleEventObserver;

public class BannerJavaSampleActivity extends AppCompatActivity {

    private BannerAdView adView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_banner_sample);

        new WindowInsetsControllerCompat(getWindow(), getWindow().getDecorView()).setAppearanceLightStatusBars(true);
        ViewCompat.setOnApplyWindowInsetsListener(getWindow().getDecorView(), (v, windowInsets) -> {
            Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(insets.left, insets.top, insets.right, insets.bottom);

            return WindowInsetsCompat.CONSUMED;
        });

        adView = findViewById(R.id.adView);  // 배너 광고 뷰
        adView.setClientId("발급받은 광고단위 ID");  // 광고단위 ID 설정
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
        getLifecycle().addObserver((LifecycleEventObserver) (source, event) -> {
            if (event == Event.ON_RESUME) {
                if (adView != null) {
                    adView.resume();
                }
            } else if (event == Event.ON_PAUSE) {
                if (adView != null) {
                    adView.pause();
                }

            } else if (event == Event.ON_DESTROY) {
                if (adView != null) {
                    adView.destroy();
                    adView = null;
                }
            }
        });

        adView.loadAd();  // 광고 요청
    }

    @Override
    public void onResume() {
        super.onResume();

        // lifecycle 사용이 불가능한 경우
        if (adView != null) {
            adView.resume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        // lifecycle 사용이 불가능한 경우
        if (adView != null) {
            adView.pause();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // lifecycle 사용이 불가능한 경우
        if (adView != null) {
            adView.destroy();
            adView = null;
        }
    }

    private void toast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
