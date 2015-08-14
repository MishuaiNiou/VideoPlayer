package com.example.vedioplayer;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.domain.VedioItem;
import com.example.utils.Utils;

public class VedioListActivity extends Activity {
	
	private ListView listView;
	private TextView textView;
	private ArrayList<VedioItem> vedioItems;
	private Utils util;
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(vedioItems != null && vedioItems.size()>0){
				textView.setVisibility(View.GONE);
				listView.setAdapter(new VedioListAdapter());
			}else{
				textView.setVisibility(View.VISIBLE);
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		initView();
		getData();
		setListener();
		
	}
	
	
	
	
	private void setListener() {
		// TODO Auto-generated method stub
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				/*VedioItem item = vedioItems.get(position);
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_VIEW);
				intent.setDataAndType(Uri.parse(item.getData()), "video/*");
				startActivity(intent);*/
				
				VedioItem item = vedioItems.get(position);
				Intent intent = new Intent(VedioListActivity.this,VedioPlayActivity.class);
	//			Log.i("--->>", item.getData());
				intent.setData(Uri.parse(item.getData()));
				startActivity(intent);
			}
		});
	}




	class VedioListAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return vedioItems.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View view;
			ViewHolder holder;
			if(convertView!=null){
				view=convertView;
				holder = (ViewHolder) view.getTag();
			}else{
				view = View.inflate(VedioListActivity.this, R.layout.vediolist_item, null);
				holder = new ViewHolder();
				holder.name = (TextView) view.findViewById(R.id.name);
				holder.duration = (TextView) view.findViewById(R.id.duration);
				holder.size = (TextView) view.findViewById(R.id.size);
				
				view.setTag(holder);
			}
			
			
			
			
			VedioItem item =vedioItems.get(position);
			holder.name.setText(item.getName());
			holder.size.setText(Formatter.formatFileSize(VedioListActivity.this, item.getSize()));
			holder.duration.setText(util.stringForTime((int)item.getDuration()));
			
			return view;
		}
		
	}

	
	static class ViewHolder{
		TextView name;
		TextView duration;
		TextView size;
	}
	
	
	private void getData() {
		// TODO Auto-generated method stub
		new Thread(){
			public void run(){
				
				vedioItems = new ArrayList<VedioItem>();
				Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				String[] projection = {
					MediaStore.Video.Media.DISPLAY_NAME,
					MediaStore.Video.Media.DURATION,
					MediaStore.Video.Media.SIZE,
					MediaStore.Video.Media.DATA
				};
				
				Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
				while(cursor.moveToNext()){
					VedioItem item = new VedioItem();
					String name = cursor.getString(0);
					item.setName(name);
					long duration = cursor.getLong(1);
					item.setDuration(duration);
					long size = cursor.getLong(2);
					item.setSize(size);
					String data = cursor.getString(3);
					item.setData(data);
					
					vedioItems.add(item);
					
				}
				cursor.close();
				
				handler.sendEmptyMessage(1);
			};
		}.start();
	}

	private void initView() {
		// TODO Auto-generated method stub
		util = new Utils();
		setContentView(R.layout.activity_vediolist);
		listView= (ListView)this.findViewById(R.id.listView1);
		textView= (TextView)this.findViewById(R.id.textview);
	}
}
