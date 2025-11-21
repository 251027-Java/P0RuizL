package com.fantasy.Service;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.net.http.HttpResponse;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.xml.crypto.Data;

import com.Logger.GlobalLogger;
import com.fantasy.Exception.HttpConnectionException;
import com.fantasy.Model.*;
import com.fantasy.Repository.*;
import com.fantasy.Request.SleeperRequestHandler;
import com.fantasy.Request.RequestModels.*;
import com.fantasy.Stats.*;

import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

public class FantasyToolService implements Closeable {
    private final IRepository repo;
    private final Scanner scan;
    private static final ObjectMapper om = new ObjectMapper();
    private DatabaseFormatter formatter;

    public FantasyToolService(IRepository repo, Scanner scan) {
        this.repo = repo;
        this.scan = scan;
        this.formatter = new DatabaseFormatter(repo);
    }

    public void start() {
        clearScreen();
        System.out.println("Welcome to Fantasy Tool!");
        // get nfl state
        NFLStateResponse nflState = RequestFormatter.getNFLState();
        GlobalLogger.debug("NFL State gotten from sleeper: "+ nflState.toString());
        int season = Integer.parseInt(nflState.getSeason());
        int currWeek = Integer.parseInt(nflState.getDisplayWeek());
        GlobalLogger.debug("Current season: " + season);
        GlobalLogger.debug("Current week: " + currWeek);
        
        
        // get userId
        long userId = this.getUserId();
        System.out.println("Getting leagues...");
        GlobalLogger.debug("UserId: " + userId);
        // clearScreen();
        // get leagues based on userId (assumes the current year)
        List<LeagueResponse> leagues = RequestFormatter.getLeaguesFromUserId(userId); 
        GlobalLogger.debug("Leagues gotten from sleeper: "+ leagues.toString());
        // process leagues into db format and insert, returning list of league ids
        List<Long> leagueIds = this.formatter.processLeagueInfo(leagues);

        // query database for leagues with league ids
        List<League> dbLeagues = this.repo.getLeaguesById(leagueIds);
        GlobalLogger.debug("Leagues gotten from database: "+ dbLeagues.toString());
        while (true) {
            // ask user to choose a league from options
            clearScreen();
            long chosenLeagueId = this.chooseLeague(dbLeagues);
            System.out.println("Getting stats...");
            League chosenLeague = this.repo.getLeagueById(chosenLeagueId);
            GlobalLogger.debug("League chosen: " + chosenLeague.getLeagueName());
            

            // add users from chosen league into database (if not there already)
            List<UserResponse> users = RequestFormatter.getUsersFromLeague(chosenLeagueId);

            // process users into db format
            this.formatter.processUsers(users);


            // check if players have been updated recently
            try {
                Boolean updatedRecently = this.playersUpdatedRecently();
                if (!updatedRecently) {
                    // get players from sleeper
                    System.out.println("Players have not been updated recently, updating...");
                    List<PlayerResponse> players = RequestFormatter.getPlayers();


                    // process players into db format and insert
                    this.formatter.processPlayers(players);
                    

                    // update players last updated
                    this.updateLastPlayerUpdate();
                    System.out.println("Players info updated");
                    GlobalLogger.debug("Players info updated");
                }
            } catch (DateTimeParseException e) {
                GlobalLogger.error(String.format("Could not parse last player update, date: %s", e.getParsedString()), e);
                // critical error
                System.out.println("Critical error: could not retrieve players' information");
                System.exit(1);

            }

            // get roster mapping to user from sleeper
            List<RosterUserResponse> rosterMapping = RequestFormatter.getRostersFromLeagueId(chosenLeagueId);

            // process roster mapping and insert to db
            List<RosterUser> rosterUsers = this.formatter.processRosterUser(rosterMapping);
            List<Long> rosterUserIds = rosterUsers.stream().map(RosterUser::getRosterUserId).toList();

            // for each week
            for (int week = 1; week < currWeek; week++) {
                // check if week scores for this league is already in database
                // assumes that if a single user's week score is in the database, 
                // all users' week scores are
                if (this.repo.getWeekScoresByRosterUserIdsAndWeek(rosterUserIds, week).size() > 0) {
                    continue;
                }

                // get rosters from sleeper 
                List<MatchupResponse> matchups = RequestFormatter.getMatchupsFromLeagueIdAndWeek(chosenLeagueId, week);

                // process rosters and insert to db
                this.formatter.processMatchups(matchups, chosenLeagueId, week);
            }

            // query database for scores for all users
            List<List<WeekScore>> allScores = new ArrayList<>();
            for (int i = 1; i < currWeek; i++) {
                allScores.add(this.repo.getWeekScoresByRosterUserIdsAndWeek(rosterUserIds, i));
            }
            
            Map<Long, AllPlayData> allPlayData = Stats.computeAllPlayLuckScore(rosterUsers, allScores);
            GlobalLogger.debug("All play data: " + allPlayData.toString());


            // compute stats from rosters information (lots of logic here)
            LuckData luckData = Stats.computeTotalLuckScore(rosterUsers, allScores);
            

            // format data
            Map<Long, String> rosterUserIdToName = this.repo.getRosterUserIdToName();
            String formattedLuckData = Stats.formatLuckData(rosterUserIdToName, luckData);
            clearScreen();
            System.out.println(formattedLuckData);

            // prompt user to choose to [q] quit, [c] choose a different league
            boolean ifQuit = this.ifQuit();
            if (ifQuit) {
                break;
            }
        }
        // maybe later, add option to choose a different username


        System.out.println("Closing application");
        

        // get league info
    }

