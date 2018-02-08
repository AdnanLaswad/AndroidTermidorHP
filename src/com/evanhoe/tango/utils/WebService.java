package com.evanhoe.tango.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.evanhoe.tango.TangoApplication;
import com.evanhoe.tango.R;
import com.evanhoe.tango.objs.InjectionStation;
import com.evanhoe.tango.objs.PersonInfo;
import com.evanhoe.tango.objs.TermicideType;
import com.evanhoe.tango.objs.User;
import com.evanhoe.tango.objs.UserLoginResult;
import com.evanhoe.tango.objs.WorkOrder;
import com.evanhoe.tango.objs.WorkOrderBrief;
import com.evanhoe.tango.objs.WorkOrderDetail;

public class WebService {

	//public final static String mainurl="https://basf.datacore.us/Pest/HPServices";

	public final static String stageurl="https://basf.datacore.us/Pest/HPServices";
	//public final static String producturl="https://basf.datacore.us/Pest/HPServices";
	public final static String producturl="https://termidorhpdataservices.com/hpservices";
	public static String application=producturl;


	private final static String devUrlBase = TangoApplication.getResourcesStatic().getString ( R.string.dev_webservice_url );
	private final static String urlBase = TangoApplication.getResourcesStatic().getString ( R.string.preprod_webservice_url );
	private final static String prodUrlBase = TangoApplication.getResourcesStatic().getString ( R.string.prod_webservice_url );

	/**
	 * returns the full URL of the Web Service Call
	 * @param webServiceName
	 */
	private static String getURL ( String webServiceName )
	{

		//String returnString = "http://" + urlBase + "/" + webServiceName + "/";
		String returnString = urlBase + "/" + webServiceName + "/";

		//if(isProd){
		if(CommonUtilities.getEnv() == 3){
			returnString = prodUrlBase + "/" + webServiceName + "/";
		}else if(CommonUtilities.getEnv() == 1){
			returnString = devUrlBase + "/" + webServiceName + "/";
		}

		return returnString;
	}


