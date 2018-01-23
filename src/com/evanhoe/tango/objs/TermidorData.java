package com.evanhoe.tango.objs;

import org.json.JSONException;
import org.json.JSONObject;

public class TermidorData {
	   	private String location;    	
		private String pidDisplayFirmware; 
		private String baseControllerFirmware; 
		private String baseDisplayFirm; 
		private String unitID; 
		private String workOrderNumber; 
		private String workOrderID;
		private String address; 
		private String city; 
		private String state; 
		private String workOrderStartDateStamp; 
		private String productApplied; 
		private short soilType; 
		private float injectorTimer; 
		private float htModePumpVolume; 
		private int htModeInjectorCount; 
		private float htModeActiveIngredientUsage; 
		private float htModeWaterUsage; 
		private float saModeActiveIngredientUsage; 
		private float saModeWaterUsage; 
		private String workOrderStartTime; 
		private int timeToComplete; 
		private boolean locationDone; 
		private boolean htModeEnabled; 
		private boolean saModeEnabled; 
		private boolean pumpAlarm; 
		private boolean waterAlarm; 
		private boolean htFlowInSAMode; 
		private String lastErrorMessage;
		
		
		/**
		 *  Constructor for Work Order Detail
		 * @param location
		 * @param pidDisplayFirmware
		 * @param baseControllerFirmware
		 * @param baseDisplayFirm
		 * @param unitID
		 * @param workOrderNumber
		 * @param workOrderID
		 * @param address
		 * @param city
		 * @param state
		 * @param workOrderStartDateStamp
		 * @param productApplied
		 * @param soilType
		 * @param injectorTimer
		 * @param htModePumpVolume
		 * @param htModeInjectorCount
		 * @param htModeActiveIngredientUsage
		 * @param htModeWaterUsage
		 * @param saModeActiveIngredientUsage
		 * @param saModeWaterUsage
		 * @param workOrderStartTime
		 * @param timeToComplete
		 * @param locationDone
		 * @param htModeEnabled
		 * @param saModeEnabled
		 * @param pumpAlarm
		 * @param waterAlarm
		 * @param htFlowInSAMode
		 * 
		 */
		public TermidorData (String location, String pidDisplayFirmware,
				String baseControllerFirmware, String baseDisplayFirm,
				String unitID, String workOrderNumber, String address, String city,
				String state, String workOrderStartDateStamp,
				String productApplied, short soilType, float injectorTimer,
				float htModePumpVolume, int htModeInjectorCount,
				float htModeActiveIngredientUsage, float htModeWaterUsage,
				float saModeActiveIngredientUsage, float saModeWaterUsage,
				String workOrderStartTime, int timeToComplete,
				boolean locationDone, boolean htModeEnabled, boolean saModeEnabled,
				boolean pumpAlarm, boolean waterAlarm, boolean htFlowInSAMode, String workOrderID) {
			super();
			this.location = location;
			this.pidDisplayFirmware = pidDisplayFirmware;
			this.baseControllerFirmware = baseControllerFirmware;
			this.baseDisplayFirm = baseDisplayFirm;
			this.unitID = unitID;
			this.workOrderNumber = workOrderNumber.trim();
			this.address = address;
			this.city = city;
			this.state = state;
			this.workOrderStartDateStamp = workOrderStartDateStamp;
			this.productApplied = productApplied;
			this.soilType = soilType;
			this.injectorTimer = injectorTimer;
			this.htModePumpVolume = htModePumpVolume;
			this.htModeInjectorCount = htModeInjectorCount;
			this.htModeActiveIngredientUsage = htModeActiveIngredientUsage;
			this.htModeWaterUsage = htModeWaterUsage;
			this.saModeActiveIngredientUsage = saModeActiveIngredientUsage;
			this.saModeWaterUsage = saModeWaterUsage;
			this.workOrderStartTime = workOrderStartTime;
			this.timeToComplete = timeToComplete;
			this.locationDone = locationDone;
			this.htModeEnabled = htModeEnabled;
			this.saModeEnabled = saModeEnabled;
			this.pumpAlarm = pumpAlarm;
			this.waterAlarm = waterAlarm;
			this.htFlowInSAMode = htFlowInSAMode;
			this.workOrderID = workOrderID;
		}

		public TermidorData () {
			// TODO Auto-generated constructor stub
		}

