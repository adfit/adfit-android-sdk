package com.kakao.adfit.publisher.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kakao.adfit.AdfitSdk
import kotlinx.android.synthetic.main.activie_about.*

class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activie_about)
        adfitVersionText.text = "Publisher SDK v${AdfitSdk.SDK_VERSION}"
    }

}