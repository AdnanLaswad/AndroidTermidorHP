package com.evanhoe.tango.dao;

import java.util.ArrayList;

import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.evanhoe.tango.dao.WorkorderDetailDAO;
import com.evanhoe.tango.objs.WorkOrder;
import com.evanhoe.tango.objs.WorkOrderDetail;
import com.evanhoe.tango.utils.SqlLiteDbHelper;

public class WorkorderDetailDAO
{
    private static String tableName = "m_ServiceWorkorderEntry";
    public WorkOrderDetail get ( )
    {
        return null;
    }

    /*
     * This method will return all WorkorderDetail records that have 
     * not been synchronized back to the Web Server.
     *
     *  @return list of unsychronized work order detail records
     */
    public static ArrayList<WorkOrderDetail> getByUnsynchronized ( Context appContext )
    {
        ArrayList<WorkOrderDetail> workorderDetails = new ArrayList<WorkOrderDetail>();
        SqlLiteDbHelper dbHelper = new SqlLiteDbHelper ( appContext );
        SQLiteDatabase newDB = dbHelper.getWritableDatabase();
        Cursor c = newDB.rawQuery(
        // TODO: Probably a good practice to put the column names here, but for now, we're good
                    "SELECT *" +
//                    "SELECT EntryTime" +
//                    ", SyncStatus" +
//                    ", ServiceWorkorderID" +
//                    ", WorkorderStatusCode" +
//                    ", SoilTypeCode_act" +
//                    ", SoilTypeRefCode_act" +
//                    ", HT_InjectionCount_act" +
//                    ", HT_LinearDistanceApplication_act" +
//                    ", HT_TermicideVolume_act" +
//                    ", HT_WaterVolume_act" +
//                    ", HT_PumpVolume_act" +
//                    ", HT_InjectorTimer_act" +
//                    ", SA_TermicideVolume_act" +
//                    ", SA_WaterVolume_act" +
//                    ", SA_PumpVolume_act" +
                    " FROM " + tableName +
                    " WHERE SyncStatus = 'N'"
                    , null);
        if (c != null)
        {
            if (c.moveToFirst())
            {
                do
                {
                    WorkOrderDetail newWOD;
                    newWOD = new WorkOrderDetail ( getColumnString ( c, "EntryTime" ) /* EntryTime */
                                                 , getColumnString ( c, "SyncStatus" ) /* SyncStatus */
                                                 , getColumnString ( c, "SyncTime" )
                                                 , getColumnString ( c, "ServiceWorkorderID" ) /* ServiceWorkorderID */
                                                 , getColumnString ( c, "TechnicianGroupID" )
                                                 , getColumnString ( c, "TechnicianPersonID" )
                                                 , getColumnString ( c, "TechnicianUserRoleCode" )
                                                 , getColumnString ( c, "TechnicianUserSubRoleCode" )
                                                 , getColumnString ( c, "WorkorderStatusCode" ) /* WorkorderStatusCode */
                                                 , getColumnString ( c, "InjectionStationID_act" )
                                                 , getColumnString ( c, "InjectionDisplayFirmwareVersion" )
                                                 , getColumnString ( c, "ControllerFirmwareVersion" )
                                                 , getColumnString ( c, "BaseDisplayFirmwareVersion" )
                                                 , getColumnString ( c, "TermicideTypeCode_act" )
                                                 , getColumnString ( c, "TermicideTypeName_act" )
                                                 , getColumnString ( c, "SoilTypeCode_act" )
                                                 , getColumnString ( c, "SoilTypeRefCode_act" )
                                                 , getColumnString ( c, "HT_InjectionCount_act" )
                                                 , getColumnString ( c, "HT_LinearDistanceApplication_act" )
                                                 , getColumnString ( c, "HT_TermicideVolume_act" )
                                                 , getColumnString ( c, "HT_WaterVolume_act" )
                                                 , getColumnString ( c, "HT_PumpVolume_act" )
                                                 , getColumnString ( c, "HT_InjectorTimer_act" )
                                                 , getColumnString ( c, "SA_TermicideVolume_act" )
                                                 , getColumnString ( c, "SA_WaterVolume_act" )
                                                 , getColumnString ( c, "SA_PumpVolume_act" )
                                                 , getColumnString ( c, "LinearMeasurementUnit_act" )
                                                 , getColumnString ( c, "VolumeMeasurementUnit_act" )
                                                 , getColumnString ( c, "Latitude" )
                                                 , getColumnString ( c, "Longitude" )
                                                 , getColumnString ( c, "WorkStartTime" )
                                                 , getColumnString ( c, "WorkFinishTime" )
                                                 , getColumnString ( c, "WorkStartTimeDate" )
                                                 , getColumnString ( c, "WorkStartTimeTime" )
                                                 , getColumnString ( c, "MinutesWorkedInSession_act" )
                                                 , getColumnString ( c, "AlertMessage" )
                                                 , getColumnString ( c, "PumpAlertFlag" )
                                                 , getColumnString ( c, "WaterAlertFlag" )
                                                 , getColumnString ( c, "UsingHTFlowInSAModeAlertFlag" )
                                                 , getColumnString ( c, "HTModeEnabled" )
                                                 , getColumnString ( c, "SAModeEnabled" )
                                                 , getColumnString ( c, "WorkorderSlotLocationUsed" )
                                                 , getColumnString ( c, "WOEGeneralNotes" )
                                                 , getColumnString ( c, "WOELocationNotes" )
                                                 );
                    workorderDetails.add ( newWOD );
                    
                } while (c.moveToNext());
            }
        }

        newDB.close();

        return workorderDetails;
    }
    
