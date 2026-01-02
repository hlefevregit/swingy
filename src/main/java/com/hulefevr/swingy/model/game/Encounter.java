// optionnel: rencontre actuelle

package com.hulefevr.swingy.model.game;

import com.hulefevr.swingy.model.ennemy.Villain;

/**
 * Représente une rencontre avec un ennemi
 */
public class Encounter {
    private Villain enemy;
    private boolean resolved;

    public Encounter(Villain enemy) {
        this.enemy = enemy;
        this.resolved = false;
    }

    public Villain getEnemy() {
        return enemy;
    }

    public void setEnemy(Villain enemy) {
        this.enemy = enemy;
    }

    public boolean isResolved() {
        return resolved;
    }

    public void setResolved(boolean resolved) {
        this.resolved = resolved;
    }

    public void endEncounter() {
        this.resolved = true;
    }
}