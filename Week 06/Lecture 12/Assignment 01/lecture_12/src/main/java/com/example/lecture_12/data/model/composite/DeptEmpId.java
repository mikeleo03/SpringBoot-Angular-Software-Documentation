package com.example.lecture_12.data.model.composite;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Data;

@Data
@Embeddable
@EqualsAndHashCode
public class DeptEmpId implements Serializable {
    private Integer empNo;
    private String deptNo;
}