    /*
     * This method will return WorkorderDetail records that  
     * match the workorderid provided.
     *
     *  @return list of unsychronized work order detail records
     */
    public static ArrayList<WorkOrderDetail> getByWorkOrderID ( Context appContext, String WorkOrderID )
    {
        ArrayList<WorkOrderDetail> workorderDetails = new ArrayList<WorkOrderDetail>();
        SqlLiteDbHelper dbHelper = new SqlLiteDbHelper ( appContext );
        SQLiteDatabase newDB = dbHelper.getWritableDatabase();
        Cursor c = newDB.rawQuery(
        // TODO: Probably a good practice to put the column names here, but for now, we're good
                    "SELECT *" +
//                    "SELECT EntryTime" +
//                    ", SyncStatus" +
//                    ", ServiceWorkorderID" +
//                    ", WorkorderStatusCode" +
//                    ", SoilTypeCode_act" +
//                    ", SoilTypeRefCode_act" +
//                    ", HT_InjectionCount_act" +
//                    ", HT_LinearDistanceApplication_act" +
//                    ", HT_TermicideVolume_act" +
//                    ", HT_WaterVolume_act" +
//                    ", HT_PumpVolume_act" +
//                    ", HT_InjectorTimer_act" +
//                    ", SA_TermicideVolume_act" +
//                    ", SA_WaterVolume_act" +
//                    ", SA_PumpVolume_act" +
                    " FROM " + tableName +
                    " WHERE ServiceWorkorderID = '" + WorkOrderID + "'"
                    , null);
        if (c != null)
        {
            if (c.moveToFirst())
            {
                do
                {
                    WorkOrderDetail newWOD;
                    newWOD = new WorkOrderDetail ( getColumnString ( c, "EntryTime" ) /* EntryTime */
                                                 , getColumnString ( c, "SyncStatus" ) /* SyncStatus */
                                                 , getColumnString ( c, "SyncTime" )
                                                 , getColumnString ( c, "ServiceWorkorderID" ) /* ServiceWorkorderID */
                                                 , getColumnString ( c, "TechnicianGroupID" )
                                                 , getColumnString ( c, "TechnicianPersonID" )
                                                 , getColumnString ( c, "TechnicianUserRoleCode" )
                                                 , getColumnString ( c, "TechnicianUserSubRoleCode" )
                                                 , getColumnString ( c, "WorkorderStatusCode" ) /* WorkorderStatusCode */
                                                 , getColumnString ( c, "InjectionStationID_act" )
                                                 , getColumnString ( c, "InjectionDisplayFirmwareVersion" )
                                                 , getColumnString ( c, "ControllerFirmwareVersion" )
                                                 , getColumnString ( c, "BaseDisplayFirmwareVersion" )
                                                 , getColumnString ( c, "TermicideTypeCode_act" )
                                                 , getColumnString ( c, "TermicideTypeName_act" )
                                                 , getColumnString ( c, "SoilTypeCode_act" )
                                                 , getColumnString ( c, "SoilTypeRefCode_act" )
                                                 , getColumnString ( c, "HT_InjectionCount_act" )
                                                 , getColumnString ( c, "HT_LinearDistanceApplication_act" )
                                                 , getColumnString ( c, "HT_TermicideVolume_act" )
                                                 , getColumnString ( c, "HT_WaterVolume_act" )
                                                 , getColumnString ( c, "HT_PumpVolume_act" )
                                                 , getColumnString ( c, "HT_InjectorTimer_act" )
                                                 , getColumnString ( c, "SA_TermicideVolume_act" )
                                                 , getColumnString ( c, "SA_WaterVolume_act" )
                                                 , getColumnString ( c, "SA_PumpVolume_act" )
                                                 , getColumnString ( c, "LinearMeasurementUnit_act" )
                                                 , getColumnString ( c, "VolumeMeasurementUnit_act" )
                                                 , getColumnString ( c, "Latitude" )
                                                 , getColumnString ( c, "Longitude" )
                                                 , getColumnString ( c, "WorkStartTime" )
                                                 , getColumnString ( c, "WorkFinishTime" )
                                                 , getColumnString ( c, "WorkStartTimeDate" )
                                                 , getColumnString ( c, "WorkStartTimeTime" )
                                                 , getColumnString ( c, "MinutesWorkedInSession_act" )
                                                 , getColumnString ( c, "AlertMessage" )
                                                 , getColumnString ( c, "PumpAlertFlag" )
                                                 , getColumnString ( c, "WaterAlertFlag" )
                                                 , getColumnString ( c, "UsingHTFlowInSAModeAlertFlag" )
                                                 , getColumnString ( c, "HTModeEnabled" )
                                                 , getColumnString ( c, "SAModeEnabled" )
                                                 , getColumnString ( c, "WorkorderSlotLocationUsed" )
                                                 , getColumnString ( c, "WOEGeneralNotes" )
                                                 , getColumnString ( c, "WOELocationNotes" )
                                                 );
                    workorderDetails.add ( newWOD );
                    
                } while (c.moveToNext());
            }
        }

        newDB.close();

        return workorderDetails;
    }
    
    
    public static ArrayList<WorkOrderDetail> getLatestEntriesForWorkorders( Context appContext )
    {
        ArrayList<WorkOrderDetail> workorderDetails = new ArrayList<WorkOrderDetail>();
        SqlLiteDbHelper dbHelper = new SqlLiteDbHelper ( appContext );
        SQLiteDatabase newDB = dbHelper.getWritableDatabase();
        Cursor c = newDB.rawQuery(
        // TODO: Probably a good practice to put the column names here, but for now, we're good
        // should be MAX(datetime(EntryTime))
                    "SELECT *" +
                    ", MAX(EntryTime)" +
                    " FROM " + tableName +
                    " GROUP BY ServiceWorkorderID"
                    , null);
        if (c != null)
        {
            if (c.moveToFirst())
            {
                do
                {
                    WorkOrderDetail newWOD;
                    newWOD = new WorkOrderDetail ( getColumnString ( c, "EntryTime" ) /* EntryTime */
                                                 , getColumnString ( c, "SyncStatus" ) /* SyncStatus */
                                                 , getColumnString ( c, "SyncTime" )
                                                 , getColumnString ( c, "ServiceWorkorderID" ) /* ServiceWorkorderID */
                                                 , getColumnString ( c, "TechnicianGroupID" )
                                                 , getColumnString ( c, "TechnicianPersonID" )
                                                 , getColumnString ( c, "TechnicianUserRoleCode" )
                                                 , getColumnString ( c, "TechnicianUserSubRoleCode" )
                                                 , getColumnString ( c, "WorkorderStatusCode" ) /* WorkorderStatusCode */
                                                 , getColumnString ( c, "InjectionStationID_act" )
                                                 , getColumnString ( c, "InjectionDisplayFirmwareVersion" )
                                                 , getColumnString ( c, "ControllerFirmwareVersion" )
                                                 , getColumnString ( c, "BaseDisplayFirmwareVersion" )
                                                 , getColumnString ( c, "TermicideTypeCode_act" )
                                                 , getColumnString ( c, "TermicideTypeName_act" )
                                                 , getColumnString ( c, "SoilTypeCode_act" )
                                                 , getColumnString ( c, "SoilTypeRefCode_act" )
                                                 , getColumnString ( c, "HT_InjectionCount_act" )
                                                 , getColumnString ( c, "HT_LinearDistanceApplication_act" )
                                                 , getColumnString ( c, "HT_TermicideVolume_act" )
                                                 , getColumnString ( c, "HT_WaterVolume_act" )
                                                 , getColumnString ( c, "HT_PumpVolume_act" )
                                                 , getColumnString ( c, "HT_InjectorTimer_act" )
                                                 , getColumnString ( c, "SA_TermicideVolume_act" )
                                                 , getColumnString ( c, "SA_WaterVolume_act" )
                                                 , getColumnString ( c, "SA_PumpVolume_act" )
                                                 , getColumnString ( c, "LinearMeasurementUnit_act" )
                                                 , getColumnString ( c, "VolumeMeasurementUnit_act" )
                                                 , getColumnString ( c, "Latitude" )
                                                 , getColumnString ( c, "Longitude" )
                                                 , getColumnString ( c, "WorkStartTime" )
                                                 , getColumnString ( c, "WorkFinishTime" )
                                                 , getColumnString ( c, "WorkStartTimeDate" )
                                                 , getColumnString ( c, "WorkStartTimeTime" )
                                                 , getColumnString ( c, "MinutesWorkedInSession_act" )
                                                 , getColumnString ( c, "AlertMessage" )
                                                 , getColumnString ( c, "PumpAlertFlag" )
                                                 , getColumnString ( c, "WaterAlertFlag" )
                                                 , getColumnString ( c, "UsingHTFlowInSAModeAlertFlag" )
                                                 , getColumnString ( c, "HTModeEnabled" )
                                                 , getColumnString ( c, "SAModeEnabled" )
                                                 , getColumnString ( c, "WorkorderSlotLocationUsed" )
                                                 , getColumnString ( c, "WOEGeneralNotes" )
                                                 , getColumnString ( c, "WOELocationNotes" )
                                                 );
                    workorderDetails.add ( newWOD );
                    
                } while (c.moveToNext());
            }
        }

        newDB.close();

        return workorderDetails;
    }

