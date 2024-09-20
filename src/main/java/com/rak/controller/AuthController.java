package com.rak.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rak.exception.EmailIdAlreadyExistException;
import com.rak.exception.EmpNotFoundException;
import com.rak.requestdto.EmpRegRequest;
import com.rak.requestdto.LoginRequest;
import com.rak.responsedto.JwtResponse;
import com.rak.security.jwt.JwtUtils;
import com.rak.security.user.SecurityUser;
import com.rak.service.EmployeeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController 
{
	// @Autowired
    private final EmployeeService employeeService;
	
	private final AuthenticationManager authenticationManager;
	
	private final JwtUtils jwtUtils;
	
	private static final Logger logger=LoggerFactory.getLogger(AuthController.class);
	
    @PostMapping("/signup") // http://localhost:8080/auth/signup
    public ResponseEntity<String> empRegistration(@RequestBody EmpRegRequest empRegRequest) throws EmpNotFoundException, EmailIdAlreadyExistException 
    {
    	employeeService.registerEmployee(empRegRequest);
    	return new ResponseEntity<>("Registration done successfully...!!!", HttpStatus.OK);
    }
    
    
    @PostMapping("/login") // http://localhost:8080/auth/login
    public ResponseEntity<JwtResponse> EmpLogin(@RequestBody LoginRequest loginRequest)
    {
    	Authentication authentication=authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
    	logger.info("authentication: {}", authentication);
    	
    	SecurityContextHolder.getContext().setAuthentication(authentication);
    	String jwt=jwtUtils.generateJwtTokenForUser(authentication);
    	
    	SecurityUser user= (SecurityUser) authentication.getPrincipal();
    	
    	List<String> roleList=user.getAuthorities().stream().map(authority->authority.getAuthority()).collect(Collectors.toList());
    	
    	JwtResponse response=JwtResponse
    							.builder()
    							.empId(user.getEmpId())
    							.email(user.getUsername())
    							.token(jwt)
    							.tokenType("Bearer")
    							.roleList(roleList)
    							.build();
    	
    	 return ResponseEntity.ok(response);
    }
    
}
