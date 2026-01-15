package com.hulefevr.swingy.view.gui;

import com.hulefevr.swingy.view.gui.screens.MainMenuPanel;

import javax.swing.*;
import java.awt.*;

/**
 * Fenêtre principale de l'UI Swing. Utilise CardLayout pour switcher entre écrans.
 */
public class GuiWindow extends JFrame {
    private CardLayout cards;
    private JPanel root;
    private com.hulefevr.swingy.view.gui.screens.MainMenuPanel mainMenuPanel;
    private com.hulefevr.swingy.view.gui.screens.CreateHeroPanel createHeroPanel;
    private com.hulefevr.swingy.view.gui.screens.SelectHeroPanel selectHeroPanel;
    private com.hulefevr.swingy.view.gui.screens.HeroSheetPanel heroSheetPanel;
    private com.hulefevr.swingy.view.gui.screens.MessagePanel messagePanel;
    private com.hulefevr.swingy.view.gui.screens.InputPanel inputPanel;
    private com.hulefevr.swingy.view.gui.screens.GamePanel gamePanel;
    private com.hulefevr.swingy.view.gui.screens.EncounterPanel encounterPanel;
    private com.hulefevr.swingy.view.gui.screens.LootPanel lootPanel;
    private com.hulefevr.swingy.view.gui.screens.LevelUpPanel levelUpPanel;
    private com.hulefevr.swingy.view.gui.screens.FinalBossIntroPanel finalBossIntroPanel;
    private com.hulefevr.swingy.view.gui.screens.FinalBossBattlePanel finalBossBattlePanel;
    private com.hulefevr.swingy.view.gui.screens.EndingPanel endingPanel;
    
    // Current game state for accessing Hero in various panels
    private com.hulefevr.swingy.model.game.GameState currentGameState;

    public static final String SPLASH = "splash";
    public static final String MAIN_MENU = "main_menu";
    public static final String CREATE_HERO = "create_hero";
    public static final String SELECT_HERO = "select_hero";
    public static final String HERO_SHEET = "hero_sheet";
    public static final String MESSAGE = "message";
    public static final String INPUT = "input";
    public static final String GAME = "game";
    public static final String ENCOUNTER = "encounter";
    public static final String LOOT = "loot";
    public static final String LEVEL_UP = "level_up";
    public static final String FINAL_BOSS_INTRO = "final_boss_intro";
    public static final String FINAL_BOSS_BATTLE = "final_boss_battle";
    public static final String ENDING = "ending";

    public GuiWindow() {
        super("Swingy - The Book of the Fallen");

        cards = new CardLayout();
        root = new JPanel(cards);

        // Panels
        SplashPanel splash = new SplashPanel(this);
        mainMenuPanel = new com.hulefevr.swingy.view.gui.screens.MainMenuPanel();
    createHeroPanel = new com.hulefevr.swingy.view.gui.screens.CreateHeroPanel();
    selectHeroPanel = new com.hulefevr.swingy.view.gui.screens.SelectHeroPanel();
    heroSheetPanel = new com.hulefevr.swingy.view.gui.screens.HeroSheetPanel();
    messagePanel = new com.hulefevr.swingy.view.gui.screens.MessagePanel();
    inputPanel = new com.hulefevr.swingy.view.gui.screens.InputPanel();
    gamePanel = new com.hulefevr.swingy.view.gui.screens.GamePanel();
    encounterPanel = new com.hulefevr.swingy.view.gui.screens.EncounterPanel();
    lootPanel = new com.hulefevr.swingy.view.gui.screens.LootPanel();
    levelUpPanel = new com.hulefevr.swingy.view.gui.screens.LevelUpPanel();
    finalBossIntroPanel = new com.hulefevr.swingy.view.gui.screens.FinalBossIntroPanel();
    endingPanel = new com.hulefevr.swingy.view.gui.screens.EndingPanel();
    finalBossBattlePanel = new com.hulefevr.swingy.view.gui.screens.FinalBossBattlePanel();

    root.add(splash, SPLASH);
    root.add(mainMenuPanel, MAIN_MENU);
    root.add(createHeroPanel, CREATE_HERO);
    root.add(selectHeroPanel, SELECT_HERO);
    root.add(heroSheetPanel, HERO_SHEET);
    root.add(messagePanel, MESSAGE);
    root.add(inputPanel, INPUT);
    root.add(gamePanel, GAME);
    root.add(encounterPanel, ENCOUNTER);
    root.add(lootPanel, LOOT);
    root.add(levelUpPanel, LEVEL_UP);
    root.add(finalBossIntroPanel, FINAL_BOSS_INTRO);
    root.add(endingPanel, ENDING);
    root.add(finalBossBattlePanel, FINAL_BOSS_BATTLE);

        setContentPane(root);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
    }

