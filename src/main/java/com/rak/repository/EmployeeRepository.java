package com.rak.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rak.entity.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> 
{
	public Optional<Employee> findByEmail(String email);
	
	boolean existsByEmail(String email);
    boolean existsByEmpId(Long empId);
}