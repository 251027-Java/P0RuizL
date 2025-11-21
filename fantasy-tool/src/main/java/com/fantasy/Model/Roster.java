package com.fantasy.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "roster")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Roster {
    @EmbeddedId
    private RosterIdObj id;

    private Boolean isStarting;

    private Integer points;

    
}
