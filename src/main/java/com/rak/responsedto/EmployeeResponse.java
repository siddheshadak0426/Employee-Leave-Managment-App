package com.rak.responsedto;

import com.rak.enums.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class EmployeeResponse 
{
    private Long empId;
    private String firstName;
    private String middleName;
    private String lastName;
    private String email;
    private Long mobile;
    private Role role;
    private boolean isProfileCompleted;
    
    
}