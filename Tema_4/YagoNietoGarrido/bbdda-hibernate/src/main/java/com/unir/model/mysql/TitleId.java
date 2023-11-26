package com.unir.model.mysql;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Date;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TitleId implements Serializable {

    private Integer empNo;
    private Date fromDate;
    private String title;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TitleId that = (TitleId) o;
        return Objects.equals(empNo, that.empNo) &&
                Objects.equals(fromDate, that.fromDate)&&
                Objects.equals(title, that.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(empNo, fromDate, title);
    }
}
