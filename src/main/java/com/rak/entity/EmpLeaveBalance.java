package com.rak.entity;

import com.rak.enums.LeaveType;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name="emp_leave_bal")
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class EmpLeaveBalance 
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long empLeaveBalanceId;
	private int sickLeaveBal=10;
    private int casualLeaveBal=10;
    private int otherLeaveBal=15;
    private int totalLeaveBal=35;
    
	@OneToOne
	@JoinColumn(name="emp_id") // Unique_key=emp_id => query method name findByEmployeeEmpId(Long employeeEmpId)
    private Employee employee;
    
    
    // ====================
    
    public boolean isLeaveAvailable(LeaveType leaveType, int leaveDays) 
    {
        switch (leaveType) 
        {
            case SICK_LEAVE:
                if (sickLeaveBal >= leaveDays) 
                {
                    sickLeaveBal -= leaveDays;
                    totalLeaveBal -= leaveDays;
                    return true;
                }
                break;
            case CASUAL_LEAVE:
                if (casualLeaveBal >= leaveDays) 
                {
                    casualLeaveBal -= leaveDays;
                    totalLeaveBal -= leaveDays;
                    return true;
                }
                break;
            case OTHERS:
                if (otherLeaveBal >= leaveDays) 
                {
                    otherLeaveBal -= leaveDays;
                    totalLeaveBal -= leaveDays;
                    return true;
                }
                break;
        }
        
        return false;  // Insufficient leave balance
    }
}
