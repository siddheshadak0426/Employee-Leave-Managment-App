package com.rak.security.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.rak.entity.Employee;
import com.rak.repository.EmployeeRepository;


// Step: 2.2
@Service
public class SecurityUserDetailsService implements UserDetailsService
{
	@Autowired
	private EmployeeRepository employeeRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException 
	{
		Employee emp=employeeRepository.findByEmail(username).orElseThrow( ()->new UsernameNotFoundException("User not found...!!!") );
		
		SecurityUser securityUser=new SecurityUser();
		securityUser.setEmployee(emp);
		
		System.out.println("securityUser username => "+securityUser.getUsername());
		System.out.println("securityUser password => "+securityUser.getPassword());
		System.out.println("securityUser authorities => "+securityUser.getAuthorities());
		
		return securityUser;
	}

}
