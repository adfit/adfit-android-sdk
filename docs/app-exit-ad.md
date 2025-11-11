## 앱 종료 광고 시작하기

앱 종료 광고는 안드로이드 앱에 최적화된 상품으로 앱 이탈 시점까지 효과적으로 광고를 노출합니다.<br/>
앱 전환 광고와 유사한 UI이나, 앱 사용자의 액션(전환, 종료)에 따라 상품이 구분됩니다.

앱 종료 광고를 시작하기 위해 먼저 카카오애드핏 플랫폼에서 자신의 매체를 등록한 후, 앱 종료 광고유형의 광고단위 ID를 발급받아야 합니다.


## AdFit SDK 연동

모든 광고 요청을 위해서는 우선 AdFit SDK 연동이 필요합니다.
해당 내용은 [AdFit 시작하기](GUIDE.md#adfit-시작하기) 내용을 참조 부탁드립니다.


## 앱 종료 광고 요청하기

앱 종료 광고는 `AdFitPopupAdLoader`와 `AdFitPopupAdRequest`를 사용하여 요청할 수 있습니다.


먼저, `Activity`의 `onCreate()` 등 적절한 시점에서 `AdFitPopupAdLoader` 인스턴스를 생성합니다.<br />이 인스턴스는 광고를 요청하고 관리하는 역할을 합니다.

```kotlin
// 팝업 광고를 요청하고 관리하는 AdFitPopupAdLoader를 생성합니다.
val popupAdLoader = AdFitPopupAdLoader.create(context, "발급받은 광고단위 ID")
```

`AdFitPopupAdLoader`가 준비되면, **사용자가 뒤로 가기 버튼을 누르는 등 `Activity`의 `finish()` 메서드가 호출되는 시점**에 `loadAd()` 메서드를 호출하여 광고를 요청합니다.

```kotlin
// 앱 종료 광고 요청 시작
popupAdLoader.loadAd(
    AdFitPopupAdRequest.build(AdFitPopupAd.Type.Exit /* 팝업 광고 타입: 앱 종료 */),
    object : AdFitPopupAdLoader.OnAdLoadListener /* 광고 요청 결과를 전달받기 위한 콜백 인터페이스 */ {

        override fun onAdLoaded(ad: AdFitPopupAd) {
            // TODO: 광고 응답에 대한 처리
            // 이 콜백 내에서 `AdFitPopupAdDialogFragment(ad).show(...)`를 호출하여 광고를 노출합니다.
        }

        override fun onAdLoadError(errorCode: Int) {
            // TODO: 요청 실패에 대한 처리
            // 오류 코드(errorCode)에 따라 분기하여 대응할 수 있습니다.
        }
    }
)
```

### 광고 요청 전 확인 사항

안정적인 광고 요청을 위해, `loadAd()`를 호출하기 전에 아래와 같은 조건들을 확인하는 것이 좋습니다.
* 광고 중복 노출 방지: 이미 화면에 팝업 광고가 표시되고 있는지 확인합니다.
* 화면 방향 확인: 팝업 광고는 세로 모드만 지원하므로, 가로 모드에서는 요청하지 않습니다.
* `Activity` 및 `AdFitPopupAdLoader` 상태 확인: `Activity`가 종료 중(`isFinishing`)이거나 파괴된(`isDestroyed`) 상태에서는 요청하지 않습니다.
* 중복 요청 방지: `isLoading` 프로퍼티를 통해 이미 다른 광고를 로딩 중인지 확인합니다. `AdFitPopupAdLoader`는 한 번에 하나의 광고만 로드할 수 있습니다.
* 광고 요청 정책 확인: `isBlockedByRequestPolicy` 프로퍼티를 통해 빈도 제한 정책 등에 따라 요청이 제한된 상태인지 확인합니다.

### 광고 요청 결과 처리

`loadAd()`는 비동기로 동작하며, 결과는 `AdFitPopupAdLoader.OnAdLoadListener`의 콜백 메서드를 통해 전달됩니다.
* 요청 성공: 광고를 성공적으로 수신하면, `AdFitPopupAd` 객체와 함께 `onAdLoaded()` 콜백이 호출됩니다.
* 요청 실패: 요청에 실패하거나 표시할 광고가 없으면, 오류 코드(`errorCode`)와 함께 `onAdLoadError()` 콜백이 호출됩니다. <br/>
  | Code | 발생 상황                              |
  |------|--------------------------------------|
  | 202  | 네트워크 오류로 광고 요청에 실패한 경우       |
  | 301  | 요청에는 성공했으나 앱 종료 광고 유형에 맞는 소재가 없는 경우 |
  | 302  | 요청에는 성공했으나 노출 가능한 광고가 없는 경우 |
  | 그 외 | 기타 오류로 광고 요청에 실패한 경우           |


## 앱 종료 광고 노출

`loadAd()`를 통해 광고 요청이 성공하면, `OnAdLoadListener`의 `onAdLoaded()` 콜백 함수가 호출됩니다.<br />
이 함수는 파라미터로 `AdFitPopupAd` 객체를 전달받으며, `AdFitPopupAdDialogFragment`에 설정하여 앱 종료 광고를 팝업 형식으로 화면에 표시할 수 있습니다.

```kotlin
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

    // 전달받은 광고(ad)를 AdFitPopupAdDialogFragment를 통해 팝업 형식으로 화면에 노출합니다.
    AdFitPopupAdDialogFragment(ad)
        .show(supportFragmentManager, AdFitPopupAdDialogFragment.TAG)
}
```


## 앱 종료 광고 이벤트 수신

`AdFitPopupAdDialogFragment`에서 발생하는 사용자가 광고를 클릭하거나 팝업을 닫는 등의 상호작용 이벤트를 수신하려면 `Activity`의 `onCreate()`와 같은 적절한 시점에 `FragmentResultListener`를 등록해야 합니다.

```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
    // ...

    /**
     * [AdFitPopupAdDialogFragment]에서 발생하는 사용자 이벤트를 수신하기 위해
     * [FragmentResultListener]를 등록합니다.
     */
    supportFragmentManager.setFragmentResultListener(
        AdFitPopupAdDialogFragment.REQUEST_KEY_POPUP_AD, // 이벤트를 식별하기 위한 고유 키
        this
    ) { _, bundle ->
        when (bundle.getString(AdFitPopupAdDialogFragment.BUNDLE_KEY_EVENT_TYPE)) {
            AdFitPopupAdDialogFragment.EVENT_AD_CLICKED -> {
                // TODO: 광고를 클릭한 경우에 대한 처리
            }

            AdFitPopupAdDialogFragment.EVENT_POPUP_CANCELED -> {
                // TODO: "취소"를 선택하거나 팝업을 취소한 경우에 대한 처리
            }

            AdFitPopupAdDialogFragment.EVENT_EXIT_CONFIRMED -> {
                // TODO: "앱 종료"를 선택한 경우에 대한 처리
                // FIXME: 실제 앱 종료를 위한 처리
            }
        }
    }

    // ...
}

```

### 이벤트 타입 상세 설명
| 이벤트 타입 상수              | 설명                                                                       |
| :------------------------ | :------------------------------------------------------------------------ |
| EVENT_AD_CLICKED          | 사용자가 팝업 내의 광고 소재(이미지, 텍스트 등)를 클릭했을 때 발생합니다.                  |
| EVENT_POPUP_CANCELED      | 사용자가 팝업의 '취소' 버튼을 누르거나 '뒤로 가기' 버튼을 눌러 팝업을 취소했을 때 발생합니다.  |
| EVENT_EXIT_CONFIRMED      | 사용자가 팝업의 '앱 종료' 버튼을 눌러 팝업을 닫았을 때 발생합니다.                       |


## 테스트를 위한 광고 요청 제한 초기화

앱 종료 광고는 사용자의 피로도 감소와 경험 품질 유지를 위해 내부적인 빈도 제한(Frequency Capping)과 같은 요청 제한 정책을 가지고 있습니다.
이 정책이 활성화되면 일정 시간 동안 새로운 광고가 요청되지 않습니다.


개발 및 테스트 단계에서는 이러한 제한 없이 광고 요청 성공/실패 흐름을 반복적으로 확인해야 할 때가 많습니다.
`AdFitPopupAdLoader.clearRequestPoliciesForTest()`는 이러한 요청 제한 정책을 강제로 초기화하여, 즉시 광고를 다시 요청할 수 있는 상태로 만들어 줍니다.

### 사용 예시

아래 코드는 `resetButton`을 클릭했을 때 `clearRequestPoliciesForTest()`를 호출하여 광고 요청 제한을 해제하는 예시입니다.
```kotlin
val resetButton = findViewById<Button>(R.id.resetButton)
resetButton.setOnClickListener {
    /**
     * 빈도 제한 등으로 인해 광고 요청이 불가능한 상태일 때,
     * 테스트를 위해 이러한 제한 정책을 초기화합니다.
     */
    popupAdLoader.clearRequestPoliciesForTest()
}
```

### 주요 특징 및 주의사항

* **테스트용 기능:** 이 메서드는 오직 테스트 목적으로만 제공됩니다. 프로덕션(릴리즈) 환경에서는 동작하지 않도록 설계되어 있습니다.
* 디버그 빌드에서만 동작: `clearRequestPoliciesForTest()`는 앱이 **디버그 빌드(`BuildConfig.DEBUG`가 `true`인 상태)일 때만 작동합니다.**<br />릴리즈 빌드에서는 호출하더라도 아무런 동작을 하지 않습니다.
