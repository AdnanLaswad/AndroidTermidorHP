package com.evanhoe.tango.objs;

//import java.text.SimpleDateFormat;
//import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

public class WorkOrderDetail
{
    private String EntryTime;
    private String SyncStatus;
    private String SyncTime;
    private String ServiceWorkorderID;
    private String TechnicianGroupID;
    private String TechnicianPersonID;
    private String TechnicianUserRoleCode;
    private String TechnicianUserSubRoleCode;
    private String WorkorderStatusCode;
    private String InjectionStationID_act;
    private String InjectionDisplayFirmwareVersion;
    private String ControllerFirmwareVersion;
    private String BaseDisplayFirmwareVersion;
    private String TermicideTypeCode_act;
    private String TermicideTypeName_act;
    private String SoilTypeCode_act;
    private String SoilTypeRefCode_act;
    private String HT_InjectionCount_act;
    private String HT_LinearDistanceApplication_act;
    private String HT_TermicideVolume_act;
    private String HT_WaterVolume_act;
    private String HT_PumpVolume_act;
    private String HT_InjectorTimer_act;
    private String SA_TermicideVolume_act;
    private String SA_WaterVolume_act;
    private String SA_PumpVolume_act;
    private String LinearMeasurementUnit_act;
    private String VolumeMeasurementUnit_act;
    private String Latitude;
    private String Longitude;
    private String WorkStartTime;
    private String WorkFinishTime;
    private String WorkStartTimeDate;
    private String WorkStartTimeTime;
    private String MinutesWorkedInSession_act;
    private String AlertMessage;
    private String PumpAlertFlag;
    private String WaterAlertFlag;
    private String UsingHTFlowInSAModeAlertFlag;
    private String HTModeEnabled;
    private String SAModeEnabled;
    private String WorkorderSlotLocationUsed;
    private String WOEGeneralNotes;
    private String WOELocationNotes;

    public WorkOrderDetail (
             String EntryTime
           , String SyncStatus
           , String SyncTime
           , String ServiceWorkorderID
           , String TechnicianGroupID
           , String TechnicianPersonID
           , String TechnicianUserRoleCode
           , String TechnicianUserSubRoleCode
           , String WorkorderStatusCode
           , String InjectionStationID_act
           , String InjectionDisplayFirmwareVersion
           , String ControllerFirmwareVersion
           , String BaseDisplayFirmwareVersion
           , String TermicideTypeCode_act
           , String TermicideTypeName_act
           , String SoilTypeCode_act
           , String SoilTypeRefCode_act
           , String HT_InjectionCount_act
           , String HT_LinearDistanceApplication_act
           , String HT_TermicideVolume_act
           , String HT_WaterVolume_act
           , String HT_PumpVolume_act
           , String HT_InjectorTimer_act
           , String SA_TermicideVolume_act
           , String SA_WaterVolume_act
           , String SA_PumpVolume_act
           , String LinearMeasurementUnit_act
           , String VolumeMeasurementUnit_act
           , String Latitude
           , String Longitude
           , String WorkStartTime
           , String WorkFinishTime
           , String WorkStartTimeDate
           , String WorkStartTimeTime
           , String MinutesWorkedInSession_act
           , String AlertMessage
           , String PumpAlertFlag
           , String WaterAlertFlag
           , String UsingHTFlowInSAModeAlertFlag
           , String HTModeEnabled
           , String SAModeEnabled
           , String WorkorderSlotLocationUsed
           , String WOEGeneralNotes
           , String WOELocationNotes
                      )
    {
        this.EntryTime = EntryTime;
        this.SyncStatus = SyncStatus;
        this.SyncTime = SyncTime;
        this.ServiceWorkorderID = ServiceWorkorderID;
        this.TechnicianGroupID = TechnicianGroupID;
        this.TechnicianPersonID = TechnicianPersonID;
        this.TechnicianUserRoleCode = TechnicianUserRoleCode;
        this.TechnicianUserSubRoleCode = TechnicianUserSubRoleCode;
        this.WorkorderStatusCode = WorkorderStatusCode;
        this.InjectionStationID_act = InjectionStationID_act;
        this.InjectionDisplayFirmwareVersion = InjectionDisplayFirmwareVersion;
        this.ControllerFirmwareVersion = ControllerFirmwareVersion;
        this.BaseDisplayFirmwareVersion = BaseDisplayFirmwareVersion;
        this.TermicideTypeCode_act = TermicideTypeCode_act;
        this.TermicideTypeName_act = TermicideTypeName_act;
        this.SoilTypeCode_act = SoilTypeCode_act;
        this.SoilTypeRefCode_act = SoilTypeRefCode_act;
        this.HT_InjectionCount_act = HT_InjectionCount_act;
        this.HT_LinearDistanceApplication_act = HT_LinearDistanceApplication_act;
        this.HT_TermicideVolume_act = HT_TermicideVolume_act;
        this.HT_WaterVolume_act = HT_WaterVolume_act;
        this.HT_PumpVolume_act = HT_PumpVolume_act;
        this.HT_InjectorTimer_act = HT_InjectorTimer_act;
        this.SA_TermicideVolume_act = SA_TermicideVolume_act;
        this.SA_WaterVolume_act = SA_WaterVolume_act;
        this.SA_PumpVolume_act = SA_PumpVolume_act;
        this.LinearMeasurementUnit_act = LinearMeasurementUnit_act;
        this.VolumeMeasurementUnit_act = VolumeMeasurementUnit_act;
        this.Latitude = Latitude;
        this.Longitude = Longitude;
        this.WorkStartTime = WorkStartTime;
        this.WorkFinishTime = WorkFinishTime;
        this.WorkStartTimeDate = WorkStartTimeDate;
        this.WorkStartTimeTime = WorkStartTimeTime;
        this.MinutesWorkedInSession_act = MinutesWorkedInSession_act;
        this.AlertMessage = AlertMessage;
        this.PumpAlertFlag = PumpAlertFlag;
        this.WaterAlertFlag = WaterAlertFlag;
        this.UsingHTFlowInSAModeAlertFlag = UsingHTFlowInSAModeAlertFlag;
        this.HTModeEnabled = HTModeEnabled;
        this.SAModeEnabled = SAModeEnabled;
        this.WorkorderSlotLocationUsed = WorkorderSlotLocationUsed;
        this.WOEGeneralNotes = WOEGeneralNotes;
        this.WOELocationNotes = WOELocationNotes;
    }

