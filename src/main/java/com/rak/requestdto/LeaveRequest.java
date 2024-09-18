package com.rak.requestdto;

import java.time.LocalDate;

import com.rak.enums.LeaveType;

import lombok.Data;

@Data
public class LeaveRequest 
{
    private LeaveType leaveType;
    private LocalDate startDate;
    private LocalDate endDate;
    private String reason;
}