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

    CreateHeroInput promptCreateHero();
    CreateHeroInput promptSelectHero(List<Hero> heroes);
    
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
}