    public WorkOrderDetail ( JSONObject j )
    {
        super();
        try
        {
            this.EntryTime = j.getString("EntryTime");
            //this.SyncStatus = j.getString("SyncStatus");
            this.SyncTime = j.getString("LastSyncTime");
            this.ServiceWorkorderID = j.getString("ServiceWorkorderID");
            this.TechnicianGroupID = j.getString("ServMgmtGroupID");
            this.TechnicianPersonID = j.getString("ServMgmtPersonID");
            this.TechnicianUserRoleCode = j.getString("ServMgmtUserRoleCode");
            this.TechnicianUserSubRoleCode = j.getString("ServMgmtUserSubRoleCode");
            this.WorkorderStatusCode = j.getString("WorkorderStatusCode");
            this.InjectionStationID_act = j.getString("InjectionStationID_act");
            //this.InjectionDisplayFirmwareVersion = j.getString("InjectionDisplayFirmwareVersion");
            //this.ControllerFirmwareVersion = j.getString("ControllerFirmwareVersion");
            //this.BaseDisplayFirmwareVersion = j.getString("BaseDisplayFirmwareVersion");
            this.TermicideTypeCode_act = j.getString("TermicideTypeCode_act");
            //this.TermicideTypeName_act = j.getString("TermicideTypeName_act");
            this.SoilTypeCode_act = j.getString("SoilTypeCode_act");
            this.SoilTypeRefCode_act = j.getString("SoilTypeRefCode_act");
            this.HT_InjectionCount_act = j.getString("HT_InjectionCount_act");
            this.HT_LinearDistanceApplication_act = j.getString("HT_LinearDistanceApplication_act");
            this.HT_TermicideVolume_act = j.getString("HT_TermicideVolume_act");
            this.HT_WaterVolume_act = j.getString("HT_WaterVolume_act");
            this.HT_PumpVolume_act = j.getString("HT_PumpVolume_act");
            this.HT_InjectorTimer_act = j.getString("HT_InjectorTimer_act");
            this.SA_TermicideVolume_act = j.getString("SA_TermicideVolume_act");
            this.SA_WaterVolume_act = j.getString("SA_WaterVolume_act");
            this.SA_PumpVolume_act = j.getString("SA_PumpVolume_act");
            this.LinearMeasurementUnit_act = j.getString("LinearMeasurementUnit_act");
            this.VolumeMeasurementUnit_act = j.getString("VolumeMeasurementUnit_act");
            this.Latitude = j.getString("Latitude");
            this.Longitude = j.getString("Longitude");
            this.WorkStartTime = j.getString("WorkStartTime");
            this.WorkFinishTime = j.getString("WorkFinishTime");
            //this.WorkStartTimeDate = j.getString("WorkStartTimeDate");
            //this.WorkStartTimeTime = j.getString("WorkStartTimeTime");
            this.MinutesWorkedInSession_act = j.getString("MinutesWorkedInSession_act");
            this.AlertMessage = j.getString("AlertMessage");
            this.PumpAlertFlag = j.getString("PumpAlertFlag");
            this.WaterAlertFlag = j.getString("WaterAlertFlag");
            this.UsingHTFlowInSAModeAlertFlag = j.getString("UsingHTFlowInSAModeAlertFlag");
            this.HTModeEnabled = j.getString("HTModeEnabled");
            this.SAModeEnabled = j.getString("SAModeEnabled");
            this.WorkorderSlotLocationUsed = j.getString("WorkorderSlotLocationUsed");
            this.WOEGeneralNotes = j.getString("GeneralNotes");
            this.WOELocationNotes = j.getString("LocationNotes");
        }
        catch ( JSONException e )
        {
            // TODO: Auto-generated catch block
            e.printStackTrace();
        }
    }

