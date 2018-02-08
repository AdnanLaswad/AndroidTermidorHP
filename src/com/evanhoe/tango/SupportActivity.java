package com.evanhoe.tango;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import com.evanhoe.tango.R;

public class SupportActivity extends Activity {
	

    int chker=0;
    
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		SharedPreferences sharedPreferences = TangoApplication.getTangoApplicationContext().getSharedPreferences(
				"url", Context.MODE_PRIVATE);
		String selecturl=sharedPreferences.getString("urltype","");
		if(selecturl.contains("stage")){
			setTheme(R.style.AppTheme);
			chker=1;
		}
		else{
			setTheme(R.style.AppTheme1);
		}
		setContentView(R.layout.activity_support);
		Button staging=(Button)findViewById(R.id.stag4);
		if(chker==0){
			staging.setVisibility(View.INVISIBLE);
		}
	
	}

		



}
