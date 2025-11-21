package com.fantasy.Model;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlayerPositionId implements Serializable{

    @Column(name = "player_id") 
    private String playerId;

    @Column(name = "position")
    private String position;

    @Override
    public String toString() {
        return String.format("PlayerPositionId{playerId=%s, position=%s}", playerId, position);
    }

    @Override
    public boolean equals(Object o ) {
        if (o instanceof PlayerPositionId playerPositionId) {
            return this.playerId.equals(playerPositionId.playerId) 
                && this.position.equals(playerPositionId.position);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.playerId, this.position);
    }
    
}