    public WorkOrderDetail ( TermidorData termidorData )
    {
        // If we are creating this object from a TermidorData, that means it hasn't
        // been synchronized with the Web Server
        this.SyncStatus = "N";
        this.ServiceWorkorderID = termidorData.getWorkOrderID();
        this.SoilTypeRefCode_act = Short.toString ( termidorData.getSoilType() );
System.out.println ( "INFO: soiltyperefcode = " + termidorData.getSoilType() + "->" + this.SoilTypeRefCode_act );
        switch ( termidorData.getSoilType() )
        {
        	case 0:
        		this.SoilTypeCode_act = "S";
        		break;
            case 1:
                this.SoilTypeCode_act = "H";
                break;
            case 2:
                this.SoilTypeCode_act = "S";
                break;
            case 3:
                this.SoilTypeCode_act = "L";
                break;
        }
        this.HT_PumpVolume_act = Float.toString ( termidorData.getHtModePumpVolume() );
        this.HT_InjectionCount_act = Integer.toString ( termidorData.getHtModeInjectorCount() );
        this.HT_TermicideVolume_act = Float.toString ( termidorData.getHtModeActiveIngredientUsage() );
        this.HT_WaterVolume_act = Float.toString ( termidorData.getHtModeWaterUsage() );
        this.SA_TermicideVolume_act = Float.toString ( termidorData.getSaModeActiveIngredientUsage() );
        this.SA_WaterVolume_act = Float.toString ( termidorData.getSaModeWaterUsage() );
        this.HTModeEnabled = termidorData.isHtModeEnabled() ? "1" : "0";
        this.SAModeEnabled = termidorData.isSaModeEnabled() ? "1" : "0";
        this.PumpAlertFlag = termidorData.isPumpAlarm() ? "1" : "0";
        this.WaterAlertFlag = termidorData.isWaterAlarm() ? "1" : "0";
        this.UsingHTFlowInSAModeAlertFlag = termidorData.isHtFlowInSAMode() ? "1" : "0";
        // TODO: This needs remove after it gets added to the local database and Termidor
        //this.ServiceWorkorderNumber = this.ServiceWorkorderID.replaceAll("[^\\.0123456789]","");

        // location;    	
        // workOrderStartDateStamp; 
        // productApplied; 
        // injectorTimer; 
        // workOrderStartTime; 
        // timeToComplete; 
        // locationDone; 

    }

    public WorkOrderDetail ( String JSON ) throws JSONException
    {
//        super();
        this ( new JSONObject ( JSON ) );
    }

    public WorkOrderDetail() {

    }

