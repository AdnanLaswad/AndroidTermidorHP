package com.evanhoe.tango.objs;



public class WorkOrderViewEntryItem implements WorkOrderViewItem{

	public final String title;
	public final String subtitle;
	public  final String status;
	
	public WorkOrderViewEntryItem(String title, String subtitle, String status) {
		this.title = title;
		this.subtitle = subtitle;
		this.status = status;
	}
	
	@Override
	public boolean isSection() {
		return false;
	}

public String getStatus(){
		return this.status;
}
}
