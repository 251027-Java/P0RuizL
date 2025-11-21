package com.fantasy.Repository;

import java.io.Closeable;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.fantasy.Model.*;
import com.fantasy.Request.RequestModels.LeagueResponse;

public interface IRepository extends Closeable {
    public void saveOrUpdate(Player player);
    public void save(League league);
    public void save(Draft draft);
    public League getLeagueById(long id);
    public Object getDraftById(long draftId);
    public List<League> getLeaguesById(List<Long> leagueIds);
    public Object getUserById(long userId);
    public void save(User dbUser);
    public SystemMetadata getSystemMetadata(String key);
    public void save(SystemMetadata systemMetadata);
    public void updateSystemMetadata(String string, String string2);
    public String getLastUpdatedPlayers();
    public void saveOrUpdate(List<PlayerPosition> playerPositions);
    public RosterUser getRosterUserByUserIdAndLeagueId(long userId, long leagueId);
    public void save(RosterUser dbRoster);
    public RosterUser getRosterUserByRosterIdAndLeagueId(Integer rosterId, long leagueId);
    public WeekScore getWeekScoreById(WeekScoreId weekNumId);
    public void save(WeekScore weekScore);
    public List<WeekScore> getWeekScoresByLeagueIdAndWeek(long chosenLeagueId, int week);
    public List<WeekScore> getWeekScoresByRosterUserIdsAndWeek(List<Long> rosterUserIds, int week);
    public RosterUser saveOrUpdate(RosterUser newRosterUser);
    public Map<Long, String> getRosterUserIdToName();
    
}
