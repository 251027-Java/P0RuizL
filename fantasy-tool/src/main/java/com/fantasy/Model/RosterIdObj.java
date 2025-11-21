package com.fantasy.Model;

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
public class RosterIdObj {
    
    @Column(name = "roster_user_id")
    private long rosterUserId;

    @Column(name = "player_id")
    private String playerId;

    @Column(name = "week_num")
    private int weekNum;


    public String toString() {
        return "RosterIdObj{" +
                "rosterUserId=" + rosterUserId +
                ", playerId='" + playerId + '\'' +
                ", weekNum=" + weekNum +
                '}';
    }


}
