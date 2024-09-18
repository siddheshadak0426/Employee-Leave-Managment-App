package com.rak.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rak.entity.Employee;
import com.rak.exception.IncorectLeaveIdException;
import com.rak.exception.InsufficientLeaveException;
import com.rak.exception.StartDateAfterEndDateException;
import com.rak.responsedto.EmployeeResponse;
import com.rak.service.AdminService;

@RestController
@RequestMapping("/admin")
public class AdminController 
{
    @Autowired
    private AdminService adminService;
    
    // Tested
    @PostMapping("/generate-employee") // http://localhost:8080/admin/generate-employee
    @PreAuthorize("hasRole('ADMIN')") //  @PreAuthorize("hasRole('ADMIN') or hasRole('EMP')")
    public ResponseEntity<Employee> generateEmployee(@RequestParam String firstName, @RequestParam String lastName) 
    {
       Employee empResponse=adminService.generateEmployee(firstName, lastName);
       System.out.println("generate empId...");
       return new ResponseEntity<>(empResponse, HttpStatus.CREATED);
    }
    
    // Tested
    @GetMapping("/employees") // http://localhost:8080/admin/employees
    @PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<EmployeeResponse>> getAllEmployees() 
    {
    	List<EmployeeResponse> responseList=adminService.getAllEmployees();
    	return new ResponseEntity<>(responseList, HttpStatus.OK);
    }
    
    // Tested
    @PostMapping("/approve-leave") // http://localhost:8080/admin/approve-leave?leaveId=1
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> approveEmpLeave(@RequestParam Long leaveId) throws IncorectLeaveIdException, StartDateAfterEndDateException, InsufficientLeaveException  
    {
       	adminService.approveLeave(leaveId);
       	return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    
    // 
    @PostMapping("/reject-leave") // http://localhost:8080/admin/reject-leave?leaveId=2
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> rejectEmpLeave(@RequestParam Long leaveId) throws IncorectLeaveIdException 
    {
        adminService.rejectLeave(leaveId);
       	return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
  
}