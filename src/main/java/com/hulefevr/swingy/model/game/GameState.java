// hero sélectionné, map, état (IN_GAME, GAME_OVER)

package com.hulefevr.swingy.model.game;

import com.hulefevr.swingy.model.hero.Hero;
import com.hulefevr.swingy.model.map.GameMap;

public class GameState {
    public enum State {
        IN_GAME,
        GAME_OVER
    }

    private State currentState;
    private Hero hero;
    private GameMap map;

    public GameState() {
        this.currentState = State.IN_GAME;
    }

    public State getCurrentState() {
        return currentState;
    }

    public void setCurrentState(State currentState) {
        this.currentState = currentState;
    }

    public Hero getHero() {
        return hero;
    }

    public void setHero(Hero hero) {
        this.hero = hero;
    }
    
    public GameMap getMap() {
        return map;
    }
    
    public void setMap(GameMap map) {
        this.map = map;
    }
}