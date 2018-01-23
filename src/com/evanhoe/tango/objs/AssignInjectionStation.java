package com.evanhoe.tango.objs;

public class AssignInjectionStation
{
    private String macAddress;
    private String personID;

    // TODO: Update this table with all fields in the local database
    
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
     * Get personID.
     *
     * @return personID as String.
     */
    public String getPersonID()
    {
        return personID;
    }
    
    /**
     * Set personID.
     *
     * @param personID the value to set.
     */
    public void setPersonID(String personID)
    {
        this.personID = personID;
    }
}
