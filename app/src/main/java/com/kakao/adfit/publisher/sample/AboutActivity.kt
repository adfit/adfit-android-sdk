package com.kakao.adfit.publisher.sample

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.kakao.adfit.AdFitSdk

class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activie_about)

        val versionTextView = findViewById<TextView>(R.id.adfitVersionText)
        versionTextView.text = "Publisher SDK v${AdFitSdk.SDK_VERSION}"
    }
}
