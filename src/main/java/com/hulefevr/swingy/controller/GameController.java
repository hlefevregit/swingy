// boucle de jeu: move, encounter, fight/run
package com.hulefevr.swingy.controller;

import com.hulefevr.swingy.view.View;
import com.hulefevr.swingy.view.ViewManager;
import com.hulefevr.swingy.model.hero.Hero;
import com.hulefevr.swingy.persistence.HeroRepository;
import com.hulefevr.swingy.persistence.db.JdbcHeroRepository;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class GameController {
    private MenuController menuController;
    private HeroRepository heroRepository;

    public GameController() {
        this.heroRepository = new JdbcHeroRepository();
    }

    public void startGame() {
        View view = ViewManager.getView();
        menuController = new MenuController(view);
        boolean running = true;

        while (running) {
            view.showMainMenu();
            String choice = view.promptMenuChoice();
            if (choice == null) {
                view.showMessage("Choix invalide.");
                continue;
            }
            choice = choice.trim();

            switch (choice) {
                case "1":
                    // Création de héros
                    Hero newHero = menuController.createHero();
                    if (newHero != null) {
                        // Sauvegarder le héros
                        try {
                            heroRepository.save(newHero);
                            view.showMessage("✓ Hero saved to database!");
                        } catch (Exception e) {
                            view.showMessage("✗ Error saving hero: " + e.getMessage());
                        }
                        // TODO: Lancer le jeu avec ce héros
                        view.showMessage("Game start will be implemented soon.");
                    }
                    break;
                case "2":
                    // Sélection d'un héros existant
                    selectExistingHero(view);
                    break;
                case "3":
                    // Afficher le lore depuis le fichier lore.txt
                    showLore(view);
                    break;
                case "4":
                    view.showMessage("Au revoir.");
                    running = false;
                    break;
                default:
                    view.showMessage("Choix non reconnu. Réessayez.");
            }
        }
        
        // Fermer la connexion à la DB
        if (heroRepository instanceof JdbcHeroRepository) {
            ((JdbcHeroRepository) heroRepository).close();
        }
    }
    
    private void selectExistingHero(View view) {
        try {
            List<Hero> heroes = heroRepository.findAll();
            
            if (heroes.isEmpty()) {
                view.showMessage("\nNo heroes found. Create one first!\n");
                return;
            }
            
            view.showMessage("\n=== Saved Heroes ===\n");
            
            // Afficher tous les héros avec leurs stats
            for (int i = 0; i < heroes.size(); i++) {
                Hero hero = heroes.get(i);
                view.showMessage((i + 1) + ". " + hero.getName() + " (" + hero.getHeroClass() + 
                                 ") - Level " + hero.getLevel() + " - " + hero.getXp() + " XP");
            }
            
            view.showMessage("\n0. Back to menu\n");
            
            // Demander le choix
            String input = view.promptSelectHeroChoice();
            if (input == null || input.trim().isEmpty()) {
                view.showMessage("Invalid choice.");
                return;
            }
            
            int choice;
            try {
                choice = Integer.parseInt(input.trim());
            } catch (NumberFormatException e) {
                view.showMessage("Invalid number.");
                return;
            }
            
            // Retour au menu
            if (choice == 0) {
                return;
            }
            
            // Vérifier le choix
            if (choice < 1 || choice > heroes.size()) {
                view.showMessage("Invalid choice. Please select a number between 1 and " + heroes.size());
                return;
            }
            
            // Récupérer le héros choisi
            Hero selectedHero = heroes.get(choice - 1);
            
            // Afficher les stats du héros
            view.showMessage("\n✓ Selected hero:\n");
            menuController.displayHeroStats(selectedHero);
            
            // Lancer le jeu avec ce héros
            startGameWithHero(selectedHero, view);
            
        } catch (Exception e) {
            view.showMessage("Error loading heroes: " + e.getMessage());
        }
    }
    
    private void startGameWithHero(Hero hero, View view) {
        view.showMessage("\n=== Starting game with " + hero.getName() + " ===\n");
        view.showMessage("Map size: " + hero.getMapSize() + "x" + hero.getMapSize());
        view.showMessage("\n[Game loop will be implemented next]");
        view.showMessage("Press Enter to continue...");
        view.promptMenuChoice(); // Attendre l'input utilisateur
    }

    private void showLore(View view) {
        Path lorePath = Path.of("lore.txt");
        try {
            List<String> lines = Files.readAllLines(lorePath, StandardCharsets.UTF_8);
            for (String line : lines) {
                view.showMessage(line);
            }
        } catch (IOException e) {
            view.showMessage("Impossible de lire le fichier lore.txt: " + e.getMessage());
        }
    }

}