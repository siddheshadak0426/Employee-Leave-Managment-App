package com.rak.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rak.entity.EmpLeaves;

@Repository
public interface LeaveRepository extends JpaRepository<EmpLeaves, Long> 
{
	Optional<EmpLeaves> findByLeaveId(Long leaveId);
	
	List<EmpLeaves> findByEmpEmpId(Long empEmpId); // findByEmpEmpId(Long empEmpId) 
}