    private static String getColumnString ( Cursor c, String columnName )
    {
        return c.getString ( c.getColumnIndex ( columnName ) );
    }

    public static ArrayList<WorkOrderDetail> getList ( Context appContext )
    {
        ArrayList<WorkOrderDetail> workorderDetails = new ArrayList<WorkOrderDetail>();
        SqlLiteDbHelper dbHelper = new SqlLiteDbHelper ( appContext );
        SQLiteDatabase newDB = dbHelper.getWritableDatabase();
        Cursor c = newDB.rawQuery(
                    "SELECT EntryTime" +
                    ", SyncStatus" +
                    ", ServiceWorkorderID" +
                    ", WorkorderStatusCode" +
                    " FROM " + tableName
                    , null);
        if (c != null)
        {
            if (c.moveToFirst())
            {
                do
                {
                    WorkOrderDetail newWOD;
//                    newWOD = new WorkOrderDetail ( getColumnString ( c, "EntryTime" ) /* entryTime */
//                                                 , getColumnString ( c, "SyncStatus" ) /* syncStatus */
//                                                 , null /* viewportCode */
//                                                 , getColumnString ( c, "ServiceWorkorderID" ) /* serviceWorkorderID */
//                                                 , null /* serviceWorkorderNumber */
//                                                 , null /* latestEntry */
//                                                 , null /* servMgmtGroupID */
//                                                 , null /* servMgmtPersonID */
//                                                 , null /* servMgmtUserRoleCode */
//                                                 , null /* servMgmtUserSubRoleCode */
//                                                 , getColumnString ( c, "WorkorderStatusCode" ) /* workorderStatusCode */
//                                                 , null /* injectionStationID_act */
//                                                 , null /* termicideTypeCode_act */
//                                                 , null /* injectionCount_act */
//                                                 , null /* linearDistanceApplication_act */
//                                                 , null /* soilTypeCode_act */
//                                                 , null /* injectionTypeCode_act */
//                                                 , null /* injectionTimerSetting_act */
//                                                 , null /* termicideInjectionVolume_act */
//                                                 , null /* totalInjectionVolume_act */
//                                                 , null /* totalInjectionTime_act */
//                                                 , null /* linearMeasurementUnit_act */
//                                                 , null /* volumeMeasurementUnit_act */
//                                                 , null /* latitude */
//                                                 , null /* longitude */
//                                                 , null /* alertMessage */
//                                                 , null /* generalNotes */
//                                                 , null /* locationNotes */
//                                                 );
//                    workorderDetails.add ( newWOD );
                    
                } while (c.moveToNext());
            }
        }

        newDB.close();

        return workorderDetails;
    }
    
