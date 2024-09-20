package com.rak.requestdto;

import lombok.Data;

@Data
public class ChangePasswordRequest
{
	private Long empId;
	private String newPassword;
}
