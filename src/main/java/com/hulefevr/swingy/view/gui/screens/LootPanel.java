package com.hulefevr.swingy.view.gui.screens;

import com.hulefevr.swingy.model.artifact.Artifact;

import javax.swing.*;
import java.awt.*;

/**
 * Panel pour afficher le loot après une victoire
 * Design inspiré de "Relic Unveiled"
 */
public class LootPanel extends JPanel {
    private Artifact artifact;
    
    private JLabel titleLabel;
    private JLabel artifactNameLabel;
    private JLabel descriptionLabel;
    private JLabel hintLabel;
    private JButton btnClaim;
    private JButton btnLeave;
    
    // Pour gérer l'attente de choix
    private final Object choiceLock = new Object();
    private String pendingChoice = null;
    
    // Couleurs du thème
    private static final Color PARCHMENT = new Color(220, 205, 175);
    private static final Color DARK_BROWN = new Color(40, 30, 20);
    private static final Color BORDER_GOLD = new Color(160, 140, 90);
    
    public LootPanel() {
        setLayout(new BorderLayout());
        setBackground(DARK_BROWN);
        
        initComponents();
        layoutComponents();
        setupKeyBindings();
    }
    
    private void initComponents() {
        // Title
        titleLabel = new JLabel("RELIC UNVEILED", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 28));
        titleLabel.setForeground(DARK_BROWN);
        titleLabel.setOpaque(true);
        titleLabel.setBackground(PARCHMENT);
        
        // Artifact name
        artifactNameLabel = new JLabel("Artifact Name", SwingConstants.LEFT);
        artifactNameLabel.setFont(new Font("Serif", Font.BOLD, 20));
        artifactNameLabel.setForeground(DARK_BROWN);
        artifactNameLabel.setOpaque(false);
        
        // Description
        descriptionLabel = new JLabel("<html><i>\"A fragment of what you were.\"</i></html>");
        descriptionLabel.setFont(new Font("Serif", Font.ITALIC, 16));
        descriptionLabel.setForeground(new Color(60, 50, 40));
        descriptionLabel.setOpaque(false);
        
        // Hint
        hintLabel = new JLabel("<html><i>hint: If fate refuses, steel will answer.</i></html>");
        hintLabel.setFont(new Font("Serif", Font.ITALIC, 12));
        hintLabel.setForeground(new Color(80, 70, 60));
        hintLabel.setOpaque(false);
        
