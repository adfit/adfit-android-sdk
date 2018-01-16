# AdFit Android SDK Guide

** Ver 3.0.2 **


### 꼭 읽어주세요!

AdFit SDK `v3.0.2` 은 기존 버전(v2.x.x)에 비해 클래스명, 내부 API 등 많은 부분이 변경되었습니다.

반드시 [SDK 연동 가이드](https://github.com/adfit/adfit-android-sdk/wiki)를 참고해 주시기를 부탁드립니다.

### `v3.0.2` 에서 **proguard** 옵션이 변경되었습니다. 업데이트 시 [변경된 옵션](https://github.com/adfit/adfit-android-sdk/wiki/%EC%8B%9C%EC%9E%91%ED%95%98%EA%B8%B0#2-%EB%8B%A8%EA%B3%84--adfit-%EB%9D%BC%EC%9D%B4%EB%B8%8C%EB%9F%AC%EB%A6%AC-%EC%B6%94%EA%B0%80-android-studio-%EA%B8%B0%EC%A4%80)을 반드시 확인해주세요.

### Activity `onPause/onResume/onDestroy` 호출시, BannerAdView 의 `pause/resume/destroy` api 를 반드시 호출하지 않을 경우, 광고 수신에 있어 불이익을 받을 수 있습니다.

---
이 가이드는 Android Application에 모바일 광고를 노출하기 위한 광고 데이터요청과 처리 방법을 설명합니다.

사이트/앱 운영정책에 어긋나는 경우 적립금 지급이 거절 될 수 있으니 유의하시기 바랍니다.

* 문의 고객센터 [https://cs.daum.net/question/1440.html](https://cs.daum.net/question/1440.html)
* 사이트/앱 운영 정책 [http://adfit.biz.daum.net/html/use.html](http://adfit.biz.daum.net/html/use.html)

이 문서는 Kakao 신디케이션 제휴 당사자에 한해 제공되는 자료로 가이드 라인을 포함한 모든 자료의 지적재산권은 주식회사 카카오가 보유합니다.

Copyright © Kakao Corp. All rights reserved.

---

