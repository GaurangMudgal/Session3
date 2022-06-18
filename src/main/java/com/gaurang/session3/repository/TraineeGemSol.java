package com.gaurang.session3.repository;

import com.gaurang.session3.entity.TraineeEmp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TraineeGemSol extends JpaRepository<TraineeEmp, Long> {

}