	public static String getToken(String username, String password){

		SharedPreferences sharedPreferences = TangoApplication.getTangoApplicationContext().getSharedPreferences(
				"url", Context.MODE_PRIVATE);
		String selecturl=sharedPreferences.getString("urltype","");
		if(selecturl.contains("stage")){
			application=stageurl;
		}
		else{
			application=producturl;
		}

		try {
			JSONObject postDataParams = new JSONObject();

			postDataParams.put("password", password);
			postDataParams.put("username", username);
			postDataParams.put("grant_type", "password");
			String strResponseJSON1 = HttpUtils.sendGet(application+"/oauth/token", postDataParams);


			JSONObject jj;
			jj = new JSONObject(strResponseJSON1);
			if (jj.getString("access_token").length() > 0) {
				// System.out.print(j.getString("access_token"));

				String token = jj.getString("access_token");
				return token;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "";

	}
	/**
	 * check if a username and password are valid
	 * @param username
	 * @param password
	 * @return boolean
	 */
	public static UserLoginResult getUser(String username, String password) {
		String token="";

		SharedPreferences sharedPreferences = TangoApplication.getTangoApplicationContext().getSharedPreferences(
				"url", Context.MODE_PRIVATE);
		String selecturl=sharedPreferences.getString("urltype","");
		if(selecturl.contains("stage")){
			application=stageurl;
		}
		else{
			application=producturl;
		}
		UserLoginResult userLoginResult = null;

		try {
			JSONObject postDataParams = new JSONObject();

			postDataParams.put("password", password);
			postDataParams.put("username", username);
			postDataParams.put("grant_type","password");
			String strResponseJSON1 = HttpUtils.sendGet(application+"/oauth/token",postDataParams);
			if(strResponseJSON1.contains("400")){
				userLoginResult = new UserLoginResult(null,true,true,token);       // handel the 400 & 503 & 500 status code
				return userLoginResult;
			}
			if(strResponseJSON1.contains("503")){
				userLoginResult = new UserLoginResult(null,false,true,token);
				return userLoginResult;
			}
			if(strResponseJSON1.contains("500")){
				userLoginResult = new UserLoginResult(null,false,true,token);
				return userLoginResult;
			}

			JSONObject jj;
			jj = new JSONObject(strResponseJSON1);
			if(jj.getString("access_token").length()>0) {
				// System.out.print(j.getString("access_token"));

				token=jj.getString("access_token");


				String url=application+"/Person?UserLoginName="+""+username;
				String strResponseJSON2 = HttpUtils.sendGet1(url,jj.getString("access_token"));
				JSONObject j=new JSONObject(strResponseJSON2);





				//	User user1 = new User(username,password,personID,UserRoleCode,UserSubRoleCode,GroupID,GroupName,FirstName,LastName,canDoTraining,remoteValidationRequired,mobileAppVersion);
				// The Mobile app is now set via User/JSON constructor.
				//user.setMobileAppVersion(j.getString("CurrentMobileAppVersionNumber"));
				User user=new User(j,username,password);
				userLoginResult = new UserLoginResult(user, true, true,token);
				return userLoginResult;
			}
			else{
				userLoginResult = new UserLoginResult(null, true, true,token);
				return userLoginResult;
			}

	  /*    String strPostJSON = "{\"username\":\"" + username
					+ "\",\"password\":\"" + password + "\"}";

			//changed login -> login2 for 2nd iteration of app
			String strUrl = getURL ( "login2" );
			String strResponseJSON = HttpUtils.sendPost(strUrl, strPostJSON);

			JSONObject j;
			j = new JSONObject(strResponseJSON);

			if (j.getString("status").equalsIgnoreCase("1")
					&& j.getString("userAuth").equalsIgnoreCase("1")) {
				j.put("username", username);
				j.put("password", password);
				// Sending the JSON into the User() constructor class moves all fields
				// from JSON into the new User Value Object (VO).
				User user = new User(j,username,password);
				// The Mobile app is now set via User/JSON constructor.
				//user.setMobileAppVersion(j.getString("CurrentMobileAppVersionNumber"));

				userLoginResult = new UserLoginResult(user, true, true);

				return userLoginResult;*/


		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			//	userLoginResult = new UserLoginResult(null,false,true,token);
			//	return userLoginResult;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			userLoginResult = new UserLoginResult(null,false,true,token);
			return userLoginResult;
		} catch (RuntimeException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
			if(e.getMessage().contains("400"))
				userLoginResult = new UserLoginResult(null,true,true,token);
			else
				userLoginResult = new UserLoginResult(null,false,true,token);
			return userLoginResult;
		}
		catch (Exception e) {
			e.printStackTrace();
			userLoginResult = new UserLoginResult(null,false,true,token);

			return userLoginResult;

		}



		userLoginResult = new UserLoginResult(null,false,true,token);
		return userLoginResult;

	}

	/**
	 * get an arraylist of workorders by username
	 * @param username
	 * @return ArrayList<WorkOrder>
	 */
	public static ArrayList<WorkOrder> getWorkOrderList(String username, String password,String token) {

		SharedPreferences sharedPreferences = TangoApplication.getTangoApplicationContext().getSharedPreferences(
				"url", Context.MODE_PRIVATE);
		String selecturl=sharedPreferences.getString("urltype","");
		if(selecturl.contains("stage")){
			application=stageurl;
		}
		else{
			application=producturl;
		}
		ArrayList<WorkOrder> workOrders = new ArrayList<WorkOrder>();
		try{

			String url=application+"/WorkOrders";
			String strResponseJSON2 = HttpUtils.sendGet1(url,token);
			JSONArray jsonperson1 = new JSONArray(strResponseJSON2);
			for(int i=0;i<jsonperson1.length();i++) {
				WorkOrder workOrder = new WorkOrder(jsonperson1.getJSONObject(i));
				workOrders.add(workOrder);

			}

/*
		JSONObject jpost = new JSONObject();
		try {
			jpost.put("username", username);
			jpost.put("password", password);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		ArrayList<WorkOrder> workOrders = new ArrayList<WorkOrder>();

		String strJSON = "";
		try {
			strJSON = HttpUtils
					.sendPost( getURL("work_orders"), jpost.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (RuntimeException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
			return null;
		}

		JSONObject j;
		try {
			j = new JSONObject(strJSON);

			String workOrderCount = j.getString("workOrderCount");

			JSONArray arrJSON = j.getJSONArray("workOrderEntries");

			for (int i = 0; i < arrJSON.length(); i++) {
				WorkOrder workOrder = new WorkOrder(arrJSON.getJSONObject(i));

				/*String syncTime = arrJSON.getJSONObject(i).getJSONObject("LastSyncTime").getString("date")+ " " + arrJSON.getJSONObject(i).getJSONObject("LastSyncTime").getString("timezone");
				workOrder.setSyncTime(syncTime);*/

			//workOrders.add(workOrder);
			return  workOrders;

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return workOrders;

	}


	/**
	 * get an arraylist of workorder details by workorder number
	 * @param woNumber
	 * @return ArrayList<WorkOrderDetail
	 */
	public static ArrayList<WorkOrderDetail> getWorkOrderDetail(String username, String password, String number,String token) {


		SharedPreferences sharedPreferences = TangoApplication.getTangoApplicationContext().getSharedPreferences(
				"url", Context.MODE_PRIVATE);
		String selecturl=sharedPreferences.getString("urltype","");
		if(selecturl.contains("stage")){
			application=stageurl;
		}
		else{
			application=producturl;
		}
		ArrayList<WorkOrderDetail> workOrderDetails = new ArrayList<WorkOrderDetail>();
/*
		JSONObject jpost = new JSONObject();
		try {
			jpost.put("username", username);
			jpost.put("password", password);
			jpost.put("wo_number", woNumber);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		ArrayList<WorkOrderDetail> workOrderDetails = new ArrayList<WorkOrderDetail>();
		String strJSON = "";

		try {
			strJSON = HttpUtils
					.sendPost( getURL("wo_details"), jpost.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (RuntimeException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
			return null;
		}*/


		JSONObject j;
		//JSONObject j2,j3;
		try {
			String url=application+"/WorkOrderDetail?WO_Number="+number;
			String strResponseJSON2 = HttpUtils.sendGet1(url,token);
			//JSONObject jj=new JSONObject(strResponseJSON2);
			j = new JSONObject(strResponseJSON2);

			//	String workOrderCount = j.getString("workOrderDetailCount");

			//JSONArray arrJSON = j.getJSONArray("workOrderDetailEntries");

			//for (int i = 0; i < arrJSON.length(); i++) {
			WorkOrderDetail woDetail = new WorkOrderDetail(j);
			woDetail.setSyncStatus ( "Y" );
				/*j2 = new JSONObject(woDetail.getEntryTime());
				woDetail.setEntryTime(j2.getString("date") + " " + j2.getString("timezone"));
				j3 = new JSONObject(woDetail.getSyncTime());
				woDetail.setSyncTime(j3.getString("date") + " " + j3.getString("timezone"));	*/
			workOrderDetails.add(woDetail);

			//}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return workOrderDetails;


	}

	public static String getStatus( String number,String token) {
		String status="";


		SharedPreferences sharedPreferences = TangoApplication.getTangoApplicationContext().getSharedPreferences(
				"url", Context.MODE_PRIVATE);
		String selecturl=sharedPreferences.getString("urltype","");
		if(selecturl.contains("stage")){
			application=stageurl;
		}
		else{
			application=producturl;
		}

		try {
			String url=application+"/WorkOrderDetail?WO_Number="+number;
			String strResponseJSON2 = HttpUtils.sendGet1(url,token);
			JSONObject jj = new JSONObject(strResponseJSON2);
			if(number.equals(jj.getString("ServiceWorkorderID"))) {
				status = jj.getString("WorkorderStatusCode");
			}
			return  status;
		} catch (JSONException e) {
			e.printStackTrace();
			return e.getMessage();
		} catch (Exception e) {
			//return e.getMessage();
			e.printStackTrace();
			return e.getMessage();
		}


		//return "";


	}

	/**
	 * send a workorder detail record to the webservice, where it will attempt to insert it into the database
	 * @param woDetail
	 * @return boolean
	 */
	public static String sendWorkOrderDetail(String username, String password, WorkOrderDetail woDetail,String token) {


		SharedPreferences sharedPreferences = TangoApplication.getTangoApplicationContext().getSharedPreferences(
				"url", Context.MODE_PRIVATE);
		String selecturl=sharedPreferences.getString("urltype","");
		if(selecturl.contains("stage")){
			application=stageurl;
		}
		else{
			application=producturl;
		}
		try {


			JSONObject postJSON = woDetail.toJSONObject();
			//postJSON.put("username", username);
			//postJSON.put("password", password);

			String strPostJSON = postJSON.toString();
// TODO: Remove this debugging
			//  System.out.println ( strPostJSON );

			String url=application+"/SaveWorkOrderDetail";
			String strResponseJSON = HttpUtils.saveData(url,postJSON,token);
			return "yes";
/*
			JSONObject j;
			j = new JSONObject(strResponseJSON);

			if (j.getString("status").equalsIgnoreCase("1")
					&& j.getString("entry").equalsIgnoreCase("1")) {
				return j.getString("LastSyncTime");
			}*/


		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (RuntimeException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}


		return null;

	}

	/**
	 * returns workorder details that match workorders and have later entry time into server database than syncTime
	 * @param username
	 * @param password
	 * @param workorders
	 * @param syncTime
	 * @return ArrayList<WorkOrderDetail>
	 */
	public static ArrayList<WorkOrderDetail> getWorkOrderDetail(String username, String password, ArrayList<WorkOrder> workorders,String syncTime,String token) {


		SharedPreferences sharedPreferences = TangoApplication.getTangoApplicationContext().getSharedPreferences(
				"url", Context.MODE_PRIVATE);
		String selecturl=sharedPreferences.getString("urltype","");
		if(selecturl.contains("stage")){
			application=stageurl;
		}
		else{
			application=producturl;
		}
		ArrayList<WorkOrderDetail> workOrderDetails = new ArrayList<WorkOrderDetail>();
		try {
			String number="";

			JSONObject j = new JSONObject();
			j.put("username", username);
			j.put("password", password);

			JSONArray ja = new JSONArray();
			for(int i=0;i < workorders.size();i++){
				JSONObject j2 = new JSONObject();
				j2.put("wo_number", workorders.get(i).getServiceWorkOrderId());
				ja.put(i, j2);
				number=workorders.get(i).getServiceWorkOrderId();
			}
			j.put("wo_numbers", ja);
			j.put("sync_time", syncTime);



			String url=application+"/WorkOrderDetailAfterTime?WO_Number="+number+"&SyncTime="+syncTime;
			String strResponseJSON2 = HttpUtils.sendGet1(url,token);
			JSONObject jj=new JSONObject(strResponseJSON2);
			/*
			String strUrl = getURL ( "aftertime_wo_details" );
			String strResponseJSON = HttpUtils.sendPost(strUrl, j.toString());

			JSONObject jResponse, jResponse2, jResponse3;
			jResponse = new JSONObject(strResponseJSON);

			String workOrderCount = jResponse.getString("workOrderDetailCount");
			*/
			JSONArray arrJSON = new JSONArray(jj);


			for (int i = 0; i < arrJSON.length(); i++) {
				WorkOrderDetail woDetail = new WorkOrderDetail(arrJSON.getJSONObject(i));
				woDetail.setSyncStatus ( "Y" );
				/*jResponse2 = new JSONObject(woDetail.getEntryTime());
				woDetail.setEntryTime(jResponse2.getString("date") + " " + jResponse2.getString("timezone"));
				jResponse3 = new JSONObject(woDetail.getSyncTime());
				woDetail.setSyncTime(jResponse3.getString("date") + " " + jResponse3.getString("timezone"));*/
				workOrderDetails.add(woDetail);
			}
			return workOrderDetails;

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (RuntimeException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return workOrderDetails;
	}


	public static ArrayList<WorkOrderBrief> getWorkOrderBriefs(String username, String password,String token) {


		SharedPreferences sharedPreferences = TangoApplication.getTangoApplicationContext().getSharedPreferences(
				"url", Context.MODE_PRIVATE);
		String selecturl=sharedPreferences.getString("urltype","");
		if(selecturl.contains("stage")){
			application=stageurl;
		}
		else{
			application=producturl;
		}
		ArrayList<WorkOrderBrief> workOrderBriefs = new ArrayList<WorkOrderBrief>();




		try{

			String url=application+"/WorkOrders";
			String strResponseJSON2 = HttpUtils.sendGet1(url,token);
			JSONArray jj = new JSONArray(strResponseJSON2);
			for(int i=0;i<jj.length();i++) {
				JSONObject j2=jj.getJSONObject(i);
				WorkOrderBrief wob = new WorkOrderBrief(j2.optString("LastSyncTime"),j2.optString("ServiceWorkorderID"));
				workOrderBriefs.add(wob);

			}




		/*
		String number="";
		JSONObject j = new JSONObject();
		try {
			j.put("username", username);
			j.put("password", password);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();


		String strJSON = "";
		try {
			strJSON = HttpUtils
					.sendPost( getURL("work_order_ids"), j.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (RuntimeException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
			return null;
		}

		JSONObject jResponse;
		try {
			j = new JSONObject(strJSON);

			String workOrderCount = j.getString("workOrderCount");

			JSONArray arrJSON = j.getJSONArray("workOrderEntries");

			for (int i = 0; i < arrJSON.length(); i++) {
				//WorkOrder workOrder = new WorkOrder(arrJSON.getJSONObject(i));
				JSONObject jResponse2 = arrJSON.getJSONObject(i);
				//jResponse2.get("LastSyncTime");
				//jResponse2.get("ServiceWorkorderID");

				/*JSONObject jResponse3 = new JSONObject(jResponse2.get("LastSyncTime").toString());
				WorkOrderBrief wob = new WorkOrderBrief(jResponse3.getString("date") + " " + jResponse3.getString("timezone"),jResponse2.get("ServiceWorkorderID").toString());*/

				/*WorkOrderBrief wob = new WorkOrderBrief(jResponse2.get("LastSyncTime").toString(),jResponse2.get("ServiceWorkorderID").toString());
				workOrderBriefs.add(wob);*/

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return workOrderBriefs;
	}

	public static ArrayList<InjectionStation> getAllowedInjectionStations(String username, String password,String token){

		SharedPreferences sharedPreferences = TangoApplication.getTangoApplicationContext().getSharedPreferences(
				"url", Context.MODE_PRIVATE);
		String selecturl=sharedPreferences.getString("urltype","");
		if(selecturl.contains("stage")){
			application=stageurl;
		}
		else{
			application=producturl;
		}

		ArrayList<InjectionStation> allowedInjStations = new ArrayList<InjectionStation>();
		String url=application+"/InjectionStations";
		String strResponseJSON2 = null;
		try {
			strResponseJSON2 = HttpUtils.sendGet1(url,token);
			//	JSONObject j=new JSONObject(strResponseJSON2);
			JSONArray arrJSON = new JSONArray(strResponseJSON2);
			for (int i = 0; i < arrJSON.length(); i++) {
				//WorkOrder workOrder = new WorkOrder(arrJSON.getJSONObject(i));
				JSONObject jResponse2 = arrJSON.getJSONObject(i);
				//jResponse2.get("LastSyncTime");
				//jResponse2.get("ServiceWorkorderID");

				/*JSONObject jResponse3 = new JSONObject(jResponse2.get("LastSyncTime").toString());
				WorkOrderBrief wob = new WorkOrderBrief(jResponse3.getString("date") + " " + jResponse3.getString("timezone"),jResponse2.get("ServiceWorkorderID").toString());*/

				InjectionStation injStation = new InjectionStation(jResponse2.get("InjectionStationID").toString(),jResponse2.get("IPAddress").toString(),jResponse2.get("ControllerIPAddress").toString(),jResponse2.get("InjectionStationName").toString());

				allowedInjStations.add(injStation);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}










/*
ArrayList<InjectionStation> allowedInjStations = new ArrayList<InjectionStation>();

		JSONObject j = new JSONObject();
		try {
			j.put("username", username);
			j.put("password", password);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		String strJSON = "";
		try {
			strJSON = HttpUtils
					.sendPost( getURL("injection_stations"), j.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (RuntimeException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
			return null;
		}

		JSONObject jResponse;
		try {
			j = new JSONObject(strJSON);

			JSONArray arrJSON = j.getJSONArray("injectionStations");

			for (int i = 0; i < arrJSON.length(); i++) {
				//WorkOrder workOrder = new WorkOrder(arrJSON.getJSONObject(i));
				JSONObject jResponse2 = arrJSON.getJSONObject(i);
				//jResponse2.get("LastSyncTime");
				//jResponse2.get("ServiceWorkorderID");

				/*JSONObject jResponse3 = new JSONObject(jResponse2.get("LastSyncTime").toString());
				WorkOrderBrief wob = new WorkOrderBrief(jResponse3.getString("date") + " " + jResponse3.getString("timezone"),jResponse2.get("ServiceWorkorderID").toString());*/

				/*InjectionStation injStation = new InjectionStation(jResponse2.get("InjectionStationID").toString(),jResponse2.get("IPAddress").toString(),jResponse2.get("ControllerIPAddress").toString(),jResponse2.get("InjectionStationName").toString());

				allowedInjStations.add(injStation);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}*/

		return allowedInjStations;


	}

	public static ArrayList<TermicideType> getTermicideTypes(String username, String password,String token){

		SharedPreferences sharedPreferences = TangoApplication.getTangoApplicationContext().getSharedPreferences(
				"url", Context.MODE_PRIVATE);
		String selecturl=sharedPreferences.getString("urltype","");
		if(selecturl.contains("stage")){
			application=stageurl;
		}
		else{
			application=producturl;
		}
		ArrayList<TermicideType> termicideTypes = new ArrayList<TermicideType>();

		String url=application+"/TermicideTypes";
		String strResponseJSON2 = null;
		try {
			strResponseJSON2 = HttpUtils.sendGet1(url,token);
			//	JSONObject j=new JSONObject(strResponseJSON2);
			JSONArray arrJSON = new JSONArray(strResponseJSON2);
			for (int i = 0; i < arrJSON.length(); i++) {
				//WorkOrder workOrder = new WorkOrder(arrJSON.getJSONObject(i));
				JSONObject jResponse2 = arrJSON.getJSONObject(i);
				//jResponse2.get("LastSyncTime");
				//jResponse2.get("ServiceWorkorderID");

				/*JSONObject jResponse3 = new JSONObject(jResponse2.get("LastSyncTime").toString());
				WorkOrderBrief wob = new WorkOrderBrief(jResponse3.getString("date") + " " + jResponse3.getString("timezone"),jResponse2.get("ServiceWorkorderID").toString());*/
				TermicideType type = new TermicideType(jResponse2.get("TermicideTypeCode").toString(),jResponse2.get("TermicideTypeName").toString(),Double.parseDouble(jResponse2.get("DilutionRatio").toString()),Double.parseDouble(jResponse2.get("InjectionCountFactor").toString()));

				termicideTypes.add(type);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}


				/*
								JSONObject j = new JSONObject();
				try {
					j.put("username", username);
					j.put("password", password);
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				String strJSON = "";
				try {
					strJSON = HttpUtils
							.sendPost( getURL("termicide_type_lookup"), j.toString());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return null;
				} catch (RuntimeException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
					return null;
				}

				JSONObject jResponse;
				try {
					j = new JSONObject(strJSON);

					JSONArray arrJSON = j.getJSONArray("TermicideTypes");

					for (int i = 0; i < arrJSON.length(); i++) {
						//WorkOrder workOrder = new WorkOrder(arrJSON.getJSONObject(i));
						JSONObject jResponse2 = arrJSON.getJSONObject(i);
						//jResponse2.get("LastSyncTime");
						//jResponse2.get("ServiceWorkorderID");

						/*JSONObject jResponse3 = new JSONObject(jResponse2.get("LastSyncTime").toString());
						WorkOrderBrief wob = new WorkOrderBrief(jResponse3.getString("date") + " " + jResponse3.getString("timezone"),jResponse2.get("ServiceWorkorderID").toString());*/

					/*	TermicideType type = new TermicideType(jResponse2.get("TermicideTypeCode").toString(),jResponse2.get("TermicideTypeName").toString(),Double.parseDouble(jResponse2.get("DilutionRatio").toString()),Double.parseDouble(jResponse2.get("InjectionCountFactor").toString()));

						termicideTypes.add(type);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return null;
				}*/

		return termicideTypes;


	}


/*	public static String getLatestVersionNumber(){

		String versionNumber = null;

		String strJSON = "";
		try {
			strJSON = HttpUtils
					.httpGet("http://" + urlBase + "/version/"
							+ "BTG");
			//TODO add viewportcode field to m_useraccess so this isnt hardcoded
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

		JSONObject j;
		try {
			j = new JSONObject(strJSON);

			JSONObject j2 = j.getJSONObject("result");
			versionNumber = j2.getString("CurrentMobileAppVersionNumber");

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

		return versionNumber;

	}*/

	/*
     * downloadNewVersion - This method will download the new APK version from the web service
     *
     */
	public static String downloadNewVersion ( String username, String password )
	{
		String apkFilename = null;
		String strUrl = getURL ( "get_latest_app" );
		String jsonData = "{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}";

		try
		{
			apkFilename = HttpUtils.sendPostBinaryResponse ( strUrl, jsonData );
		}
		catch ( IOException e )
		{
			apkFilename = "IOException: " + e.getMessage();
			e.printStackTrace();
		}
		catch ( RuntimeException e2 )
		{
			apkFilename = "RunTimeException: " + e2.getMessage();
			e2.printStackTrace();
		}

		return apkFilename;
	}
}
