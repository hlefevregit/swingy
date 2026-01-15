package com.hulefevr.swingy.service;

import com.hulefevr.swingy.model.hero.Hero;
import com.hulefevr.swingy.model.ennemy.Villain;
import com.hulefevr.swingy.model.game.BattleResult;
import com.hulefevr.swingy.model.artifact.Artifact;
import java.util.Random;

/**
 * Service gérant les combats entre le héros et les ennemis.
 */
public class BattleService {
    private Random random = new Random();
    private LootService lootService = new LootService();
    
    /**
     * Combat entre le héros et l'ennemi.
     * @return Résultat du combat
     */
    public BattleResult fight(Hero hero, Villain enemy) {
        // Combat simplifié pour l'instant
        // TODO: Implémenter un système de combat tour par tour plus complexe
        
        // Utiliser les stats max du héros, pas ses HP actuels
        int heroScore = hero.getAttack() + hero.getDefense() + (hero.getMaxHitPoints() / 2);
        int enemyScore = enemy.getAttack() + enemy.getDefense() + (enemy.getHitPoints() / 2);
        
        // Ajouter un facteur aléatoire (±30% de la moyenne des scores)
        int variance = (heroScore + enemyScore) / 6;
        heroScore += random.nextInt(variance);
        enemyScore += random.nextInt(variance);
        
        boolean victory = heroScore > enemyScore;
        
        if (victory) {
            // Calculer XP gagné
            int xpGained = enemy.getLevel() * 100 + random.nextInt(50);
            
            // Chance de loot (30%)
            Artifact loot = null;
            if (random.nextDouble() < 0.3) {
                loot = lootService.generateLoot(enemy.getLevel());
            }
            
            return new BattleResult(true, xpGained, loot);
        } else {
            // Défaite : le héros perd des HP
            hero.takeDamage(enemy.getAttack());
            
            // Si HP à 0, mort
            if (hero.getHitPoints() <= 0) {
                return new BattleResult(false, 0, null);
            }
            
            // Sinon, combat perdu mais héros survit
            return new BattleResult(false, 0, null);
        }
    }
    
    /**
     * Tentative de fuite du combat.
     * @return true si la fuite réussit, false sinon
     */
    public boolean attemptRun(Hero hero, Villain enemy) {
        // Chance de fuite basée sur la vitesse/level
        // 50% de base + bonus si hero level > enemy level
        double baseChance = 0.5;
        double levelBonus = (hero.getLevel() - enemy.getLevel()) * 0.1;
        double totalChance = Math.min(0.9, Math.max(0.1, baseChance + levelBonus));
        
        return random.nextDouble() < totalChance;
    }
}
