package com.hulefevr.swingy.controller;

import com.hulefevr.swingy.model.hero.Hero;
import com.hulefevr.swingy.model.game.GameState;
import com.hulefevr.swingy.model.map.GameMap;
import com.hulefevr.swingy.model.map.Position;
import com.hulefevr.swingy.model.game.Encounter;
import com.hulefevr.swingy.persistence.HeroRepository;
import com.hulefevr.swingy.service.MapGenerator;
import com.hulefevr.swingy.service.MovementService;
import com.hulefevr.swingy.service.BattleService;
import com.hulefevr.swingy.service.LootService;
import com.hulefevr.swingy.view.View;
import com.hulefevr.swingy.validation.dto.MoveInput;

/**
 * Contrôleur principal de la boucle de jeu.
 * Gère le cycle : affichage -> input -> mouvement -> rencontre -> combat -> loot
 */
public class GameLoopController {
    private final View view;
    private final HeroRepository heroRepository;
    private final MapGenerator mapGenerator;
    private final MovementService movementService;
    private final BattleService battleService;
    private final LootService lootService;
    
    public GameLoopController(View view, HeroRepository heroRepository) {
        this.view = view;
        this.heroRepository = heroRepository;
        this.mapGenerator = new MapGenerator();
        this.movementService = new MovementService();
    this.battleService = new BattleService();
    this.lootService = new LootService(heroRepository);
    }
    
    /**
     * Lance la boucle de jeu principale avec le héros sélectionné.
     * @param hero Le héros qui va jouer
     */
    public void startGameLoop(Hero hero) {
        // Initialiser l'état du jeu
        GameState gameState = new GameState();
        gameState.setHero(hero);
        
        // Message d'intro narratif
        showIntroduction(hero);
        
        // Boucle principale du jeu
        boolean gameRunning = true;
        while (gameRunning) {
            // Générer une nouvelle map pour ce niveau
            GameMap map = mapGenerator.generate(hero.getMapSize());
            gameState.setMap(map);
            
            // Placer le héros au centre de la map
            int mapSize = hero.getMapSize();
            hero.setPosition(new Position(mapSize / 2, mapSize / 2));
            
            // Message de niveau
            showLevelMessage(hero.getLevel());
            
            // Boucle de la map actuelle
            boolean levelComplete = false;
            boolean playerDead = false;
            boolean playerQuit = false;
            boolean gameBeat = false; // Niveau 15 atteint
            
            while (!levelComplete && !playerDead && !playerQuit && !gameBeat) {
                // 1. Afficher l'état actuel (HUD + Map)
                view.showGameHud(gameState);
                view.showMap(mapGenerator.renderMap(map, hero));
                
                // 2. Demander au joueur de bouger
                MoveInput moveInput = view.promptMove();
                
                // Vérifier si le joueur veut quitter
                if (moveInput.getDirection().equalsIgnoreCase("Q")) {
                    playerQuit = true;
                    view.showMessage("\n⚠ Saving and exiting...");
                    saveHero(hero);
                    break;
                }
                
                // 3. Traiter le mouvement
                ControllerResult moveResult = movementService.move(hero, moveInput.getDirection(), map);
                
                view.showMessage(moveResult.getMessage());
                
                // 4. Vérifier si sortie atteinte
                if (moveResult.isExitReached()) {
                    levelComplete = true;
                    view.showMessage("\n✓ You reached the exit!");
                    continue;
                }
                
                // 5. Vérifier rencontre avec ennemi
                if (moveResult.hasEncounter()) {
                    Encounter encounter = moveResult.getEncounter();
                    if (encounter != null && !encounter.isResolved()) {
                        // Afficher la rencontre
                        view.showEncounter(encounter);
                        
                        // Gérer le combat
                        var encounterResult = handleEncounter(hero, encounter, gameState);
                        playerDead = encounterResult.isPlayerDead();
                        gameBeat = encounterResult.isGameBeat();
                        
                        // Rafraîchir l'affichage après le combat
                        view.showGameHud(gameState);
                    }
                }
            }
            
            // Vérifier fin du jeu
            if (playerQuit) {
                // Le joueur a quitté volontairement
                gameRunning = false;
            } else if (playerDead) {
                showDefeatScreen();
                gameRunning = false;
            } else if (gameBeat) {
                // Niveau 15 atteint, jeu terminé
                gameRunning = false;
            } else if (levelComplete) {
                // Le héros a atteint la sortie - continuer au niveau suivant
                // Note: Le level up se fait automatiquement dans gainExperience() pendant les combats
                System.out.println("DEBUG: Exit reached, hero level=" + hero.getLevel());
                
                // La map sera regénérée pour le niveau actuel dans la prochaine itération
                // Pas besoin de vérifier checkLevelUp() ici car c'est géré pendant les combats
            }
        }
    }
    
