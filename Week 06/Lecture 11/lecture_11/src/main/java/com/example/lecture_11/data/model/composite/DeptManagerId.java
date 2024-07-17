package com.example.lecture_11.data.model.composite;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DeptManagerId implements Serializable {
    private Integer empNo;
    private String deptNo;
}
