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
            // promptMenuChoice affiche déjà le menu, pas besoin d'appeler showMainMenu
            String choice = view.promptMenuChoice();
            System.out.println("GameController: received menu choice -> '" + choice + "'");
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
                        // Lancer le jeu avec le nouveau héros
                        startGameWithHero(newHero, view);
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
                    // Close GUI resources if any, then stop the loop
                    try {
                        view.close();
                    } catch (Exception ignored) {
                    }
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
        // Ensure the JVM exits cleanly (dispose GUI windows may not always terminate AWT threads)
        try {
            System.exit(0);
        } catch (SecurityException ignored) {
        }
    }
    
    private void selectExistingHero(View view) {
        try {
            List<Hero> heroes = heroRepository.findAll();
            
            if (heroes.isEmpty()) {
                view.showMessage("\nNo heroes found. Create one first!\n");
                return;
            }
            
            // Pour la console, afficher la liste. Pour la GUI, le SelectHeroPanel l'affiche déjà
            if (!(view instanceof com.hulefevr.swingy.view.gui.GuiView)) {
                view.showMessage("\n=== Saved Heroes ===\n");
                
                // Afficher tous les héros avec leurs stats
                for (int i = 0; i < heroes.size(); i++) {
                    Hero hero = heroes.get(i);
                    view.showMessage((i + 1) + ". " + hero.getName() + " (" + hero.getHeroClass() + 
                                     ") - Level " + hero.getLevel() + " - " + hero.getXp() + " XP");
                }
                
                view.showMessage("\n0. Back to menu\n");
            }
            
            // Use view.promptSelectHero which for GUI will show the select panel, and for console will print and read.
            String sel = view.promptSelectHero(heroes);
            System.out.println("GameController.selectExistingHero: received selection -> '" + sel + "'");
            if (sel == null || sel.trim().isEmpty()) {
                view.showMessage("Invalid choice.");
                return;
            }
            sel = sel.trim();

            // Delete action (format: D<n>)
            if (sel.startsWith("D") || sel.startsWith("d")) {
                String num = sel.substring(1);
                int idx;
                try {
                    idx = Integer.parseInt(num);
                } catch (NumberFormatException e) {
                    view.showMessage("Invalid delete command.");
                    return;
                }
                if (idx < 1 || idx > heroes.size()) {
                    view.showMessage("Invalid index for delete.");
                    return;
                }
                Hero toDelete = heroes.get(idx - 1);
                boolean deleted = heroRepository.deleteByName(toDelete.getName());
                if (deleted) view.showMessage("Deleted " + toDelete.getName());
                else view.showMessage("Failed to delete " + toDelete.getName());
                return;
            }

            // Back command
            if ("0".equals(sel)) {
                return;
            }

            // Otherwise expect a number for selection
            int choice;
            try {
                choice = Integer.parseInt(sel);
            } catch (NumberFormatException e) {
                view.showMessage("Invalid number.");
                return;
            }
            if (choice < 1 || choice > heroes.size()) {
                view.showMessage("Invalid choice. Please select a number between 1 and " + heroes.size());
                return;
            }
            Hero selectedHero = heroes.get(choice - 1);
            // view.showMessage("\n✓ Selected hero:\n");
            // menuController.displayHeroStats(selectedHero);
            System.out.println("GameController: Starting game with hero " + selectedHero.getName());
            startGameWithHero(selectedHero, view);
            System.out.println("GameController: Game finished, returning to menu");
        } catch (Exception e) {
            view.showMessage("Error loading heroes: " + e.getMessage());
        }
    }

    private void startGameWithHero(Hero hero, View view) {
        System.out.println("GameController: Starting game loop in background thread");
        
        // Lancer la boucle de jeu dans un thread séparé pour ne pas bloquer
        Thread gameThread = new Thread(() -> {
            try {
                // Créer le contrôleur de boucle de jeu et lancer
                GameLoopController gameLoop = new GameLoopController(view, heroRepository);
                gameLoop.startGameLoop(hero);
                
                // Sauvegarder le héros après la partie (XP, level, artefacts)
                try {
                    heroRepository.save(hero);
                    view.showMessage("\n✓ Progress saved!");
                } catch (Exception e) {
                    view.showMessage("\n✗ Error saving progress: " + e.getMessage());
                }
            } catch (Exception e) {
                System.err.println("Error in game loop: " + e.getMessage());
                e.printStackTrace();
            }
        });
        
        gameThread.setName("GameLoop-Thread");
        gameThread.start();
        
        // Attendre que le thread se termine avant de retourner au menu
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Game thread interrupted");
        }
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