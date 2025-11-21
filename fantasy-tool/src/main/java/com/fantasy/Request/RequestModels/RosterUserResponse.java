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
public class RosterUserResponse {
    @JsonProperty("owner_id")
    private long userId;

    @JsonProperty("league_id")
    private long leagueId;

    @JsonProperty("roster_id")
    private int rosterId;

    @JsonProperty("players")
    private List<String> roster;

    @JsonProperty("starters")
    private List<String> starters;

    @JsonProperty("settings")
    private Settings settings;

}