    public static WorkOrderDetail getLatestByWorkOrderID ( Context appContext, String WorkOrderID )
    {
        String s=WorkOrderID;
    	WorkOrderDetail newWOD = null;
        SqlLiteDbHelper dbHelper = new SqlLiteDbHelper ( appContext );
        SQLiteDatabase newDB = dbHelper.getWritableDatabase();
        Cursor c = newDB.rawQuery(
        // TODO: Probably a good practice to put the column names here, but for now, we're good
        // Needs to be MAX(datetime(EntryTime))
        		"SELECT *" +
                ", MAX(EntryTime)" +
                " FROM " + tableName +
                " WHERE ServiceWorkorderID = '" + WorkOrderID + "'" +
                " GROUP BY ServiceWorkorderID"    
                    , null);
        if (c != null)
        {
            if (c.moveToFirst())
            {
                do
                {
                    newWOD = new WorkOrderDetail ( getColumnString ( c, "EntryTime" ) /* EntryTime */
                                                 , getColumnString ( c, "SyncStatus" ) /* SyncStatus */
                                                 , getColumnString ( c, "SyncTime" )
                                                 , getColumnString ( c, "ServiceWorkorderID" ) /* ServiceWorkorderID */
                                                 , getColumnString ( c, "TechnicianGroupID" )
                                                 , getColumnString ( c, "TechnicianPersonID" )
                                                 , getColumnString ( c, "TechnicianUserRoleCode" )
                                                 , getColumnString ( c, "TechnicianUserSubRoleCode" )
                                                 , getColumnString ( c, "WorkorderStatusCode" ) /* WorkorderStatusCode */
                                                 , getColumnString ( c, "InjectionStationID_act" )
                                                 , getColumnString ( c, "InjectionDisplayFirmwareVersion" )
                                                 , getColumnString ( c, "ControllerFirmwareVersion" )
                                                 , getColumnString ( c, "BaseDisplayFirmwareVersion" )
                                                 , getColumnString ( c, "TermicideTypeCode_act" )
                                                 , getColumnString ( c, "TermicideTypeName_act" )
                                                 , getColumnString ( c, "SoilTypeCode_act" )
                                                 , getColumnString ( c, "SoilTypeRefCode_act" )
                                                 , getColumnString ( c, "HT_InjectionCount_act" )
                                                 , getColumnString ( c, "HT_LinearDistanceApplication_act" )
                                                 , getColumnString ( c, "HT_TermicideVolume_act" )
                                                 , getColumnString ( c, "HT_WaterVolume_act" )
                                                 , getColumnString ( c, "HT_PumpVolume_act" )
                                                 , getColumnString ( c, "HT_InjectorTimer_act" )
                                                 , getColumnString ( c, "SA_TermicideVolume_act" )
                                                 , getColumnString ( c, "SA_WaterVolume_act" )
                                                 , getColumnString ( c, "SA_PumpVolume_act" )
                                                 , getColumnString ( c, "LinearMeasurementUnit_act" )
                                                 , getColumnString ( c, "VolumeMeasurementUnit_act" )
                                                 , getColumnString ( c, "Latitude" )
                                                 , getColumnString ( c, "Longitude" )
                                                 , getColumnString ( c, "WorkStartTime" )
                                                 , getColumnString ( c, "WorkFinishTime" )
                                                 , getColumnString ( c, "WorkStartTimeDate" )
                                                 , getColumnString ( c, "WorkStartTimeTime" )
                                                 , getColumnString ( c, "MinutesWorkedInSession_act" )
                                                 , getColumnString ( c, "AlertMessage" )
                                                 , getColumnString ( c, "PumpAlertFlag" )
                                                 , getColumnString ( c, "WaterAlertFlag" )
                                                 , getColumnString ( c, "UsingHTFlowInSAModeAlertFlag" )
                                                 , getColumnString ( c, "HTModeEnabled" )
                                                 , getColumnString ( c, "SAModeEnabled" )
                                                 , getColumnString ( c, "WorkorderSlotLocationUsed" )
                                                 , getColumnString ( c, "WOEGeneralNotes" )
                                                 , getColumnString ( c, "WOELocationNotes" )
                                                 );
                    
                    
                } while (c.moveToNext());
            }
        }

        newDB.close();

        return newWOD;
    }
    
    
    public static String getLatestWorkOrderDetailSyncTime ( Context appContext)
    {
    	String maxTime = null;
        SqlLiteDbHelper dbHelper = new SqlLiteDbHelper ( appContext );
        SQLiteDatabase newDB = dbHelper.getWritableDatabase();
        Cursor c = newDB.rawQuery(
        // TODO: Probably a good practice to put the column names here, but for now, we're good
        // MAX(SyncTime) should be MAX(datetime(SyncTime))
        		"SELECT " +
                " MAX(SyncTime)" +
                " FROM " + tableName    
                    , null);
        if (c != null)
        {
            if (c.moveToFirst())
            {
                do
                {

                    maxTime = getColumnString ( c, "MAX(SyncTime)" );
                   
                } while (c.moveToNext());
            }
        }

        newDB.close();

        return maxTime;
    }

