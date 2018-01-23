package com.evanhoe.tango.dao;

import java.util.ArrayList;

import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.evanhoe.tango.utils.SqlLiteDbHelper;
import com.evanhoe.tango.objs.WorkOrder;

public class WorkOrderDAO
{
    public static ArrayList<WorkOrder> getAll ( Context appContext )
    {
        // Call the funct‰ion with isTraining of null so it just returns everything
        return getAll ( appContext, null );
    }
    
    /**
     * gets workorders from database based on if training
     * @param appContext
     * @param isTraining "Y" or "N" for if training
     * @return
     */
    public static ArrayList<WorkOrder> getAll ( Context appContext, String isTraining )
    {
    	ArrayList<WorkOrder> workOrders = new ArrayList<WorkOrder>();

        try
        {
            //SqlLiteDbHelper dbHelper = new SqlLiteDbHelper( this.getApplicationContext() );
            SqlLiteDbHelper dbHelper = new SqlLiteDbHelper ( appContext );
            SQLiteDatabase newDB = dbHelper.getWritableDatabase();
            Cursor c = newDB.rawQuery(
                              "SELECT ServiceWorkorderID" +
                              ", ServiceWorkorderNumber" +
                              ", ServMgmtWorkorderID" +
                              ", AddressLine1" +
                              ", AddressLine2" +
                              ", StateProvinceCode" +
                              ", City " +
                              ", HT_LinearDistanceApplication_pln " +
                              ", LinearMeasurementUnit_pln " +                              
                              ", ServMgmtGroupID" +
                              ", ServMgmtPersonID" +
                              ", ServMgmtUserRoleCode" +
                              ", ServMgmtUserSubRoleCode" +
                              ", Demo" +
                              ", TermicideTypeCode_pln" + 
                              ", SoilTypeCode_pln" + 
                              ", VolumeMeasurementUnit_pln" +
                              ", SyncTime" + 
                              " FROM m_ServiceWorkorderHdr" +
                              (isTraining != null ? " WHERE Demo = '" + isTraining + "'" : "" )
                            , null);

            if (c != null)
            {
                if (c.moveToFirst())
                {
                    do
                    {
                        String number = c.getString(c.getColumnIndex("ServiceWorkorderID"));
                        int workorderNumber = c.getInt(c.getColumnIndex("ServiceWorkorderNumber"));
                        String managementWorkorderID = c.getString(c.getColumnIndex("ServMgmtWorkorderID"));
                        String addressLine1 = c.getString(c.getColumnIndex("AddressLine1"));
                        String addressLine2 = c.getString(c.getColumnIndex("AddressLine2"));
                        String stateProvinceCode = c.getString(c.getColumnIndex("StateProvinceCode"));
                        String city = c.getString(c.getColumnIndex("City"));
                        float linearDistance = c.getFloat(c.getColumnIndex("HT_LinearDistanceApplication_pln"));
                        String linearUnit = c.getString(c.getColumnIndex("LinearMeasurementUnit_pln"));
                        
                        workOrders.add ( new WorkOrder ( number
                                                       , workorderNumber
                                                       , managementWorkorderID
                                                       , null /* locationTitle */
                                                       , null /* locationCode */
                                                       , addressLine1
                                                       , addressLine2
                                                       , null /* attentionLine */
                                                       , city
                                                       , stateProvinceCode
                                                       , null /* postalCode */
                                                       , null /* countryCode */
                                                       , null /* generalNotes */
                                                       , null /* workOrderStatusCode */
                                                       , null /* latitude */
                                                       , null /* longitude */
                                                       , linearDistance
                                                       , linearUnit
                                                       , c.getString(c.getColumnIndex("ServMgmtGroupID"))
                                                       , c.getString(c.getColumnIndex("ServMgmtPersonID"))
                                                       , c.getString(c.getColumnIndex("ServMgmtUserRoleCode"))
                                                       , c.getString(c.getColumnIndex("ServMgmtUserSubRoleCode"))
                                                       , c.getString(c.getColumnIndex("Demo"))
                                                       , c.getString(c.getColumnIndex("TermicideTypeCode_pln"))                                                       
                                                       , c.getString(c.getColumnIndex("SoilTypeCode_pln"))                       		
                                                       , c.getString(c.getColumnIndex("VolumeMeasurementUnit_pln"))              
                                                       , c.getString(c.getColumnIndex("SyncTime"))           
                        		)
                                       );
                        
                    } while (c.moveToNext());
                }
            }

            newDB.close();
        }
        catch (SQLiteException se)
        {
            //Log.e(getClass().getSimpleName(), "Could not create or Open the database");
            Log.e("WorkOrderDAO", "Could not create or Open the database");
            se.printStackTrace();
        }
        finally
        {

        }

        return workOrders;
    }
      

