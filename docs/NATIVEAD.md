## 네이티브 광고 시작하기

네이티브 광고를 시작하기 위해 먼저 카카오애드핏 플랫폼에서 자신의 매체를 등록한 후, <br/>
네이티브 광고유형의 광고단위 ID를 발급받아야 합니다.


## AdFit SDK 연동

네이티브 광고 요청을 위해서는 우선 AdFit SDK 연동이 필요합니다.
해당 내용은 [AdFit 시작하기](GUIDE.md#adfit-시작하기) 내용을 참조 부탁드립니다.


## 네이티브 광고 요청하기

네이티브 광고를 요청하기 위해서는 `AdFitNativeAdLoader`와 `AdFitNativeAdRequest`를 통해 가능합니다.

```kotlin
// AdFitNativeAdLoader 생성
val nativeAdLoader: AdFitNativeAdLoader = AdFitNativeAdLoader.create(activity, "발급받은 광고단위 ID")

// ...

// 새로운 네이티브 광고 요청 정보 설정
val request = AdFitNativeAdRequest.Builder()
    .setAdInfoIconPosition(AdFitAdInfoIconPosition.RIGHT_TOP) // 광고 정보 아이콘 위치 설정
    .setVideoAutoPlayPolicy(AdFitVideoAutoPlayPolicy.WIFI_ONLY) // 비디오 광고 자동 재생 정책 설정
    .build()

nativeAdLoader.loadAd(request, object : AdFitNativeAdLoader.AdLoadListener {

    override fun onAdLoaded(binder: AdFitNativeAdBinder) {
        // TODO: 광고 응답에 대한 처리
    }

    override fun onAdLoadError(errorCode: Int) {
        // TODO: 요청 실패에 대한 처리
    }
})
```

* `AdFitNativeAdRequest.Builder`의 `setAdInfoIconPosition()`으로 광고 정보 아이콘 위치를 설정할 수 있습니다.<br/>
  기본값은 `RIGHT_TOP`(오른쪽 상단)입니다.<br/>

  | AdFitAdInfoIconPosition | 위치              |
  |-------------------------|------------------|
  | LEFT_TOP                | 왼쪽 상단          |
  | RIGHT_TOP               | 오른쪽 상단 (기본값) |
  | LEFT_BOTTOM             | 왼쪽 하단          |
  | RIGHT_BOTTOM            | 오른쪽 상단         |

* `AdFitNativeAdRequest.Builder`의 `setVideoAutoPlayPolicy()`로 비디오 광고 자동 재생 정책을 설정할 수 있습니다.<br/>
  기본값은 `WIFI_ONLY`(WiFi 연결 상태에만 자동 재생)입니다.<br/>
  
  | AdFitVideoAutoPlayPolicy | 설정                     |
  |--------------------------|-------------------------|
  | NONE                     | 자동 재생하지 않음          |
  | WIFI_ONLY                | WiFi 연결 상태에만 자동 재생 |
  | ALWAYS                   | 항상 자동 재생             |

* 요청에 성공하여 새로운 광고를 응답 받은 경우, 응답 받은 광고 소재 정보를 `AdFitNativeAdBinder`를 통해 <br/>
  `AdLoadListener.onAdLoaded()`로 전달받을 수 있습니다. <br/>
* 요청에 실패하거나 응답 받은 소재가 없는 경우, 오류 코드(`errorCode: Int`)를 <br/>
  `AdLoadListener.onAdLoadError()`로 전달받을 수 있습니다. <br/>
  | Code | 발생 상황                              |
  |------|--------------------------------------|
  | 202  | 네트워크 오류로 광고 요청에 실패한 경우       |
  | 302  | 요청에는 성공했으나 노출 가능한 광고가 없는 경우 |
  | 그 외 | 기타 오류로 광고 요청에 실패한 경우           |
* `AdFitNativeAdLoader.load()`는 동시에 하나의 요청만 처리할 수 있으며, 이전 요청이 진행 중이면 새로운 호출은 무시됩니다.


## 네이티브 광고 레이아웃 구성

네이티브 광고 레이아웃은 서비스 컨텐츠와 어울리도록 구성되어야 하므로, 서비스에서 직접 광고 레이아웃을 구현하는 과정이 필요합니다.<br/>
네이티브 광고 레이아웃 구성 샘플은 아래와 같습니다.<br/>

<img src="https://t1.daumcdn.net/adfit_sdk/document-assets/ios/native-ad-components3.png" width="640" style="border:1px solid #aaa"/>

| 번호 | 설명                       | UI 클래스                | AdFitNativeAdLayout |
|-----|---------------------------|------------------------|---------------------|
| -   | 광고 영역                   | AdFitNativeAdView      | containerView       |
| 1   | 광고 제목                   | TextView               | titleView           |
| 2   | 행동유도버튼                 | Button                 | callToActionButton  |
| 3   | 광고주 이름 (브랜드명)         | TextView               | profileNameView     |
| 4   | 광고주 아이콘 (브랜드 로고)     | ImageView              | profileIconView     |
| 5   | 미디어 소재 (이미지, 비디오 등)  | AdFitMediaView         | mediaView           |
| 6   | 광고 정보 아이콘              | -                      | -                   |
| 7   | 광고 홍보문구                | TextView               | bodyView            |

- 네이티브 광고는 위의 요소들로 구성됩니다.
- 각 요소들은 위 표를 참고하여 대응하는 UI 클래스를 통해 표시되도록 구현해야 합니다.
- "제목 텍스트"와 "미디어 소재" 요소는 필수입니다.
- "광고 정보 아이콘 이미지"는 "광고 영역" 내에 `AdFitNativeAdRequest`에 설정한 위치에 표시됩니다.
- 사용자가 광고임을 명확히 인지할 수 있도록 "광고", "AD", "Sponsored" 등의 텍스트를 별도로 표시해주셔야 합니다.
- "광고 영역"은 `AdFitNativeAdView`, "미디어 소재"는 `AdFitMediaView`를 사용하셔야 합니다.


## 네이티브 광고 레이아웃을 SDK로 전달

SDK에서는 서비스에서 구현한 레이아웃의 각 요소를 구분할 수 있도록 `AdFitNativeAdLayout`을 통해 광고 레이아웃을 전달받습니다.<br/>
`AdFitNativeAdLayout`은 `AdFitNativeAdLayout.Budiler`를 통해 생성 가능합니다.<br/>

```kotlin
// 광고 SDK에 넘겨줄 [AdFitNativeAdLayout] 정보 구성
val nativeAdLayout: AdFitNativeAdLayout =
    AdFitNativeAdLayout.Builder(nativeAdView.containerView) // 네이티브 광고 영역 (광고 아이콘이 배치 됩니다)
        .setTitleView(nativeAdView.titleTextView) // 광고 제목 (필수)
        .setBodyView(nativeAdView.bodyTextView) // 광고 홍보문구
        .setProfileIconView(nativeAdView.profileIconView) // 광고주 아이콘 (브랜드 로고)
        .setProfileNameView(nativeAdView.profileNameTextView) // 광고주 이름 (브랜드명)
        .setMediaView(nativeAdView.mediaView) // 광고 미디어 소재 (이미지, 비디오) (필수)
        .setCallToActionButton(nativeAdView.callToActionButton) // 행동유도버튼 (알아보기, 바로가기 등)
        .build()
```


### 네이티브 광고 샘플 layout.xml

```xml
<?xml version="1.0" encoding="utf-8"?>
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_gravity="center"
    android:background="@android:color/white">

    <com.kakao.adfit.ads.na.AdFitNativeAdView
        android:id="@+id/containerView"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_gravity="center"
        android:layout_marginLeft="23dp"
        android:layout_marginTop="15dp"
        android:layout_marginRight="23dp"
        android:layout_marginBottom="15dp">

        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:orientation="vertical">

            <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_margin="3dp"
                android:gravity="center_vertical"
                android:orientation="horizontal">

                <ImageView
                    android:id="@+id/profileIconView"
                    android:layout_width="50dp"
                    android:layout_height="50dp"
                    android:contentDescription="AD Profile Icon"
                    tools:src="@mipmap/ic_launcher" />

                <LinearLayout
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:layout_marginLeft="7dp"
                    android:layout_marginRight="7dp"
                    android:gravity="center_vertical"
                    android:orientation="vertical">

                    <TextView
                        android:id="@+id/titleTextView"
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:textStyle="bold"
                        tools:text="AD Title" />

                    <TextView
                        android:id="@+id/profileNameTextView"
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        tools:text="AD Profile Name" />
                </LinearLayout>
            </LinearLayout>

            <com.kakao.adfit.ads.na.AdFitMediaView
                android:id="@+id/mediaView"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_margin="3dp" />

            <TextView
                android:id="@+id/bodyTextView"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_margin="3dp"
                tools:text="AD Description" />

            <Button
                android:id="@+id/callToActionButton"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                tools:text="AD Call To Action Button" />
        </LinearLayout>
    </com.kakao.adfit.ads.na.AdFitNativeAdView>
</FrameLayout>
```


## 네이티브 광고 노출 및 해제

응답 받은 `AdFitNativeAdBinder`와 구성한 `AdFitNativeAdLayout`으로 네이티브 광고를 노출할 수 있습니다.

```kotlin
val nativeAdBinder: AdFitNativeAdBinder
val nativeAdLayout: AdFitNativeAdLayout

// 광고 노출 및 측정 시작
nativeAdBinder.bind(nativeAdLayout)

// 광고 노출 해제 및 측정 중단
nativeAdBinder.unbind()
```

* `bind()` 호출 시, `AdFitNativeAdLayout`에 광고 소재(텍스트, 이미지, 비디오 등의 리소스)를 적용합니다.
* `bind()` 호출 시부터 `AdFitNativeAdLayout`의 노출 상태를 측정하며, `unbind()`가 호출되면 측정을 중단합니다.
* `unbind()` 호출은 필수 사항이 아닙니다.
