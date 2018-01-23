package com.evanhoe.tango;

import com.evanhoe.tango.R;
import android.app.Activity;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class BaseActivity extends Activity {

	public Toast CreateToast (int color, String message, int locVertical)
    {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_layout,
                (ViewGroup) findViewById(R.id.toast_layout_root));
        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText(message);
        text.setGravity(Gravity.CENTER);
        layout.setBackgroundColor(color);
        Toast toast = new Toast(getApplicationContext());
        int offset = 0;
        if (locVertical == Gravity.TOP)
        {
        	int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                offset = getResources().getDimensionPixelSize(resourceId) * 2;
            }      
        }
        toast.setGravity(Gravity.FILL_HORIZONTAL | locVertical | Gravity.CENTER_HORIZONTAL | Gravity.CENTER, 0, offset);
        toast.setDuration(Toast.LENGTH_LONG);
        
        toast.setView(layout);
        return toast;
    }
}
