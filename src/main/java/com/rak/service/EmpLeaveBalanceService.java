package com.rak.service;

import com.rak.entity.EmpLeaveBalance;
import com.rak.exception.EmpNotFoundException;

public interface EmpLeaveBalanceService 
{
	EmpLeaveBalance getEmpLeaveBalance(Long empId) throws EmpNotFoundException;
}
