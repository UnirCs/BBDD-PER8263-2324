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
        private String title;
        private Date fromDate;

        @Override
        public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                TitleId that = (TitleId) o;
                return empNo.equals(that.empNo) &&
                        title.equals(that.title) &&
                        fromDate.equals(that.fromDate);
        }

        @Override
        public int hashCode() {
                return Objects.hash(empNo, title, fromDate);
        }
}
