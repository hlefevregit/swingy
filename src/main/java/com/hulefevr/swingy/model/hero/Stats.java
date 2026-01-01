// atk/def/hp base + calculs

package com.hulefevr.swingy.model.hero;

public class Stats {
    private int baseAttack;
    private int baseDefense;
    private int baseHP;

    public Stats(int baseAttack, int baseDefense, int baseHP) {
        this.baseAttack = baseAttack;
        this.baseDefense = baseDefense;
        this.baseHP = baseHP;
    }

    public int getBaseAttack() {
        return baseAttack;
    }

    public int getBaseDefense() {
        return baseDefense;
    }

    public int getBaseHP() {
        return baseHP;
    }

    public int calculateAttack(int level) {
        return baseAttack + (level * 2);
    }

    public int calculateDefense(int level) {
        return baseDefense + (level * 2);
    }

    public int calculateHP(int level) {
        return baseHP + (level * 10);
    }
}