// contrat commun console/gui
package com.hulefevr.swingy.view;

import java.util.List;
import com.hulefevr.swingy.model.hero.Hero;
import com.hulefevr.swingy.model.game.GameState;
import com.hulefevr.swingy.model.game.Encounter;
import com.hulefevr.swingy.model.game.BattleResult;
import com.hulefevr.swingy.model.artifact.Artifact;
import com.hulefevr.swingy.validation.dto.*;

public interface View {
    void showMainMenu();
    String promptMenuChoice();
    void showHeroList(List<Hero> heroes);

    /**
     * Prompt the user to select a hero from the given list. Returns the user's raw input (console) or
     * a selection string (GUI) - should be parsed by the caller.
     */
    String promptSelectHero(List<Hero> heroes);

    /**
     * Display a full hero sheet; GUI should show a dedicated panel, console should print lines.
     */
    void showHeroDetails(Hero hero);

    CreateHeroInput promptCreateHero();
    
    
    void showGameHud(GameState state);
    void showMap(String render);

    MoveInput promptMove();

    void showEncounter(Encounter encounter);
    FightRunInput promptFightOrRun();
    void showBattleResult(BattleResult result);
    LootChoiceInput promptLootChoice(Artifact drop);
    void showValidationErrors(List<String> errors);

    void showMessage(String message);
    String promptSelectHeroChoice();
    
    /**
     * Show the level up screen with epic presentation.
     * For GUI: shows dedicated level-up panel with the hero's new level and narrative message.
     * For console: prints the message using showMessage.
     */
    void showLevelUp(int newLevel, String message);

    /**
     * Optional lifecycle hook to allow the view to release resources / close UI.
     * Default implementation is a no-op for console-based views.
     */
    default void close() {
        // no-op
    }
}