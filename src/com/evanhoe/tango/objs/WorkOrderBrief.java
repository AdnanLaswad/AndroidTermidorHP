package com.evanhoe.tango.objs;

public class WorkOrderBrief {
	
	private String lastSyncTime;
	private String workorderID;
	
	public WorkOrderBrief(String lastSyncTime, String workorderID) {
		super();
		this.lastSyncTime = lastSyncTime;
		this.workorderID = workorderID;
	}

	public String getLastSyncTime() {
		return lastSyncTime;
	}

	public void setLastSyncTime(String lastSyncTime) {
		this.lastSyncTime = lastSyncTime;
	}

	public String getWorkorderID() {
		return workorderID;
	}

	public void setWorkorderID(String workorderID) {
		this.workorderID = workorderID;
	}
	
	

}
