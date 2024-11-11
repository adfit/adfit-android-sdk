package com.kakao.adfit.publisher.sample;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.CookieManager;
import android.webkit.WebView;

import com.kakao.adfit.AdFitSdk;
import com.kakao.adfit.ads.na.AdFitVideoAutoPlayPolicy;

import androidx.annotation.MainThread;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class WebViewAdJavaSampleActivity extends AppCompatActivity {

    private static final String URL = "https://m.daum.net/"; // FIXME :: AdFit 광고를 송출하려는 웹 페이지 URL

    private WebView webView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_webview_ad);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.layout), (v, windowInsets) -> {
            Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(insets.left, insets.top, insets.right, insets.bottom);

            return WindowInsetsCompat.CONSUMED;
        });

        webView = findViewById(R.id.webView);  // 배너 광고 뷰
        initializeWebView(webView);
        initializeVideoAutoPlayPolicy("WifiOnly");
        webView.loadUrl(URL);
    }

    @MainThread
    @SuppressLint("SetJavaScriptEnabled")
    private void initializeWebView(WebView webView) {
        // 3자 쿠키 사용 설정 허용
        CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true);

        // JavaScript 사용 설정
        webView.getSettings().setJavaScriptEnabled(true);

        // Local Storage 사용 설정
        webView.getSettings().setDomStorageEnabled(true);

        // 비디오 자동 재생 허용 설정
        webView.getSettings().setMediaPlaybackRequiresUserGesture(false);

        // AdFit SDK에 WebView 등록
        AdFitSdk.register(webView);
    }

    @MainThread
    private void initializeVideoAutoPlayPolicy(String mode) {
        // 비디오 광고 자동 재생 설정 (기본값: `WIFI_ONLY`)
        if (TextUtils.equals("Always", mode)) { // 항상 자동 재생 허용
            AdFitSdk.setVideoAdAutoPlayPolicy(AdFitVideoAutoPlayPolicy.ALWAYS);
        } else if (TextUtils.equals("WifiOnly", mode)) { // 와아피이 연결 상태에서만 자동 재생 허용
            AdFitSdk.setVideoAdAutoPlayPolicy(AdFitVideoAutoPlayPolicy.WIFI_ONLY);
        } else if (TextUtils.equals("None", mode)) { // 자동 재생을 허용하지 않음
            AdFitSdk.setVideoAdAutoPlayPolicy(AdFitVideoAutoPlayPolicy.NONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        webView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        webView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        webView.destroy();
    }
}
