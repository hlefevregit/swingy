// optionnel: rencontre actuelle

package com.hulefevr.swingy.model.game;

import com.hulefevr.swingy.model.ennemy.Villain;

/**
 * Représente une rencontre avec un ennemi
 */
public class Encounter {
    private Villain enemy;
    private boolean inProgress;

    public Encounter(Villain enemy) {
        this.enemy = enemy;
        this.inProgress = true;
    }

    public Villain getEnemy() {
        return enemy;
    }

    public void setEnemy(Villain enemy) {
        this.enemy = enemy;
    }

    public boolean isInProgress() {
        return inProgress;
    }

    public void setInProgress(boolean inProgress) {
        this.inProgress = inProgress;
    }

    public void endEncounter() {
        this.inProgress = false;
    }
}