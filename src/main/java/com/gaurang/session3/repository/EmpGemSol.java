package com.gaurang.session3.repository;

import com.gaurang.session3.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmpGemSol extends JpaRepository<Employee, Long> {

}
