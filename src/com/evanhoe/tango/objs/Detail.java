package com.evanhoe.tango.objs;

/**
 * Created by Sana Fiaz on 12/17/2017.
 */

public class Detail {
    private WorkOrder workOrder;

    public Detail(WorkOrder workOrder){
        this.workOrder=workOrder;
    }
    public void setWorkOrder(WorkOrder workOrder){
        this.workOrder=workOrder;
    }
    public WorkOrder getWorkOrder(){
        return workOrder;
    }
}
