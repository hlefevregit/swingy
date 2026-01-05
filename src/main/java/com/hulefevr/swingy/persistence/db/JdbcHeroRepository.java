package com.hulefevr.swingy.persistence.db;

import com.hulefevr.swingy.model.hero.Hero;
import com.hulefevr.swingy.model.artifact.Artifact;
import com.hulefevr.swingy.model.artifact.ArtifactType;
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
        // Avant de sauvegarder le héros, persister les artefacts équipés (si présent)
        Integer weaponId = null;
        Integer armorId = null;
        Integer helmId = null;

        try {
            if (hero.getWeapon() != null) {
                weaponId = ensureArtifactExists(hero.getWeapon());
            }
            if (hero.getArmor() != null) {
                armorId = ensureArtifactExists(hero.getArmor());
            }
            if (hero.getHelm() != null) {
                helmId = ensureArtifactExists(hero.getHelm());
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to persist artifacts for hero: " + hero.getName(), e);
        }

        String sql = "INSERT INTO heroes (name, hero_class, level, experience, " +
                     "base_attack, base_defense, base_hit_points, current_hit_points, " +
                     "weapon_id, armor_id, helm_id) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                     "ON CONFLICT(name) DO UPDATE SET " +
                     "hero_class = excluded.hero_class, " +
                     "level = excluded.level, " +
                     "experience = excluded.experience, " +
                     "base_attack = excluded.base_attack, " +
                     "base_defense = excluded.base_defense, " +
                     "base_hit_points = excluded.base_hit_points, " +
                     "current_hit_points = excluded.current_hit_points, " +
                     "weapon_id = excluded.weapon_id, " +
                     "armor_id = excluded.armor_id, " +
                     "helm_id = excluded.helm_id";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, hero.getName());
            stmt.setString(2, hero.getHeroClass().name());
            stmt.setInt(3, hero.getLevel());
            stmt.setInt(4, hero.getExperience());
            stmt.setInt(5, getBaseAttack(hero));
            stmt.setInt(6, getBaseDefense(hero));
            stmt.setInt(7, getBaseHP(hero));
            stmt.setInt(8, hero.getHitPoints());

            if (weaponId != null) stmt.setInt(9, weaponId); else stmt.setNull(9, Types.INTEGER);
            if (armorId != null) stmt.setInt(10, armorId); else stmt.setNull(10, Types.INTEGER);
            if (helmId != null) stmt.setInt(11, helmId); else stmt.setNull(11, Types.INTEGER);

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
                Hero hero = HeroRowMapper.mapRow(rs);

                // Charger les artefacts associés
                int weaponId = rs.getInt("weapon_id");
                if (!rs.wasNull()) {
                    Artifact weapon = loadArtifactById(weaponId);
                    hero.setWeapon(weapon);
                }
                int armorId = rs.getInt("armor_id");
                if (!rs.wasNull()) {
                    Artifact armor = loadArtifactById(armorId);
                    hero.setArmor(armor);
                }
                int helmId = rs.getInt("helm_id");
                if (!rs.wasNull()) {
                    Artifact helm = loadArtifactById(helmId);
                    hero.setHelm(helm);
                }

                return Optional.of(hero);
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
                Hero hero = HeroRowMapper.mapRow(rs);

                // Charger artefacts
                int weaponId = rs.getInt("weapon_id");
                if (!rs.wasNull()) {
                    hero.setWeapon(loadArtifactById(weaponId));
                }
                int armorId = rs.getInt("armor_id");
                if (!rs.wasNull()) {
                    hero.setArmor(loadArtifactById(armorId));
                }
                int helmId = rs.getInt("helm_id");
                if (!rs.wasNull()) {
                    hero.setHelm(loadArtifactById(helmId));
                }

                heroes.add(hero);
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

    /**
     * Vérifie si un artefact existe (même nom/type/bonus) et le crée si besoin.
     * Retourne l'id de l'artefact.
     */
    private Integer ensureArtifactExists(Artifact artifact) throws SQLException {
        // Chercher d'abord
        String select = "SELECT id FROM artifacts WHERE name = ? AND type = ? AND bonus_value = ? LIMIT 1";
        try (PreparedStatement stmt = connection.prepareStatement(select)) {
            stmt.setString(1, artifact.getName());
            stmt.setString(2, artifact.getType().name());
            stmt.setInt(3, artifact.getBonusValue());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        }

        // Sinon insérer
        String insert = "INSERT INTO artifacts (name, type, bonus_value) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(insert)) {
            stmt.setString(1, artifact.getName());
            stmt.setString(2, artifact.getType().name());
            stmt.setInt(3, artifact.getBonusValue());
            int affected = stmt.executeUpdate();
            if (affected == 0) {
                throw new SQLException("Inserting artifact failed, no rows affected.");
            }
        }

        // JDBC driver for SQLite may not support getGeneratedKeys(); use last_insert_rowid()
        try (Statement last = connection.createStatement();
             ResultSet rs2 = last.executeQuery("SELECT last_insert_rowid()")) {
            if (rs2.next()) {
                return rs2.getInt(1);
            } else {
                throw new SQLException("Inserting artifact failed, no ID obtained via last_insert_rowid().");
            }
        }
    }

    /**
     * Charge un artefact depuis son id
     */
    private Artifact loadArtifactById(int id) throws SQLException {
        String sql = "SELECT name, type, bonus_value FROM artifacts WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String name = rs.getString("name");
                String typeStr = rs.getString("type");
                int bonus = rs.getInt("bonus_value");
                ArtifactType type;
                try {
                    type = ArtifactType.valueOf(typeStr);
                } catch (IllegalArgumentException e) {
                    // Par sécurité, fallback sur WEAPON
                    type = ArtifactType.WEAPON;
                }
                return new Artifact(name, type, bonus);
            }
            return null;
        }
    }
}