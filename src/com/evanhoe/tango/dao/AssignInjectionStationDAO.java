package com.evanhoe.tango.dao;

import com.evanhoe.tango.objs.AssignInjectionStation;

public class AssignInjectionStationDAO
{
    public static AssignInjectionStation get ( String macAddress, String personID )
    {
        AssignInjectionStation thisRecord = null;

        // TODO: Make this function actually retrieve from the database
        thisRecord = new AssignInjectionStation();
        thisRecord.setMacAddress ( macAddress );
        thisRecord.setPersonID ( personID );

        return thisRecord;
    }
}
