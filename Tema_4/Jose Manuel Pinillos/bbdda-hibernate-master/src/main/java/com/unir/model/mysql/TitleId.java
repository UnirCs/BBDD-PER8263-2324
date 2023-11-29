package com.unir.model.mysql;

import lombok.*;

import java.io.Serializable;
import java.sql.Date;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TitleId implements Serializable {

    private Integer empNo;
    private String title;
    private Date fromDate;
}
