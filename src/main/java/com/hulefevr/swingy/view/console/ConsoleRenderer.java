// affiche map, hud, etc.
package com.hulefevr.swingy.view.console;

import com.hulefevr.swingy.view.View;
import com.hulefevr.swingy.model.hero.Hero;
import com.hulefevr.swingy.model.game.GameState;
import com.hulefevr.swingy.model.game.Encounter;
import com.hulefevr.swingy.model.game.BattleResult;
import com.hulefevr.swingy.model.artifact.Artifact;
import com.hulefevr.swingy.validation.dto.*;
import java.util.List;
import java.util.Scanner;

public class ConsoleRenderer implements View {
    private Scanner scanner = new Scanner(System.in);

    @Override
    public void showMainMenu() {
        System.out.println("=== Main Menu ===");
        System.out.println("1. Create Hero");
        System.out.println("2. Select Hero");
        System.out.println("3. Show Lore");
        System.out.println("4. Exit");
    }

    @Override
    public String promptMenuChoice() {
        System.out.print("Choice: ");
        return scanner.nextLine();
    }

    @Override
    public void showHeroList(List<Hero> heroes) {
        System.out.println("=== Heroes ===");
        for (int i = 0; i < heroes.size(); i++) {
            System.out.println((i + 1) + ". " + heroes.get(i).getName());
        }
    }

    @Override
    public CreateHeroInput promptCreateHero() {
        System.out.print("Hero name: ");
        String name = scanner.nextLine();
        System.out.print("Hero class: ");
        String type = scanner.nextLine();
        return new CreateHeroInput(name, type);
    }

    @Override
    public CreateHeroInput promptSelectHero(List<Hero> heroes) {
        System.out.print("Select hero (number): ");
        int choice = Integer.parseInt(scanner.nextLine());
        return new CreateHeroInput(heroes.get(choice - 1).getName(), null);
    }

    @Override
    public void showGameHud(GameState state) {
        System.out.println("=== HUD ===");
        if (state.getHero() != null) {
            System.out.println("Hero: " + state.getHero().getName());
        }
        System.out.println("===========");
    }

    @Override
    public void showMap(String render) {
        System.out.println(render);
    }

    @Override
    public MoveInput promptMove() {
        System.out.print("Move (N/S/E/W): ");
        String direction = scanner.nextLine();
        return new MoveInput(direction);
    }

    @Override
    public void showEncounter(Encounter encounter) {
        System.out.println("=== Encounter ===");
        System.out.println("You encountered an enemy!");
    }

	@Override
	public FightRunInput promptFightOrRun() {
		System.out.print("(F)ight or (R)un? ");
		String choice = scanner.nextLine();
		return new FightRunInput(choice);
	}    @Override
    public void showBattleResult(BattleResult result) {
        if (result.isVictory()) {
            System.out.println("Victory! +" + result.getXpGained() + " XP");
        } else {
            System.out.println("Defeat...");
        }
    }

	@Override
	public LootChoiceInput promptLootChoice(Artifact drop) {
		System.out.println("Loot found: " + drop.getName());
		System.out.print("(T)ake or (L)eave? ");
		String choice = scanner.nextLine();
		return new LootChoiceInput(choice);
	}    @Override
    public void showValidationErrors(List<String> errors) {
        System.out.println("=== ERRORS ===");
        for (String error : errors) {
            System.out.println("- " + error);
        }
    }

    @Override
    public void showMessage(String message) {
        System.out.println(message);
    }

    // Méthodes spécifiques à ConsoleRenderer (pas dans l'interface View)
    public void renderMap(char[][] map) {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                System.out.print(map[i][j] + " ");
            }
            System.out.println();
        }
    }

    @Override
    public String promptSelectHeroChoice() {
        System.out.print("Select hero number (or 0 to go back): ");
        return scanner.nextLine();
    }

    public void renderHUD(String hud) {
        System.out.println("=== HUD ===");
        System.out.println(hud);
        System.out.println("===========\n");
    }

    public void renderError(String[] errors) {
        System.out.println("=== ERRORS ===");
        for (String error : errors) {
            System.out.println("- " + error);
        }
        System.out.println("==============\n");
    }
}