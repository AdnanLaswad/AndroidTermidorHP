package com.evanhoe.tango.objs;

public class InjectionStation
{
    private String InjectionStationID;
    private String IPAddress;
    private String ControllerIPAddress;
    private String InjectionStationName;
    private String WirelessFirmwareVersion;
    private String InjectionDisplayFirmwareVersion;
    private String ControllerFirmwareVersion;
	private String BaseDisplayFirmwareVersion;
    private String TelemetryVersion;
    private String BaseUnitID;
    private String ControllerID;
    private String InjectorID;
    private String WirelessID;
    private String ToolStatusCode;
    private String GeneralNotes;
    private String LocationNotes;
    
    
    public InjectionStation(String injectionStationID, String iPAddress,
			String controllerIPAddress, String injectionStationName) {
		super();
		InjectionStationID = injectionStationID;
		IPAddress = iPAddress;
		ControllerIPAddress = controllerIPAddress;
		InjectionStationName = injectionStationName;
	}
    
    /**
     * Get InjectionStationID.
     *
     * @return InjectionStationID as String.
     */
    public String getInjectionStationID()
    {
        return InjectionStationID;
    }
    
    /**
     * Set InjectionStationID.
     *
     * @param InjectionStationID the value to set.
     */
    public void setInjectionStationID(String InjectionStationID)
    {
        this.InjectionStationID = InjectionStationID;
    }
    
    /**
     * Get IPAddress.
     *
     * @return IPAddress as String.
     */
    public String getIPAddress()
    {
        return IPAddress;
    }
    
    /**
     * Set IPAddress.
     *
     * @param IPAddress the value to set.
     */
    public void setIPAddress(String IPAddress)
    {
        this.IPAddress = IPAddress;
    }
    
    /**
     * Get ControllerIPAddress.
     *
     * @return ControllerIPAddress as String.
     */
    public String getControllerIPAddress()
    {
        return ControllerIPAddress;
    }
    
    /**
     * Set ControllerIPAddress.
     *
     * @param ControllerIPAddress the value to set.
     */
    public void setControllerIPAddress(String ControllerIPAddress)
    {
        this.ControllerIPAddress = ControllerIPAddress;
    }
    
    /**
     * Get InjectionStationName.
     *
     * @return InjectionStationName as String.
     */
    public String getInjectionStationName()
    {
        return InjectionStationName;
    }
    
    /**
     * Set InjectionStationName.
     *
     * @param InjectionStationName the value to set.
     */
    public void setInjectionStationName(String InjectionStationName)
    {
        this.InjectionStationName = InjectionStationName;
    }
    
    /**
     * Get WirelessFirmwareVersion.
     *
     * @return WirelessFirmwareVersion as String.
     */
    public String getWirelessFirmwareVersion()
    {
        return WirelessFirmwareVersion;
    }
    
    /**
     * Set WirelessFirmwareVersion.
     *
     * @param WirelessFirmwareVersion the value to set.
     */
    public void setWirelessFirmwareVersion(String WirelessFirmwareVersion)
    {
        this.WirelessFirmwareVersion = WirelessFirmwareVersion;
    }
    
    /**
     * Get InjectionDisplayFirmwareVersion.
     *
     * @return InjectionDisplayFirmwareVersion as String.
     */
    public String getInjectionDisplayFirmwareVersion()
    {
        return InjectionDisplayFirmwareVersion;
    }
    
    /**
     * Set InjectionDisplayFirmwareVersion.
     *
     * @param InjectionDisplayFirmwareVersion the value to set.
     */
    public void setInjectionDisplayFirmwareVersion(String InjectionDisplayFirmwareVersion)
    {
        this.InjectionDisplayFirmwareVersion = InjectionDisplayFirmwareVersion;
    }
    
    /**
     * Get ControllerFirmwareVersion.
     *
     * @return ControllerFirmwareVersion as String.
     */
    public String getControllerFirmwareVersion()
    {
        return ControllerFirmwareVersion;
    }
    
