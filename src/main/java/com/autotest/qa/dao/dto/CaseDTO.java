package com.autotest.qa.dao.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class CaseDTO implements Serializable {

    private Long id;
    private String caseName;
    private String caseDescription;
    private String caseProperty;
    private Integer caseType;
    private String caseParam;
    private Integer case_run;

}