    public boolean ifQuit() {
        do {
            System.out.println("Press [q] to quit, [c] to choose a different league");
            String input = this.scan.nextLine().strip().toLowerCase();
            if (input.equals("q")) {
                return true;
            } else if (input.equals("c")) {
                return false;
            } else {
                System.out.println("Invalid input\n");
            }
        } while (true);
    }

    
    /**
     * Get userId from sleeper based on input username from user
     * @return userId of user
     */
    public long getUserId(){
        // prompt user for username until valid httpresponse is returned
        do {
            System.out.println("Please enter your Sleeper username");
            String username = this.scan.nextLine();
            try {
                HttpResponse<String> response = SleeperRequestHandler.getUserFromUsername(username);

                // had a valid username, now need to get userId from user_id
                // attribute in response
                if (response.statusCode() == 200) {
                    
                    UsernameResponse resp = om.readValue(response.body(), UsernameResponse.class);
                    long userId = resp.getUser_id();
                    return userId;
                }
            } catch (Exception e) {
                GlobalLogger.debug("Could not find user", e);
                System.out.println("Invalid username\n");
            }
        } while (true);
    }


    /**
     * Ask user to choose a league from options
     * @param leagues list of leagues that are available
     * @return the id of the chosen league
     */
    public Long chooseLeague(List<League> leagues) {
        // prompt user to choose a league

        do {
            System.out.println("Please choose a league to get stats for");
            for (int i = 0; i < leagues.size(); i++) {
                System.out.printf("[%d] %s\n\n", i+1, leagues.get(i).getLeagueName());
            }
            String input = this.scan.nextLine();
            try {
                int choice = Integer.parseInt(input);
                if (choice >= 1 && choice <= leagues.size()) { // 1-indexed for user
                    return leagues.get(choice-1).getLeagueId();
                }
            } catch (NumberFormatException e) {
            }
            System.out.println("Invalid input\n");
        } while (true);
        // return the id of the chosen league
    }

 

    private boolean playersUpdatedRecently() throws DateTimeParseException {
        SystemMetadata lastUpdateData = this.repo.getSystemMetadata("last_player_update");

        if (lastUpdateData == null) { // hasn't been put in db yet
            return false;
        }

        String lastUpdateStr = lastUpdateData.getValue();

        LocalDateTime lastUpdate = LocalDateTime.parse(lastUpdateStr);
        LocalDateTime now = LocalDateTime.now();
        // if less than 1 day has passed, return true
        return lastUpdate.isAfter(now.minusDays(1));
        
       
    }

    private void updateLastPlayerUpdate() {
        LocalDateTime now = LocalDateTime.now();
        // if it is already present, update it, otherwise add it
        if (this.repo.getSystemMetadata("last_player_update") == null) {
            this.repo.save(new SystemMetadata("last_player_update", now.toString()));
        } else {
            this.repo.updateSystemMetadata("last_player_update", now.toString());
        }
    }

    

    /**
     * Clears the screen. Utility method for cleaning the terminal
     */
    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public void close() throws IOException {
        this.repo.close();
    }


    
}
