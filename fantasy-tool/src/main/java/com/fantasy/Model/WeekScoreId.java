package com.fantasy.Model;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WeekScoreId implements Serializable {
    @Column(name = "roster_user_id")
    private Long rosterUserId;
    @Column(name = "week_num")
    private Integer weekNum;


    @Override
    public String toString() {
        return "WeekScoreId{" +
                "rosterUserId='" + rosterUserId + '\'' +
                ", weekNum=" + weekNum +
                '}';
    }

    public boolean equals(Object o) {
        if (o instanceof WeekScoreId weekScoreId) {
            return this.rosterUserId.equals(weekScoreId.rosterUserId) 
                && this.weekNum.equals(weekScoreId.weekNum);
        }
        return false;
    }

    public int hashCode() {
        return Objects.hash(this.rosterUserId, this.weekNum);
    }
}