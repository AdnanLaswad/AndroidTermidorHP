package com.evanhoe.tango.objs;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class WorkOrder implements Parcelable {

    private String serviceWorkOrderId;
    private int serviceWorkorderNumber;
    private String serviceManagementWorkorderId;
    private String locationTitle;
    private String locationCode;
    private String addressLine1;
    private String addressLine2;
    private String attentionLine;
    private String city;
    private String stateProvinceCode;
    private String postalCode;
    private String countryCode;
    private String generalNotes;
    private String workorderStatusCode;
   // private String latitude;
   // private String longitude;
    private double linearDistancePlanned;
    private String linearMeasurementUnit;
    private String servMgmtGroupID;
    private String servMgmtPersonID;private String servMgmtUserRoleCode;
    private String servMgmtUserSubRoleCode;
    private String demo;
    
    private String termicideTypeCodePlanned;
    private String soilTypeCodePlanned;
    private String volumeMeasurementUnit;
    
    private String syncTime;


    public String getSyncTime() {
		return syncTime;
	}
	public void setSyncTime(String syncTime) {
		this.syncTime = syncTime;
	}
	public String getVolumeMeasurementUnit() {
		return volumeMeasurementUnit;
	}
	public void setVolumeMeasurementUnit(String volumeMeasurementUnit) {
		this.volumeMeasurementUnit = volumeMeasurementUnit;
	}
	public String getTermicideTypeCodePlanned() {
		return termicideTypeCodePlanned;
	}
	public void setTermicideTypeCodePlanned(String termicideTypeCodePlanned) {
		this.termicideTypeCodePlanned = termicideTypeCodePlanned;
	}
	public String getSoilTypeCodePlanned() {
		return soilTypeCodePlanned;
	}
	public void setSoilTypeCodePlanned(String soilTypeCodePlanned) {
		this.soilTypeCodePlanned = soilTypeCodePlanned;
	}
	public String getDemo() {
		return demo;
	}
	public void setDemo(String demo) {
		this.demo = demo;
	}
	public String getLinearMeasurementUnit() {
		return linearMeasurementUnit;
	}
	public void setLinearMeasurementUnit(String linearMeasurementUnit) {
		this.linearMeasurementUnit = linearMeasurementUnit;
	}
	public String getServiceWorkOrderId ()
    {
        return serviceWorkOrderId;
    }
    public void setServiceWorkOrderId ( String serviceWorkOrderId )
    {
        this.serviceWorkOrderId = serviceWorkOrderId;
    }

    /**
     * Get serviceWorkorderNumber.
     *
     * @return serviceWorkorderNumber as String.
     */
    public int getServiceWorkorderNumber()
    {
        return serviceWorkorderNumber;
    }
    
    /**
     * Set serviceWorkorderNumber.
     *
     * @param serviceWorkorderNumber the value to set.
     */
    public void setServiceWorkorderNumber(int serviceWorkorderNumber)
    {
        this.serviceWorkorderNumber = serviceWorkorderNumber;
    }

    /**
     * Get serviceManagementWorkorderId.
     *
     * @return serviceManagementWorkorderId as String.
     */
    public String getServiceManagementWorkorderId()
    {
        return serviceManagementWorkorderId;
    }
    
    /**
     * Set serviceManagementWorkorderId.
     *
     * @param serviceManagementWorkorderId the value to set.
     */
    public void setServiceManagementWorkorderId(String serviceManagementWorkorderId)
    {
        this.serviceManagementWorkorderId = serviceManagementWorkorderId;
    }

    public String getLocationTitle ()
    {
        return locationTitle;
    }
    public void setLocationTitle ( String locationTitle )
    {
        this.locationTitle = locationTitle;
    }

    public String getLocationCode ()
    {
        return locationCode;
    }
    public void setLocationCode ( String locationCode )
    {
        this.locationCode = locationCode;
    }

    public String getAddressLine1 ()
    {
        return addressLine1;
    }
    public void setAddressLine1 ( String addressLine1 )
    {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2 ()
    {
        return addressLine2;
    }
    public void setAddressLine2 ( String addressLine2 )
    {
        this.addressLine2 = addressLine2;
    }

    public String getAttentionLine ()
    {
        return attentionLine;
    }
    public void setAttentionLine ( String attentionLine )
    {
        this.attentionLine = attentionLine;
    }

    public String getCity ()
    {
        return city;
    }
    public void setCity ( String city )
    {
        this.city = city;
    }

    public String getStateProvinceCode ()
    {
        return stateProvinceCode;
    }
    public void setStateProvinceCode ( String stateProvinceCode )
    {
        this.stateProvinceCode = stateProvinceCode;
    }

    public String getPostalCode ()
    {
        return postalCode;
    }
    public void setPostalCode ( String postalCode )
    {
        this.postalCode = postalCode;
    }

    public String getCountryCode ()
    {
        return countryCode;
    }
    public void setCountryCode ( String countryCode )
    {
        this.countryCode = countryCode;
    }

    public String getGeneralNotes ()
    {
        return generalNotes;
    }
    public void setGeneralNotes ( String generalNotes )
    {
        this.generalNotes = generalNotes;
    }

    public String getWorkorderStatusCode ()
    {
        return workorderStatusCode;
    }
    public void setWorkorderStatusCode ( String workorderStatusCode )
    {
        this.workorderStatusCode = workorderStatusCode;
    }
/*
    public String getLatitude ()
    {
        return latitude;
    }
    public void setLatitude ( String latitude )
    {
        this.latitude = latitude;
    }

    public String getLongitude ()
    {
        return longitude;
    }

    public void setLongitude ( String longitude )
    {
        this.longitude = longitude;
    }
*/
    public double getLinearDistancePlanned ( )
    {
        return linearDistancePlanned;
    }
    public void setLinearDistancePlanned ( double linearDistnace)
    {
        this.linearDistancePlanned = linearDistancePlanned;
    }

    /**
     * Get servMgmtGroupID.
     *
     * @return servMgmtGroupID as String.
     */
    public String getServMgmtGroupID()
    {
        return servMgmtGroupID;
    }
    
    /**
     * Set servMgmtGroupID.
     *
     * @param servMgmtGroupID the value to set.
     */
    public void setServMgmtGroupID(String servMgmtGroupID)
    {
        this.servMgmtGroupID = servMgmtGroupID;
    }
    
    /**
     * Get servMgmtPersonID.
     *
     * @return servMgmtPersonID as String.
     */
    public String getServMgmtPersonID()
    {
        return servMgmtPersonID;
    }
    
    /**
     * Set servMgmtPersonID.
     *
     * @param servMgmtPersonID the value to set.
     */
    public void setServMgmtPersonID(String servMgmtPersonID)
    {
        this.servMgmtPersonID = servMgmtPersonID;
    }
    
    /**
     * Get servMgmtUserRoleCode.
     *
     * @return servMgmtUserRoleCode as String.
     */
    public String getServMgmtUserRoleCode()
    {
        return servMgmtUserRoleCode;
    }
    
    /**
     * Set servMgmtUserRoleCode.
     *
     * @param servMgmtUserRoleCode the value to set.
     */
    public void setServMgmtUserRoleCode(String servMgmtUserRoleCode)
    {
        this.servMgmtUserRoleCode = servMgmtUserRoleCode;
    }
    
    /**
     * Get servMgmtUserSubRoleCode.
     *
     * @return servMgmtUserSubRoleCode as String.
     */
    public String getServMgmtUserSubRoleCode()
    {
        return servMgmtUserSubRoleCode;
    }
    
    /**
     * Set servMgmtUserSubRoleCode.
     *
     * @param servMgmtUserSubRoleCode the value to set.
     */
    public void setServMgmtUserSubRoleCode(String servMgmtUserSubRoleCode)
    {
        this.servMgmtUserSubRoleCode = servMgmtUserSubRoleCode;
    }

    public WorkOrder ( String serviceWorkOrderId, int serviceWorkorderNumber,
            String serviceManagementWorkorderId, String locationTitle,
            String locationCode, String addressLine1, String addressLine2,
            String attentionLine, String city, String stateProvinceCode,
            String postalCode, String countryCode, String generalNotes,
            String workorderStatusCode, String latitude, String longitude,
            double linearDistancePlanned, String linearMeasurementUnit,
            String servMgmtGroupID, String servMgmtPersonID,
            String servMgmtUserRoleCode, String servMgmtUserSubRoleCode, String demo,
            String termicideTypeCodePlanned, String soilTypeCodePlanned, String volumeMeasurementUnit, String LastSyncTime)
    {
        super ();
        this.serviceWorkOrderId = serviceWorkOrderId;
        this.serviceWorkorderNumber = serviceWorkorderNumber;
        this.serviceManagementWorkorderId = serviceManagementWorkorderId;
        this.locationTitle = locationTitle;
        this.locationCode = locationCode;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.attentionLine = attentionLine;
        this.city = city;
        this.stateProvinceCode = stateProvinceCode;
        this.postalCode = postalCode;
        this.countryCode = countryCode;
        this.generalNotes = generalNotes;
        this.workorderStatusCode = workorderStatusCode;
      //  this.latitude = latitude;
      //  this.longitude = longitude;
        this.linearDistancePlanned = linearDistancePlanned;
        this.linearMeasurementUnit = linearMeasurementUnit;
        this.servMgmtGroupID = servMgmtGroupID;
        this.servMgmtPersonID = servMgmtPersonID;
        this.servMgmtUserRoleCode = servMgmtUserRoleCode;
        this.servMgmtUserSubRoleCode = servMgmtUserSubRoleCode;
        this.demo = demo;
        this.termicideTypeCodePlanned = termicideTypeCodePlanned;
        this.soilTypeCodePlanned = soilTypeCodePlanned;
        this.volumeMeasurementUnit = volumeMeasurementUnit;
        this.syncTime = LastSyncTime;
    }
    
    /**
     * Constructor for a WorkOrder. Takes a JSON string of attributes as parameter.
     * @param JSON
     */
    public WorkOrder(JSONObject j){
        super();
        
        try {
            this.serviceWorkOrderId = j.getString ( "ServiceWorkorderID" );
            this.serviceWorkorderNumber = j.getInt ( "ServiceWorkorderNumber" );
            this.serviceManagementWorkorderId = j.getString ( "ServMgmtWorkorderID" );
            this.locationTitle = j.getString ( "LocationTitle" );
            this.locationCode = j.getString ( "LocationCode" );
            this.addressLine1 = j.getString ( "AddressLine1" );
            this.addressLine2 = j.getString ( "AddressLine2" );
            this.attentionLine = j.getString ( "AttentionLine" );
            this.city = j.getString ( "City" );
            this.stateProvinceCode = j.getString ( "StateProvinceCode" );
            this.postalCode = j.getString ( "PostalCode" );
            this.countryCode = j.getString ( "CountryCode" );
          //  this.workorderStatusCode = j.getString ( "WorkOrderStatusCode" );
            //this.latitude = j.getString ( "Latitude" );
            //this.longitude = j.getString ( "Longitude" );
            this.linearDistancePlanned = j.getDouble ( "HT_LinearDistanceApplication_pln" );
            this.linearMeasurementUnit = j.getString("LinearMeasurementUnit_pln");
            this.servMgmtGroupID = j.getString ( "ServMgmtGroupID" );
            this.servMgmtPersonID = j.getString ( "ServMgmtPersonID" );
            this.servMgmtUserRoleCode = j.getString ( "ServMgmtUserRoleCode" );
            this.servMgmtUserSubRoleCode = j.getString ( "ServMgmtUserSubRoleCode" );
            //this.generalNotes = j.getString ( "GeneralNotes" );
            this.demo = j.getString("Demo");
            this.termicideTypeCodePlanned = j.getString("TermicideTypeCode_pln");
            this.soilTypeCodePlanned = j.getString("SoilTypeCode_pln");
            this.volumeMeasurementUnit = j.getString("VolumeMeasurementUnit_pln");
            this.syncTime = j.getString("LastSyncTime");
            
            
            
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
    
    /**
     * Constructor for a WorkOrder. Takes a JSON string of attributes as parameter.
     * @param JSON
     * @throws JSONException 
     */
    public WorkOrder(String JSON) throws JSONException{
        //super();
        
       // try {
            //JSONObject j;
            //j = new JSONObject(JSON);
            this(new JSONObject(JSON));
            
/*
            this.serviceWorkOrderId = j.getString ( "ServiceWorkorderID" );
            this.locationTitle = j.getString ( "LocationTitle" );
            this.locationCode = j.getString ( "LocationCode" );
            this.addressLine1 = j.getString ( "AddressLine1" );
            this.addressLine2 = j.getString ( "AddressLine2" );
            this.attentionLine = j.getString ( "AttentionLine" );
            this.city = j.getString ( "City" );
            this.stateProvinceCode = j.getString ( "StateProvinceCode" );
            this.postalCode = j.getString ( "PostalCode" );
            this.countryCode = j.getString ( "CountryCode" );
            //this.workorderStatusCode = j.getString ( "WorkorderStatusCode" );
            //this.latitude = j.getString ( "Latitude" );
            //this.longitude = j.getString ( "Longitude" );
            this.linearDistancePlanned = j.getDouble ( "LinearDistanceApplication_pln" );
            this.linearMeasurementUnit = j.getString("LinearMeasurementUnit_pln");
            */
            //this.generalNotes = j.getString ( "GeneralNotes" );
       // } catch (JSONException e) {
            // TODO Auto-generated catch block
        //    e.printStackTrace();
       // }
        
    }
    @Override
    public int describeContents ()
    {
        // There is nothing special in this class we need to define
        return 0;
    }

    @Override
    public void writeToParcel ( Parcel out, int flags )
    {
        out.writeString ( this.serviceWorkOrderId );
        out.writeInt ( this.serviceWorkorderNumber );
        out.writeString ( this.serviceManagementWorkorderId );
        out.writeString ( this.locationTitle );
        out.writeString ( this.locationCode );
        out.writeString ( this.addressLine1 );
        out.writeString ( this.addressLine2 );
        out.writeString ( this.attentionLine );
        out.writeString ( this.city );
        out.writeString ( this.stateProvinceCode );
        out.writeString ( this.postalCode );
        out.writeString ( this.countryCode );
        out.writeString ( this.generalNotes );
        out.writeString ( this.workorderStatusCode );
        //out.writeString ( this.latitude );
        //out.writeString ( this.longitude );
        out.writeDouble ( this.linearDistancePlanned );
        out.writeString ( this.linearMeasurementUnit );
        out.writeString ( this.servMgmtGroupID );
        out.writeString ( this.servMgmtPersonID );
        out.writeString ( this.servMgmtUserRoleCode );
        out.writeString ( this.servMgmtUserSubRoleCode );
        out.writeString ( this.demo );
        out.writeString ( this.termicideTypeCodePlanned );
        out.writeString ( this.soilTypeCodePlanned );
        out.writeString ( this.volumeMeasurementUnit );
    }
    
    public static final Parcelable.Creator<WorkOrder> CREATOR 
        = new Parcelable.Creator<WorkOrder>()
        {
            public WorkOrder createFromParcel ( Parcel in )
            {
                return new WorkOrder ( in );
            }
            public WorkOrder[] newArray ( int size )
            {
                return new WorkOrder[size];
            }
        };
        
    private WorkOrder ( Parcel in )
    {
        this.serviceWorkOrderId = in.readString();
        this.serviceWorkorderNumber = in.readInt();
        this.serviceManagementWorkorderId = in.readString();
        this.locationTitle = in.readString();
        this.locationCode = in.readString();
        this.addressLine1 = in.readString();
        this.addressLine2 = in.readString();
        this.attentionLine = in.readString();
        this.city = in.readString();
        this.stateProvinceCode = in.readString();
        this.postalCode = in.readString();
        this.countryCode = in.readString();
        this.generalNotes = in.readString();
        this.workorderStatusCode = in.readString();
      //  this.latitude = in.readString();
      //  this.longitude = in.readString();
        this.linearDistancePlanned = in.readDouble();
        this.linearMeasurementUnit = in.readString();
        this.servMgmtGroupID = in.readString();
        this.servMgmtPersonID = in.readString();
        this.servMgmtUserRoleCode = in.readString();
        this.servMgmtUserSubRoleCode = in.readString();
        this.demo = in.readString();
        this.termicideTypeCodePlanned = in.readString();
        this.soilTypeCodePlanned = in.readString();
        this.volumeMeasurementUnit = in.readString();
    }
}
