package com.hulefevr.swingy.view.gui.screens;

import com.hulefevr.swingy.model.game.GameState;
import com.hulefevr.swingy.model.game.Encounter;
import com.hulefevr.swingy.model.hero.Hero;
import com.hulefevr.swingy.model.hero.HeroClass;
import com.hulefevr.swingy.model.map.GameMap;
import com.hulefevr.swingy.model.map.Position;
import com.hulefevr.swingy.model.ennemy.Villain;
import com.hulefevr.swingy.model.ennemy.VillainType;

import javax.swing.*;

/**
 * Test simple pour visualiser le GamePanel
 */
public class GamePanelDemo {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Créer la fenêtre
            JFrame frame = new JFrame("GamePanel Demo - The Book of the Fallen");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1200, 800);
            frame.setLocationRelativeTo(null);
            
            // Créer le GamePanel
            GamePanel gamePanel = new GamePanel();
            
            // Créer un état de jeu de test
            GameState gameState = createTestGameState();
            
            // Mettre à jour le panneau avec l'état de test
            gamePanel.updateGameState(gameState);
            
            // Ajouter quelques messages de test
            gamePanel.addMessage("Vous entrez dans les Terres désolées...");
            gamePanel.addMessage("Un Watcher vous observe de loin.");
            gamePanel.addMessage("Vous ressentez une présence menaçante.");
            
            // Ajouter des actions aux boutons
            gamePanel.setNorthAction(e -> {
                moveHero(gameState, 0, -1);
                gamePanel.updateGameState(gameState);
                gamePanel.addMessage("Vous avez bougé vers le Nord");
            });
            
            gamePanel.setEastAction(e -> {
                moveHero(gameState, 1, 0);
                gamePanel.updateGameState(gameState);
                gamePanel.addMessage("Vous avez bougé vers l'Est");
            });
            
            gamePanel.setSouthAction(e -> {
                moveHero(gameState, 0, 1);
                gamePanel.updateGameState(gameState);
                gamePanel.addMessage("Vous avez bougé vers le Sud");
            });
            
            gamePanel.setWestAction(e -> {
                moveHero(gameState, -1, 0);
                gamePanel.updateGameState(gameState);
                gamePanel.addMessage("Vous avez bougé vers l'Ouest");
            });
            
            gamePanel.setStatsAction(e -> {
                gamePanel.addMessage("=== Stats du Héros ===");
                Hero hero = gameState.getHero();
                gamePanel.addMessage(String.format("Niveau: %d, XP: %d/%d", 
                    hero.getLevel(), hero.getXp(), hero.getXpForNextLevel()));
            });
            
            gamePanel.setMenuAction(e -> {
                gamePanel.addMessage("Retour au menu...");
            });
            
            gamePanel.setSwitchViewAction(e -> {
                gamePanel.addMessage("Changement de vue vers Console...");
            });
            
            // Ajouter le panneau à la fenêtre
            frame.add(gamePanel);
            
            // Afficher la fenêtre
            frame.setVisible(true);
        });
    }
    
    private static GameState createTestGameState() {
        // Créer un héros de test
        Hero hero = new Hero("Happy", HeroClass.EXILE, 20, 15, 100);
        hero.setLevel(7);
        hero.setExperience(7000);
        hero.setCurrentHitPoints(55);
        
        // Créer une map de test
        int mapSize = 15;
        GameMap map = new GameMap(mapSize);
        
        // Positionner le héros au centre
        Position heroPos = new Position(mapSize / 2, mapSize / 2);
        hero.setPosition(heroPos);
        
        // Ajouter quelques ennemis
        addEnemy(map, mapSize / 2 - 2, mapSize / 2 - 3, VillainType.WATCHER, 3);
        addEnemy(map, mapSize / 2 + 3, mapSize / 2 + 1, VillainType.HERALD, 5);
        addEnemy(map, mapSize / 2 - 1, mapSize / 2 + 2, VillainType.THURIFER, 4);
        addEnemy(map, mapSize / 2 + 2, mapSize / 2 - 2, VillainType.ARCHON, 8);
        
        // Définir la sortie
        map.setExitPosition(new Position(mapSize - 2, mapSize - 2));
        
        // Créer l'état du jeu
        GameState state = new GameState();
        state.setHero(hero);
        state.setMap(map);
        state.setCurrentState(GameState.State.IN_GAME);
        
        return state;
    }
    
    private static void addEnemy(GameMap map, int x, int y, VillainType type, int level) {
        Villain villain = new Villain(type.getDisplayName(), level, type);
        Encounter encounter = new Encounter(villain);
        map.addEncounter(new Position(x, y), encounter);
    }
    
    private static void moveHero(GameState state, int dx, int dy) {
        Hero hero = state.getHero();
        Position currentPos = hero.getPosition();
        Position newPos = new Position(currentPos.getX() + dx, currentPos.getY() + dy);
        
        if (state.getMap().isValidPosition(newPos)) {
            hero.setPosition(newPos);
        }
    }
}
