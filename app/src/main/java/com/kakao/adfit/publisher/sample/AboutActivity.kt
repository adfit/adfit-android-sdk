package com.kakao.adfit.publisher.sample

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import com.kakao.adfit.AdFitSdk

class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activie_about)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.layout)) { v, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updatePadding(insets.left, insets.top, insets.right, insets.bottom)

            WindowInsetsCompat.CONSUMED
        }

        val versionTextView = findViewById<TextView>(R.id.adfitVersionText)
        versionTextView.text = "Publisher SDK v${AdFitSdk.SDK_VERSION}"
    }
}
