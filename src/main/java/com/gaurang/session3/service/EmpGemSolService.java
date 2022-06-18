package com.gaurang.session3.service;

import com.gaurang.session3.entity.Employee;
import com.gaurang.session3.entity.PermanentEmp;
import com.gaurang.session3.entity.TraineeEmp;
import com.gaurang.session3.repository.EmpGemSol;
import com.gaurang.session3.repository.PermEmpGemSol;
import com.gaurang.session3.repository.TraineeGemSol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class EmpGemSolService {

    @Autowired
    private EmpGemSol empGemSol;

    @Autowired
    private PermEmpGemSol permEmpGemSol;

    @Autowired
    private TraineeGemSol traineeGemSol;

    Logger logger = LoggerFactory.getLogger(EmpGemSolService.class);

    final AtomicReference<List<PermanentEmp>> permEmps = new AtomicReference<>();
    final AtomicReference<List<TraineeEmp>> traineeEmps = new AtomicReference<>();

    @PostConstruct
    private void init() {
        // Just to add huge dummy data in DB
        logger.info("In init!");
        List<PermanentEmp> permanentEmps = new ArrayList<>();
        List<TraineeEmp> traineeEmps = new ArrayList<>();
        for (long i = 1; i <= 1000; i++) {
            PermanentEmp permanentEmp = new PermanentEmp();
            permanentEmp.setEmpName("permEmp"+i);
            permanentEmp.setEmpDept("XYZ"+((i%4)+1));
            permanentEmps.add(permanentEmp);

            TraineeEmp traineeEmp = new TraineeEmp();
            traineeEmp.setEmpName("traineeEmp"+i);
            traineeEmp.setEmpDept("XYZ"+((i%4)+1));
            traineeEmps.add(traineeEmp);
        }
        permEmpGemSol.saveAll(permanentEmps);
        traineeGemSol.saveAll(traineeEmps);
    }

    private CompletableFuture<Void> getPermEmployees() {
        return CompletableFuture.runAsync(() -> {
            logger.info("Current Thread: {}", Thread.currentThread().getName());
            permEmps.set(permEmpGemSol.findAll());
            logger.info("Leaving Thread: {}", Thread.currentThread().getName());
        }).toCompletableFuture();
    }

    private CompletableFuture<Void> getTraineeEmployees() {
        return CompletableFuture.runAsync(() -> {
            logger.info("Current Thread: {}", Thread.currentThread().getName());
            traineeEmps.set(traineeGemSol.findAll());
            logger.info("Leaving Thread: {}", Thread.currentThread().getName());
        }).toCompletableFuture();
    }

    public void saveAllEmployees() {
        List<Employee> employees = new ArrayList<>();

        try {
            CompletableFuture.allOf(getPermEmployees(), getTraineeEmployees()).thenRun(() -> {
                permEmps.get().forEach(emp -> {
                    Employee e = new Employee();
                    e.setEmpName(emp.getEmpName());
                    e.setEmpDept(emp.getEmpDept());
                    employees.add(e);
                });
                traineeEmps.get().forEach(emp -> {
                    Employee e = new Employee();
                    e.setEmpName(emp.getEmpName());
                    e.setEmpDept(emp.getEmpDept());
                    employees.add(e);
                });
                empGemSol.saveAll(employees);
            }).thenRun(() -> {
                logger.info("Saved all employees into DB!");
            });
        } catch (Exception e) {
            logger.info("An error occurred: {}", e.getMessage());
        }
    }

}
