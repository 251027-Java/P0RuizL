package com.fantasy.Model;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "roster_user")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RosterUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto-increment in DB
    @Column(name = "roster_user_id")
    private Long rosterUserId;

    @Column(name = "roster_id")
    private Integer rosterId;

    @Column(name = "user_id_num")
    private Long userId;

    @Column(name = "league_id")
    private Long leagueId;

    @Column(name = "wins")
    private Integer wins;

    @Column(name = "ties")
    private Integer ties;

    @Column(name = "losses")
    private Integer losses;

    @Column(name = "fpts_decimal")
    private Integer fptsDecimal;

    @Column(name = "fpts_against_decimal")
    private Integer fptsAgainstDecimal;

    @Column(name = "fpts_against")
    private Integer fptsAgainst;

    @Column(name = "fpts")
    private Integer fpts;

    // constructor where everything except rosterUserId is passed in
    public RosterUser(Integer rosterId, Long userId, Long leagueId, Integer wins, Integer ties, Integer losses, Integer fptsDecimal, Integer fptsAgainstDecimal, Integer fptsAgainst, Integer fpts) {
        this.rosterId = rosterId;
        this.userId = userId;
        this.leagueId = leagueId;
        this.wins = wins;
        this.ties = ties;
        this.losses = losses;
        this.fptsDecimal = fptsDecimal;
        this.fptsAgainstDecimal = fptsAgainstDecimal;
        this.fptsAgainst = fptsAgainst;
        this.fpts = fpts;
    }

    @Override
    public String toString() {
        return "RosterUser{" +
                "rosterUserId=" + rosterUserId +
                ", rosterId=" + rosterId +
                ", userId=" + userId +
                ", leagueId=" + leagueId +
                ", wins=" + wins +
                ", ties=" + ties +
                ", losses=" + losses +
                ", fptsDecimal=" + fptsDecimal +
                ", fptsAgainstDecimal=" + fptsAgainstDecimal +
                ", fptsAgainst=" + fptsAgainst +
                ", fpts=" + fpts +
                '}';
    }
    
}
