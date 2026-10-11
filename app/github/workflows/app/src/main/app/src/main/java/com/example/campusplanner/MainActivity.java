package com.example.campusplanner;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.os.Build;

public class MainActivity extends AppCompatActivity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        webView = new WebView(this);
        WebSettings s = webView.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.addJavascriptInterface(new JsBridge(), "Android");
        webView.loadUrl("file:///android_asset/index.html");
        setContentView(webView);

        // 请求悬浮窗权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            startActivityForResult(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName())), 100);
        }
    }

    // JS调用安卓的桥梁
    class JsBridge {
        @android.webkit.JavascriptInterface
        public void startFloatPet() {
            startService(new Intent(MainActivity.this, FloatWindowService.class));
        }
        @android.webkit.JavascriptInterface
        public void stopFloatPet() {
            stopService(new Intent(MainActivity.this, FloatWindowService.class));
        }
    }

    // 返回键：网页能返回就返回，否则退出
    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) webView.goBack();
        else super.onBackPressed();
    }
}
