package com.gaurang.session3.repository;

import com.gaurang.session3.entity.PermanentEmp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermEmpGemSol extends JpaRepository<PermanentEmp, Long> {

}
