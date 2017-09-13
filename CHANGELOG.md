# 변경이력

## v2.4.4
* 버그 픽스.

## v2.4.3
* 안정화.

## v2.4.2
* 버그 픽스.

## v2.4.1
* 버그 픽스.
* 전면형 샘플 제거.

## v2.4.0
* in App Browser 추가. 
* https 지원.
* aar 지원.
* 기타 버그 픽스.

## v2.3.7
* Thread 이슈 수정

## v2.3.6
* 전면형 광고가 가운데로 재배치되지 않는 이슈 수정

## v2.3.5

### Change
* Apache HttpClient 코드를 HttpURLConnection Wrapper 유틸로 교체

## v2.3.4

### Bugfix
* 전면형 광고 상품 노출 기능 개선

### Change
* Google Play Service SDK를 반드시 넣도록 변경
* OS 지원을 기존 Android OS 2.1(Level 7)에서 2.3(Level 9)로 변경

## v2.3.3

### Bugfix

* Google Advertising ID 추출 로직 개선

## v2.3.2

### Bugfix

* 일부 앱에서 ConnectException/IllegalStateException가 발생하는 오류를 수정

## v2.3.1

### Bugfix

* 일부 앱에서 TransactionTooLargeException 가 발생하는 오류를 수정

## v2.3.0

### New

* IDE에서 Layout 작업시에 광고 영역을 노출함(isInEditMode 지원)
* 처리할 수 없는 URL 중에 intent scheme인 경우에는 마켓으로 이동함.
* Google Advertising ID 추출 로직 추가.

### Change

* 기본 배너 크기를 320x48에서 320x50으로 변경.
* 프로젝트 빌드 스크립트 수정(Google Play Service SDK를 Dependency에 추가)

### Bugfix

* 가이드 앱의 빌드 SDK 버전을 8에서 7로 낮춤.
* Sample 앱 레이아웃 일부 수정.
* 테스트 앱의 빌드 SDK 범위를 8에서 7로 변경.

## v2.2.3.3
* UDID 키 생성 로직 개선
* 일부 기기에서 광고를 제대로 받아오지 못하는 버그 수정

## v2.2.3
* 광고 효율 관련 로직 개선
* 필수 권한 대상에서 android.permission.WRITE_EXTERNAL_STORAGE 제외

## v2.2.1
* 앱에서 광고 업데이트 시에 간헐적으로 발생하는 NullPointerException 문제 수정
* 광고 레이아웃 높이와 관련된 기능 수정 및 관련 API 추가
 * setAdUnitSize
* Android SDK 2.1(Level 7) 지원

## v2.2
* 리치미디어 노출 관련 추가 기능 탑재
* 필수 권한으로 android.permission.WRITE_EXTERNAL_STORAGE 추가
* Android SDK 지원 레벨을 2.1(Level 7)에서 2.2(Level 8)로 변경
* AdInterstitial 사용시 가이드 수정

## v2.1.2

* 간헐적으로 발생하는 NullPointerException 오류 수정

## v2.1.1

* 특정 Expandable 소재 노출 시 앱이 멈추는 문제점 수정
* 미니사이트에서 닫기 버튼이 간헐적으로 동작하지 않는 현상 수정

## v2.1

* 앱 사용자의 성별 및 생일 정보 설정을 위한 AdInfoClass 추가
* 광고 전환 애니메이션 AnimationType.FADE, AnimationType.SLIDE 추가
* 광고 요청시에 광고 영역이 보이지 않으면 명시적으로 adFailed() 호출
* 앱 바로가기 사용시에 Interstitial 에서 발생하는 문제점 수정
* WebView 에서 Scroll 시에 발생하는 OutOfMemoryError 수정
* 추가된 API
	* AdInfo Class
		* `setGender(String gender)`
		* `String getGender()`
		* `void setBirth(String date)`
		* `String getBirth()`
	* AdView Class
		* `void setAdInfo(AdInfo info)`
		* `AdInfo getAdInfo()`
	* AdInterstitial Class
		* `void setAdInfo(AdInfo info)`
		* `AdInfo getAdInfo()`

## v2.0.4

