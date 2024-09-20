package com.rak.service;

import com.rak.exception.EmpNotFoundException;
import com.rak.responsedto.LeavebalResponse;

public interface EmpLeaveBalanceService 
{
	LeavebalResponse getEmpLeaveBalance(Long empId) throws EmpNotFoundException;
}
