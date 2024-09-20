package com.rak.service;

import java.util.List;

import com.rak.entity.Employee;
import com.rak.exception.IncorectLeaveIdException;
import com.rak.exception.InsufficientLeaveException;
import com.rak.exception.StartDateAfterEndDateException;
import com.rak.responsedto.EmployeeResponse;

public interface AdminService 
{
    Long generateEmployee();
    void approveLeave(Long leaveId) throws IncorectLeaveIdException, StartDateAfterEndDateException, InsufficientLeaveException;
    void rejectLeave(Long leaveId) throws IncorectLeaveIdException;
    List<EmployeeResponse> getAllEmployees();
}