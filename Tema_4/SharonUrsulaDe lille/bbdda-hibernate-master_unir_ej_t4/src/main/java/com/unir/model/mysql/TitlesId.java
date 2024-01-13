package com.unir.model.mysql;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TitlesId implements Serializable {

    private Integer empNo;
    private Date fromDate;
    private String title;
}
