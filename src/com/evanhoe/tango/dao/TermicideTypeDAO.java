package com.evanhoe.tango.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.evanhoe.tango.objs.InjectionStation;
import com.evanhoe.tango.objs.TermicideType;
import com.evanhoe.tango.utils.SqlLiteDbHelper;

public class TermicideTypeDAO {

	private final static String TABLE_NAME = "m_TermicideType";
		
	//need a function yet to get info based on an id
	
	
	public static TermicideType get ( Context appContext, String TermicideTypeCode )
    {
		TermicideType returnType = null;
        // We are only populating these 4 fields at this time - 19-Nov-2014 JWM
        String getIdQuery = "SELECT TermicideTypeCode"
                          + ", TermicideTypeName"
                          + ", DilutionRatio"
                          + ", InjectionCountFactor"
                          + " FROM " + TABLE_NAME
                          + " WHERE UPPER(TermicideTypeCode) = UPPER( ? )";

        SqlLiteDbHelper dbHelper = new SqlLiteDbHelper ( appContext );
        SQLiteDatabase newDB = dbHelper.getWritableDatabase();
        String queryArgs[] = { TermicideTypeCode };
        Cursor c = newDB.rawQuery ( getIdQuery, queryArgs );
        if ( c != null )
        {
            boolean canContinue = false;
            canContinue = c.moveToFirst();
            while ( canContinue )
            {
            	returnType = new TermicideType ( c.getString ( c.getColumnIndex ( "TermicideTypeCode" ) )
                                                     , c.getString ( c.getColumnIndex ( "TermicideTypeName" ) )
                                                     , Double.parseDouble(c.getString ( c.getColumnIndex ( "DilutionRatio" )) )
                                                     , Double.parseDouble(c.getString ( c.getColumnIndex ( "InjectionCountFactor" )) ) );

                canContinue = c.moveToNext();
            }
        }

        newDB.close();

        return returnType;
    }
	
	public static TermicideType getByName ( Context appContext, String TermicideTypeName )
    {
		TermicideType returnType = null;
        // We are only populating these 4 fields at this time - 19-Nov-2014 JWM
        String getIdQuery = "SELECT TermicideTypeCode"
                          + ", TermicideTypeName"
                          + ", DilutionRatio"
                          + ", InjectionCountFactor"
                          + " FROM " + TABLE_NAME
                          + " WHERE UPPER(TermicideTypeName) = UPPER( ? )";

        SqlLiteDbHelper dbHelper = new SqlLiteDbHelper ( appContext );
        SQLiteDatabase newDB = dbHelper.getWritableDatabase();
        String queryArgs[] = { TermicideTypeName };
        Cursor c = newDB.rawQuery ( getIdQuery, queryArgs );
        if ( c != null )
        {
            boolean canContinue = false;
            canContinue = c.moveToFirst();
            while ( canContinue )
            {
            	returnType = new TermicideType ( c.getString ( c.getColumnIndex ( "TermicideTypeCode" ) )
                                                     , c.getString ( c.getColumnIndex ( "TermicideTypeName" ) )
                                                     , Double.parseDouble(c.getString ( c.getColumnIndex ( "DilutionRatio" )) )
                                                     , Double.parseDouble(c.getString ( c.getColumnIndex ( "InjectionCountFactor" )) ) );
                canContinue = c.moveToNext();
            }
        }

        newDB.close();

        return returnType;
    }
	
	
    public static boolean add ( Context appContext, TermicideType typeToAdd )
    {
        boolean returnValue = true;
        SqlLiteDbHelper dbHelper;
        SQLiteDatabase sqliteDatabase;

        try
        {
            dbHelper = new SqlLiteDbHelper(appContext);
            sqliteDatabase = dbHelper.getWritableDatabase();

            ContentValues contentValues = new ContentValues();
            contentValues.put ( "TermicideTypeCode", typeToAdd.getTermicideTypeCode() );
            contentValues.put ( "TermicideTypeName", typeToAdd.getTermicideTypeName() );
            contentValues.put ( "DilutionRatio", typeToAdd.getDilutionRatio() );
            contentValues.put ( "InjectionCountFactor", typeToAdd.getInjectionCountFactor() );


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