    /**
     * Show a message panel with options and block until user picks one.
     * Caller MUST NOT be on the EDT.
     * Returns the option label the user clicked (or null).
     */
    public String showMessageAndWait(String title, String message, String[] options) {
        try {
            SwingUtilities.invokeAndWait(() -> {
                messagePanel.setMessage(title, message, options);
                cards.show(root, MESSAGE);
                messagePanel.requestFocusInWindow();
                root.revalidate();
                root.repaint();
            });
        } catch (Exception e) {
            SwingUtilities.invokeLater(() -> {
                messagePanel.setMessage(title, message, options);
                cards.show(root, MESSAGE);
                messagePanel.requestFocusInWindow();
            });
        }
        return messagePanel.waitForResult();
    }

    /**
     * Non-blocking show of a simple message (OK button).
     */
    public void showMessageNonBlocking(String title, String message) {
        SwingUtilities.invokeLater(() -> {
            messagePanel.showMessageNonBlocking(title, message);
            cards.show(root, MESSAGE);
        });
    }

    /**
     * Show a single-line text input prompt and block until user submits or cancels.
     * Caller MUST NOT be on the EDT.
     */
    public String showInputAndWait(String title, String prompt) {
        try {
            SwingUtilities.invokeAndWait(() -> {
                inputPanel.setPrompt(title, prompt);
                cards.show(root, INPUT);
                inputPanel.requestFocusForField();
                root.revalidate();
                root.repaint();
            });
        } catch (Exception e) {
            SwingUtilities.invokeLater(() -> {
                inputPanel.setPrompt(title, prompt);
                cards.show(root, INPUT);
                inputPanel.requestFocusForField();
            });
        }
        return inputPanel.waitForResult();
    }

    /**
     * Show create-hero panel and block until user submits or cancels. Caller MUST NOT be on the EDT.
     */
    public com.hulefevr.swingy.validation.dto.CreateHeroInput showCreateHeroAndWait() {
        try {
            SwingUtilities.invokeAndWait(() -> {
                createHeroPanel.reset();
                cards.show(root, CREATE_HERO);
                createHeroPanel.requestFocusInWindow();
                root.revalidate();
                root.repaint();
            });
        } catch (Exception e) {
            SwingUtilities.invokeLater(() -> {
                createHeroPanel.reset();
                cards.show(root, CREATE_HERO);
                createHeroPanel.requestFocusInWindow();
            });
        }

        return createHeroPanel.waitForResult();
    }

    /**
     * Show the select-hero panel populated with the list and block until a choice is made.
     * Returns the raw string: "0" for back, "D<n>" for delete n, or "<n>" for selection.
     */
    public String showSelectHeroAndWait(java.util.List<com.hulefevr.swingy.model.hero.Hero> heroes) {
        try {
            SwingUtilities.invokeAndWait(() -> {
                selectHeroPanel.reset();
                selectHeroPanel.setHeroes(heroes);
                cards.show(root, SELECT_HERO);
                selectHeroPanel.requestFocusInWindow();
                root.revalidate();
                root.repaint();
            });
        } catch (Exception e) {
            SwingUtilities.invokeLater(() -> {
                selectHeroPanel.reset();
                selectHeroPanel.setHeroes(heroes);
                cards.show(root, SELECT_HERO);
                selectHeroPanel.requestFocusInWindow();
            });
        }
        return selectHeroPanel.waitForResult();
    }

