// menu principal: create/select/lore/quit

package com.hulefevr.swingy.controller;

import com.hulefevr.swingy.model.hero.Hero;
import com.hulefevr.swingy.model.hero.HeroClass;
import com.hulefevr.swingy.service.HeroFactory;
import com.hulefevr.swingy.validation.dto.CreateHeroInput;
import com.hulefevr.swingy.view.View;

/**
 * Contrôleur pour les menus (création/sélection de héros)
 */
public class MenuController {
    private View view;

    public MenuController(View view) {
        this.view = view;
    }

    /**
     * Gère la création d'un nouveau héros
     * @return Le héros créé, ou null si annulé
     */
    public Hero createHero() {
        view.showMessage("\n=== Create a Fallen Soul ===\n");
        
        // Afficher les classes disponibles
        view.showMessage("Available classes:");
        for (HeroClass heroClass : HeroClass.values()) {
            view.showMessage("  " + heroClass.name() + " - " + HeroFactory.getClassDescription(heroClass));
        }
        view.showMessage("");
        
        // Demander les infos du héros
        CreateHeroInput input = view.promptCreateHero();
        
        // Valider le nom
        if (input.getName() == null || input.getName().trim().isEmpty()) {
            view.showMessage("Error: Hero name cannot be empty.");
            return null;
        }
        
        // Valider et parser la classe
        HeroClass selectedClass = null;
        try {
            selectedClass = HeroClass.valueOf(input.getHeroClass().toUpperCase());
        } catch (Exception e) {
            view.showMessage("Error: Invalid class. Choose from: EXILE, REVENANT, PENITENT, WARDEN, SORCERER");
            return null;
        }
        
        // Créer le héros
        Hero hero = HeroFactory.createHero(input.getName().trim(), selectedClass);
        
        // Afficher les stats du héros créé
        displayHeroStats(hero);
        
        view.showMessage("\n✓ Hero created successfully!\n");
        return hero;
    }
    
    /**
     * Affiche les stats complètes d'un héros
     */
    public void displayHeroStats(Hero hero) {
        // Prefer view-specific hero sheet rendering (GUI will show a panel, console will print)
        view.showHeroDetails(hero);
    }
}