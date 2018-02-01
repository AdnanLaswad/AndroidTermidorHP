package com.evanhoe.tango;

import com.evanhoe.tango.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by jakubpiasecki on 28/10/14.
 */
public class OptionsMenuActivity extends BaseActivity {


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.actionSettings) {
            startActivity(new Intent(this, SettingsActivity.class));
        }
        if (id == R.id.actionWorkOrderList) {
            startActivity(new Intent(this, WorkOrderListActivity.class));           
        }
        if (id == R.id.actionSupport) {
            startActivity(new Intent(this, SupportActivity.class));           
        }
        if (id == R.id.actionLogOut) {

            // workOrderListIntent.putExtra("token", token);
      SharedPreferences      sharedPreferences1 = getSharedPreferences(
                    "userdata", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor1 = sharedPreferences1.edit();
            editor1.putString("username","");
            editor1.putString("password","");
            editor1.putString("token","");
            sharedPreferences1.edit();
            editor1.commit();


          SharedPreferences  sharedPreferences = getSharedPreferences(
                    "url", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("urltype","product");

            sharedPreferences.edit();
            editor.commit();

         /*   SharedPreferences      sharedPreferences = getSharedPreferences(
                    "userdata1", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("username","");
            editor.putString("password","");

            sharedPreferences.edit();
            editor.commit();*/
        	Intent intent = new Intent(this, LoginActivity.class);
        	intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Removes other Activities from stack
        	startActivity(intent);
            return true;
        }
        if (id == R.id.actionTraining) {
        	boolean isTrainingMode = !(((TangoApplication) this.getApplication()).getIsTrainingMode());
        	((TangoApplication) this.getApplication()).setIsTrainingMode(isTrainingMode);
        	
        	finish();
        	final Intent workOrderListIntent = new Intent ( this, WorkOrderListActivity.class );
        	startActivityForResult ( workOrderListIntent, 0 );
        	
        	if(isTrainingMode){
        		getActionBar().setTitle(R.string.training_work_order_list);
        	}else{
        		getActionBar().setTitle(R.string.work_order_list);
        	}
        	
        	
        }
        return super.onOptionsItemSelected(item);
    }
}
