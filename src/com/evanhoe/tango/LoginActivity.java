package com.evanhoe.tango;

//import java.io.IOException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.evanhoe.tango.WorkOrderListActivity;
import com.evanhoe.tango.objs.InjectionStation;
import com.evanhoe.tango.objs.User;
import com.evanhoe.tango.objs.UserLoginResult;
import com.evanhoe.tango.dao.UserDAO;
import com.evanhoe.tango.dao.InjectionStationDAO;
import com.evanhoe.tango.objs.WorkOrderDetail;
import com.evanhoe.tango.utils.CommonUtilities;
//import com.evanhoe.tango.net.NetUtils;
import com.evanhoe.tango.utils.CommonUtils;
import com.evanhoe.tango.utils.HttpUtils;
import com.evanhoe.tango.utils.SqlLiteDbHelper;
import com.evanhoe.tango.utils.WebService;



import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import net.hockeyapp.android.CrashManager;
import net.hockeyapp.android.UpdateManager;

public class LoginActivity extends BaseActivity implements OnClickListener
{
    SharedPreferences sharedPreferences;
    int count;
  int checkkstatus=0;
    //private FirebaseAnalytics mFirebaseAnalytics;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.login);
        //setTheme(R.style.AppTheme1);
        setContentView(R.layout.activity_login);
        //mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
     /*
        sharedPreferences = getSharedPreferences(
                "userdata", Context.MODE_PRIVATE);
        String token=sharedPreferences.getString("token","");
     /*   if(token.length()>2){
            Intent i=new Intent(this,DisclaimerActivity.class);
            //startActivity(i);
            startActivityForResult(i, 0);
            finish();
        }*/
        /////////////////////////////////////////////////////////////////////////
        //removed inputType="textPassword" from style and the following change to password text field
        //so that font for the hint text would be correct
        EditText password = (EditText) findViewById(R.id.et_password);
        password.setTransformationMethod(new PasswordTransformationMethod());
        /////////////////////////////////////////////////////////////////////////

        Button loginButton = (Button) findViewById ( R.id.btn_login );
        if ( loginButton != null )
        {
            loginButton.setOnClickListener ( this );
        }