    public String getEntryTime ( )
    {
        return EntryTime;
    }
    public void setEntryTime ( String EntryTime)
    {
        this.EntryTime = EntryTime;
    }

    public String getSyncStatus ( )
    {
        return SyncStatus;
    }
    public void setSyncStatus ( String SyncStatus)
    {
        this.SyncStatus = SyncStatus;
    }

    public String getSyncTime ( )
    {
        return SyncTime;
    }
    public void setSyncTime ( String SyncTime)
    {
        this.SyncTime = SyncTime;
    }

    public String getServiceWorkorderID ( )
    {
        return ServiceWorkorderID;
    }
    public void setServiceWorkorderID ( String ServiceWorkorderID)
    {
        this.ServiceWorkorderID = ServiceWorkorderID;
    }

    public String getTechnicianGroupID ( )
    {
        return TechnicianGroupID;
    }
    public void setTechnicianGroupID ( String TechnicianGroupID)
    {
        this.TechnicianGroupID = TechnicianGroupID;
    }

    public String getTechnicianPersonID ( )
    {
        return TechnicianPersonID;
    }
    public void setTechnicianPersonID ( String TechnicianPersonID)
    {
        this.TechnicianPersonID = TechnicianPersonID;
    }

    public String getTechnicianUserRoleCode ( )
    {
        return TechnicianUserRoleCode;
    }
    public void setTechnicianUserRoleCode ( String TechnicianUserRoleCode)
    {
        this.TechnicianUserRoleCode = TechnicianUserRoleCode;
    }

    public String getTechnicianUserSubRoleCode ( )
    {
        return TechnicianUserSubRoleCode;
    }
    public void setTechnicianUserSubRoleCode ( String TechnicianUserSubRoleCode)
    {
        this.TechnicianUserSubRoleCode = TechnicianUserSubRoleCode;
    }

    public String getWorkorderStatusCode ( )
    {
        return WorkorderStatusCode;
    }
    public void setWorkorderStatusCode ( String WorkorderStatusCode)
    {
        this.WorkorderStatusCode = WorkorderStatusCode;
    }

    public String getInjectionStationID_act ( )
    {
        return InjectionStationID_act;
    }
    public void setInjectionStationID_act ( String InjectionStationID_act)
    {
        this.InjectionStationID_act = InjectionStationID_act;
    }

    public String getInjectionDisplayFirmwareVersion ( )
    {
        return InjectionDisplayFirmwareVersion;
    }
    public void setInjectionDisplayFirmwareVersion ( String InjectionDisplayFirmwareVersion)
    {
        this.InjectionDisplayFirmwareVersion = InjectionDisplayFirmwareVersion;
    }

    public String getControllerFirmwareVersion ( )
    {
        return ControllerFirmwareVersion;
    }
    public void setControllerFirmwareVersion ( String ControllerFirmwareVersion)
    {
        this.ControllerFirmwareVersion = ControllerFirmwareVersion;
    }

    public String getBaseDisplayFirmwareVersion ( )
    {
        return BaseDisplayFirmwareVersion;
    }
    public void setBaseDisplayFirmwareVersion ( String BaseDisplayFirmwareVersion)
    {
        this.BaseDisplayFirmwareVersion = BaseDisplayFirmwareVersion;
    }

    public String getTermicideTypeCode_act ( )
    {
        return TermicideTypeCode_act;
    }
    public void setTermicideTypeCode_act ( String TermicideTypeCode_act)
    {
        this.TermicideTypeCode_act = TermicideTypeCode_act;
    }

    public String getTermicideTypeName_act ( )
    {
        return TermicideTypeName_act;
    }
    public void setTermicideTypeName_act ( String TermicideTypeName_act)
    {
        this.TermicideTypeName_act = TermicideTypeName_act;
    }

    public String getSoilTypeCode_act ( )
    {
        return SoilTypeCode_act;
    }
    public void setSoilTypeCode_act ( String SoilTypeCode_act)
    {
        this.SoilTypeCode_act = SoilTypeCode_act;
    }

    public String getSoilTypeRefCode_act ( )
    {
        return SoilTypeRefCode_act;
    }
    public void setSoilTypeRefCode_act ( String SoilTypeRefCode_act)
    {
        this.SoilTypeRefCode_act = SoilTypeRefCode_act;
    }

