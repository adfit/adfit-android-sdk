## 배너 광고 시작하기

배너 광고를 시작하기 위해 먼저 카카오애드핏 플랫폼에서 자신의 매체를 등록한 후, <br/>
배너 광고유형의 광고단위 ID를 발급받아야 합니다.


## AdFit SDK 연동

배너 광고 요청을 위해서는 우선 AdFit SDK 연동이 필요합니다.
해당 내용은 [AdFit 시작하기](GUIDE.md#adfit-시작하기) 내용을 참조 부탁드립니다.


## 화면에 BannerAdView를 추가하기

배너 광고를 노출하기 위해서는 앱 화면에 `BannerAdView` 추가가 필요합니다.
배너 광고를 노출할 위치에 아래와 같이 `BannerAdView`를 추가합니다.

[`activity_banner_sample.xml`](https://github.com/adfit/adfit-android-sdk/blob/master/app/src/main/res/layout/activity_banner_sample.xml)
```xml
<com.kakao.adfit.ads.ba.BannerAdView
    android:layout_width="wrap_content"
    android:layout_height="wrap_content" />
```

* `BannerAdView` 크기는 광고를 노출하는 시점에 [AdFit 플랫폼](https://adfit.kakao.com)에서 설정한 크기에 맞춰 자동 조절됩니다.
* 수신한 광고가 없을 경우, `BannerAdView`는 0x0 사이즈를 갖습니다.
  * 최초부터 사이즈 설정이 필요하신 경우, [`minWidth`](https://developer.android.com/reference/android/view/View#attr_android:minWidth), [`minHeight`](https://developer.android.com/reference/android/view/View#attr_android:minHeight) 속성을 이용하시기 바랍니다.
* `BannerAdView`가 [AdFit 플랫폼](https://adfit.kakao.com)에서 설정한 크기보다 작을 경우, 광고로 인한 **_수익이 발생하지 않을 수 있습니다!!_**
  * ex. 320x50 사이즈를 설정한 경우, 사이즈는 320dp x 50dp 보다 커야 합니다.
* `BannerAdView` 영역을 다른 뷰로 가릴 경우, 광고로 인한 **_수익이 발생하지 않을 수 있습니다!!_**


## BannerAdView 설정 및 광고 요청

배너 광고를 요청하기 위해서는 `BannerAdView` 설정이 필요합니다.
`BannerAdView`를 설정하고 광고를 요청하는 코드는 다음과 같습니다.

[`BannerSampleActivity.kt`](https://github.com/adfit/adfit-android-sdk/blob/master/app/src/main/java/com/kakao/adfit/publisher/sample/BannerSampleActivity.kt)
```kotlin
val adView: BannerAdView
adView.setClientId("발급받은 광고단위 ID")  // 광고단위 ID 설정
adView.setAdListener(object : AdListener {  // optional :: 광고 수신 리스너 설정

    override fun onAdLoaded() {
        // 배너 광고 노출 완료 시 호출
    }

    override fun onAdFailed(errorCode: Int) {
        // 배너 광고 노출 실패 시 호출
    }

    override fun onAdClicked() {
        // 배너 광고 클릭 시 호출
    }
})

// Activity(또는 Fragment)의 lifecycle에 따라 호출
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
```

* 모든 API는 메인 스레드(UI 스레드)에서 호출하시기 바랍니다.
* 광고 요청에 성공한 경우, 응답받은 광고가 `BannerAdView`에 노출됩니다.
* 광고 요청에 실패한 경우, 오류 코드를 `AdListener.onAdFailed(errorCode: Int)`로 전달받을 수 있습니다.<br/>
  | Code | 발생 상황                              |
  |------|-------------------------------------|
  | 202  | 네트워크 오류로 광고 요청에 실패한 경우        |
  | 302  | 요청에는 성공했으나 노출 가능한 광고가 없는 경우 |
  | 그 외 | 기타 오류로 광고 요청에 실패한 경우           |
* 광고 요청에 실패한 경우, 노출되고 있던 이전 광고가 있을 경우에는 이전 광고가 계속 노출됩니다.
* `AdFitNativeAdLoader.load()`는 동시에 하나의 요청만 처리할 수 있으며, 이전 요청이 진행 중이면 새로운 호출은 무시됩니다.
* 광고 갱신 주기는 [AdFit 플랫폼](https://adfit.kakao.com)에서 설정 가능합니다.
* `Activity`(또는 `Fragment`)의 [`Lifecycle`](https://developer.android.com/guide/components/activities/activity-lifecycle#lc)에 따라 <br/>
`BannerAdView`의 `pause`/`resume`/`destroy` API를 호출하지 않을 경우, **광고 수신에 불이익을 받을 수 있습니다.**
* [Lifecycle 라이브러리](https://developer.android.com/topic/libraries/architecture/lifecycle) 사용이 불가능한 경우,
각 이벤트 시점에 맞는 메소드(ex. `onResume()`)를 직접 오버라이드하여 호출합니다.
* Java 코드는 [`BannerJavaSampleActivity.java`](https://github.com/adfit/adfit-android-sdk/blob/master/app/src/main/java/com/kakao/adfit/publisher/sample/BannerJavaSampleActivity.java) 파일을 참조하시기 바랍니다.
