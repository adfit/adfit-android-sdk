## 비즈보드 광고 시작하기

비즈보드 광고는 네이티브 광고를 기반으로 AdFit SDK에서는 비즈보드 광고 소재에 적합한 템플릿 레이아웃을 제공하고 있습니다. <br/>
<br/>
비즈보드 광고를 시작하기 위해 먼저 카카오애드핏 플랫폼에서 자신의 매체를 등록한 후, <br/>
네이티브 광고유형의 광고단위 ID를 발급받아야 합니다.


## AdFit SDK 연동

비즈보드 광고 요청을 위해서는 우선 AdFit SDK 연동이 필요합니다.
해당 내용은 [AdFit 시작하기](GUIDE.md#adfit-시작하기) 내용을 참조 부탁드립니다.


## 비즈보드 광고 요청하기

비즈보드 광고는 네이티브 광고를 기반으로 AdFit SDK에서는 비즈보드 광고 소재에 적합한 템플릿 레이아웃을 제공하고 있습니다. <br/>
비즈보드 광고 요청에 대한 내용은 [네이티브 광고 요청하기](NATIVEAD.md#네이티브-광고-요청하기) 내용을 참조 부탁드립니다.

## 비즈보드 광고 템플릿 레이아웃 구성

비즈보드 광고는 서비스에서 직접 광고 레이아웃을 구현할 필요 없이 <br/>
AdFit SDK에서는 비즈보드 광고 소재에 적합한 템플릿 레이아웃으로 'AdFitBizBoardAdTemplateLayout'을 제공하고 있습니다. <br/>
<br/>
사용방법은 아래와 같이 너비는 `match_parent`로 높이는 `wrap_content`로 설정하시면 <br/>
높이를 단말 화면비율에 따라 "1029:222" 비율에 맞춰 조정합니다. <br/>
<br/>
필요에 따라 좌우 여백(`marginHorizontal`)을 추가해서 사용하시면 됩니다. (여백 설정은 필수가 아닙니다.)

```xml
<?xml version="1.0" encoding="utf-8"?>
<com.kakao.adfit.ads.na.AdFitBizBoardAdTemplateLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="wrap_content" />
```

### 배경 색상 조정

비즈보드 광고 템플릿 레이아웃은 기본적으로 `#f3f3f3` 색상 배경으로 처리되어 있습니다. <br/>
배경 색상을 조정하기 위해서는 아래와 같은 설정이 필요합니다. <br/>

```xml
<?xml version="1.0" encoding="utf-8"?>
<com.kakao.adfit.ads.na.AdFitBizBoardAdTemplateLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:adfit_backgroundColor="#f3f3f3" />
```

### 배경 모서리 라운드 처리 조정

비즈보드 광고 템플릿 레이아웃은 기본적으로 `radius="5dp"`로 설정된 둥근 모서리를 가진 배경으로 처리되어 있습니다. <br/>
배경의 모서리 라운드 처리를 조정하기 위해서는 아래와 같은 설정이 필요합니다. <br/>

```xml
<?xml version="1.0" encoding="utf-8"?>
<com.kakao.adfit.ads.na.AdFitBizBoardAdTemplateLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:adfit_backgroundCornerRadius="5dp" />
```

직각 모서리를 원하신다면 아래와 같이 `adfit_backgroundCornerRadius="0dp"`로 처리하시면 됩니다.

```xml
<?xml version="1.0" encoding="utf-8"?>
<com.kakao.adfit.ads.na.AdFitBizBoardAdTemplateLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:adfit_backgroundCornerRadius="0dp" />
```

### 높이 비율 조정하기

비즈보드 광고 템플릿 레이아웃은 기본적으로 단말 화면비율에 따라 높이를 "1029:222" 비율에 맞춰 조정합니다. <br/>
높이 비율을 조정하기 위해서는 아래와 같은 설정이 필요합니다. <br/>

```xml
<?xml version="1.0" encoding="utf-8"?>
<com.kakao.adfit.ads.na.AdFitBizBoardAdTemplateLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:adfit_contentAspectRatio="1029:222" />
```

### 최대 높이 제한하기

비즈보드 광고 템플릿 레이아웃은 기본적으로 최대 높이는 없으며, 단말 화면비율에 따라 조정됩니다. <br/>
비즈보드 광고 영역이 너무 커지는 것을 방지하기 위해 최대 높이 제한하기 위해서는 아래와 같은 설정이 필요합니다. <br/>

```xml
<?xml version="1.0" encoding="utf-8"?>
<com.kakao.adfit.ads.na.AdFitBizBoardAdTemplateLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:adfit_contentMaxHeight="74dp" />
```

## 비즈보드 광고 노출 및 해제

응답 받은 `AdFitNativeAdBinder`를 `AdFitBizBoardAdTemplateLayout`에 적용하여 비즈보드 광고를 노출할 수 있습니다.

```kotlin
val nativeAdBinder: AdFitNativeAdBinder
val bizBoardAdTemplateLayout: AdFitBizBoardAdTemplateLayout

// 광고 노출 및 측정 시작
nativeAdBinder.bind(bizBoardAdTemplateLayout)

// 광고 노출 해제 및 측정 중단
nativeAdBinder.unbind()
```

* `bind()` 호출 시, `AdFitBizBoardAdTemplateLayout`에 광고 소재를 적용합니다.
* `bind()` 호출 시부터 `AdFitBizBoardAdTemplateLayout`의 노출 상태를 측정하며, `unbind()`가 호출되면 측정을 중단합니다.
* `unbind()` 호출은 필수 사항이 아닙니다.
