package com.evanhoe.tango.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SqlLiteDbHelper extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_PATH = "/data/data/com.evanhoe.tango/databases/";
    // Database Name
    private static final String DATABASE_NAME = "TG.sqlite";

    @SuppressWarnings("unused")
	private SQLiteDatabase db;

   
    Context ctx;
    public SqlLiteDbHelper(Context context) {
       super(context, DATABASE_NAME, null, DATABASE_VERSION);
       ctx = context;
    }

    
    /**
     * If the database doesn't exist on the device it needs copied 
     * @throws IOException
     */
    public void CopyDataBaseFromAsset() throws IOException{
    	 	
       InputStream in  = ctx.getAssets().open("TG.sqlite");
       Log.e("Database", "Starting copying" );
       String outputFileName = DATABASE_PATH+DATABASE_NAME;
       File databaseFile = new File( "/data/data/com.evanhoe.tango/databases");
        // check if databases folder exists, if not create one and its subfolders
        if (!databaseFile.exists()){
            databaseFile.mkdir();
        }
      
       OutputStream out = new FileOutputStream(outputFileName);
       byte[] buffer = new byte[1024];
       int length;
      
      
       while ((length = in.read(buffer))>0){
              out.write(buffer,0,length);
       }
       Log.e("Database", "Completed" );
       out.flush();
       out.close();
       in.close();
      
    }
   
   
    /**
     * open connection to database
     * @throws SQLException
     */
    public void openDataBase () throws SQLException{
       String path = DATABASE_PATH+DATABASE_NAME;
       db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS | SQLiteDatabase.CREATE_IF_NECESSARY);
    }

       @Override
       public void onCreate(SQLiteDatabase db) {
             
             
       }

       @Override
       public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
              // TODO Auto-generated method stub
             
       }
}
