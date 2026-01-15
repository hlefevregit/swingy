package com.hulefevr.swingy.service;

import com.hulefevr.swingy.model.map.GameMap;
import com.hulefevr.swingy.model.map.Position;
import com.hulefevr.swingy.model.game.Encounter;
import com.hulefevr.swingy.model.ennemy.Villain;
import com.hulefevr.swingy.model.ennemy.VillainType;
import java.util.Random;

/**
 * Service de génération de maps aléatoires.
 */
public class MapGenerator {
    private Random random = new Random();
    
    /**
     * Génère une nouvelle map de la taille donnée.
     * @param size Taille de la map (size x size)
     * @return GameMap générée avec héros au centre, sortie sur un bord, ennemis aléatoires
     */
    public GameMap generate(int size, int heroLevel) {
        GameMap map = new GameMap(size);
        
        // Placer la sortie sur un bord aléatoire
        Position exit = generateExitPosition(size);
        map.setExitPosition(exit);
        
        // Générer des ennemis aléatoires (environ 10% de la map)
        int enemyCount = Math.max(1, (size * size) / 10);
        for (int i = 0; i < enemyCount; i++) {
            Position pos = generateRandomPosition(size, exit);
            // Ne pas placer d'ennemi au centre (position de départ du héros)
            if (pos.getX() == size/2 && pos.getY() == size/2) {
                continue;
            }
            
            Villain villain = generateVillain(heroLevel);
            Encounter encounter = new Encounter(villain);
            map.addEncounter(pos, encounter);
        }
        
        return map;
    }
    
    /**
     * Génère la position de sortie sur un bord aléatoire.
     */
    private Position generateExitPosition(int size) {
        int side = random.nextInt(4); // 0=top, 1=right, 2=bottom, 3=left
        
        switch (side) {
            case 0: return new Position(random.nextInt(size), 0); // top
            case 1: return new Position(size - 1, random.nextInt(size)); // right
            case 2: return new Position(random.nextInt(size), size - 1); // bottom
            case 3: return new Position(0, random.nextInt(size)); // left
            default: return new Position(0, 0);
        }
    }
    
    /**
     * Génère une position aléatoire qui n'est pas la sortie.
     */
    private Position generateRandomPosition(int size, Position exit) {
        Position pos;
        do {
            pos = new Position(random.nextInt(size), random.nextInt(size));
        } while (pos.equals(exit));
        
        return pos;
    }
    
    /**
     * Génère un ennemi selon le niveau.
     */
    private Villain generateVillain(int level) {
        VillainType type = getVillainTypeForLevel(level);
        return new Villain(type.getDisplayName(), level, type);
    }
    
    /**
     * Retourne le type d'ennemi selon le niveau (basé sur lore.txt).
     */
    private VillainType getVillainTypeForLevel(int level) {
        if (level <= 3) return VillainType.WATCHER;
        if (level <= 7) return random.nextBoolean() ? VillainType.HERALD : VillainType.THURIFER;
        if (level <= 10) {
            int choice = random.nextInt(3);
            if (choice == 0) return VillainType.ARCHON;
            if (choice == 1) return VillainType.VIRTUE;
            return VillainType.DOMINION;
        }
        // Level 11-15
        int choice = random.nextInt(2);
        return choice == 0 ? VillainType.SERAPH : VillainType.PRINCIPALITY;
    }
    
    /**
     * Rend la map sous forme de String pour affichage console.
     * Legend: @=Hero  .=Empty  V=Villain  #=Exit
     */
    public String renderMap(GameMap map, com.hulefevr.swingy.model.hero.Hero hero) {
        int size = map.getSize();
        StringBuilder sb = new StringBuilder();
        
        sb.append("Legend: @=You  .=Empty  V=Villain  #=Exit\n");
        sb.append("┌" + "─".repeat(size * 2 - 1) + "┐\n");
        
        Position heroPos = hero.getPosition();
        
        for (int y = 0; y < size; y++) {
            sb.append("|");
            for (int x = 0; x < size; x++) {
                Position pos = new Position(x, y);
                char tile = '.';
                
                // Afficher le héros à sa vraie position
                if (pos.equals(heroPos)) {
                    tile = '@';
                } else if (pos.equals(map.getExitPosition())) {
                    tile = '#';
                } else if (map.getEncounterAt(pos) != null && !map.getEncounterAt(pos).isResolved()) {
                    tile = 'V';
                }
                
                sb.append(tile);
                if (x < size - 1) sb.append(" ");
            }
            sb.append("|\n");
        }
        
        sb.append("└" + "─".repeat(size * 2 - 1) + "┘\n");
        
        return sb.toString();
    }
}
