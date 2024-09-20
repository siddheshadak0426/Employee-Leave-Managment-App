package com.rak.requestdto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class EmpRegRequest
{
	private Long empId;
	private String firstName;
    private String middleName;
    private String lastName;
    private String email;
    private String password;
    private Long mobile;
}
