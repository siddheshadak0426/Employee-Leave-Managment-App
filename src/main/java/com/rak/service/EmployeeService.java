package com.rak.service;

import java.util.List;

import com.rak.entity.EmpLeaves;
import com.rak.enums.LeaveStatus;
import com.rak.exception.EmailIdAlreadyExistException;
import com.rak.exception.EmpNotFoundException;
import com.rak.exception.InsufficientLeaveException;
import com.rak.exception.StartDateAfterEndDateException;
import com.rak.requestdto.LeaveRequest;
import com.rak.requestdto.EmpRegRequest;
import com.rak.responsedto.EmployeeResponse;

public interface EmployeeService 
{
	void registerEmployee(EmpRegRequest empRegRequest) throws EmpNotFoundException, EmailIdAlreadyExistException;

    void changePassword(Long empId, String newPassword) throws EmpNotFoundException;
    
    List<EmpLeaves> getAllAppliedLeaves(Long empId) throws EmpNotFoundException;
    
    LeaveStatus getLeaveStatus(Long leaveId);
    
    List<EmployeeResponse> getAllEmps();
    
    void applyForLeave(Long empId, LeaveRequest leaveRequest) throws EmpNotFoundException, StartDateAfterEndDateException, InsufficientLeaveException;

}