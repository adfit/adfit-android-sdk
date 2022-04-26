package com.kakao.adfit.publisher.sample;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.UiThread;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;

import com.kakao.adfit.ads.AdError;
import com.kakao.adfit.ads.na.AdFitAdInfoIconPosition;
import com.kakao.adfit.ads.na.AdFitMediaView;
import com.kakao.adfit.ads.na.AdFitNativeAdBinder;
import com.kakao.adfit.ads.na.AdFitNativeAdLayout;
import com.kakao.adfit.ads.na.AdFitNativeAdLoader;
import com.kakao.adfit.ads.na.AdFitNativeAdRequest;
import com.kakao.adfit.ads.na.AdFitNativeAdView;
import com.kakao.adfit.ads.na.AdFitVideoAutoPlayPolicy;

import org.jetbrains.annotations.NotNull;

public class NativeAdJavaSampleActivity extends AppCompatActivity implements AdFitNativeAdLoader.AdLoadListener {

    private final String adUnitId = "발급받은 광고단위 ID"; // FIXME: 발급받은 광고단위 ID를 입력해주세요.

    private FrameLayout nativeAdFrameLayout;
    private Button loadAdButton;

    private AdFitNativeAdLoader nativeAdLoader;
    private AdFitNativeAdBinder nativeAdBinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_native_ad_smaple);

        nativeAdFrameLayout = findViewById(R.id.nativeAdFrameLayout); // 광고를 노출할 위치

        loadAdButton = findViewById(R.id.loadAdButton);
        loadAdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setEnabled(false); // 광고 요청 버튼 비활성화 (동시 요청 방지)
                loadNativeAd();
            }
        });

        // [AdFitNativeAdLoader] 생성
        nativeAdLoader = AdFitNativeAdLoader.create(this, adUnitId);
    }

    @Override
    protected void onDestroy() {
        if (nativeAdBinder != null) {
            nativeAdBinder.unbind(); // 노출 중인 광고가 있으면 해제
            nativeAdBinder = null;
        }

        nativeAdLoader = null;

        super.onDestroy();
    }

    /**
     * 새로운 네이티브 광고를 요청합니다.
     */
    private void loadNativeAd() {
        if (nativeAdLoader == null) {
            return;
        }

        if (TextUtils.equals(adUnitId, "발급받은 광고단위 ID")) {
            Toast.makeText(this, "광고단위 ID를 확인해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        // 네이티브 광고 요청 정보 설정
        AdFitNativeAdRequest request = new AdFitNativeAdRequest.Builder()

                /*
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

                /*
                 * 비디오 광고 자동 재생 정책을 설정합니다.
                 *
                 * 기본값: [AdFitVideoAutoPlayPolicy.WIFI_ONLY] WiFi 연결 상태에서만 자동재생
                 *
                 * @see [AdFitVideoAutoPlayPolicy.ALWAYS] 항상 자동재생
                 * @see [AdFitVideoAutoPlayPolicy.WIFI_ONLY] WiFi 연결 상태에서만 자동재생
                 * @see [AdFitVideoAutoPlayPolicy.NONE] 자동 재생하지 않음
                 */
                .setVideoAutoPlayPolicy(AdFitVideoAutoPlayPolicy.WIFI_ONLY) // 비디오 광고 자동 재생 정책 설정
                .build();

        /*
         * 새로운 네이티브 광고를 요청합니다.
         *
         * 요청에 성공하여 새로운 광고를 전달받은 경우,
         * [AdFitNativeAdLoader.AdLoadListener.onAdLoaded] 콜백을 통해 광고 소재를 전달받습니다.
         *
         * 요청에 실패하거나 전달받은 광고가 없을 경우,
         * [AdFitNativeAdLoader.AdLoadListener.onAdLoadError] 콜백을 통해 오류코드를 전달받습니다.
         *
         * 동시에 하나의 요청만 처리할 수 있으며,
         * 이전 요청이 진행 중이면 새로운 요청은 무시됩니다.
         */
        nativeAdLoader.loadAd(request, this);
    }

    /**
     * 광고 요청 성공 콜백
     * <p>
     * 요청에 성공하여 소재를 응답받았을 때 호출됩니다.
     *
     * @param binder 광고 소재 정보를 갖고 있는 [AdFitNativeAdBinder]
     */
    @UiThread
    @Override
    public void onAdLoaded(@NotNull AdFitNativeAdBinder binder) {
        if (nativeAdLoader == null ||
                getLifecycle().getCurrentState() == Lifecycle.State.DESTROYED
        ) {
            // [Activity]가 먼저 종료된 경우, 메모리 누수(Memory Leak) 및 오류를 방지를 위해 응답을 무시
            return;
        }

        // 이전에 노출 중인 광고가 있으면 해제
        if (nativeAdBinder != null) {
            nativeAdBinder.unbind();
        }
        nativeAdFrameLayout.removeAllViews();

        View nativeAdView = getLayoutInflater().inflate(R.layout.item_native_ad, nativeAdFrameLayout, false);
        nativeAdFrameLayout.addView(nativeAdView);

        // 광고 SDK에 넘겨줄 [AdFitNativeAdLayout] 정보 구성
        AdFitNativeAdLayout nativeAdLayout =
                new AdFitNativeAdLayout.Builder((AdFitNativeAdView) nativeAdView.findViewById(R.id.containerView)) // 네이티브 광고 영역 (광고 아이콘이 배치 됩니다)
                        .setTitleView((TextView) nativeAdView.findViewById(R.id.titleTextView)) // 광고 타이틀 문구 (필수)
                        .setProfileIconView((ImageView) nativeAdView.findViewById(R.id.profileIconView)) // 광고주(브랜드) 이름
                        .setProfileNameView((TextView) nativeAdView.findViewById(R.id.profileNameTextView)) // // 광고주 아이콘 (브랜드 로고)
                        .setMediaView((AdFitMediaView) nativeAdView.findViewById(R.id.mediaView)) // 광고 이미지 소재 또는 비디오 소재 (필수)
                        .setCallToActionButton((Button) nativeAdView.findViewById(R.id.callToActionButton)) // 행동 유도 버튼 (ex. 알아보기, 바로가기 등)
                        .build();

        // 광고 노출
        nativeAdBinder = binder;
        binder.bind(nativeAdLayout);

        // 광고 요청 버튼 활성화
        loadAdButton.setEnabled(true);
    }

    @UiThread
    @Override
    public void onAdLoadError(int errorCode) {
        // 광고 요청 버튼 활성화
        loadAdButton.setEnabled(true);

        // TODO: 요청 실패 처리
        if (errorCode == AdError.NO_AD.getErrorCode()) {
            // 요청에는 성공했으나 노출 가능한 광고가 없는 경우
        } else if (errorCode == AdError.HTTP_FAILED.getErrorCode()) {
            // 네트워크 오류로 광고 요청에 실패한 경우
        } else {
            // 기타 오류로 광고 요청에 실패한 경우
        }

        if (nativeAdBinder == null) {
            // TODO: 보여지고 있는 광고가 없을 때의 처리
        } else {
            // TODO: 보여지고 있는 광고가 있을 때의 처리
        }
    }
}
