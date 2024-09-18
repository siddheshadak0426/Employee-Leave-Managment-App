package com.rak.requestdto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class EmployeeRegRequest 
{
	private Long empId; 
	private String middleName;
    private String email;
    private String password;
    private Long mobile;
}
