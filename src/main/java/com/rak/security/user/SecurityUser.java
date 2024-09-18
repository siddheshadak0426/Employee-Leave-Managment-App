package com.rak.security.user;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.rak.entity.Employee;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

// Step: 2.1
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class SecurityUser implements UserDetails 
{
	private Employee employee;
	
	public SecurityUser(Employee employee) 
	{
		this.employee=employee;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() 
	{
		// Role empRole =employee.getRole();
		// GrantedAuthority authorities=new SimpleGrantedAuthority(empRole.toString());
		// List<GrantedAuthority> roleList=new ArrayList<>();
		// roleList.add(authorities);
		
		// OR
		List<GrantedAuthority> roleList=new ArrayList<>();
		roleList.add(new SimpleGrantedAuthority(employee.getRole().toString()));
		return roleList;
	}

	@Override
	public String getPassword() 
	{
		return employee.getPassword();
	}

	@Override
	public String getUsername() 
	{
		return employee.getEmail(); // here email is userName
	}

}
