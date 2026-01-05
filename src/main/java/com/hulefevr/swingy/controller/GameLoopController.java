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
            
            while (!levelComplete && !playerDead && !playerQuit) {
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
                        playerDead = handleEncounter(hero, encounter, gameState);
                        
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
            } else if (levelComplete) {
                // Level up si assez d'XP
                if (checkLevelUp(hero)) {
                    showLevelUpScreen(hero);
                    
                    // Vérifier si level 15 atteint
                    if (hero.getLevel() >= 15) {
                        showFinalBoss(hero);
                        gameRunning = false;
                    }
                } else {
                    // Pas assez d'XP, continuer sur la même map
                    view.showMessage("\nYou need more experience to ascend...");
                    view.showMessage("Press Enter to continue exploring...");
                    view.promptMenuChoice();
                }
            }
        }
    }
    
    /**
     * Gère une rencontre avec un ennemi.
     * @return true si le joueur est mort, false sinon
     */
    private boolean handleEncounter(Hero hero, Encounter encounter, GameState gameState) {
        // Demander fight ou run
        var choice = view.promptFightOrRun();
        
        if (choice.getChoice().equalsIgnoreCase("R")) {
            // Tentative de fuite
            boolean escaped = battleService.attemptRun(hero, encounter.getEnemy());
            if (escaped) {
                view.showMessage("You fled successfully.");
                encounter.setResolved(true);
                return false;
            } else {
                view.showMessage("You failed to run! Combat begins.");
            }
        }
        
        // Combat
        var battleResult = battleService.fight(hero, encounter.getEnemy());
        view.showBattleResult(battleResult);
        
        if (!battleResult.isVictory()) {
            return true; // Joueur mort
        }
        
        // Victoire : gagner XP
        hero.gainExperience(battleResult.getXpGained());
        view.showMessage("+" + battleResult.getXpGained() + " XP gained!");
        
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
        return false;
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
     */
    private void showLevelUpScreen(Hero hero) {
        int oldLevel = hero.getLevel();
        hero.levelUp();
        
        view.showMessage("\n" + "═".repeat(50));
        
        // Messages narratifs selon le niveau
        if (hero.getLevel() == 2) {
            view.showMessage("You remember your wings.");
            view.showMessage("They were not given.");
            view.showMessage("They were earned.");
        } else if (hero.getLevel() == 5) {
            view.showMessage("The Light no longer burns.");
            view.showMessage("It recoils.");
            view.showMessage("\nYou ascend.");
        } else if (hero.getLevel() == 10) {
            view.showMessage("The Choir falls silent.");
            view.showMessage("They know your name now.");
            view.showMessage("\nYou are no longer fleeing.");
            view.showMessage("You are returning.");
        } else {
            view.showMessage("The Trial continues.");
            view.showMessage("You grow stronger.");
        }
        
        view.showMessage("\n✧ LEVEL ASCENDED ✧");
        view.showMessage("Level " + oldLevel + " → " + hero.getLevel());
        view.showMessage("New map size: " + hero.getMapSize() + "x" + hero.getMapSize());
        view.showMessage("═".repeat(50) + "\n");
        
        view.showMessage("Press Enter to continue...");
        view.promptMenuChoice();
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
