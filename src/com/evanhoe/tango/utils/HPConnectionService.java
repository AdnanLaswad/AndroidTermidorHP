package com.evanhoe.tango.utils;


import API.ADK.API_ADK;
import API.ADK.API_ADK.Device;
import API.ADK.API_ADK.Device.Controller;
import API.ADK.API_ADK.Device.MessageChannel;
import API.ADK.ReturnCode;
import API.ADK.ConstantList;
import com.evanhoe.tango.TangoApplication;


public class HPConnectionService {
//	private static API_ADK ADK = null;
	private Controller controller;
	private Device device;
	private MessageChannel channel;
	private API_ADK ADK;
	
	
	public HPConnectionService(Controller controller, Device device,
		MessageChannel channel, API_ADK ADK) {
	super();
	this.controller = controller;
	this.device = device;
	this.channel = channel;
	this.ADK = ADK;
}
	
	public HPConnectionService()
	{
		//empty
	}

	
	
	
	private static int filterMsgID = 512; //0x200
	
	 
	
	
	
	public API_ADK.Device.Controller connectToHP (API_ADK.Device hpDevice, String issue) 
	{
		API_ADK.Device.Controller hpController = null;
		API_ADK.Device.MessageChannel rxChannel = null;	
		try 
		{	
			hpController = initController(hpDevice);			
			if (hpController==null)
			{
				issue = "init fail";
//				return null;
			}
			rxChannel = hpDevice.createMessageChannelObject((byte) 0);
			if (rxChannel==null) {
				issue = "rxChannel fail";
//				return null;
			}
			issue = "done";
			return hpController;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}

			 
		
	}
	
	public API_ADK adk()
	{
		API_ADK adk_temp;
		ReturnCode returnCode;
		adk_temp = API_ADK.createAPI_ADKObject();
		if (adk_temp==null) 
		{
			System.out.println("adk create failed");
			return null;
			
		}
		returnCode = adk_temp.initializeADK();
		if (returnCode!=ReturnCode.SUCCESS)
		{
			System.out.println("init failed: " + returnCode.toString());
			return null;
		}
		return adk_temp;
	}
	