        // Buttons
        btnClaim = createButton("CLAIM IT");
        btnLeave = createButton("LEAVE IT");
    }
    
    private JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Serif", Font.BOLD, 16));
        btn.setBackground(DARK_BROWN);
        btn.setForeground(PARCHMENT);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_GOLD, 3),
            BorderFactory.createEmptyBorder(12, 35, 12, 35)
        ));
        return btn;
    }
    
    private void layoutComponents() {
        // Main panel avec bordures
        JPanel mainContent = new JPanel(new BorderLayout(15, 15));
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
        
        // Center: Content
        JPanel centerPanel = new JPanel(new BorderLayout(20, 20));
        centerPanel.setOpaque(false);
        
        // Left: Gravure placeholder
        JPanel gravurePanel = new JPanel();
        gravurePanel.setBackground(new Color(50, 50, 50));
        gravurePanel.setPreferredSize(new Dimension(400, 450));
        gravurePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_GOLD, 3),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        // Placeholder pour la gravure
        JLabel gravureLabel = new JLabel("⚜", SwingConstants.CENTER);
        gravureLabel.setFont(new Font("Serif", Font.BOLD, 120));
        gravureLabel.setForeground(PARCHMENT);
        gravurePanel.add(gravureLabel);
        
        centerPanel.add(gravurePanel, BorderLayout.WEST);
        
        // Right: Artifact info
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(30, 20, 30, 20));
        
        // Artifact name
        JPanel namePanel = new JPanel(new BorderLayout());
        namePanel.setOpaque(false);
        namePanel.add(artifactNameLabel, BorderLayout.NORTH);
        namePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        
        // Description area
        JPanel descPanel = new JPanel(new BorderLayout());
        descPanel.setOpaque(false);
        descPanel.add(descriptionLabel, BorderLayout.NORTH);
        descPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 80, 0));
        
        // Second description line (repeated for emphasis)
        JLabel descLabel2 = new JLabel("<html><i>\"A fragment of what you were.\"</i></html>");
        descLabel2.setFont(new Font("Serif", Font.ITALIC, 16));
        descLabel2.setForeground(new Color(60, 50, 40));
        JPanel desc2Panel = new JPanel(new BorderLayout());
        desc2Panel.setOpaque(false);
        desc2Panel.add(descLabel2, BorderLayout.NORTH);
        desc2Panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 40, 0));
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        buttonPanel.setOpaque(false);
        buttonPanel.add(btnClaim);
        buttonPanel.add(btnLeave);
        
        // Hint at bottom
        JPanel hintPanel = new JPanel(new BorderLayout());
        hintPanel.setOpaque(false);
        hintPanel.add(hintLabel, BorderLayout.NORTH);
        hintPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));
        
        infoPanel.add(namePanel);
        infoPanel.add(descPanel);
        infoPanel.add(desc2Panel);
        infoPanel.add(buttonPanel);
        infoPanel.add(Box.createVerticalGlue());
        infoPanel.add(hintPanel);
        
        centerPanel.add(infoPanel, BorderLayout.CENTER);
        
        mainContent.add(centerPanel, BorderLayout.CENTER);
        
        add(mainContent, BorderLayout.CENTER);
    }
    
    /**
     * Configure le panel avec l'artefact
     */
    public void setArtifact(Artifact artifact) {
        this.artifact = artifact;
        
        if (artifact != null) {
            // Formater le nom avec le bonus
            String bonus = "";
            if (artifact.getType() != null) {
                switch (artifact.getType()) {
                    case WEAPON:
                        bonus = " (+" + artifact.getBonusValue() + " ATK)";
                        break;
                    case ARMOR:
                        bonus = " (+" + artifact.getBonusValue() + " DEF)";
                        break;
                    case HELM:
                        bonus = " (+" + artifact.getBonusValue() + " HP)";
                        break;
                }
            }
            
            artifactNameLabel.setText(artifact.getName() + bonus);
        }
    }
    
    /**
     * Attend que l'utilisateur choisisse Claim ou Leave
     */
    public String waitForChoice() {
        synchronized (choiceLock) {
            pendingChoice = null;
            try {
                choiceLock.wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return "L"; // Leave par défaut si interrompu
            }
            return pendingChoice != null ? pendingChoice : "L";
        }
    }
    
    /**
     * Configure les listeners des boutons pour le système d'attente
     */
    public void setupChoiceWaiting() {
        // Supprimer les anciens listeners
        for (java.awt.event.ActionListener al : btnClaim.getActionListeners()) {
            btnClaim.removeActionListener(al);
        }
        for (java.awt.event.ActionListener al : btnLeave.getActionListeners()) {
            btnLeave.removeActionListener(al);
        }
        
        // Ajouter les nouveaux listeners
        btnClaim.addActionListener(e -> notifyChoice("T")); // T = Take
        btnLeave.addActionListener(e -> notifyChoice("L")); // L = Leave
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
    
    /**
     * Configure les raccourcis clavier T et L
     */
    private void setupKeyBindings() {
        InputMap inputMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getActionMap();
        
        // T pour CLAIM IT
        inputMap.put(KeyStroke.getKeyStroke('t'), "claim");
        inputMap.put(KeyStroke.getKeyStroke('T'), "claim");
        actionMap.put("claim", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                btnClaim.doClick();
            }
        });
        
        // L pour LEAVE IT
        inputMap.put(KeyStroke.getKeyStroke('l'), "leave");
        inputMap.put(KeyStroke.getKeyStroke('L'), "leave");
        actionMap.put("leave", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                btnLeave.doClick();
            }
        });
    }
}
