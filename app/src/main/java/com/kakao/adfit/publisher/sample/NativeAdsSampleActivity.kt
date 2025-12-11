package com.kakao.adfit.publisher.sample

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.Toast
import androidx.annotation.UiThread
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.updatePadding
import androidx.lifecycle.Lifecycle
import com.kakao.adfit.ads.AdError
import com.kakao.adfit.ads.na.AdFitAdInfoIconPosition
import com.kakao.adfit.ads.na.AdFitNativeAdBinder
import com.kakao.adfit.ads.na.AdFitNativeAdLayout
import com.kakao.adfit.ads.na.AdFitNativeAdLoader
import com.kakao.adfit.ads.na.AdFitNativeAdRequest
import com.kakao.adfit.ads.na.AdFitNativeAdView
import com.kakao.adfit.ads.na.AdFitVideoAutoPlayPolicy

class NativeAdsSampleActivity : AppCompatActivity(), AdFitNativeAdLoader.AdsLoadListener {

    private val adUnitId: String = "발급받은 광고단위 ID" // FIXME: 발급받은 광고단위 ID를 입력해주세요.

    private lateinit var nativeAdFrameLayout: FrameLayout
    private lateinit var nativeAdLayout: AdFitNativeAdLayout
    private lateinit var loadAdButton: Button

