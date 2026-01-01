package com.hulefevr.swingy.persistence.db;

import com.hulefevr.swingy.model.hero.Hero;
import com.hulefevr.swingy.persistence.HeroRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implémentation JDBC du repository de héros (SQLite)
 */
public class JdbcHeroRepository implements HeroRepository {
    private static final String DB_URL = "jdbc:sqlite:swingy.db";
    private Connection connection;
    
    public JdbcHeroRepository() {
        try {
            // Charger le driver SQLite
            Class.forName("org.sqlite.JDBC");
            // Créer la connexion
            connection = DriverManager.getConnection(DB_URL);
            // Initialiser le schéma
            DbSchema.initializeSchema(connection);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("SQLite JDBC driver not found", e);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to connect to database", e);
        }
    }
    
    @Override
    public void save(Hero hero) {
        String sql = "INSERT INTO heroes (name, hero_class, level, experience, " +
                     "base_attack, base_defense, base_hit_points, current_hit_points) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?) " +
                     "ON CONFLICT(name) DO UPDATE SET " +
                     "hero_class = excluded.hero_class, " +
                     "level = excluded.level, " +
                     "experience = excluded.experience, " +
                     "base_attack = excluded.base_attack, " +
                     "base_defense = excluded.base_defense, " +
                     "base_hit_points = excluded.base_hit_points, " +
                     "current_hit_points = excluded.current_hit_points";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, hero.getName());
            stmt.setString(2, hero.getHeroClass().name());
            stmt.setInt(3, hero.getLevel());
            stmt.setInt(4, hero.getExperience());
            stmt.setInt(5, getBaseAttack(hero));
            stmt.setInt(6, getBaseDefense(hero));
            stmt.setInt(7, getBaseHP(hero));
            stmt.setInt(8, hero.getHitPoints());
            
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save hero: " + hero.getName(), e);
        }
    }
    
    @Override
    public Optional<Hero> findByName(String name) {
        String sql = "SELECT * FROM heroes WHERE name = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return Optional.of(HeroRowMapper.mapRow(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find hero: " + name, e);
        }
    }
    
    @Override
    public List<Hero> findAll() {
        String sql = "SELECT * FROM heroes ORDER BY level DESC, name ASC";
        List<Hero> heroes = new ArrayList<>();
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                heroes.add(HeroRowMapper.mapRow(rs));
            }
            return heroes;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find all heroes", e);
        }
    }
    
    @Override
    public boolean deleteByName(String name) {
        String sql = "DELETE FROM heroes WHERE name = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, name);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete hero: " + name, e);
        }
    }
    
    @Override
    public boolean exists(String name) {
        String sql = "SELECT COUNT(*) FROM heroes WHERE name = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to check hero existence: " + name, e);
        }
    }
    
    /**
     * Ferme la connexion à la base de données
     */
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            // Log l'erreur mais ne throw pas
            System.err.println("Error closing database connection: " + e.getMessage());
        }
    }
    
    // Méthodes utilitaires pour extraire les stats de base
    // (car Hero calcule les stats finales avec le niveau)
    
    private int getBaseAttack(Hero hero) {
        // Formule inverse: base = current - (level - 1) * 2
        return hero.getAttack() - (hero.getLevel() - 1) * 2;
    }
    
    private int getBaseDefense(Hero hero) {
        return hero.getDefense() - (hero.getLevel() - 1) * 2;
    }
    
    private int getBaseHP(Hero hero) {
        return hero.getMaxHitPoints() - (hero.getLevel() - 1) * 10;
    }
}