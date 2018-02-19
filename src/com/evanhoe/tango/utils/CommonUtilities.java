package com.evanhoe.tango.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import org.json.JSONObject;

import com.evanhoe.tango.LoginActivity;
import com.evanhoe.tango.TangoApplication;
import com.evanhoe.tango.WorkOrderDetailActivity;
import com.evanhoe.tango.WorkOrderListActivity;
import com.evanhoe.tango.dao.AssignInjectionStationDAO;
import com.evanhoe.tango.dao.InjectionStationDAO;
import com.evanhoe.tango.dao.TermicideTypeDAO;
import com.evanhoe.tango.dao.UserDAO;
import com.evanhoe.tango.dao.WorkOrderDAO;
import com.evanhoe.tango.dao.WorkorderDetailDAO;
import com.evanhoe.tango.objs.AssignInjectionStation;
import com.evanhoe.tango.objs.InjectionStation;
import com.evanhoe.tango.objs.TermicideType;
import com.evanhoe.tango.objs.User;
import com.evanhoe.tango.objs.UserLoginResult;
import com.evanhoe.tango.objs.WorkOrder;
import com.evanhoe.tango.objs.WorkOrderBrief;
import com.evanhoe.tango.objs.WorkOrderDetail;
import com.evanhoe.tango.objs.WorkorderStatus;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

public class CommonUtilities {
	
	public static int getEnv(){
		// 1 - DEV
		// 2 - PRE-PROD
		// 3 - PROD
		return 3;		
	}
	
	public static boolean clearDB(Context appContext){
		
		InjectionStationDAO.deleteAll(appContext);
		TermicideTypeDAO.deleteAll(appContext);
		WorkorderDetailDAO.deleteAll(appContext);
		WorkOrderDAO.deleteAll(appContext);
		UserDAO.deleteAllUsers(appContext);
		
		return true;
	}
	
	
	public static boolean isOnline(Context context){
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		if(ni==null){
			return false;
		}
		return true;
	}
	
	/**
	 * Takes a string and breaks it into array of chunkSize arrays of integer (decimal) values replacing characters
	 * @param input
	 * @param chunkSize
	 * @param includeMessageNumber
	 * @return
	 */
	
