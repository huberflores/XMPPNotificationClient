package org.apache.android.xmpp;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream; 
import java.io.OutputStream;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 *  
 * @author Huber Flores
 *
 */

public class AccelerometerDatabase {
	public static final String KEY_ID = "id";
    
	      
    public static final String KEY_TIME_SERVER = "servertime";
    public static final String KEY_TIME_MOBILE = "mobiletime";
    public static final String KEY_PAYLOAD = "payload";
    public static final String KEY_FROM = "fromxmpp";
    public static final String KEY_BATTERY = "battery";
    
	
	 private DatabaseHelper mDbHelper;
	 private SQLiteDatabase mDb;
	    
	 private static final String DATABASE_PATH = "/sdcard/";
	 private static final String DATABASE_NAME = "DBmotion";
	 private static final int DATABASE_VERSION = 1;
	    
	 private static final String MOTION_TABLE = "motion";

	 private static final String CREATE_MOTION_TABLE = "create table "+MOTION_TABLE+" ("
	                                         +KEY_ID+" integer primary key autoincrement, "
	                                         +KEY_FROM+" text, "
	                                         +KEY_PAYLOAD+" text, "
	                                         +KEY_TIME_SERVER+" real, "
	                                         +KEY_TIME_MOBILE+" real, "
	                                         +KEY_BATTERY+" real);";
                                         
	 private final Context mCtx;

	 private static class DatabaseHelper extends SQLiteOpenHelper {
	        DatabaseHelper(Context context) { 
	            super(context, DATABASE_NAME, null, DATABASE_VERSION); 
	        }
	    
	        public void onCreate(SQLiteDatabase db) { 
	            db.execSQL(CREATE_MOTION_TABLE);
	        }
	    
	        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	            db.execSQL("DROP TABLE IF EXISTS "+MOTION_TABLE);
	            onCreate(db);
	        }
	                
	  }
       	
	 
	 public void Reset() { mDbHelper.onUpgrade(this.mDb, 1, 1); }
	    
	    public AccelerometerDatabase(Context ctx) { 
	        mCtx = ctx;
	        mDbHelper = new DatabaseHelper(mCtx);
	    }
	    
	    public AccelerometerDatabase open() throws SQLException {
	    	
	        mDb = mDbHelper.getReadableDatabase();
	    	
	        return this;
	    }
	    
	    public void close() { mDbHelper.close(); }
	    
	    public void createEntry(String fromxmpp ,String payload, double servertime, double mobiletime, double battery){
	    	mDb.execSQL("insert into " + MOTION_TABLE + "(fromxmpp, payload, servertime, mobiletime, battery) VALUES (" + "'"+ fromxmpp +"'" + ","+ "'"+ payload + "'"+ ","+ servertime + ","+ mobiletime + "," + battery +");");
	    }

	    
	    //Copy the Database from its default location
	    public void copyDataBase() throws IOException{
			 
	    	InputStream myInput = new FileInputStream(mCtx.getDatabasePath(DATABASE_NAME).getAbsolutePath());
	    	Toast.makeText(null, mCtx.getDatabasePath(DATABASE_NAME).getAbsolutePath(), Toast.LENGTH_LONG).show();
	    	String outFileName = DATABASE_PATH + DATABASE_NAME;
	     	OutputStream myOutput = new FileOutputStream(outFileName);
	     	byte[] buffer = new byte[1024];
	    	int length;
	    	while ((length = myInput.read(buffer))>0){
	    		myOutput.write(buffer, 0, length);
	    	}
	     	myOutput.flush();
	    	myOutput.close();
	    	myInput.close();
	    	
	    	File borrar = new File(mCtx.getDatabasePath(DATABASE_NAME).getAbsolutePath());
	    	borrar.delete();
	 
	    }
	

	   
}
