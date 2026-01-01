// création tables si besoin

package com.hulefevr.swingy.persistence.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Gère le schéma de la base de données SQLite
 */
public class DbSchema {
    
    /**
     * Crée les tables si elles n'existent pas
     */
    public static void initializeSchema(Connection connection) throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            // Table des héros
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS heroes (" +
                "    id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "    name TEXT NOT NULL UNIQUE," +
                "    hero_class TEXT NOT NULL," +
                "    level INTEGER NOT NULL DEFAULT 1," +
                "    experience INTEGER NOT NULL DEFAULT 0," +
                "    base_attack INTEGER NOT NULL," +
                "    base_defense INTEGER NOT NULL," +
                "    base_hit_points INTEGER NOT NULL," +
                "    current_hit_points INTEGER NOT NULL," +
                "    weapon_id INTEGER," +
                "    armor_id INTEGER," +
                "    helm_id INTEGER," +
                "    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ")"
            );
            
            // Table des artefacts (pour plus tard)
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS artifacts (" +
                "    id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "    name TEXT NOT NULL," +
                "    type TEXT NOT NULL," +
                "    bonus_value INTEGER NOT NULL," +
                "    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ")"
            );
        }
    }
    
    /**
     * Supprime toutes les tables (utile pour reset)
     */
    public static void dropAllTables(Connection connection) throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS heroes");
            stmt.execute("DROP TABLE IF EXISTS artifacts");
        }
    }
}