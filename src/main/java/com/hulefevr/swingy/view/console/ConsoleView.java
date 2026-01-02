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
		// Pas besoin de créer un Scanner ici
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
	public CreateHeroInput promptSelectHero(List<Hero> heroes) {
		System.out.print("Select hero by number: ");
		int choice = Integer.parseInt(InputManager.readLine());
		return new CreateHeroInput(heroes.get(choice - 1).getName(), null);
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
	public String promptSelectHeroChoice() {
		System.out.print("Select hero number (or 0 to go back): ");
		return InputManager.readLine();
	}
}