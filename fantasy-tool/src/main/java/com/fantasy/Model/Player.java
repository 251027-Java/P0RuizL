package com.fantasy.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "player")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Player {
    
    @Id
    @Column(name = "player_id")
    private String playerId;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "team")
    private String team;

    @Column(name = "fantasy_data_id")
    private Integer fantasyDataId;

    @Column(name = "stats_id")
    private String statsId;

    @Column(name = "roto_world_id")
    private Integer rotoworldId;


}
