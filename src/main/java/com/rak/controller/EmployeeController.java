package com.rak.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rak.entity.EmpLeaves;
import com.rak.enums.LeaveStatus;
import com.rak.exception.EmpNotFoundException;
import com.rak.exception.InsufficientLeaveException;
import com.rak.exception.StartDateAfterEndDateException;
import com.rak.requestdto.ChangePasswordRequest;
import com.rak.requestdto.LeaveRequest;
import com.rak.responsedto.LeavebalResponse;
import com.rak.service.EmpLeaveBalanceService;
import com.rak.service.EmployeeService;

@RestController
@RequestMapping("/employees")
public class EmployeeController 
{    
    @Autowired
    private EmployeeService employeeService;
    
    @Autowired
    private EmpLeaveBalanceService empLeaveBalanceService;
    
    // Tested
    @PostMapping("/change-password") // http://localhost:8080/employees/change-password
    @PreAuthorize("hasRole('EMP')")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest) throws EmpNotFoundException 
    {
        employeeService.changePassword(changePasswordRequest.getEmpId(), changePasswordRequest.getNewPassword());
        return new ResponseEntity<>("password changed successfully...!!!",HttpStatus.OK);
    }
    
    // Tested
    @PostMapping("/leaves/empId/{empId}") // http://localhost:8080/employees/leaves/empId/249161001
    @PreAuthorize("hasRole('EMP')")
    public ResponseEntity<String> applyForLeave(@PathVariable Long empId, @RequestBody LeaveRequest leaveRequest) throws EmpNotFoundException, StartDateAfterEndDateException, InsufficientLeaveException
    {
    	employeeService.applyForLeave(empId, leaveRequest);
    	return ResponseEntity.status(HttpStatus.CREATED).body("leave applied successfully, waiting for approval...!!!");
    }
   
    // Tested
    @GetMapping("leaves/empId/{empId}") // http://localhost:8080/employees/leaves/empId/249161001
    @PreAuthorize("hasRole('EMP')")
    public ResponseEntity<List<EmpLeaves>> getAllAppliedLeaves(@PathVariable Long empId) throws EmpNotFoundException 
    {
    	List<EmpLeaves> leaves = employeeService.getAllAppliedLeaves(empId);
        return ResponseEntity.ok(leaves);
    }
    
    // 
    @GetMapping("/leave-balance/empId/{empId}") // http://localhost:8080/employees/leave-balance/empId/12345
    @PreAuthorize("hasRole('EMP')")
    public ResponseEntity<LeavebalResponse> getEmpLeaveBalance(@PathVariable Long empId) throws EmpNotFoundException
    {
    	LeavebalResponse leaveBal= empLeaveBalanceService.getEmpLeaveBalance(empId);
    	return ResponseEntity.ok(leaveBal);
    }
    
    // 
    @GetMapping("/leaves-status/leaveId/{leaveId}") // http://localhost:8080/employees/leaves-status/leaveId/12   
    @PreAuthorize("hasRole('EMP')")
    public ResponseEntity<LeaveStatus> getLeaveStatus(@PathVariable Long leaveId) 
    {
        return ResponseEntity.ok(employeeService.getLeaveStatus(leaveId));
    }
    
}