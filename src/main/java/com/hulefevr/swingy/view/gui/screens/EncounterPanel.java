package com.hulefevr.swingy.view.gui.screens;

import com.hulefevr.swingy.model.hero.Hero;
import com.hulefevr.swingy.model.ennemy.Villain;

import javax.swing.*;
import java.awt.*;

/**
 * Panel pour afficher un combat entre le héros et un ennemi
 * Design inspiré de "Heaven vs Abyss"
 */
public class EncounterPanel extends JPanel {
    private Hero hero;
    private Villain villain;
    
    private JLabel titleLabel;
    private JLabel villainNameLabel;
    private JProgressBar villainHpBar;
    private JLabel heroNameLabel;
    private JProgressBar heroHpBar;
    private JLabel messageLabel;
    private JButton btnFight;
    private JButton btnRun;
    
    // Pour gérer l'attente de choix
    private final Object choiceLock = new Object();
    private String pendingChoice = null;
    
    // Couleurs du thème
    private static final Color PARCHMENT = new Color(220, 205, 175);
    private static final Color DARK_BROWN = new Color(40, 30, 20);
    private static final Color BORDER_GOLD = new Color(160, 140, 90);
    private static final Color HP_RED = new Color(139, 0, 0);
    private static final Color HP_BG = new Color(100, 100, 100);
    
    public EncounterPanel() {
        setLayout(new BorderLayout());
        setBackground(DARK_BROWN);
        
        initComponents();
        layoutComponents();
    }
    
    private void initComponents() {
        // Title
        titleLabel = new JLabel("HEAVEN vs ABYSS", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 24));
        titleLabel.setForeground(DARK_BROWN);
        titleLabel.setOpaque(true);
        titleLabel.setBackground(PARCHMENT);
        
        // Villain info
        villainNameLabel = new JLabel("Enemy", SwingConstants.CENTER);
        villainNameLabel.setFont(new Font("Serif", Font.BOLD, 16));
        villainNameLabel.setForeground(DARK_BROWN);
        villainNameLabel.setOpaque(true);
        villainNameLabel.setBackground(PARCHMENT);
        
        villainHpBar = new JProgressBar(0, 100);
        villainHpBar.setStringPainted(true);
        villainHpBar.setForeground(HP_RED);
        villainHpBar.setBackground(HP_BG);
        villainHpBar.setString("HP");
        
        // Hero info
        heroNameLabel = new JLabel("Hero", SwingConstants.CENTER);
        heroNameLabel.setFont(new Font("Serif", Font.BOLD, 16));
        heroNameLabel.setForeground(DARK_BROWN);
        heroNameLabel.setOpaque(true);
        heroNameLabel.setBackground(PARCHMENT);
        
        heroHpBar = new JProgressBar(0, 100);
        heroHpBar.setStringPainted(true);
        heroHpBar.setForeground(HP_RED);
        heroHpBar.setBackground(HP_BG);
        heroHpBar.setString("HP");
        
        // Message
        messageLabel = new JLabel("What will you do?", SwingConstants.CENTER);
        messageLabel.setFont(new Font("Serif", Font.ITALIC, 18));
        messageLabel.setForeground(DARK_BROWN);
        messageLabel.setOpaque(true);
        messageLabel.setBackground(PARCHMENT);
        
