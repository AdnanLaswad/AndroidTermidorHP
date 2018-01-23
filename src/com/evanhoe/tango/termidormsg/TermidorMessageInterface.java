package com.evanhoe.tango.termidormsg;

import com.evanhoe.tango.objs.WorkOrder;
import com.evanhoe.tango.objs.TermidorData;
import API.ADK.API_ADK;

/** This is the interface class for sending and receiving messages
 * between the Android Application and the Termidor HP device.
 * @author jmcginn
 *
 */
public interface TermidorMessageInterface
{
    /** This method will send the address of a WorkOrder to the Termidor HP device.
     * 
     * @param wo - WorkOrder to be sent to Termidor HP
     * @return boolean - success or failure of sending the workorder to the device
     * @throws InterruptedException 
     */
    public boolean sendWorkOrderToTermidorHP ( WorkOrder wo ) throws InterruptedException;

    /** This method will request data from the Termidor HP device
     * 
     * @return WorkOrderDetail - the Termidor HP existing data
     *         null            - if no data exists on the Unit (no workorder being worked on)
     * @throws InterruptedException 
     */

    public TermidorData getData (  ) throws InterruptedException;


    /** This method will erase the existing data on the Termidor HP device.
     * 
     * @return boolean - success or failure of the data erasure.
     * @throws InterruptedException 
     */
    public boolean eraseData (  ) throws InterruptedException;
    
    public boolean initialize ( String unitAddress );
    public void deInitialize (  );
}
