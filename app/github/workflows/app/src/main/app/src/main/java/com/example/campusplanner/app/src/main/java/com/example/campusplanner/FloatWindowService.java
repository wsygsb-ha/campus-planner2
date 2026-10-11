package com.example.campusplanner;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.LinearLayout;

public class FloatWindowService extends Service {
    private WindowManager wm;
    private View floatView;
    private WindowManager.LayoutParams params;

    @Override
    public void onCreate() {
        super.onCreate();
        wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        addFloat();
    }

    private void addFloat() {
        params = new WindowManager.LayoutParams();
        params.type = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
                ? WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
                : WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        params.format = PixelFormat.TRANSLUCENT;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        params.width = LinearLayout.LayoutParams.WRAP_CONTENT;
        params.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        params.x = 100;
        params.y = 400;

        floatView = LayoutInflater.from(this).inflate(R.layout.float_window, null);
        WebView web = floatView.findViewById(R.id.float_web);
        web.getSettings().setJavaScriptEnabled(true);
        web.setBackgroundColor(0);
        web.loadUrl("file:///android_asset/float_pet.html");

        // 拖动悬浮窗
        floatView.setOnTouchListener(new View.OnTouchListener() {
            int sx, sy; float mx, my;
            @Override
            public boolean onTouch(View v, MotionEvent e) {
                if (e.getAction() == MotionEvent.ACTION_DOWN) {
                    sx = params.x; sy = params.y;
                    mx = e.getRawX(); my = e.getRawY();
                    return false;
                } else if (e.getAction() == MotionEvent.ACTION_MOVE) {
                    params.x = sx + (int)(e.getRawX() - mx);
                    params.y = sy + (int)(e.getRawY() - my);
                    wm.updateViewLayout(floatView, params);
                    return true;
                }
                return false;
            }
        });

        wm.addView(floatView, params);
    }

    @Override public IBinder onBind(Intent i) { return null; }

    @Override public void onDestroy() {
        super.onDestroy();
        if (floatView != null) wm.removeView(floatView);
    }
}
