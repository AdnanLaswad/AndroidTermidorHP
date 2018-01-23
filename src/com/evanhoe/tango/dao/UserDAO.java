package com.evanhoe.tango.dao;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.evanhoe.tango.objs.User;
import com.evanhoe.tango.objs.WorkOrder;
import com.evanhoe.tango.objs.WorkOrderDetail;
import com.evanhoe.tango.utils.EncryptionUtilities;
import com.evanhoe.tango.utils.SqlLiteDbHelper;

public class UserDAO {

	public static boolean storeUser ( Context appContext, User user )
    {
		//deleteByUsername(appContext, user.getUsername());
		deleteAllUsers( appContext );
        boolean returnValue = true;
        SqlLiteDbHelper dbHelper;
        SQLiteDatabase sqliteDatabase;
        EncryptionUtilities myEncryptUtils = new EncryptionUtilities();

        try
        {
            dbHelper = new SqlLiteDbHelper(appContext);
            sqliteDatabase = dbHelper.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("GroupID", user.getGroupID() );
            contentValues.put("GroupName", user.getGroupName() );
            contentValues.put("PersonID", user.getPersonID() );
            contentValues.put("FirstName", user.getFirstName() );
            contentValues.put("LastName", user.getLastName() );
            contentValues.put("UserRoleCode", user.getUserRoleCode() );
            contentValues.put("UserSubRoleCode", user.getUserSubRoleCode() );
            contentValues.put("UserLogInName", user.getUsername() );
            contentValues.put("UserPassword", myEncryptUtils.encrypt ( user.getPassword() ) );
            contentValues.put("RemoteValidationRequired",user.getRemoteValidationRequired() );
            //contentValues.put("CanCompleteTrainingSWO",user.getCanDoTraining() );
            contentValues.put("CanCompleteTrainingSWO",user.getCanDoTrainingFromDB() );
            contentValues.put("RememberUserLogin", user.getRememberMeString() );            
            sqliteDatabase.insert("m_UserAccess", null, contentValues);
            sqliteDatabase.close();
        }
        catch (Exception e)
        {
            returnValue = false;
            //e.printStackTrace();
        }

        return returnValue;
    }
	
    public static boolean deleteByUsername ( Context appContext, String username )
    {
        boolean returnValue = true;

        try
        {
            SqlLiteDbHelper dbHelper = new SqlLiteDbHelper ( appContext );
            SQLiteDatabase sqliteDatabase = dbHelper.getWritableDatabase();
            sqliteDatabase.execSQL ( "DELETE FROM m_UserAccess WHERE UserLogInName = '" + username + "'" );
            sqliteDatabase.close();
        }
        catch ( SQLiteException se )
        {
            returnValue = false;
        }

        return returnValue;
    }
	
    public static boolean deleteAllUsers ( Context appContext )
    {
        boolean returnValue = true;

        try
        {
            SqlLiteDbHelper dbHelper = new SqlLiteDbHelper ( appContext );
            SQLiteDatabase sqliteDatabase = dbHelper.getWritableDatabase();
            sqliteDatabase.execSQL ( "DELETE FROM m_UserAccess" );
            sqliteDatabase.close();
        }
        catch ( SQLiteException se )
        {
            returnValue = false;
        }

        return returnValue;
    }
    
    
    public static User getLastUser ( Context appContext )
    {
        EncryptionUtilities myEncryptUtils = new EncryptionUtilities();
        SqlLiteDbHelper dbHelper = new SqlLiteDbHelper ( appContext );
        SQLiteDatabase newDB = dbHelper.getWritableDatabase();
        User user = null;
        Cursor c = newDB.rawQuery(
        // TODO: Probably a good practice to put the column names here, but for now, we're good
                    "SELECT *" +
                    " FROM " + "m_UserAccess"
                    , null);
        if (c!=null)
        {
        	if (c.moveToFirst())
            {
        		user = new User ( getColumnString ( c, "UserLogInName" )
                    , myEncryptUtils.decrypt ( getColumnString ( c, "UserPassword" ) )
                    , getColumnString ( c, "PersonID" )
                    , getColumnString ( c, "UserRoleCode" )
                    , getColumnString ( c, "UserSubRoleCode" )
                    , getColumnString ( c, "GroupID" )
                    , getColumnString ( c, "GroupName" )
                    , getColumnString ( c, "FirstName" )
                    , getColumnString ( c, "LastName" )
                    , getColumnString ( c, "CanCompleteTrainingSWO" )
                    , getColumnString ( c, "RemoteValidationRequired" )
                    , getColumnString ( c, "RememberUserLogin" )
                    );
            }
        }
        newDB.close();
        return user;
    }
    
