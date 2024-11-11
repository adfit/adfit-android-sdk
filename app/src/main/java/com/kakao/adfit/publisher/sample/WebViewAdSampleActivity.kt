package com.kakao.adfit.publisher.sample

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.CookieManager
import android.webkit.WebView
import androidx.annotation.MainThread
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.updatePadding
import com.kakao.adfit.AdFitSdk
import com.kakao.adfit.ads.na.AdFitVideoAutoPlayPolicy

class WebViewAdSampleActivity : AppCompatActivity() {

    private val url = "https://m.daum.net/" // FIXME :: AdFit 광고를 송출하려는 웹 페이지 URL

    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_webview_ad)

        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = true
        ViewCompat.setOnApplyWindowInsetsListener(window.decorView) { v, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updatePadding(insets.left, insets.top, insets.right, insets.bottom)

            WindowInsetsCompat.CONSUMED
        }

        webView = findViewById(R.id.webView)
        initializeWebView(webView)
        initializeVideoAutoPlayPolicy("WifiOnly")
        webView.loadUrl(url)
    }

    @MainThread
    @SuppressLint("SetJavaScriptEnabled")
    private fun initializeWebView(webView: WebView) {
        // 3자 쿠키 사용 설정 허용
        CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true)

        // JavaScript 사용 설정
        webView.getSettings().javaScriptEnabled = true

        // Local Storage 사용 설정
        webView.getSettings().domStorageEnabled = true

        // 비디오 자동 재생 허용 설정
        webView.getSettings().mediaPlaybackRequiresUserGesture = false

        // AdFit SDK에 WebView 등록
        AdFitSdk.register(webView)
    }

    @MainThread
    private fun initializeVideoAutoPlayPolicy(mode: String) {
        // 비디오 광고 자동 재생 설정 (기본값: `WIFI_ONLY`)
        when (mode) {
            // 항상 자동 재생 허용
            "Always" -> {
                AdFitSdk.videoAdAutoPlayPolicy = AdFitVideoAutoPlayPolicy.ALWAYS
            }

            // 와아피이 연결 상태에서만 자동 재생 허용
            "WifiOnly" -> {
                AdFitSdk.videoAdAutoPlayPolicy = AdFitVideoAutoPlayPolicy.WIFI_ONLY
            }

            // 자동 재생을 허용하지 않음
            "None" -> {
                AdFitSdk.videoAdAutoPlayPolicy = AdFitVideoAutoPlayPolicy.NONE
            }
        }
    }

    override fun onResume() {
        super.onResume()
        webView.onResume()
    }

    override fun onPause() {
        super.onPause()
        webView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        webView.destroy()
    }
}
