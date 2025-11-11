package com.kakao.adfit.publisher.sample

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.text.TextUtils
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

        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = true
        ViewCompat.setOnApplyWindowInsetsListener(window.decorView) { v, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updatePadding(insets.left, insets.top, insets.right, insets.bottom)

            WindowInsetsCompat.CONSUMED
        }

        val loadAdButton = findViewById<Button>(R.id.loadAdButton)
        loadAdButton.setOnClickListener { loadPopupAd() }

        val resetButton = findViewById<Button>(R.id.resetButton)
        resetButton.setOnClickListener {
            /**
             * "오늘 그만보기"가 설정되었거나 광고 노출 빈도 제한 등으로 인해 새로운 팝업 광고를 요청할 수 없는 경우,
             * 테스트를 위해 요청제한 초기화
             *
             * 테스트 목적으로만 제공되며, 디버그 빌드([BuildConfig.DEBUG]가 `true`인 상태)에서만 작동합니다.
             */
            popupAdLoader.clearRequestPoliciesForTest()
        }

        /**
         * 팝업 광고 요청을 위한 [AdFitPopupAdLoader] 생성
         */
        popupAdLoader = createPopupAdLoader(this as Context, adUnitId)

        /**
         * 팝업 광고 이벤트 수신을 위한 [FragmentResultListener] 등록
         */
        supportFragmentManager.setFragmentResultListener(
            AdFitPopupAdDialogFragment.REQUEST_KEY_POPUP_AD,
            this
        ) { _, bundle ->
            when (bundle.getString(AdFitPopupAdDialogFragment.BUNDLE_KEY_EVENT_TYPE)) {
                AdFitPopupAdDialogFragment.EVENT_AD_CLICKED -> toast("광고 클릭")
                AdFitPopupAdDialogFragment.EVENT_POPUP_CANCELED -> toast("팝업 취소")
                AdFitPopupAdDialogFragment.EVENT_EXIT_CONFIRMED -> {
                    toast("앱종료")
                    super.finish() // FIXME: 앱 종료 처리
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
        val isPopupAdShowing = supportFragmentManager.findFragmentByTag(AdFitPopupAdDialogFragment.TAG) == null
        if (isPopupAdShowing) {
            // 광고가 이미 노출 중인 경우
            return
        }

        val loadingStarted = loadPopupAd()
        if (!loadingStarted && !popupAdLoader.isLoading) {
            // 요청이 불가능한 경우, 종료 처리
            super.finish()
        }
    }

    private fun createPopupAdLoader(context: Context, adUnitId: String): AdFitPopupAdLoader {
        return AdFitPopupAdLoader.create(context, adUnitId)
    }

    /**
     * 새로운 팝업 광고 요청
     */
    private fun loadPopupAd(): Boolean {
        if (TextUtils.equals(adUnitId, "발급받은 광고단위 ID")) {
            toast("광고단위 ID를 확인해주세요.")
            return false
        }

        if (supportFragmentManager.findFragmentByTag(AdFitPopupAdDialogFragment.TAG) != null) {
            // 이미 광고가 노출 중인 경우
            return false
        }


        val orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // 가로 모드일 경우, 팝업 광고를 지원하고 있지 않습니다.
            return false
        }

        val popupAdLoader = popupAdLoader
        if (isFinishing || isDestroyed || popupAdLoader.isDestroyed) {
            // 이미 종료 처리된 경우
            return false
        }

        if (popupAdLoader.isLoading) {
            // 팝업 광고를 이미 요청 중인 경우
            return false
        }

        if (popupAdLoader.isBlockedByRequestPolicy) {
            // "오늘 그만보기"가 설정되었거나 광고 노출 빈도 제한 등으로 인해 새로운 팝업 광고를 요청할 수 없는 경우
            toast("광고 요청 제한 상태")
            return false
        }

        return popupAdLoader.loadAd(
            AdFitPopupAdRequest.build(AdFitPopupAd.Type.Exit /* 앱종료형 */),
            this as AdFitPopupAdLoader.OnAdLoadListener
        )
    }

    /**
     * 광고 요청 성공 콜백
     *
     * 요청에 성공하여 소재를 응답받았을 때 호출됩니다.
     *
     * @param ad 광고 소재 정보를 갖고 있는 [AdFitPopupAd]
     */
    @UiThread
    override fun onAdLoaded(ad: AdFitPopupAd) {
        if (isFinishing || isDestroyed) {
            // 이미 종료된 상태인 경우, 오류 방지를 위해 무시
            return
        }

        if (supportFragmentManager.findFragmentByTag(AdFitPopupAdDialogFragment.TAG) != null) {
            return
        }

        toast("광고 요청 성공")

        /**
         * [AdFitPopupAdDialogFragment]를 통해 팝업 광고 노출
         */
        AdFitPopupAdDialogFragment(ad)
            .show(supportFragmentManager, AdFitPopupAdDialogFragment.TAG)
    }

    /**
     * 광고 요청 실패 콜백
     *
     * 요청에 실패하거나 전달 받은 광고가 없을 때 호출됩니다.
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
