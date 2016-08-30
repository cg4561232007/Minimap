package com.heihei.minimap;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.R.string;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class mapInfo extends Activity{
	private static final int POWER_LOW = 0;
	public int WIDTH;
	public int HEIGHT=0;
	public int []buff1;
	public int []bitmap1;
	private String tag;
	private Paint paint;
	private MapActivity mapActivity;
	private int[] buff01;
	private ImageView imageview;
	private int displaywidth;
	private int displayheight;
	private Integer[] mThumbIds = { R.drawable.ditu1, R.drawable.ditu2};
	private Bitmap bmp;
	private Integer[] mImageIds ={ R.drawable.ditu1, R.drawable.ditu2};
	double lat[]={34.25064037479732,34.25055508361014,34.25056689347377,34.250535069004805};
	double log[]={108.97725509797573,108.97642455055725,108.97612181752922,108.97562145366143};
	private float scalewidth;
	private float scaleheight;
	double dis[]={0,0,0,0};
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
	private LocationManager locationmanager;
	private String provider;
	private Location location;
	/**
	 * @param context
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mapinfo);
		Intent intent=getIntent();
		imageview=(ImageView) findViewById(R.id.myimageview);
		/*
		 * 也启动定位服务
		 */
		bmp=BitmapFactory.decodeResource(getResources(),R.drawable.ditu1);
		imageview.setImageBitmap(bmp);
		locationmanager=(LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Criteria criteria=new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setPowerRequirement(POWER_LOW);
		provider=locationmanager.getBestProvider(criteria, true);
		location=locationmanager.getLastKnownLocation(provider);
		updateWithNewLocation(location);
		locationmanager.requestLocationUpdates(provider, 1000, 0.0f, locationListener);
		
		
}		
	private void updateWithNewLocation(Location location) {
		// TODO Auto-generated method stub
		if(location!=null){
			double latMy=location.getLatitude();
			double logMy=location.getLongitude();
			nearBy(lat, log, latMy, logMy);
		}
	}
	public void nearBy(double lat[],double log[],double x,double y){
		
		for(int i=0;i<lat.length;i++){
			dis[i]=getDistance(lat[i], log[i], x, y);			
			char a[]={'A','B','C','D'};
			if(dis[i]<10){
				Toast.makeText(this,"您现在位于楼梯口:"+a[i]+"处！", Toast.LENGTH_SHORT).show();
			}
		}	
	}
	private double getDistance(double lat1, double log1, double lat2, double log2) {
		// TODO Auto-generated method stub
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
	private double rad(double d) {
		// TODO Auto-generated method stub
		return d * Math.PI / 180.0; 
	}
	/*public int[] MyX(String a[]){
		
		int length=a.length/2;
		int pixelx[]=new int[length];
		for(int i=0;i<length;i++){
			
			pixelx[i]=Integer.parseInt(a[i*2]);
		}
		return pixelx;		
	} 
	public int[] MyY(String a[]){
		
		int length=a.length/2;
		int pixely[]=new int[length];
		for(int i=0;i<length;i++){
			
			pixely[i]=Integer.parseInt(a[i*2+1] );
		}
		return pixely;		
	} 
	public String[] StoreXY(ArrayList<String> ar){
		String[] str = (String[])ar.toArray();
		String[] values=str.toString().split(",");
		return values;
		
	}
	public ArrayList<String> getKeyXY(int a[]){
		ArrayList<String> it= new ArrayList<String>();
		for(int x=0;x<WIDTH;x++){
			for(int y=0;y<HEIGHT;y++){
				if(a[WIDTH*y+x]==Color.BLUE){
					String tmp=x+","+y;
					System.out.println(tmp);
					it.add(tmp);
				}
			}
		}
		return it;			
	}*/

}
