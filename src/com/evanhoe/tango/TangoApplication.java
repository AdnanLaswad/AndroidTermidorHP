package com.evanhoe.tango;

import com.evanhoe.tango.objs.Unit;
import com.evanhoe.tango.objs.User;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;

public class TangoApplication extends Application
{
	
    private static final String PREFS_NAME = "TangoPrefs";
    private static final String MACADDRESS_PREF_NAME = "ChosenUnitMacAddress";
    private static final String BTNAME_PREF_NAME = "ChosenUnitBtName";

    private static Context context;
    private User user;
    boolean isTrainingMode = false;
    private String unitMacAddress;
    private String unitBtName;
    
    public boolean getIsTrainingMode() {
		return isTrainingMode;
	}
	public void setIsTrainingMode(boolean isTraining) {
		this.isTrainingMode = isTraining;
	}
	@Override
    public void onCreate ( )
    {
        super.onCreate();

        this.context = getApplicationContext();

        // get shared preferences
        SharedPreferences prefs = getSharedPreferences ( PREFS_NAME, 0 );
        // check preferences for macaddress
        String chosenMacAddress = null;
        try
        {
            chosenMacAddress = prefs.getString ( MACADDRESS_PREF_NAME, null );
        }
        catch ( ClassCastException e )
        {
            chosenMacAddress = null;
        }

        // set macaddress for use later
        this.unitMacAddress = chosenMacAddress;
        
     // check preferences for btname
        String chosenBtName = null;
        try
        {
        	chosenBtName = prefs.getString ( BTNAME_PREF_NAME, null );
        }
        catch ( ClassCastException e )
        {
        	chosenBtName = null;
        }

        // set btname for use later
        this.unitBtName = chosenBtName;
    }

    public static Resources getResourcesStatic()
    {
        return context.getResources();
    }

	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}

    /*
     * This method returns the context of the application for use anywhere.
     * This saves the effort of having to retrieve it locally and use
     * it in function calls. Now, just TangoApplication.getTangoApplicationContext()
     * returns the context and we can move on in the function
     */
    public static Context getTangoApplicationContext ( )
    {
        return context;
    }

    // TODO: methods for storing/retrieving the database DAO factory

    // TODO: methods for storing/retrieving the device type and message service
    
    /**
     * Get unitMacAddress.
     *
     * @return unitMacAddress as String.
     */
    public String getUnitMacAddress()
    {
        return unitMacAddress;
    }
    
    /**
     * Set unitMacAddress.
     *
     * @param unitMacAddress the value to set.
     */
    public void setUnitMacAddress ( String unitMacAddress )
    {
        this.unitMacAddress = unitMacAddress;

        // get shared preferences
        SharedPreferences prefs = getSharedPreferences ( PREFS_NAME, MODE_PRIVATE );

        // update mac address in preferences
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString ( MACADDRESS_PREF_NAME, unitMacAddress );
        editor.commit ( );
    }
	public String getUnitBtName() {
		return unitBtName;
	}
	public void setUnitBtName(String unitBtName) {
		this.unitBtName = unitBtName;
		
        // get shared preferences
        SharedPreferences prefs = getSharedPreferences ( PREFS_NAME, MODE_PRIVATE );

        // update mac address in preferences
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString ( BTNAME_PREF_NAME, unitBtName );
        editor.commit ( );
	}

    

}
