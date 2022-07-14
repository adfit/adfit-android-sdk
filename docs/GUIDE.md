## AdFit 시작하기

* 최신 버전의 AdFit SDK 사용을 권장합니다.
* 최신 버전의 [Android Studio](https://developer.android.com/studio/) 사용을 권장합니다. Eclipse에 대한 기술 지원은 하지 않습니다.
* 최신 버전의 [Kotlin](https://developer.android.com/kotlin/) 사용을 권장합니다.
* AdFit SDK는 [Android 5.0(Lollipop, API Level 21)](https://developer.android.com/about/versions/android-5.0) 이상 기기에서 동작합니다.


### 광고단위 ID 발급받기

광고를 수신하기 위해서는 먼저 [AdFit 플랫폼](https://adfit.kakao.com)에서 앱을 등록하고 광고단위 ID를 발급받아야 합니다.<br/>
아래의 웹 사이트에서 앱을 등록하고 광고단위 ID를 발급 받을 수 있습니다.<br/>
* AdFit 플랫폼 : [https://adfit.kakao.com](https://adfit.kakao.com)


### 프로젝트에 AdFit SDK 추가하기

AdFit SDK를 사용하기 위해서는 Kotlin과 Google Play Service SDK에 대한 설정이 필요합니다.
Kotlin과 Google Play Service SDK 설정 방법에 대해서는 아래 사이트와 샘플 프로젝트를 참고 부탁드립니다.
* Kotlin 설정 방법: [https://kotlinlang.org/docs/tutorials/kotlin-android.html](https://kotlinlang.org/docs/tutorials/kotlin-android.html)
* 기존 앱에 Kotlin 설정 방법: [https://developer.android.com/kotlin/add-kotlin](https://developer.android.com/kotlin/add-kotlin)
* Google Play Service SDK 설정 방법: [https://developers.google.com/android/guides/setup](https://developers.google.com/android/guides/setup)

AdFit SDK를 추가하는 방법은 다음과 같습니다.

1. 먼저 최상위 [`build.gradle`](https://github.com/adfit/adfit-android-sdk/blob/master/build.gradle) 파일에 Maven repository를 추가합니다.
    ```gradle
    allprojects {
        repositories {
            google()
            jcenter()
            maven { url 'https://devrepo.kakao.com/nexus/content/groups/public/' }
        }
    }
    ```
2. App 모듈 [`build.gradle`](https://github.com/adfit/adfit-android-sdk/blob/master/app/build.gradle) 파일에 최신 버전의 AdFit SDK를 추가합니다.
    ```gradle
    dependencies {
        implementation "org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlin_version"
        implementation "com.google.android.gms:play-services-ads-identifier:$play_service_version"

        implementation "com.kakao.adfit:ads-base:$adfit_version"
    }
    ```
3. 설정 후, 툴바의 Sync Project with Gradle Files를 눌러 변경사항을 반영합니다.


## <del>타겟 API 레벨 28 이상 대응하기</del>

<del>앱의 targetSdkVersion 설정이 Android 9(API 레벨 28) 이상인 경우, 광고 노출 및 클릭이 정상적으로 동작하기 위해서는
일반 텍스트 트래픽을 허용하는 네트워크 보안 설정이 필요합니다.<del/>

AdFit SDK v3.11.10 버전부터는 일반 텍스트 트래픽을 허용하는 네트워크 보안 설정이 필요하지 않습니다.


## 광고 추가하기

앱에 광고를 노출하기 위해서는 광고유형에 따라 앱 화면에 광고를 추가하는 과정이 필요합니다.<br/>
[AdFit 플랫폼](https://adfit.kakao.com)에서 설정한 광고유형에 따라 아래 문서를 참고하시기 바랍니다.
* [배너 광고 시작하기](BANNERAD.md)
* [네이티브 광고 시작하기](NATIVEAD.md)
* [비즈보드 광고 시작하기](BIZBOARD.md)


## FAQ


### Q1. 광고 영역이 비어보입니다. 버그 아닌가요?

최초 광고를 서버로부터 수신하여 로딩하기 전 까지는 시간이 걸리기 때문에 잠시 비어있을 수 있습니다.
광고를 노출하는 시점에 [AdFit 플랫폼](https://adfit.kakao.com)에서 설정한 크기에 맞춰 영역의 크기가 조절되며,
최초 광고를 수신한 이후에는 새로운 광고를 요청 중이거나 로딩에 실패해도 이전 광고를 계속 보여주게 됩니다.
광고 로딩 완료 또는 로딩 실패할 때에 대한 처리가 필요한 경우, 리스너를 등록하여 각 시점에 맞게 처리하시면 됩니다.

### Q2. 광고 수신이 되지 않을때는 어떻게 하나요?

AdFit은 유효 광고의 100% 노출을 보장하지 않습니다. 유효 광고 노출율은 송출 가능한 광고의 총 수량과 광고 호출수에 따라 달라지게 됩니다.
광고의 총 수량은 한정되어 있으나, 이에 비해 광고의 호출수가 많기 때문에 유효 광고의 수신에 실패하는 경우가 자주 발생할 수 있습니다.
또한 시간대나 앱의 종류, 날짜에 따라서도 노출 가능한 광고의 수가 달라질 수 있습니다.

### Q3. 3.0.0 이후 버전에서 에러 코드는 어떻게 되나요?

광고 수신 리스너의 에러 콜백에서 받을 수 있는 에러 코드는 아래와 같습니다.

code | 발생 상황
-----|--------------
202  | 광고 요청에 실패한 경우 발생하는 에러 (보통 일시적인 네트워크 오류)
301  | 유효하지 않은 광고 응답을 수신한 경우 발생하는 에러
302  | 보여줄 광고가 없는 경우 발생하는 에러 (NO_AD)
501  | 광고 로딩에 실패한 경우 발생하는 에러
601  | SDK 내부에서 발생한 에러
