package com.evanhoe.tango.objs;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import com.evanhoe.tango.utils.CommonUtilities;

public class User
{
	private String username;
	private String password;
	private String personID;
	private String UserRoleCode;
	private String UserSubRoleCode;
	private String GroupID;
	private String GroupName;
	private String FirstName;
	private String LastName;
	private boolean canDoTraining;
	private String remoteValidationRequired;
	private String mobileAppVersion;
	private boolean rememberMe;
	
	/*private String lastSyncTime;
	
	public String getLastSyncTime() {
		return lastSyncTime;
		//return "2014-11-05 12:38:24 -05:00";
	}

	public void setLastSyncTime(String lastSyncTime) {
		this.lastSyncTime = lastSyncTime;
	}
		
	public void updateSyncTimeToCurrent(){
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.SECOND, 1);
        SimpleDateFormat sdf = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss" );
        setLastSyncTime( sdf.format ( cal.getTime() ) + " " + CommonUtilities.getOffsetGMT()  );
	}*/

	public void setCanDoTrainingFromDB(String YorN){
		
		if(YorN.equalsIgnoreCase("Y")){
			canDoTraining = true;
		}else{
			canDoTraining = false;
		}
		
	}
	
	public void setRememberMeFromDB(String YorN)
	{
		if(YorN.equalsIgnoreCase("Y"))
		{
			rememberMe = true;
		}
		else
		{
			rememberMe = false;
		}			
	}
	public String getCanDoTrainingFromDB(){
		
		if(canDoTraining == true){
			return "Y";
		}else{
			return "N";
		}
		
	}
	
	public String getRememberMeString()
	{
		if (rememberMe)
		{
			return "Y";
		}
		else
		{
			return "N";
		}
	}
	
	public String getMobileAppVersion() {
		return mobileAppVersion;
	}

	public void setMobileAppVersion(String mobileAppVersion) {
		this.mobileAppVersion = mobileAppVersion;
	}
	


	public String getUsername() {

		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPersonID() {
		return personID;
	}

	public void setPersonID(String personID) {
		this.personID = personID;
	}

	public String getUserRoleCode() {
		return UserRoleCode;
	}

	public void setUserRoleCode(String userRoleCode) {
		UserRoleCode = userRoleCode;
	}

	public String getUserSubRoleCode() {
		return UserSubRoleCode;
	}

	public void setUserSubRoleCode(String userSubRoleCode) {
		UserSubRoleCode = userSubRoleCode;
	}

	public String getGroupID() {
		return GroupID;
	}

	public void setGroupID(String groupID) {
		GroupID = groupID;
	}

	public String getGroupName() {
		return GroupName;
	}

	public void setGroupName(String groupName) {
		GroupName = groupName;
	}

	public String getFirstName() {
		return FirstName;
	}

	public void setFirstName(String firstName) {
		FirstName = firstName;
	}

	public String getLastName() {
		return LastName;
	}

	public void setLastName(String lastName) {
		LastName = lastName;
	}



	public boolean canDoTraining() {
		return canDoTraining;
	}

	public void setCanDoTraining(boolean canDoTraining) {
		this.canDoTraining = canDoTraining;
	}

	public String getRemoteValidationRequired() {
		return remoteValidationRequired;
	}

	public void setRemoteValidationRequired(String remoteValidationRequired) {
		this.remoteValidationRequired = remoteValidationRequired;
	}

	public boolean getRememberMe() {
		return rememberMe;
	}
	
	public void setRememberMe(boolean rememberMe) {
		this.rememberMe = rememberMe;
	}
	
	public User(String username, String password, String personID,
			String userRoleCode, String userSubRoleCode, String groupID,
			String groupName, String firstName, String lastName,
			String canDoTraining, String remoteValidationRequired, String rememberMe) {
		super();
		this.username = username;
		this.password = password;
		this.personID = personID;
		UserRoleCode = userRoleCode;
		UserSubRoleCode = userSubRoleCode;
		GroupID = groupID;
		GroupName = groupName;
		FirstName = firstName;
		LastName = lastName;
		//this.canDoTraining = canDoTraining;
		setCanDoTrainingFromDB(canDoTraining);
		this.remoteValidationRequired = remoteValidationRequired;
		if (remoteValidationRequired.equals("Y")) 
		{
			setRememberMeFromDB("N");
		}
		else
		{
			setRememberMeFromDB(rememberMe);
		}
	}
	
	   /**
     * Constructor for a User. Takes a JSON string of attributes as parameter.
     * @param JSON
     */
    public User(JSONObject j,String username,String password){
        super();
        
        try {
        	//this.username = j.getString ( "username" );
        	//this.password = j.getString ( "password" );
			this.username=username;
			this.password=password;
    		this.personID = j.getString ( "PersonID" );
    		this.UserRoleCode = j.getString ( "UserRoleCode" );
    		this.UserSubRoleCode = j.getString ( "UserSubRoleCode" );
    		this.GroupID = j.getString ( "GroupID" );
    		this.GroupName = j.getString ( "GroupName" );
    		this.FirstName = j.getString ( "FirstName" );
    		this.LastName = j.getString ( "LastName" );
    		//this.canDoTraining = j.getString ( "CanCompleteTrainingSWO" );
    		setCanDoTrainingFromDB(j.getString ( "CanCompleteTrainingSWO" ));
    		this.remoteValidationRequired = j.getString ( "RemoteValidationRequired" );
    		this.mobileAppVersion = j.getString("CurrentMobileAppVersionNumber");

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
}


