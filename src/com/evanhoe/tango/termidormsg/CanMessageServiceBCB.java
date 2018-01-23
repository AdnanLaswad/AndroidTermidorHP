package com.evanhoe.tango.termidormsg;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;

import com.evanhoe.tango.TangoApplication;
import com.evanhoe.tango.objs.TermidorData;
import com.evanhoe.tango.objs.WorkOrder;
import com.evanhoe.tango.utils.CommonUtilities;



import API.ADK.API_ADK;
import API.ADK.ConstantList;
import API.ADK.MessageStructure;
import API.ADK.ResponseBuffer;
import API.ADK.ReturnCode;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;


public class CanMessageServiceBCB implements TermidorMessageInterface{
	
    private BluetoothAdapter mBluetoothAdapter = null;
    private BluetoothSocket btSocket = null;
    private BluetoothDevice device = null;
    private OutputStream outStream = null;
    private InputStream inputStream = null;
    private static String address = null;
    
    private Thread rxThread = null;
    
    private static ConcurrentLinkedQueue<TermidorData> _GetDataQueue = new ConcurrentLinkedQueue<TermidorData>();
    private static ConcurrentLinkedQueue<WorkOrder> _SendDataQueue = new ConcurrentLinkedQueue<WorkOrder>();
    private static ConcurrentLinkedQueue<String> _StatusMessagesQueue = new ConcurrentLinkedQueue<String>();
    private static ConcurrentLinkedQueue<String> _ToThreadMessagesQueue = new ConcurrentLinkedQueue<String>();
    private static ConcurrentLinkedQueue<String> _ToThreadMessagesQueue2 = new ConcurrentLinkedQueue<String>();
    
    
    private int REQUEST_DELETE_MESSAGE_ID = 275;//hex 113
    private int REQUEST_DATA_MESSAGE_ID = 273;//111
    private int DATA_RECEIVED_MESSAGE_ID = 274;//112
    private int REQUEST_SEND_DATA_MESSAGE_ID = 272;//110
    private String errorString = "";    
    private String memoryLocation = "0001";
    
    public boolean eraseData() throws InterruptedException 
	{

			boolean resp = false;
			boolean complete=false;

			MessageStructure incomingMessage;
			resp = sendEraseByteMessageToTermidorTest2();
			Thread.sleep(5000);
	 		if (!resp)
	 		{
	 			incomingMessages.clear();
	 			return false;
	 		}
	 		
	 		incomingMessages.clear();
			return true;
			
	}    
    
    public boolean eraseData2() throws InterruptedException 
	{

		ReturnCode returnCode;
		boolean resp = false;
		boolean complete=false;
		long startTime = System.currentTimeMillis();
		long elapsedTime = 0;


			MessageStructure incomingMessage;
			resp = sendEraseCommandToTermidor2(REQUEST_DELETE_MESSAGE_ID);
			Thread.sleep(300);
	 		if (!resp)
	 		{
	 			incomingMessages.clear();
	 			return false;
	 		}
	 		while(!complete && elapsedTime < 20000)
	         {      
	    		 if(incomingMessages.size()==0){
	    			 Thread.sleep(500);
	    			 elapsedTime = (new Date()).getTime() - startTime;
	    			 continue;
	    		 }
	    		 
	    		 incomingMessage = incomingMessages.poll();
	    		 Log.d("TAG", "READ inputStream: " + incomingMessage.messageID + " " + incomingMessage.dataLength + " " + incomingMessage.data[0]+ " " + incomingMessage.data[1]+ " " + incomingMessage.data[2]+ " " + incomingMessage.data[3]+ " " + incomingMessage.data[4]+ " " + incomingMessage.data[5]+ " " + incomingMessage.data[6]+ " " + incomingMessage.data[7] + " buffersize: " + incomingMessages.size());
	    		 

	    		 switch (incomingMessage.dataLength)
				 	{
				 		case 1:    			 
				 		complete = true;
				 		break;	 		
				 	}

	 		 Thread.sleep(500);
	 		 elapsedTime = (new Date()).getTime() - startTime;
	      }
	// 	 hp.rxCancel(controller, device, rxChannel, adk);
	 		incomingMessages.clear();
			return true;
			
	}
    
    public boolean sendWorkOrderToTermidorHP(WorkOrder wo) throws InterruptedException
	{
    	boolean complete=false;
    	boolean resp = false;
		long startTime = System.currentTimeMillis();
		long elapsedTime = 0;
    	
		incomingMessages.clear();
		
    	MessageStructure incomingMessage = new MessageStructure();
		//sendByteMessageToTermidor2(REQUEST_SEND_DATA_MESSAGE_ID);		
    	sendByteMessageToTermidorTest3();
    	Thread.sleep(300);
		
		
    	 while(!complete && elapsedTime < 30000)
         {      
    		 if(incomingMessages.size()==0){
    			 Thread.sleep(500);
    			 elapsedTime = (new Date()).getTime() - startTime;
    			 continue;
    		 }
    		 
    		 incomingMessage = incomingMessages.poll();
    		 Log.d("TAG", "READ inputStream: " + incomingMessage.messageID + " " + incomingMessage.dataLength + " " + incomingMessage.data[0]+ " " + incomingMessage.data[1]+ " " + incomingMessage.data[2]+ " " + incomingMessage.data[3]+ " " + incomingMessage.data[4]+ " " + incomingMessage.data[5]+ " " + incomingMessage.data[6]+ " " + incomingMessage.data[7] + " buffersize: " + incomingMessages.size());
    		 

    		 switch (incomingMessage.messageID)
			 	{
    		 case 288:
    			 resp = sendWorkOrderBCB(wo);
    			 break;
    		 case 289:
    			 complete=true;
    			 break;		 	
			 	default: //rxString = rxString + " " + Integer.toString(incomingMessage.messageID) + ":" + Integer.toString(incomingMessage.data[0]);
			 	}
    		 
    		 Thread.sleep(300);
    		 if (resp)
    		 {
    			 complete = true;
    		 }
    		 elapsedTime = (new Date()).getTime() - startTime;
         }
    	
    	return complete;
    	
	}
    
    public boolean sendWorkOrderToTermidorHP2(WorkOrder wo) throws InterruptedException
	{
		String issue="";
		String rxString = null;
		ReturnCode returnCode;
		boolean resp = false;
		boolean complete=false;
		long startTime = System.currentTimeMillis();
		long elapsedTime = 0;

			//Thread.sleep(1000);
			incomingMessages.clear();
			
			MessageStructure incomingMessage = new MessageStructure();
			resp = sendByteMessageToTermidor2(REQUEST_SEND_DATA_MESSAGE_ID);		
			Thread.sleep(300);
			if (!resp)
			{
				return false;
			}
			resp = false;
			boolean tryedToSendAnyway = false;
			
	    	 while(!complete && elapsedTime < 30000)
	         {      
	    		 if(incomingMessages.size()==0){
	    			 Thread.sleep(500);
	    			 elapsedTime = (new Date()).getTime() - startTime;
	    			 continue;
	    		 }
	    		 
	    		 incomingMessage = incomingMessages.poll();
	    		 Log.d("TAG", "READ inputStream: " + incomingMessage.messageID + " " + incomingMessage.dataLength + " " + incomingMessage.data[0]+ " " + incomingMessage.data[1]+ " " + incomingMessage.data[2]+ " " + incomingMessage.data[3]+ " " + incomingMessage.data[4]+ " " + incomingMessage.data[5]+ " " + incomingMessage.data[6]+ " " + incomingMessage.data[7] + " buffersize: " + incomingMessages.size());
	    		 

	    		 switch (incomingMessage.messageID)
				 	{
	    		 case 288:
	    			 resp = sendWorkOrder2(wo);
	    			 break;
	    		 case 289:
	    			 complete=true;
	    			 break;		 	
				 	default: //rxString = rxString + " " + Integer.toString(incomingMessage.messageID) + ":" + Integer.toString(incomingMessage.data[0]);
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
    
    public static int unsignedByteToInt(byte b) {
        return (int) b & 0xFF;
      }
    
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

			//resp = sendByteMessageToTermidor2(REQUEST_DATA_MESSAGE_ID);
		
			//resp = sendEraseByteMessageToTermidor(REQUEST_DELETE_MESSAGE_ID);
		
			//resp = sendEraseCommandToTermidor2(REQUEST_DELETE_MESSAGE_ID);

			//resp = sendByteMessageToTermidor2(REQUEST_DATA_MESSAGE_ID);
			resp = sendGetByteMessageToTermidorTest3();
			
			//Thread.sleep(5000);
			//resp = sendEraseCommandToTermidor(REQUEST_DELETE_MESSAGE_ID);
			
			MessageStructure incomingMessage;
	    	 while(!complete && elapsedTime < 30000)
	         {      
	    		 if(incomingMessages.size()==0){
	    			 Thread.sleep(500);
	    			 elapsedTime = (new Date()).getTime() - startTime;
	    			 continue;
	    		 }
	    		 
	    		 incomingMessage = incomingMessages.poll();
	    		 //Log.d("TAG", "inputStream = " + incomingMessage.messageID);
	    		 Log.d("TAG", "READ inputStream: " + incomingMessage.messageID + " " + incomingMessage.dataLength + " " + incomingMessage.data[0]+ " " + incomingMessage.data[1]+ " " + incomingMessage.data[2]+ " " + incomingMessage.data[3]+ " " + incomingMessage.data[4]+ " " + incomingMessage.data[5]+ " " + incomingMessage.data[6]+ " " + incomingMessage.data[7] + " buffersize: " + incomingMessages.size());
	    		 if (incomingMessage.messageID == 256 || incomingMessage.messageID == 290){
	    			 if (incomingMessage.messageID == 256)    				
	    			 {    				
		                 if (msgCounter<totalMessages)
		                 {	         
		                	 messageData = new int[totalMessages][8];
		                	 messageData[msgCounter] = incomingMessage.data;
		                	 rx = true;	                 	
		                 	msgCounter += 1;     
		                 	//Thread.sleep(300);
		                 } 
		                 
		                 while(!complete && elapsedTime < 45000)
		                 {       
		                	 
		    	    		 if(incomingMessages.size()==0){
		    	    			 Thread.sleep(500);
		    	    			 elapsedTime = (new Date()).getTime() - startTime;
		    	    			 continue;
		    	    		 }
		    	    		 
		    	    		 incomingMessage = incomingMessages.poll();
		    	    		 Log.d("TAG", "READ inputStream: " + incomingMessage.messageID + " " + incomingMessage.dataLength + " " + incomingMessage.data[0]+ " " + incomingMessage.data[1]+ " " + incomingMessage.data[2]+ " " + incomingMessage.data[3]+ " " + incomingMessage.data[4]+ " " + incomingMessage.data[5]+ " " + incomingMessage.data[6]+ " " + incomingMessage.data[7] + " buffersize: " + incomingMessages.size());
		                	 
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
		                     		//resp = sendByteMessageToTermidor(DATA_RECEIVED_MESSAGE_ID);
		                     		resp = sendReceivedByteMessageToTermidorBCB();
		                     		rxString = CommonUtilities.blockToString(messageData, true);
		                     		System.out.println(rxString);
		                     		woDetail = splitWorkOrderDetail(rxString);
		                     		complete = true;		  
		                     		
		                     		// add an erase here
		                     		//sendEraseCommandToTermidor2(REQUEST_DELETE_MESSAGE_ID);
		                     		sendEraseByteMessageToTermidorTest2();
		                     	}
		                     } 
		                     //Thread.sleep(300);
		                     elapsedTime = (new Date()).getTime() - startTime;
		                     
		                 }		 
	    			 }
		    		 else
		             {
			    		 switch (incomingMessage.messageID)
						 	{
						 		case 290:
						 		totalMessages = 32;
						 		break;
						 	}
		    		
		              }
	    		 }

	    		 //Thread.sleep(300);
	    		 elapsedTime = (new Date()).getTime() - startTime;
	         }
	    	 if(complete != true){
	 			woDetail.setLastErrorMessage("Unable to Connect to Unit");
	 			incomingMessages.clear();
				return woDetail;	    		 
	    	 }
	    	 if ( woDetail.getWorkOrderID() == null ||
                  woDetail.getWorkOrderID().toString().equals("") )
	    	 {
	    		 woDetail.setLastErrorMessage("SUCCESS");	 
	    		 incomingMessages.clear();
	    		 return null;
	    	 }
	    	woDetail.setLastErrorMessage("SUCCESS");
	    	incomingMessages.clear();
	 		return woDetail;

	}
    
