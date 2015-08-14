package com.example.vedioplayer;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import com.example.utils.Utils;
//import com.nineoldandroids.view.ViewPropertyAnimator;

public class VedioPlayActivity extends Activity {

	protected static final int PROGRESS = 0;
	private VideoView videoView;
	private Uri uri;
	private TextView current_time;
	private SeekBar seekbar;
	private TextView video_duration;
	private Button play_pause;
	private Button switch_player;
	private Utils utils;
	private boolean isplaying = true;
	private boolean isdestory = false;
	
	private LinearLayout control_player;
	private boolean istouched = true;
//	private GestureDetector gestureDetector;
	
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case PROGRESS:
				
				int currentPosition = videoView.getCurrentPosition();
				current_time.setText(utils.stringForTime(currentPosition));
				
				int duration = videoView.getDuration();
				video_duration.setText(utils.stringForTime(duration));
				
				seekbar.setProgress(currentPosition);
				
				if(!isdestory){
					handler.sendEmptyMessageDelayed(PROGRESS, 1000);
				}
				
				break;

			default:
				break;
			}
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vedioplayer);
		videoView=(VideoView)this.findViewById(R.id.vedioview);
		
		initview();
		
	/*	play_pause.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						switch(v.getId()){
						case R.id.play_pause:
							if(isplaying){
								videoView.pause();
								play_pause.setBackgroundResource(R.drawable.play_bg);
							}else{
								videoView.start();
								play_pause.setBackgroundResource(R.drawable.pause_bg);
							}
						}
					}
				});*/
		
		play_pause.setOnClickListener(mClickListener);
		switch_player.setOnClickListener(mClickListener);
		
		utils = new Utils();
		uri = getIntent().getData();
		videoView.setVideoURI(uri);
		Log.i("---->>", uri.toString());
		
		
		seekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				if(fromUser){
					videoView.seekTo(progress);
				}
			}
		});
		
		
		
		videoView.setOnPreparedListener(new OnPreparedListener() {
			
			@Override
			public void onPrepared(MediaPlayer mp) {
				// TODO Auto-generated method stub
				
				videoView.start();
				Toast.makeText(getApplicationContext(), "系统播放器播放成功！", 1).show();
				isplaying = videoView.isPlaying();
				if(isplaying){
					play_pause.setBackgroundResource(R.drawable.pause_bg);
				}else{
					play_pause.setBackgroundResource(R.drawable.play_bg);
				}
				
				seekbar.setMax(videoView.getDuration());
				handler.sendEmptyMessage(PROGRESS);
				
			}
		});
		
		videoView.setOnCompletionListener(new OnCompletionListener() {
			
			@Override
			public void onCompletion(MediaPlayer mp) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "播放完成", 1).show();
				finish();
			}
		});
		
		videoView.setOnErrorListener(new OnErrorListener() {
			
			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				// TODO Auto-generated method stub
//				Toast.makeText(getApplicationContext(), "播放出错", 1).show();
				startVitamioPlayActivity();
				return true;
			}
		});
		
	//	videoView.setMediaController(new MediaController(this));
		
//		gestureDetector = new GestureDetector(this, new MyOnGestureListener());
	
	}
	
	private OnClickListener mClickListener = new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.switch_player:
				startVitamioPlayActivity();
				break;
				
			case R.id.play_pause:
				if(isplaying){
					videoView.pause();
					play_pause.setBackgroundResource(R.drawable.play_bg);
				}else{
					videoView.start();
					play_pause.setBackgroundResource(R.drawable.pause_bg);
				}
				
				isplaying = ! isplaying;
				break;

			default:
				break;
			}
		}

		
		
	};

	private void initview() {
		current_time = (TextView)this.findViewById(R.id.current_time);
		seekbar = (SeekBar)this.findViewById(R.id.seekbar);
		video_duration = (TextView)this.findViewById(R.id.video_duration);
		play_pause = (Button)this.findViewById(R.id.play_pause);
		switch_player = (Button)this.findViewById(R.id.switch_player);
		control_player = (LinearLayout)findViewById(R.id.control_player);
	}
	
	private void startVitamioPlayActivity() {
		Intent intent = new Intent(VedioPlayActivity.this,VitamioVideoPlayActivity.class);
		intent.setData(uri);
		startActivity(intent);
		finish();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		isdestory = true;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		
		if(istouched){
			control_player.setVisibility(View.VISIBLE);
		}else{
			control_player.setVisibility(View.INVISIBLE);
		}
		istouched = !istouched;
		
		return super.onTouchEvent(event);
	}
	
/*	class MyOnGestureListener extends SimpleOnGestureListener{
		@Override
		public boolean onSingleTapConfirmed(MotionEvent e) {
			// TODO Auto-generated method stub
			if(istouched){
				ViewPropertyAnimator.animate(control_player).translationY(0).setDuration(200);
			}else{
				ViewPropertyAnimator.animate(control_player).translationY(control_player.getHeight()).setDuration(200);
			}
			istouched = !istouched;
			return super.onSingleTapConfirmed(e);
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		gestureDetector.onTouchEvent(event);
		
		return super.onTouchEvent(event);
	}*/

}
