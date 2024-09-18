package com.rak.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.rak.entity.EmpLeaveBalance;
import com.rak.entity.Employee;
import com.rak.enums.LeaveType;
import com.rak.exception.EmpNotFoundException;
import com.rak.repository.EmpLeaveBalRepository;
import com.rak.repository.EmployeeRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class EmpLeaveBalanceServiceImpl implements EmpLeaveBalanceService
{
	@Autowired
	private EmployeeRepository employeeRepository;
	
	@Autowired
	private EmpLeaveBalRepository empLeaveBalRepository;
	
	@Override
	public EmpLeaveBalance getEmpLeaveBalance(Long empId) throws EmpNotFoundException 
	{
		Employee emp=employeeRepository.findById(empId).orElseThrow( ()-> new EmpNotFoundException("employee not found with given empId...!!!") );
		
		EmpLeaveBalance empLeaveBalance= empLeaveBalRepository.findByEmployeeEmpId(empId).get();
		
		return empLeaveBalance;
	}
	
	// Reset total leaves for all employees on January 1st every year
	// (seconds, minutes, hours, day of the month, month, , day of the week) => * means any day of the week
	@Scheduled(cron = "0 0 0 1 1 *") 
	public void resetEmployeeLeaves() 
	{
        List<EmpLeaveBalance> empLeaveBalList=empLeaveBalRepository.findAll();
        
        empLeaveBalList = empLeaveBalList
				        			.stream()
				        			.map(emp->{
				        				emp.setSickLeaveBal(LeaveType.SICK_LEAVE.getDefaultDays());
				        				emp.setCasualLeaveBal(LeaveType.CASUAL_LEAVE.getDefaultDays());
				        				emp.setOtherLeaveBal(LeaveType.OTHERS.getDefaultDays());
				        				emp.setTotalLeaveBal(LeaveType.SICK_LEAVE.getDefaultDays()+LeaveType.CASUAL_LEAVE.getDefaultDays()+LeaveType.OTHERS.getDefaultDays());
				        				return emp;
				        			})
				        			.toList();
        
        empLeaveBalList = empLeaveBalList
					        			.stream()
					        			.map(obj->empLeaveBalRepository.save(obj))
					        			.toList();
        
	}
	
}