    /**
     * Set ControllerFirmwareVersion.
     *
     * @param ControllerFirmwareVersion the value to set.
     */
    public void setControllerFirmwareVersion(String ControllerFirmwareVersion)
    {
        this.ControllerFirmwareVersion = ControllerFirmwareVersion;
    }
    
    /**
     * Get BaseDisplayFirmwareVersion.
     *
     * @return BaseDisplayFirmwareVersion as String.
     */
    public String getBaseDisplayFirmwareVersion()
    {
        return BaseDisplayFirmwareVersion;
    }
    
    /**
     * Set BaseDisplayFirmwareVersion.
     *
     * @param BaseDisplayFirmwareVersion the value to set.
     */
    public void setBaseDisplayFirmwareVersion(String BaseDisplayFirmwareVersion)
    {
        this.BaseDisplayFirmwareVersion = BaseDisplayFirmwareVersion;
    }
    
    /**
     * Get TelemetryVersion.
     *
     * @return TelemetryVersion as String.
     */
    public String getTelemetryVersion()
    {
        return TelemetryVersion;
    }
    
    /**
     * Set TelemetryVersion.
     *
     * @param TelemetryVersion the value to set.
     */
    public void setTelemetryVersion(String TelemetryVersion)
    {
        this.TelemetryVersion = TelemetryVersion;
    }
    
    /**
     * Get BaseUnitID.
     *
     * @return BaseUnitID as String.
     */
    public String getBaseUnitID()
    {
        return BaseUnitID;
    }
    
    /**
     * Set BaseUnitID.
     *
     * @param BaseUnitID the value to set.
     */
    public void setBaseUnitID(String BaseUnitID)
    {
        this.BaseUnitID = BaseUnitID;
    }
    
    /**
     * Get ControllerID.
     *
     * @return ControllerID as String.
     */
    public String getControllerID()
    {
        return ControllerID;
    }
    
    /**
     * Set ControllerID.
     *
     * @param ControllerID the value to set.
     */
    public void setControllerID(String ControllerID)
    {
        this.ControllerID = ControllerID;
    }
    
    /**
     * Get InjectorID.
     *
     * @return InjectorID as String.
     */
    public String getInjectorID()
    {
        return InjectorID;
    }
    
    /**
     * Set InjectorID.
     *
     * @param InjectorID the value to set.
     */
    public void setInjectorID(String InjectorID)
    {
        this.InjectorID = InjectorID;
    }
    
    /**
     * Get WirelessID.
     *
     * @return WirelessID as String.
     */
    public String getWirelessID()
    {
        return WirelessID;
    }
    
    /**
     * Set WirelessID.
     *
     * @param WirelessID the value to set.
     */
    public void setWirelessID(String WirelessID)
    {
        this.WirelessID = WirelessID;
    }
    
    /**
     * Get ToolStatusCode.
     *
     * @return ToolStatusCode as String.
     */
    public String getToolStatusCode()
    {
        return ToolStatusCode;
    }
    
    /**
     * Set ToolStatusCode.
     *
     * @param ToolStatusCode the value to set.
     */
    public void setToolStatusCode(String ToolStatusCode)
    {
        this.ToolStatusCode = ToolStatusCode;
    }
    
    /**
     * Get GeneralNotes.
     *
     * @return GeneralNotes as String.
     */
    public String getGeneralNotes()
    {
        return GeneralNotes;
    }
    
    /**
     * Set GeneralNotes.
     *
     * @param GeneralNotes the value to set.
     */
    public void setGeneralNotes(String GeneralNotes)
    {
        this.GeneralNotes = GeneralNotes;
    }
    
    /**
     * Get LocationNotes.
     *
     * @return LocationNotes as String.
     */
    public String getLocationNotes()
    {
        return LocationNotes;
    }
    
    /**
     * Set LocationNotes.
     *
     * @param LocationNotes the value to set.
     */
    public void setLocationNotes(String LocationNotes)
    {
        this.LocationNotes = LocationNotes;
    }
}
