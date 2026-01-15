package com.hulefevr.swingy.view.gui;

import com.hulefevr.swingy.view.View;
import com.hulefevr.swingy.model.hero.Hero;
import com.hulefevr.swingy.model.game.GameState;
import com.hulefevr.swingy.model.game.Encounter;
import com.hulefevr.swingy.model.game.BattleResult;
import com.hulefevr.swingy.model.artifact.Artifact;
import com.hulefevr.swingy.validation.dto.*;
import java.util.List;
import java.awt.Dialog;
import javax.swing.*;

/**
 * Vue GUI (Swing) - À implémenter complètement plus tard
 */
public class GuiView implements View {
    private GuiWindow window;
    private com.hulefevr.swingy.model.ennemy.Villain currentVillain;
    private com.hulefevr.swingy.model.hero.Hero currentHero;
    private boolean splashShown = false;

    public GuiView() {
        System.out.println("DEBUG: GuiView constructor called");
        // Initialize Swing window on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            try {
                System.out.println("DEBUG: Creating GuiWindow...");
                window = new GuiWindow();
                window.setVisible(true);
                window.showSplash();
                System.out.println("DEBUG: GuiWindow created and visible");
            } catch (Exception e) {
                System.err.println("ERROR: Failed to create GuiWindow!");
                e.printStackTrace();
            }
        });
    }

    @Override
    public void close() {
        // Dispose the main window on the EDT to allow the JVM to exit
        if (window != null) {
            SwingUtilities.invokeLater(() -> {
                try {
                    window.dispose();
                } catch (Exception ignored) {
                }
            });
        }
    }
    
    @Override
    public void showMainMenu() {
        // Switch to the main menu panel in the window if available
        if (window != null) {
            window.showMainMenu();
        } else {
            JOptionPane.showMessageDialog(null, "Main Menu (GUI à implémenter)");
        }
    }

    @Override
    public String promptMenuChoice() {
        System.out.println("DEBUG: promptMenuChoice called, splashShown=" + splashShown);
        // La première fois, attendre que le splash soit passé
        if (!splashShown) {
            splashShown = true;
            System.out.println("DEBUG: Waiting for window to initialize...");
            // Attendre que la fenêtre soit initialisée
            int maxWait = 40; // Maximum 20 secondes (40 * 500ms)
            int waited = 0;
            while (window == null && waited < maxWait) {
                try {
                    Thread.sleep(500);
                    waited++;
                    if (waited % 4 == 0) {
                        System.out.println("DEBUG: Still waiting... (" + (waited / 2) + "s)");
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
            
            if (window == null) {
                System.err.println("ERROR: GUI window failed to initialize after " + (waited / 2) + " seconds!");
                return null;
            }
            
            System.out.println("DEBUG: Window initialized, waiting for splash to be visible (2.5s)...");
            // Laisser le temps au splash de s'afficher
            try {
                Thread.sleep(2500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        System.out.println("DEBUG: Calling showMainMenuAndWait()...");
        // Show main menu inside the main window and wait for selection.
        // This method should not be called on the EDT because it blocks until user chooses.
        try {
            String result = window.showMainMenuAndWait();
            System.out.println("DEBUG: showMainMenuAndWait returned: " + result);
            return result;
        } catch (Exception e) {
            System.err.println("ERROR in showMainMenuAndWait: " + e.getMessage());
            e.printStackTrace();
            // fallback to simple dialog if something goes wrong
            return JOptionPane.showInputDialog("Choose option:");
        }
    }

    @Override
    public void showHeroList(List<Hero> heroes) {
        // In GUI mode, prefer showing the selection panel inside the main window.
        if (window != null) {
            // we'll rely on promptSelectHero to display the list and return user's choice
            return;
        }
        StringBuilder sb = new StringBuilder("Heroes:\n");
        for (Hero h : heroes) {
            sb.append("- ").append(h.getName()).append("\n");
        }
        JOptionPane.showMessageDialog(null, sb.toString());
    }

    @Override
    public CreateHeroInput promptCreateHero() {
        // Show the create-hero panel inside the main window and block until user submits.
        if (window != null) {
            try {
                com.hulefevr.swingy.validation.dto.CreateHeroInput input = window.showCreateHeroAndWait();
                return input;
            } catch (Exception e) {
                // fallback to simple dialogs
            }
        }
        String name = JOptionPane.showInputDialog("Hero name:");
        String type = JOptionPane.showInputDialog("Hero class:");
        return new CreateHeroInput(name, type);
    }

    @Override
    public String promptSelectHero(List<Hero> heroes) {
        if (window != null) {
            try {
                return window.showSelectHeroAndWait(heroes);
            } catch (Exception e) {
                // fallback
            }
        }
        return JOptionPane.showInputDialog("Select hero number (or 0 to go back):");
    }

    @Override
    public void showHeroDetails(Hero hero) {
        if (window != null) {
            window.showHeroSheet(hero);
            return;
        }
        // fallback to dialog
        StringBuilder sb = new StringBuilder();
        sb.append("Name: ").append(hero.getName()).append("    Class: ").append(hero.getHeroClass()).append("\n");
        sb.append("Level: ").append(hero.getLevel()).append("   XP: ").append(hero.getXp()).append("/ ").append(hero.getXpForNextLevel()).append("\n\n");
        sb.append("Attack: ").append(hero.getAttack()).append("   Defense: ").append(hero.getDefense()).append("   HP: ").append(hero.getHitPoints()).append("/").append(hero.getMaxHitPoints()).append("\n\n");
        sb.append("Relics Equipped:\n");
        sb.append(" Weapon: ").append(hero.getWeapon() != null ? hero.getWeapon().getName() : "None").append("\n");
        sb.append(" Armor : ").append(hero.getArmor() != null ? hero.getArmor().getName() : "None").append("\n");
        sb.append(" Helm  : ").append(hero.getHelm() != null ? hero.getHelm().getName() : "None").append("\n");
        JOptionPane.showMessageDialog(null, sb.toString());
    }


    @Override
    public void showGameHud(GameState state) {
        if (window != null) {
            if (state != null && state.getHero() != null) {
                this.currentHero = state.getHero();
            }
            window.showGame(state);
            return;
        }
    }

    @Override
    public void showMap(String render) {
        // La map est affichée graphiquement dans le GamePanel
        // On peut ignorer le rendu texte pour la GUI
        // Ou l'ajouter comme message si nécessaire pour le debug
        if (window != null) {
            // Ne rien faire, la map est rendue graphiquement
            return;
        }
        JOptionPane.showMessageDialog(null, render);
    }

    @Override
    public MoveInput promptMove() {
        if (window != null) {
            // Utiliser le système d'attente du GamePanel
            String direction = window.waitForMoveInput();
            return new MoveInput(direction);
        }
        String direction = JOptionPane.showInputDialog("Move (N/S/E/W):");
        return new MoveInput(direction);
    }

    @Override
    public void showEncounter(Encounter encounter) {
        if (window != null) {
            String message = "⚔ Encounter: " + encounter.getEnemy().getName() + " (Level " + encounter.getEnemy().getLevel() + ")";
            window.addGameMessage(message);
            // Stocker l'ennemi pour l'EncounterPanel
            this.currentVillain = encounter.getEnemy();
            return;
        }
        JOptionPane.showMessageDialog(null, "Encounter!");
    }

    @Override
    public FightRunInput promptFightOrRun() {
        if (window != null && currentHero != null && currentVillain != null) {
            // Utiliser l'EncounterPanel
            String choice = window.showEncounterAndWait(currentHero, currentVillain);
            // Retourner au jeu après le choix
            window.returnToGame();
            return new FightRunInput(choice);
        }
        if (window != null) {
            String[] opts = new String[]{"F", "R"};
            String choice = window.showMessageAndWait("Battle", "Fight or Run?", opts);
            if (choice == null) choice = "R";
            return new FightRunInput(choice);
        }
        int result = JOptionPane.showConfirmDialog(null, "Fight?", "Battle", JOptionPane.YES_NO_OPTION);
        String choice = (result == JOptionPane.YES_OPTION) ? "F" : "R";
        return new FightRunInput(choice);
    }

    @Override
    public void showBattleResult(BattleResult result) {
        String msg = result.isVictory() ? "Victory!" : "Defeat...";
        if (window != null) {
            // Ajouter le message dans le GamePanel au lieu d'afficher un dialogue séparé
            String fullMsg = msg + (result.isVictory() ? (" +" + result.getXpGained() + " XP gained!") : "");
            window.addGameMessage(fullMsg);
            // S'assurer qu'on est bien sur le GamePanel
            window.returnToGame();
            return;
        }
        JOptionPane.showMessageDialog(null, msg);
    }

    @Override
    public LootChoiceInput promptLootChoice(Artifact drop) {
        if (window != null) {
            String choice = window.showLootAndWait(drop);
            if (choice == null) choice = "L";
            window.returnToGame(); // Retour au GamePanel
            return new LootChoiceInput(choice);
        }
        int result = JOptionPane.showConfirmDialog(null, "Take loot: " + drop.getName() + "?", 
            "Loot", JOptionPane.YES_NO_OPTION);
        String choice = (result == JOptionPane.YES_OPTION) ? "T" : "L";
        return new LootChoiceInput(choice);
    }

    @Override
    public void showValidationErrors(List<String> errors) {
        StringBuilder sb = new StringBuilder();
        for (String error : errors) {
            sb.append("- ").append(error).append("\n");
        }
        if (window != null) {
            window.showMessageAndWait("Errors", sb.toString(), new String[]{"OK"});
            return;
        }
        JOptionPane.showMessageDialog(null, sb.toString(), "Errors", JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void showMessage(String message) {
        if (window != null) {
            // Ajouter le message dans le GamePanel au lieu d'un dialogue
            window.addGameMessage(message);
            return;
        }
        JOptionPane.showMessageDialog(null, message);
    }

    @Override
    public String promptSelectHeroChoice() {
        if (window != null) {
            return window.showInputAndWait("Select Hero", "Select hero number (or 0 to go back):");
        }
        return JOptionPane.showInputDialog("Select hero number (or 0 to go back):");
    }
    
    @Override
    public void showLevelUp(int newLevel, String message) {
        if (window != null) {
            window.showLevelUpAndWait(newLevel, message);
        } else {
            JOptionPane.showMessageDialog(null, message + "\n\nNew Level: " + newLevel);
        }
    }

    // Méthodes spécifiques GUI (anciennes)
    public void renderMap(char[][] map) {
        // Ancienne méthode - peut être supprimée ou adaptée
    }

    public void renderHUD(String hud) {
        // Ancienne méthode - peut être supprimée ou adaptée
    }

    public void renderError(String[] errors) {
        // Ancienne méthode - peut être supprimée ou adaptée
    }
    
    /**
     * Affiche l'intro du boss final (The Threshold)
     */
    public void showFinalBossIntro() {
        if (window != null) {
            window.showFinalBossIntroAndWait();
        } else {
            JOptionPane.showMessageDialog(null, "THE THRESHOLD\n\n\"You climbed. You endured. You were never forgiven.\"");
        }
    }
    
    /**
     * Affiche le combat du boss final
     */
    public void showFinalBossBattle(Hero hero, String message) {
        if (window != null) {
            window.showFinalBossBattleAndWait(hero, message);
        } else {
            JOptionPane.showMessageDialog(null, message);
        }
    }
    
    /**
     * Affiche l'écran de fin et retourne le choix de l'utilisateur
     */
    public String showEnding() {
        if (window != null) {
            return window.showEndingAndWait();
        } else {
            int choice = JOptionPane.showConfirmDialog(null, 
                "THE FALL IS ETERNAL\n\n\"This is not punishment. This is correction.\"\n\nReturn to menu?", 
                "Ending", 
                JOptionPane.YES_NO_OPTION);
            return (choice == JOptionPane.YES_OPTION) ? "MENU" : "QUIT";
        }
    }
}