    public static boolean add ( Context appContext, WorkOrderDetail workorderdetail )
    {
        boolean returnValue = true;
        SqlLiteDbHelper dbHelper;
        SQLiteDatabase sqliteDatabase;
        
        
        try
        {
            dbHelper = new SqlLiteDbHelper ( appContext );
            sqliteDatabase = dbHelper.getWritableDatabase();
            ContentValues contentValues = new ContentValues();

            contentValues.put ( "EntryTime", workorderdetail.getEntryTime() );
            contentValues.put ( "SyncStatus", workorderdetail.getSyncStatus() );
            contentValues.put ( "SyncTime", workorderdetail.getSyncTime() );
            contentValues.put ( "ServiceWorkorderID", workorderdetail.getServiceWorkorderID() );
            contentValues.put ( "TechnicianGroupID", workorderdetail.getTechnicianGroupID() );
            contentValues.put ( "TechnicianPersonID", workorderdetail.getTechnicianPersonID() );
            contentValues.put ( "TechnicianUserRoleCode", workorderdetail.getTechnicianUserRoleCode() );
            contentValues.put ( "TechnicianUserSubRoleCode", workorderdetail.getTechnicianUserSubRoleCode() );
            contentValues.put ( "WorkorderStatusCode", workorderdetail.getWorkorderStatusCode() );
            contentValues.put ( "InjectionStationID_act", workorderdetail.getInjectionStationID_act() );
            contentValues.put ( "InjectionDisplayFirmwareVersion", workorderdetail.getInjectionDisplayFirmwareVersion() );
            contentValues.put ( "ControllerFirmwareVersion", workorderdetail.getControllerFirmwareVersion() );
            contentValues.put ( "BaseDisplayFirmwareVersion", workorderdetail.getBaseDisplayFirmwareVersion() );
            contentValues.put ( "TermicideTypeCode_act", workorderdetail.getTermicideTypeCode_act() );
            contentValues.put ( "TermicideTypeName_act", workorderdetail.getTermicideTypeName_act() );
            contentValues.put ( "SoilTypeCode_act", workorderdetail.getSoilTypeCode_act() );
            contentValues.put ( "SoilTypeRefCode_act", workorderdetail.getSoilTypeRefCode_act() );
            contentValues.put ( "HT_InjectionCount_act", workorderdetail.getHT_InjectionCount_act() );
            contentValues.put ( "HT_LinearDistanceApplication_act", workorderdetail.getHT_LinearDistanceApplication_act() );
            contentValues.put ( "HT_TermicideVolume_act", workorderdetail.getHT_TermicideVolume_act() );
            contentValues.put ( "HT_WaterVolume_act", workorderdetail.getHT_WaterVolume_act() );
            contentValues.put ( "HT_PumpVolume_act", workorderdetail.getHT_PumpVolume_act() );
            contentValues.put ( "HT_InjectorTimer_act", workorderdetail.getHT_InjectorTimer_act() );
            contentValues.put ( "SA_TermicideVolume_act", workorderdetail.getSA_TermicideVolume_act() );
            contentValues.put ( "SA_WaterVolume_act", workorderdetail.getSA_WaterVolume_act() );
            contentValues.put ( "SA_PumpVolume_act", workorderdetail.getSA_PumpVolume_act() );
            contentValues.put ( "LinearMeasurementUnit_act", workorderdetail.getLinearMeasurementUnit_act() );
            contentValues.put ( "VolumeMeasurementUnit_act", workorderdetail.getVolumeMeasurementUnit_act() );
            contentValues.put ( "Latitude", workorderdetail.getLatitude() );
            contentValues.put ( "Longitude", workorderdetail.getLongitude() );
            contentValues.put ( "WorkStartTime", workorderdetail.getWorkStartTime() );
            contentValues.put ( "WorkFinishTime", workorderdetail.getWorkFinishTime() );
            contentValues.put ( "WorkStartTimeDate", workorderdetail.getWorkStartTimeDate() );
            contentValues.put ( "WorkStartTimeTime", workorderdetail.getWorkStartTimeTime() );
            contentValues.put ( "MinutesWorkedInSession_act", workorderdetail.getMinutesWorkedInSession_act() );
            contentValues.put ( "AlertMessage", workorderdetail.getAlertMessage() );
            contentValues.put ( "PumpAlertFlag", workorderdetail.getPumpAlertFlag() );
            contentValues.put ( "WaterAlertFlag", workorderdetail.getWaterAlertFlag() );
            contentValues.put ( "UsingHTFlowInSAModeAlertFlag", workorderdetail.getUsingHTFlowInSAModeAlertFlag() );
            contentValues.put ( "HTModeEnabled", workorderdetail.getHTModeEnabled() );
            contentValues.put ( "SAModeEnabled", workorderdetail.getSAModeEnabled() );
            contentValues.put ( "WorkorderSlotLocationUsed", workorderdetail.getWorkorderSlotLocationUsed() );
            contentValues.put ( "WOEGeneralNotes", workorderdetail.getWOEGeneralNotes() );
            contentValues.put ( "WOELocationNotes", workorderdetail.getWOELocationNotes() );

            sqliteDatabase.insert(tableName, null, contentValues);
            sqliteDatabase.close();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            returnValue = false;
        }

        return returnValue;
    }
    public boolean delete ( WorkOrderDetail workorderDetail )
    {
        return true;
    }

