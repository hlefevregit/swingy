package com.hulefevr.swingy.model.ennemy;

public class Villain {
    private String name;
    private int level;
    private VillainType type;
    private int attack;
    private int defense;
    private int hitPoints;

    public Villain(String name, int level, VillainType type) {
        this.name = name;
        this.level = level;
        this.type = type;
        
        // Calculer stats basées sur le level et le type
        this.attack = type.getBaseAttack() + (level - 1) * 2;
        this.defense = type.getBaseDefense() + (level - 1) * 2;
        this.hitPoints = type.getBaseHitPoints() + (level - 1) * 10;
    }
    
    // Ancien constructeur pour compatibilité
    public Villain(String name, int level, int attack, int defense, int health) {
        this.name = name;
        this.level = level;
        this.type = VillainType.WATCHER; // Type par défaut
        this.attack = attack;
        this.defense = defense;
        this.hitPoints = health;
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }
    
    public VillainType getType() {
        return type;
    }

    public int getAttack() {
        return attack;
    }

    public int getDefense() {
        return defense;
    }

    public int getHitPoints() {
        return hitPoints;
    }
    
    // Alias pour compatibilité
    public int getHealth() {
        return hitPoints;
    }
}