    /**
     * Show a hero sheet (non-blocking); caller can continue.
     */
    public void showHeroSheet(com.hulefevr.swingy.model.hero.Hero hero) {
        SwingUtilities.invokeLater(() -> {
            heroSheetPanel.showHero(hero);
            cards.show(root, HERO_SHEET);
            root.revalidate();
            root.repaint();
        });
    }

    /**
     * Affiche l'écran principal (menu)
     */
    public void showMainMenu() {
        SwingUtilities.invokeLater(() -> cards.show(root, MAIN_MENU));
    }

    /**
     * Shows the main menu and blocks until the user makes a selection.
     * Caller MUST NOT be on the EDT.
     */
    public String showMainMenuAndWait() {
        // System.out.println("DEBUG GuiWindow: showMainMenuAndWait called");
        // Reset panel state and show it on the EDT. Use invokeAndWait so the UI is visible
        // before we block waiting for selection. Caller MUST NOT be on the EDT.
        try {
            SwingUtilities.invokeAndWait(() -> {
                // System.out.println("DEBUG GuiWindow: Resetting mainMenuPanel and showing it");
                mainMenuPanel.reset();
                cards.show(root, MAIN_MENU);
                mainMenuPanel.requestFocusInWindow();
                root.revalidate();
                root.repaint();
                // System.out.println("DEBUG GuiWindow: Menu panel should now be visible");
            });
        } catch (Exception e) {
            System.err.println("ERROR GuiWindow: Exception in invokeAndWait: " + e.getMessage());
            e.printStackTrace();
            SwingUtilities.invokeLater(() -> {
                mainMenuPanel.reset();
                cards.show(root, MAIN_MENU);
                mainMenuPanel.requestFocusInWindow();
            });
        }

        // System.out.println("DEBUG GuiWindow: About to wait for selection...");
        String result = mainMenuPanel.waitForSelection();
        // System.out.println("DEBUG GuiWindow: Selection received: " + result);
        return result;
    }

    /**
     * Affiche l'écran de splash
     */
    public void showSplash() {
        SwingUtilities.invokeLater(() -> cards.show(root, SPLASH))
        ;
    }
    
    /**
     * Affiche l'écran de jeu avec l'état actuel
     */
    public void showGame(com.hulefevr.swingy.model.game.GameState gameState) {
        this.currentGameState = gameState;
        try {
            SwingUtilities.invokeAndWait(() -> {
                gamePanel.updateGameState(gameState);
                cards.show(root, GAME);
                root.revalidate();
                root.repaint();
            });
        } catch (Exception e) {
            SwingUtilities.invokeLater(() -> {
                gamePanel.updateGameState(gameState);
                cards.show(root, GAME);
                root.revalidate();
                root.repaint();
            });
        }
    }
    
    /**
     * Met à jour l'état du jeu affiché
     */
    public void updateGameState(com.hulefevr.swingy.model.game.GameState gameState) {
        this.currentGameState = gameState;
        SwingUtilities.invokeLater(() -> {
            gamePanel.updateGameState(gameState);
        });
    }
    
    /**
     * Ajoute un message dans le panneau de jeu
     */
    public void addGameMessage(String message) {
        SwingUtilities.invokeLater(() -> {
            gamePanel.addMessage(message);
        });
    }
    
    /**
     * Accès au GamePanel pour définir les actions
     */
    public com.hulefevr.swingy.view.gui.screens.GamePanel getGamePanel() {
        return gamePanel;
    }
    
