package com.example.vedioplayer;

import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.MediaPlayer.OnCompletionListener;
import io.vov.vitamio.MediaPlayer.OnErrorListener;
import io.vov.vitamio.MediaPlayer.OnPreparedListener;
import io.vov.vitamio.widget.VideoView;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.example.utils.Utils;

public class VitamioVideoPlayActivity extends Activity {

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
	
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case PROGRESS:
				
				int currentPosition = (int) videoView.getCurrentPosition();
				current_time.setText(utils.stringForTime(currentPosition));
				int duration = (int) videoView.getDuration();
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
		if (!LibsChecker.checkVitamioLibs(this))
			return;
		setContentView(R.layout.activity_vitamiovideoplayer);
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
		
		utils = new Utils();
		uri = getIntent().getData();
		videoView.setVideoURI(uri);
		
		
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
				Toast.makeText(getApplicationContext(), "万能播放器播放视频成功！", 1).show();
				isplaying = videoView.isPlaying();
				if(isplaying){
					play_pause.setBackgroundResource(R.drawable.pause_bg);
				}else{
					play_pause.setBackgroundResource(R.drawable.play_bg);
				}
				
				seekbar.setMax((int)videoView.getDuration());
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
				Toast.makeText(getApplicationContext(), "播放出错", 1).show();
				return true;
			}
		});
		
	//	videoView.setMediaController(new MediaController(this));
	
	}
	
	private OnClickListener mClickListener = new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.switch_player:
				Intent intent = new Intent(VitamioVideoPlayActivity.this,VedioPlayActivity.class);
				intent.setData(uri);
				startActivity(intent);
				finish();
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
	}
	
	
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		isdestory = true;
	}

}