    public String getHT_InjectionCount_act ( )
    {
        return HT_InjectionCount_act;
    }
    public void setHT_InjectionCount_act ( String HT_InjectionCount_act)
    {
        this.HT_InjectionCount_act = HT_InjectionCount_act;
    }

    public String getHT_LinearDistanceApplication_act ( )
    {
        return HT_LinearDistanceApplication_act;
    }
    public void setHT_LinearDistanceApplication_act ( String HT_LinearDistanceApplication_act)
    {
        this.HT_LinearDistanceApplication_act = HT_LinearDistanceApplication_act;
    }

    public String getHT_TermicideVolume_act ( )
    {
        return HT_TermicideVolume_act;
    }
    public void setHT_TermicideVolume_act ( String HT_TermicideVolume_act)
    {
        this.HT_TermicideVolume_act = HT_TermicideVolume_act;
    }

    public String getHT_WaterVolume_act ( )
    {
        return HT_WaterVolume_act;
    }
    public void setHT_WaterVolume_act ( String HT_WaterVolume_act)
    {
        this.HT_WaterVolume_act = HT_WaterVolume_act;
    }

    public String getHT_PumpVolume_act ( )
    {
        return HT_PumpVolume_act;
    }
    public void setHT_PumpVolume_act ( String HT_PumpVolume_act)
    {
        this.HT_PumpVolume_act = HT_PumpVolume_act;
    }

    public String getHT_InjectorTimer_act ( )
    {
        return HT_InjectorTimer_act;
    }
    public void setHT_InjectorTimer_act ( String HT_InjectorTimer_act)
    {
        this.HT_InjectorTimer_act = HT_InjectorTimer_act;
    }

    public String getSA_TermicideVolume_act ( )
    {
        return SA_TermicideVolume_act;
    }
    public void setSA_TermicideVolume_act ( String SA_TermicideVolume_act)
    {
        this.SA_TermicideVolume_act = SA_TermicideVolume_act;
    }

    public String getSA_WaterVolume_act ( )
    {
        return SA_WaterVolume_act;
    }
    public void setSA_WaterVolume_act ( String SA_WaterVolume_act)
    {
        this.SA_WaterVolume_act = SA_WaterVolume_act;
    }

    public String getSA_PumpVolume_act ( )
    {
        return SA_PumpVolume_act;
    }
    public void setSA_PumpVolume_act ( String SA_PumpVolume_act)
    {
        this.SA_PumpVolume_act = SA_PumpVolume_act;
    }

    public String getLinearMeasurementUnit_act ( )
    {
        return LinearMeasurementUnit_act;
    }
    public void setLinearMeasurementUnit_act ( String LinearMeasurementUnit_act)
    {
        this.LinearMeasurementUnit_act = LinearMeasurementUnit_act;
    }

    public String getVolumeMeasurementUnit_act ( )
    {
        return VolumeMeasurementUnit_act;
    }
    public void setVolumeMeasurementUnit_act ( String VolumeMeasurementUnit_act)
    {
        this.VolumeMeasurementUnit_act = VolumeMeasurementUnit_act;
    }

    public String getLatitude ( )
    {
        return Latitude;
    }
    public void setLatitude ( String Latitude)
    {
        this.Latitude = Latitude;
    }

    public String getLongitude ( )
    {
        return Longitude;
    }
    public void setLongitude ( String Longitude)
    {
        this.Longitude = Longitude;
    }

    public String getWorkStartTime ( )
    {
        return WorkStartTime;
    }
    public void setWorkStartTime ( String WorkStartTime)
    {
        this.WorkStartTime = WorkStartTime;
    }

    public String getWorkFinishTime ( )
    {
        return WorkFinishTime;
    }
    public void setWorkFinishTime ( String WorkFinishTime)
    {
        this.WorkFinishTime = WorkFinishTime;
    }

    public String getWorkStartTimeDate ( )
    {
        return WorkStartTimeDate;
    }
    public void setWorkStartTimeDate ( String WorkStartTimeDate)
    {
        this.WorkStartTimeDate = WorkStartTimeDate;
    }

    public String getWorkStartTimeTime ( )
    {
        return WorkStartTimeTime;
    }
    public void setWorkStartTimeTime ( String WorkStartTimeTime)
    {
        this.WorkStartTimeTime = WorkStartTimeTime;
    }

