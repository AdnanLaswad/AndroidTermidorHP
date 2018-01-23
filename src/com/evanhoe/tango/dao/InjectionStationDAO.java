package com.evanhoe.tango.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.evanhoe.tango.objs.InjectionStation;
import com.evanhoe.tango.utils.SqlLiteDbHelper;

public class InjectionStationDAO
{
    private final static String TABLE_NAME = "m_InjectionStation";

    public static String getID ( Context appContext, String macAddress )
    {
        String returnID = null;
        String getIdQuery = "SELECT InjectionStationID"
                          + " FROM " + TABLE_NAME
                          + " WHERE UPPER(ControllerIPAddress) = UPPER( ? )";

        SqlLiteDbHelper dbHelper = new SqlLiteDbHelper ( appContext );
        SQLiteDatabase newDB = dbHelper.getWritableDatabase();
        String queryArgs[] = { macAddress };
        Cursor c = newDB.rawQuery ( getIdQuery, queryArgs );
        if ( c != null )
        {
            boolean canContinue = false;
            canContinue = c.moveToFirst();
            while ( canContinue )
            {
            //if ( c.moveToFirst() )
            //{
                //do
                //{
                returnID = c.getString ( c.getColumnIndex ( "InjectionStationID" ) );
                //} while ( c.moveToNext() );
                canContinue = c.moveToNext();
            //}
            }
        }

        newDB.close();

        return returnID;
    }

    public static InjectionStation get ( Context appContext, String injectionStationID )
    {
        InjectionStation returnStation = null;
        // We are only populating these 4 fields at this time - 19-Nov-2014 JWM
        String getIdQuery = "SELECT InjectionStationID"
                          + ", IPAddress"
                          + ", ControllerIPAddress"
                          + ", InjectionStationName"
                          + " FROM " + TABLE_NAME
                          + " WHERE UPPER(InjectionStationID) = UPPER( ? )";

        SqlLiteDbHelper dbHelper = new SqlLiteDbHelper ( appContext );
        SQLiteDatabase newDB = dbHelper.getWritableDatabase();
        String queryArgs[] = { injectionStationID };
        Cursor c = newDB.rawQuery ( getIdQuery, queryArgs );
        if ( c != null )
        {
            boolean canContinue = false;
            canContinue = c.moveToFirst();
            while ( canContinue )
            {
                returnStation = new InjectionStation ( c.getString ( c.getColumnIndex ( "InjectionStationID" ) )
                                                     , c.getString ( c.getColumnIndex ( "IPAddress" ) )
                                                     , c.getString ( c.getColumnIndex ( "ControllerIPAddress" ) )
                                                     , c.getString ( c.getColumnIndex ( "InjectionStationName" ) ) );

                canContinue = c.moveToNext();
            }
        }

        newDB.close();

        return returnStation;
    }

    public static boolean add ( Context appContext, InjectionStation stationToAdd )
    {
        boolean returnValue = true;
        SqlLiteDbHelper dbHelper;
        SQLiteDatabase sqliteDatabase;

        try
        {
            dbHelper = new SqlLiteDbHelper(appContext);
            sqliteDatabase = dbHelper.getWritableDatabase();

            ContentValues contentValues = new ContentValues();
            contentValues.put ( "InjectionStationID", stationToAdd.getInjectionStationID() );
            contentValues.put ( "IPAddress", stationToAdd.getIPAddress() );
            contentValues.put ( "ControllerIPAddress", stationToAdd.getControllerIPAddress() );
           // contentValues.put ( "ControllerIPAddress", "00:12:F3:29:32:8B");
            contentValues.put ( "InjectionStationName", stationToAdd.getInjectionStationName() );
            contentValues.put ( "WirelessFirmwareVersion", stationToAdd.getWirelessFirmwareVersion() );
            contentValues.put ( "InjectionDisplayFirmwareVersion", stationToAdd.getInjectionDisplayFirmwareVersion() );
            contentValues.put ( "ControllerFirmwareVersion", stationToAdd.getControllerFirmwareVersion() );
            contentValues.put ( "BaseDisplayFirmwareVersion", stationToAdd.getBaseDisplayFirmwareVersion() );
            contentValues.put ( "TelemetryVersion", stationToAdd.getTelemetryVersion() );
            contentValues.put ( "BaseUnitID", stationToAdd.getBaseUnitID() );
            contentValues.put ( "ControllerID", stationToAdd.getControllerID() );
            contentValues.put ( "InjectorID", stationToAdd.getInjectorID() );
            contentValues.put ( "WirelessID", stationToAdd.getWirelessID() );
            contentValues.put ( "ToolStatusCode", "ZZ" /*stationToAdd.getToolStatusCode()*/ );
            contentValues.put ( "GeneralNotes", stationToAdd.getGeneralNotes() );
            contentValues.put ( "LocationNotes", stationToAdd.getLocationNotes() );

            sqliteDatabase.insert(TABLE_NAME, null, contentValues);
            sqliteDatabase.close();
        }
        catch (Exception e)
        {
            returnValue = false;
            //e.printStackTrace();
        }

        return returnValue;
    }

    public static boolean deleteAll ( Context appContext )
    {
        String deleteAllQuery = "DELETE FROM " + TABLE_NAME;
        boolean returnValue = true;

        try
        {
            SqlLiteDbHelper dbHelper = new SqlLiteDbHelper ( appContext );
            SQLiteDatabase sqliteDatabase = dbHelper.getWritableDatabase();
            sqliteDatabase.execSQL ( deleteAllQuery );
            sqliteDatabase.close();
        }
        catch ( SQLiteException se )
        {
            returnValue = false;
        }

        return returnValue;
    }
}
