package com.fantasy.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name= "player_position")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlayerPosition {

    @EmbeddedId
    private PlayerPositionId id;


    @Override
    public boolean equals(Object o ) {
        if (o instanceof PlayerPosition playerPosition) {
            return this.id.equals(playerPosition.id);
        }
        return false;
    }


    
}