    public static boolean deleteAll ( Context appContext )
    {
        boolean returnValue = true;

        try
        {
            SqlLiteDbHelper dbHelper = new SqlLiteDbHelper ( appContext );
            SQLiteDatabase sqliteDatabase = dbHelper.getWritableDatabase();
            sqliteDatabase.execSQL ( "DELETE FROM m_ServiceWorkorderHdr" );
            sqliteDatabase.close();
        }
        catch ( SQLiteException se )
        {
            returnValue = false;
        }

        return returnValue;
    }

    public static boolean add ( Context appContext, WorkOrder thisWorkOrder )
    {
        boolean returnValue = true;
        SqlLiteDbHelper dbHelper;
        SQLiteDatabase sqliteDatabase;

        try
        {
            dbHelper = new SqlLiteDbHelper(appContext);
            sqliteDatabase = dbHelper.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("ServiceWorkorderId", thisWorkOrder.getServiceWorkOrderId() );
            contentValues.put("ServiceWorkorderNumber", thisWorkOrder.getServiceWorkorderNumber() );
            contentValues.put("ServMgmtWorkorderId", thisWorkOrder.getServiceManagementWorkorderId() );
            contentValues.put("AddressLine1", thisWorkOrder.getAddressLine1() );
            contentValues.put("AddressLine2", thisWorkOrder.getAddressLine2() );
            contentValues.put("StateProvinceCode", thisWorkOrder.getStateProvinceCode() );
            contentValues.put("City", thisWorkOrder.getCity() );
            contentValues.put("CountryCode", thisWorkOrder.getCountryCode() );
            contentValues.put("HT_LinearDistanceApplication_pln", thisWorkOrder.getLinearDistancePlanned() );
            contentValues.put("LinearMeasurementUnit_pln", thisWorkOrder.getLinearMeasurementUnit() );
            contentValues.put("ServMgmtGroupID",thisWorkOrder.getServMgmtGroupID() );
            contentValues.put("ServMgmtPersonID",thisWorkOrder.getServMgmtPersonID() );
            contentValues.put("ServMgmtUserRoleCode",thisWorkOrder.getServMgmtUserRoleCode() );
            contentValues.put("ServMgmtUserSubRoleCode",thisWorkOrder.getServMgmtUserSubRoleCode() );
            contentValues.put("TermicideTypeCode_pln",thisWorkOrder.getTermicideTypeCodePlanned() );
            contentValues.put("SoilTypeCode_pln",thisWorkOrder.getSoilTypeCodePlanned() );
            contentValues.put("VolumeMeasurementUnit_pln",thisWorkOrder.getVolumeMeasurementUnit() );
            contentValues.put("Demo",thisWorkOrder.getDemo() );
            contentValues.put("SyncTime",thisWorkOrder.getSyncTime() );
            
            sqliteDatabase.insert("m_ServiceWorkorderHdr", null, contentValues);
            sqliteDatabase.close();
        }
        catch (Exception e)
        {
            returnValue = false;
            //e.printStackTrace();
        }

        return returnValue;
    }
}
