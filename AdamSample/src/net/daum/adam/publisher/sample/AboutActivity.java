package net.daum.adam.publisher.sample;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import net.daum.adam.publisher.impl.AdCommon;

public class AboutActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);

        TextView version = (TextView) findViewById(R.id.version);
        version.setText("Publisher SDK v" + AdCommon.SDK_VERSION);
    }
}