    public TermidorData getData2() throws InterruptedException
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

			//resp = sendByteMessageToTermidor2(REQUEST_DATA_MESSAGE_ID);
		
			//resp = sendEraseByteMessageToTermidor(REQUEST_DELETE_MESSAGE_ID);
		
			//resp = sendEraseCommandToTermidor2(REQUEST_DELETE_MESSAGE_ID);

			resp = sendByteMessageToTermidor2(REQUEST_DATA_MESSAGE_ID);
			
			//Thread.sleep(5000);
			//resp = sendEraseCommandToTermidor(REQUEST_DELETE_MESSAGE_ID);
			
			MessageStructure incomingMessage;
	    	 while(!complete && elapsedTime < 30000)
	         {      
	    		 if(incomingMessages.size()==0){
	    			 Thread.sleep(500);
	    			 elapsedTime = (new Date()).getTime() - startTime;
	    			 continue;
	    		 }
	    		 
	    		 incomingMessage = incomingMessages.poll();
	    		 //Log.d("TAG", "inputStream = " + incomingMessage.messageID);
	    		 Log.d("TAG", "READ inputStream: " + incomingMessage.messageID + " " + incomingMessage.dataLength + " " + incomingMessage.data[0]+ " " + incomingMessage.data[1]+ " " + incomingMessage.data[2]+ " " + incomingMessage.data[3]+ " " + incomingMessage.data[4]+ " " + incomingMessage.data[5]+ " " + incomingMessage.data[6]+ " " + incomingMessage.data[7] + " buffersize: " + incomingMessages.size());
	    		 if (incomingMessage.messageID == 256 || incomingMessage.messageID == 290){
	    			 if (incomingMessage.messageID == 256)    				
	    			 {    				
		                 if (msgCounter<totalMessages)
		                 {	         
		                	 messageData = new int[totalMessages][8];
		                	 messageData[msgCounter] = incomingMessage.data;
		                	 rx = true;	                 	
		                 	msgCounter += 1;     
		                 	//Thread.sleep(300);
		                 } 
		                 
		                 while(!complete && elapsedTime < 45000)
		                 {       
		                	 
		    	    		 if(incomingMessages.size()==0){
		    	    			 Thread.sleep(500);
		    	    			 elapsedTime = (new Date()).getTime() - startTime;
		    	    			 continue;
		    	    		 }
		    	    		 
		    	    		 incomingMessage = incomingMessages.poll();
		    	    		 Log.d("TAG", "READ inputStream: " + incomingMessage.messageID + " " + incomingMessage.dataLength + " " + incomingMessage.data[0]+ " " + incomingMessage.data[1]+ " " + incomingMessage.data[2]+ " " + incomingMessage.data[3]+ " " + incomingMessage.data[4]+ " " + incomingMessage.data[5]+ " " + incomingMessage.data[6]+ " " + incomingMessage.data[7] + " buffersize: " + incomingMessages.size());
		                	 
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
		                     		resp = sendByteMessageToTermidor(DATA_RECEIVED_MESSAGE_ID);
		                     		rxString = CommonUtilities.blockToString(messageData, true);
		                     		System.out.println(rxString);
		                     		woDetail = splitWorkOrderDetail(rxString);
		                     		complete = true;		  
		                     		
		                     		// add an erase here
		                     		sendEraseCommandToTermidor2(REQUEST_DELETE_MESSAGE_ID);
		                     	}
		                     } 
		                     //Thread.sleep(300);
		                     elapsedTime = (new Date()).getTime() - startTime;
		                     
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

	    		 //Thread.sleep(300);
	    		 elapsedTime = (new Date()).getTime() - startTime;
	         }
	    	 if(complete != true){
	 			woDetail.setLastErrorMessage("Unable to Connect to Unit");
	 			incomingMessages.clear();
				return woDetail;	    		 
	    	 }
	    	 if ( woDetail.getWorkOrderID() == null ||
                  woDetail.getWorkOrderID().toString().equals("") )
	    	 {
	    		 woDetail.setLastErrorMessage("SUCCESS");	 
	    		 incomingMessages.clear();
	    		 return null;
	    	 }
	    	woDetail.setLastErrorMessage("SUCCESS");
	    	incomingMessages.clear();
	 		return woDetail;

	}
    
    
    public boolean sendWorkOrderToTermidorHP3 ( WorkOrder wo ){
    	

    	
    	try
        {
            _SendDataQueue.add(wo);
            //getUnitID = Initialize();

            String issue = "";
            String rxString = null;
            boolean returnCode;
            boolean resp = false;
            boolean complete = false;
            //long startTime = Environment.TickCount;
            //long elapsedTime = 0;
            //if (ManageConnection())
            //{
            CanMessage incomingMessage = new CanMessage();
            resp = sendByteMessageToTermidor(REQUEST_SEND_DATA_MESSAGE_ID);
            Thread.sleep(300);
            if (!resp)
            {
                //CloseCurrentExistingController();
                return false;
            }
           
            resp = false;
           
            boolean backupSend = false; 
    		long t= System.currentTimeMillis();
    		long end = t+30000;
    		while( _StatusMessagesQueue.size() == 0 && System.currentTimeMillis() < end) {
    			
    			try {
    				Thread.sleep(500);
    				
                    // if after 20 second and havn't reiceved message, assume unit is ready and we just missed it
    				// Is this dangerous!?
                    if (backupSend == false && (end - System.currentTimeMillis()) < 10000) {
                        resp = sendWorkOrder(wo);
                        backupSend = true;
                    }
    				
    			} catch (InterruptedException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
    		}
            
    		String status  = "";    		
    		status = _StatusMessagesQueue.poll();

            if (status != null && status == "SendComplete") {
                complete = true;
            }
            
	   		if (resp)
	   		{
	   			complete = true;
	   		}
            

            return complete;

        }
        catch (Exception e)
        {
            return false;
        }
    	   	
    }
    
	
	public void ReceiveThreadFunc()
    {
		BufferedReader reader = null;
        try {
            inputStream = btSocket.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream));
            Log.d("TAG", "inputStream OK");

        } catch (IOException e2) {
            inputStream = null;
            Log.d("TAG", "inputStream KO");
            e2.printStackTrace();
        }
        

        CanMessage canMessage;
        TermidorData woDetail = new TermidorData();
        boolean firstMessageRecivieData = true;
        boolean resp = false;
        boolean complete = false;
        boolean rx = false;
        int msgCounter = 0;
        int totalMessages = 0;
        String rxString = "";
        int[][] messageData;
        messageData = new int[1][];
        
        
        while (true) {
        	
        	String status = _ToThreadMessagesQueue.poll();
        	if(status != null && status == "EndThread"){
        		break;
        	}
        	
        	
	        try {
	        	String line = "";
	            if(inputStream!=null){
	            	line = reader.readLine();
	                Log.d("TAG", "inputStream: " + line);
	            }else{
	                Log.d("TAG", "inputStream is null!");
	                break;
	            } 
	            
	            
	            canMessage = new CanMessage(line);
	            
	            if (canMessage.identifier > 201 && canMessage.identifier < 299){
	            		            
	            	if (canMessage.identifier == 256 || canMessage.identifier == 290){
	            
	            		if (canMessage.identifier == 256)
                        {
	            			
	            			 if (msgCounter < totalMessages && firstMessageRecivieData)
                             {
                                 messageData = new int[totalMessages][];
                                 //messageData[msgCounter] = incomingMessage;

                                 int[] temp = new int[8];
                                 for (int i = 0; i < temp.length; i++)
                                 {
                                     temp[i] = (int)canMessage.message[i];
                                 }
                                 messageData[msgCounter] = temp;

                                 rx = true;
                                 msgCounter += 1;
                                 //mRxEvent.WaitOne(100, false);
                                 firstMessageRecivieData = false;
                             }
	            			 
                             while (!complete)
                             {
                            	 
                            	//check if we have a message to give up
                             	String status2 = _ToThreadMessagesQueue.poll();
                            	if(status2 != null && status2 == "EndThread"){
                            		//tell thread to end
                            		_ToThreadMessagesQueue.add("EndThread");
                            		break;
                            	}
 
                	            if(inputStream!=null){
                	            	line = reader.readLine();
                	                Log.d("TAG", "inputStream: " + msgCounter);
                	            }else{
                	                Log.d("TAG", "inputStream is null!");
                	                continue;
                	            } 
                	            
                	            canMessage = new CanMessage(line);
                                if (msgCounter < totalMessages)
                                {
                                	
                                	if (canMessage.identifier == 256)
                                    {

                                        //messageData[msgCounter] = incomingMessage;
                                        int[] temp = new int[8];
                                        for (int i = 0; i < temp.length; i++)
                                        {
                                            temp[i] = (int)canMessage.message[i];
                                        }
                                        messageData[msgCounter] = temp;

                                        msgCounter += 1;
                                        rx = true;
                                    }
                                	if (msgCounter == totalMessages && rx)
                                    {
//                                        resp = sendByteMessageToTermidor(DATA_RECEIVED_MESSAGE_ID);
//                                        rxString = CommonUtilities.blockToString(messageData, true);
//                                        Log.d("TAG", rxString);
//                                        woDetail = splitWorkOrderDetail(rxString);                                      
//                                        _GetDataQueue.add(woDetail);
//                                        complete = true;
//                                        _StatusMessagesQueue.add("GetComplete");
//                                        sendEraseCommandToTermidor(REQUEST_DELETE_MESSAGE_ID);
                                        
                                		Runnable r = new MyGetWorkOrderThread(messageData);
                                		new Thread(r).start();

                                        
                                    }
                                
                                }

                            	 
                             }
	            			
                        }else{
                        	
                            switch (canMessage.identifier)
                            {
                                case 290:
                                    totalMessages = 31;
                                    break;
                            }
                        	
                        }
	            	
	            	}else if(canMessage.identifier == 288 || canMessage.identifier == 289){
	            		
	            		 if (canMessage.identifier == 288)
                         {
                             //WorkOrder wo = _SendDataQueue.poll();
                             //if (wo != null && _SendDataQueue.size() < 1)
	            			 if ( _SendDataQueue.size() == 1)
                             {
                                 Thread threadSendWO = new Thread(//() => sendWorkOrder(wo));
                                 new Runnable()
                                 {
                                     public void run()
                                     {    
                                    	 WorkOrder wo = _SendDataQueue.poll();
                                    	 sendWorkOrder(wo);              
                                     }
                                 });
                                 threadSendWO.start();
                                 //resp = sendWorkOrder(wo);
                             }
                         }
                         else if (canMessage.identifier == 289)
                         {
                             _StatusMessagesQueue.add("SendComplete");
                         }
	            		
	            	}
	            	            
	            }
	            
	        }catch (Exception e3){
	        	
	        	Log.e("TAG", "input stream", e3);
	        	//break;
	        }
		
        }
		
