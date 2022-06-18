package com.gaurang.session3.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "GEM_SOL_PERM_EMP")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PermanentEmp {

    @Id
    @Column(name = "EMP_ID")
    @GeneratedValue
    private long empId;

    @Column(name = "EMP_NAME")
    private String empName;

    @Column(name = "EMP_DEPT")
    private String empDept;

}
