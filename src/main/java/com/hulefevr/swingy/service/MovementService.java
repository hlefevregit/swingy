package com.hulefevr.swingy.service;

import com.hulefevr.swingy.model.hero.Hero;
import com.hulefevr.swingy.model.map.GameMap;
import com.hulefevr.swingy.model.map.Position;
import com.hulefevr.swingy.controller.ControllerResult;

/**
 * Service gérant les déplacements du héros sur la map.
 */
public class MovementService {
    
    /**
     * Déplace le héros dans la direction donnée.
     * @param hero Le héros à déplacer
     * @param direction N, S, E, W
     * @param map La map actuelle
     * @return Résultat du déplacement
     */
    public ControllerResult move(Hero hero, String direction, GameMap map) {
        Position currentPos = hero.getPosition();
        Position newPos = calculateNewPosition(currentPos, direction);
        
        // Vérifier si la nouvelle position est valide
        if (!map.isValidPosition(newPos)) {
            return new ControllerResult(false, false, null, "You cannot go that way. Edge of the world.");
        }
        
        // Déplacer le héros
        hero.setPosition(newPos);
        
        // Vérifier si on a atteint la sortie
        boolean exitReached = map.isExitReached(newPos, hero);
        if (exitReached) {
            return new ControllerResult(false, true, null, "You have reached the edge of the map!");
        }
        
        // Vérifier s'il y a une rencontre
        boolean hasEncounter = map.hasEncounterAt(newPos);
        var encounter = hasEncounter ? map.getEncounterAt(newPos) : null;
        
        // Message selon la direction
        String directionName = getDirectionName(direction);
        String message = "You moved " + directionName + ".";
        
        return new ControllerResult(hasEncounter, false, encounter, message);
    }
    
    /**
     * Calcule la nouvelle position selon la direction.
     */
    private Position calculateNewPosition(Position current, String direction) {
        int x = current.getX();
        int y = current.getY();
        
        switch (direction.toUpperCase()) {
            case "W": y--; break;
            case "S": y++; break;
            case "D": x++; break;
            case "A": x--; break;
            default: break;
        }
        
        return new Position(x, y);
    }
    
    /**
     * Retourne le nom complet de la direction.
     */
    private String getDirectionName(String direction) {
        switch (direction.toUpperCase()) {
            case "W": return "West";
            case "S": return "South";
            case "D": return "East";
            case "A": return "West";
            default: return "somewhere";
        }
    }
}