    public String getMinutesWorkedInSession_act ( )
    {
        return MinutesWorkedInSession_act;
    }
    public void setMinutesWorkedInSession_act ( String MinutesWorkedInSession_act)
    {
        this.MinutesWorkedInSession_act = MinutesWorkedInSession_act;
    }

    public String getAlertMessage ( )
    {
        return AlertMessage;
    }
    public void setAlertMessage ( String AlertMessage)
    {
        this.AlertMessage = AlertMessage;
    }

    public String getPumpAlertFlag ( )
    {
        return PumpAlertFlag;
    }
    public void setPumpAlertFlag ( String PumpAlertFlag)
    {
        this.PumpAlertFlag = PumpAlertFlag;
    }

    public String getWaterAlertFlag ( )
    {
        return WaterAlertFlag;
    }
    public void setWaterAlertFlag ( String WaterAlertFlag)
    {
        this.WaterAlertFlag = WaterAlertFlag;
    }

    public String getUsingHTFlowInSAModeAlertFlag ( )
    {
        return UsingHTFlowInSAModeAlertFlag;
    }
    public void setUsingHTFlowInSAModeAlertFlag ( String UsingHTFlowInSAModeAlertFlag)
    {
        this.UsingHTFlowInSAModeAlertFlag = UsingHTFlowInSAModeAlertFlag;
    }

    public String getHTModeEnabled ( )
    {
        return HTModeEnabled;
    }
    public void setHTModeEnabled ( String HTModeEnabled)
    {
        this.HTModeEnabled = HTModeEnabled;
    }

    public String getSAModeEnabled ( )
    {
        return SAModeEnabled;
    }
    public void setSAModeEnabled ( String SAModeEnabled)
    {
        this.SAModeEnabled = SAModeEnabled;
    }

    public String getWorkorderSlotLocationUsed ( )
    {
        return WorkorderSlotLocationUsed;
    }
    public void setWorkorderSlotLocationUsed ( String WorkorderSlotLocationUsed)
    {
        this.WorkorderSlotLocationUsed = WorkorderSlotLocationUsed;
    }

    public String getWOEGeneralNotes ( )
    {
        return WOEGeneralNotes;
    }
    public void setWOEGeneralNotes ( String WOEGeneralNotes)
    {
        this.WOEGeneralNotes = WOEGeneralNotes;
    }

    public String getWOELocationNotes ( )
    {
        return WOELocationNotes;
    }
    public void setWOELocationNotes ( String WOELocationNotes)
    {
        this.WOELocationNotes = WOELocationNotes;
    }

    /**
     * function to get around JSONObject.put not working on nulls.
     * checks if value is null returns empty string if yes, 
     * returns value if no
     * @param value
     * @return String
     */
     private String checkIfNull(String value){            
        if(value == null){
            return "";
        }else{
            return value;
        }      
    }
       
