package com.rak.responsedto;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JwtResponse 
{
	private Long empId;
	private String email;
	private String token;
	private String tokenType="Bearer";
	private List<String> roleList;
}
