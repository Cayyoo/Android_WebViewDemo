package com.example.webview.one;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.webview.R;

public class One01Activity extends Activity {

	private TextView txtTitle;
	private ProgressBar loadingProgress;
	private WebView webview;

	private String mytitle;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题
		setContentView(R.layout.activity_one_01);

		txtTitle = (TextView) findViewById(R.id.txtTitle);
		webview = (WebView) findViewById(R.id.wv);
		loadingProgress = (ProgressBar) findViewById(R.id.loadingProgress);// 此控件为一个进度条控件，属于自己添加的控件

		// 设置WebView属性，能够执行Javascript脚本
		webview.getSettings().setJavaScriptEnabled(true);
		// 加载需要显示的网页
		webview.loadUrl("http://www.baidu.com");

		// 此处能拦截超链接的url,即拦截href请求的内容.
		webview.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onReceivedTitle(WebView view, String title) {
				mytitle = title;

				super.onReceivedTitle(view, title);
			}

			@Override
			public void onProgressChanged(WebView view, int progress) {
				txtTitle.setText("");
				loadingProgress.setVisibility(View.VISIBLE);
				loadingProgress.setProgress(progress * 100);

				if (progress == 100) {
					loadingProgress.setVisibility(View.GONE);
					txtTitle.setText(mytitle);
				}

				super.onProgressChanged(view, progress);
			}

		});

		/**
		 * 如果希望点击链接由自己处理，而不是新开Android的系统browser中响应该链接。
		 * 需要给WebView添加一个事件监听对象（WebViewClient)，并重写shouldOverrideUrlLoading方法.
		 */
		webview.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);

				return true;
			}
		});

	}

	//给布局中的"跳转"按钮设置方法
	public void open(View v){//注意：必须有参
		startActivity(new Intent(One01Activity.this,One02Activity.class));
	}

	//在布局中给"关闭"按钮设置的方法
	public void close(View v) {//注意：必须有参
		One01Activity.this.finish();
	}

	//设置回退，覆盖Activity类的onKeyDown(int keyCoder,KeyEvent event)方法
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) {
			webview.goBack(); // goBack()表示返回WebView的上一页面

			return true;
		}

		Toast.makeText(One01Activity.this, "正在退出", Toast.LENGTH_LONG).show();//最后一步退出时给出提示

		return super.onKeyDown(keyCode, event);
	}

}
