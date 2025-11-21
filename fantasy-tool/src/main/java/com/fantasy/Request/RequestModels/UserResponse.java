package com.fantasy.Request.RequestModels;

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
public class UserResponse {
    
    @JsonProperty("user_id")
    private long userId;

    @JsonProperty("display_name")
    private String displayName;
    // TODO: update this to have team name

}