    public static boolean deleteAll ( Context appContext )
    {
        boolean returnValue = true;

        try
        {
            SqlLiteDbHelper dbHelper = new SqlLiteDbHelper ( appContext );
            SQLiteDatabase sqliteDatabase = dbHelper.getWritableDatabase();
            sqliteDatabase.execSQL ( "DELETE FROM " + tableName );
            sqliteDatabase.close();
        }
        catch ( SQLiteException se )
        {
            returnValue = false;
        }

        return returnValue;
    }
    
    
    public static boolean deleteAllSyncedByID ( Context appContext, String workOrderID )
    {
        boolean returnValue = true;

        try
        {
            SqlLiteDbHelper dbHelper = new SqlLiteDbHelper ( appContext );
            SQLiteDatabase sqliteDatabase = dbHelper.getWritableDatabase();
            sqliteDatabase.execSQL ( "DELETE FROM " + tableName + " WHERE SyncStatus = 'Y' AND ServiceWorkorderID = '" + workOrderID + "'" );
            sqliteDatabase.close();
        }
        catch ( SQLiteException se )
        {
            returnValue = false;
        }

        return returnValue;
    }
    
    
    public static boolean deleteAllNotSpecified ( Context appContext, ArrayList<String> workOrderIDs )
    {
        boolean returnValue = true;
        
        
        String statementAddition = "";
        for ( String workorderID : workOrderIDs )
        {   		
        	statementAddition = statementAddition + " AND ServiceWorkorderID != '" + workorderID + "'";    		
        }
        
        if(statementAddition == ""){
        	return true;
        }
        

        try
        {
            SqlLiteDbHelper dbHelper = new SqlLiteDbHelper ( appContext );
            SQLiteDatabase sqliteDatabase = dbHelper.getWritableDatabase();
            sqliteDatabase.execSQL ( "DELETE FROM " + tableName + " WHERE SyncStatus = 'Y'" + statementAddition );
            sqliteDatabase.close();
        }
        catch ( SQLiteException se )
        {
            returnValue = false;
        }

        return returnValue;
    }
    
