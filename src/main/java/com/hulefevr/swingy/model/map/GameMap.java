package com.hulefevr.swingy.model.map;

import com.hulefevr.swingy.model.game.Encounter;
import java.util.HashMap;
import java.util.Map;

/**
 * Représente la carte du jeu avec les positions des ennemis et de la sortie.
 */
public class GameMap {
    private int size;
    private Position exitPosition;
    private Map<Position, Encounter> encounters;
    
    public GameMap(int size) {
        this.size = size;
        this.encounters = new HashMap<>();
    }
    
    public int getSize() {
        return size;
    }
    
    public Position getExitPosition() {
        return exitPosition;
    }
    
    public void setExitPosition(Position exitPosition) {
        this.exitPosition = exitPosition;
    }
    
    /**
     * Ajoute une rencontre à une position donnée.
     */
    public void addEncounter(Position position, Encounter encounter) {
        encounters.put(position, encounter);
    }
    
    /**
     * Récupère la rencontre à une position donnée.
     * @return l'encounter ou null si aucune rencontre
     */
    public Encounter getEncounterAt(Position position) {
        return encounters.get(position);
    }
    
    /**
     * Vérifie s'il y a une rencontre à la position donnée.
     */
    public boolean hasEncounterAt(Position position) {
        return encounters.containsKey(position) && encounters.get(position) != null;
    }
    
    /**
     * Supprime la rencontre à une position donnée.
     */
    public void removeEncounter(Position position) {
        encounters.remove(position);
    }
    
    /**
     * Vérifie si la position donnée est la sortie.
     */
    public boolean isExitReached(Position position, com.hulefevr.swingy.model.hero.Hero hero) {
        return exitPosition != null && exitPosition.equals(position);
    }
    
    /**
     * Vérifie si une position est valide (dans les limites de la map).
     */
    public boolean isValidPosition(Position position) {
        return position.getX() >= 0 && position.getX() < size 
            && position.getY() >= 0 && position.getY() < size;
    }
}