	public static int[][] stringToBlock(String input, int chunkSize, boolean includeMessageNumber){
		String tempInput = input;
		int tempChunkSize = chunkSize;
		if (input==null) {
			return null;
		}
		try {
			if (includeMessageNumber) {
				tempChunkSize-=1;					
			}
			int numMessages = tempInput.length() / tempChunkSize;
			if (tempInput.length() % tempChunkSize > 0) {
				numMessages+=1;
			}
			int arrayOfDataArray[][] = new int[numMessages][chunkSize];			
			String stringToSend = null;
			for (int i=0; i<numMessages; i++){
				if (tempInput.length() < tempChunkSize) {
					stringToSend = tempInput;
				} else {
					stringToSend = tempInput.substring(0, tempChunkSize);
		    		tempInput = tempInput.substring(tempChunkSize, tempInput.length());
				}    		
	    		arrayOfDataArray[i] = stringToIntArray(stringToSend, includeMessageNumber);
	    		if (includeMessageNumber) {
	    			arrayOfDataArray[i][0] = (int)i;
	    		}
			}
			return arrayOfDataArray;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Takes an Array of Integer Arrays and converts to concatenated string
	 * @param inputArrayOfArrays
	 * @param messageNumberIncluded
	 * @return
	 */
	public static String blockToString(int[][] inputArrayOfArrays, boolean messageNumberIncluded) {
		if (inputArrayOfArrays==null){
			return null;
		}
		try {
			StringBuilder builder = new StringBuilder(); 
			for (int i=0; i<inputArrayOfArrays.length; i++){
				for (int x =0; x<inputArrayOfArrays[i].length; x++) {
					if (messageNumberIncluded && x==0) {
						x=1;
					}
//put code here to skip weird character in HP
//					if(inputArrayOfArrays[i][x]!=250) {
						builder.append((char)inputArrayOfArrays[i][x]);						
//					}
				}
			}
				return builder.toString();			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;	
	}
	
	/**
	 * Takes a string and returns an array of integer (decimal) values representing characters
	 * @param input
	 * @return
	 */
	private static int[] stringToIntArray(String input, boolean includeMessageNumber) {
	        if(input == null){
	            return null;
	        }
	        try{   
	        	if (includeMessageNumber) {
	        		input = " " + input;
	        	}
	        	int listInt[] = new int[input.length()];
		    	for (int i=0; i< listInt.length; i++){
		    		listInt[i] = (int)input.charAt(i);        		
		    	}
		    	return listInt;
		    }
		    catch(Exception e){
		    	e.printStackTrace();
		    }
		    return null;
	}
	
	/**
	 * Adds spaces to string
	 * @param text
	 * @param size
	 * @return
	 */
    public static String addPadding(String text, int size) {
        StringBuilder builder = new StringBuilder(text);
        while (builder.length() < size) {
            builder.append(' ');
        }
        return builder.toString();
    }
    
    public static void syncTheUnsynced(Context appContext,String token){
	 // get unsynched details, and send to web server
    	if(isOnline(appContext)){
    		
    	
	    ArrayList<WorkOrderDetail> unsyncedDetails;
	    unsyncedDetails = WorkorderDetailDAO.getByUnsynchronized ( appContext );
	    for ( WorkOrderDetail syncThisDetail : unsyncedDetails )
	    {
	        try
	        {
                //Calendar cal = Calendar.getInstance();
                //SimpleDateFormat sdf = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss" );
	        	//syncThisDetail.setSyncTime( sdf.format ( cal.getTime() ) + " " + CommonUtilities.getOffsetGMT() );
	            String username = ((TangoApplication) appContext).getUser().getUsername();
	            String password = ((TangoApplication) appContext).getUser().getPassword();
	        	String syncTime = WebService.sendWorkOrderDetail ( username, password, syncThisDetail,token );
	        	if ( syncTime != null )
	            {
					Calendar cal = Calendar.getInstance();
					SimpleDateFormat sdf = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss" );
	                // success
	                syncThisDetail.setSyncStatus ( "Y" );

	                syncThisDetail.setSyncTime(sdf.format ( cal.getTime()));
	                WorkorderDetailDAO.updateRecord(appContext, syncThisDetail);
	            }
	            else
	            {
	                // failure
	                //Toast.makeText(getApplicationContext(), "Upload failed!", Toast.LENGTH_SHORT).show();
	            }
	        }
	        catch ( Exception e )
	        {
	            //TODO: standard exception block
	            e.printStackTrace();
	            //Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

	        }
	    }
	    
    	}
    }
    
    
    public static void refreshWorkOrderDetails(Context appContext, String workorderID,String token){
    	
    	if(isOnline(appContext)){
    		
    		syncTheUnsynced(appContext,token);
	    	ArrayList<WorkOrderDetail> detailList = WebService.getWorkOrderDetail(((TangoApplication) appContext).getUser().getUsername(), ((TangoApplication) appContext).getUser().getPassword(), workorderID,token );
	
	        // insert details into local db
	        if ( detailList != null ){
	        	WorkorderDetailDAO.deleteAllSyncedByID(appContext, workorderID);
	            for ( WorkOrderDetail thisDetail : detailList )
	            {
	                //thisDetail.setSyncTime ( "1900-01-01 12:59:00" );
	                thisDetail.setTermicideTypeName_act ( "Unknown" );
	                WorkorderDetailDAO.add ( appContext, thisDetail );
	            }
	        }
	        
        
    	}
        
    }
    
    //fix for open workorder detail without workorder issue
    public static void deleteSyncedOrphanDetails(Context appContext){
    	
    	ArrayList<WorkOrder> workorders =  WorkOrderDAO.getAll(appContext);
    	ArrayList<String> workorderIDs = new ArrayList<String>();
    	
    	for ( WorkOrder workorder : workorders )
        {
    		workorderIDs.add(workorder.getServiceWorkOrderId());
    		
    		
        }
    	   	
    	WorkorderDetailDAO.deleteAllNotSpecified(appContext,workorderIDs);
    	    	   	
    }
    
    
    public static void refreshWorkOrders(Context appContext,String token){
    	
        String username = ((TangoApplication) appContext).getUser().getUsername();
        String password = ((TangoApplication) appContext).getUser().getPassword();
 
    	if(isOnline(appContext)){
        
    		syncTheUnsynced(appContext,token);
	        ArrayList<WorkOrder> workOrderList = WebService.getWorkOrderList ( username,password ,token);
	
	        if ( workOrderList != null )
	        {
				try {
		            // Empty the table before populating
		            WorkOrderDAO.deleteAll ( appContext);
		
		            // insert records into database
		            for ( WorkOrder thisWorkOrder : workOrderList )
		            {
	                    WorkOrderDAO.add ( appContext, thisWorkOrder );
	                    refreshWorkOrderDetails(appContext, thisWorkOrder.getServiceWorkOrderId(),token);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//((TangoApplication) appContext).getUser().updateSyncTimeToCurrent();
	        }
        
    	}
    	
    }


    public static UserLoginResult login(Context appContext, String username, String password, boolean rememberMe){
    	
    	User user = null;
    	
    	if(isOnline(appContext)){
	    	//use webservice	    	
    		UserLoginResult userLoginResult = WebService.getUser(username, password);
    		if(userLoginResult.getToken().equals("0")){
    			return userLoginResult;
			}
			if (userLoginResult.getUser()!=null)
	        {
				user = userLoginResult.getUser();
				user.setRememberMe(rememberMe);
				UserDAO.storeUser(appContext, user);
				((TangoApplication) appContext).setUser(user);
				
				return userLoginResult;
	        }
	        else
	        {
	        	if(userLoginResult.isServiceAvailable()==false){
	        		
	        		//use local db  		
	        		User retrievedUser = UserDAO.getByUsername(appContext, username, password);
	        		userLoginResult.setUser(retrievedUser);
	        		
	    			if (retrievedUser!=null)
	    	        {
	    				user = retrievedUser;
	    				user.setRememberMe(rememberMe);
	    				UserDAO.storeUser(appContext, user);
	    				((TangoApplication) appContext).setUser(user);
	    				return userLoginResult;
	    	        }
	    	        else
	    	        {
	    	        	return userLoginResult;              
	    	        }    	
	        		
	        	}
	        	
	        	return userLoginResult;
	        }
					
    	}else{
    		//use local db  		
    		User retrievedUser = UserDAO.getByUsername(appContext, username, password);
    		UserLoginResult userLoginResult = new UserLoginResult(retrievedUser,false,false);
    		
			if (retrievedUser!=null)
	        {
				user = retrievedUser;
				user.setRememberMe(rememberMe);
				UserDAO.storeUser(appContext, user);
				((TangoApplication) appContext).setUser(user);
				return userLoginResult;
	        }
	        else
	        {
	        	return userLoginResult;              
	        }    		
    		  		
    	}
		
    }
    
    public static String getOffsetGMT(){
    	
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"), Locale.getDefault());
        String   timeZone = new SimpleDateFormat("Z").format(calendar.getTime());
        return timeZone.substring(0, 3) + ":"+ timeZone.substring(3, 5);
    	
    }
    
    public static String getDistanceByInjectionCount(String injectionCount, String lengthMeasurementUnit){
    	
    	if(lengthMeasurementUnit.equalsIgnoreCase("ft")){
    		//ft
    		return Double.toString((Integer.parseInt(injectionCount) * 2));
    	}else{
    		//m
    		return Double.toString(((Integer.parseInt(injectionCount) * 2) * 3.28084));
    	}
    	
    }
    
    public static boolean areAnyWorkordersOpen(Context appContext){
    	
    	ArrayList<WorkOrderDetail> wodList = WorkorderDetailDAO.getLatestEntriesForWorkorders(appContext);
    	for ( WorkOrderDetail thisDetail : wodList ){

    		String personid=((TangoApplication) TangoApplication.getTangoApplicationContext()).getUser().getPersonID();
    		String ss=thisDetail.getTechnicianPersonID();

			ArrayList<WorkOrder> w=WorkOrderDAO.getAll(appContext);
			for(WorkOrder wor:w) {
				if (wor.getServiceWorkOrderId().equals(thisDetail.getServiceWorkorderID())) {
                    String status="N";
                     if(((TangoApplication) TangoApplication.getTangoApplicationContext()).getIsTrainingMode()){
                     	status="Y";
					 }
					 if(status.equals(wor.getDemo()))

                     if (thisDetail.getWorkorderStatusCode().equalsIgnoreCase(WorkorderStatus.OPEN)) {
						return true;
					}
				}
			}
		}  	
    	return false;
    	
    }
    
    public static boolean isThisWorkorderOpen(Context appContext, String workorderID){
    	
    	WorkOrderDetail wod = WorkorderDetailDAO.getLatestByWorkOrderID(appContext, workorderID); 
    	if(wod==null){
    		return false;
    	}
    	
    	if(wod.getWorkorderStatusCode().equalsIgnoreCase(WorkorderStatus.OPEN)){
    		return true;
    	}else{
    		return false;
    	}
    	
    }
    public static String getStatus(String number,String token){
    	String s=WebService.getStatus(number,token);
    	return s;
	}
    public static String getWorkOrderStatus(Context appContext, String workorderID){
    	
    	WorkOrderDetail wod = WorkorderDetailDAO.getLatestByWorkOrderID(appContext, workorderID); 
    	if(wod==null){
    		return "";
    	}
    	
    	if(wod.getWorkorderStatusCode().equalsIgnoreCase(WorkorderStatus.OPEN)){
    		return "OPEN";
    	}else if(wod.getWorkorderStatusCode().equalsIgnoreCase(WorkorderStatus.ONHOLD)){
    		return "ON HOLD";
    	}else if(wod.getWorkorderStatusCode().equalsIgnoreCase(WorkorderStatus.ASSIGNED)){
    		return "ASSIGNED";
    	}else{
    		return "COMPLETED";
    	}
    	
    }
    
    public static boolean isNewVersionAvailable ( Context appContext )
    {
        boolean returnValue = false;

        if ( isOnline ( appContext ) )
        {
            try
            {
                String appVersionName = appContext.getPackageManager().getPackageInfo(appContext.getPackageName(), 0).versionName;
                User thisUser = ((TangoApplication) appContext).getUser();

                double serverVersionNumber = Double.parseDouble(thisUser.getMobileAppVersion());
                double appVersionNumber = Double.parseDouble(appVersionName);
                
                // Just in case the User object becomes null, this short circuits the error
                if ( thisUser != null && thisUser.getMobileAppVersion() != null && appVersionNumber < serverVersionNumber )
                {
                    returnValue = true;
                }
            }
            catch ( NameNotFoundException nnfe )
            {
                ; /* do nothing, returnValue is already false */
            }
        }

        return returnValue;
    }

    public static void loadAuthorizedInjectionStations ( Context appContext,String token )
    {
        if ( isOnline ( appContext ) )
        {
            InjectionStationDAO.deleteAll ( appContext );
            ArrayList<InjectionStation> stationList = CommonUtilities.getAllowedInjectionStations( appContext,token );
            if(stationList != null){
            	for ( InjectionStation thisStation : stationList )
            	{
                	InjectionStationDAO.add ( appContext, thisStation );
            	}
            }
        }
    }

    public static void checkAppVersion(Context appContext){
    	
    	if(!isOnline(appContext)){
    		return;
    	}
    	
    	try {
			String appVersionName = appContext.getPackageManager().getPackageInfo(appContext.getPackageName(), 0).versionName;
			String serverVersionName = ((TangoApplication) appContext).getUser().getMobileAppVersion();
			
			
			if(!appVersionName.equalsIgnoreCase(serverVersionName)){
				
				Log.i("appVersion","wrong version");
				
				try {
					
				    Intent i = new Intent("android.intent.action.MAIN");
				    i.setComponent(ComponentName.unflattenFromString("com.android.chrome/com.android.chrome.Main"));
				    i.addCategory("android.intent.category.LAUNCHER");
				    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				    i.setData(Uri.parse("http://10.50.16.153/LoginActivity.apk"));
				    appContext.startActivity(i);
					
				}
				catch(ActivityNotFoundException e) {
				    // Chrome is probably not installed
				}
				
				
			}else{
				Log.i("appVersion","right version");
			}
    	
    	
    	} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
    
    
    public static void checkForNewDetails(Context appContext,String token){
    	if(isOnline(appContext)){
	    	//syncTheUnsynced(appContext);
	    		
	    	ArrayList<WorkOrder> workorders =  WorkOrderDAO.getAll(appContext);
	    	
	    	String username = ((TangoApplication) appContext).getUser().getUsername();
	    	String password = ((TangoApplication) appContext).getUser().getPassword();
            String syncTime = WorkorderDetailDAO.getLatestWorkOrderDetailSyncTime(appContext);
            if ( syncTime == null )
            {
                syncTime = "1970-01-01 00:00:00";
            }
	    	ArrayList<WorkOrderDetail> wodList = WebService.getWorkOrderDetail(username, password, workorders, syncTime,token);
	    	if(wodList != null){
	    		for(WorkOrderDetail wod : wodList){   
	    			//wod.setSyncTime ( "1900-01-01 12:59:00" );
	    			wod.setTermicideTypeName_act ( "Unknown" );
	    			WorkorderDetailDAO.add(appContext, wod);    		
	    		}
	    	}
	    	
	    	//((TangoApplication) appContext).getUser().updateSyncTimeToCurrent();
	    	syncTheUnsynced(appContext,token);
    	}
    	
    }
    

    
    public static boolean checkIfWorkorderChanges(Context appContext,String token){
    	
    	deleteSyncedOrphanDetails(appContext);
    	
    	if(!isOnline(appContext)){
    		return false;
    	}
    	/*if(((TangoApplication) appContext).getUser().getLastSyncTime() == null){
    		return true;
    	}*/
    	
    	ArrayList<WorkOrderBrief> workOrderBriefs = WebService.getWorkOrderBriefs(((TangoApplication) appContext).getUser().getUsername(), ((TangoApplication) appContext).getUser().getPassword(),token);
    	ArrayList<WorkOrder> workorders =  WorkOrderDAO.getAll(appContext);
    	
    	if(workOrderBriefs==null){
    		return false;
    	}
    	
    	if(workOrderBriefs.size() != workorders.size()){
    		return true;
    	}
    	
    	
    	for(WorkOrderBrief wob : workOrderBriefs){  
    		boolean isFound = false;
    		for(WorkOrder wo: workorders){   	   			
    			if(wob.getWorkorderID().equalsIgnoreCase(wo.getServiceWorkOrderId())){   
    				isFound = true;
    				if(!(wob.getLastSyncTime().equalsIgnoreCase(wo.getSyncTime()))){
    					return true;
    				}    				    				
    			}   			
    		}	
			if(!isFound){
				return true;
			}
    	}
    	    	
    	return false;
    	  	
    }

    public static boolean isUnitAuthorized ( Context appContext, String macAddress, String personID )
    {
        boolean returnValue = false;

        if ( macAddress != null )
        {
            // get injectionStationID from macAddress
            String thisStationID = InjectionStationDAO.getID ( appContext, macAddress );

            // We only load assigned stations to the m_InjectionStation table, so no need to check m_AssignInjectionStation
            if ( thisStationID != null )
            {
                returnValue = true;
            }
        }

        return returnValue;
    }
    
    public static ArrayList<InjectionStation> getAllowedInjectionStations(Context appContext,String token)
    {
        return WebService.getAllowedInjectionStations(((TangoApplication) appContext).getUser().getUsername(), ((TangoApplication) appContext).getUser().getPassword(),token);
    }
    
    
    public static void refreshTermicideTypes(Context appContext,String token)
    {
    	if ( isOnline ( appContext ) )
    	{
    		TermicideTypeDAO.deleteAll(appContext);
    	
	    	ArrayList<TermicideType> types = WebService.getTermicideTypes(((TangoApplication) appContext).getUser().getUsername(), ((TangoApplication) appContext).getUser().getPassword(),token);
	    	for(TermicideType type : types){  
	    		TermicideTypeDAO.add(appContext, type);   		
	    	}
    	}
    	 	
    }
    
    public static TermicideType getTermicideTypeByCode(Context appContext, String termicideTypeCode){ 	
    	return TermicideTypeDAO.get(appContext, termicideTypeCode);   	
    }
    
    public static TermicideType getTermicideTypeByName(Context appContext, String termicideTypeName){ 	
    	return TermicideTypeDAO.getByName(appContext, termicideTypeName);   	
    }
    
    public static boolean isUsernameInLocalDB(Context appContext, String username){
    	
    	User user = UserDAO.getByUsername(appContext, username);
    	if(user == null){
    		return false;
    	}else{
    		return true;
    	}
    	
    }
    
    
}
