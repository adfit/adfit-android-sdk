# AdFit(Ad@m) Android SDK Guide

**Ver 2.4.2**

이 가이드는 Android Application에 모바일 광고를 노출하기 위한 광고 데이터요청과 처리 방법을 설명합니다.

사이트/앱 운영정책에 어긋나는 경우 적립금 지급이 거절 될 수 있으니 유의하시기 바랍니다.

* 문의 고객센터 [https://cs.daum.net/question/256.html](https://cs.daum.net/question/256.html)
* 사이트/앱 운영 정책 [http://adfit.biz.daum.net/html/use.html](http://adfit.biz.daum.net/html/use.html)

이 문서는 Kakao 신디케이션 제휴 당사자에 한해 제공되는 자료로 가이드 라인을 포함한 모든 자료의 지적재산권은 주식회사 카카오가 보유합니다.

Copyright © Kakao Corp. All rights reserved.

---

## AdFit(Ad@m) 광고 삽입 방법

### AdFit(Ad@m) SDK 구성

* AdfitSDK-X.X.X.aar : AdFit(Ad@m) 광고를 삽입해주는 라이브러리
* AdfitSample/src/com/kakao/adfit/publisher/sample/
	- BannerTypeXML1.java : 광고를 xml 로 붙인 샘플
	- BannerTypeXML2.java : 광고 Visible 처리 및 pause, resume 처리 예시 샘플
	- BannerTypeJava.java : 광고를 java 코드로 붙인 샘플

### 1 단계 : 광고단위ID(Client ID) 발급받기
실제 광고를 다운로드 받고, 수익창출을 위해서 http://adfit.biz.daum.net/ 에서 매체 등록 후 광고단위ID(Client ID)를 발급받아야 한다.
아래 URL 을 통해 애플리케이션을 등록할 수 있다.
[http://adfit.biz.daum.net/](http://adfit.biz.daum.net/)

### 2 단계 : Adfit 라이브러리 추가 (Android Studio 기준)
AdFit 라이브러리를 프로젝트 build.gradle에 추가한다.

![](http://t1.daumcdn.net/adfit/image/guide/include_sdk.png)

**build.gradle**

	allprojects {
    	repositories {
        	jcenter()
	        flatDir {
    	        dirs 'libs'
        	}
        
	    }
	}
	
	dependencies {
    	compile(name:'AdfitSDK-2.4.2', ext:'aar')
	}


App에서 Proguard를 사용하고 있다면, 반드시 아래 내용을 추가로 넣어주어야 한다.

```
-keep class com.kakao.adfit.publisher.* { public *; }
```

### 3 단계 : AndroidManifest.xml 설정
- 아래 두 가지 필수 권한을 AndroidManifist.xml 에 추가한다.

	```
	<uses-permission android:name="android.permission.INTERNET" />
	<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
	```
	**최소한의 권한으로 INTERNET, ACCESS_NETWORK_STATE 권한을 설정해야 한다. 필수 권한 미 설정시 정상적 광고 노출 되지 않는다.**

- 광고를 넣을 Activity 에 반드시 android:configChanges=”orientation” 을 설정해준다.
	**AndroidManifest.xml**

		<application
			android:icon="@drawable/icon"
			android:label="@string/appName" >

			<activity
				android:name=".TestAppActivity"
				android:configChanges="orientation|keyboardHidden"
				android:label="@string/appName" >
				<intent-filter>
					<action android:name="android.intent.action.MAIN" />
					<category android:name="android.intent.category.LAUNCHER" />
				</intent-filter>
			</activity>

			<!-- 광고를 노출할 Activity 에 android:configChanges=”orientation”을 반드시 추가해야 한다. -->
			<activity
				android:name=".BannerActivity"
				android:configChanges="orientation|keyboardHidden" />
		</application>

		<!-- 아래 권한을 반드시 추가해야 한다. -->
		<uses-permission android:name="android.permission.INTERNET" />
		<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />

### 4 단계 : Google Play Service SDK 설정
Google Play Store에 App을 개시하는 경우, App 내에 광고가 있다면 [반드시 Google Advertising ID를 사용하도록 규정이 변경](https://play.google.com/about/developer-content-policy.html#ADID)되었다.

이에 따라, SDK에서 Google Play Service SDK를 사용할 수 있는 경우에 한해 Google Advertising ID를 사용할 수 있도록 기능이 추가되었다.

참고로, Google Play Service SDK를 사용하지 않은 앱에 대해서는 _"광고가 Google Advertising ID를 사용하지 않았다"_ 는 이유로 Google Play Store에서 임의로 Reject 당할 수도 있다.

이에 따라, **<span style="color:red">SDK에서 Google Play Service SDK가 없이는 라이브러리를 사용할 수 없도록 변경</span>되었다.**

#### 4-a. Android Studio(Gradle)에서 설정하기

Gradle로 App을 빌드하실 경우, build.gradle을 다음과 같이 수정한다.

```
apply plugin: 'com.android.application'
...

dependencies {
    compile 'com.google.android.gms:play-services-base:+'
	compile 'com.google.android.gms:play-services-ads:+'
}
```

그 다음 Sync Project with Gradle Files를 눌러서 프로젝트를 업데이트 한다.


#### 4-b 기타 환경에서 설정하기
##### 4-b-1. 라이브러리 추가하기

** <android-sdk>/extras/google/google_play_services/libproject/google-play-services_lib/ **에서 라이브러리 프로젝트를 App내의 프로젝트로 복사 후 해당 프로젝트를 연결한다.
 
프로젝트 연결에 대한 보다 자세한 사항은 [Referencing Library Project](http://developer.android.com/tools/projects/projects-cmdline.html#ReferencingLibraryProject) 페이지를 참고하기 바란다.

##### 4-b-2. AndroidManifest.xml에 meta-data 태그 추가

Google Play Service SDK를 사용한다면 반드시 AndroidManifest.xml의 application 태그 아래 meta-data 태그를 추가해줘야 한다.

**AndroidManifest.xml**

		<application
			android:icon="@drawable/icon"
			android:label="@string/appName" >

			<!-- Google Play Service SDK를 사용하는 App에 한해 아래 meta-data 테그를 추가한다. -->
			<!-- (https://developer.android.com/google/play-services/setup.html) -->
			<meta-data android:name="com.google.android.gms.version"
							android:value="@integer/google_play_services_version" />

			...



##### 4-b-3. Proguard 설정
App에서 Proguard를 사용하고 있다면, 반드시 아래 내용을 추가로 넣어주어야 한다.

```
-keep public class com.google.android.gms.common.internal.safeparcel.SafeParcelable {
    public static final *** NULL;
}

-keepnames class * implements android.os.Parcelable
-keepclassmembers class * implements android.os.Parcelable {
  public static final *** CREATOR;
}

-keep @interface android.support.annotation.Keep
-keep @android.support.annotation.Keep class *
-keepclasseswithmembers class * {
  @android.support.annotation.Keep <fields>;
}
-keepclasseswithmembers class * {
  @android.support.annotation.Keep <methods>;
}

-keep @interface com.google.android.gms.common.annotation.KeepName
-keepnames @com.google.android.gms.common.annotation.KeepName class *
-keepclassmembernames class * {
  @com.google.android.gms.common.annotation.KeepName *;
}

-keep @interface com.google.android.gms.common.util.DynamiteApi
-keep public @com.google.android.gms.common.util.DynamiteApi class * {
  public <fields>;
  public <methods>;
}

-dontwarn android.security.NetworkSecurityPolicy
```

위 내용은 AdfitSample 프로젝트에 적용되어 있으니 참고하기 바란다.

또한, Google Play Service SDK와 관련해 보다 자세한 사항은 [Setting Up Google Play Services](https://developers.google.com/android/guides/setup) 링크를 참고하기 바란다.

### 5 단계 : 광고 요청을 위한 UI 구성 및 설정

#### 5-a. Xml 방식
* Layout 의 main.xml 에서 광고가 노출되고자 하는 곳에 AdView 객체를 추가한다.  
* _광고를 노출 가능한 최소크기(320DIP x 50DIP)보다 작게 광고 뷰가 할당되는 경우에는 광고가 노출되지 않을 수 있다._
* 그 이외의 속성 값은 어플리케이션의 특성에 따라 자유롭게 변경 가능하다.

**res/layout/main.xml**

<pre><code>&lt;RelativeLayout
	xmlns:app="http://schemas.android.com/apk/res/[APP_PACKAGENAME]"
	android:layout_width="fill_parent"
	android:layout_height="fill_parent">

	&lt;!-- 광고를 사용하기 위해서는 반드시 광고단위ID를 발급받아 사용해야 한다. -->
	&lt;com.kakao.adfit.publisher.AdView
		android:id="@+id/adview"
		android:visibility="invisible"
		android:layout_width="wrap_content"
		android:layout_height="wrap_content"
		android:layout_alignParentBottom="true"
		clientId=”광고단위ID”
		requestInterval=”60”/>
&lt;/RelativeLayout></code></pre>

위 레이아웃에 설정한 AdView 객체를 Activity 에서 사용하는 방법을 아래 예를 통해 살펴보도록 하자.


현재 5 개의 Listener 를 지원하고 있으며, 자세한 내역은 아래 예제 코드와 Class Reference 를 통해 살펴보도록 하자.

**[YourApplication]Activity.java**

	public class BannerTypeXML1 extends Activity {
		private static final String LOGTAG = "BannerTypeXML1";
		private AdView adView = null;

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.adam_sample_1);
			initAdam();
		}


		@Override
		public void onDestroy() {
			super.onDestroy();  

			if (adView != null) {
				adView.destroy();
				adView = null;
			}
		}  

		private void initAdam() {
			// AdFit(Ad@m) sdk 초기화 시작
			adView = (AdView) findViewById(R.id.adview);

			// 광고 리스너 설정

			// 1. 광고 클릭시 실행할 리스너
			adView.setOnAdClickedListener(new OnAdClickedListener() {  
				@Override
				public void OnAdClicked() {
					Log.i(LOGTAG, "광고를 클릭했습니다.");
				}
			});

			// 2. 광고 내려받기 실패했을 경우에 실행할 리스너
			adView.setOnAdFailedListener(new OnAdFailedListener() {
				@Override
				public void OnAdFailed(AdError error, String message) {
					Log.w(LOGTAG, message);
				}
			});

			// 3. 광고를 정상적으로 내려받았을 경우에 실행할 리스너  
			adView.setOnAdLoadedListener(new OnAdLoadedListener() {
				@Override
				public void OnAdLoaded() {
					Log.i(LOGTAG, "광고가 정상적으로 로딩되었습니다.");
				}  
			});  

			// 4. 광고를 불러올때 실행할 리스너
			adView.setOnAdWillLoadListener(new OnAdWillLoadListener() {
				@Override
				public void OnAdWillLoad(String url) {
					Log.i(LOGTAG, "광고를 불러옵니다. : " + url);
				}
			});


			// 5. 광고를 닫았을때 실행할 리스너
			adView.setOnAdClosedListener(new OnAdClosedListener() {
				@Override
				public void OnAdClosed() {
					Log.i(LOGTAG, "광고를 닫았습니다.");
				}
			});


			// 할당 받은 광고단위ID 설정
			// adView.setClientId(“광고단위ID”);


			// 광고 갱신 주기를 12초로 설정
			// adView.setRequestInterval(12);


			// 광고 영역에 캐시 사용 여부 : 기본 값은 true
			adView.setAdCache(false);

			// Animation 효과 : 기본 값은 AnimationType.NONE
			adView.setAnimationType(AnimationType.FLIP_HORIZONTAL);
			adView.setVisibility(View.VISIBLE);
		}
	}

광고 영역은 웹뷰를 사용하고 있고, 기본적으로 캐시를 사용하고 있다. 만약 캐시를 사용하지 않을 경우에는 위 예제와 같이 `adView.setAdCache(false);` 를 호출해 캐시를 사용하지 않도록 설정할 수 있다. 이 경우에는 기존에 캐시 영역의 데이터를 모두 삭제한다.


AdView 클래스에는 위와 같이 5 개의 리스너를 제공하고 있다.

* AdView.OnAdClickedListener : 광고 클릭할 경우 실행할 리스너
* AdView.OnAdFailedListener : 광고 내려받기 실패할 경우 실행할 리스너
* AdView.OnAdLoadedListener : 광고가 내려받았을 경우 실행할 리스너
* AdView.OnAdWillLoadListener : 광고를 불러오기 전에 실행할 리스너
* AdView.OnAdClosedListener : 광고를 닫을 때 실행할 리스너

위 예제에서는 현재 5 개의 리스너를 설정하고 있지만, 리스너가 필요가 없으면 굳이 설정하지 않아도 된다. 리스너와 관련된 자세한 내역은 클래스 레퍼런스를 통해 살펴보도록 하자.


#### 5-b. Java 방식
광고를 넣고자 하는 view 가 들어 있는 Activity 가 생성될 때 AdView 객체를 생성하고 광고 요청을 위해 광고 View 에 필요한 리스너와 할당 받은 광고단위ID를 설정 한다. XML 레이아웃을 이용해 광고 생성할 때와 거의 동일하다.

<pre><code>
public class BannerTypeJava extends Activity {
	private static final String LOGTAG = "BannerTypeJava";
	private RelativeLayout relativeLayout = null;
	private AdView adView = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		relativeLayout = new RelativeLayout(this);

		// AdFit(Ad@m) 광고 뷰 생성 및 설정
		adView = new AdView(this);

		// 광고 클릭시 실행할 리스너
		adView.setOnAdClickedListener(new OnAdClickedListener() {
			@Override
			public void OnAdClicked() {
				Log.i(LOGTAG, "광고를 클릭했습니다.");
			}
		});

		// 광고 내려받기 실패했을 경우에 실행할 리스너
		adView.setOnAdFailedListener(new OnAdFailedListener() {
			@Override
			public void OnAdFailed(AdError arg0, String arg1) {
				Log.w(LOGTAG, arg1);
			}
		});

		// 광고를 정상적으로 내려받았을 경우에 실행할 리스너
		adView.setOnAdLoadedListener(new OnAdLoadedListener() {

			@Override
			public void OnAdLoaded() {
				Log.i(LOGTAG, "광고가 정상적으로 로딩되었습니다.");
			}
		});

		// 광고를 불러올때 실행할 리스너
		adView.setOnAdWillLoadListener(new OnAdWillLoadListener() {

			@Override
			public void OnAdWillLoad(String arg1) {
				Log.i(LOGTAG, "광고를 불러옵니다. : " + arg1);
			}
		});

		// 광고를 닫았을때 실행할 리스너
		adView.setOnAdClosedListener(new OnAdClosedListener() {

			@Override
			public void OnAdClosed() {
				Log.i(LOGTAG, "광고를 닫았습니다.");
			}
		});

		// 할당 받은 광고단위ID 설정
		adView.setClientId("광고단위ID");

		// 광고 갱신 시간 : 기본 60초
		adView.setRequestInterval(12);

		// Animation 효과 : 기본 값은 AnimationType.NONE
		adView.setAnimationType(AnimationType.FLIP_HORIZONTAL);

		adView.setVisibility(View.VISIBLE);

				// XML상에 android:layout_alignParentBottom="true" 와 같은 역할을 함
				RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
				params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

				// 위에서 만든 레이아웃을 광고 뷰에 적용함.
				adView.setLayoutParams(params);

		setContentView(relativeLayout);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		if (adView != null) {
			adView.destroy();
			adView = null;
		}
	}
}</code></pre>


## 추가정보(FAQ)


### Q1. 광고 수신이 되지 않을때는 어떻게 하나요?

AdFit(Ad@m) 은 유효 광고의 100% 노출을 보장하지 않습니다. 유효 광고 노출율은 송출 가능한 광고의 총 수량과 광고 호출수에 따라 달라지게 됩니다. 광고의 총 수량은 한정되어 있으나, 이에 비해 광고의 호출수가 많기 때문에 유효 광고의 수신에 실패하는 경우가 자주 발생할 수 있습니다. 또한 시간대나 앱의 종류, 날짜에 따라서도 노출 가능한 광고의 수가 달라질 수 있습니다.

### Q2. 인터넷(3G 또는 WIFI)이 연결되지 않을 경우에 어떻게 하나요?

내부적으로 인터넷 연결이 끊기면 광고 송출이 자동으로 중지되고, 연결되면 자동으로 광고 송출을 시작하게 됩니다.

### Q3. 광고 영역이 텅 비어보입니다. 아담 버그 아닌가요?

최초 광고를 내려받기 전 까지는 광고 요청에 시간이 걸리기 때문에 잠시 비어있을 수 있습니다. SDK 2.0 부터는 기본적으로 광고를 감싸고 있는 영역이 View.GONE 상태였다가, 광고가 완전히 내려받은 후에 View.VISIBLE 로 바꾸고 있습니다. 한번 View.VISIBLE 로 바뀐 영역은 광고 내려받기가 실패할 지라도 다시 가리지 않습니다.

어떠한 사유로 광고 내려받기가 실패할 경우에는 AdView 객체의 OnAdFailedListener 리스너를 설정해 필요한 기능을 앱에 맞게 설정하시면 됩니다.

### Q4. 시스템 앱에서 Expandable 광고가 보이지 않습니다. 왜 그런건가요?

키보드 앱과 같은 시스템 앱에서는 기존 광고 영역을 자유롭게 변경하기 어려운 관계로 Expandable(확장형) 광고를 보여주기 어렵습니다. 따라서 시스템 앱에서는 단순 클릭형 배너만 노출됩니다.

### Q5. Build PATH 에 라이브러리가 있는데도 앱 사용시 오류가 납니다.

Android Tools 버전 17 부터는 libs 폴더에 있는 라이브러리는 앱에 자동으로 포함됩니다.[^1]

만약 libs 폴더에 있지 않은 경우에는 해당 라이브러리가 Export 되고 있는지 반드시 확인해야 합니다.

### Q6. 2.3.0 sdk로 변경만 하면 구글 광고 ID 를 sdk 내에서 생성해주는건지 답변부탁드립니다.

SDK 내에서 Google 광고 ID를 생성해주지는 않습니다.

앱에서 Google 광고 ID를 추출할 수 있으면 해당 ID를 사용하는 것 뿐입니다.

앱을 빌드시, 반드시 Google Play Service SDK를 함께 넣어주셔야만 Google 광고 ID를 사용할 수 있습니다.

Google Play Service SDK 설정과 관련해 보다 자세한 사항은 [Setting Up Google Play Services](http://developer.android.com/google/play-services/setup.html) 페이지를 참고하면 될 것 같습니다.

[^1]: http://tools.android.com/recent/dealingwithdependenciesinandroidprojects
[^2]: https://developer.android.com/google/play-services/setup.html?hl=ko