        // Buttons
        btnFight = createButton("FIGHT");
        btnRun = createButton("RUN (50%)");
    }
    
    private JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Serif", Font.BOLD, 16));
        btn.setBackground(DARK_BROWN);
        btn.setForeground(PARCHMENT);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_GOLD, 3),
            BorderFactory.createEmptyBorder(15, 40, 15, 40)
        ));
        return btn;
    }
    
    private void layoutComponents() {
        // Main panel avec bordures
        JPanel mainContent = new JPanel(new BorderLayout(10, 10));
        mainContent.setBackground(PARCHMENT);
        mainContent.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(DARK_BROWN, 15),
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_GOLD, 3),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
            )
        ));
        
        // Top: Title
        mainContent.add(titleLabel, BorderLayout.NORTH);
        
        // Center: Combat info
        JPanel centerPanel = new JPanel(new BorderLayout(20, 20));
        centerPanel.setOpaque(false);
        
        // Villain panel (left)
        JPanel villainPanel = new JPanel(new BorderLayout(5, 5));
        villainPanel.setOpaque(false);
        villainPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_GOLD, 2),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        villainPanel.add(villainNameLabel, BorderLayout.NORTH);
        villainPanel.add(villainHpBar, BorderLayout.CENTER);
        
        // Hero panel (right)
        JPanel heroPanel = new JPanel(new BorderLayout(5, 5));
        heroPanel.setOpaque(false);
        heroPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_GOLD, 2),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        heroPanel.add(heroNameLabel, BorderLayout.NORTH);
        heroPanel.add(heroHpBar, BorderLayout.CENTER);
        
        // Combat visualization (center image placeholder)
        JPanel combatArea = new JPanel();
        combatArea.setBackground(new Color(50, 50, 50));
        combatArea.setPreferredSize(new Dimension(400, 300));
        combatArea.setBorder(BorderFactory.createLineBorder(BORDER_GOLD, 3));
        
        JPanel topRow = new JPanel(new GridLayout(1, 3, 10, 0));
        topRow.setOpaque(false);
        topRow.add(villainPanel);
        topRow.add(combatArea);
        topRow.add(heroPanel);
        
        centerPanel.add(topRow, BorderLayout.NORTH);
        centerPanel.add(messageLabel, BorderLayout.CENTER);
        
        mainContent.add(centerPanel, BorderLayout.CENTER);
        
        // Bottom: Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10));
        buttonPanel.setOpaque(false);
        buttonPanel.add(btnFight);
        buttonPanel.add(btnRun);
        
        mainContent.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainContent, BorderLayout.CENTER);
    }
    
    /**
     * Configure le panel avec le héros et l'ennemi
     */
    public void setEncounter(Hero hero, Villain villain) {
        this.hero = hero;
        this.villain = villain;
        
        // Update labels
        heroNameLabel.setText(hero.getName() + " (" + hero.getHeroClass().getDisplayName() + ")");
        villainNameLabel.setText(villain.getName());
        
        // Update HP bars
        updateHpBars();
        
        messageLabel.setText("You strike... The light recoils.");
    }
    
    /**
     * Met à jour les barres de HP
     */
    public void updateHpBars() {
        if (hero != null) {
            int hpPercent = (int) ((double) hero.getHitPoints() / hero.getMaxHitPoints() * 100);
            heroHpBar.setValue(hpPercent);
            heroHpBar.setString("HP " + hero.getHitPoints() + "/" + hero.getMaxHitPoints());
        }
        
        if (villain != null) {
            int hpPercent = (int) ((double) villain.getHitPoints() / villain.getHitPoints() * 100);
            villainHpBar.setValue(hpPercent);
            villainHpBar.setString("HP " + villain.getHitPoints());
        }
    }
    
    /**
     * Définit le message affiché
     */
    public void setMessage(String message) {
        messageLabel.setText(message);
    }
    
    /**
     * Attend que l'utilisateur choisisse Fight ou Run
     */
    public String waitForChoice() {
        synchronized (choiceLock) {
            pendingChoice = null;
            try {
                choiceLock.wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return "R"; // Run par défaut si interrompu
            }
            return pendingChoice != null ? pendingChoice : "R";
        }
    }
    
    /**
     * Configure les listeners des boutons pour le système d'attente
     */
    public void setupChoiceWaiting() {
        // Supprimer les anciens listeners
        for (java.awt.event.ActionListener al : btnFight.getActionListeners()) {
            btnFight.removeActionListener(al);
        }
        for (java.awt.event.ActionListener al : btnRun.getActionListeners()) {
            btnRun.removeActionListener(al);
        }
        
        // Ajouter les nouveaux listeners
        btnFight.addActionListener(e -> notifyChoice("F"));
        btnRun.addActionListener(e -> notifyChoice("R"));
    }
    
    /**
     * Notifie qu'un choix a été fait
     */
    private void notifyChoice(String choice) {
        synchronized (choiceLock) {
            pendingChoice = choice;
            choiceLock.notifyAll();
        }
    }
}