package com.rak.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.rak.entity.EmpLeaveBalance;
import com.rak.entity.EmpLeaves;
import com.rak.entity.Employee;
import com.rak.enums.LeaveStatus;
import com.rak.enums.LeaveType;
import com.rak.enums.Role;
import com.rak.exception.IncorectLeaveIdException;
import com.rak.exception.InsufficientLeaveException;
import com.rak.exception.StartDateAfterEndDateException;
import com.rak.repository.EmpLeaveBalRepository;
import com.rak.repository.EmployeeRepository;
import com.rak.repository.LeaveRepository;
import com.rak.responsedto.EmployeeResponse;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class AdminServiceImpl implements AdminService 
{
    
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private LeaveRepository leaveRepository;
    
    @Autowired
    private EmpLeaveBalRepository empLeaveBalRepository;
    
    private static Long empCounter=1000L;
    
    private static final Logger logger=LoggerFactory.getLogger(AdminServiceImpl.class);

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Employee generateEmployee(String firstName, String lastName)
    {
    	// create employee object
    	Employee emp=Employee
    					.builder()
    					.empId(generateEmpId())
    					.firstName(firstName)
    					.lastName(lastName)
    					.password(passwordEncoder.encode(generateTempPassword()))
    					.role(Role.ROLE_EMP)
    					.isProfileCompleted(false)
    					.leaveList(new ArrayList<>())
    					.build();
    	
        employeeRepository.save(emp); // firstly save parent entity => here EmpLeaveBalance is null
        
        EmpLeaveBalance empLeaveBalance=new EmpLeaveBalance();
        empLeaveBalance.setEmployee(emp); 
        empLeaveBalRepository.save(empLeaveBalance);
        
        logger.debug("new employee is created with employee details ==>> {} ", emp);
    	return emp;
    }
    
    @Override
    public List<EmployeeResponse> getAllEmployees()
    {
    	List<Employee> empList= employeeRepository.findAll();
    	List<EmployeeResponse> responseList=empList
    										.stream()
    										.map(emp->EmployeeResponse
    												.builder()
    												.empId(emp.getEmpId())
    												.firstName(emp.getFirstName())
    												.lastName(emp.getLastName())
    												.email(emp.getEmail())
    												.mobile(emp.getMobile())
    												.build()
    										)
    										.toList();
    	
        return responseList;
    }

    @Override
    public void approveLeave(Long leaveId) throws IncorectLeaveIdException, StartDateAfterEndDateException, InsufficientLeaveException 
    {
    	EmpLeaves leaveObj= leaveRepository.findByLeaveId(leaveId).orElseThrow( ()-> new IncorectLeaveIdException("incorect leaveId...!!!") );
    	Employee  emp=leaveObj.getEmp();
    	EmpLeaveBalance empLeaveBal=empLeaveBalRepository.findByEmployeeEmpId(emp.getEmpId()).get();
    	
    	
    	LocalDate startDate=leaveObj.getStartDate();
    	LocalDate endDate=leaveObj.getEndDate();
    	LeaveType leaveType=leaveObj.getLeaveType();
   
    	int noOfLeaveDays=(int) ChronoUnit.DAYS.between(startDate, endDate);
    	
    	if(startDate.isAfter(endDate))
    		throw new StartDateAfterEndDateException("start date must be before endDate...!!!");
    	
    	if(!isLeaveAvailable(emp, leaveType, noOfLeaveDays))
    		throw new InsufficientLeaveException("leave balalance is no sufficient...!!!");
    	
    	deductLeavesFromEmpLeaveBalance(noOfLeaveDays, leaveType, empLeaveBal);
    	empLeaveBalRepository.save(empLeaveBal);
    	leaveObj.setLeaveStatus(LeaveStatus.APPROVED);
    	leaveRepository.save(leaveObj);
    }
    
    @Override
	public void rejectLeave(Long leaveId) throws IncorectLeaveIdException 
    {
    	EmpLeaves leaveObj= leaveRepository.findByLeaveId(leaveId).orElseThrow( ()-> new IncorectLeaveIdException("incorect leaveId...!!!") );
    	leaveObj.setLeaveStatus(LeaveStatus.REJECTED);
    	leaveRepository.save(leaveObj);
    }

    // ========================== Utility Method ====================
    
    public Long generateEmpId()
    {
		return (long) Math.floor(Math.random()*1000000000+1);
//    	LocalDate date=LocalDate.now();
//		
//		int year=date.getYear()%100;
//		int month=date.getMonthValue();
//		int day=date.getDayOfMonth();
//		
//		StringBuilder str=new StringBuilder();
//		str.append(year).append(month).append(day).append(empCounter++);
//		Long empId=Long.parseLong(str.toString());
//		return empId;
		
    }
    
    
    
    public String generateTempPassword()
    {
    	String characters="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890!@#$%&*";
    	StringBuilder password=new StringBuilder("");
    	int passwordLength=8;
    	
    	for(int i=0;i<passwordLength;i++)
    	{
            int index = (int) (Math.random() * characters.length());
            password.append(characters.charAt(index));
    	}
    	
    	// logger.debug("password is generated: ", password);
    	return password.toString();
    }
    
    
    
    
    
   
    public boolean isLeaveAvailable(Employee emp, LeaveType leaveType, int leaveDays) 
    {
		EmpLeaveBalance empLeaveBalance= empLeaveBalRepository.findByEmployeeEmpId(emp.getEmpId()).get();
		switch (leaveType) 
        {
            case SICK_LEAVE:
                if (empLeaveBalance.getSickLeaveBal() >= leaveDays) 
                    return true;
                break;
            case CASUAL_LEAVE:
                if (empLeaveBalance.getCasualLeaveBal() >= leaveDays) 
                
                    return true;
                break;
            case OTHERS:
                if (empLeaveBalance.getOtherLeaveBal() >= leaveDays) 
                    return true;
                break;
        }
        
        return false;  // Insufficient leave balance
    }
    
    public void deductLeavesFromEmpLeaveBalance(int noOfDays, LeaveType leaveType, EmpLeaveBalance empLeaveBalance)
    {
    	if(LeaveType.SICK_LEAVE==leaveType) 
    		empLeaveBalance.setSickLeaveBal(empLeaveBalance.getSickLeaveBal()-noOfDays);

    	else if(LeaveType.CASUAL_LEAVE==leaveType) 
    		empLeaveBalance.setCasualLeaveBal(empLeaveBalance.getCasualLeaveBal()-noOfDays);
    	
    	else 
    		empLeaveBalance.setOtherLeaveBal(empLeaveBalance.getOtherLeaveBal()-noOfDays);
    	
		empLeaveBalance.setTotalLeaveBal(empLeaveBalance.getTotalLeaveBal()-noOfDays);
    }
    
    
}