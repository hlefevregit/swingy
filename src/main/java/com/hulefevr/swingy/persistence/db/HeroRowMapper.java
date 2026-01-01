// ResultSet -> Hero

package com.hulefevr.swingy.persistence.db;

import com.hulefevr.swingy.model.hero.Hero;
import com.hulefevr.swingy.model.hero.HeroClass;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Convertit une ligne de la base de données en objet Hero
 */
public class HeroRowMapper {
    
    /**
     * Convertit un ResultSet en Hero
     * @param rs Le ResultSet positionné sur une ligne
     * @return Le héros créé à partir des données
     */
    public static Hero mapRow(ResultSet rs) throws SQLException {
        String name = rs.getString("name");
        HeroClass heroClass = HeroClass.valueOf(rs.getString("hero_class"));
        int baseAttack = rs.getInt("base_attack");
        int baseDefense = rs.getInt("base_defense");
        int baseHP = rs.getInt("base_hit_points");
        
        // Créer le héros avec les stats de base
        Hero hero = new Hero(name, heroClass, baseAttack, baseDefense, baseHP);
        
        // Restaurer le niveau et l'XP
        int level = rs.getInt("level");
        int experience = rs.getInt("experience");
        int currentHP = rs.getInt("current_hit_points");
        
        // Utiliser reflection ou créer des setters pour restaurer l'état
        // Pour l'instant, on va ajouter des setters dans Hero
        restoreHeroState(hero, level, experience, currentHP);
        
        // TODO: Charger les artefacts équipés (weapon_id, armor_id, helm_id)
        
        return hero;
    }
    
    /**
     * Restaure l'état d'un héros (level, XP, HP)
     */
    private static void restoreHeroState(Hero hero, int level, int experience, int currentHP) {
        // Restaurer le niveau et l'XP directement
        hero.setLevel(level);
        hero.setExperience(experience);
        
        // Restaurer les HP
        hero.setCurrentHitPoints(currentHP);
    }
}