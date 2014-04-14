package net.daum.adam.publisher.sample;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import net.daum.adam.publisher.AdView;
import net.daum.adam.publisher.AdView.*;
import net.daum.adam.publisher.impl.AdError;

public class BannerTypeJava extends Activity {
    private static final String LOGTAG = "BannerTypeJava";
    private RelativeLayout relativeLayout = null;
    private AdView adView = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        relativeLayout = new RelativeLayout(this);

        initAdam();

        relativeLayout.addView(adView);

        setContentView(relativeLayout);

        // XML상에 android:layout_alignParentBottom="true" 와 같은 역할을 함
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        // 위에서 만든 레이아웃을 광고 뷰에 적용함.
        adView.setLayoutParams(params);
    }

    public void initAdam() {
        // Ad@m 광고 뷰 생성 및 설정
        adView = new AdView(this);

        // 광고 클릭시 실행할 리스너
        adView.setOnAdClickedListener(new OnAdClickedListener() {
            public void OnAdClicked() {
                Log.i(LOGTAG, "광고를 클릭했습니다.");
            }
        });

        // 광고 내려받기 실패했을 경우에 실행할 리스너
        adView.setOnAdFailedListener(new OnAdFailedListener() {
            public void OnAdFailed(AdError arg0, String arg1) {
                Log.w(LOGTAG, arg1);
            }
        });

        // 광고를 정상적으로 내려받았을 경우에 실행할 리스너
        adView.setOnAdLoadedListener(new OnAdLoadedListener() {
            public void OnAdLoaded() {
                Log.i(LOGTAG, "광고가 정상적으로 로딩되었습니다.");
            }
        });

        // 광고를 불러올때 실행할 리스너
        adView.setOnAdWillLoadListener(new OnAdWillLoadListener() {
            public void OnAdWillLoad(String arg1) {
                Log.i(LOGTAG, "광고를 불러옵니다. : " + arg1);
            }
        });

        // 광고를 닫았을때 실행할 리스너
        adView.setOnAdClosedListener(new OnAdClosedListener() {
            public void OnAdClosed() {
                Log.i(LOGTAG, "광고를 닫았습니다.");
            }
        });

        // 할당 받은 clientId 설정
        adView.setClientId("TestClientId");

        // 광고 갱신 시간 : 기본 60초
        adView.setRequestInterval(12);

        // Animation 효과 : 기본 값은 AnimationType.NONE
        adView.setAnimationType(AnimationType.FLIP_HORIZONTAL);
        adView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if ( adView != null ) {
            adView.destroy();
            adView = null;
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}