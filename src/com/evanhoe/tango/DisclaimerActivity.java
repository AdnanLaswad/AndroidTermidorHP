package com.evanhoe.tango;

import com.evanhoe.tango.dao.UserDAO;
import com.evanhoe.tango.objs.User;
import com.evanhoe.tango.objs.UserLoginResult;
import com.evanhoe.tango.utils.CommonUtilities;
import com.evanhoe.tango.utils.GetLocation;
import com.evanhoe.tango.utils.WebService;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class DisclaimerActivity extends Activity
{
	String token;
	int clickCount;
	String username="";
	String password="";
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate ( Bundle savedInstanceState )
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disclaimer);
        token=getIntent().getStringExtra("token");
        TextView tvVersion = (TextView) findViewById(R.id.tvVersion);
        tvVersion.setText("Version");
        try {
            if(CommonUtilities.getEnv() == 1 || CommonUtilities.getEnv() == 2){
            	tvVersion.setText("Version: " + getPackageManager().getPackageInfo(getPackageName(), 0).versionName + " Testing");
            }else{
            	tvVersion.setText("Version: " + getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
            }
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        clickCount = 0;
        TextView btn=(TextView) findViewById(R.id.txtCaution);
        btn.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    //DO you work here
                	clickCount++;
                	if(clickCount==7){
                		
                		
                		new AlertDialog.Builder(DisclaimerActivity.this)               	
                        .setTitle("Clear DB")
                        .setMessage("Do you wish to clear the database?")
                        .setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) 
                                    {
                                    	CommonUtilities.clearDB(TangoApplication.getTangoApplicationContext());
                                    	
                                    	Toast.makeText(TangoApplication.getTangoApplicationContext(),"Database Wiped", 
                                                Toast.LENGTH_SHORT).show();
                                    	
                                    	finish();
                                    }
                                })
                        .setNegativeButton("No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) 
                                {
                                	clickCount = 0;
                                }
                            })
                        .show();
                		
                	}else if(clickCount > 7){
                		clickCount = 0;               		
                	}
                }
            });
    }

    public void continueButton ( View view )
    {
     SharedPreferences sharedPreferences = getSharedPreferences(
                "userdata", Context.MODE_PRIVATE);
        username=sharedPreferences.getString("username","");
        password=sharedPreferences.getString("password","");
        if(username.length()>3&&password.length()>3) {
            User u = UserDAO.getByUsername(getApplicationContext(), username);
            ((TangoApplication) getApplicationContext()).setUser(u);
            // new RetrieveWOListTask().execute();

            final Intent workorderListIntent = new Intent(this, WorkOrderListActivity.class);
            // workorderListIntent.putExtra("token",token);
            startActivity(workorderListIntent);
           // finish();
        }
        else{
            Toast.makeText(getApplicationContext(),"Need login Again",Toast.LENGTH_LONG).show();
            SharedPreferences      sharedPreferences1 = getSharedPreferences(
                    "userdata", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor1 = sharedPreferences1.edit();
            editor1.putString("username","");
            editor1.putString("password","");
            editor1.putString("token","");
            sharedPreferences1.edit();
            editor1.commit();
            final Intent workorderListIntent = new Intent(this, LoginActivity.class);
            // workorderListIntent.putExtra("token",token);
            startActivity(workorderListIntent);
            finish();
        }
    }



    }

/////////////////////////////////////////////////////////////////////////////////////////////////////////

