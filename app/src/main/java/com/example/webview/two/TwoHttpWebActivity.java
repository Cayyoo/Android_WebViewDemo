package com.example.webview.two;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;

import com.example.webview.R;

public class TwoHttpWebActivity extends Activity {
	private EditText et_url;
	private Button bt_open;
	private WebView webview;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.activity_two_http_web);

		init();

		bt_open.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				String url=et_url.getText().toString();//输入网址
				webview.loadUrl(url);//加载页面


				/**
				 * 如果希望点击链接由自己处理，而不是新开Android的系统browser中响应该链接。
				 * 需要给WebView添加一个事件监听对象（WebViewClient)，并重写shouldOverrideUrlLoading方法.
				 */
				webview.setWebViewClient(new MyWebViewClient());
			}
		});
	}

	private void init(){
		et_url = (EditText) super.findViewById(R.id.et_url);
		bt_open = (Button) super.findViewById(R.id.bt_open);
		webview = (WebView) super.findViewById(R.id.webview);
	}


	private class MyWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);

			return true;
		}
	}

	//设置回退，覆盖Activity类的onKeyDown(int keyCoder,KeyEvent event)方法
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) {
			webview.goBack(); //goBack()表示返回WebView的上一页面

			return true;
		}

		finish();//结束退出程序

		return false;
	}

}
