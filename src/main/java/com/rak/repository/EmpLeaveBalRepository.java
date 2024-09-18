package com.rak.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rak.entity.EmpLeaveBalance;

@Repository
public interface EmpLeaveBalRepository extends JpaRepository<EmpLeaveBalance, Long>
{
	public Optional<EmpLeaveBalance> findByEmployeeEmpId(Long employeeEmpId);
}
