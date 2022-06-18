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

    @PostConstruct
    private void init() {
        // Just to add huge dummy data in DB
        logger.info("In init!");
        List<PermanentEmp> permanentEmps = new ArrayList<>();
        List<TraineeEmp> traineeEmps = new ArrayList<>();
        for (long i = 1; i <= 1000; i++) {
            permanentEmps.add(new PermanentEmp(i, "permEmp"+i, "XYZ"+i%3));
            traineeEmps.add(new TraineeEmp(i, "traineeEmp"+i, "XYZ"+i%3));
        }
        permEmpGemSol.saveAll(permanentEmps);
        traineeGemSol.saveAll(traineeEmps);
    }

    @Async
    private CompletableFuture<List<PermanentEmp>> getPermanentEmployees() {
        List<PermanentEmp> permanentEmps = permEmpGemSol.findAll();
        return CompletableFuture.completedFuture(permanentEmps);
    }

    @Async
    private CompletableFuture<List<TraineeEmp>> getTraineeEmployees() {
        List<TraineeEmp> traineeEmps = traineeGemSol.findAll();
        return CompletableFuture.completedFuture(traineeEmps);
    }

    public void saveAllEmployees() {

        final AtomicReference<List<PermanentEmp>> permEmps = new AtomicReference<>();
        final AtomicReference<List<TraineeEmp>> traineeEmps = new AtomicReference<>();
        List<Employee> employees = new ArrayList<>();

        try {
            CompletableFuture.runAsync(() -> {
                logger.info("Current Thread: {}", Thread.currentThread().getName());
                permEmps.set(permEmpGemSol.findAll());
            }).thenRunAsync(() -> {
                logger.info("Current Thread: {}", Thread.currentThread().getName());
                traineeEmps.set(traineeGemSol.findAll());
            }).thenRunAsync(() -> {
                permEmps.get().forEach(emp -> {
                    Employee e = new Employee(emp.getEmpId(), emp.getEmpName(), emp.getEmpDept());
                    employees.add(e);
                });
                traineeEmps.get().forEach(emp -> {
                    Employee e = new Employee(emp.getEmpId(), emp.getEmpName(), emp.getEmpDept());
                    employees.add(e);
                });
                empGemSol.saveAll(employees);
                logger.info("Saved all employees into DB!");
            });
        } catch (Exception e) {
            logger.info("An error occurred: {}", e.getMessage());
        }
    }

}
