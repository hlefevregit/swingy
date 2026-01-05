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

    public static final String SPLASH = "splash";
    public static final String MAIN_MENU = "main_menu";
    public static final String CREATE_HERO = "create_hero";
    public static final String SELECT_HERO = "select_hero";
    public static final String HERO_SHEET = "hero_sheet";
    public static final String MESSAGE = "message";
    public static final String INPUT = "input";

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

    root.add(splash, SPLASH);
    root.add(mainMenuPanel, MAIN_MENU);
    root.add(createHeroPanel, CREATE_HERO);
    root.add(selectHeroPanel, SELECT_HERO);
    root.add(heroSheetPanel, HERO_SHEET);
    root.add(messagePanel, MESSAGE);
    root.add(inputPanel, INPUT);

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
        // Reset panel state and show it on the EDT. Use invokeAndWait so the UI is visible
        // before we block waiting for selection. Caller MUST NOT be on the EDT.
        try {
            SwingUtilities.invokeAndWait(() -> {
                mainMenuPanel.reset();
                cards.show(root, MAIN_MENU);
                // request focus so key bindings / focus events work
                mainMenuPanel.requestFocusInWindow();
                // revalidate/repaint to ensure components are shown
                root.revalidate();
                root.repaint();
            });
        } catch (Exception e) {
            // If invokeAndWait fails, fall back to invokeLater (best effort)
            SwingUtilities.invokeLater(() -> {
                mainMenuPanel.reset();
                cards.show(root, MAIN_MENU);
                mainMenuPanel.requestFocusInWindow();
            });
        }

        // Block until selection is made
        return mainMenuPanel.waitForSelection();
    }

    /**
     * Affiche l'écran de splash
     */
    public void showSplash() {
        SwingUtilities.invokeLater(() -> cards.show(root, SPLASH));
    }
}
