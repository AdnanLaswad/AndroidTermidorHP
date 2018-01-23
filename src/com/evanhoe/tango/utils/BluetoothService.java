package com.evanhoe.tango.utils;

import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Set;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;

import com.evanhoe.tango.objs.Unit;

public class BluetoothService
{
    public static ArrayList<Unit> getPairedDevices ( )
    {
        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();

        Set<BluetoothDevice> pairedDevices = btAdapter.getBondedDevices();
        ArrayList<Unit> unitPairedList = new ArrayList<Unit>();
        for ( BluetoothDevice thisBTDevice : pairedDevices )
        {
            Unit thisBTUnit = new Unit ( thisBTDevice.getAddress(), thisBTDevice.getName(), Unit.UNIT_PAIRED );
            thisBTUnit.setBluetoothDevice ( thisBTDevice );
            unitPairedList.add ( thisBTUnit );
        }

        return unitPairedList;
    }

    public static void searchForDevices ( Activity someActivity, BroadcastReceiver someReceiver )
    {
        //IntentFilter intentFilter = new IntentFilter();
        //intentFilter.addAction ( BluetoothDevice.ACTION_FOUND );
        //intentFilter.addAction ( BluetoothAdapter.ACTION_DISCOVERY_STARTED );
        //intentFilter.addAction ( BluetoothAdapter.ACTION_DISCOVERY_FINISHED );
        //someActivity.registerReceiver ( someReceiver, intentFilter );

        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
        btAdapter.startDiscovery();
    }

    /*
     * This method should be called from an onPause() method call in an Activity so that
     * it can unregister the receiver so we don't get stray stack traces.
     */
    public static void stopSearchForDevices ( Activity someActivity, BroadcastReceiver someReceiver )
    {
        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
        btAdapter.cancelDiscovery();
        // We might be listening to something else, therefore, don't turn off Recevier
        //someActivity.unregisterReceiver ( someReceiver );
    }

    /*
     * This method will attempt to pair to the Bluetooth device with the associated macAddress
     *
     */
    public static boolean pairDevice ( BluetoothDevice btDevice )
    {
        boolean returnBoolean = false;
        Method method;
        try
        {
System.out.print ( "Pairing to device [ " + btDevice.getAddress() + " ]..." );
            method = btDevice.getClass().getMethod("createBond", (Class[]) null);
            Boolean returnValue = (Boolean) method.invoke(btDevice, (Object[]) null);
System.out.println ( returnValue.booleanValue() );
            returnBoolean = returnValue.booleanValue();
        }
        catch ( NoSuchMethodException nsme )
        {
            nsme.printStackTrace ( );
            returnBoolean = false;
        }
        catch ( IllegalAccessException iae )
        {
            iae.printStackTrace ( );
            returnBoolean = false;
        }
        catch ( InvocationTargetException ite )
        {
            ite.printStackTrace ( );
            returnBoolean = false;
        }

        return returnBoolean;
    }

    /*
     * This method will attempt to unpair from the Bluetooth device with the associated macAddress
     *
     */
    public static boolean unpairDevice ( BluetoothDevice btDevice )
    {
        boolean returnBoolean = false;

        //Class btClass = Class.forName("android.bluetooth.BluetoothDevice");
        //Method removeBondMethod = btClass.getMethod("removeBond");
        try
        {
System.out.print ( "Unpairing from device [ " + btDevice.getAddress() + " ]..." );
            Method removeBondMethod = btDevice.getClass().getMethod("removeBond");
            Boolean returnValue = (Boolean) removeBondMethod.invoke(btDevice);
System.out.println ( returnValue.booleanValue() );
            returnBoolean = returnValue.booleanValue();
        }
        catch ( NoSuchMethodException nsme )
        {
            nsme.printStackTrace ( );
            returnBoolean = false;
        }
        catch ( IllegalAccessException iae )
        {
            iae.printStackTrace ( );
            returnBoolean = false;
        }
        catch ( InvocationTargetException ite )
        {
            ite.printStackTrace ( );
            returnBoolean = false;
        }
        return returnBoolean;
    }
}
