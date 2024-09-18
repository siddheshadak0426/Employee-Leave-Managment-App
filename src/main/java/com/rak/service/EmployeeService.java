package com.rak.service;

import java.util.List;

import com.rak.entity.EmpLeaves;
import com.rak.entity.Employee;
import com.rak.exception.EmailIdAlreadyExistException;
import com.rak.exception.EmpIdNotExistException;
import com.rak.exception.EmpNotFoundException;
import com.rak.exception.InsufficientLeaveException;
import com.rak.exception.StartDateAfterEndDateException;
import com.rak.requestdto.EmployeeRegRequest;
import com.rak.requestdto.LeaveRequest;
import com.rak.requestdto.UpdateProfileRequest;
import com.rak.responsedto.EmployeeResponse;

public interface EmployeeService 
{
	EmployeeResponse updateEmployeeProfile(Long empId, UpdateProfileRequest updateProfileRequest) throws EmpNotFoundException, EmailIdAlreadyExistException;

    void changePassword(Long empId, String newPassword) throws EmpNotFoundException;
    
    List<EmpLeaves> getAllAppliedLeaves(Long empId) throws EmpNotFoundException;
    
    EmpLeaves getLeaveStatus(Long leaveId);
    
    List<EmployeeResponse> getAllEmps();
    
    void applyForLeave(Long empId, LeaveRequest leaveRequest) throws EmpNotFoundException, StartDateAfterEndDateException, InsufficientLeaveException;
    

    //=========
    
    EmployeeResponse registerEmployee(EmployeeRegRequest employeeRegRequest) throws EmailIdAlreadyExistException, EmpIdNotExistException;

}