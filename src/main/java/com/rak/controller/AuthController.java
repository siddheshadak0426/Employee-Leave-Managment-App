package com.rak.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rak.entity.LogninRequest;
import com.rak.exception.EmailIdAlreadyExistException;
import com.rak.exception.EmpIdNotExistException;
import com.rak.requestdto.EmployeeRegRequest;
import com.rak.responsedto.EmployeeResponse;
import com.rak.responsedto.JwtResponse;
import com.rak.security.jwt.JwtUtils;
import com.rak.security.user.SecurityUser;
import com.rak.service.EmployeeService;

import jakarta.validation.Valid;

// Step: 5
@RestController
@RequestMapping("/auth")
public class AuthController 
{
	@Autowired
	private EmployeeService employeeService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JwtUtils jwtUtils;
	
	@PostMapping("/signup") // http://localhost:8080/auth/signup
	public ResponseEntity<EmployeeResponse> registerUser(@RequestBody EmployeeRegRequest employeeRegRequest)
	{
		try 
		{
			EmployeeResponse empDto=employeeService.registerEmployee(employeeRegRequest);
			return ResponseEntity.ok(empDto);
		} 
		catch (EmailIdAlreadyExistException e) 
		{
			e.printStackTrace();
		} 
		catch (EmpIdNotExistException e) 
		{
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	@PostMapping("/login") // http://localhost:8080/auth/login
	public ResponseEntity<JwtResponse> authenticateUser(@RequestBody @Valid LogninRequest request)
	{
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		String jwt=jwtUtils.generateJwtTokenForUser(authentication);
		SecurityUser user=(SecurityUser) authentication.getPrincipal();
		
		List<String> roleList=user.getAuthorities().stream().map(authority->authority.getAuthority()).toList();
		
		JwtResponse response=JwtResponse.builder().email(user.getUsername()).role(roleList.get(0)).token(jwt).build();
		
		return ResponseEntity.ok(response);
	}
	
	
}