    /**
     * Affiche le jeu et attend que le joueur fasse un mouvement.
     * Bloque jusqu'à ce qu'un bouton de direction soit cliqué.
     * IMPORTANT: Ne doit PAS être appelée depuis l'EDT !
     */
    public String waitForMoveInput() {
        // S'assurer que les boutons sont configurés pour notifier
        try {
            SwingUtilities.invokeAndWait(() -> {
                gamePanel.setupMoveWaiting();
            });
        } catch (Exception e) {
            // Si erreur, essayer quand même
            SwingUtilities.invokeLater(() -> {
                gamePanel.setupMoveWaiting();
            });
        }
        
        return gamePanel.waitForMove();
    }
    
    /**
     * Affiche le panneau de combat et attend le choix du joueur.
     * IMPORTANT: Ne doit PAS être appelée depuis l'EDT !
     */
    public String showEncounterAndWait(com.hulefevr.swingy.model.hero.Hero hero, com.hulefevr.swingy.model.ennemy.Villain villain) {
        try {
            SwingUtilities.invokeAndWait(() -> {
                encounterPanel.setEncounter(hero, villain);
                encounterPanel.setupChoiceWaiting();
                cards.show(root, ENCOUNTER);
                root.revalidate();
                root.repaint();
            });
        } catch (Exception e) {
            SwingUtilities.invokeLater(() -> {
                encounterPanel.setEncounter(hero, villain);
                encounterPanel.setupChoiceWaiting();
                cards.show(root, ENCOUNTER);
            });
        }
        
        // Attendre le choix (Fight ou Run)
        return encounterPanel.waitForChoice();
    }
    
    /**
     * Affiche le panel de loot et attend le choix (T = Take, L = Leave).
     * Caller MUST NOT be on the EDT.
     */
    public String showLootAndWait(com.hulefevr.swingy.model.artifact.Artifact artifact) {
        try {
            SwingUtilities.invokeAndWait(() -> {
                com.hulefevr.swingy.model.hero.Hero hero = currentGameState != null ? currentGameState.getHero() : null;
                lootPanel.updateLoot(hero, artifact);
                lootPanel.setupChoiceWaiting();
                cards.show(root, LOOT);
                root.revalidate();
                root.repaint();
            });
        } catch (Exception e) {
            SwingUtilities.invokeLater(() -> {
                com.hulefevr.swingy.model.hero.Hero hero = currentGameState != null ? currentGameState.getHero() : null;
                lootPanel.updateLoot(hero, artifact);
                lootPanel.setupChoiceWaiting();
                cards.show(root, LOOT);
            });
        }
        
        return lootPanel.waitForChoice();
    }
    
    /**
     * Retourne au panneau de jeu
     */
    public void returnToGame() {
        SwingUtilities.invokeLater(() -> {
            cards.show(root, GAME);
        });
    }
    
    /**
     * Affiche l'écran épique de level up et attend que l'utilisateur continue.
     * Caller MUST NOT be on the EDT.
     */
    public void showLevelUpAndWait(int newLevel, String message) {
        // System.out.println("DEBUG GuiWindow: showLevelUpAndWait called with level " + newLevel);
        try {
            SwingUtilities.invokeAndWait(() -> {
                // System.out.println("DEBUG GuiWindow: Setting up level up panel on EDT");
                levelUpPanel.setLevelUp(newLevel, message);
                cards.show(root, LEVEL_UP);
                root.revalidate();
                root.repaint();
            });
        } catch (Exception e) {
            // System.out.println("DEBUG GuiWindow: Exception in invokeAndWait: " + e.getMessage());
            e.printStackTrace();
            SwingUtilities.invokeLater(() -> {
                levelUpPanel.setLevelUp(newLevel, message);
                cards.show(root, LEVEL_UP);
            });
        }
        
        // System.out.println("DEBUG GuiWindow: Waiting for user to click continue...");
        // Attendre que l'utilisateur clique sur CONTINUE
        levelUpPanel.waitForContinue();
        // System.out.println("DEBUG GuiWindow: User clicked continue, returning to game");
        
        // Retourner au jeu
        returnToGame();
    }
    
