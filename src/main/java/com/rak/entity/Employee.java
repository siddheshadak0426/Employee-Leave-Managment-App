package com.rak.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rak.enums.LeaveType;
import com.rak.enums.Role;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Entity
@Table(name="emps")
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@ToString(exclude = "leaveList")
@EqualsAndHashCode
@Builder
public class Employee 
{
    @Id
    private Long empId; // Should be given by admin
    private String firstName;
    private String middleName;
    private String lastName;
    private String email; // userName;
    private Long mobile;
    private String password; 
   
    @Enumerated(EnumType.STRING)
    private Role role=Role.ROLE_EMP; // default role is ROLE_EMP ==>> if you use Builder design pattern then its value is null
    
    private boolean isProfileCompleted; // default false
    
    @OneToMany(mappedBy = "emp", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EmpLeaves> leaveList = new ArrayList<>();

    
    
//    @OneToOne(mappedBy = "employee", cascade = CascadeType.ALL)
//    @JsonIgnore
//    private EmpLeaveBalance empLeaveBal;
    
    
	
    
	// =======================
	
//	public boolean isLeaveAvailable2(LeaveType leaveType, int leaveDays) 
//    {
//        switch (leaveType) 
//        {
//            case SICK_LEAVE:
//                if (sickLeaveBal >= leaveDays) 
//                {
//                    sickLeaveBal -= leaveDays;
//                    totalLeaveBal -= leaveDays;
//                    return true;
//                }
//                break;
//            case CASUAL_LEAVE:
//                if (casualLeaveBal >= leaveDays) 
//                {
//                    casualLeaveBal -= leaveDays;
//                    totalLeaveBal -= leaveDays;
//                    return true;
//                }
//                break;
//            case OTHERS:
//                if (otherLeaveBal >= leaveDays) 
//                {
//                    otherLeaveBal -= leaveDays;
//                    totalLeaveBal -= leaveDays;
//                    return true;
//                }
//                break;
//        }
//        
//        return false;  // Insufficient leave balance
//    }
    
	
    
    
    
	
	
    
}