package com.evanhoe.tango.termidormsg;
import API.ADK.API_ADK;
import API.ADK.API_ADK.Device;
import API.ADK.API_ADK.Device.Controller;
import API.ADK.API_ADK.Device.MessageChannel;
import API.ADK.ConstantList;
import API.ADK.MessageStructure;
import API.ADK.ReturnCode;
import API.ADK.CANInfo;
import API.ADK.ControllerStatus;
import java.util.Date;

import com.evanhoe.tango.objs.TermidorData;
import com.evanhoe.tango.objs.WorkOrder;
import com.evanhoe.tango.utils.CommonUtilities;
import com.evanhoe.tango.utils.HPConnectionService;



/**
 * This class sends and receives data to/from a IXXAT CAN network controller according to message standards required
 * by the Termidor HP
 * @author dan.freeman
 *
 */
public class CANMessageService implements TermidorMessageInterface
{
	private static HPConnectionService hp = new HPConnectionService();
	private static Controller controller;
	private static MessageChannel rxChannel;
	private static Device device;
	private static API_ADK adk = hp.adk();
	private final static int REQUEST_DELETE_MESSAGE_ID = 275;
	private final static int REQUEST_DATA_MESSAGE_ID = 273;
	private final static int DATA_RECEIVED_MESSAGE_ID = 274;
	private final static int REQUEST_SEND_DATA_MESSAGE_ID = 272;
	private String errorString = "";
	static String memoryLocation = "0001"; //memory slot on HP (should always be slot 1 - 0001)
	private boolean sendWorkOrder(WorkOrder wo) 
	{
		int woBlock[][];
		int msgSize = 8;
		int msgID = 512;
		try {		
			String woText = buildMessageWithPadding(wo, memoryLocation);
			System.out.println("woText = " + woText);
			woBlock = CommonUtilities.stringToBlock(woText, msgSize, true);
			ReturnCode returnCode = null;
			MessageStructure msgToSend = messageSetup(msgSize, msgID); 
			for (int i=0; i<woBlock.length; i++){
				msgToSend.data = woBlock[i];
				Thread.sleep(300);
				returnCode = controller.transmitMessage(msgToSend, ConstantList.BINARY_FORMAT);
		        if (returnCode != ReturnCode.SUCCESS){
		        	return false;
		        }
			}
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean eraseData() throws InterruptedException 
	{

		ReturnCode returnCode;
		boolean resp = false;
		boolean complete=false;
		long startTime = System.currentTimeMillis();
		long elapsedTime = 0;
		/**/boolean checkConnection = ManageConnection();
		if (!checkConnection)
		{
			hp.rxCancel( controller, device, rxChannel, adk );
			returnCode = adk.deinitializeADK();
			adk = hp.adk();
			checkConnection = ManageConnection();
		}
		if (checkConnection)
		{/**/
			MessageStructure incomingMessage = new MessageStructure();
			resp = sendEraseCommandToTermidor(controller, REQUEST_DELETE_MESSAGE_ID);
			Thread.sleep(1000);
	 		if (!resp)
	 		{
	 			return false;
	 		}
	 		while(!complete && elapsedTime < 30000)
	         {      
	    		 returnCode = rxChannel.receiveMessage(incomingMessage);
	    		 if (returnCode == ReturnCode.SUCCESS)
	    		 {
	    		 switch (incomingMessage.dataLength)
				 	{
				 		case 1:    			 
				 		complete = true;
				 		break;	 		
				 	}
	    		 }
	    		 else
	    		 {
	    			 return false;
	    		 }
	 		 Thread.sleep(300);
	 		 elapsedTime = (new Date()).getTime() - startTime;
	      }
	// 	 hp.rxCancel(controller, device, rxChannel, adk);
			return true;
			
		/**/}
		else
		{
			return false;
		}/**/
	}
	private boolean sendByteMessageToTermidor(API_ADK.Device.Controller controller, int msgID) {
		int msgSize = 1;
		try {
			ReturnCode returnCode = null;
			MessageStructure msgToSend = messageSetup(msgSize, msgID); 
			msgToSend.data[0] = 0x31;
			returnCode = controller.transmitMessage(msgToSend, ConstantList.BINARY_FORMAT);
	        if (returnCode != ReturnCode.SUCCESS){
	        	return false;
	        }
	        return true;
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}		
	}
	private boolean sendEraseCommandToTermidor(API_ADK.Device.Controller controller, int msgID) {
		int msgSize = 1;
		try {
			ReturnCode returnCode = null;
			MessageStructure msgToSend = messageSetup(msgSize, msgID); 
			msgToSend.data[0] = 0x32;
			returnCode = controller.transmitMessage(msgToSend, ConstantList.BINARY_FORMAT);
	        if (returnCode != ReturnCode.SUCCESS){
	        	return false;
	        }
	        return true;
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}		
	}
	private boolean buildConnection() throws InterruptedException
	{
		ReturnCode returnCode;
		String issue = "";
		device = hp.connectToHPDevice(adk);
		if (device==null)
		{
			return false;
		}
		controller = hp.connectToHP(device, issue);
		if (controller==null)
		{
			device.disconnectDevice();
			return false;
		}
		rxChannel = hp.rxSetup(device);
		if (rxChannel==null)
		{
			API_ADK.intDeviceList.numberOfMessageChannel[0] = 0;
			returnCode = controller.deinitializeController();
			returnCode = device.disconnectDevice();
			return false;
		}
		returnCode = controller.startController();
		if (returnCode!=ReturnCode.SUCCESS)
		{
//			returnCode = rxChannel.clearReceiveFilter(ConstantList.EXTENDED_FRAME);
			returnCode = rxChannel.clearReceiveFilter(ConstantList.STANDARD_FRAME);
			Thread.sleep(1000);
			returnCode = rxChannel.deinitializeMessageChannel();
			Thread.sleep(1000);
			API_ADK.intDeviceList.numberOfMessageChannel[0] = 0;
			returnCode = controller.deinitializeController();
			returnCode = device.disconnectDevice();
			return false;
		}
			
		return true;

	}
	private boolean ManageConnection() throws InterruptedException
	{
		boolean resp = true;
		String rxString;
		CANInfo ci = new CANInfo();
		ReturnCode rt;
 		if (device != null)
 		{
 			if (controller != null) { 				
 				rt = controller.readControllerStatus(ci);
 				System.out.println("rt: " + rt.toString());
 				if (rt!= ReturnCode.SUCCESS) { 					
 					hp.destroyADK(adk, controller, device, rxChannel);
 					adk = hp.adk();
 					resp = buildConnection();
 	 				if (!resp)
 	 				{
 	 					errorString = "Unable to Connect to Unit";
 	 					return false;
 	 				}
 	 				else {
 	 					rt = controller.readControllerStatus(ci);
 	 				}
 				} else {
 					
 				}
 				
 			}
 			else
 			{
 				resp = buildConnection();
 				if (!resp)
 				{
 					errorString = "Unable to Connect to Unit";
 					return false;
 				}
 				else {
 					rt = controller.readControllerStatus(ci);
 				}
 			}
 			if(ci.controllerStatus == ControllerStatus.CONTROLLER_START)
 			{
 				hp.rxCancel(controller, device, rxChannel, adk);
 				resp = buildConnection();
 				if (!resp)
 				{
 					errorString = "Unable to Connect to Unit";
 				}
 			}
 		}
 		else
 		{
 			resp = buildConnection();
 			if (!resp)
 			{
 				errorString = "Unable to Connect to Unit";
 			}
 		}
 		return resp;
		
	}

	
	
	
	
	
	/**
	 * Sets up the header pieces of the message for transmission to CAN
	 * @param dataLength
	 * @param msgID
	 * @return
	 */
	private MessageStructure messageSetup(int dataLength, int msgID) {
		try {
			MessageStructure msgToSend = new MessageStructure();
			msgToSend.frameFormat = ConstantList.EXTENDED_FRAME;
			msgToSend.frameType = ConstantList.DATA_FRAME;
			msgToSend.dataLength = (byte) dataLength;
			msgToSend.messageID = msgID; 
			return msgToSend;
		} 
		catch  (Exception e) {
			e.printStackTrace();
			return null;
		}		
	}
	
	/**
	 * Adds required padding and removes extra characters as necessary for HP workorders
	 * @param wo
	 * @param memoryLocation
	 * @return
	 */
	private String buildMessageWithPadding(WorkOrder wo, String memoryLocation){
		try {
			StringBuilder builder = new StringBuilder();			
			String woNum = CommonUtilities.addPadding(wo.getServiceManagementWorkorderId(), 20);			
			if (woNum.length() > 20) woNum = woNum.substring(0, 20);
			String address = CommonUtilities.addPadding(wo.getAddressLine1(), 20);
			if (address.length() > 20) address = address.substring(0, 20);
			String city = CommonUtilities.addPadding(wo.getCity(), 20);
			if (city.length() > 20) city = city.substring(0, 20);
			String state = CommonUtilities.addPadding(wo.getStateProvinceCode(), 2);
			if (state.length() > 2) state = state.substring(0, 2);
			builder.append(memoryLocation);
			builder.append(",");
			builder.append(woNum);
			builder.append(",");
			builder.append(address);
			builder.append(",");
			builder.append(city);
			builder.append(",");
			builder.append(state);	
			
// taken out for test build 12-1-14
			builder.append(",");
//
			String woNumber = CommonUtilities.addPadding(wo.getServiceWorkOrderId(), 20);
			if (woNumber.length() > 20) woNumber = woNumber.substring(0, 20);
			builder.append(woNumber);
			return builder.toString();
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
   /**
    * Sends workorder to Termidor
    */
	public boolean sendWorkOrderToTermidorHP(WorkOrder wo) throws InterruptedException
	{
		String issue="";
		String rxString = null;
		ReturnCode returnCode;
		boolean resp = false;
		boolean complete=false;
		long startTime = System.currentTimeMillis();
		long elapsedTime = 0;
		boolean checkConnection = ManageConnection();
		if (!checkConnection)
		{
			hp.rxCancel( controller, device, rxChannel, adk );
			returnCode = adk.deinitializeADK();
			adk = hp.adk();
			checkConnection = ManageConnection();
		}
		if (checkConnection)
		{
			MessageStructure incomingMessage = new MessageStructure();
			resp = sendByteMessageToTermidor(controller, REQUEST_SEND_DATA_MESSAGE_ID);		
			Thread.sleep(300);
			if (!resp)
			{
				return false;
			}
			resp = false;
			boolean tryedToSendAnyway = false;
			
	    	 while(!complete && elapsedTime < 30000)
	         {      
	    		 returnCode = rxChannel.receiveMessage(incomingMessage);
	    		 if (returnCode == ReturnCode.SUCCESS)
	    		 {
	    		 switch (incomingMessage.messageID)
				 	{
	    		 case 288:
	    			 resp = sendWorkOrder(wo);
	    			 break;
	    		 case 289:
	    			 complete=true;
	    			 break;		 	
				 	default: //rxString = rxString + " " + Integer.toString(incomingMessage.messageID) + ":" + Integer.toString(incomingMessage.data[0]);
				 	}
	    		 }
	    		 else
	    		 {
	    			 // try to break gridlock of unit waiting for send
	    			 if(tryedToSendAnyway){	    			 
	    				 return false;
	    			 }else{
	    		    	 if(complete == false && resp == false){
	    		    		 tryedToSendAnyway = true;
	    		    		 resp = sendWorkOrder(wo);
	    		    	 }	    				 
	    			 }
	    		 }
	    		 Thread.sleep(300);
	    		 if (resp)
	    		 {
	    			 complete = true;
	    		 }
	    		 elapsedTime = (new Date()).getTime() - startTime;
	    		 
	         }
	//    	 hp.rxCancel(controller, device, rxChannel, adk);
	    	 
	    	 //here we assume we just missed the 288 message to send earlier and send now
	    	/* if(complete == false){
	    		 resp = sendWorkOrder(wo);
	    		 if (resp)
	    		 {
	    			 complete = true;
	    		 }
	    	 }*/
	    	 
			return complete;
		}
		else
		{
			return false;
		}
	}
	
	public void MonitorDevice()
	{
		String issue="";
		HPConnectionService hp = new HPConnectionService(controller, device, rxChannel, adk);
		adk = hp.adk();
		device = hp.connectToHPDevice(adk);
		controller = hp.connectToHP(device, issue);
//		rxChannel = hp.rxSetup(device);
		controller.startController();
	}
	
	/**
	 * Gets workorder detail from Termidor
	 */
	public TermidorData getData() throws InterruptedException
	{
		TermidorData woDetail = new TermidorData(); 
		ReturnCode returnCode;
		boolean resp = false;
		boolean complete=false;
		boolean rx=false;
     	int msgCounter = 0;
     	int totalMessages = 0;
 		int totalBytes = 0;
 		String rxString = "";
 		int messageData[][];
		messageData = new int[1][8];
		long startTime = System.currentTimeMillis();
		long elapsedTime = 0;
		boolean checkConnection = ManageConnection();
		if (!checkConnection)
		{
			rxString  = hp.rxCancel( controller, device, rxChannel, adk );
			returnCode = adk.deinitializeADK();
			adk = hp.adk();
			checkConnection = ManageConnection();
		}
		if (checkConnection)
		{
			resp = sendByteMessageToTermidor(controller, REQUEST_DATA_MESSAGE_ID);
			
			MessageStructure incomingMessage = new MessageStructure();
	    	 while(!complete && elapsedTime < 30000)
	         {      
	    		 returnCode = rxChannel.receiveMessage(incomingMessage);
	    		 if (returnCode == ReturnCode.SUCCESS)
	    		 {
	    			 if (incomingMessage.dataLength == 8)    				
	    			 {    				
		                 if (msgCounter<totalMessages)
		                 {	         
		                	 messageData = new int[totalMessages][8];
		                	 messageData[msgCounter] = incomingMessage.data;
		                	 rx = true;	                 	
		                 	msgCounter += 1;     
		                 	Thread.sleep(300);
		                 } 
		                 incomingMessage = new MessageStructure();
		                 while(rxChannel.receiveMessage(incomingMessage) == ReturnCode.SUCCESS && !complete && elapsedTime < 45000)
		                 {             
		                     if (msgCounter<totalMessages)
		                     {
		                     	if (incomingMessage.dataLength == 8)
		                     	{
	
		                     		messageData[msgCounter] = incomingMessage.data;
		                     		msgCounter += 1;		                     		
		                     		rx = true;
		                     	}               		
		                     	if(msgCounter==totalMessages && rx)
		                     	{
		                     		resp = sendByteMessageToTermidor(controller, DATA_RECEIVED_MESSAGE_ID);
		                     		rxString = CommonUtilities.blockToString(messageData, true);
		                     		System.out.println(rxString);
		                     		woDetail = splitWorkOrderDetail(rxString);
		                     		complete = true;		  
		                     		
		                     		// add an erase here
		                     		sendEraseCommandToTermidor(controller, REQUEST_DELETE_MESSAGE_ID);
		                     	}
		                     } 
		                     Thread.sleep(300);
		                     elapsedTime = (new Date()).getTime() - startTime;
		                     incomingMessage = new MessageStructure();
		                 }		 
	    			 }
		    		 else
		             {
			    		 switch (incomingMessage.messageID)
						 	{
						 		case 290:
						 		totalMessages = 31;
						 		break;
						 	}
		    		
		                 }
	    			 
	    		 	}
	    		 else {elapsedTime = (new Date()).getTime() - startTime;}
	    		 Thread.sleep(300);
	    		 elapsedTime = (new Date()).getTime() - startTime;
	         }
	    	 if(complete != true){
	 			woDetail.setLastErrorMessage("Unable to Connect to Unit");
				return woDetail;	    		 
	    	 }
	    	 if ( woDetail.getWorkOrderID() == null ||
                  woDetail.getWorkOrderID().toString().equals("") )
	    	 {
	    		 woDetail.setLastErrorMessage("SUCCESS");	    		 
	    		 return null;
	    	 }
	    	woDetail.setLastErrorMessage("SUCCESS");
	 		return woDetail;
		}
		else
		{
			woDetail.setLastErrorMessage("Unable to Connect to Unit");
			return woDetail;
		}
	}
    
	
	/**
	 * Splits workorder detail string from Termidor into TermidorData object
	 * @param detailString
	 * @return
	 */
	private TermidorData splitWorkOrderDetail(String detailString)
	{		
		detailString = detailString.substring(0,216);		
		boolean locationDone = false; 	
		boolean htModeEnabled = false; 
		boolean saModeEnabled = false; 
		boolean pumpAlarm = false; 
		boolean waterAlarm = false; 
		boolean htFlowInSAMode = false;
		String location = detailString.substring(0,4).trim();
		String pidDisplayFirmware = detailString.substring(5,9).trim();
		String baseControllerFirmware = detailString.substring(10,14).trim();
		String baseDisplayFirm = detailString.substring(15,19).trim();
		String unitID = detailString.substring(20,24).trim();
		String workOrderNumber = detailString.substring(25,45).trim();
		String address = detailString.substring(46,66).trim();
		String city = detailString.substring(67,87).trim();
		String state = detailString.substring(88,90).trim();
		String workOrderStartDateStamp= detailString.substring(91,101).trim();
		String productApplied = detailString.substring(102,118).trim();
		StringBuilder builder = new StringBuilder();
		for (int i=0; i<productApplied.length(); i++)
		{
			if((int)productApplied.charAt(i)!=250)
			{
				builder.append(productApplied.charAt(i));				
			}
		}
		productApplied = builder.toString();
		short soilType = Short.parseShort(detailString.substring(119,120));
		float injectorTimer = Float.parseFloat(detailString.substring(121,129));
		float htModePumpVolume = Float.parseFloat(detailString.substring(130,136));
		int htModeInjectorCount = Integer.parseInt(detailString.substring(137,143));
		float htModeActiveIngredientUsage = Float.parseFloat(detailString.substring(144,150));
		float htModeWaterUsage = Float.parseFloat(detailString.substring(151,157));
		float saModeActiveIngredientUsage = Float.parseFloat(detailString.substring(158,164));
		float saModeWaterUsage = Float.parseFloat(detailString.substring(165,171));
		String workOrderStartTime = detailString.substring(172,177).trim();
		int timeToComplete = Integer.parseInt(detailString.substring(178,183));
		
		int locationDoneInt = Integer.parseInt(detailString.substring(184,185));
		int htModeEnabledInt = Integer.parseInt(detailString.substring(186,187));
		int saModeEnabledInt = Integer.parseInt(detailString.substring(187,188));
		
		int pumpAlarmInt = Integer.parseInt(detailString.substring(189,190));
		int waterAlarmInt = Integer.parseInt(detailString.substring(190,191));
		int htFlowInSAModeInt = Integer.parseInt(detailString.substring(191,192));
		
		String workOrderID = detailString.substring(195,215).trim();
		
		
		System.out.println(location + "| " + pidDisplayFirmware + "| " + baseControllerFirmware + "| " + baseDisplayFirm + "| " + unitID + "| " + workOrderNumber
				 + "| " + address + "| " + city + "| " + state + "| " + workOrderStartDateStamp + "| " + productApplied + "| " + Short.toString(soilType) + "| " + 
				Float.toString(injectorTimer) + "| " + Float.toString(htModePumpVolume) + "| " + Integer.toString(htModeInjectorCount) + "| " + Float.toString(htModeActiveIngredientUsage)
				 + "| " + Float.toString(htModeWaterUsage) + "| " + Float.toString(htModeActiveIngredientUsage) + "| " + Float.toString(saModeActiveIngredientUsage)
				 + "| " + Float.toString(saModeWaterUsage) + "| " + workOrderStartTime  + "| " + Integer.toString(timeToComplete)); 
		
		
		if (locationDoneInt==1)
		{
			locationDone = true;
		}
		else
		{
			locationDone = false;
		}
		if (htModeEnabledInt==1)
		{
			htModeEnabled = true;
		}
		else
		{
			htModeEnabled = false;
		}
		if (saModeEnabledInt==1)
		{
			saModeEnabled = true;
		}
		else
		{
			saModeEnabled = false;
		}
		if (pumpAlarmInt==1)
		{
			pumpAlarm = true;
		}
		else
		{
			pumpAlarm = false;
		}
		if (waterAlarmInt==1)
		{
			waterAlarm = true;
		}
		else
		{
			waterAlarm = false;
		}
		if (htFlowInSAModeInt==1)
		{
			htFlowInSAMode = true;
		}
		else
		{
			htFlowInSAMode = false;
		}
		TermidorData woDetail = new TermidorData(location,pidDisplayFirmware,baseControllerFirmware,baseDisplayFirm,unitID,
												workOrderNumber,address,city,state,workOrderStartDateStamp,
												productApplied,soilType,injectorTimer,htModePumpVolume,htModeInjectorCount,
												htModeActiveIngredientUsage,htModeWaterUsage,saModeActiveIngredientUsage,saModeWaterUsage,workOrderStartTime,
												timeToComplete,locationDone,htModeEnabled,saModeEnabled,pumpAlarm,
												waterAlarm, htFlowInSAMode, workOrderID);
		
		return woDetail;
		
	}
	
	
    public boolean initialize ( String unitAddress ){return true;}
    public void deInitialize (  ){}
}

