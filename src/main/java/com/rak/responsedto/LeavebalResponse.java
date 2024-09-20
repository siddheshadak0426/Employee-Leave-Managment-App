package com.rak.responsedto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LeavebalResponse 
{
	private int sickLeaveBal;
    private int casualLeaveBal;
    private int otherLeaveBal;
    private int totalLeaveBal;
}
