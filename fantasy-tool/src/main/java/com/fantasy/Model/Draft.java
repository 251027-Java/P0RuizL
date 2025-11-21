package com.fantasy.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "draft")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Draft {
    
    @Id
    @Column(name = "draft_id")
    private long draftId;

    @Column(name = "league_id")
    private long leagueId;


    @Override
    public String toString() {
        return "Draft{" +
                "draftId=" + draftId +
                ", leagueId=" + leagueId +
                '}';
    }
}