    private lateinit var nativeAdLoader: AdFitNativeAdLoader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_native_ad_sample)

        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = true
        ViewCompat.setOnApplyWindowInsetsListener(window.decorView) { v, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updatePadding(insets.left, insets.top, insets.right, insets.bottom)

            WindowInsetsCompat.CONSUMED
        }

        nativeAdFrameLayout = findViewById(R.id.nativeAdFrameLayout)

        val nativeAdView = layoutInflater.inflate(R.layout.item_native_ad, nativeAdFrameLayout, false)
        nativeAdFrameLayout.addView(nativeAdView)

        nativeAdLayout = AdFitNativeAdLayout.Builder(nativeAdView.findViewById(R.id.containerView)) // 네이티브 광고 영역 (광고 아이콘이 배치 됩니다)
            .setContainerViewClickable(false) // 광고 영역 클릭 가능 여부 (기본값: false)
            .setTitleView(nativeAdView.findViewById(R.id.titleTextView)) // 광고 제목 (필수)
            .setBodyView(nativeAdView.findViewById(R.id.bodyTextView)) // 광고 홍보문구
            .setProfileIconView(nativeAdView.findViewById(R.id.profileIconView)) // 광고주 아이콘 (브랜드 로고)
            .setProfileNameView(nativeAdView.findViewById(R.id.profileNameTextView)) // 광고주 이름 (브랜드명)
            .setMediaView(nativeAdView.findViewById(R.id.mediaView)) // 광고 미디어 소재 (이미지, 비디오) (필수)
            .setCallToActionButton(nativeAdView.findViewById(R.id.callToActionButton)) // 행동유도버튼 (알아보기, 바로가기 등)
            .build()

        loadAdButton = findViewById(R.id.loadAdButton)
        loadAdButton.setOnClickListener {
            loadNativeAd(3) // FIXME: 필요한 개수만큼 네이티브 광고 요청
        }

        // [AdFitNativeAdLoader] 생성
        nativeAdLoader = AdFitNativeAdLoader.create(this, adUnitId)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    /**
     * [count]개의 네이티브 광고를 요청합니다.
     */
    private fun loadNativeAd(count: Int) {
        if (lifecycle.currentState == Lifecycle.State.DESTROYED) {
            return
        }

        if (TextUtils.equals(adUnitId, "발급받은 광고단위 ID")) {
            Toast.makeText(this, "광고단위 ID를 확인해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        // (샘플 구현용) 광고 요청 버튼 비활성화 (동시 요청 제한)
        loadAdButton.isEnabled = false

        // 네이티브 광고 요청 정보 설정
        val request = AdFitNativeAdRequest.Builder()

            /**
             * 광고 정보 아이콘 위치를 설정합니다.
             *
             * 광고 정보 아이콘은 [AdFitNativeAdView] 내 설정한 위치에 노출됩니다.
             *
             * 기본값: [AdFitAdInfoIconPosition.RIGHT_TOP] 우상단
             *
             * @see [AdFitAdInfoIconPosition.LEFT_TOP] 좌상단
             * @see [AdFitAdInfoIconPosition.RIGHT_TOP] 우상단
             * @see [AdFitAdInfoIconPosition.LEFT_BOTTOM] 좌하단
             * @see [AdFitAdInfoIconPosition.RIGHT_BOTTOM] 우하단
             */
            .setAdInfoIconPosition(AdFitAdInfoIconPosition.RIGHT_TOP) // 광고 정보 아이콘 위치 설정 (container view 내에서의 광고 아이콘 위치)

            /**
             * 비디오 광고 자동 재생 정책을 설정합니다.
             *
             * 기본값: [AdFitVideoAutoPlayPolicy.WIFI_ONLY] WiFi 연결 상태에서만 자동재생
             *
             * @see [AdFitVideoAutoPlayPolicy.ALWAYS] 항상 자동재생
             * @see [AdFitVideoAutoPlayPolicy.WIFI_ONLY] WiFi 연결 상태에서만 자동재생
             * @see [AdFitVideoAutoPlayPolicy.NONE] 자동 재생하지 않음
             */
            .setVideoAutoPlayPolicy(AdFitVideoAutoPlayPolicy.WIFI_ONLY) // 비디오 광고 자동 재생 정책 설정
            .build()

        /**
         * **여러 개**의 네이티브 광고를 한 번에 요청합니다.
         *
         * 요청이 성공하면 [AdFitNativeAdLoader.AdsLoadListener.onAdsLoaded]를 통해 결과 리스트를 받습니다.
         * 이때 전달받은 광고의 개수는 요청한 [count]보다 적을 수 있습니다 (**부분 성공**).
         *
         * 유효한 광고가 하나도 없거나 오류가 발생한 경우 [AdFitNativeAdLoader.AdsLoadListener.onAdLoadError]가 호출됩니다.
         *
         * **참고:**
         * - 이미 로딩이 진행 중인 경우, 이 요청은 무시됩니다.
         * - [count]는 1 이상이어야 합니다.
         *
         * @param request 광고 요청에 필요한 설정 정보
         * @param count 요청할 광고의 최대 개수 (1 이상)
         * @param listener 요청 결과를 전달받을 리스너 ([AdFitNativeAdLoader.AdsLoadListener])
         * @return 요청이 정상적으로 시작되면 `true`, 시작되지 않으면 `false`
         */
        nativeAdLoader.loadAds(request, count, this)
    }

    /**
     * 광고 로딩에 성공했을 때 호출됩니다.
     *
     * 1개 이상의 유효한 광고가 로드된 경우 호출됩니다.
     *
     * @param binders 로드된 광고 데이터([AdFitNativeAdBinder])들의 리스트.
     * (리스트의 크기 <= 요청한 개수)
     */
    @UiThread
    override fun onAdsLoaded(binders: List<AdFitNativeAdBinder>) {
        if (lifecycle.currentState == Lifecycle.State.DESTROYED) {
            // [Activity]가 먼저 종료된 경우, 메모리 누수(Memory Leak) 및 오류를 방지를 위해 응답을 무시
            return
        }

        // 2. 응답받은 광고 리스트 반복 처리
        binders.forEach { binder ->
            // (선택사항) 광고 클릭 리스너 등록
            if ("false".toBoolean()) {
                binder.onAdClickListener = object : AdFitNativeAdBinder.OnAdClickListener {

                    override fun onAdClicked(view: View) {
                        Toast.makeText(view.context, "광고 클릭", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            // 레이아웃에 데이터 바인딩
            binder.bind(nativeAdLayout /* FIXME: 각각 다른 View에 데이터 바인딩해야 합니다. */)
        }

        // (샘플 구현용) 광고 요청 버튼 활성화
        loadAdButton.isEnabled = true
    }

    /**
     * 광고 로딩에 실패했을 때 호출됩니다.
     *
     * 모든 광고 로딩에 실패하거나, 네트워크 오류 등이 발생한 경우 호출됩니다.
     *
     * @param errorCode 발생한 오류 코드 ([com.kakao.adfit.ads.AdError] 참조)
     */
    @UiThread
    override fun onAdLoadError(errorCode: Int) {
        if (lifecycle.currentState == Lifecycle.State.DESTROYED) {
            // [Activity]가 먼저 종료된 경우, 오류를 방지를 위해 무시
            return
        }

        // TODO: 요청 실패 처리
        when (errorCode) {
            AdError.NO_AD.errorCode -> {
                // 요청에는 성공했으나 노출 가능한 광고가 없는 경우
            }

            AdError.HTTP_FAILED.errorCode -> {
                // 네트워크 오류로 광고 요청에 실패한 경우
            }

            else -> {
                // 기타 오류로 광고 요청에 실패한 경우
            }
        }

        // (샘플 구현용) 광고 요청 버튼 활성화
        loadAdButton.isEnabled = true
    }
}
