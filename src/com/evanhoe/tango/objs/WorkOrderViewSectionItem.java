package com.evanhoe.tango.objs;


public class WorkOrderViewSectionItem implements WorkOrderViewItem{

	private final String title;
	
	public WorkOrderViewSectionItem(String title) {
		this.title = title;
	}
	
	public String getTitle(){
		return title;
	}
	
	@Override
	public boolean isSection() {
		return true;
	}

}
