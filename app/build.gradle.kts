plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.kakao.adfit.publisher.sample"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.kakao.adfit.publisher.sample"
        minSdk = 21
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    val adfitVersion = "3.23.3"
    val kotlinVersion = "2.0.21"
    val playServiceVersion = "18.0.1"

    // recommended :: AdFit SDK 추가 시 함께 추가되는 의존성으로, 최신 버전 사용을 권장
    implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")
    implementation("com.google.android.gms:play-services-ads-identifier:$playServiceVersion")

    // required :: AdFit SDK
    implementation("com.kakao.adfit:ads-base:$adfitVersion")

    // optional :: 샘플 구현용으로 선택 사항
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
}