    public static User getByUsername ( Context appContext, String username, String password )
    {
        EncryptionUtilities myEncryptUtils = new EncryptionUtilities();
        ArrayList<WorkOrderDetail> workorderDetails = new ArrayList<WorkOrderDetail>();
        SqlLiteDbHelper dbHelper = new SqlLiteDbHelper ( appContext );
        SQLiteDatabase newDB = dbHelper.getWritableDatabase();
        User user = null;
        Cursor c = newDB.rawQuery(
        // TODO: Probably a good practice to put the column names here, but for now, we're good
                    "SELECT *" +
                    " FROM " + "m_UserAccess" +
                    " WHERE UserLogInName = '" + username + "'" + " AND UserPassword = '" + 
                    myEncryptUtils.encrypt ( password ) + "'"
                    , null);
        if (c != null)
        {
            if (c.moveToFirst())
            {
                do
                {
                    user = new User ( getColumnString ( c, "UserLogInName" )
                                                 , myEncryptUtils.decrypt ( getColumnString ( c, "UserPassword" ) )
                                                 , getColumnString ( c, "PersonID" )
                                                 , getColumnString ( c, "UserRoleCode" )
                                                 , getColumnString ( c, "UserSubRoleCode" )
                                                 , getColumnString ( c, "GroupID" )
                                                 , getColumnString ( c, "GroupName" )
                                                 , getColumnString ( c, "FirstName" )
                                                 , getColumnString ( c, "LastName" )
                                                 , getColumnString ( c, "CanCompleteTrainingSWO" )
                                                 , getColumnString ( c, "RemoteValidationRequired" )
                                                 , getColumnString ( c, "RememberUserLogin" )
                                                 );
                    
                } while (c.moveToNext());
            }
        }

        newDB.close();

        return user;
    }
    
    //this is to check if just username is in local db
    public static User getByUsername ( Context appContext, String username)
    {
        EncryptionUtilities myEncryptUtils = new EncryptionUtilities();
        ArrayList<WorkOrderDetail> workorderDetails = new ArrayList<WorkOrderDetail>();
        SqlLiteDbHelper dbHelper = new SqlLiteDbHelper ( appContext );
        SQLiteDatabase newDB = dbHelper.getWritableDatabase();
        User user = null;
        Cursor c = newDB.rawQuery(
        // TODO: Probably a good practice to put the column names here, but for now, we're good
                    "SELECT *" +
                    " FROM " + "m_UserAccess" +
                    " WHERE UserLogInName = '" + username + "'"
                    , null);
        if (c != null)
        {
            if (c.moveToFirst())
            {
                do
                {
                    user = new User ( getColumnString ( c, "UserLogInName" )
                                                 , myEncryptUtils.decrypt ( getColumnString ( c, "UserPassword" ) )
                                                 , getColumnString ( c, "PersonID" )
                                                 , getColumnString ( c, "UserRoleCode" )
                                                 , getColumnString ( c, "UserSubRoleCode" )
                                                 , getColumnString ( c, "GroupID" )
                                                 , getColumnString ( c, "GroupName" )
                                                 , getColumnString ( c, "FirstName" )
                                                 , getColumnString ( c, "LastName" )
                                                 , getColumnString ( c, "CanCompleteTrainingSWO" )
                                                 , getColumnString ( c, "RemoteValidationRequired" )
                                                 , getColumnString ( c, "RememberUserLogin" )
                                                 );
                    
                } while (c.moveToNext());
            }
        }

        newDB.close();

        return user;
    }
    
    private static String getColumnString ( Cursor c, String columnName )
    {
        return c.getString ( c.getColumnIndex ( columnName ) );
    }
}
