package com.unir.model.mysql;

import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SalariesId implements Serializable {

    private Integer empNo;
    private Date fromDate;
}
