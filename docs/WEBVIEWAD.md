# WebView 내에서 광고 시작하기

모바일 앱의 `WebView`로 구성된 웹 페이지 내에서도 AdFit 광고를 송출할 수 있습니다.

## AdFit SDK 연동

AdFit 광고 송출을 위해서는 우선 AdFit Android SDK 연동이 필요합니다.  
해당 내용은 [AdFit 시작하기](GUIDE.md#adfit-시작하기) 내용을 참조하시기 바랍니다.

# WebView 초기화하기

광고를 송출을 위해 `WebView`를 AdFit SDK에 등록하는 과정이 필요합니다.
광고를 송출하려는 `WebView`를 Main Thread 상에서 아래와 같은 설정과 함께
`AdFitSdk.register(webView: WebView)`를 호출하여 광고 송출에 필요한 설정을 합니다.

```kotlin
import android.webkit.CookieManager
import android.webkit.WebView
import com.kakao.adfit.AdFitSdk

class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        webView = findViewById(R.id.webview)
        initializeWebView(webView)
    }

    @MainThread
    private fun initializeWebView(webView: WebView) {
        // 3자 쿠키 사용 설정 허용
        CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true)

        // JavaScript 사용 설정
        webView.getSettings().javaScriptEnabled = true

        // Local Storage 사용 설정
        webView.getSettings().domStorageEnabled = true

        // 비디오 자동 재생 허용 설정
        webView.getSettings().mediaPlaybackRequiresUserGesture = false

        // AdFit SDK에 WebView 등록
        AdFitSdk.register(webView)
    }
}
```

# 광고 송출하기

설정을 마친 `WebView`의 웹 페이지 내에서 AdFit 광고를 송출할 수 있습니다.  
웹 페이지 내에서 아래와 같이 AdFit Web SDK를 사용하는 스크립트를 추가하여 AdFit 광고를 송출합니다.

```javascript
<ins class="kakao_ad_area" style="display:none;" data-ad-unit="광고단위 ID"></ins>
<script type="text/javascript" src="//t1.daumcdn.net/kas/static/ba.min.js" async></script>
```

자세한 사항은 AdFit Web SDK 가이드 문서를 참고 바랍니다.  
- 가이드: https://github.com/adfit/adfit-web-sdk

# 비디오 광고 자동 재생 설정하기

앱의 설정에 따라 AdFit을 통해 송출되는 비디오 광고의 자동 재생 여부를 아래와 같이 설정할 수 있습니다.  
설정하지 않을 경우, 기본 값은 `AdFitVideoAutoPlayPolicy.WIFI_ONLY` (WiFi 연결 상태에서만 자동 재생)입니다.

```kotlin
// 자동 재생하지 않음
AdFitSdk.mediaAutoPlayPolicy = AdFitVideoAutoPlayPolicy.NONE

// WiFi 연결 상태에서만 자동 재생
AdFitSdk.mediaAutoPlayPolicy = AdFitVideoAutoPlayPolicy.WIFI_ONLY

// 항상 자동 재생
AdFitSdk.mediaAutoPlayPolicy = AdFitVideoAutoPlayPolicy.ALWAYS
```
