package com.heihei.minimap;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
	protected static final String TAG = "MainActivity";
	private EditText etname;
	private EditText etkey;
	private CheckBox ckbox;
	private Button bt;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		etname=(EditText) findViewById(R.id.et_name);
		etkey=(EditText) findViewById(R.id.et_key);
		ckbox=(CheckBox) findViewById(R.id.ckbox);
		bt=(Button)this.findViewById(R.id.bt);
		bt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String logname=etname.getText().toString().trim();
				String logkey=etkey.getText().toString().trim();
				if(TextUtils.isEmpty(logname)||TextUtils.isEmpty(logkey)){
					Toast.makeText(MainActivity.this, "�û��������벻��Ϊ��", Toast.LENGTH_LONG).show();
				}else{
					if(ckbox.isChecked()){
						//��������
						Log.i(TAG, "�����û���");
					}
					//�������ݣ�������Ϣ��������
					//������������Ϣ
					if("admin".equals(logname)&&("admin".equals(logkey))){
						Toast.makeText(MainActivity.this, "��½�ɹ�", Toast.LENGTH_SHORT).show();
						Intent intent=new Intent(MainActivity.this,MapActivity.class);
						startActivity(intent);
					}else{
						Toast.makeText(MainActivity.this, "�û������������", Toast.LENGTH_SHORT).show();
					}
				}
				
			}
		});
	}

	
}