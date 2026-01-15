package com.hulefevr.swingy.view.console;

import com.hulefevr.swingy.view.View;
import com.hulefevr.swingy.model.hero.Hero;
import com.hulefevr.swingy.model.game.GameState;
import com.hulefevr.swingy.model.game.Encounter;
import com.hulefevr.swingy.model.game.BattleResult;
import com.hulefevr.swingy.model.artifact.Artifact;
import com.hulefevr.swingy.validation.dto.*;
import com.hulefevr.swingy.util.InputManager;
import java.util.List;

public class ConsoleView implements View {

	public ConsoleView() {
		// No special init
	}

	@Override
	public void showMainMenu() {
		System.out.println("=== Swingy Main Menu ===");
		System.out.println("1. Create Hero");
		System.out.println("2. Select Hero");
		System.out.println("3. Exit");
	}

	@Override
	public String promptMenuChoice() {
		showMainMenu();
		System.out.print("Enter your choice: ");
		return InputManager.readLine();
	}

	@Override
	public void showHeroList(List<Hero> heroes) {
		System.out.println("=== Hero List ===");
		for (int i = 0; i < heroes.size(); i++) {
			System.out.println((i + 1) + ". " + heroes.get(i).getName());
		}
	}

	@Override
	public CreateHeroInput promptCreateHero() {
		System.out.print("Enter hero name: ");
		String name = InputManager.readLine();
		System.out.print("Enter hero class: ");
		String heroClass = InputManager.readLine();
		return new CreateHeroInput(name, heroClass);
	}
	@Override
	public void showGameHud(GameState state) {
		System.out.println("=== Game HUD ===");
		System.out.println("Hero: " + state.getHero().getName());
		System.out.println("Level: " + state.getHero().getLevel());
		System.out.println("XP: " + state.getHero().getXp());
	}
	@Override
	public void showMap(String render) {
		System.out.println("=== Map ===");
		System.out.println(render);
	}
	@Override
	public MoveInput promptMove() {
		System.out.print("Enter move direction (N/S/E/W) or (Q)uit: ");
		String direction = InputManager.readLine();
		return new MoveInput(direction);
	}
	@Override
	public void showEncounter(Encounter encounter) {
		System.out.println("=== Encounter ===");
		System.out.println("You encountered a " + encounter.getEnemy().getName());
	}
	@Override
	public FightRunInput promptFightOrRun() {
		System.out.print("Do you want to (F)ight or (R)un? ");
		String choice = InputManager.readLine();
		return new FightRunInput(choice);
	}
	@Override
	public void showBattleResult(BattleResult result) {
		System.out.println("=== Battle Result ===");
		if (result.isVictory()) {
			System.out.println("You won the battle!");
		} else {
			System.out.println("You were defeated...");
		}
	}
	@Override
	public LootChoiceInput promptLootChoice(Artifact drop) {
		System.out.println("You found a " + drop.getName());
		System.out.print("Do you want to (T)ake it or (L)eave it? ");
		String choice = InputManager.readLine();
		return new LootChoiceInput(choice);
	}
	@Override
	public void showValidationErrors(List<String> errors) {
		System.out.println("=== Validation Errors ===");
		for (String error : errors) {
			System.out.println("- " + error);
		}
	}
	@Override
	public void showMessage(String message) {
		System.out.println(message);
	}
	
	@Override
	public void showLevelUp(int newLevel, String message) {
		// Console affiche juste le message
		System.out.println(message);
	}

	@Override
	public String promptSelectHero(List<Hero> heroes) {
		// Show list then read a line (caller will parse)
		showHeroList(heroes);
		System.out.print("Select hero by number (or 0 to go back): ");
		return InputManager.readLine();
	}

	@Override
	public void showHeroDetails(Hero hero) {
		// Reuse display style used in MenuController.displayHeroStats
		System.out.println("\n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
		System.out.println("  FALLEN: " + hero.getName());
		System.out.println("  Class: " + hero.getHeroClass().name());
		System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
		System.out.println("  Level:      " + hero.getLevel());
		System.out.println("  Experience: " + hero.getExperience() + " / " + hero.getXpForNextLevel());
		System.out.println("  Attack:     " + hero.getAttack());
		System.out.println("  Defense:    " + hero.getDefense());
		System.out.println("  Hit Points: " + hero.getHitPoints() + " / " + hero.getMaxHitPoints());
		System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
		System.out.println("  Map Size:   " + hero.getMapSize() + "x" + hero.getMapSize());
		if (hero.getWeapon() != null || hero.getArmor() != null || hero.getHelm() != null) {
			System.out.println("\n  Artifacts:");
			if (hero.getWeapon() != null) System.out.println("    Weapon: " + hero.getWeapon().getName());
			if (hero.getArmor() != null) System.out.println("    Armor:  " + hero.getArmor().getName());
			if (hero.getHelm() != null) System.out.println("    Helm:   " + hero.getHelm().getName());
		}
		System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
	}
	@Override
	public String promptSelectHeroChoice() {
		System.out.print("Select hero number (or 0 to go back): ");
		return InputManager.readLine();
	}
}