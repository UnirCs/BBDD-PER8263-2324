package com.unir.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
public class OracleJobs {

    private String jobId;

    @Setter
    private int maxSalary;

    @Setter
    private int minSalary;

    @Setter
    private String jobTitle;
}
