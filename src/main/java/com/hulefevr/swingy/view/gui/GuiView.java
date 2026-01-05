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

    public GuiView() {
        // Initialize Swing window on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            window = new GuiWindow();
            window.setVisible(true);
            window.showSplash();
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
        // Show a modal dialog containing the MainMenuPanel and return the user's choice
        final String[] result = new String[1];

        // Show main menu inside the main window and wait for selection.
        // This method should not be called on the EDT because it blocks until user chooses.
        try {
            return window.showMainMenuAndWait();
        } catch (Exception e) {
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
            StringBuilder sb = new StringBuilder();
            if (state.getHero() != null) {
                sb.append("Hero: ").append(state.getHero().getName()).append("\n");
                sb.append("Level: ").append(state.getHero().getLevel()).append("\n");
                sb.append("XP: ").append(state.getHero().getXp()).append("\n");
            }
            window.showMessageNonBlocking("HUD", sb.toString());
            return;
        }
    }

    @Override
    public void showMap(String render) {
        if (window != null) {
            window.showMessageNonBlocking("Map", render);
            return;
        }
        JOptionPane.showMessageDialog(null, render);
    }

    @Override
    public MoveInput promptMove() {
        if (window != null) {
            String[] opts = new String[]{"N", "S", "E", "W", "Q"};
            String choice = window.showMessageAndWait("Move", "Choose direction:", opts);
            if (choice == null) return new MoveInput("Q");
            return new MoveInput(choice);
        }
        String direction = JOptionPane.showInputDialog("Move (N/S/E/W):");
        return new MoveInput(direction);
    }

    @Override
    public void showEncounter(Encounter encounter) {
        if (window != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("You encountered: ").append(encounter.getEnemy().getName()).append("\n");
            window.showMessageNonBlocking("Encounter", sb.toString());
            return;
        }
        JOptionPane.showMessageDialog(null, "Encounter!");
    }

    @Override
    public FightRunInput promptFightOrRun() {
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
            window.showMessageNonBlocking("Battle Result", msg + (result.isVictory() ? ("\n+" + result.getXpGained() + " XP gained!") : ""));
            return;
        }
        JOptionPane.showMessageDialog(null, msg);
    }

    @Override
    public LootChoiceInput promptLootChoice(Artifact drop) {
        if (window != null) {
            String[] opts = new String[]{"T", "L"};
            String choice = window.showMessageAndWait("Loot", "Take loot: " + drop.getName() + "?", opts);
            if (choice == null) choice = "L";
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
            window.showMessageNonBlocking(null, message);
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
}