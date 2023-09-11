package com.kakao.adfit.publisher.sample

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.kakao.adfit.ads.AdListener
import com.kakao.adfit.ads.ba.BannerAdView

class BannerSampleActivity : AppCompatActivity() {

    private lateinit var adView: BannerAdView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_banner_sample)

        adView = findViewById(R.id.adView) // 배너 광고 뷰
        adView.setClientId("발급받은 광고단위 ID")  // 광고단위 ID 설정
        adView.setAdListener(object : AdListener { // 광고 수신 리스너 설정

            override fun onAdLoaded() {
                toast("Banner is loaded")
            }

            override fun onAdFailed(errorCode: Int) {
                toast("Failed to load banner :: errorCode = $errorCode")
            }

            override fun onAdClicked() {
                toast("Banner is clicked")
            }
        })

        // lifecycle 사용 가능한 경우
        // 참조 :: https://developer.android.com/topic/libraries/architecture/lifecycle
        lifecycle.addObserver(object : LifecycleObserver {

            @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
            fun onResume() {
                adView.resume()
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
            fun onPause() {
                adView.pause()
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            fun onDestroy() {
                adView.destroy()
            }
        })

        adView.loadAd()  // 광고 요청
    }

    override fun onResume() {
        super.onResume()

        // lifecycle 사용이 불가능한 경우
        adView.resume()
    }

    override fun onPause() {
        super.onPause()

        // lifecycle 사용이 불가능한 경우
        adView.pause()
    }

    override fun onDestroy() {
        super.onDestroy()

        // lifecycle 사용이 불가능한 경우
        adView.destroy()
    }

    private fun toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
