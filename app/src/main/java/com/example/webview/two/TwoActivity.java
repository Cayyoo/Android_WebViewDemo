package com.example.webview.two;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.example.webview.R;

public class TwoActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_two);

		findViewById(R.id.btn_local).setOnClickListener(this);
		findViewById(R.id.btn_web).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_local:
				startActivity(new Intent(this, TwoLocalWebActivity.class) );
				break;
			case R.id.btn_web:
				startActivity(new Intent(this, TwoHttpWebActivity.class) );
				break;
			default:
				break;
		}
	}

}
