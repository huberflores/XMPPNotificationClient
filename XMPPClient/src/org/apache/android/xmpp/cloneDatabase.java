package org.apache.android.xmpp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;

/**
 *  
 * @author Huber Flores
 *
 */


public class cloneDatabase {

	private String outFileName;
	
	//Copy the Database from its default location
    public void copyDataBase() throws IOException{
		 
    	InputStream myInput = new FileInputStream("/data/data/org.apache.android.xmpp/databases/DBmotion");
    	Calendar calendar = Calendar.getInstance();
    	
    	this.outFileName = "/sdcard/DBmotion" + calendar.getTimeInMillis()+".sql";
     	OutputStream myOutput = new FileOutputStream(outFileName);
     	byte[] buffer = new byte[1024];
    	int length;
    	while ((length = myInput.read(buffer))>0){
    		myOutput.write(buffer, 0, length);
    	}
     	myOutput.flush();
    	myOutput.close();
    	myInput.close();
    	
    	File borrar = new File("/data/data/org.apache.android.xmpp/databases/DBmotion");
    	borrar.delete();
 
    }
    
    public boolean fileToCopy(){
    	File check = new File("/data/data/org.apache.android.xmpp/databases/DBmotion");
    	if (check.exists()==true){
    		return true;
    	}else{
    		return false;
    	}
    }

    public String getDataBasePath(){
    	return this.outFileName;
    }
	
}
