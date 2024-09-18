package com.rak.responsedto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeResponse 
{
    private Long empId;
    private String firstName;
    private String middleName;
    private String lastName;
    private String email;
    private Long mobile;
    
}