        _StatusMessagesQueue.add("ThreadEnded");
		
    }
	
	
	
	private boolean sendWorkOrder(WorkOrder wo) 
	{
		int woBlock[][];
		int msgSize = 8;
		int msgID = 512;
		try {		
			String woText = buildMessageWithPadding(wo, memoryLocation);
			System.out.println("woText = " + woText);
			woBlock = CommonUtilities.stringToBlock(woText, msgSize, true);
			CanMessage msgToSend = new CanMessage(msgID, msgSize, new int[msgSize],0,""); 
			
			boolean returnCode = false;
            for (int i = 0; i < woBlock.length; i++)
            {
            	          
            for (int j = 0; j <= woBlock[i].length - 1; j++)
            {
                msgToSend.message[j] = (byte)woBlock[i][j];
            }

            Thread.sleep(300);
            returnCode = false;

            try
            {
            	String message = msgToSend.toString() + "\n";
                
                byte[] msgBuffer = message.getBytes();
                try {
                        outStream.write(msgBuffer);
                        System.out.println("outputstream = " + msgToSend.toString());
                } catch (IOException e) {
                        return false;
                }
                
                returnCode = true;
            }
            catch (Exception e)
            {
                returnCode = false;
            }

            if (returnCode != true)
            {
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
	
	private boolean sendWorkOrder2(WorkOrder wo) 
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
				returnCode = transmitMessage(msgToSend, ConstantList.BINARY_FORMAT);
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
	
	private byte[] formBCBMessage(int[] messageData) {
		byte[] buffer = new byte[16];
		int a; byte b;
		int total = 0;
		
		a = 165; b = (byte) a; buffer[0] = b; total += a;
		a = 9; b = (byte) a; buffer[1] = b; total += a;
		a = 8; b = (byte) a; buffer[2] = b; total += a;
		a = 0; b = (byte) a; buffer[3] = b; total += a;
		a = 2; b = (byte) a; buffer[4] = b; total += a;
		a = 0; b = (byte) a; buffer[5] = b; total += a;
		a = 0; b = (byte) a; buffer[6] = b; total += a;
		a = messageData[0]; b = (byte) a; buffer[7] = b; total += a;
		a = messageData[1]; b = (byte) a; buffer[8] = b; total += a;
		a = messageData[2]; b = (byte) a; buffer[9] = b; total += a;
		a = messageData[3]; b = (byte) a; buffer[10] = b; total += a;
		a = messageData[4]; b = (byte) a; buffer[11] = b; total += a;
		a = messageData[5]; b = (byte) a; buffer[12] = b; total += a;
		a = messageData[6]; b = (byte) a; buffer[13] = b; total += a;
		a = messageData[7]; b = (byte) a; buffer[14] = b; total += a;

		String hex = Integer.toHexString(total);
		hex = hex.substring(hex.length()-2);
		int checkSum = Integer.parseInt(hex, 16);
		
		a = checkSum; b = (byte) a; buffer[15] = b;
		
		return buffer;
	
	}
	
	private boolean sendWorkOrderBCB(WorkOrder wo) 
	{
		int woBlock[][];
		int msgSize = 8;
		int msgID = 512;
		try {		
			String woText = buildMessageWithPadding(wo, memoryLocation);
			System.out.println("woText = " + woText);
			woBlock = CommonUtilities.stringToBlock(woText, msgSize, true);

			for (int i=0; i<woBlock.length; i++){
				//byte[] buffer = new byte[16];
				//msgToSend.data = woBlock[i];
				
				byte[] buffer = formBCBMessage(woBlock[i]);				
				Thread.sleep(300);
				outStream.write(buffer);

			}
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
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
	
	public void sendMessage(){
		
      try {
              outStream = btSocket.getOutputStream();
      } catch (IOException e) {
              Log.e("TAG", "ON RESUME: Output stream creation failed.", e);
      }

      String message = "c can_start\n";
      byte[] msgBuffer = message.getBytes();
      try {
              outStream.write(msgBuffer);

      } catch (IOException e) {
              Log.e("TAG", "ON RESUME: Exception during write.", e);
      }
		
	
	}
	
	public boolean eraseData3() throws InterruptedException 
	{
		
		boolean resp = false;
		 
		//resp = sendEraseCommandToTermidor(REQUEST_DELETE_MESSAGE_ID);
		resp = sendEraseByteMessageToTermidor(REQUEST_DELETE_MESSAGE_ID);
		Thread.sleep(2500);
        
        return resp;
				
	}
	
	
	
	
    public boolean sendEraseCommandToTermidor(int msgID)
    {
    	String hex = Integer.toHexString(msgID);

        String message = "m ed1 " + hex + " 32\n";
    	
        //String message = "m ed1 113 32\n";
        byte[] msgBuffer = message.getBytes();
        try {
                outStream.write(msgBuffer);
        } catch (Exception e) {
                return false;
        }
        return true;
    }
    
    private boolean sendByteMessageToTermidor(int msgID)
    {

        String hex = Integer.toHexString(msgID);

        String message = "m ed1 " + hex + " 31\n";

        byte[] msgBuffer = message.getBytes();
        try {
                outStream.write(msgBuffer);
        } catch (IOException e) {
                return false;
        }
        return true;
    }
    
    //binary
    private boolean sendEraseByteMessageToTermidor(int msgID)
    {

        byte[] msgBuffer = {88,-127,0,0,1,19,50};
        try {
                outStream.write(msgBuffer);
        } catch (IOException e) {
                return false;
        }
        return true;
    }
 
    private boolean sendGetByteMessageToTermidorTest2()
    {

    	String message = "0xA5 0x00 0x01 0x11 0x01 0x31 0x00 0x00 0x00 0x00 0x00 0x00 0x00 0xE9";
    	byte[] msgBuffer = message.getBytes();
    	
        try {
                outStream.write(0xA5);
                outStream.write(0x00);
                outStream.write(0x01);
                outStream.write(0x11);
                outStream.write(0x01);
                outStream.write(0x31);
                outStream.write(0x00);
                outStream.write(0x00);
                outStream.write(0x00);
                outStream.write(0x00);
                outStream.write(0x00);
                outStream.write(0x00);
                outStream.write(0x00);
                outStream.write(0xE9);

                outStream.flush();
        } catch (IOException e) {
                return false;
        }
        return true;
    }
    
    private boolean sendGetByteMessageToTermidorTest()
    {

    	String message = "A5 11 00 01 01 11 01 00 00 31 00 00 00 00 00 00 00 1B 4C";
    	byte[] msgBuffer = message.getBytes();
    	
        try {
                outStream.write(0xA5);
                outStream.write(0x11);
                outStream.write(0x00);
                outStream.write(0x01);
                outStream.write(0x01);
                outStream.write(0x11);
                outStream.write(0x01);
                outStream.write(0x00);
                outStream.write(0x00);
                outStream.write(0x31);
                outStream.write(0x00);
                outStream.write(0x00);
                outStream.write(0x00);
                outStream.write(0x00);
                outStream.write(0x00);
                outStream.write(0x00);
                outStream.write(0x00);
                outStream.write(0x84);
                outStream.write(0x82);

                outStream.flush();
        } catch (IOException e) {
                return false;
        }
        return true;
    }
    
    private boolean sendDataRecievedMessageToTermidorTest()
    {
    	//crc ??????????????????
    	String message = "A5 11 00 01 01 12 01 00 00 31 00 00 00 00 00 00 00 1B 4C";
    	byte[] msgBuffer = message.getBytes();
    	
        try {
                outStream.write(0xA5);
                outStream.write(0x11);
                outStream.write(0x00);
                outStream.write(0x01);
                outStream.write(0x01);
                outStream.write(0x12);
                outStream.write(0x01);
                outStream.write(0x00);
                outStream.write(0x00);
                outStream.write(0x31);
                outStream.write(0x00);
                outStream.write(0x00);
                outStream.write(0x00);
                outStream.write(0x00);
                outStream.write(0x00);
                outStream.write(0x00);
                outStream.write(0x00);
                outStream.write(0x84);
                outStream.write(0x82);

                outStream.flush();
        } catch (IOException e) {
                return false;
        }
        return true;
    }
    
    private boolean sendEraseByteMessageToTermidorTest2()
    {

    	String message = "A5 07 01 13 01 32 00 00 00 00 00 00 00 F3";
    	//     A5 09 01 13 01 00 00 32 00 00 00 00 00 00 00 F5
    	//byte[] msgBuffer = message.getBytes();
    	
    	byte[] msgBuffer = new byte[]{
    			(byte) 0xA5,
    			(byte) 0x09,
    			(byte) 0x01,
    			(byte) 0x13,
    			(byte) 0x01,
    			(byte) 0x00,
    			(byte) 0x00,
    			(byte) 0x32,
    			(byte) 0x00,
    			(byte) 0x00,
    			(byte) 0x00,
    			(byte) 0x00,
    			(byte) 0x00,
    			(byte) 0x00,
    			(byte) 0x00,    			
    			(byte) 0xF5};
    	
        try {
//                outStream.write(0xA5);
//                outStream.write(0x09);
//                outStream.write(0x01);
//                outStream.write(0x13);
//                outStream.write(0x01);
//                outStream.write(0x00);
//                outStream.write(0x00);                  
//                outStream.write(0x32);
//                outStream.write(0x00);
//                outStream.write(0x00);
//                outStream.write(0x00);
//                outStream.write(0x00);
//                outStream.write(0x00);
//                outStream.write(0x00);
//                outStream.write(0x00);
//                outStream.write(0xF5);
        		
        		outStream.write(msgBuffer);
        	
                outStream.flush();
        } catch (IOException e) {
                return false;
        }
        return true;
    }   
    
    private boolean sendGetByteMessageToTermidorTest3()
    {

    	String message = "0xA5 0x09 0x01 0x11 0x01 0x00 0x00 0x31 0x00 0x00 0x00 0x00 0x00 0x00 0x00 0xF2";
    	//     A5 08 01 13 01 32 00 00 00 00 00 00 00 F4
    	//byte[] msgBuffer = message.getBytes();
    	byte[] msgBuffer = new byte[]{
    			(byte) 0xA5,
    			(byte) 0x09,
    			(byte) 0x01,
    			(byte) 0x11,
    			(byte) 0x01,
    			(byte) 0x00,
    			(byte) 0x00,
    			(byte) 0x31,
    			(byte) 0x00,
    			(byte) 0x00,
    			(byte) 0x00,
    			(byte) 0x00,
    			(byte) 0x00,
    			(byte) 0x00,
    			(byte) 0x00,    			
    			(byte) 0xF2};
    	
        try {
//                outStream.write(0xA5);
//                outStream.write(0x09);
//                outStream.write(0x01);
//                outStream.write(0x11);
//                outStream.write(0x01);
//                outStream.write(0x00);
//                outStream.write(0x00);                  
//                outStream.write(0x31);
//                outStream.write(0x00);
//                outStream.write(0x00);
//                outStream.write(0x00);
//                outStream.write(0x00);
//                outStream.write(0x00);
//                outStream.write(0x00);
//                outStream.write(0x00);
//                outStream.write(0xF2);
        	
        		outStream.write(msgBuffer);

                outStream.flush();
        } catch (IOException e) {
                return false;
        }
        return true;
    }   
    
    private boolean sendKeepAliveMessageToTermidor()
    {

    	byte[] msgBuffer = new byte[]{
    			(byte) 0xA5,
    			(byte) 0x06,
    			(byte) 0xAB};
    	
        try {
        	
        		outStream.write(msgBuffer);

                outStream.flush();
        } catch (IOException e) {
                return false;
        }
        return true;
    }   
    
    private boolean sendReceivedByteMessageToTermidorBCB()
    {

    	String message = "0xA5 0x09 0x01 0x11 0x01 0x00 0x00 0x31 0x00 0x00 0x00 0x00 0x00 0x00 0x00 0xF2";
    	//     A5 08 01 13 01 32 00 00 00 00 00 00 00 F4
    	//byte[] msgBuffer = message.getBytes();
    	
    	byte[] msgBuffer = new byte[]{
    			(byte) 0xA5,
    			(byte) 0x09,
    			(byte) 0x01,
    			(byte) 0x12,
    			(byte) 0x01,
    			(byte) 0x00,
    			(byte) 0x00,
    			(byte) 0x31,
    			(byte) 0x00,
    			(byte) 0x00,
    			(byte) 0x00,
    			(byte) 0x00,
    			(byte) 0x00,
    			(byte) 0x00,
    			(byte) 0x00,    			
    			(byte) 0xF3};
    	
        try {
//                outStream.write(0xA5);
//                outStream.write(0x09);
//                outStream.write(0x01);
//                outStream.write(0x12);
//                outStream.write(0x01);
//                outStream.write(0x00);
//                outStream.write(0x00);                  
//                outStream.write(0x31);
//                outStream.write(0x00);
//                outStream.write(0x00);
//                outStream.write(0x00);
//                outStream.write(0x00);
//                outStream.write(0x00);
//                outStream.write(0x00);
//                outStream.write(0x00);
//                outStream.write(0xF3);

        		outStream.write(msgBuffer);
                outStream.flush();
        } catch (IOException e) {
                return false;
        }
        return true;
    }   
    
    private boolean sendByteMessageToTermidorTest3()
    {

    	String message = "0xA5 0x09 0x01 0x10 0x01 0x00 0x00 0x31 0x00 0x00 0x00 0x00 0x00 0x00 0x00 0xF1";
    	//     A5 08 01 13 01 32 00 00 00 00 00 00 00 F4
    	//byte[] msgBuffer = message.getBytes();
    	
    	byte[] msgBuffer = new byte[]{
    			(byte) 0xA5,
    			(byte) 0x09,
    			(byte) 0x01,
    			(byte) 0x10,
    			(byte) 0x01,
    			(byte) 0x00,
    			(byte) 0x00,
    			(byte) 0x31,
    			(byte) 0x00,
    			(byte) 0x00,
    			(byte) 0x00,
    			(byte) 0x00,
    			(byte) 0x00,
    			(byte) 0x00,
    			(byte) 0x00,    			
    			(byte) 0xF1};
    	
        try {
//                outStream.write(0xA5);
//                outStream.write(0x09);
//                outStream.write(0x01);
//                outStream.write(0x10);
//                outStream.write(0x01);
//                outStream.write(0x00);
//                outStream.write(0x00);                  
//                outStream.write(0x31);
//                outStream.write(0x00);
//                outStream.write(0x00);
//                outStream.write(0x00);
//                outStream.write(0x00);
//                outStream.write(0x00);
//                outStream.write(0x00);
//                outStream.write(0x00);
//                outStream.write(0xF1);

        		outStream.write(msgBuffer);
                outStream.flush();
        } catch (IOException e) {
                return false;
        }
        return true;
    }   
    
    private boolean sendEraseByteMessageToTermidorTest3()
    {

    	String message = "A5 07 01 13 01 32 00 00 00 00 00 00 00 F3";
    	//     A5 08 01 13 01 32 00 00 00 00 00 00 00 F4
    	byte[] msgBuffer = message.getBytes();
    	
        try {
                outStream.write(0xA5);
                outStream.write(0x08);
                outStream.write(0x01);
                outStream.write(0x13);
                outStream.write(0x01);
                outStream.write(0x00);
                outStream.write(0x00);                
                outStream.write(0x32);
                outStream.write(0x00);
                outStream.write(0x00);
                outStream.write(0x00);
                outStream.write(0x00);
                outStream.write(0x00);
                outStream.write(0x00);
                outStream.write(0x00);
                outStream.write(0xF4);

                outStream.flush();
        } catch (IOException e) {
                return false;
        }
        return true;
    }   
    
    private boolean sendEraseByteMessageToTermidorTest4()
    {

    	String message = "A5 07 01 13 01 32 00 00 00 00 00 00 00 F3";
    	//     A5 08 01 13 01 32 00 00 00 00 00 00 00 F4
    	byte[] msgBuffer = message.getBytes();
    	
        try {
                outStream.write(0xA5);
                outStream.write(0x07);
                outStream.write(0x01);
                outStream.write(0x13);
                outStream.write(0x01);
                outStream.write(0x32);
                outStream.write(0x00);
                outStream.write(0x00);
                outStream.write(0x00);
                outStream.write(0x00);
                outStream.write(0x00);
                outStream.write(0x00);
                outStream.write(0x00);
                outStream.write(0xF3);

                outStream.flush();
        } catch (IOException e) {
                return false;
        }
        return true;
    }   
    
    private boolean sendEraseByteMessageToTermidorTest5()
    {

    	String message = "A5 07 01 13 01 32 00 00 00 00 00 00 00 F3";
    	//     A5 08 01 13 01 32 00 00 00 00 00 00 00 F4
    	byte[] msgBuffer = message.getBytes();
    	
        try {
                outStream.write(0xA5);
                outStream.write(0x00);
                outStream.write(0x01);
                outStream.write(0x13);
                outStream.write(0x01);
                outStream.write(0x32);
                outStream.write(0x00);
                outStream.write(0x00);
                outStream.write(0x00);
                outStream.write(0x00);
                outStream.write(0x00);
                outStream.write(0x00);
                outStream.write(0x00);
                outStream.write(0xEC);

                outStream.flush();
        } catch (IOException e) {
                return false;
        }
        return true;
    }  
    
    private boolean sendEraseByteMessageToTermidorTest()
    {

    	String message = "A5 11 00 01 01 13 01 00 00 32 00 00 00 00 00 00 00 1B 4C";
    	byte[] msgBuffer = message.getBytes();
    	
        try {
                outStream.write(0xA5);
                outStream.write(0x11);
                outStream.write(0x00);
                outStream.write(0x01);
                outStream.write(0x01);
                outStream.write(0x13);
                outStream.write(0x01);
                outStream.write(0x00);
                outStream.write(0x00);
                outStream.write(0x32);
                outStream.write(0x00);
                outStream.write(0x00);
                outStream.write(0x00);
                outStream.write(0x00);
                outStream.write(0x00);
                outStream.write(0x00);
                outStream.write(0x00);
                outStream.write(0x1B);
                outStream.write(0x4C);

                outStream.flush();
        } catch (IOException e) {
                return false;
        }
        return true;
    }   
    
//    public void deInitializeWithoutReceiveThread() {
//
//        if (btSocket != null)
//        {
//            if (btSocket.isConnected())
//            {
//            	try {
//					btSocket.close();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//            }
//            btSocket = null;
//        }
//    }
    
    public void deInitialize() {

    	if(connectFail==false){
    		
	        _ToThreadMessagesQueue.add("EndThread");
	
			long t= System.currentTimeMillis();
			long end = t+10000;
			while( _StatusMessagesQueue.size() == 0 && System.currentTimeMillis() < end) {
				_StatusMessagesQueue.clear();
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
	        _ToThreadMessagesQueue2.add("EndThread");
	
			t= System.currentTimeMillis();
			end = t+10000;
			while( _StatusMessagesQueue.size() == 0 && System.currentTimeMillis() < end) {
				
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		
    	}
//		

		
//        String message = "C CAN_STOP\n";
//        byte[] msgBuffer = message.getBytes();
//        try {
//            outStream.write(msgBuffer);
//        } catch (IOException e) {
//        	//return false;
//            //Log.e("TAG", "ON RESUME: Exception during write.", e);
//        }
        
        
		
        
        try {
			outStream.close();
			inputStream.close();
			outStream = null;
			inputStream=null;
			Thread.sleep(500);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
        
        rxThread = null;

        if (btSocket != null)
        {
            if (btSocket.isConnected())
            {
            	try {
					btSocket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
            btSocket = null;
        }
        
        //mBluetoothAdapter.disable();
        
    }
	
    
    boolean connectFail = true;
    
    public boolean initialize(String unitAddress){
    	connectFail = true;
    	
    	address = unitAddress;
    	
    	try{
    		boolean success = false;
    		
    		for(int i = 0;i < 2;i++){
		    	success = initialize2();
		    	if(!success){
		    		deInitialize();
		    		Thread.sleep(500);
		    	}else{
		    		return success;
		    	}
    		}
    	
    		//if failed 3 times maybe try to turn bluetooth on and off and try one more time?
    		
    		return success;
    	}catch(Exception e){
    		return false;
    	}
    
    }
    
//    public boolean initializeWithoutReceiveThread(String unitAddress){
//    	
//    	address = unitAddress;
//    	
//    	try{
//    		boolean success = false;
//    		
//    		for(int i = 0;i < 3;i++){
//		    	success = initialize3();
//		    	if(!success){
//		    		deInitializeWithoutReceiveThread();
//		    		Thread.sleep(500);
//		    	}else{
//		    		return success;
//		    	}
//    		}
//    		
//    		return success;
//    	}catch(Exception e){
//    		return false;
//    	}
//    
//    }
    
//	public boolean initialize3(){
//
//		
//		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//        if (mBluetoothAdapter == null) {
//            	Log.i("TAG","Bluetooth not available.");
//                return false;
//        }
//
//        if (!mBluetoothAdapter.isEnabled()) {
//                Log.i("TAG","Please enable your BT and re-run this program.");
//                return false;
//        }
//		
//        device = mBluetoothAdapter.getRemoteDevice(address);
//        btSocket = null;
//        
//        try {
//            Method m = device.getClass().getMethod("createInsecureRfcommSocket", new Class[] { int.class });
//            btSocket = (BluetoothSocket) m.invoke(device, 2);
//        } catch(NoSuchMethodException e) {
//            e.printStackTrace();
//        } catch(InvocationTargetException e) {
//            e.printStackTrace();
//        } catch(IllegalAccessException e) {
//            e.printStackTrace();
//        }
//
//        
//        // Discovery may be going on, e.g., if you're running a
//        // 'scan for devices' search from your handset's Bluetooth
//        // settings, so we call cancelDiscovery(). It doesn't hurt
//        // to call it, but it might hurt not to... discovery is a
//        // heavyweight process; you don't want it in progress when
//        // a connection attempt is made.
//        mBluetoothAdapter.cancelDiscovery();
//
//        // Blocking connect, for a simple client nothing else can
//        // happen until a successful connection is made, so we
//        // don't care if it blocks.
//        try {
//            // This is a blocking call and will only return on a successful connection or an exception
//            Log.i("TAG","Connecting to socket...");
//            btSocket.connect();
//            Log.i("TAG","Connected");
//        } catch (IOException e) {
//            try {
//                btSocket.connect();
//                Log.i("TAG","Connected");
//            } catch (IOException e2) {
//            	return false;
//                //Log.e("TAG", e.toString());
//            }
//        }
//             
//        
//        try {
//            outStream = btSocket.getOutputStream();
//        } catch (IOException e) {
//        	return false;
//            //Log.e("TAG", "ON RESUME: Output stream creation failed.", e);
//        }
//
//        String message = "c can_start\n";
//        byte[] msgBuffer = message.getBytes();
//        try {
//            outStream.write(msgBuffer);
//        } catch (IOException e) {
//        	return false;
//            //Log.e("TAG", "ON RESUME: Exception during write.", e);
//        }
//        
//        
//        _GetDataQueue.clear();
//        _SendDataQueue.clear();
//        _StatusMessagesQueue.clear();
//        _ToThreadMessagesQueue.clear();
//               
//	
//        return true;
//	}
    
	public boolean initialize2(){

			
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//		mBluetoothAdapter.enable();
//		try {
//			Thread.sleep(3000);
//		} catch (InterruptedException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
        if (mBluetoothAdapter == null) {
            	Log.i("TAG","Bluetooth not available.");
                return false;
        }

        if (!mBluetoothAdapter.isEnabled()) {
                Log.i("TAG","Please enable your BT and re-run this program.");
                return false;
        }
		
        //device = mBluetoothAdapter.getRemoteDevice("00:12:F3:21:64:D9");
        //device = mBluetoothAdapter.getRemoteDevice("00:12:F3:23:40:AB");
        //device = mBluetoothAdapter.getRemoteDevice("00:12:F3:21:64:D3");
        
        device = mBluetoothAdapter.getRemoteDevice(address);
        btSocket = null;
        
        try {
            //Method m = device.getClass().getMethod("createInsecureRfcommSocket", new Class[]{int.class});
            //btSocket = (BluetoothSocket) m.invoke(device, 1);      
            //UUID uuid = UUID.fromString("0000110E-0000-1000-8000-00805F9B34FB");
			//btSocket = device.createInsecureRfcommSocketToServiceRecord(uuid);

            
            //Method m = device.getClass().getMethod("createRfcommSocket", new Class[] { int.class } );
            Method m = device.getClass().getMethod("createInsecureRfcommSocket", new Class[] { int.class });
            btSocket = (BluetoothSocket) m.invoke(device, 1); 
            
            
        	
        	
      //***      Method m = device.getClass().getMethod("createInsecureRfcommSocket", new Class[] { int.class });
      //***      btSocket = (BluetoothSocket) m.invoke(device, 2);
        } catch(NoSuchMethodException e) {
            e.printStackTrace();
        } catch(InvocationTargetException e) {
            e.printStackTrace();
        } catch(IllegalAccessException e) {
            e.printStackTrace();
        } /*catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/

        
        // Discovery may be going on, e.g., if you're running a
        // 'scan for devices' search from your handset's Bluetooth
        // settings, so we call cancelDiscovery(). It doesn't hurt
        // to call it, but it might hurt not to... discovery is a
        // heavyweight process; you don't want it in progress when
        // a connection attempt is made.
        mBluetoothAdapter.cancelDiscovery();

        // Blocking connect, for a simple client nothing else can
        // happen until a successful connection is made, so we
        // don't care if it blocks.
        try {
            // This is a blocking call and will only return on a successful connection or an exception
            Log.i("TAG","Connecting to socket...");
            btSocket.connect();
            Log.i("TAG","Connected");
        } catch (IOException e) {
            try {
                btSocket.connect();
                Log.i("TAG","Connected");
            } catch (IOException e2) {
            	return false;
                //Log.e("TAG", e.toString());
            }
        }
             
        connectFail = false;
        
        _GetDataQueue.clear();
        _SendDataQueue.clear();
        _StatusMessagesQueue.clear();
        _ToThreadMessagesQueue.clear();
        _ToThreadMessagesQueue2.clear();
        incomingMessages.clear();
        
        
        
//	      rxThread = new Thread(new Runnable()
//	      {
//	          public void run()
//	          {                        
//	          	ReceiveThreadFunc();              
//	          }
//	      });
//	      rxThread.start();
        
//        rxThread = new ReceiveData();
//        rxThread.start();
        
        
        
        try {
            outStream = btSocket.getOutputStream();
        } catch (IOException e) {
        	return false;
            //Log.e("TAG", "ON RESUME: Output stream creation failed.", e);
        }
        
        

       
        
//        rxThread = new Thread(new Runnable()
//        {
//            public void run()
//            {                        
//            	ReceiveThreadFunc();              
//            }
//        });
//        rxThread.start();
        
        //respBuffer.resetResponseBuffer();
        
       rxThread = new ReceiveData();   
       rxThread.start();
//        
 //      transmitThread = new TransmitBulkMessage();
 //      transmitThread.start();
       

       //sendEraseByteMessageToTermidorTest();
       try {
    	   sendKeepAliveMessageToTermidor();
    	   Thread.sleep(2000);
	       //sendGetByteMessageToTermidorTest3();
	       //Thread.sleep(5000);
	       //sendGetByteMessageToTermidorTest3();
	       //sendGetByteMessageToTermidorTest3();
	       //Thread.sleep(40000);
    	   
    	   
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
       //sendGetByteMessageToTermidorTest2();
    /*   
       try{

    	   
       //sendEraseByteMessageToTermidorTest2();
       //Thread.sleep(300);
       //sendEraseByteMessageToTermidorTest3();
       //Thread.sleep(300);
       //sendEraseByteMessageToTermidorTest4();
       Thread.sleep(5000);
       //sendEraseByteMessageToTermidorTest5();
    	   sendByteMessageToTermidorTest3();
       }catch(Exception e){
    	   
    	   
       }*/
        
/*
        String command = "C CAN_INIT " + "250" + " " + "HIGH\n";
        byte[] msgBuffer2 = command.getBytes();
        try {
            outStream.write(msgBuffer2);
            //Thread.sleep(1000);
        } catch (Exception e) {
        	return false;
            //Log.e("TAG", "ON RESUME: Exception during write.", e);
        }
        
        
        command = "C CAN_INFO\n";
        byte[] msgBuffer3 = command.getBytes();
        try {
            outStream.write(msgBuffer3);
            //Thread.sleep(1000);
        } catch (Exception e) {
        	return false;
            //Log.e("TAG", "ON RESUME: Exception during write.", e);
        }       
        
        

        String message = "C CAN_START\n";
        byte[] msgBuffer = message.getBytes();
        try {
            outStream.write(msgBuffer);
            //Thread.sleep(1000);
        } catch (Exception e) {
        	return false;
            //Log.e("TAG", "ON RESUME: Exception during write.", e);
        }
        
       */ 

        
        

        
	
        return true;
	}
	
	
	public TermidorData getData3(){
		
		
		try{
			boolean resp = false;		
			resp = sendByteMessageToTermidor(REQUEST_DATA_MESSAGE_ID);
			
			
			long t= System.currentTimeMillis();
			long end = t+30000;
			while( _StatusMessagesQueue.size() == 0 && System.currentTimeMillis() < end) {
				
				Thread.sleep(500);
			}
			
			
			if(_GetDataQueue.size() != 1){
				//we failed to get data from the device :(,,,
				//errorCode = "GetDataFailed";
				return null;
			}
			
			TermidorData woDetail = null;
			woDetail = _GetDataQueue.poll();
			_StatusMessagesQueue.poll();
			
//			deInitialize();
//			Thread.sleep(1000);
//			initializeWithoutReceiveThread(address);
			
			//do an erase after pull
			//sendEraseCommandToTermidor(REQUEST_DELETE_MESSAGE_ID);
			sendEraseByteMessageToTermidor(REQUEST_DELETE_MESSAGE_ID);
			Thread.sleep(50);
			sendEraseByteMessageToTermidor(REQUEST_DELETE_MESSAGE_ID);
			Thread.sleep(50);			
			sendEraseByteMessageToTermidor(REQUEST_DELETE_MESSAGE_ID);
			Thread.sleep(2500);
			
//			deInitializeWithoutReceiveThread();
//			Thread.sleep(10000);
//			initialize(address);

			
			if (woDetail.getWorkOrderID() == null || woDetail.getWorkOrderID().toString().equals("")){
                   woDetail.setLastErrorMessage("SUCCESS");
                   return null;
            }
            woDetail.setLastErrorMessage("SUCCESS");
            return woDetail;
	
            
		}catch(Exception e){			
			return null;			
		}
        
		
	}
	
	
	public class MyGetWorkOrderThread implements Runnable {

		   int[][] messageData;
		
		   public MyGetWorkOrderThread(int[][] paramMessageData) {
			   messageData = paramMessageData;
		       // store parameter for later user
		   }

		   public void run() {
			   
               sendByteMessageToTermidor(DATA_RECEIVED_MESSAGE_ID);
               String rxString = CommonUtilities.blockToString(messageData, true);
               Log.d("TAG", rxString);
               TermidorData woDetail;
               woDetail = splitWorkOrderDetail(rxString);                                      
               _GetDataQueue.add(woDetail);
               //complete = true;
               _StatusMessagesQueue.add("GetComplete");
               sendEraseCommandToTermidor(REQUEST_DELETE_MESSAGE_ID);  
					   
		   }
		}
	
	
	
	  public final int BYTE_BUFFER_LENGTH = 2048;
	  /** Input for frameFormat. */
	  public static final byte STANDARD_FRAME = 0, EXTENDED_FRAME = 1;

	  /** Input for frameType. BOTH_FRAME is only used internally for filtering. */
	  public static final byte DATA_FRAME = 1, REMOTE_FRAME = 2, BOTH_FRAME = 3;
	  public boolean versionNeeded = false;
	  public ReturnCode receiveThreadFlag = ReturnCode.THREAD_STOPPED;
	  //public volatile ResponseBuffer respBuffer;
	  //public volatile ArrayList<MessageStructure> incomingMessages = new ArrayList<MessageStructure>();
	  private static ConcurrentLinkedQueue<MessageStructure> incomingMessages = new ConcurrentLinkedQueue<MessageStructure>();
	  
	public class ReceiveData extends Thread
    {
      /** Run the thread. */
      public void run()
      {
    	  
    	respBuffer = new ResponseBuffer();
    	
    	try {
            inputStream = btSocket.getInputStream();
            Log.d("TAG", "inputStream OK");

        } catch (IOException e2) {
            inputStream = null;
            Log.d("TAG", "inputStream KO");
            e2.printStackTrace();
        }
    	  
        byte[] buffer = new byte[BYTE_BUFFER_LENGTH];
        ArrayList<Integer> buffer2 = new ArrayList<Integer>();
        
        
        int i, length, offset;
        String tempString = null;
        MessageStructure tempMessage; //= new MessageStructure();
        //tempMessage.data = new int[8];
        int tempMessageID, tempData;
        byte tempFrameFormat;
        byte tempFrameType;
        byte tempMessageLength;
        ArrayList<Integer> messages = new ArrayList<Integer>();
        ArrayList<Integer> leftOver = new ArrayList<Integer>();
        messages.clear();
        
        int bufferCount = 0;
        boolean gotGetCMD = false;
        
		long t= System.currentTimeMillis();
		long end = t+60000;
       
        // Keep listening to the InputStream while connected
        offset = 0;
        receiveThreadFlag = ReturnCode.THREAD_RUNNING;
        while (true)
        {
        	
      		String status = _ToThreadMessagesQueue.poll();
      		if(System.currentTimeMillis() < end && status != null && status == "EndThread"){
      			_StatusMessagesQueue.add("ThreadEnded");
      			 Log.d("TAG", "buffer read #: "+ bufferCount);
      			Log.d("TAG", "inputStream: " + messages.size());
      			Log.d("TAG", "inputStream: " + messages);
      			Log.d("TAG", "inputStream: " + gotGetCMD);
      			return;
      		}
        	
          // Reset the length and try to read from the InputStream.
          length = 0;
          try
          {
        	  bufferCount++;
        	  buffer2.clear();
        	  for(int a=0; a < leftOver.size();a++){       		  
        		  buffer2.add(leftOver.get(a));
        	  }
        	  leftOver.clear();
        	  
            // If there is unparsed data, do not overwrite it.
            // The length of the unparsed data is the offset.
        	  
        	  buffer = new byte[BYTE_BUFFER_LENGTH];
        	  Thread.sleep(100);
        	  length = inputStream.read(buffer);
        	  
        	  Log.d("TAG", "buffer read #: "+ bufferCount + " buffer read length: "+ length);
        	  
        	  for(int j = 0;j < length;j++){
        		  buffer2.add(unsignedByteToInt(buffer[j]));
        	  }
        	  
        	  for(int j = 0;j < buffer2.size();j++){
        		  
        		  if(buffer2.size() - j < 16){
        			  leftOver.add(buffer2.get(j));
        			  		       			  
        		  }else{
        		          		 	        		  	        		  
	        		  /*if(buffer2.get(j)==165 && buffer2.get(j+1)==17 && buffer2.get(j+2)==0 && buffer2.get(j+3)==1 && buffer2.get(j+4)==8 && buffer2.get(j+5)==0 && buffer2.get(j+6)==1 && buffer2.get(j+7)==0 && buffer2.get(j+8)==0){
	        			  messages.add(buffer2.get(j+9));    			  
	        		  }*/
	        		  
	        		  if(buffer2.get(j)==165 && buffer2.get(j+1)==8 && buffer2.get(j+2)==8 && buffer2.get(j+3)==0 && buffer2.get(j+4)==1 && buffer2.get(j+5)==0 && buffer2.get(j+6)==0){
	        			  messages.add(buffer2.get(j+7)); 
	        			  Log.d("TAG", "buffer read #: "+ bufferCount + " inputStream: " + buffer2.get(j+7));
	        			  
	        			  tempMessage = new MessageStructure();
	        			  tempMessage.messageID = 256;
	        			  tempMessage.dataLength=8;
	                      tempMessage.data = new int[8];	                      
	                      for(int k=0;k < 8;k++){
	                    	  tempMessage.data[k] = buffer2.get(j+7+k); 
	                      }	        			  
	                      incomingMessages.add(tempMessage);
	        		  }	 
	        		  
	        		  if(buffer2.get(j)==165 && buffer2.get(j+1)==8 && buffer2.get(j+2)==1 && buffer2.get(j+3)==34 && buffer2.get(j+4)==1 && buffer2.get(j+5)==0 && buffer2.get(j+6)==0){
	        			  gotGetCMD = true;  
	        			  Log.d("TAG", "inputStream: gotGet");
	        			  
	        			  tempMessage = new MessageStructure();
	        			  tempMessage.messageID = 290;
	                      tempMessage.data = new int[8];
	                      for(int k=0;k < 8;k++){
	                    	  tempMessage.data[k] = buffer2.get(j+7+k); 
	                      }
	                      incomingMessages.add(tempMessage);
	        		  }	
	        		  
	        		  if(buffer2.get(j)==165 && buffer2.get(j+1)==8 && buffer2.get(j+2)==1 && buffer2.get(j+3)==33 && buffer2.get(j+4)==1 && buffer2.get(j+5)==0 && buffer2.get(j+6)==0){ 
	        			  
	        			  tempMessage = new MessageStructure();
	        			  tempMessage.messageID = 289;
	                      tempMessage.data = new int[8];
	                      for(int k=0;k < 8;k++){
	                    	  tempMessage.data[k] = buffer2.get(j+7+k); 
	                      }
	                      incomingMessages.add(tempMessage);
	        		  }	
	        		  
	        		  if(buffer2.get(j)==165 && buffer2.get(j+1)==8 && buffer2.get(j+2)==1 && buffer2.get(j+3)==32 && buffer2.get(j+4)==1 && buffer2.get(j+5)==0 && buffer2.get(j+6)==0){ 
	        			  
	        			  tempMessage = new MessageStructure();
	        			  tempMessage.messageID = 288;
	                      tempMessage.data = new int[8];
	                      for(int k=0;k < 8;k++){
	                    	  tempMessage.data[k] = buffer2.get(j+7+k); 
	                      }
	                      incomingMessages.add(tempMessage);
	        		  }	
	        		  
        		  }
        		  
        		  
        	  }
        	  
        	  
        	  
            //length = inputStream.read(buffer, offset,
            //    (BYTE_BUFFER_LENGTH - offset));
            //Thread.sleep(1000);
          }
          catch (Exception e)
          {
            // Error reading from the InputStream.
            // The connection is lost or the Bluetooth buffer is overflowed.
            e.printStackTrace();
            
            // stop the receive thread.
            break;
          }
          
          /*

          if (length <= 0)
          {
              continue;  // No data.
          }

          if (offset != 0)
          {
            // The returned length does not include the offset, add it.
            length = length + offset;
            // Reset the offset.
            offset = 0;
          }

          i = 0;
          while (i < length)
          {
          		status = _ToThreadMessagesQueue.poll();
          		if(status != null && status == "EndThread"){
          			_StatusMessagesQueue.add("ThreadEnded");
          			return;
          		}
          	
              tempMessage = new MessageStructure();
              tempMessage.data = new int[8];
        	  
            // Check if the beginning of the buffer is a response or an error.
            if ((buffer[i] == (byte) 'I') || (buffer[i] == (byte) 'E'))
            {
              int startIndex = i;

              while((i < length) && (buffer[i] != 13) && (buffer[i] != 10))
              {
                // Increment i until end of the buffer or find "\n" or "\r".
                i++;
              }

              if ((i >= length) && (buffer[i-1] != 13) && (buffer[i-1] != 10))
              {
                // At the end of the buffer, "\n" or "\r" is not found, it is
                // an incomplete response. Set the offset and copy the remaining
                // data to the beginning of the buffer.
                offset = i - startIndex;
                for (int l = 0; l < offset; l++)
                {
                  buffer[l] = buffer[startIndex + l];
                }
                break;
              }

              // The length of the current response or error.
              int indexLength = i - startIndex;

              // Some automatic error response sent by the CANblue should be
              // ignored. Use "CAN_INFO" command to read the status of the 
              // controller. "E 8X XX..", check the first and third characters.
              if ((buffer[startIndex] == 'E')
                  && (buffer[startIndex + 2] == '8'))
              {
                // Do nothing.
              }
              else if ((versionNeeded == false) && (buffer[startIndex] == 'I') 
                  && (buffer[startIndex + 2] == 'C') &&
                  (buffer[startIndex + 3] == 'A') &&
                  (buffer[startIndex + 4] == 'N') &&
                  (buffer[startIndex + 5] == 'b')){
                  // "I CANblue Generic - Bridge v1.901".
                  // The device information is currently not needed, throw them.
                  versionNeeded = false;
              }
              else
              {
                // Response information or other error messages.
                if (respBuffer.bufferIndex >= respBuffer.bufferLength)
                {
                  // If the buffer is full, ignore the rest of the response.
                  continue;
                }
                tempString = ASCIIArrayToString(buffer, startIndex,
                    indexLength);
                if (tempString == null)
                {
                  continue;  // Invalid data.
                }
                // Fill the buffer, increase the index, and set the flag.
                respBuffer.buffer[respBuffer.bufferIndex] = tempString;
                respBuffer.bufferIndex++;
                respBuffer.responseFlag = true;
              }
            }
            // Check if the beginning of the buffer is a message.
            else if (buffer[i] == (byte) 'M')
            {
              // Store the current time.
              Date globalDate = new Date();

              int startIndex = i;
              while((i < length) && (buffer[i] != 13) && (buffer[i] != 10))
              {
                // Increment i until end of the buffer or find "\n" or "\r".
                i++;
              }

              if ((i >= length) && (buffer[i-1] != 13) && (buffer[i-1] != 10))
              {
                // At the end of the buffer, "\n" or "\r" is not found, it is
                // an incomplete message. Set the offset and copy the remaining
                // data to the beginning of the buffer.
                offset = i - startIndex;
                for (int l = 0; l < offset; l++)
                {
                  buffer[l] = buffer[startIndex + l];
                }
                continue;
              }

              // The length of the current message.
              int indexLength = i - startIndex;

              // Parse the message and put it into temporary buffer.
              // Time stamp.
              tempMessage.timeStamp = globalDate.getTime();
              // Frame format.
              if (buffer[startIndex + 2] == 'E')
              {
                tempMessage.frameFormat = EXTENDED_FRAME;
              }
              else if (buffer[startIndex + 2] == 'S')
              {
                tempMessage.frameFormat = STANDARD_FRAME;
              }
              else
              {
                continue;  // Invalid message.
              }

              // Data length. Check if the data length is between 0-8.
              if ((buffer[startIndex + 4] < '0')
                  || (buffer[startIndex + 4] > '8'))
              {
                continue;  // Invalid message.
              }
              else
              {
                // Fill with number, not ASCII format;
                tempMessage.dataLength = (byte) (buffer[startIndex + 4] - '0');
              }

              // Frame type and length of data array.
              if (buffer[startIndex + 3] == 'D')
              {
                tempMessage.frameType = DATA_FRAME;
              }
              else if (buffer[startIndex + 3] == 'R')
              {
                tempMessage.frameType = REMOTE_FRAME;
              }
              else
              {
                continue;  // Invalid message.
              }

              // Find the Message ID.
              int j;
              for (j = startIndex + 6; j < startIndex + indexLength; j++)
              {
                // Find the space between the message ID and data. If there is
                // no space, it means that the rest of the buffer are messageID.
                if (buffer[j] == ' ')
                {
                  break;
                }
              }

              // Convert and copy the Message ID.
              tempMessageID = HexASCIIArrayToInt(buffer, startIndex + 6, 
                  j - startIndex - 6);
              if (tempMessageID == -1)
              {
                continue;  // Invalid message ID.
              }
              tempMessage.messageID = tempMessageID;

              // If there are data, convert and copy them.
              if ((tempMessage.dataLength != 0)
                  && (j != startIndex + indexLength))
              {
                for (int k = 0; k < tempMessage.dataLength; k++)
                {
                  tempData = HexASCIIArrayToInt(buffer, (j + 1) + (3 * k), 2);
                  if (tempData == -1)
                  {
                    continue;  // Invalid data.
                  }
                  tempMessage.data[k] = tempData;
                }
              }

              // Apply the filtering process.
              //applyFilteringMessage(tempMessage);
              //intCANMessage[i].intReceiveBuffer.queuePut(tempMessage);
              

              if(tempMessage.messageID > 201 && tempMessage.messageID < 299){
            	  incomingMessages.add(tempMessage);
            	  Log.d("TAG", "WRITE inputStream: " + tempMessage.messageID + " " + tempMessage.dataLength + " " + tempMessage.data[0]+ " " + tempMessage.data[1]+ " " + tempMessage.data[2]+ " " + tempMessage.data[3]+ " " + tempMessage.data[4]+ " " + tempMessage.data[5]+ " " + tempMessage.data[6]+ " " + tempMessage.data[7] + " buffersize: " + incomingMessages.size());
              }
            }
            else if (buffer[i] == (byte) 'X')
            {
              int startIndex = i;

              // The minimum length of the binary message is 4, check it first.
              if (i + 4 > length)
              {
                  offset = length - startIndex;
                  for (int l = 0; l < offset; l++)
                  {
                    buffer[l] = buffer[startIndex + l];
                  }
                  i = length;
                  continue;
              }

              // Store the current time.
              Date globalDate = new Date();
              tempMessage.timeStamp = globalDate.getTime();

              // Check whether the frame info is valid and check its content.
              i++; // Increase the index.
              if((buffer[i] & 0x30) != 0x00)
              {
                continue; // Invalid message.
              }
              tempFrameFormat = (byte) (buffer[i] & 0x80);
              tempFrameType = (byte) (buffer[i] & 0x40);
              tempMessage.dataLength = (byte) (buffer[i] & 0x0F);
              if (tempMessage.dataLength > 8)
              {
                continue; // Invalid data length.
              }
              tempMessageLength = 0;
              if (tempFrameFormat == 0)
              {
                tempMessageLength += 2; // 2bytes ID.
              }
              else
              {
                tempMessageLength += 4; // 4bytes ID.
              }
              if (tempFrameType == 0)
              {
                tempMessageLength += tempMessage.dataLength; // Data Length.
              }
              // Check whether the message is complete.
              if (i + tempMessageLength >= length)
              {
                  offset = length - startIndex;
                  for (int l = 0; l < offset; l++)
                  {
                    buffer[l] = buffer[startIndex + l];
                  }
                  i = length;
                  continue;
              }

              if (tempFrameFormat == 0)
              {
                tempMessage.frameFormat = STANDARD_FRAME;
                i = i + 2; // Jump the index to the end of the message ID byte.
                // Check the validity of the STD messageID.
                // Should not be bigger than 0x7FF.
                if (buffer[i-1] > 0x7)
                {
                  continue; // Invalid message ID.
                }
                tempMessage.messageID = (int) (((buffer[i - 1] & 0xFF) * Math.pow(16,2))
                    + (buffer[i] & 0xFF));
              }
              else
              {
                tempMessage.frameFormat = EXTENDED_FRAME;
                i = i + 4; // Jump the index to the end of the message ID byte.
                // Check the validity of the EXT messageID.
                // Should not be bigger than 0x1FFFFFFF.
                if (buffer[i-3] > 0x1F)
                {
                  continue; // Invalid message ID.
                }
                tempMessage.messageID = (int) (((buffer[i - 3] & 0xFF) * Math.pow(16,6))
                    + ((buffer[i - 2] & 0xFF) * Math.pow(16,4))
                    + ((buffer[i - 1] & 0xFF) * Math.pow(16,2))
                    + (buffer[i] & 0xFF));
              }

              // Increase the index to the beginning of the data or 
              // invalid index in case of remote frame.
              i++;
              if (tempFrameType == 0)
              {
                tempMessage.frameType = DATA_FRAME;
                for (byte j = 0; j < tempMessage.dataLength; j++)
                {
                  tempMessage.data[j] = (buffer[i++] & 0xFF);
                }
              }
              else
              {
                tempMessage.frameType = REMOTE_FRAME;
              }

              // Apply the filtering process.
              //applyFilteringMessage(tempMessage);
              //incomingMessages.add(tempMessage);
              if(tempMessage.messageID > 201 && tempMessage.messageID < 299){
            	  incomingMessages.add(tempMessage);
            	  Log.d("TAG", "WRITE inputStream: " + tempMessage.messageID + " " + tempMessage.dataLength + " " + tempMessage.data[0]+ " " + tempMessage.data[1]+ " " + tempMessage.data[2]+ " " + tempMessage.data[3]+ " " + tempMessage.data[4]+ " " + tempMessage.data[5]+ " " + tempMessage.data[6]+ " " + tempMessage.data[7] + " buffersize: " + incomingMessages.size());
              }
              Log.d("TAG", "WRITE inputStream: " + incomingMessages.size());
              continue; // No need to check for '\n' or '\r'.
            }
            else
            {
              i++; // Throw away unknown data.
            }

            while((buffer[i] == 13) || (buffer[i] == 10))
            {
              // Check whether the buffer contains \r or \n, and throw them.
              i++;
              if (i < length)
              {
                continue;
              }
              else
              {
                // If the i is at the end of the buffer
                break;
              }
            }
          }*/
        }
        receiveThreadFlag = ReturnCode.THREAD_STOPPED;
      }
    }
	

	public int HexASCIIArrayToInt(byte[] inputArray, int startIndex, 
    		int inputLength)
    {
      // Check the validity of the input.
      if (inputArray == null)
      {
        return -1;
      }

      if ((inputArray[startIndex] > '8') && (inputLength == 8))
      {
        // Since the return value is int, it would not be able to return more
        // than 0x7FFF FFFF, so the input of this method should be limited.
        return -1;
      }
      else if (inputLength > 8)
      {
        // More than 8characters.
        return -1;
      }

      int temporaryValue = 0;
      for (int e = 0; e < inputLength; e++)
      {
        if ((inputArray[startIndex + e] >= '0') &&
            (inputArray[startIndex + e] <= '9'))
        {
          temporaryValue += ((inputArray[startIndex + e] - '0') *
              (Math.pow(16, inputLength - 1 - e)));
        }
        else if ((inputArray[startIndex + e] >= 'A') &&
            (inputArray[startIndex + e] <= 'F'))
        {
          temporaryValue += ((inputArray[startIndex + e] - 'A' + 10) *
              (Math.pow(16, inputLength - 1 - e)));
        }
        else if ((inputArray[startIndex + e] >= 'a') &&
            (inputArray[startIndex + e] <= 'f'))
        {
          temporaryValue += ((inputArray[startIndex + e] - 'a' + 10) *
              (Math.pow(16, inputLength - 1 - e)));
        }
        else
        {
          return -1;  // Unsupported value.
        }
      }
      return temporaryValue;
    }
	
	
	 public String ASCIIArrayToString (byte[] inputArray, int startIndex,
		        int inputLength)
		    {
		    	
		    	
		      // Check the validity of the input.
		      if (inputArray == null)
		      {
		        return null;
		      }

		      char[] tempCharBuffer = new char[inputLength];
		      String tempString;
		      for (int j = 0; j < inputLength; j++)
		      {
		        tempCharBuffer[j] = (char) inputArray[startIndex + j];
		      }
		      try
		      {
		        tempString = String.copyValueOf(tempCharBuffer);
		      }
		      catch (Exception e)
		      {
		        e.printStackTrace();
		        return null;
		      }
		      return tempString;
		    }
	 
    /** A response buffer for each Device / Bluetooth connection. */
    private volatile ResponseBuffer respBuffer;
    
    /**
     * Transmit thread for transmitting bulk message and increase data rate.
     */
    private TransmitBulkMessage transmitThread;
    /** The status of the transmit thread whether it is still running or not. */
    private ReturnCode transmitThreadFlag = ReturnCode.THREAD_STOPPED;

    /** The index of the bulk transmit buffer. */
    private volatile int bulkBufferIndex;
    /** The bulk transmit buffer. */
    private volatile byte[] bulkBuffer = new byte[500];
    /** The semaphore used by the transmit thread and put message method. */
    private Semaphore bulkBufferFlag = new Semaphore(1, false);
    /** The limit of the bulk transmit buffer. */
    private final int bulkBufferFull = 400;
    
    /** Global variable for waiting for response counter. */
    private long timeOutCounter;

    /** Global variable for {@link ReturnCode}. */
    private ReturnCode returnCode;
    
    /** Sleep time for a thread in ms. */
    public final int SLEEP_TIME = 100;
    /** The maximum length of the sending message array. */
    public final byte MAXIMUM_MESSAGE_ARRAY_LENGTH = 50; 
//    /** Input for frameFormat. */
//    public static final byte STANDARD_FRAME = 0, EXTENDED_FRAME = 1;
//
//    /** Input for frameType. BOTH_FRAME is only used internally for filtering. */
//    public static final byte DATA_FRAME = 1, REMOTE_FRAME = 2, BOTH_FRAME = 3;
    /** Input for transmit message format. */
    public static final byte BINARY_FORMAT = 0, ASCII_FORMAT = 1;
    
    volatile ReturnCode deviceStatus = ReturnCode.OBJECT_IS_NOT_INITIALIZED;
    
    /** The transmit buffer. */
    private byte[] dataArray = new byte[MAXIMUM_MESSAGE_ARRAY_LENGTH];
    /** Temporary array for converting the data from an integer to ASCII. */
    private byte[] tempData = null;
    /** Temporary array for converting the message ID from an integer to ASCII. */
    private byte[] tempMessageID = null;
    /** The index used for the transmit buffer. */
    private byte byteIndex = 0;
    
    /**
     * Transmit a message to the controller.
     * There are 2 message formats, ASCII and binary. Binary format is 
     * shorter than the ASCII format, so it takes less time and increase 
     * data rate. 
     * This method is optimized for fast and repetitive sending message for 
     * more than 1 message in the interval of 5ms.
     * Note that sending any message will change the receive message format 
     * to the same format as the sending message.
     * Read the error status to check whether there is an error in sending 
     * the message.
     * @param message       Input: Message to be sent.
     * @param messageFormat Input: ({@link ConstantList#ASCII_FORMAT}, or 
     *     {@link ConstantList#BINARY_FORMAT})
     * @return {@link ReturnCode#SUCCESS}, 
     *         {@link ReturnCode#OBJECT_IS_DEINITIALIZED},
     *         {@link ReturnCode#OBJECT_IS_NOT_INITIALIZED},
     *         {@link ReturnCode#INVALID_MESSAGE_FORMAT},
     *         {@link ReturnCode#INVALID_MESSAGE_ID_VALUE},
     *         {@link ReturnCode#INVALID_FRAME_FORMAT_VALUE}, 
     *         {@link ReturnCode#INVALID_DATA_LENGTH}, 
     *         {@link ReturnCode#INVALID_DATA_VALUE}, 
     *         {@link ReturnCode#INVALID_FRAME_TYPE_VALUE}, 
     *         {@link ReturnCode#EXCEPTION_SEMAPHORE_ACQUIRE}, 
     *         {@link ReturnCode#EXCEPTION_SEMAPHORE_RELEASE}, 
     *         {@link ReturnCode#WAITING_TO_SEND_TIMEOUT}, or 
     *         {@link ReturnCode#EXCEPTION_SLEEP_THREAD}.
     */
    public ReturnCode transmitMessage(MessageStructure message, 
  		  byte messageFormat)
    {
      // Can not be used if the controller object is not initialized.
      if (messageFormat == BINARY_FORMAT)
  	  {
  		  return PutCANblueBinaryMessage(message);
  	  }
  	  else
  	  {
  		  return ReturnCode.INVALID_MESSAGE_FORMAT;
  	  }
    }
    
    public ReturnCode PutCANblueBinaryMessage(MessageStructure message)
    {


      // Check the validity of the input.
      if (message == null)
      {
        return ReturnCode.MESSAGESTRUCTURE_OBJECT_NOT_VALID;
      }
      byteIndex = 0;
      dataArray[byteIndex++] = 'X';

      dataArray[byteIndex] = 0x00; // Prepare the frame info byte.
      if (message.frameFormat == STANDARD_FRAME)
      {
        // Fill with nothing.
        if ((message.messageID > 0x7FF)  || (message.messageID < 0))
        {
        	return ReturnCode.INVALID_MESSAGE_ID_VALUE;
        }
        else
        {
        	dataArray[byteIndex] |= message.dataLength;
        }
      }
      else if (message.frameFormat == EXTENDED_FRAME)
      {
        dataArray[byteIndex] |= 0x80;
        if ((message.messageID > 0x1FFFFFFFL) || (message.messageID < 0))
        {
          return ReturnCode.INVALID_MESSAGE_ID_VALUE;
        }
        else
        {
        	dataArray[byteIndex] |= message.dataLength;
        }
      }
      else
      {
        return ReturnCode.INVALID_FRAME_FORMAT_VALUE;
      }

      if (message.frameType == DATA_FRAME)
      {
        // Fill with nothing.
      }
      else if (message.frameType == REMOTE_FRAME)
      {
        dataArray[byteIndex] |= 0x40;
      }
      else
      {
        return ReturnCode.INVALID_FRAME_TYPE_VALUE;
      }

      byteIndex++; // Increase the byte index and fill it with message ID.
      if (message.frameFormat == STANDARD_FRAME)
      {
        for (byte i = 0; i < 2; i++)
        {
        	dataArray[byteIndex++] = (byte)(0xFF
        			& (message.messageID >> (8 * (1 - i))));
        }
      }
      else if (message.frameFormat == EXTENDED_FRAME)
      {
        for (byte i = 0; i < 4; i++)
        {
        	dataArray[byteIndex++] = (byte)(0xFF
        			& (message.messageID >> (8 * (3 - i))));
        }
      }

      // Check the data length.
      if ((message.dataLength > 8) || (message.dataLength < 0))
      {
        return ReturnCode.INVALID_DATA_LENGTH;
      }
      // Fill the data for data frame.
      if (message.frameType == DATA_FRAME)
      {
        for(byte i = 0; i < message.dataLength; i++)
        {
        	if ((message.data[i] > 0xFF) || (message.data[i] < 0))
            {
              return ReturnCode.INVALID_DATA_VALUE;
            }
        	dataArray[byteIndex++] = (byte) message.data[i];
        }
      }
      else if (message.frameType == REMOTE_FRAME)
      {
        // Fill with nothing.
      }

      // Put the data into the buffer.
      timeOutCounter = 0;
      while(true)
      {
        if ((bulkBufferIndex < bulkBufferFull))
        {
          try
          {
            bulkBufferFlag.acquire();
          }
          catch (InterruptedException e)
          {
            e.printStackTrace();
            return ReturnCode.EXCEPTION_SEMAPHORE_ACQUIRE;
          }
          for (byte i = 0; i < byteIndex; i++)
          {
            bulkBuffer[bulkBufferIndex++] = dataArray[i];
          }
          try
          {
            bulkBufferFlag.release();
          }
          catch (Exception e)
          {
            e.printStackTrace();
            return ReturnCode.EXCEPTION_SEMAPHORE_RELEASE;
          }
          break;
        }
        timeOutCounter++;
        if (timeOutCounter >= 1000)
        {
          return ReturnCode.WAITING_TO_SEND_TIMEOUT;
        }
        try
        {
          Thread.sleep(0, 100000);
        }
        catch (Exception e)
        {
          e.printStackTrace();
          return ReturnCode.EXCEPTION_SLEEP_THREAD;
        }
      }
      return ReturnCode.SUCCESS;
    }
  
    
	   /**
     * Put data to Bluetooth output stream and a '\n' is automatically added.
     * This method is used to transmit command.
     * @param data Input: String of command.
     * @return {@link ReturnCode#SUCCESS}, 
     *         {@link ReturnCode#STRING_NOT_VALID}, 
     *         {@link ReturnCode#FAIL_CONVERTING_TO_ARRAY}, or 
     *         {@link ReturnCode#FAIL_WRITING_TO_BLUETOOTH}.
     */
    private ReturnCode writeStringToBluetooth(String data)
    {
      // Check the validity of the input.
      if (data == null)
      {
        return ReturnCode.STRING_NOT_VALID;
      }
      char returnLine = 0xA;
      String sendCommand = data + returnLine;
      byte[] buffer;

      try
      {
        buffer = sendCommand.getBytes();
      }
      catch (Exception e)
      {
        e.printStackTrace();
        return ReturnCode.FAIL_CONVERTING_TO_ARRAY;
      }

      try
      {
    	  outStream.write(buffer);
      }
      catch (Exception e)
      {
        e.printStackTrace();
        return ReturnCode.FAIL_WRITING_TO_BLUETOOTH;
      }
      return ReturnCode.SUCCESS;
    }
	
	   /**
     * Send a command and wait for a response.
     * In case of multiple line responses, more waiting time is handled by 
     * each calling methods.
     * @param data            Input: String of command (without a '\n').
     * @param waitForResponse Input: Maximum time needed to wait for the 
     *   response (in {@value API.ADK.ConstantList#SLEEP_TIME}ms unit).
     * @return {@link ReturnCode#SUCCESS}, 
     *         {@link ReturnCode#STRING_NOT_VALID}, 
     *         {@link ReturnCode#FAIL_CONVERTING_TO_ARRAY}, 
     *         {@link ReturnCode#FAIL_WRITING_TO_BLUETOOTH}, 
     *         {@link ReturnCode#WAITING_FOR_RESPONSE_TIMEOUT}, 
     *         {@link ReturnCode#EXCEPTION_SLEEP_THREAD}, or 
     *         {@link ReturnCode#NO_RESPONSE}.
     */
    private ReturnCode sendAndReceiveCommand(String data, int waitForResponse)
    {
      // Check the validity of the input.
      if (data == null)
      {
        return ReturnCode.STRING_NOT_VALID;
      }

      // empty the response buffer.
      respBuffer.resetResponseBuffer();

      returnCode = writeStringToBluetooth(data);
      if (returnCode != ReturnCode.SUCCESS)
      {
        return returnCode;
      }

      // Wait for any response and return immediately.
      // In case of multiple line responses, it is handled by each
      // calling methods.
      timeOutCounter = 0;
      while(respBuffer.responseFlag == false)
      {
        timeOutCounter++;
        if ((timeOutCounter >= waitForResponse) || (timeOutCounter < 0))
        {
          return ReturnCode.WAITING_FOR_RESPONSE_TIMEOUT;
        }
        try
        {
          Thread.sleep(SLEEP_TIME);
        }
        catch (Exception e)
        {
          e.printStackTrace();
          return ReturnCode.EXCEPTION_SLEEP_THREAD;
        }
      }
      // Check the content of the buffer
      if (respBuffer.bufferIndex == 0)
      {
        return ReturnCode.NO_RESPONSE;
      }
      return ReturnCode.SUCCESS;
    }

    /**
     * The transmit thread for transmitting bulk CAN message. This thread is 
     * used internally. It is started automatically after successfully 
     * connecting to the device in {@link Driver.CANDevice#connectDevice()}.
     * @author hroesdiyono
     */
    private class TransmitBulkMessage extends Thread
    {
      /** Run the thread. */
      public void run()
      {
        Date timeValue;
        long timeStamp1, timeStamp2, timeDifference;

        transmitThreadFlag = ReturnCode.THREAD_RUNNING;

        timeValue = new Date();
        timeStamp1 = timeValue.getTime();
        while(true)
        {
          try
          {
            Thread.sleep(0, 500000);
          }
          catch (InterruptedException e2)
          {
            e2.printStackTrace();
            break;
          }
          
    		String status = _ToThreadMessagesQueue2.poll();
    		if(status != null && status == "EndThread"){
    			_StatusMessagesQueue.add("ThreadEnded");
    			return;
    		}
          
          timeValue = new Date();
          timeStamp2 = timeValue.getTime();
          timeDifference = timeStamp2 - timeStamp1;
          if ((bulkBufferIndex >= bulkBufferFull) || (timeDifference >= 5))
          {
            if(bulkBufferIndex == 0)
            {
            }
            else
            {
              try
              {
                bulkBufferFlag.acquire();
              }
              catch (InterruptedException e1)
              {
                e1.printStackTrace();
                break;
              }
              try
              {
            	  outStream.write(bulkBuffer, 0, bulkBufferIndex);
              }
              catch (IOException e)
              {
                e.printStackTrace();
                break;
              }
              bulkBufferIndex = 0;
              try
              {
                bulkBufferFlag.release();
              }
              catch (Exception e)
              {
                e.printStackTrace();
                break;
              }
            }
            timeValue = new Date();
            timeStamp1 = timeValue.getTime();
          }
        }
        transmitThreadFlag = ReturnCode.THREAD_STOPPED;
      }
    }

	private boolean sendByteMessageToTermidor2(int msgID) {
		int msgSize = 1;
		try {
			ReturnCode returnCode = null;
			MessageStructure msgToSend = messageSetup(msgSize, msgID); 
			msgToSend.data[0] = 0x31;
			returnCode = transmitMessage(msgToSend, ConstantList.BINARY_FORMAT);
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
	
    public boolean sendEraseCommandToTermidor2(int msgID)
    {
		int msgSize = 1;
		try {
			ReturnCode returnCode = null;
			MessageStructure msgToSend = messageSetup(msgSize, msgID); 
			msgToSend.data[0] = 0x32;
			returnCode = transmitMessage(msgToSend, ConstantList.BINARY_FORMAT);
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
	
	
}