* 최초 광고 뷰가 INVISIBLE일 때 광고가 내려오지 않는 부분 수정
* 광고를 불러오는 과정에서 발생하는 ANR 문제 수정
* 전면형 광고의 닫기 버튼에 대한 리스너 추가
	* AdInterstitial Class
		* `void setOnAdClosedListener(AdView.OnAdClosedListener listener)`

## v2.0.3

* 화면에서 뷰가 해제될 때, 일부 BroadcastReceiver leak 문제 해결


## v2.0.2

* 포커스 이동 버그 수정
* 전면형 광고 노출시 닫기 버튼 위치 수정
* 광고 영역 크기가 320DP x 48DP 보다 작을 때 광고 갱신 중단

## v2.0.1

* 광고 무한 갱신 및 깜빡임 버그 수정
* WIFI에서 3G로 네트워크 전환시 광고 수신 중단되는 버그 수정
* WebView 에서 발생하는 Memory Leak 수정
* 추가된 API
	* `void setThreadPriority()`
	* `int getThreadPriority(int priority)`


## v2.0

* ACCESS_NETWORK_STATE 권한을 필수 권한 추가
* 전면형 광고(AdInterstitial class) 추가
* 광고 사용방법(API) 변경
* 일부 클래스 변경 및 제거
	* MobileAdView class 를 AdView class 로 변경하고 API 수정
	* AdHttpListener class 와 AdConfig class 제거

## v1.4.2

* 앱에서 웹뷰 광고 노출시에 간헐적으로 발생하는 에러 수정

## v1.4.1.1


* 앱에서 Proguard 적용시 SDK 와 관련해 발생하는 에러 수정

## v1.4.1


* 기존 AsyncTask 에서 발생하는 NullPointerException 수정
* 특정 광고 노출시 AdWebView 에서 발생하는 NullPointerException 수정
* 네트워크 상태에 따라 간헐적으로 발생하는 ANR 현상 수정
* 광고 영역을 명시적으로 삭제될 경우, 내부적으로 광고 관련 데이터를 제거함

## v1.4.0.1
* SDK에서 요구하는 필수권한이 있음에도 특정 상황 시 권한 요청 메시지가 출력되는
현상 수정

## v1.4.0
* 다양한 광고 Targeting 기능 추가
* INTERNET 과 ACCESS_WIFI_STATE 권한을 필수 권한으로 변경
* keyboardView 에 광고 노출시 클릭 작동 안되는 현상 수정
* 특정 상황에서 UserAgent 를 조회할 수 없을 때 발생하는 문제점 수정

## v1.3.2
* Android 3.0(honeycomb) 이상 버전에서 Ad@m 클릭스 한글 깨짐 현상 수정

## v1.3.1
* AsyncTask 사용 안함

## v1.3.0
* 광고 삽입 방식을 보다 간편한 방식으로 변경
	* v1.2 이하 방식과 일부 호환되지 않으니 sample 코드를 참고하여 수정 필요
* 다양한 가로 사이즈에서 배너 이미지 변형 없이 광고 노출 지원
* 제거된 함수들
	* MobileAdView class
		* `void setEnabled(Boolean enabled)`
		* `void refreshFreshAd()`
		* `void setAdTextColor(String textcolor)`
		* `String getAdTextColor()`
		* `void setBackgroundColor(String in_backcolor)`
		* `String getBackgroundColor()`
	* AdConfig class
		* `void setGender(String in_gender)`
		* `String getGender()`
		* `void setBirthday(int year, int month, int day)`
		* `void setBirthday(GregorianCalendar in_calendar)`
		* `GregorianCalendar getBirthday()`
		* `void setAllowUseOfLocation(Boolean in_userLocation)`
* 추가된 함수들
	* MobileAdView class
		* `void pause()`
		* `void resume()`
		* `void destroy()`
* 광고갱신방식변경
	* 광고 view 가 화면에 보이지 않는 경우에는 광고 갱신이 자동 중단됨
	* 기존에 setAdListener(null)이나 setEnabled(false) 함수 호출을 통해 광고 갱신을 중단하는 방식은 더 이상 지원되지 않음
	* 수동으로 광고 갱신을 제어하고 싶은 경우에는 pause, resume, destroy 함수를 참조
	* 위치정보 등 수집 항목 제거(location, gender, birthday)