    public static boolean deleteAllSynced ( Context appContext )
    {
        boolean returnValue = true;

        try
        {
            SqlLiteDbHelper dbHelper = new SqlLiteDbHelper ( appContext );
            SQLiteDatabase sqliteDatabase = dbHelper.getWritableDatabase();
            sqliteDatabase.execSQL ( "DELETE FROM " + tableName + " WHERE SyncStatus = 'Y'" );
            sqliteDatabase.close();
        }
        catch ( SQLiteException se )
        {
            returnValue = false;
        }

        return returnValue;
    }
    
    
    public static boolean updateRecord(Context appContext, WorkOrderDetail woDetail){
    	
        try
        {
        	
		SqlLiteDbHelper dbHelper = new SqlLiteDbHelper(appContext);
		SQLiteDatabase sqliteDatabase = dbHelper.getWritableDatabase();

		String strWhere = "ServiceWorkorderID=?";
		String[] whereValues = new String[]{woDetail.getServiceWorkorderID()};
		ContentValues contentValues = new ContentValues();
		contentValues.put("SyncStatus", woDetail.getSyncStatus());
		contentValues.put("SyncTime", woDetail.getSyncTime());
		sqliteDatabase.update("m_ServiceWorkorderEntry", contentValues, strWhere, whereValues);

		sqliteDatabase.close();
		
        }
        catch ( SQLiteException se )
        {
            return false;
        }
        
        return true;
    	
    }
    
    
    
    
}
