package com.hulefevr.swingy.model.game;

import com.hulefevr.swingy.model.artifact.Artifact;

/**
 * Résultat d'un combat entre le héros et un ennemi
 */
public class BattleResult {
    private boolean victory;
    private int xpGained;
    private Artifact lootDropped;
    private boolean heroLeveledUp;

    public BattleResult(boolean victory, int xpGained) {
        this.victory = victory;
        this.xpGained = xpGained;
        this.lootDropped = null;
        this.heroLeveledUp = false;
    }

    public boolean isVictory() {
        return victory;
    }

    public void setVictory(boolean victory) {
        this.victory = victory;
    }

    public int getXpGained() {
        return xpGained;
    }

    public void setXpGained(int xpGained) {
        this.xpGained = xpGained;
    }

    public Artifact getLootDropped() {
        return lootDropped;
    }

    public void setLootDropped(Artifact lootDropped) {
        this.lootDropped = lootDropped;
    }

    public boolean isHeroLeveledUp() {
        return heroLeveledUp;
    }

    public void setHeroLeveledUp(boolean heroLeveledUp) {
        this.heroLeveledUp = heroLeveledUp;
    }

    public boolean hasLoot() {
        return lootDropped != null;
    }
}