		public String getLocation() {
			return location;
		}
		public void setLocation(String location) {
			this.location = location;
		}
		public String getPidDisplayFirmware() {
			return pidDisplayFirmware;
		}
		public void setPidDisplayFirmware(String pidDisplayFirmware) {
			this.pidDisplayFirmware = pidDisplayFirmware;
		}
		public String getBaseControllerFirmware() {
			return baseControllerFirmware;
		}
		public void setBaseControllerFirmware(String baseControllerFirmware) {
			this.baseControllerFirmware = baseControllerFirmware;
		}
		public String getBaseDisplayFirm() {
			return baseDisplayFirm;
		}
		public void setBaseDisplayFirm(String baseDisplayFirm) {
			this.baseDisplayFirm = baseDisplayFirm;
		}
		public String getUnitID() {
			return unitID;
		}
		public void setUnitID(String unitID) {
			this.unitID = unitID;
		}	
		public String getWorkOrderNumber() {
			return workOrderNumber;
		}
		public void setWorkOrderNumber(String workOrderNumber) {
			this.workOrderNumber = workOrderNumber.trim();
		}
		public String getWorkOrderID() {
			return workOrderID;
		}
		public void setWorkOrderID(String workOrderID) {
			this.workOrderID = workOrderID.trim();
		}
		public String getAddress() {
			return address;
		}
		public void setAddress(String address) {
			this.address = address;
		}
		public String getCity() {
			return city;
		}
		public void setCity(String city) {
			this.city = city;
		}
		public String getState() {
			return state;
		}
		public void setState(String state) {
			this.state = state;
		}
		public String getWorkOrderStartDateStamp() {
			return workOrderStartDateStamp;
		}
		public void setWorkOrderStartDateStamp(String workOrderStartDateStamp) {
			this.workOrderStartDateStamp = workOrderStartDateStamp;
		}
		public String getProductApplied() {
			return productApplied;
		}
		public void setProductApplied(String productApplied) {
			this.productApplied = productApplied;
		}
		public short getSoilType() {
			return soilType;
		}
		public void setSoilType(short soilType) {
			this.soilType = soilType;
		}
		public float getInjectorTimer() {
			return injectorTimer;
		}
		public String getLastErrorMessage() {
			return lastErrorMessage;
		}
		public void setInjectorTimer(float injectorTimer) {
			this.injectorTimer = injectorTimer;
		}
		public float getHtModePumpVolume() {
			return htModePumpVolume;
		}
		public void setHtModePumpVolume(float htModePumpVolume) {
			this.htModePumpVolume = htModePumpVolume;
		}
		public int getHtModeInjectorCount() {
			return htModeInjectorCount;
		}
		public void setHtModeInjectorCount(int htModeInjectorCount) {
			this.htModeInjectorCount = htModeInjectorCount;
		}
		public float getHtModeActiveIngredientUsage() {
			return htModeActiveIngredientUsage;
		}
		public void setHtModeActiveIngredientUsage(float htModeActiveIngredientUsage) {
			this.htModeActiveIngredientUsage = htModeActiveIngredientUsage;
		}
		public float getHtModeWaterUsage() {
			return htModeWaterUsage;
		}
		public void setHtModeWaterUsage(float htModeWaterUsage) {
			this.htModeWaterUsage = htModeWaterUsage;
		}
		public float getSaModeActiveIngredientUsage() {
			return saModeActiveIngredientUsage;
		}
		public void setSaModeActiveIngredientUsage(float saModeActiveIngredientUsage) {
			this.saModeActiveIngredientUsage = saModeActiveIngredientUsage;
		}
		public float getSaModeWaterUsage() {
			return saModeWaterUsage;
		}
		public void setSaModeWaterUsage(float saModeWaterUsage) {
			this.saModeWaterUsage = saModeWaterUsage;
		}
		public String getWorkOrderStartTime() {
			return workOrderStartTime;
		}
		public void setWorkOrderStartTime(String workOrderStartTime) {
			this.workOrderStartTime = workOrderStartTime;
		}
		public int getTimeToComplete() {
			return timeToComplete;
		}
		public void setTimeToComplete(int timeToComplete) {
			this.timeToComplete = timeToComplete;
		}
		public boolean isLocationDone() {
			return locationDone;
		}
		public void setLocationDone(boolean locationDone) {
			this.locationDone = locationDone;
		}
		public boolean isHtModeEnabled() {
			return htModeEnabled;
		}
		public void setHtModeEnabled(boolean htModeEnabled) {
			this.htModeEnabled = htModeEnabled;
		}
		public boolean isSaModeEnabled() {
			return saModeEnabled;
		}
		public void setSaModeEnabled(boolean saModeEnabled) {
			this.saModeEnabled = saModeEnabled;
		}
		public boolean isPumpAlarm() {
			return pumpAlarm;
		}
		public void setPumpAlarm(boolean pumpAlarm) {
			this.pumpAlarm = pumpAlarm;
		}	
		public boolean isWaterAlarm() {
			return waterAlarm;
		}
		public void setWaterAlarm(boolean waterAlarm) {
			this.waterAlarm = pumpAlarm;
		}	
		public boolean isHtFlowInSAMode() {
			return htFlowInSAMode;
		}
		public void setHtFlowInSAMode(boolean htFlowInSAMode) {
			this.htFlowInSAMode = htFlowInSAMode;
		}	
		public void setLastErrorMessage(String lastErrorMessage) {
			this.lastErrorMessage = lastErrorMessage;
		}	

	}
