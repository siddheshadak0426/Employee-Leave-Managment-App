package com.rak;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EmployeeLeaveManagmentAppApplication 
{
	
	public static void main(String[] args) 
	{
		SpringApplication.run(EmployeeLeaveManagmentAppApplication.class, args);
		
		
//		Employee emp=new Employee();
//		System.out.println("emp: "+emp);
//		
//		Employee emp2=Employee.builder().firstName("balaji").lastName("Gapat").build();
//		System.out.println("emp2: "+emp2);
		
	}
}