    /**
     * Affiche l'écran d'intro du boss final et attend que l'utilisateur continue.
     * Caller MUST NOT be on the EDT.
     */
    public void showFinalBossIntroAndWait() {
        // System.out.println("DEBUG GuiWindow: showFinalBossIntroAndWait called");
        try {
            SwingUtilities.invokeAndWait(() -> {
                // System.out.println("DEBUG GuiWindow: Showing final boss intro panel on EDT");
                cards.show(root, FINAL_BOSS_INTRO);
                root.revalidate();
                root.repaint();
            });
        } catch (Exception e) {
            // System.out.println("DEBUG GuiWindow: Exception in invokeAndWait: " + e.getMessage());
            e.printStackTrace();
            SwingUtilities.invokeLater(() -> {
                cards.show(root, FINAL_BOSS_INTRO);
            });
        }
        
        // System.out.println("DEBUG GuiWindow: Waiting for user to click continue...");
        // Attendre que l'utilisateur clique sur FACE THE ARCHANGEL
        finalBossIntroPanel.waitForContinue();
        // System.out.println("DEBUG GuiWindow: User clicked continue");
    }
    
    /**
     * Affiche l'écran de combat du boss final et attend chaque tour.
     * Caller MUST NOT be on the EDT.
     */
    public void showFinalBossBattleAndWait(com.hulefevr.swingy.model.hero.Hero hero, String message) {
        // System.out.println("DEBUG GuiWindow: showFinalBossBattleAndWait called");
        try {
            SwingUtilities.invokeAndWait(() -> {
                // System.out.println("DEBUG GuiWindow: Setting up final boss battle panel on EDT");
                finalBossBattlePanel.setHero(hero);
                finalBossBattlePanel.setMessage(message);
                cards.show(root, FINAL_BOSS_BATTLE);
                root.revalidate();
                root.repaint();
            });
        } catch (Exception e) {
            // System.out.println("DEBUG GuiWindow: Exception in invokeAndWait: " + e.getMessage());
            e.printStackTrace();
            SwingUtilities.invokeLater(() -> {
                finalBossBattlePanel.setHero(hero);
                finalBossBattlePanel.setMessage(message);
                cards.show(root, FINAL_BOSS_BATTLE);
            });
        }
        
        // System.out.println("DEBUG GuiWindow: Waiting for user to click next turn...");
        // Attendre que l'utilisateur clique sur NEXT TURN
        finalBossBattlePanel.waitForNextTurn();
        // System.out.println("DEBUG GuiWindow: User clicked next turn");
    }
    
    /**
     * Affiche l'écran de fin épique et attend le choix de l'utilisateur.
     * Caller MUST NOT be on the EDT.
     * Returns "MENU" or "QUIT"
     */
    public String showEndingAndWait() {
        // System.out.println("DEBUG GuiWindow: showEndingAndWait called");
        try {
            SwingUtilities.invokeAndWait(() -> {
                //System.out.println("DEBUG GuiWindow: Showing ending panel on EDT");
                cards.show(root, ENDING);
                root.revalidate();
                root.repaint();
            });
        } catch (Exception e) {
            // System.out.println("DEBUG GuiWindow: Exception in invokeAndWait: " + e.getMessage());
            e.printStackTrace();
            SwingUtilities.invokeLater(() -> {
                cards.show(root, ENDING);
            });
        }
        
        // System.out.println("DEBUG GuiWindow: Waiting for user action...");
        // Attendre que l'utilisateur clique sur RETURN TO COVER ou QUIT
        String action = endingPanel.waitForAction();
        // System.out.println("DEBUG GuiWindow: User chose: " + action);
        return action;
    }
}
