package com.example.webview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.webview.four.FourActivity;
import com.example.webview.one.One01Activity;
import com.example.webview.three.ThreeActivity;
import com.example.webview.two.TwoActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * WebView一些基本运用：
 * 1、加载HTTP URL
 * 2、加载本地HTML
 * 3、嵌套在ScrollView中
 * 4、与JS页面交互
 * 5、WebView保存Cookie
 */
public class MainActivity extends Activity {
    @Bind(R.id.btn_01)
    protected Button btn_01;
    @Bind(R.id.btn_02)
    protected Button btn_02;
    @Bind(R.id.btn_03)
    protected Button btn_03;
    @Bind(R.id.btn_04)
    protected Button btn_04;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_01,R.id.btn_02,R.id.btn_03,R.id.btn_04})
    public void onClickEvent(View v){
        switch (v.getId()){
            case R.id.btn_01:
                startActivity(new Intent(this, One01Activity.class));
                break;
            case R.id.btn_02:
                startActivity(new Intent(this, TwoActivity.class));
                break;
            case R.id.btn_03:
                startActivity(new Intent(this, ThreeActivity.class));
                break;
            case R.id.btn_04:
                startActivity(new Intent(this, FourActivity.class));
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

}
