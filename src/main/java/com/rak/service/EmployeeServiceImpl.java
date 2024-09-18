package com.rak.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.rak.entity.EmpLeaveBalance;
import com.rak.entity.EmpLeaves;
import com.rak.entity.Employee;
import com.rak.enums.LeaveType;
import com.rak.exception.EmailIdAlreadyExistException;
import com.rak.exception.EmpIdNotExistException;
import com.rak.exception.EmpNotFoundException;
import com.rak.exception.InsufficientLeaveException;
import com.rak.exception.StartDateAfterEndDateException;
import com.rak.repository.EmpLeaveBalRepository;
import com.rak.repository.EmployeeRepository;
import com.rak.repository.LeaveRepository;
import com.rak.requestdto.EmployeeRegRequest;
import com.rak.requestdto.LeaveRequest;
import com.rak.requestdto.UpdateProfileRequest;
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
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public EmployeeResponse updateEmployeeProfile(Long empId, UpdateProfileRequest updateProfileRequest) throws EmpNotFoundException, EmailIdAlreadyExistException 
    {
    	Optional<Employee> empOptional=employeeRepository.findById(empId);
    	if(empOptional.isEmpty())
    		throw new EmpNotFoundException("Employee not found...!!!");
    	
    	Optional<Employee> emp2=employeeRepository.findByEmail(updateProfileRequest.getEmail());
    	if(emp2.isPresent())
    		throw new EmailIdAlreadyExistException("email id already exist...!!!");
    	
    	// update object fields
    	Employee emp=empOptional.get();
    	emp.setMiddleName(updateProfileRequest.getMiddleName());
    	emp.setEmail(updateProfileRequest.getEmail());
    	emp.setPassword(passwordEncoder.encode(updateProfileRequest.getPassword()));
    	emp.setMobile(updateProfileRequest.getMobile());
    	emp.setProfileCompleted(true);
    	
    	// save the changes
    	employeeRepository.save(emp);
    	
    	// build return object
    	EmployeeResponse empDto=EmployeeResponse
								.builder()
								.empId(emp.getEmpId())
								.firstName(emp.getFirstName())
								.middleName(emp.getMiddleName())
								.lastName(emp.getLastName())
								.email(emp.getEmail())
								.mobile(emp.getMobile())
								.build();
		
       return empDto;
    }

    @Override
    public void changePassword(Long empId, String newPassword) throws EmpNotFoundException 
    {
    	Employee emp=employeeRepository.findById(empId).orElseThrow( ()-> new EmpNotFoundException("emp not found...!!!") );
    	emp.setPassword(newPassword);
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
    public EmpLeaves getLeaveStatus(Long leaveId) 
    {
    	return leaveRepository.findByLeaveId(leaveId).get();
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
	
	// -------------------------
	
	@Override
	public EmployeeResponse registerEmployee(EmployeeRegRequest employeeRegRequest) throws EmailIdAlreadyExistException, EmpIdNotExistException 
	{
		// checks for validation
		if(employeeRepository.existsByEmail(employeeRegRequest.getEmail())) 
			throw new EmailIdAlreadyExistException(employeeRegRequest.getEmail()+" => already exists...!!!");
		
		// if(!employeeRepository.existByEmpId(employeeRegRequest.getEmpId()))
			// throw new EmpIdNotExistException(employeeRegRequest.getEmpId()+" => this empId not given by Admin...!!!");
		
//		Optional<Employee> empOptional=employeeRepository.findById(employeeRegRequest.getEmpId());
//    	if(empOptional.isEmpty())
//    		throw new EmpIdNotExistException(employeeRegRequest.getEmpId()+" => this empId not given by Admin...!!!");
		
// 		Employee emp=empOptional.get();
		
		
		
		Employee emp=new Employee();
    	// update employee object fields

		emp.setMiddleName(employeeRegRequest.getMiddleName());
		emp.setEmail(employeeRegRequest.getEmail());
		emp.setPassword(passwordEncoder.encode(employeeRegRequest.getPassword()));
		emp.setMobile(employeeRegRequest.getMobile());
		emp.setProfileCompleted(true);
		
		// update in database
		employeeRepository.save(emp);
		
		// build return response object
    	EmployeeResponse empDto=EmployeeResponse
								.builder()
								.empId(emp.getEmpId())
								.firstName(emp.getFirstName())
								.middleName(emp.getMiddleName())
								.lastName(emp.getLastName())
								.email(emp.getEmail())
								.mobile(emp.getMobile())
								.build();
		
		return empDto;
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