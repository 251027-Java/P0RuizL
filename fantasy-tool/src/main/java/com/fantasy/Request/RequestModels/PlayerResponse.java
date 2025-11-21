package com.fantasy.Request.RequestModels;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
@NoArgsConstructor
@AllArgsConstructor
public class PlayerResponse {
    @JsonProperty("player_id")
    private String playerId;

    @JsonProperty("fantasy_positions")
    private List<String> fantasyPositions;

    @JsonProperty("team")
    private String team;

    @JsonProperty("full_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("roto_world_id")
    private Integer rotoworldId;

    @JsonProperty("stats_id")
    private String statsId;

    @JsonProperty("fantasy_data_id")
    private Integer fantasyDataId;

    public String getFullName() {
        return firstName + " " + lastName;
    }

}