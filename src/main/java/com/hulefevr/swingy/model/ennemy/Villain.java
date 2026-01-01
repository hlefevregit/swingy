package com.hulefevr.swingy.model.ennemy;

public class Villain {
    private String name;
    private int level;
    private int attack;
    private int defense;
    private int health;

    public Villain(String name, int level, int attack, int defense, int health) {
        this.name = name;
        this.level = level;
        this.attack = attack;
        this.defense = defense;
        this.health = health;
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public int getAttack() {
        return attack;
    }

    public int getDefense() {
        return defense;
    }

    public int getHealth() {
        return health;
    }
}