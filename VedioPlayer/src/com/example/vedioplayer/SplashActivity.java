package com.example.vedioplayer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.Window;

public class SplashActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				startVedioList();
			}
		}, 2000);
	}
	
	

	private boolean isStartVedioList = false;
	
	protected void startVedioList() {
		// TODO Auto-generated method stub
		if(!isStartVedioList){
			
			isStartVedioList = true;
			Intent intent = new Intent(SplashActivity.this, VedioListActivity.class);
			startActivity(intent);		
			finish();
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		startVedioList();
		return super.onTouchEvent(event);
	}
}
