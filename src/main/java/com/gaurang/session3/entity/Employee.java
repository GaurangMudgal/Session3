package com.gaurang.session3.entity;

import lombok.*;
import javax.persistence.*;

@Entity
@Table(name = "GEM_SOL_EMP")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Employee {

    @Id
    @Column(name = "EMP_ID")
    @GeneratedValue
    private long empId;

    @Column(name = "EMP_NAME")
    private String empName;

    @Column(name = "EMP_DEPT")
    private String empDept;

}
