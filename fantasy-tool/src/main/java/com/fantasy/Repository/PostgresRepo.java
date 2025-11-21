package com.fantasy.Repository;

import com.Logger.GlobalLogger;

import java.io.Closeable;
import java.sql.*;

public class PostgresRepo implements  Closeable {
    private static final String Postgre_URL = "jdbc:postgresql://localhost:5432/fantasydb";
    private static final String Postgre_User = "postgres";
    private static final String Postgre_PW = "mysecretpassword";
    private final Connection connection;

    public PostgresRepo() throws ExceptionInInitializerError {
        try {
            connection = DriverManager.getConnection(Postgre_URL, Postgre_User, Postgre_PW);
            try {

                createTables();
            } catch (SQLException e) {
                GlobalLogger.error("Could not create tables", e);
            }
        } catch (SQLException e) {
            GlobalLogger.error("Could not connect to database", e);
            throw new ExceptionInInitializerError(e);
        }
    }

    public void close() {
        try (this.connection) {
            this.connection.close();
        } catch (Exception e) {
            GlobalLogger.error("Could not close connection", e);
        }
    }

    /**
     * Initializes the tables necessary for the application if they are not already
     * created Must have
     * already created a connection to the database
     *
     * @throws SQLException
     */
    private void createTables() throws SQLException {
        String createTablesString = """
-- create tables

CREATE TABLE IF NOT EXISTS player (
    player_id INT PRIMARY KEY,
    team VARCHAR(50),
    full_name VARCHAR(50),
    fantasy_data_id INT,
    stats_id INT,
    roto_world_id INT
);

CREATE TABLE IF NOT EXISTS player_positions (
    player_id INT,
    position VARCHAR(10),
    PRIMARY KEY (player_id, position)
);

CREATE TABLE IF NOT EXISTS users (
    user_id INT PRIMARY KEY,
    username VARCHAR(50),
    display_name VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS league (
    league_id INT PRIMARY KEY,
    total_rosters INT,
    season INT
);

CREATE TABLE IF NOT EXISTS matchup (
    matchup_id INT PRIMARY KEY,
    league_id INT,
    week INT
);

CREATE SEQUENCE IF NOT EXISTS roster_user_id_seq START 1 INCREMENT 1;

CREATE TABLE IF NOT EXISTS roster_user (
    roster_user_id INT PRIMARY KEY DEFAULT nextval('roster_user_id_seq'),
    user_id INT,
    league_id INT
);

CREATE TABLE IF NOT EXISTS roster (
    roster_user_id INT,
    player_id INT,
    matchup_id INT,
    points DECIMAL(5, 2),
    week INT,
    starting BOOLEAN,
    PRIMARY KEY (roster_user_id, player_id)
);

CREATE TABLE IF NOT EXISTS transaction (
    transaction_id INT PRIMARY KEY,
    transaction_type VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS transaction_player (
    transaction_id INT,
    player_id INT,
    roster_user_id INT,
    added BOOLEAN,
    PRIMARY KEY (transaction_id, roster_user_id, player_id)
);

CREATE TABLE IF NOT EXISTS draft (
    draft_id INT PRIMARY KEY,
    league_id INT
);

CREATE TABLE IF NOT EXISTS draft_player (
    draft_id INT,
    player_id INT,
    pick INT,
    roster_user_id INT,
    PRIMARY KEY (roster_user_id, pick)
);

-- add foreign keys to tables if missing

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_constraint WHERE conname = 'fk_player_positions_player'
    ) THEN
        ALTER TABLE player_positions
        ADD CONSTRAINT fk_player_positions_player
        FOREIGN KEY (player_id) REFERENCES player(player_id);
    END IF;
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_constraint WHERE conname = 'fk_matchup_league'
    ) THEN
        ALTER TABLE matchup
        ADD CONSTRAINT fk_matchup_league
        FOREIGN KEY (league_id) REFERENCES league(league_id);
    END IF;
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_constraint WHERE conname = 'fk_roster_user_user'
    ) THEN
        ALTER TABLE roster_user
        ADD CONSTRAINT fk_roster_user_user
        FOREIGN KEY (user_id) REFERENCES users(user_id);
    END IF;

    IF NOT EXISTS (
        SELECT 1 FROM pg_constraint WHERE conname = 'fk_roster_user_league'
    ) THEN
        ALTER TABLE roster_user
        ADD CONSTRAINT fk_roster_user_league
        FOREIGN KEY (league_id) REFERENCES league(league_id);
    END IF;
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_constraint WHERE conname = 'fk_roster_roster_user'
    ) THEN
        ALTER TABLE roster
        ADD CONSTRAINT fk_roster_roster_user
        FOREIGN KEY (roster_user_id) REFERENCES roster_user(roster_user_id);
    END IF;

    IF NOT EXISTS (
        SELECT 1 FROM pg_constraint WHERE conname = 'fk_roster_player'
    ) THEN
        ALTER TABLE roster
        ADD CONSTRAINT fk_roster_player
        FOREIGN KEY (player_id) REFERENCES player(player_id);
    END IF;

    IF NOT EXISTS (
        SELECT 1 FROM pg_constraint WHERE conname = 'fk_roster_matchup'
    ) THEN
        ALTER TABLE roster
        ADD CONSTRAINT fk_roster_matchup
        FOREIGN KEY (matchup_id) REFERENCES matchup(matchup_id);
    END IF;
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_constraint WHERE conname = 'fk_transaction_player_transaction'
    ) THEN
        ALTER TABLE transaction_player
        ADD CONSTRAINT fk_transaction_player_transaction
        FOREIGN KEY (transaction_id) REFERENCES transaction(transaction_id);
    END IF;

    IF NOT EXISTS (
        SELECT 1 FROM pg_constraint WHERE conname = 'fk_transaction_player_player'
    ) THEN
        ALTER TABLE transaction_player
        ADD CONSTRAINT fk_transaction_player_player
        FOREIGN KEY (player_id) REFERENCES player(player_id);
    END IF;

    IF NOT EXISTS (
        SELECT 1 FROM pg_constraint WHERE conname = 'fk_transaction_player_roster_user'
    ) THEN
        ALTER TABLE transaction_player
        ADD CONSTRAINT fk_transaction_player_roster_user
        FOREIGN KEY (roster_user_id) REFERENCES roster_user(roster_user_id);
    END IF;
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_constraint WHERE conname = 'fk_draft_league'
    ) THEN
        ALTER TABLE draft
        ADD CONSTRAINT fk_draft_league
        FOREIGN KEY (league_id) REFERENCES league(league_id);
    END IF;
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_constraint WHERE conname = 'fk_draft_player_draft'
    ) THEN
        ALTER TABLE draft_player
        ADD CONSTRAINT fk_draft_player_draft
        FOREIGN KEY (draft_id) REFERENCES draft(draft_id);
    END IF;

    IF NOT EXISTS (
        SELECT 1 FROM pg_constraint WHERE conname = 'fk_draft_player_player'
    ) THEN
        ALTER TABLE draft_player
        ADD CONSTRAINT fk_draft_player_player
        FOREIGN KEY (player_id) REFERENCES player(player_id);
    END IF;

    IF NOT EXISTS (
        SELECT 1 FROM pg_constraint WHERE conname = 'fk_draft_player_roster_user'
    ) THEN
        ALTER TABLE draft_player
        ADD CONSTRAINT fk_draft_player_roster_user
        FOREIGN KEY (roster_user_id) REFERENCES roster_user(roster_user_id);
    END IF;
END $$;
        
        """;
        try (Statement st = this.connection.createStatement()) {
            st.execute(createTablesString);
        }
    }
}
