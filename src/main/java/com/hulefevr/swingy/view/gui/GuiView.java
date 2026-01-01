package com.hulefevr.swingy.view.gui;

import com.hulefevr.swingy.view.View;
import com.hulefevr.swingy.model.hero.Hero;
import com.hulefevr.swingy.model.game.GameState;
import com.hulefevr.swingy.model.game.Encounter;
import com.hulefevr.swingy.model.game.BattleResult;
import com.hulefevr.swingy.model.artifact.Artifact;
import com.hulefevr.swingy.validation.dto.*;
import java.util.List;
import javax.swing.*;

/**
 * Vue GUI (Swing) - À implémenter complètement plus tard
 */
public class GuiView implements View {
    
    @Override
    public void showMainMenu() {
        // TODO: Implémenter avec Swing
        JOptionPane.showMessageDialog(null, "Main Menu (GUI à implémenter)");
    }

    @Override
    public String promptMenuChoice() {
        // TODO: Implémenter avec Swing
        return JOptionPane.showInputDialog("Choose option:");
    }

    @Override
    public void showHeroList(List<Hero> heroes) {
        // TODO: Implémenter avec Swing
        StringBuilder sb = new StringBuilder("Heroes:\n");
        for (Hero h : heroes) {
            sb.append("- ").append(h.getName()).append("\n");
        }
        JOptionPane.showMessageDialog(null, sb.toString());
    }

    @Override
    public CreateHeroInput promptCreateHero() {
        // TODO: Implémenter avec Swing
        String name = JOptionPane.showInputDialog("Hero name:");
        String type = JOptionPane.showInputDialog("Hero class:");
        return new CreateHeroInput(name, type);
    }

    @Override
    public CreateHeroInput promptSelectHero(List<Hero> heroes) {
        // TODO: Implémenter avec Swing
        return new CreateHeroInput("Default", "Warrior");
    }

    @Override
    public void showGameHud(GameState state) {
        // TODO: Implémenter avec Swing
    }

    @Override
    public void showMap(String render) {
        // TODO: Implémenter avec Swing
        JOptionPane.showMessageDialog(null, render);
    }

    @Override
    public MoveInput promptMove() {
        // TODO: Implémenter avec Swing
        String direction = JOptionPane.showInputDialog("Move (N/S/E/W):");
        return new MoveInput(direction);
    }

    @Override
    public void showEncounter(Encounter encounter) {
        // TODO: Implémenter avec Swing
        JOptionPane.showMessageDialog(null, "Encounter!");
    }

    @Override
    public FightRunInput promptFightOrRun() {
        // TODO: Implémenter avec Swing
        int result = JOptionPane.showConfirmDialog(null, "Fight?", "Battle", JOptionPane.YES_NO_OPTION);
        String choice = (result == JOptionPane.YES_OPTION) ? "F" : "R";
        return new FightRunInput(choice);
    }

    @Override
    public void showBattleResult(BattleResult result) {
        // TODO: Implémenter avec Swing
        String msg = result.isVictory() ? "Victory!" : "Defeat...";
        JOptionPane.showMessageDialog(null, msg);
    }

    @Override
    public LootChoiceInput promptLootChoice(Artifact drop) {
        // TODO: Implémenter avec Swing
        int result = JOptionPane.showConfirmDialog(null, "Take loot: " + drop.getName() + "?", 
            "Loot", JOptionPane.YES_NO_OPTION);
        String choice = (result == JOptionPane.YES_OPTION) ? "T" : "L";
        return new LootChoiceInput(choice);
    }

    @Override
    public void showValidationErrors(List<String> errors) {
        // TODO: Implémenter avec Swing
        StringBuilder sb = new StringBuilder("Errors:\n");
        for (String error : errors) {
            sb.append("- ").append(error).append("\n");
        }
        JOptionPane.showMessageDialog(null, sb.toString(), "Errors", JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void showMessage(String message) {
        JOptionPane.showMessageDialog(null, message);
    }

    @Override
    public String promptSelectHeroChoice() {
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