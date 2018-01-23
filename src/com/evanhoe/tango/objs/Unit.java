package com.evanhoe.tango.objs;

import android.bluetooth.BluetoothDevice;

public class Unit
{
    private String macAddress;
    private String btName;
    private String unitName;
    private int paired;
    private BluetoothDevice bluetoothDevice;

    static final public int UNIT_PAIRED = 1;
    static final public int UNIT_PAIR_IN_PROGRESS = 2;
    static final public int UNIT_NOT_PAIRED = 3;

    public Unit ( String macAddress, String unitName, int paired )
    {
        this.macAddress = macAddress;
        this.unitName = unitName;
        this.paired = paired;
    }
    
    /**
     * Get macAddress.
     *
     * @return macAddress as String.
     */
    public String getMacAddress()
    {
        return macAddress;
    }
    
    /**
     * Set macAddress.
     *
     * @param macAddress the value to set.
     */
    public void setMacAddress(String macAddress)
    {
        this.macAddress = macAddress;
    }
    
    /**
     * Get unitName.
     *
     * @return unitName as String.
     */
    public String getUnitName()
    {
        return unitName;
    }
    
    /**
     * Set unitName.
     *
     * @param unitName the value to set.
     */
    public void setUnitName(String unitName)
    {
        this.unitName = unitName;
    }
    
    /**
     * Get paired.
     *
     * @return paired as int
     */
    public int getPaired()
    {
        return paired;
    }

    /**
     * Get paired string.
     *
     * @return pairedString as String
     */
    public String getPairedString()
    {
        String returnString = null;

        if ( paired == UNIT_PAIRED )
            returnString = "Connected";
        else if ( paired == UNIT_NOT_PAIRED )
            returnString = "Not connected";
        else if ( paired == UNIT_PAIR_IN_PROGRESS )
            returnString = "Connecting...";
        else
            returnString = "";

        return returnString;
    }
    
    /**
     * Set paired.
     *
     * @param paired the value to set.
     */
    public void setPaired(int paired)
    {
        this.paired = paired;
    }
    
    /**
     * Get btName.
     *
     * @return btName as String.
     */
    public String getBtName()
    {
        return btName;
    }
    
    /**
     * Set btName.
     *
     * @param btName the value to set.
     */
    public void setBtName(String btName)
    {
        this.btName = btName;
    }
    
    /**
     * Get bluetoothDevice.
     *
     * @return bluetoothDevice as BluetoothDevice.
     */
    public BluetoothDevice getBluetoothDevice()
    {
        return bluetoothDevice;
    }
    
    /**
     * Set bluetoothDevice.
     *
     * @param bluetoothDevice the value to set.
     */
    public void setBluetoothDevice(BluetoothDevice bluetoothDevice)
    {
        this.bluetoothDevice = bluetoothDevice;
    }
}
