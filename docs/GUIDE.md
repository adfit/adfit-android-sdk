## AdFit 시작하기
* 최신 버전의 Adfit SDK 사용을 권장합니다.
* 최신 버전의 [Android Studio](https://developer.android.com/studio/) 사용을 권장합니다. Eclipse에 대한 기술 지원은 하지 않습니다.
* 최신 버전의 [Kotlin](https://developer.android.com/kotlin/) 사용을 권장합니다.
* Adfit SDK는 [Android 4.0(Ice Cream Sandwich, API Level 14)](https://developer.android.com/about/versions/android-4.0) 이상 기기에서 동작합니다.


### 1 단계 : 광고단위ID(Client ID) 발급받기
광고를 수신하기 위해서는 먼저 [AdFit 플랫폼](http://adfit.kakao.com)에서 앱을 등록하고 광고단위 ID(Client ID)를 발급받아야 합니다.
아래의 웹 사이트에서 앱을 등록하고 광고단위 ID를 발급 받을 수 있습니다.
AdFit 플랫폼 : [http://adfit.kakao.com](http://adfit.kakao.com)


### 2 단계 : 프로젝트에 Adfit SDK 추가하기
Adfit SDK를 사용하기 위해서는 Kotlin과 Google Play Service SDK에 대한 설정이 필요합니다.
Kotlin과 Google Play Service SDK 설정 방법에 대해서는 아래 사이트와 샘플 프로젝트를 참고 부탁드립니다.
* Kotlin 설정 방법: [http://kotlinlang.org/docs/tutorials/kotlin-android.html](http://kotlinlang.org/docs/tutorials/kotlin-android.html)
* 기존 앱에 Kotlin 설정 방법: [https://developer.android.com/kotlin/add-kotlin](https://developer.android.com/kotlin/add-kotlin)
* Google Play Service SDK 설정 방법: [https://developers.google.com/android/guides/setup](https://developers.google.com/android/guides/setup)

Adfit SDK를 추가하는 방법은 다음과 같습니다.

1. 먼저 최상위 [`build.gradle`](https://github.com/adfit/adfit-android-sdk/blob/master/build.gradle) 파일에 Maven repository를 추가합니다.
    ```gradle
    allprojects {
        repositories {
            google()
            jcenter()
            maven { url 'http://devrepo.kakao.com:8088/nexus/content/groups/public/' }
        }
    }
    ```
2. App 모듈 [`build.gradle`](https://github.com/adfit/adfit-android-sdk/blob/master/app/build.gradle) 파일에 최신 버전의 Adfit SDK를 추가합니다.
    ```gradle
    dependencies {
        implementation "org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlin_version"
        implementation "com.google.android.gms:play-services-ads-identifier:$play_service_version"

        implementation "com.kakao.adfit:ads-base:$adfit_version"
    }
    ```
3. 설정 후, 툴바의 Sync Project with Gradle Files를 눌러 변경사항을 반영합니다.


## 타겟 API 레벨 28 이상 대응하기

앱의 targetSdkVersion 설정이 Android 9(API 레벨 28) 이상인 경우, 광고 노출 및 클릭이 정상적으로 동작하기 위해서는
일반 텍스트 트래픽을 허용하는 네트워크 보안 설정이 필요합니다.

1. 프로젝트에 리소스 xml 디렉토리에 [`network_security_config.xml`](../app/src/main/res/xml/network_security_config.xml)을 추가합니다.
    ```xml
    <?xml version="1.0" encoding="utf-8"?>
    <network-security-config>
        <base-config cleartextTrafficPermitted="true"/>
    </network-security-config>
    ```
2. 생성한 파일을 [네트워크 보안 구성 파일로 설정](https://github.com/adfit/adfit-android-sdk/commit/030eabe94692df82f290800ef394bf48c62ddf55)합니다.
앱 [`AndroidManifest.xml`](../app/src/main/AndroidManifest.xml) 파일에서 `<application>`의 `android:networkSecurityConfig` 속성으로 지정합니다.
    ```xml
    <?xml version="1.0" encoding="utf-8"?>
    <manifest ... >
        <application 
            android:networkSecurityConfig="@xml/network_security_config"
            ... >
            ...
        </application>
    </manifest>
    ```

* 네트워크 보안 구성: https://developer.android.com/training/articles/security-config.html
* 설정 commit: https://github.com/adfit/adfit-android-sdk/commit/030eabe94692df82f290800ef394bf48c62ddf55


## 배너 광고 추가하기


### 1 단계 : 화면에 BannerAdView를 추가하기

배너 광고를 노출할 위치에 아래와 같이 BannerAdView를 추가합니다.

[**activity_banner_sample.xml**](https://github.com/adfit/adfit-android-sdk/blob/master/app/src/main/res/layout/activity_banner_sample.xml)
```xml
<com.kakao.adfit.ads.ba.BannerAdView
    android:layout_width="wrap_content"
    android:layout_height="wrap_content" />
```

* 뷰의 크기는 광고를 노출하는 시점에 [AdFit 플랫폼](http://adfit.kakao.com)에서 설정한 크기에 맞춰 자동 조절됩니다.
* 수신한 광고가 없을 경우 뷰는 0x0 사이즈를 갖습니다.
* 뷰가 설정한 크기보다 작을 경우, 광고로 인한 **_수익이 발생하지 않습니다!!_**
* ex. 320x50 사이즈를 설정한 경우, 뷰는 320dp x 50dp 보다 커야 합니다.


### 2 단계 : BannerAdView 설정 및 광고 요청

배너 광고를 요청하기 위해서는 BannerAdView 설정이 필요합니다.
BannerAdView를 설정하고 광고를 요청하는 코드는 다음과 같습니다.

[**BannerSampleActivity.kt**](https://github.com/adfit/adfit-android-sdk/blob/master/app/src/main/java/com/kakao/adfit/publisher/sample/BannerSampleActivity.kt)
```kotlin
val adView: BannerAdView
adView.setClientId("your-clientId")  // 할당 받은 광고 단위(clientId) 설정
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

// activity 또는 fragment의 lifecycle에 따라 호출
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

* 모든 api는 메인 스레드(UI 스레드)에서 호출하시기 바랍니다.
* 광고 요청에 실패한 경우, 이전 광고가 계속 노출됩니다.
* 광고 갱신 주기는 [AdFit 플랫폼](http://adfit.kakao.com)에서 설정 가능합니다.
* Java 코드는 [BannerJava320x50Activity.java](https://github.com/adfit/adfit-android-sdk/blob/master/app/src/main/java/com/kakao/adfit/publisher/sample/BannerJava320x50Activity.java) 파일을 참조하시기 바랍니다.
* [Lifecycle](https://developer.android.com/guide/components/activities/activity-lifecycle#lc)에 따라
BannerAdView의 pause/resume/destroy API를 호출하지 않을 경우,  **광고 수신에 불이익을 받을 수 있습니다.**
* [Lifecycle 라이브러리](https://developer.android.com/topic/libraries/architecture/lifecycle) 사용이 불가능한 경우,
각 이벤트 시점에 맞는 메소드(ex. onResume())를 직접 오버라이드하여 호출합니다.


## FAQ


### Q1. 광고 영역이 비어보입니다. 버그 아닌가요?

최초 광고를 서버로부터 수신하여 로딩하기 전 까지는 시간이 걸리기 때문에 잠시 비어있을 수 있습니다.
광고를 노출하는 시점에 [AdFit 플랫폼](http://adfit.kakao.com)에서 설정한 크기에 맞춰 영역의 크기가 조절되며,
최초 광고를 수신한 이후에는 새로운 광고를 요청 중이거나 로딩에 실패해도 이전 광고를 계속 보여주게 됩니다.
광고 로딩 완료 또는 로딩 실패할 때에 대한 처리가 필요한 경우, AdListener를 등록하여 각 시점에 맞게 처리하시면 됩니다.

### Q2. 광고 수신이 되지 않을때는 어떻게 하나요?

AdFit은 유효 광고의 100% 노출을 보장하지 않습니다. 유효 광고 노출율은 송출 가능한 광고의 총 수량과 광고 호출수에 따라 달라지게 됩니다.
광고의 총 수량은 한정되어 있으나, 이에 비해 광고의 호출수가 많기 때문에 유효 광고의 수신에 실패하는 경우가 자주 발생할 수 있습니다.
또한 시간대나 앱의 종류, 날짜에 따라서도 노출 가능한 광고의 수가 달라질 수 있습니다.

### Q3. 3.0.0 이후 버전에서 에러 코드는 어떻게 되나요?

AdListener의 onAdFailed(int code)에서 받을 수 있는 에러 코드는 아래와 같습니다.

code | 발생 상황
-----|--------------
202  | 광고 요청에 실패한 경우 발생하는 에러 (보통 일시적인 네트워크 오류)
301  | 유효하지 않은 광고 응답을 수신한 경우 발생하는 에러
302  | 보여줄 광고가 없는 경우 발생하는 에러 (NO_AD)
501  | 광고 로딩에 실패한 경우 발생하는 에러
601  | SDK 내부에서 발생한 에러
