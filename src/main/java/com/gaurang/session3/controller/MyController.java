package com.gaurang.session3.controller;


import com.gaurang.session3.service.EmpGemSolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyController {

    @Autowired
    private EmpGemSolService empGemSolService;

    @PostMapping(value = "/saveAllEmployee")
    private ResponseEntity<String> saveAllEmployee() {
        empGemSolService.saveAllEmployees();
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
