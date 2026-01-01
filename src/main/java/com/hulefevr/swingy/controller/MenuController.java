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
            selectedClass = HeroClass.valueOf(input.getType().toUpperCase());
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
        view.showMessage("\n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        view.showMessage("  FALLEN: " + hero.getName());
        view.showMessage("  Class: " + hero.getHeroClass().name());
        view.showMessage("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        view.showMessage("  Level:      " + hero.getLevel());
        view.showMessage("  Experience: " + hero.getExperience() + " / " + hero.getXpForNextLevel());
        view.showMessage("  Attack:     " + hero.getAttack());
        view.showMessage("  Defense:    " + hero.getDefense());
        view.showMessage("  Hit Points: " + hero.getHitPoints() + " / " + hero.getMaxHitPoints());
        view.showMessage("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        view.showMessage("  Map Size:   " + hero.getMapSize() + "x" + hero.getMapSize());
        
        // Afficher artefacts si équipés
        if (hero.getWeapon() != null || hero.getArmor() != null || hero.getHelm() != null) {
            view.showMessage("\n  Artifacts:");
            if (hero.getWeapon() != null) view.showMessage("    Weapon: " + hero.getWeapon().getName());
            if (hero.getArmor() != null) view.showMessage("    Armor:  " + hero.getArmor().getName());
            if (hero.getHelm() != null) view.showMessage("    Helm:   " + hero.getHelm().getName());
        }
        view.showMessage("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
    }
}