	public API_ADK.Device connectToHPDevice(API_ADK ADK)
	{
		String macAddress="";
		if (((TangoApplication) TangoApplication.getTangoApplicationContext()).getUnitMacAddress() != null )
		{
			macAddress = ((TangoApplication) TangoApplication.getTangoApplicationContext()).getUnitMacAddress();
		}		
		else
		{
			return null;
		}
		API_ADK.Device hpDevice = null;
		ReturnCode returnCode = null;
		int selectedDevice = 99; //no device selected
		try 
		{	
			
			ADK.searchAvailableDevice(API_ADK.intDeviceList);
        	if (API_ADK.intDeviceList.numberOfDevice == 0) 
        	{
//        		return null;
        	}
			for (int i=0; i<API_ADK.intDeviceList.numberOfDevice; i++) 
			{
				if (API_ADK.intDeviceList.deviceMACAddress[i].equals(macAddress)) 
	        	{
	        		//make this the one and exit the loop
					System.out.println(API_ADK.intDeviceList.deviceMACAddress[i]);
					selectedDevice = i;
					break;
	        	}				
	        }
//		selectedDevice = 0;
			if (selectedDevice != 99)
			{
				hpDevice= ADK.createDeviceObject((byte)selectedDevice);  
			}
			else
			{
				return null;
			}
			if (hpDevice == null) 
			{
				return null;		
			}
			returnCode = hpDevice.connectDevice();
			if (returnCode != ReturnCode.SUCCESS)
			{
//				return null;
			}
			return hpDevice;
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
			 
	}
	private static API_ADK.Device.Controller initController (API_ADK.Device hpDevice)
	{
		API_ADK.Device.Controller hpController = null;
		ReturnCode returnCode = null;
		try 
		{
			hpController = hpDevice.createControllerObject((byte)0);
			if (hpController == null) 
			{
				return null;
			}
			returnCode = hpController.initializeController(250, ConstantList.SOFTWARE_FILTER);
			if (returnCode != ReturnCode.SUCCESS)
			{
				return null;
			}
			return hpController;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public String rxCancel (API_ADK.Device.Controller hpController, API_ADK.Device hpDevice, API_ADK.Device.MessageChannel rxChannel, API_ADK hpADK) throws InterruptedException
	{
		if (hpController!=null)
		{
			if (hpDevice!=null)
			{
				ReturnCode returnCode;
				returnCode = hpController.stopController();
				Thread.sleep(500);
				if (returnCode != ReturnCode.SUCCESS) 
				{
//					return "1 " + returnCode.toString();
				}
				returnCode = rxChannel.clearReceiveFilter(ConstantList.STANDARD_FRAME);
				Thread.sleep(500);
				if (returnCode != ReturnCode.SUCCESS) 
				{
//					return "2 " + returnCode.toString();
				}
				returnCode = rxChannel.deinitializeMessageChannel();
				Thread.sleep(500);
				if (returnCode != ReturnCode.SUCCESS) 
				{
//					return "3 " + returnCode.toString();
				}
				API_ADK.intDeviceList.numberOfMessageChannel[0] = 0;
				returnCode = hpController.deinitializeController();				
				Thread.sleep(500);
				if (returnCode != ReturnCode.SUCCESS) 
				{
					return "4 " + returnCode.toString() + " --- " + API_ADK.intDeviceList.numberOfMessageChannel[0];
				}
				returnCode = hpDevice.disconnectDevice();
				Thread.sleep(500);
				if (returnCode != ReturnCode.SUCCESS) 
				{
					return "5 " + returnCode.toString();
				}
//				returnCode = hpADK.deinitializeADK();
				return returnCode.toString();

			}
		}
		return "not initialized";
	}
	public boolean destroyADK(API_ADK adk, Controller hpController, Device hpDevice, MessageChannel hpChannel) throws InterruptedException
	{
		ReturnCode returnCode;
		String returnString;
		returnString = rxCancel(hpController, hpDevice, hpChannel, adk);
		System.out.println("cancel: " + returnString);
		returnCode = adk.deinitializeADK();
		System.out.println("destroy: " + returnCode.toString());
		hpController = null;
		hpDevice = null;
		hpChannel = null;
		adk = null;
		return true;
	}
	public API_ADK.Device.MessageChannel rxSetup(API_ADK.Device hpDevice)
///	public static String rxSetup(API_ADK.Device hpDevice)
	{
		API_ADK.Device.MessageChannel rxChannel = null;
		ReturnCode returnCode = null;
		try
		{
			rxChannel = hpDevice.createMessageChannelObject((byte) 0);
			if (rxChannel == null)
			{
				return null;
			}
			returnCode = rxChannel.initializeMessageChannel();
	        if (returnCode != ReturnCode.SUCCESS)
	        {
	        	return null;
	        }
	        returnCode = rxChannel.addReceiveFilter(API_ADK.EXTENDED_FRAME, 0x120, API_ADK.DATA_FRAME);
	        if (returnCode != ReturnCode.SUCCESS)
	        {
	        	return null;	        	
	        }
	        returnCode = rxChannel.addReceiveFilter(API_ADK.EXTENDED_FRAME, 0x121, API_ADK.DATA_FRAME);
	        if (returnCode != ReturnCode.SUCCESS)
	        {
	        	return null;	        	
	        }
	        returnCode = rxChannel.addReceiveFilter(API_ADK.EXTENDED_FRAME, 0x122, API_ADK.DATA_FRAME);
	        if (returnCode != ReturnCode.SUCCESS)
	        {
	        	return null;	        	
	        }

	        returnCode = rxChannel.addReceiveFilter(API_ADK.EXTENDED_FRAME, 0x123, API_ADK.DATA_FRAME);
	        if (returnCode != ReturnCode.SUCCESS)
	        {
	        	return null;	        	
	        }

	        returnCode = rxChannel.addReceiveFilter(API_ADK.EXTENDED_FRAME, 0x200, API_ADK.DATA_FRAME);
	        if (returnCode != ReturnCode.SUCCESS)
	        {
	        	return null;	        	
	        }

	        returnCode = rxChannel.setReceiveFilter(API_ADK.STANDARD_FRAME, true);
	        if (returnCode != ReturnCode.SUCCESS)
	        {
	        	return null;	        	
	        }
	        return rxChannel;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	

	
	
	public Controller getController() {
		return controller;
	}

	public void setController(Controller controller) {
		this.controller = controller;
	}

	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}

	public MessageChannel getChannel() {
		return channel;
	}

	public void setChannel(MessageChannel channel) {
		this.channel = channel;
	}

	public API_ADK getADK() {
		return ADK;
	}

	public void setADK(API_ADK aDK) {
		ADK = aDK;
	}


}
