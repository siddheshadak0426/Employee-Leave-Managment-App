package com.rak.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rak.entity.EmpLeaveBalance;
import com.rak.entity.EmpLeaves;
import com.rak.exception.EmailIdAlreadyExistException;
import com.rak.exception.EmpNotFoundException;
import com.rak.exception.InsufficientLeaveException;
import com.rak.exception.StartDateAfterEndDateException;
import com.rak.requestdto.LeaveRequest;
import com.rak.requestdto.UpdateProfileRequest;
import com.rak.responsedto.EmployeeResponse;
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
    @PutMapping("/update-profile/empId/{empId}") // http://localhost:8080/employees/update-profile/empId/249141001
    @PreAuthorize("hasRole('EMP')")
    public ResponseEntity<EmployeeResponse> updateEmployeeProfile(@PathVariable Long empId, @RequestBody UpdateProfileRequest UpdateProfileRequest) throws EmpNotFoundException, EmailIdAlreadyExistException 
    {
    	EmployeeResponse empDto=employeeService.updateEmployeeProfile(empId, UpdateProfileRequest);
    	return new ResponseEntity<>(empDto, HttpStatus.OK);
    }
    
    // Tested
    @PostMapping("/change-password/empId/{empId}") // http://localhost:8080/employees/change-password/empId/249161001?newPassword=Gapat@123
    @PreAuthorize("hasRole('EMP')")
    public ResponseEntity<Void> changePassword(@PathVariable Long empId, @RequestParam String newPassword) throws EmpNotFoundException 
    {
        employeeService.changePassword(empId, newPassword);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    // Tested
    @PostMapping("/leaves/empId/{empId}") // http://localhost:8080/employees/leaves/empId/249161001
    @PreAuthorize("hasRole('EMP')")
    public ResponseEntity<Void> applyForLeave(@PathVariable Long empId, @RequestBody LeaveRequest leaveRequest) throws EmpNotFoundException, StartDateAfterEndDateException, InsufficientLeaveException
    {
    	employeeService.applyForLeave(empId, leaveRequest);
    	return ResponseEntity.status(HttpStatus.CREATED).build();
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
    public ResponseEntity<EmpLeaveBalance> getEmpLeaveBalance(@PathVariable Long empId) throws EmpNotFoundException
    {
    	EmpLeaveBalance empLeaveBal= empLeaveBalanceService.getEmpLeaveBalance(empId);
    	return ResponseEntity.ok(empLeaveBal);
    }
    
    // 
    @GetMapping("/leaves-status/leaveId/{leaveId}") // http://localhost:8080/employees/leaves-status/leaveId/12   
    @PreAuthorize("hasRole('EMP')")
    public ResponseEntity<EmpLeaves> getLeaveStatus(@PathVariable Long leaveId) 
    {
        return ResponseEntity.ok(employeeService.getLeaveStatus(leaveId));
    }
    
}