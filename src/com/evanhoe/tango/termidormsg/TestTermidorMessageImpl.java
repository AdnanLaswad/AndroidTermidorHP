package com.evanhoe.tango.termidormsg;

import com.evanhoe.tango.objs.WorkOrder;
import com.evanhoe.tango.objs.TermidorData;

public class TestTermidorMessageImpl implements TermidorMessageInterface
{
    private static String currentServiceWorkorderId;
    /** This method will send the address of a WorkOrder to the Termidor HP device.
     * 
     * @param wo - WorkOrder to be sent to Termidor HP
     * @return boolean - success or failure of sending the workorder to the device
     * @throws InterruptedException 
     */
    public boolean sendWorkOrderToTermidorHP ( WorkOrder wo ) throws InterruptedException
    {
        currentServiceWorkorderId = wo.getServiceWorkOrderId();
        return true;
    }

    /** This method will request data from the Termidor HP device
     * 
     * @return WorkOrderDetail - the Termidor HP existing data
     * @throws InterruptedException 
     */

    public TermidorData getData (  ) throws InterruptedException
    {
        /** quickly bail out of the function if there is no workorder, we return NULL in this case */
        if ( currentServiceWorkorderId == null ) return null;

        TermidorData thisData = new TermidorData();
        thisData.setHtModeInjectorCount(10);
        thisData.setWorkOrderNumber(currentServiceWorkorderId);
	   	thisData.setLocation ( "xxx" );
		thisData.setPidDisplayFirmware ( "0.1.1" );
		thisData.setBaseControllerFirmware ( "0.1.1" );
		thisData.setBaseDisplayFirm ( "0.1.1" );
		thisData.setUnitID ( "01:23:45:67:89:AB" );
		//thisData.setWorkOrderNumber ( "xxx" );
		thisData.setAddress ( "some address" );
		thisData.setCity ( "cityname" );
		thisData.setState ( "KL" );
		thisData.setWorkOrderStartDateStamp ( "01-jan-1900 01:00:00" );
		thisData.setProductApplied ( "KillTermites" );
		thisData.setSoilType ( (short) 1 );
		thisData.setInjectorTimer ( (float) 1.0 );
		thisData.setHtModePumpVolume ( (float) 1.0 );
		thisData.setHtModeInjectorCount ( 12 );
		thisData.setHtModeActiveIngredientUsage ( (float) 1.0 );
		thisData.setHtModeWaterUsage ( (float) 1.0 );
		thisData.setSaModeActiveIngredientUsage ( (float) 1.0 );
		thisData.setSaModeWaterUsage ( (float) 1.0 );
		thisData.setWorkOrderStartTime ( "01-jan-1900 01:01:01" );
		thisData.setTimeToComplete ( 120 );
		thisData.setLocationDone ( true );
		thisData.setHtModeEnabled ( true );
		thisData.setSaModeEnabled ( false );
		thisData.setPumpAlarm ( false );
		thisData.setWaterAlarm ( false );
		thisData.setHtFlowInSAMode ( false );
		thisData.setLastErrorMessage("SUCCESS");
        return thisData;
    }


    /** This method will erase the existing data on the Termidor HP device.
     * 
     * @return boolean - success or failure of the data erasure.
     */
    public boolean eraseData (  )
    {
        return true;
    }
    
    public boolean initialize ( String unitAddress ){return true;}
    public void deInitialize (  ){}
}
