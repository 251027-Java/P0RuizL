package com.fantasy.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "week_score")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WeekScore {

    @EmbeddedId
    private WeekScoreId id;

    @Column(name = "score")
    private Double score;

    

    
}
