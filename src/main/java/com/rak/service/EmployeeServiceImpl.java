package com.rak.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.rak.entity.EmpLeaveBalance;
import com.rak.entity.EmpLeaves;
import com.rak.entity.Employee;
import com.rak.enums.LeaveStatus;
import com.rak.enums.LeaveType;
import com.rak.exception.EmailIdAlreadyExistException;
import com.rak.exception.EmpNotFoundException;
import com.rak.exception.InsufficientLeaveException;
import com.rak.exception.StartDateAfterEndDateException;
import com.rak.repository.EmpLeaveBalRepository;
import com.rak.repository.EmployeeRepository;
import com.rak.repository.LeaveRepository;
import com.rak.requestdto.EmpRegRequest;
import com.rak.requestdto.LeaveRequest;
import com.rak.responsedto.EmployeeResponse;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService 
{    
    @Autowired
    private EmployeeRepository employeeRepository;
    
    @Autowired
    private LeaveRepository leaveRepository;
    
    @Autowired
    private EmpLeaveBalRepository empLeaveBalRepository;
    
    private final static Logger logger=LoggerFactory.getLogger(EmployeeServiceImpl.class);
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void registerEmployee(EmpRegRequest empRegRequest) throws EmpNotFoundException, EmailIdAlreadyExistException 
    {
    	Optional<Employee> empOptional=employeeRepository.findById(empRegRequest.getEmpId());
    	if(empOptional.isEmpty())
    	{
    		logger.error("empId: {} is incorrect first get empId from admin...!!!",empRegRequest.getEmpId());
    		throw new EmpNotFoundException("first get empId from admin...!!!");
    	}
    	
    	Optional<Employee> emp2=employeeRepository.findByEmail(empRegRequest.getEmail());
    	if(emp2.isPresent())
    	{
    		logger.error("email: {} already exists...!!!", empRegRequest.getEmail());
    		throw new EmailIdAlreadyExistException("email id already exist...!!!");
    	}
    	
    	Employee emp=empOptional.get();
    	
    	emp.setEmpId(empRegRequest.getEmpId());
    	emp.setFirstName(empRegRequest.getFirstName());
    	emp.setMiddleName(empRegRequest.getMiddleName());
    	emp.setLastName(empRegRequest.getLastName());
    	emp.setEmail(empRegRequest.getEmail());
    	emp.setPassword(passwordEncoder.encode(empRegRequest.getPassword()));
    	// emp.setPassword(empRegRequest.getPassword());
    	emp.setMobile(empRegRequest.getMobile());
    	emp.setProfileCompleted(true);
    	
    	// save the changes
    	employeeRepository.save(emp);
    	
    }

    @Override
    public void changePassword(Long empId, String newPassword) throws EmpNotFoundException 
    {
    	Employee emp=employeeRepository.findById(empId).orElseThrow( ()-> new EmpNotFoundException("emp not found...!!!") );
    	emp.setPassword(passwordEncoder.encode(newPassword));
    	employeeRepository.save(emp);
    }

    @Override
    public List<EmpLeaves> getAllAppliedLeaves(Long empId) throws EmpNotFoundException 
    {
        Optional<Employee> empOptional=employeeRepository.findById(empId);
    	if(empOptional.isEmpty())
    		throw new EmpNotFoundException("Employee not found...!!!");
    	
    	return leaveRepository.findByEmpEmpId(empId);
    }

    @Override
    public LeaveStatus getLeaveStatus(Long leaveId) 
    {
    	EmpLeaves empLeave=leaveRepository.findByLeaveId(leaveId).get();
    	return empLeave.getLeaveStatus();
    }
    
    
    @Override
	public List<EmployeeResponse> getAllEmps() 
    {
		// TODO Auto-generated method stub
		return null;
	}
    
    
    // Reset total leaves for all employees on January 1st every year
    @Scheduled(cron = "0 0 0 1 1 *")   // At midnight (00:00) on January 1st of every year => Each value in the cron expression represents a specific time unit (seconds, minutes, hours, day of the month, month, , day of the week) => * means any day of the week
    public void resetEmployeeLeaves() 
    {
        List<Employee> employees = employeeRepository.findAll();
        for (Employee employee : employees) 
        {
            // employee.setTotalLeaves(12); // Reset to 12 leaves
        }
        
        employeeRepository.saveAll(employees);
        System.out.println("Employee leaves reset to 12 for the new year.");
    }

	@Override
	public void applyForLeave(Long empId, LeaveRequest leaveRequest) throws EmpNotFoundException, StartDateAfterEndDateException, InsufficientLeaveException 
	{
		Employee emp=employeeRepository.findById(empId).orElseThrow( ()-> new EmpNotFoundException("emp not found...!!!") );
    	
		LeaveType leaveType=leaveRequest.getLeaveType(); // SICK_LEAVE
		LocalDate startDate=leaveRequest.getStartDate();
		LocalDate endDate=leaveRequest.getEndDate();
		int leaveDays=(int) ChronoUnit.DAYS.between(startDate, endDate);
		
		if(!startDate.isBefore(endDate))
			throw new StartDateAfterEndDateException("StartDate must be befor endDate...!!!");
		
		if(!isLeaveAvailable(emp, leaveType, leaveDays))
			throw new InsufficientLeaveException("leave balalance is no sufficient...!!!");
		
		// so far so good
		EmpLeaves leave=new EmpLeaves();
		leave.setLeaveType(leaveType);
		leave.setStartDate(startDate);
		leave.setEndDate(endDate);
		leave.setReason(leaveRequest.getReason());
		leave.setEmp(emp); 
		
		leaveRepository.save(leave);
	}
	
	
	// =============== Utility Method ==========================
	
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

	
	
}