    /**
     * Gère une rencontre avec un ennemi.
     * @return résultat de la rencontre (mort du joueur, jeu terminé)
     */
    private EncounterResult handleEncounter(Hero hero, Encounter encounter, GameState gameState) {
        // Demander fight ou run
        var choice = view.promptFightOrRun();
        
        if (choice.getChoice().equalsIgnoreCase("R")) {
            // Tentative de fuite
            boolean escaped = battleService.attemptRun(hero, encounter.getEnemy());
            if (escaped) {
                view.showMessage("You fled successfully.");
                encounter.setResolved(true);
                return new EncounterResult(false, false);
            } else {
                view.showMessage("You failed to run! Combat begins.");
            }
        }
        
        // Combat
        var battleResult = battleService.fight(hero, encounter.getEnemy());
        view.showBattleResult(battleResult);
        
        if (!battleResult.isVictory()) {
            return new EncounterResult(true, false); // Joueur mort
        }
        
        // Victoire : gagner XP
        int oldLevel = hero.getLevel();
        int oldXp = hero.getXp();
        System.out.println("DEBUG: Before gainExperience - Level: " + oldLevel + ", XP: " + oldXp + "/" + hero.getXpForNextLevel());
        boolean didLevelUp = hero.gainExperience(battleResult.getXpGained());
        System.out.println("DEBUG: After gainExperience - Level: " + hero.getLevel() + ", XP: " + hero.getXp() + "/" + hero.getXpForNextLevel() + ", didLevelUp: " + didLevelUp);
        view.showMessage("+" + battleResult.getXpGained() + " XP gained!");
        
        // Vérifier si level up pendant le combat
        if (didLevelUp) {
            System.out.println("DEBUG: Hero leveled up during battle! " + oldLevel + " -> " + hero.getLevel());
            showLevelUpScreen(hero);
            
            // Vérifier si level 15 atteint
            if (hero.getLevel() >= 15) {
                showFinalBoss(hero);
                encounter.setResolved(true);
                return new EncounterResult(false, true); // Jeu terminé
            }
        }
        
        // Loot potentiel
        if (battleResult.getLootDropped() != null) {
            var lootChoice = view.promptLootChoice(battleResult.getLootDropped());
            if (lootChoice.getChoice().equalsIgnoreCase("T")) {
                lootService.equipArtifact(hero, battleResult.getLootDropped());
                view.showMessage("✓ " + battleResult.getLootDropped().getName() + " equipped!");
            } else {
                view.showMessage("Left behind.");
            }
        }
        
        encounter.setResolved(true);
        return new EncounterResult(false, false);
    }
    
    /**
     * Classe interne pour retourner le résultat d'une rencontre
     */
    private static class EncounterResult {
        private final boolean playerDead;
        private final boolean gameBeat;
        
        public EncounterResult(boolean playerDead, boolean gameBeat) {
            this.playerDead = playerDead;
            this.gameBeat = gameBeat;
        }
        
        public boolean isPlayerDead() { return playerDead; }
        public boolean isGameBeat() { return gameBeat; }
    }
    
    /**
     * Vérifie si le héros peut level up.
     */
    private boolean checkLevelUp(Hero hero) {
        int xpNeeded = hero.getXpForNextLevel();
        return hero.getXp() >= xpNeeded;
    }
    
    /**
     * Affiche l'écran de level up avec message narratif.
     * NOTE: Cette méthode N'APPELLE PAS hero.levelUp() - le niveau doit déjà avoir été incrémenté.
     */
    private void showLevelUpScreen(Hero hero) {
        int currentLevel = hero.getLevel();
        
        System.out.println("DEBUG: showLevelUpScreen called - current level: " + currentLevel);
        
        // Construire le message narratif selon le niveau
        StringBuilder message = new StringBuilder();
        
        if (hero.getLevel() == 2) {
            message.append("You remember your wings.\n");
            message.append("They were not given.\n");
            message.append("They were earned.");
        } else if (hero.getLevel() == 5) {
            message.append("The Light no longer burns.\n");
            message.append("It recoils.\n\n");
            message.append("You ascend.");
        } else if (hero.getLevel() == 10) {
            message.append("The Choir falls silent.\n");
            message.append("They know your name now.\n\n");
            message.append("You are no longer fleeing.\n");
            message.append("You are returning.");
        } else {
            message.append("The Trial continues.\n");
            message.append("You grow stronger.");
        }
        
        // Afficher l'écran épique de level up (GUI) ou le message (console)
        System.out.println("DEBUG: Calling view.showLevelUp with level " + hero.getLevel());
        view.showLevelUp(hero.getLevel(), message.toString());
        System.out.println("DEBUG: view.showLevelUp returned");
        
        // Pour la console, afficher les infos supplémentaires
        if (!(view instanceof com.hulefevr.swingy.view.gui.GuiView)) {
            view.showMessage("\n✧ LEVEL ASCENDED ✧");
            view.showMessage("New Level: " + hero.getLevel());
            view.showMessage("New map size: " + hero.getMapSize() + "x" + hero.getMapSize());
            view.showMessage("═".repeat(50) + "\n");
            view.showMessage("Press Enter to continue...");
            view.promptMenuChoice();
        }
    }
    
