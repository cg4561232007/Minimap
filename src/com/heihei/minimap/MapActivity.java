package com.heihei.minimap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MapActivity extends Activity{
	
	private static final int POWER_LOW = 0;
	private static final int ACCESS=1;
	private TextView mylocationText;
	private final LocationListener locationListener=new LocationListener() {
		
		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			updateWithNewLocation(null);
		}
		
		@Override
		public void onLocationChanged(Location location) {
			/*
			 * 更新位置信息
			 */
			updateWithNewLocation(location);
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
		}
	};
	/*private Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			Animation anim=(Animation) msg.obj;
			if(anim!=null){
				Log.i(tag, "tupianyinggaixianshi");
			}
			imageview.startAnimation(anim);
		};
	};*/
	
	private TextView mylocationText0;
	private TextView mylocationText1;
	private TextView mylocationText2;
	private Spinner spinner;
	private ArrayAdapter<String> adapter;
	protected Animation myAnimation;
	private LocationManager locationmanager;
	private String provider;
	private SensorManager sensormanager;
	private ImageView imageview;
	private Location location;
	private String tag;
	private SensorEventListener sensorlistener;
	private Animation anim;
	private Canvas canvas;
	private Paint paint;
	protected static boolean flag=true;
	private static final String[] locationStr={"东二楼","西二楼","理科楼","交大电脑城"};
	double lat_dsn[]={34.25078053323789,34.25064037479732,34.25076359052623,34.251056187832106};
	double log_dsn[]={108.98055469464295,108.97725509797573,108.97904495712882,108.98316240240929};
	protected int item_i;
	private int nowWidth;
	private int nowHeight;
	private int height;
	double ltoplat=34.25253927533243;
   	double ltoplog=108.97459965142605;       //左上角
   	double rtoplat=34.2524320118852;
   	double rtoplog=108.98309173781692;       //右上角
   	double rbutlat=34.24332351509415;
   	double rbutlog=108.98308215948921;       //右下角
   	double lbutlat=34.2431274637764;
   	double lbutlog=108.97500428759209;       //左下角
	private int ltopx;
	private int ltopy;
	private int lbutx;
	private int lbuty;
	private double distance_x;
	private double distance_y;
	private Bitmap bitmap;
	private double imagex;
	private double imagey;
	private float locationy;
	private float locationx;
	private float location_xier_x;
	private float location_xier_y;
	private float location_like_x;
	private float location_like_y;
	private float location_donger_x;
	private float location_donger_y;
	private float location_jddn_x;
	private float location_jddn_y;
	private boolean focus=true;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		//去除title    
	    //requestWindowFeature(Window.FEATURE_NO_TITLE);    
	    //去掉Activity上面的状态栏
	    //getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,      
	                 //     WindowManager.LayoutParams. FLAG_FULLSCREEN);   
		super.onCreate(savedInstanceState);
		setContentView(R.layout.second_one);
		/*
		 * 生成spinner
		 */
		distance_x=getDistance(ltoplat, ltoplog, rtoplat, rtoplog);
		distance_y=getDistance(ltoplat, ltoplog, lbutlat, lbutlog);
		initialUI();
		/*
		 * 启动GPS定位服务
		 */
		/*locationmanager=(LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Criteria criteria=new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setPowerRequirement(POWER_LOW);
		provider=locationmanager.getBestProvider(criteria, true);
		location=locationmanager.getLastKnownLocation(provider);
		updateWithNewLocation(location);
		locationmanager.requestLocationUpdates(provider, 1000, 0.0f, locationListener);*/
	}
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
	    // TODO Auto-generated method stub
	    	super.onWindowFocusChanged(hasFocus);
	    	if(focus&&flag){
	    	locationmanager=(LocationManager) getSystemService(Context.LOCATION_SERVICE);
			Criteria criteria=new Criteria();
			criteria.setAccuracy(Criteria.ACCURACY_FINE);
			criteria.setPowerRequirement(POWER_LOW);
			provider=locationmanager.getBestProvider(criteria, true);
			location=locationmanager.getLastKnownLocation(provider);
			paint=new Paint();
	        //设置线条粗细
	        paint.setStrokeWidth(7);
			paint.setStyle(Style.FILL);
			paint.setColor(Color.RED);	
			//装入imageview作为背景
			bitmap = Bitmap.createBitmap(imageview.getWidth(),  
					imageview.getHeight(), Bitmap.Config.ARGB_8888);
			imageview.setDrawingCacheEnabled(true);
			bitmap=imageview.getDrawingCache();
			canvas=new Canvas(bitmap);
			updateWithNewLocation(location);
			
			locationmanager.requestLocationUpdates(provider, 1000, 0.0f, locationListener);
			//地图左上角坐标，相对父的坐标
	        ltopx=imageview.getLeft();
	        ltopy=imageview.getTop();   
	        //地图左下角坐标
	        lbutx=ltopx;
	        lbuty=ltopy+height;      
	   
	        location_xier_x=(float) ((getDistance(lat_dsn[1], ltoplog, lat_dsn[1], log_dsn[1]))/(distance_x)*(double)nowWidth);
		
			location_xier_y=(float) ((getDistance(ltoplat, log_dsn[1], lat_dsn[1], log_dsn[1]))/(distance_y) *(double)height);
			
			Log.i(tag, location_xier_y+"location_xier_yshiduoshao");
			location_like_x=(float) ((getDistance(lat_dsn[2], ltoplog, lat_dsn[2], log_dsn[2]))/(distance_x)*(double)nowWidth);
			location_like_y=(float) ((getDistance(ltoplat, log_dsn[2], lat_dsn[2], log_dsn[2]))/(distance_y) *(double)height);
			location_donger_x=(float) ((getDistance(lat_dsn[0], ltoplog, lat_dsn[0], log_dsn[0]))/(distance_x)*(double)nowWidth);
			location_donger_y=(float) ((getDistance(ltoplat, log_dsn[0], lat_dsn[0], log_dsn[0]))/(distance_y) *(double)height);
			location_jddn_x=(float) ((getDistance(lat_dsn[3], ltoplog, lat_dsn[3], log_dsn[3]))/(distance_x)*(double)nowWidth);
			location_jddn_y=(float) ((getDistance(ltoplat, log_dsn[3], lat_dsn[3], log_dsn[3]))/(distance_y) *(double)height);
			final float points[]={location_xier_x,location_xier_y,location_like_x,location_like_y,location_donger_x,
					location_donger_y,location_jddn_x,location_jddn_y};
			//画图
			paint=new Paint();
			paint.setAntiAlias(true);
	        paint.setColor(Color.GRAY);	
	        //设置线条粗细
	        paint.setStrokeWidth(7);
			paint.setStyle(Style.FILL);
			//装入imageview作为背景
			for(int i=0;i<4;i++){
				canvas.drawCircle(points[2*i], points[2*i+1], 6.0f, paint);
			}				

			imageview.setImageBitmap(bitmap);}
			
	    }
	private void initialUI() {
		
		imageview=(ImageView) findViewById(R.id.jiaoda);	
		DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm); 
        nowWidth = dm.widthPixels; //当前分辨率 宽度
        nowHeight=dm.heightPixels-125;
        height = (int)((float)nowWidth/0.9043 * 1);
        if (height > nowHeight) {
        	height = nowHeight; 
        	imageview.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, height));}

		spinner=(Spinner) findViewById(R.id.spiner);
		adapter=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, locationStr);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		/*
		 * 选择目的地
		 */
		myAnimation=AnimationUtils.loadAnimation(this, R.anim.anim);
		spinner.setOnTouchListener(new Spinner.OnTouchListener() {
			

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				v.startAnimation(myAnimation);
				return false;
			}
		});
		spinner.setOnFocusChangeListener(new Spinner.OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub		
			}
		});
		spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
			
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				item_i=position;
				Log.i(tag, "spinner里面的Item_i:"+item_i);
				
				Toast.makeText(MapActivity.this, "您选择的是"+locationStr[position], Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	@Override
	protected void onResume() {
		super.onResume();
		
	};
	 	@Override  
	    protected void onPause() {  
	        super.onPause();  
 
	    }  
	protected void updateWithNewLocation(Location location) {
		// TODO Auto-generated method stub
		mylocationText0=(TextView)findViewById(R.id.mylocationtext0);
		mylocationText1=(TextView)findViewById(R.id.mylocationtext1);
		mylocationText=(TextView)findViewById(R.id.mylocationtext2);
		mylocationText2=(TextView)findViewById(R.id.mylocationtext3);
		if(focus&&flag){
		if(location!=null){
 
			double log=location.getLongitude();
			double lat=location.getLatitude();
			mylocationText0.setText("您现在处于纬度："+lat+"\n");
			mylocationText1.setText("您现在处于经度："+log+"\n");
			//测试item_i是否得到了spinner的值
			Log.i(tag, "Item_i:"+item_i);
			double distance=getDistance(lat, log, lat_dsn[item_i], log_dsn[item_i]);
			mylocationText.setText("您距离目的地还有："+distance+"米");
			//计算表示人位置的点在图像上的位置
			//李帆的
			double x1=getDistance(lat, log, rtoplat, rtoplog);              //相对于右上角实际距离1
	        double x2=getDistance(lat, log, rbutlat, rbutlog);              //相对于右下角实际距离2
	        double cos=(x1*x1+distance_y*distance_y-x2*x2)/(2*x1*distance_y);        //余弦
	        double sin=Math.sqrt((1-cos*cos));                              //正弦
	        locationy=(float) (cos*x1/distance_y*height+ltopy);
	        locationx=(float) (nowWidth-sin*x1/distance_x*nowWidth+ltopx);
	        //我的
			imagex=(float)(getDistance(lat, ltoplog, lat, log)/(distance_x)*(double)nowWidth);
		    imagey=(float)(getDistance(ltoplat,log, lat, log)/(distance_y) *(double)height); 
		    //画出人的行径
		    //
		    paint.setColor(Color.RED);
		    canvas.drawCircle((float)imagex, (float)imagey, 5.0f, paint);
			imageview.setImageBitmap(bitmap);	   	   
			if(isArrived(distance)){
			/*
			 * 弹出一个对话框选择后加载minimap
			 */
			focus=false;
			flag=false;
			Dialog myDialog=dialog(this);
			myDialog.show();
			}else{
				mylocationText2.setText("您正在接近目的地...");
			}
		    } 
			else{	//加入if判断和两个标志位都为假
				
				mylocationText2.setText("获取信息失败，网络未连接！");				
				
				}
		}	
		
	}

	@Override
	 protected void onDestroy() {
	  // TODO Auto-generated method stub
	  super.onDestroy();
	 }
	private boolean isArrived(double distance) {
		if((distance<50)&&(distance>20)){
			Toast.makeText(MapActivity.this, "您已经离目的地很近了",Toast.LENGTH_SHORT).show();
			return false;
		}if(distance<20){
			mylocationText2.setText("您已经到达目的地，是否进入minimap？");
			return true;
		}else{
			return false;
		}
	}
	private Dialog dialog(Context context){
		AlertDialog.Builder builder=new AlertDialog.Builder(context);
		builder.setTitle("minimap indicates");
		builder.setMessage("您已经到达目的地，是否进入minimap");
		builder.setIcon(R.drawable.ic_launcher);
		builder.setPositiveButton("取消",new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub				
				
				dialog.dismiss();
			}
		});
		builder.setNegativeButton("确定",new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub			
				focus=false;
				flag=false;
				Intent intent=new Intent(MapActivity.this, minimapActivity.class);
				startActivity(intent);
				
			}
		});
		return builder.create();
	}
	private double rad(double d)  
       {  
           return d * Math.PI / 180.0;  
       }  
 
    public double getDistance(double lat1, double log1, double lat2,  
               double log2)  
       {  
           double radLat1 = rad(lat1);  
           double radLat2 = rad(lat2);  
           double a = radLat1 - radLat2;
           double radLog1 = rad(log1);  
           double radLog2 = rad(log2);
           double b = rad(log1) - rad(log2); 
           //距离米
           double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)  
                   + Math.cos(radLat1) * Math.cos(radLat2)  
                   * Math.pow(Math.sin(b / 2), 2)));  
           s = s * 6378.137;  
           s = Math.round(s * 1000);  
           return s ;//计算单位：m 
       }
   

    
	}
  
