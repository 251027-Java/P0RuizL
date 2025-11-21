// package com.fantasy.Repository;

// import com.fantasy.Model.*;

// import java.util.ArrayList;
// import java.util.List;

// import org.hibernate.Session;
// import org.hibernate.Transaction;

// public class HibernateRepo implements IRepository {

//     public void saveOrUpdate(Player player) {
//         try (Session session = HibernateUtil.getSessionFactory().openSession()) {
//             // check if player already exists, if it does update, otherwise insert
//             Player dbPlayer = this.getPlayerByPlayerId(player.getPlayerId());
//             Transaction tx = session.beginTransaction();
//             if (dbPlayer == null) {
//                 session.persist(player);
//             } else {
//                 dbPlayer.setFullName(player.getFullName());
//                 dbPlayer.setTeam(player.getTeam());
//                 dbPlayer.setRotoworldId(player.getRotoworldId());
//                 dbPlayer.setStatsId(player.getStatsId());
//                 dbPlayer.setFantasyDataId(player.getFantasyDataId());
//             }
//             tx.commit();
//         }
//     }

//     private Player getPlayerByPlayerId(String playerId) {
//         try (Session session = HibernateUtil.getSessionFactory().openSession()) {
//             return session.find(Player.class, playerId);
//         }   
//     }

//     public void save(League league) {
//         try (Session session = HibernateUtil.getSessionFactory().openSession()) {
//             Transaction tx = session.beginTransaction();
//             session.persist(league);
//             tx.commit();
//         }
//     }

//     public void save(Draft draft) {
//         try (Session session = HibernateUtil.getSessionFactory().openSession()) {
//             Transaction tx = session.beginTransaction();
//             session.persist(draft);
//             tx.commit();
//         }
//     }

//     public void save(User dbUser) {
//         try (Session session = HibernateUtil.getSessionFactory().openSession()) {
//             Transaction tx = session.beginTransaction();
//             session.persist(dbUser);
//             tx.commit();
//         }
//     }

//     public void save(SystemMetadata systemMetadata) {
//         try (Session session = HibernateUtil.getSessionFactory().openSession()) {
//             Transaction tx = session.beginTransaction();
//             session.persist(systemMetadata);
//             tx.commit();
//         }
//     }

//     public void saveOrUpdate(List<PlayerPosition> playerPositions) {
//         if (playerPositions.size() == 0) {
//             return;
//         }
//         String playerId = playerPositions.get(0).getPlayerId();
//         try (Session session = HibernateUtil.getSessionFactory().openSession()) {
//             // get all player positions in the db for this player
//             Transaction tx = session.beginTransaction();
//             List<PlayerPosition> existingPlayerPositions = this.getPlayerPositionsByPlayerId(playerId);
//             // if empty, insert all player positions in list
//             if (existingPlayerPositions.size() == 0) {
//                 for (PlayerPosition playerPosition : playerPositions) {
//                     session.persist(playerPosition);
//                 }
//             } else { // find the ones that are not in the list and insert
//                 for (PlayerPosition playerPosition : playerPositions) {
//                     int index = existingPlayerPositions.indexOf(playerPosition);
//                     boolean found = index != -1;
//                     if (!found) {
//                         session.persist(playerPosition);
//                         tx.commit();
//                     }
//                 }

//                 List<Integer> indexsToRemove = new ArrayList<Integer>();
//                 // find the existing indexs that are no longer there and delete
//                 for (int i = 0; i < existingPlayerPositions.size(); i++) {
//                     PlayerPosition existingPlayerPosition = existingPlayerPositions.get(i);
//                     int index = playerPositions.indexOf(existingPlayerPosition);
//                     boolean found = index != -1;
//                     if (!found) {
//                         indexsToRemove.add(i);
//                     }
//                 }

//                 // remove the ones that are not in the list
//                 for (int i = 0; i < indexsToRemove.size(); i++) {
//                     int index = indexsToRemove.get(i);
//                     PlayerPosition playerPosition = existingPlayerPositions.get(index);
//                     session.remove(playerPosition);
//                 }
//             }
//             tx.commit();
//         }
//     }

//     // get all player positions for a given playerid
//     public List<PlayerPosition> getPlayerPositionsByPlayerId(String playerId) {
//         try (Session session = HibernateUtil.getSessionFactory().openSession()) {
//             return session.createQuery("from PlayerPosition where playerId = :playerId", PlayerPosition.class)
//                 .setParameter("playerId", playerId)
//                 .getResultList();
//         }
//     }

//     // search for league by id 
//     public League getLeagueById(long id) {
//         try (Session session = HibernateUtil.getSessionFactory().openSession()) {
//             return session.find(League.class, id);
//         }
//     }

//     public Draft getDraftById(long draftId) {
//         try (Session session = HibernateUtil.getSessionFactory().openSession()) {
//             return session.find(Draft.class, draftId);
//         }
//     }

//     public User getUserById(long userId) {
//         try (Session session = HibernateUtil.getSessionFactory().openSession()) {
//             return session.find(User.class, userId);
//         }
//     }

//     public List<League> getLeaguesById(List<Long> leagueIds) {
//         try (Session session = HibernateUtil.getSessionFactory().openSession()) {
//             return session.createQuery("from League where id in :ids", League.class)
//                 .setParameter("ids", leagueIds)
//                 .getResultList();
//         }
//     }

//     public List<User> getUsers() {
//         try (Session session = HibernateUtil.getSessionFactory().openSession()) {
//             return session.createQuery("from User", User.class).getResultList();
//         }
//     } 

//     public SystemMetadata getSystemMetadata(String key) {
//         try (Session session = HibernateUtil.getSessionFactory().openSession()) {
//             return session.find(SystemMetadata.class, key);
//         }
//     }

//     public String getLastUpdatedPlayers() {
//         try (Session session = HibernateUtil.getSessionFactory().openSession()) {
//             return session.find(SystemMetadata.class, "last_updated_players").getValue();
//         }
//     }

//     public void updateSystemMetadata(String key, String value) {
//         try (Session session = HibernateUtil.getSessionFactory().openSession()) {
//             Transaction tx = session.beginTransaction();
//             SystemMetadata systemMetadata = session.find(SystemMetadata.class, key);
//             systemMetadata.setValue(value);
//             tx.commit();
//         }
//     }



//     public void close() {
//         HibernateUtil.shutdown();
//     }

// }