    /**
     * Affiche l'introduction narrative.
     */
    private void showIntroduction(Hero hero) {
        view.showMessage("\n" + "═".repeat(50));
        view.showMessage("You were not cast down.");
        view.showMessage("You were erased.");
        view.showMessage("");
        view.showMessage("Your name was struck from the Choir.");
        view.showMessage("Your wings burned, but did not break.");
        view.showMessage("");
        view.showMessage("Between Heaven and the Abyss lies the Trial —");
        view.showMessage("a shifting land, watched by Angels who do not forgive.");
        view.showMessage("");
        view.showMessage("Reach the edge of this world.");
        view.showMessage("Defy the Judgment.");
        view.showMessage("Remember who you were.");
        view.showMessage("═".repeat(50));
        view.showMessage("\nPress Enter to begin your descent...");
        view.promptMenuChoice();
    }
    
    /**
     * Affiche le message narratif du niveau.
     */
    private void showLevelMessage(int level) {
        String zoneName = getZoneName(level);
        view.showMessage("\n━━━ " + zoneName + " ━━━\n");
    }
    
    /**
     * Retourne le nom de la zone selon le niveau.
     */
    private String getZoneName(int level) {
        if (level <= 3) return "Plains of Ash";
        if (level <= 6) return "Fields of Chains";
        if (level <= 9) return "Vaults of Judgment";
        if (level <= 12) return "The Silent Choir";
        return "The Threshold";
    }
    
    /**
     * Affiche l'écran de défaite.
     */
    private void showDefeatScreen() {
        view.showMessage("\n" + "═".repeat(50));
        view.showMessage("Your wings finally burn to ash.");
        view.showMessage("The Judgment is complete.");
        view.showMessage("═".repeat(50));
        view.showMessage("\nGAME OVER");
        view.showMessage("\nPress Enter to return to menu...");
        view.promptMenuChoice();
    }
    
    /**
     * Affiche la séquence du boss final (niveau 15).
     */
    private void showFinalBoss(Hero hero) {
        view.showMessage("\n" + "═".repeat(50));
        view.showMessage("The land ends.");
        view.showMessage("");
        view.showMessage("No ash.");
        view.showMessage("No chains.");
        view.showMessage("No walls.");
        view.showMessage("");
        view.showMessage("Only light — vast, silent, absolute.");
        view.showMessage("");
        view.showMessage("You have ascended.");
        view.showMessage("═".repeat(50));
        view.showMessage("\nPress Enter...");
        view.promptMenuChoice();
        
        view.showMessage("\nYou expect battle.");
        view.showMessage("You expect resistance.");
        view.showMessage("");
        view.showMessage("None comes.");
        view.showMessage("");
        view.showMessage("The Choir does not sing.");
        view.showMessage("The gates do not close.");
        view.showMessage("");
        view.showMessage("They were never meant to stop you.");
        view.showMessage("\nPress Enter...");
        view.promptMenuChoice();
        
        view.showMessage("\n" + "═".repeat(50));
        view.showMessage("          THE FALL IS ETERNAL");
        view.showMessage("═".repeat(50));
        view.showMessage("");
        view.showMessage("You reached Heaven.");
        view.showMessage("");
        view.showMessage("Heaven rejected you.");
        view.showMessage("");
        view.showMessage("The Trial continues.");
        view.showMessage("");
        view.showMessage("═".repeat(50));
        view.showMessage("\nThank you for playing.");
        view.showMessage("\nPress Enter to return to menu...");
        view.promptMenuChoice();
    }
    
    /**
     * Sauvegarde le héros dans la base de données.
     */
    private void saveHero(Hero hero) {
        try {
            heroRepository.save(hero);
            view.showMessage("✓ Progress saved!");
        } catch (Exception e) {
            view.showMessage("✗ Error saving: " + e.getMessage());
        }
    }
}