    /**
     * returns a JSONObject representation
     * @return JSONObject
     * @throws JSONException 
     */
    public JSONObject toJSONObject() throws JSONException{
             
        JSONObject j = new JSONObject();

        //Calendar cal = Calendar.getInstance();
        //SimpleDateFormat sdf = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss" );

        j.put ( "EntryTime", checkIfNull(this.EntryTime));
        //j.put ( "EntryTime", sdf.format ( cal.getTime() ) );
        j.put ( "EntityActive", "Y" );
        //j.put ( "SyncStatus", checkIfNull(this.SyncStatus));
        //j.put ( "SyncTime", checkIfNull(this.SyncTime));
        j.put ( "ViewportCode", "BTG" );
        j.put ( "ServiceWorkorderID", checkIfNull(this.ServiceWorkorderID));
        // BLUF: Remove everything but 0-9 and period.
        // Regular Expression: '^' opposite of what's in brackets, which consists of 
        // a period (escaped by slashes), and the numbers 0-9
        j.put ( "ServiceWorkorderNumber", checkIfNull(this.ServiceWorkorderID.replaceAll("[^\\.0123456789]","").replaceAll("^0*","")));
        j.put ( "LatestEntry", "Y" );
        j.put ( "ServMgmtGroupID", checkIfNull(this.TechnicianGroupID));
        j.put ( "ServMgmtPersonID", checkIfNull(this.TechnicianPersonID));
        j.put ( "ServMgmtUserRoleCode", checkIfNull(this.TechnicianUserRoleCode));
        j.put ( "ServMgmtUserSubRoleCode", checkIfNull(this.TechnicianUserSubRoleCode));
        j.put ( "WorkorderStatusCode", checkIfNull(this.WorkorderStatusCode));
        //j.put ( "InjectionStationID_act", "INJ00296408080739925666" );
        j.put ( "InjectionStationID_act", checkIfNull(this.InjectionStationID_act) );
        
        //j.put ( "InjectionDisplayFirmwareVersion", checkIfNull(this.InjectionDisplayFirmwareVersion));
        //j.put ( "ControllerFirmwareVersion", checkIfNull(this.ControllerFirmwareVersion));
        //j.put ( "BaseDisplayFirmwareVersion", checkIfNull(this.BaseDisplayFirmwareVersion));
        j.put ( "TermicideTypeCode_act", checkIfNull(this.TermicideTypeCode_act));
        //j.put ( "TermicideTypeName_act", checkIfNull(this.TermicideTypeName_act));
        j.put ( "SoilTypeCode_act", checkIfNull(this.SoilTypeCode_act));
        j.put ( "SoilTypeRefCode_act", checkIfNull(this.SoilTypeRefCode_act));
        j.put ( "HT_InjectionCount_act", checkIfNull(this.HT_InjectionCount_act));
        j.put ( "HT_LinearDistanceApplication_act", checkIfNull(this.HT_LinearDistanceApplication_act));
        j.put ( "HT_TermicideVolume_act", checkIfNull(this.HT_TermicideVolume_act));
        j.put ( "HT_WaterVolume_act", checkIfNull(this.HT_WaterVolume_act));
        j.put ( "HT_PumpVolume_act", checkIfNull(this.HT_PumpVolume_act));
        j.put ( "HT_InjectorTimer_act", checkIfNull(this.HT_InjectorTimer_act));
        j.put ( "SA_InjectionCount_act", checkIfNull(this.HT_InjectionCount_act));
        j.put ( "SA_LinearDistanceApplication_act", checkIfNull(this.HT_LinearDistanceApplication_act));
        j.put ( "SA_TermicideVolume_act", checkIfNull(this.SA_TermicideVolume_act));
        j.put ( "SA_WaterVolume_act", checkIfNull(this.SA_WaterVolume_act));
        j.put ( "SA_PumpVolume_act", checkIfNull(this.SA_PumpVolume_act));
        j.put ( "SA_InjectorTimer_act", checkIfNull(this.HT_InjectorTimer_act));
        j.put ( "LinearMeasurementUnit_act", checkIfNull(this.LinearMeasurementUnit_act));
        j.put ( "VolumeMeasurementUnit_act", checkIfNull(this.VolumeMeasurementUnit_act));
        j.put ( "Latitude", checkIfNull(this.Latitude));
        j.put ( "Longitude", checkIfNull(this.Longitude));
        j.put ( "WorkStartTime", checkIfNull(this.WorkStartTime));
        j.put ( "WorkFinishTime", checkIfNull(this.WorkFinishTime));
        //j.put ( "WorkStartTimeDate", checkIfNull(this.WorkStartTimeDate));
        //j.put ( "WorkStartTimeTime", checkIfNull(this.WorkStartTimeTime));
        j.put ( "MinutesWorkedInSession_act", checkIfNull(this.MinutesWorkedInSession_act));
        j.put ( "AlertMessage", checkIfNull(this.AlertMessage));
        j.put ( "PumpAlertFlag", checkIfNull(this.PumpAlertFlag));
        j.put ( "WaterAlertFlag", checkIfNull(this.WaterAlertFlag));
        j.put ( "UsingHTFlowInSAModeAlertFlag", checkIfNull(this.UsingHTFlowInSAModeAlertFlag));
        j.put ( "HTModeEnabled", checkIfNull(this.HTModeEnabled));
        j.put ( "SAModeEnabled", checkIfNull(this.SAModeEnabled));
        j.put ( "WorkorderSlotLocationUsed", checkIfNull(this.WorkorderSlotLocationUsed));
        j.put ( "GeneralNotes", checkIfNull(this.WOEGeneralNotes));
        j.put ( "LocationNotes", checkIfNull(this.WOELocationNotes));

        return ( j );
    }
}
