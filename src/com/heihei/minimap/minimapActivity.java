package com.heihei.minimap;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification.Action;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.InputEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.Gallery.LayoutParams;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ViewSwitcher.ViewFactory;

public class minimapActivity extends MapActivity implements OnItemSelectedListener, ViewFactory,Runnable {
	private ImageView imageview;
	private mapInfo mapinfo;
	private LinearLayout layout;
	private int displaywidth;
	private int displayheight;
	private float scalewidth=1;
	private float scaleheight=1;
	private int time=0;
	private ImageView newimageview;
	private EditText editText;
	private ImageSwitcher is;
	@SuppressWarnings("deprecation")
	private Gallery gallery;
	private Integer[] mThumbIds = { R.drawable.ditu1, R.drawable.ditu2,R.drawable.ditu3, R.drawable.ditu4,R.drawable.ditu5, R.drawable.ditu6,R.drawable.ditu7};
	private Bitmap bmp;
	private Integer[] mImageIds = { R.drawable.ditu1, R.drawable.ditu2,R.drawable.ditu3, R.drawable.ditu4,R.drawable.ditu5, R.drawable.ditu6,R.drawable.ditu7};
	private String tag;
	protected LayoutInflater factory;
	private View dialogview;
	private  String extra;
	private View enterView;
	private Vector<Integer> ImageIDs;
	public static String A;
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Intent intent = getIntent();
		super.onCreate(savedInstanceState);
		factory=LayoutInflater.from(this);
		dialogview = factory.inflate(R.layout.my_anim, null); 
		editText=(EditText)dialogview.findViewById(R.id.diaedit01);
		setContentView(R.layout.minimap);
		/*
		 * 图片所占的屏幕分辨率大小
		 */
		DisplayMetrics dm=new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		displaywidth=dm.widthPixels;
		displayheight=dm.heightPixels;
		Bitmap bmp[]=new Bitmap[mImageIds.length];
		for(int i=0;i<mThumbIds.length;i++){
			bmp[i]=BitmapFactory.decodeResource(getResources(), mThumbIds[i]);
		}
		/*
		 * gallery与imageswitcher
		 */
	       ImageIDs=new Vector <Integer>(5,1);
	        ImageIDs.add(R.drawable.ditu1);
	        ImageIDs.add(R.drawable.ditu2);
	        ImageIDs.add(R.drawable.ditu3);
	        ImageIDs.add(R.drawable.ditu4);
	        
		is = (ImageSwitcher) findViewById(R.id.switcher);
		is.setFactory(this);
		gallery = (Gallery) findViewById(R.id.gallery);
		gallery.setAdapter(new ImageAdapter(this));
		gallery.setOnItemSelectedListener(this);
		/*
		 * mapinfo触摸事件
		 */
		is.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				int x=(int)event.getX();
				int y=(int)event.getY();
				try {
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						Canvas canvas=new Canvas();
						Paint paint=new Paint();
						paint.setColor(Color.GREEN);
						paint.setAlpha(255);
						paint.setStyle(Style.FILL);
						canvas.drawCircle(x, y, 1, paint);
						break;
					case MotionEvent.ACTION_UP:
						break;
					case MotionEvent.ACTION_MOVE:
					default:
						break;
					} 
				}catch (Exception e) {
					e.printStackTrace();
				}
				return true;			
			}
		});
	}

	private Dialog dialog(Context context){
		LayoutInflater inflater=LayoutInflater.from(this);
		enterView=inflater.inflate(R.layout.my_anim, null);//引用布局文件
		AlertDialog.Builder builder=new AlertDialog.Builder(context);
		builder.setIcon(R.drawable.ic_launcher);
		builder.setTitle("输入");
		builder.setView(enterView);
		builder.setNegativeButton("确定",new DialogInterface.OnClickListener() {
		
		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			editText=(EditText)enterView.findViewById(R.id.diaedit01);	
			String str=editText.getText().toString().trim();
			if(TextUtils.isEmpty(str)){
				Toast.makeText(minimapActivity.this, "密钥不能为空", 1).show();
			}
			if(str!=null){
				int i=0;
	    		Log.i(tag, i+"str不为零");
	    	}
			if(str.equals("helloman")){
				Toast.makeText(minimapActivity.this, "密码正确！", 3).show();
				Intent intent=new Intent();
				intent.setAction(Intent.ACTION_PICK);
				intent.setType("image/*");
				startActivityForResult(intent, 0);
			}else{
				Toast.makeText(minimapActivity.this, "密码错误！", 3).show();
			}
			dialog.dismiss();
			
		}
	});
	builder.setPositiveButton("取消",new DialogInterface.OnClickListener() {
		
		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			dialog.dismiss();
		}
	});
		return builder.create();
	}
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        createOptionsMenu(menu);
        return true;
    }
    private void createOptionsMenu(Menu menu) {
        menu.setQwertyMode(true);
        menu.add(Menu.NONE, 1, 1, "进入");
        menu.add(Menu.NONE, 2, 2, "退出");
        menu.add(Menu.NONE, 3, 3, "添加（仅第三方）");
        menu.add(Menu.NONE, 4, 4, "下载");

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
		return OptionsMenuChoice(item);
    	
    }
    private boolean OptionsMenuChoice(MenuItem item) {
    	//ImageAdapter imgad=new ImageAdapter(this);   	
    	int i = (int) gallery.getSelectedItemId();
    	/*
    	 * 这里出了问题
    	 */
        switch (item.getItemId()) {
            case 1:
            	Log.i(tag,i+"在1这里吗");
            	//big(item,i);
            	Intent intent=new Intent(minimapActivity.this,mapInfo.class);
            	startActivity(intent);
                Toast.makeText(this, "第三层平面图", 3)
                        .show();
                return true;
            case 2:
            	Log.i(tag,i+"在2这里吗");
            	//small(item,i);
                Toast.makeText(this, "退出", 1)
                        .show();
                return true;
            case 3:
            	Log.i(tag,i+"在3这里吗");
                Toast.makeText(this, "请输入密钥后添加", 3)
                .show();
            	Dialog hehedialog=dialog(this);
            	hehedialog.show();
                return true;
            case 4:
            	Log.i(tag,i+"在4这里吗");
                Toast.makeText(this, "下载地图", 1)
                        .show();
                return true;
        }
        return false;
    }
    
	@Override
	public View makeView() {
		// TODO Auto-generated method stub
		  ImageView i = new ImageView(this);
		  i.setBackgroundColor(0xFF000000);
		  i.setScaleType(ImageView.ScaleType.FIT_CENTER);
		  i.setLayoutParams(new ImageSwitcher.LayoutParams(
		  LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		  return i;
	}
	public class ImageAdapter extends BaseAdapter{
		private Context mContext;
		public ImageAdapter(Context context){
			mContext =context;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mThumbIds.length;
			//return ImageIDs.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}
		
		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@SuppressWarnings("deprecation")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			 ImageView i = new ImageView(mContext);
			 i.setImageResource(mThumbIds[position]);
			 i.setAdjustViewBounds(true);
			 i.setLayoutParams(new Gallery.LayoutParams(
			 LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			 i.setBackgroundResource(R.drawable.tiankong1);
			 return i;
		}
	}
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		//is.setImageResource(ImageIDs.get(position));
		is.setImageResource(mImageIds[position]);
	}
	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		
	}    
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(data!=null){
			Uri uri=data.getData();
			
		}
		//缩略图Bitmap bitmap=data.getParcelableExtra("data");
	};
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
   }


