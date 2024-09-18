package com.rak.enums;

public enum LeaveType 
{
    SICK_LEAVE(10),
    CASUAL_LEAVE(15),
    OTHERS(10);
	
	private int defaultDays;
	
	private LeaveType(int defaultDays)
	{
		this.defaultDays=defaultDays;
	}
	
	public int getDefaultDays()
	{
		return this.defaultDays;
	}
}