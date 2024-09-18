package com.rak.entity;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rak.enums.LeaveStatus;
import com.rak.enums.LeaveType;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="emp_leaves")
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class EmpLeaves 
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long leaveId;

    @Enumerated(EnumType.STRING)
    private LeaveType leaveType;  // SICK_LEAVE

    private LocalDate startDate; // 2024-09-16
    private LocalDate endDate;  // 2024-09-19

    @Enumerated(EnumType.STRING)
    private LeaveStatus leaveStatus = LeaveStatus.PENDING;

    private String reason;  

    @ManyToOne(cascade = CascadeType.ALL)
    @JsonIgnore
    @JoinColumn(name = "emp_id") // FK=emp_id => query method name findByEmpEmpId(Long empEmpId)
    private Employee emp;
}