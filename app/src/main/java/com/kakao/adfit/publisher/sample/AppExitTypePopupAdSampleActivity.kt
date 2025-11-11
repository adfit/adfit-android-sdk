package com.kakao.adfit.publisher.sample

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.annotation.UiThread
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.FragmentResultListener
import com.kakao.adfit.ads.AdError
import com.kakao.adfit.ads.popup.AdFitPopupAd
import com.kakao.adfit.ads.popup.AdFitPopupAdDialogFragment
import com.kakao.adfit.ads.popup.AdFitPopupAdLoader
import com.kakao.adfit.ads.popup.AdFitPopupAdRequest

class AppExitTypePopupAdSampleActivity : AppCompatActivity(), AdFitPopupAdLoader.OnAdLoadListener {

    private val adUnitId: String = "발급받은 광고단위 ID" // FIXME: 발급받은 광고단위 ID를 입력해주세요.

    private lateinit var popupAdLoader: AdFitPopupAdLoader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_popup_ad_smaple)

        /**
         * 앱 종료 광고 요청을 위한 [AdFitPopupAdLoader] 생성
         */
        popupAdLoader = createPopupAdLoader(this, adUnitId)

        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = true
        ViewCompat.setOnApplyWindowInsetsListener(window.decorView) { v, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updatePadding(insets.left, insets.top, insets.right, insets.bottom)

            WindowInsetsCompat.CONSUMED
        }

        val loadAdButton = findViewById<Button>(R.id.loadAdButton)
        loadAdButton.setOnClickListener { loadNewAd() }

        val resetButton = findViewById<Button>(R.id.resetButton)
        resetButton.setOnClickListener {
            /**
             * 광고 노출 빈도 제한 등으로 인해 새로운 팝업 광고를 요청할 수 없는 경우,
             * 테스트를 위해 광고 요청 제한 초기화
             *
             * 테스트 목적으로만 제공되며, 디버그 빌드([BuildConfig.DEBUG]가 `true`인 상태)에서만 작동합니다.
             */
            popupAdLoader.clearRequestPoliciesForTest()
        }

        /**
         * [AdFitPopupAdDialogFragment]에서 발생하는 이벤트를 수신하기 위한 [FragmentResultListener] 등록
         */
        supportFragmentManager.setFragmentResultListener(
            AdFitPopupAdDialogFragment.REQUEST_KEY_POPUP_AD,
            this
        ) { _, bundle ->
            when (bundle.getString(AdFitPopupAdDialogFragment.BUNDLE_KEY_EVENT_TYPE)) {
                AdFitPopupAdDialogFragment.EVENT_AD_CLICKED -> {
                    // TODO: 광고를 클릭한 경우에 대한 처리
                    toast("광고 클릭")
                }

                AdFitPopupAdDialogFragment.EVENT_POPUP_CANCELED -> {
                    // TODO: 팝업을 취소한 경우에 대한 처리
                    toast("팝업 취소")

//                    if (!isFinishing) {
//                        super.finish() // FIXME: 앱 종료 처리
//                    }
                }

                AdFitPopupAdDialogFragment.EVENT_EXIT_CONFIRMED -> {
                    // TODO: "앱 종료"를 선택한 경우에 대한 처리
                    toast("앱 종료")

                    if (!isFinishing) {
                        super.finish() // FIXME: 앱 종료 처리
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        /**
         * [AdFitPopupAdLoader] 종료 처리
         */
        popupAdLoader.destroy()
    }

    override fun finish() {
        // 이미 광고가 화면에 노출 중인지 확인
        val isPopupAdShowing = supportFragmentManager.findFragmentByTag(AdFitPopupAdDialogFragment.TAG) != null
        if (isPopupAdShowing) {
            return
        }

        // 이미 다른 광고를 로딩 중인지 확인
        if (popupAdLoader.isLoading) {
            return
        }

        // 새로운 광고 요청 시도
        val loadingStarted = loadNewAd()
        if (!loadingStarted) {
            // 요청이 시작되지 않은 경우, 즉시 앱 종료를 처리합니다.
            super.finish()
        }
    }

    /**
     * 앱 종료 광고 요청을 위한 [AdFitPopupAdLoader] 생성
     */
    private fun createPopupAdLoader(context: Context, adUnitId: String): AdFitPopupAdLoader {
        return AdFitPopupAdLoader.create(context, adUnitId)
    }

    /**
     * 새로운 앱 종료 광고 요청
     *
     * @return 광고 요청이 성공적으로 '시작'한 경우, true
     */
    private fun loadNewAd(): Boolean {
        if (adUnitId == "발급받은 광고단위 ID") {
            toast("광고단위 ID를 확인해주세요.")
            return false
        }

        // 이미 광고가 화면에 노출 중인지 확인
        val isPopupAdShowing = supportFragmentManager.findFragmentByTag(AdFitPopupAdDialogFragment.TAG) != null
        if (isPopupAdShowing) {
            // AdFitPopupAdDialogFragment를 통해 광고가 이미 노출 중이므로, 새로운 요청을 중단합니다.
            return false
        }

        // 가로 모드인지 확인
        val orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // 가로 모드에서는 팝업 광고를 지원하지 않으므로 요청을 중단합니다.
            return false
        }

        // Activity와 AdFitPopupAdLoader가 유효한 상태인지 확인
        if (isFinishing || isDestroyed || popupAdLoader.isDestroyed) {
            // 종료 중(isFinishing)이거나 파괴된(isDestroyed) 상태에서는  요청할 수 없습니다.
            return false
        }

        // 이미 다른 광고를 로딩 중인지 확인
        if (popupAdLoader.isLoading) {
            // AdFitPopupAdLoader.load()는 동시에 하나의 요청만 처리할 수 있으며, 이전 요청이 완료되기 전까지 새로운 요청은 처리되지 않습니다.
            // 광고가 이미 로딩 중이므로, 새로운 요청을 중단합니다.
            return false
        }

        // 광고 요청이 정책에 의해 제한되었는지 확인
        if (popupAdLoader.isBlockedByRequestPolicy) {
            // 빈도 제한(frequency capping) 등으로 인해 요청이 제한됩니다.
            // TODO: 광고 요청 제한 상태에 대한 처리
            toast("광고 요청 제한 상태")
            return false
        }

        // 새로운 광고 요청
        return popupAdLoader.loadAd(
            AdFitPopupAdRequest.build(AdFitPopupAd.Type.Exit /* 팝업 광고 타입: 앱 종료 */),
            this /* 광고 요청 결과를 전달받기 위한 콜백 인터페이스 */
        )
    }

    /**
     * 광고 요청 성공 콜백
     *
     * [AdFitPopupAdLoader.loadAd]를 통한 광고 요청이 성공하고, 화면에 표시할 광고 데이터를 성공적으로 받아왔을 때 호출됩니다.
     * 이 콜백은 UI 스레드에서 실행됩니다.
     *
     * @param ad 성공적으로 수신한 광고 데이터
     */
    @UiThread
    override fun onAdLoaded(ad: AdFitPopupAd) {
        // Activity가 화면에 더 이상 표시되지 않는 상태인지 확인
        if (isFinishing || isDestroyed) {
            // 광고가 비동기적으로 로드되는 동안 사용자가 화면을 이탈할 수 있으므로,
            // 이 방어 코드는 예기치 않은 오류를 방지하는 데 필수적입니다.
            return
        }

        toast("광고 요청 성공")

        /**
         * 응답 받은 광고([AdFitPopupAd])를 [AdFitPopupAdDialogFragment]를 통해 팝업 광고로 노출합니다.
         */
        AdFitPopupAdDialogFragment(ad)
            .show(supportFragmentManager, AdFitPopupAdDialogFragment.TAG)
    }

    /**
     * 광고 요청 실패 콜백
     *
     * [AdFitPopupAdLoader.loadAd]를 통한 광고 요청이 실패하거나 전달 받은 광고가 없을 때 호출됩니다.
     * 이 콜백은 UI 스레드에서 실행됩니다.
     *
     * @param errorCode 광고 요청에서 발생한 오류코드
     *
     * @see [com.kakao.adfit.ads.AdError]
     */
    @UiThread
    override fun onAdLoadError(errorCode: Int) {
        if (isFinishing || isDestroyed) {
            // 이미 종료된 상태인 경우, 오류 방지를 위해 무시
            return
        }

        toast("광고 요청 실패 [$errorCode]")

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

        super.finish() // FIXME: 앱 종료 처리
    }

    private fun toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