        checkDBexists();
    }

    public void onResume()
    {
        super.onResume();
        count = 0;
        TextView btn=(TextView) findViewById(R.id.txtCaution1);

        EditText usernameEntry = (EditText) findViewById ( R.id.et_username );
        EditText passwordEntry = (EditText) findViewById ( R.id.et_password );
        final CheckBox rememberBox = (CheckBox) findViewById ( R.id.chk_remember );
        sharedPreferences = getSharedPreferences(
                "userdata1", Context.MODE_PRIVATE);
        String username=sharedPreferences.getString("username","");
        String password=sharedPreferences.getString("password","");
        User user = UserDAO.getLastUser(getApplicationContext());
        if (username.length()>2) {
            usernameEntry.setText(username);
            passwordEntry.setText(password);
            rememberBox.setChecked(true);
        }
        else
        {
            usernameEntry.setText("");
            passwordEntry.setText("");
            rememberBox.setChecked(false);
        }
        btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                count++;
                if(count==7){
                    EditText usernameEntry = (EditText) findViewById ( R.id.et_username );
                    String username = usernameEntry.getText().toString();
                    EditText passwordEntry = (EditText) findViewById ( R.id.et_password );
                    String password = passwordEntry.getText().toString();
                    // Toast.makeText(getApplicationContext(),"seven",Toast.LENGTH_LONG).show();
                    sharedPreferences = getSharedPreferences(
                            "url", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("urltype","stage");

                    sharedPreferences.edit();
                    editor.commit();


                   rememberBox.setChecked(false);




            final  RetrieveLoginTask r=new RetrieveLoginTask();


                    r.execute(username,password);
new Handler().postDelayed(new Runnable() {
    @Override
    public void run() {
        if(checkkstatus==0) {
           // Toast.makeText(getApplicationContext(),"Login SERVICE UNAVAILABLE ",Toast.LENGTH_LONG).show();
            Toast toast = CreateToast(getResources().getColor(R.color.message_error), "Login Service Unavailable", Gravity.BOTTOM);
            toast.show();
            r.cancel(true);
            Intent i = new Intent(LoginActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        }
    }
},7000);
                    // new RetrieveLoginTask().execute(username, password);

                }
                if(count>7){
                    count=0;
                }
            }
        });
        checkForCrashes();
        checkForUpdates();
    }

    //hockeyapp method
    private void checkForCrashes() {
        //CrashManager.register(this, "d3ede97ee5a6b1d85a3cd067f7b75062");
        CrashManager.register(this, "599df58d165f44ad9915baf35dd53e55");
    }
    //hockeyapp method
    private void checkForUpdates() {
        // Remove this for store builds!
        //UpdateManager.register(this, "d3ede97ee5a6b1d85a3cd067f7b75062");
        UpdateManager.register(this, "599df58d165f44ad9915baf35dd53e55");
    }

    public void onClick ( View view )
    {
        if ( view.getId() == R.id.btn_login )
        {
            EditText usernameEntry = (EditText) findViewById ( R.id.et_username );
            String username = usernameEntry.getText().toString();
            EditText passwordEntry = (EditText) findViewById ( R.id.et_password );
            String password = passwordEntry.getText().toString();
            // call web service for valid logon
            int status = 0;
            // Needed for a later Sprint
            //boolean networkConnected = NetUtils.isInternetAccessible ( getApplicationContext() );
            //if ( ! networkConnected )
            //{
            //    status = 1;
            //}
            //else
            //{
            //}

            // if valid, go to next class

            sharedPreferences = getSharedPreferences(
                    "url", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("urltype","product");

            sharedPreferences.edit();
            editor.commit();


          //  new RetrieveLoginTask().execute(username, password);
            //User user = WebService.getUser(username, "password");

            final  RetrieveLoginTask r=new RetrieveLoginTask();


            r.execute(username,password);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(checkkstatus==0) {
                      //  Toast.makeText(getApplicationContext(),"Login SERVICE UNAVAILABLE ",Toast.LENGTH_LONG).show();

                        Toast toast = CreateToast(getResources().getColor(R.color.message_error), "Login Service Unavailable", Gravity.BOTTOM);
                        toast.show();
                        r.cancel(true);
                        Intent i = new Intent(LoginActivity.this, LoginActivity.class);
                        startActivity(i);
                        finish();
                    }
                }
            },7000);

        }
    }

    private void checkDBexists() {

        File database = getApplicationContext().getDatabasePath("TG.sqlite");

        if (!database.exists()) {
            // Database does not exist so copy it from assets here
            Log.i("Database", "Not Found");

            SqlLiteDbHelper db = new SqlLiteDbHelper(getApplicationContext());
            try {
                db.CopyDataBaseFromAsset();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                Log.i("Database", "Exception");
                e.printStackTrace();
            }

        } else {
            Log.i("Database", "Found");
        }
    }

    class RetrieveLoginTask extends AsyncTask<Object, Void, Integer>
    {
        boolean error = false;
        ProgressDialog sendReceivPB = null;
        final Integer RESULT_INVALID_LOGIN = 1;
        final Integer RESULT_NEW_VERSION = 2;
        final Integer RESULT_INVALID_SERVICE_UNAVAILABLE = 3;
        final Integer RESULT_INVALID_OFFLINE = 4;

        //protected void onPostExecute(Object[] result)
        @Override
        protected void onPostExecute(Integer result)
        {
            checkkstatus=1;
            // execution of result of Long time consuming operation
            //sendReceivPB.setVisibility(View.GONE);
            sendReceivPB.dismiss();
            if ( result == RESULT_INVALID_LOGIN )
            {
                Toast toast = CreateToast(getResources().getColor(R.color.message_error), "Invalid Login Attempt", Gravity.BOTTOM);
                toast.show();
            }
            else if ( result == RESULT_INVALID_OFFLINE )
            {
                Toast toast = CreateToast(getResources().getColor(R.color.message_error), "No Network Access", Gravity.BOTTOM);
                toast.show();
            }
            else if ( result == RESULT_INVALID_SERVICE_UNAVAILABLE )
            {
                Toast toast = CreateToast(getResources().getColor(R.color.message_error), "Login Service Unavailable", Gravity.BOTTOM);
                toast.show();
            }
            else if ( result == RESULT_NEW_VERSION )
            {
                new AlertDialog.Builder(LoginActivity.this)
                        .setTitle(getApplicationContext().getString(R.string.newVersionTitle))
                        .setMessage(getApplicationContext().getString(R.string.newVersionMessage))
                        .setPositiveButton(getApplicationContext().getString(R.string.ok),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which)
                                    {
                                        DownloadApkAsync asyncTask = new DownloadApkAsync();
                                        asyncTask.execute();

                                        return;
                                    }
                                })
                        .show();
            }
        }

        @Override
        protected void onPreExecute()
        {
            // Things to be done before execution of long running operation. For
            // example showing ProgessDialog

            //sendReceivPB = (ProgressBar)findViewById(R.id.progressBar1);
            //sendReceivPB = new ProgressDialog(LoginActivity.this);
            //sendReceivPB.setMessage("Loading...");
            //sendReceivPB.setMessage(null);

            //sendReceivPB.show();

            sendReceivPB = ProgressDialog.show(LoginActivity.this,null,null);
            sendReceivPB.setContentView(new ProgressBar(LoginActivity.this));
            //sendReceivPB.setVisibility(View.VISIBLE);

        }
        //protected Object[] doInBackground(Object... params) {
        @Override
        protected Integer doInBackground(Object... params) {

            Integer result = null;

            CheckBox rememberBox = (CheckBox) findViewById ( R.id.chk_remember );

            UserLoginResult userLoginResult = CommonUtilities.login(LoginActivity.this.getApplication(), params[0].toString(), params[1].toString(), rememberBox.isChecked());

            if (userLoginResult.getUser()!=null)
            {
                if ( CommonUtilities.isNewVersionAvailable ( getApplicationContext() ) )
                {
                    error = true;
                    result = RESULT_NEW_VERSION;
                }
                else
                {
                    String token=userLoginResult.getToken();
                    if(userLoginResult.isServiceAvailable()==true){

                        CommonUtilities.loadAuthorizedInjectionStations ( getApplicationContext(),token);
                        CommonUtilities.refreshTermicideTypes(getApplicationContext(),token);
                    }
                    //CommonUtilities.getTermicideTypeByName(getApplicationContext(), "Termidor HP");

                    // check if this user is authorized for the currently selected unit
                    String selectedMacAddress = ((TangoApplication) getApplication()).getUnitMacAddress ( );
                    if ( selectedMacAddress != null && ! CommonUtilities.isUnitAuthorized ( getApplicationContext(), selectedMacAddress, userLoginResult.getUser().getPersonID() ) )
                    {
                        ((TangoApplication) getApplication()).setUnitMacAddress ( null );
                    }

                    //final Intent workOrderListIntent = new Intent ( LoginActivity.this, WorkOrderListActivity.class );
                    //	Toast.makeText(getApplicationContext(),userLoginResult.getToken(),Toast.LENGTH_LONG).show();

                    final Intent workOrderListIntent = new Intent ( LoginActivity.this, DisclaimerActivity.class );
                    try
                    {
                        if(isCancelled()){

                        }
                        else {
                            checkkstatus=1;
                            //sendReceivPB.setVisibility(View.GONE);
                            if (rememberBox.isChecked()) {
                                sharedPreferences = getSharedPreferences(
                                        "userdata", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("username", userLoginResult.getUser().getUsername());
                                editor.putString("password", userLoginResult.getUser().getPassword());
                                sharedPreferences.edit();
                                editor.commit();
                                // workOrderListIntent.putExtra("token", token);
                                sharedPreferences = getSharedPreferences(
                                        "userdata", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor1 = sharedPreferences.edit();
                                editor1.putString("token", token);
                                sharedPreferences.edit();
                                editor1.commit();

                                sharedPreferences = getSharedPreferences(
                                        "userdata1", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor2 = sharedPreferences.edit();
                                editor2.putString("username", userLoginResult.getUser().getUsername());
                                editor2.putString("password", userLoginResult.getUser().getPassword());
                                sharedPreferences.edit();
                                editor2.commit();

                                startActivityForResult(workOrderListIntent, 0);
                                //finish();///////////// 1 change
                            } else {
                                sharedPreferences = getSharedPreferences(
                                        "userdata", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();// edit calss to edit the value that saved in the shared pref

                                editor.putString("username", userLoginResult.getUser().getUsername());
                                editor.putString("password", userLoginResult.getUser().getPassword());

                                // editor.putString("username","");      //  This is the problem for crashing the code
                                // editor.putString("password","");
                                sharedPreferences.edit();
                                editor.commit();
                                // workOrderListIntent.putExtra("token", token);
                                sharedPreferences = getSharedPreferences(
                                        "userdata", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor1 = sharedPreferences.edit();
                                editor1.putString("token", token);
                                sharedPreferences.edit();
                                editor1.commit();


                                sharedPreferences = getSharedPreferences(
                                        "userdata1", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor2 = sharedPreferences.edit();
                                editor2.putString("username", "");
                                editor2.putString("password", "");
                                sharedPreferences.edit();
                                editor2.commit();


                                startActivityForResult(workOrderListIntent, 0);
                                //finish();////////////// 1 change

                            }
                        }
                    }
                    catch ( Exception e )
                    {
                        //sendReceivPB.setVisibility(View.GONE);
                        sendReceivPB.dismiss();
                        e.printStackTrace();
                    }
                }
            }
            else
            {
                error = true;

                if(userLoginResult.isOnline() && userLoginResult.isServiceAvailable()){
                    result = RESULT_INVALID_LOGIN;
                }else if(userLoginResult.isOnline()==false && userLoginResult.isServiceAvailable()==false){

                    if(CommonUtilities.isUsernameInLocalDB(LoginActivity.this, params[0].toString())){
                        result = RESULT_INVALID_LOGIN;
                    }else{
                        result = RESULT_INVALID_OFFLINE;
                    }

                }else if(userLoginResult.isOnline() && userLoginResult.isServiceAvailable()==false){

                    if(userLoginResult.isServiceAvailable()==false){
                        result = RESULT_INVALID_SERVICE_UNAVAILABLE;
                    }
                    else{
                        result = RESULT_INVALID_LOGIN;
                    }
                    /*
                    if(CommonUtilities.isUsernameInLocalDB(LoginActivity.this, params[0].toString())){
                        result = RESULT_INVALID_LOGIN;
                    }else{
                        result = RESULT_INVALID_SERVICE_UNAVAILABLE;
                    }*/

                }

            }
            return result;

        }

    }

    private class DownloadApkAsync extends AsyncTask<String, Void, String>
    {
        ProgressDialog apkProgress = null; //this progressdialog show the progress on android screen
        private String apkFilename;

        @Override
        protected void onPreExecute()
        {
            apkProgress = ProgressDialog.show ( LoginActivity.this, null, null );
            apkProgress.setContentView ( new ProgressBar ( LoginActivity.this ) );
        }

        @Override
        protected String doInBackground ( String... params )
        {
            User currentUser = ((TangoApplication) getApplication()).getUser();
            apkFilename = WebService.downloadNewVersion ( currentUser.getUsername(), currentUser.getPassword() );

            return apkFilename;
        }

        @Override
        protected void onPostExecute ( String result )
        {
            if ( apkProgress != null ) apkProgress.dismiss();

            File apkFile = new File ( apkFilename );
            Intent installIntent = new Intent ( Intent.ACTION_VIEW );
            installIntent.setDataAndType ( Uri.fromFile(apkFile), HttpUtils.APK_MIME_TYPE );
            startActivity ( installIntent );
        }
    }
}
