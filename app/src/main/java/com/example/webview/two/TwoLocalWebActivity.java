package com.example.webview.two;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

import com.example.webview.R;

public class TwoLocalWebActivity extends Activity {
    private WebView webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two_local_web);

        webview = (WebView) super.findViewById(R.id.webview);

        webview.getSettings().setJavaScriptEnabled(true);//启用javascript
        webview.getSettings().setBuiltInZoomControls(true);//控制页面缩放
        webview.loadUrl("file:/android_asset/html5/index.html");//读取网页，加载本地html
    }

}
