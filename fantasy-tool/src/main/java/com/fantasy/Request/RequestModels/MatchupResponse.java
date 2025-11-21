package com.fantasy.Request.RequestModels;

import java.util.List;
import java.util.Map;

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
public class MatchupResponse {
    
    @JsonProperty("matchup_id")
    private Integer matchupId;

    @JsonProperty("roster_id")
    private Integer rosterId;

    @JsonProperty("starters")
    private List<String> starters;

    @JsonProperty("players_points")
    private Map<String, Double> playersPoints;

    @JsonProperty("points")
    private Double points;

}