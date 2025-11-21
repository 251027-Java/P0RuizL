package com.fantasy.Service;

import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.Logger.GlobalLogger;
import com.fantasy.Request.SleeperRequestHandler;
import com.fantasy.Request.RequestModels.LeagueResponse;
import com.fantasy.Request.RequestModels.MatchupResponse;
import com.fantasy.Request.RequestModels.NFLStateResponse;
import com.fantasy.Request.RequestModels.PlayerResponse;
import com.fantasy.Request.RequestModels.RosterUserResponse;
import com.fantasy.Request.RequestModels.UserResponse;

import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

/**
 * RequestFormatter is a class used to format the responses from sleeper into
 * POJO
 */
public class RequestFormatter {

    private static final ObjectMapper om = new ObjectMapper();

    public static List<PlayerResponse> getPlayers() {
        try {
            HttpResponse<String> response = SleeperRequestHandler.getPlayers();
            GlobalLogger.debug("Players retrieved from sleeper");
            if (response.statusCode() == 200) {

                Map<String, PlayerResponse> map = om.readValue(response.body(),
                        new TypeReference<Map<String, PlayerResponse>>() {
                        });
                List<PlayerResponse> resp = new ArrayList<>(map.values());

                return resp;
            }
        } catch (Exception e) {
            GlobalLogger.error("Could not get players", e);
        }
        System.out.println("No users found");
        return List.of();
    }

    /**
     * Get leagues for the current year from sleeper based on userId
     * 
     * @param userId the user_id of leagues to look for
     * @return list of leagues
     */
    public static List<LeagueResponse> getLeaguesFromUserId(long userId) {
        // get the current year from time clock
        int year = LocalDate.now().getYear();

        try {
            HttpResponse<String> response = SleeperRequestHandler.getLeaguesFromUserIDAndSeason(userId, year);
            if (response.statusCode() == 200) {
                List<LeagueResponse> resp = om.readValue(response.body(), new TypeReference<List<LeagueResponse>>() {
                });
                return resp;
            }
        } catch (Exception e) {
            GlobalLogger.error(String.format("Could not get leagues from user_id '%s'", userId), e);
        }
        System.out.println("No leagues found");
        return List.of();
    }

    /**
     * Get users from a sleeper league based on leagueId
     * 
     * @param leagueId the id of the league
     * @return a list of users
     */
    public static List<UserResponse> getUsersFromLeague(long leagueId) {
        try {
            HttpResponse<String> response = SleeperRequestHandler.getUsersFromLeague(leagueId);
            if (response.statusCode() == 200) {
                List<UserResponse> resp = om.readValue(response.body(), new TypeReference<List<UserResponse>>() {
                });
                return resp;
            }
        } catch (Exception e) {
            GlobalLogger.error(String.format("Could not get users from league_id '%s'", leagueId), e);
        }
        System.out.println("No users found");
        return List.of();
    }

    public static List<RosterUserResponse> getRostersFromLeagueId(long leagueId) {
        try {
            HttpResponse<String> response = SleeperRequestHandler.getRostersFromLeague(leagueId);
            if (response.statusCode() == 200) {
                List<RosterUserResponse> resp = om.readValue(response.body(),
                        new TypeReference<List<RosterUserResponse>>() {
                        });
                return resp;
            }
        } catch (Exception e) {
            GlobalLogger.error(String.format("Could not get rosters from league_id '%s'", leagueId), e);
        }
        System.out.println("No rosters found");
        return List.of();
    }

    public static List<MatchupResponse> getMatchupsFromLeagueIdAndWeek(long leagueId, int weekNum) {
        try {
            HttpResponse<String> response = SleeperRequestHandler.getMatchupsFromLeagueIdAndWeek(leagueId, weekNum);
            if (response.statusCode() == 200) {
                List<MatchupResponse> resp = om.readValue(
                        response.body(),
                        new TypeReference<List<MatchupResponse>>() {});
                return resp;
            }
        } catch (Exception e) {
            GlobalLogger.error(
                    String.format("Could not get matchups from league_id '%s' for week '%d'",
                            leagueId,
                            weekNum),
                    e);
        }
        System.out.println("No matchups found");
        return List.of();
    }

    public static NFLStateResponse getNFLState() {
        try {
            HttpResponse<String> response = SleeperRequestHandler.getNFLState();
            if (response.statusCode() == 200) {
                NFLStateResponse resp = om.readValue(response.body(), NFLStateResponse.class);
                return resp;
            }
        } catch (Exception e) {
            GlobalLogger.error("Could not get nfl state", e);
        }
        System.out.println("No nfl state found");
        return null;
    }
}
