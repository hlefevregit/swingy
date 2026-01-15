package com.hulefevr.swingy.view.gui.screens;

import com.hulefevr.swingy.model.artifact.Artifact;
import com.hulefevr.swingy.model.artifact.ArtifactType;
import com.hulefevr.swingy.model.hero.Hero;

import javax.swing.*;
import java.awt.*;

/**
 * Panel pour afficher le loot après une victoire
 * Design "Relic Unveiled" - style grimoire dark fantasy
 */
public class LootPanel extends JPanel {
    private Hero hero;
    private Artifact artifact;
    
    // UI Components
    private JLabel relicIconLabel;
    private JLabel relicNameLabel;
    private JLabel relicTypeLabel;
    private JLabel currentItemLabel;
    private JLabel comparisonAtkLabel;
    private JLabel comparisonDefLabel;
    private JLabel comparisonHpLabel;
    private JLabel inventorySummaryLabel;
    private JLabel loreLabel;
    private JLabel hintLabel;
    private JButton btnClaim;
    private JButton btnLeave;
    
    // Pour gérer l'attente de choix
    private final Object choiceLock = new Object();
    private String pendingChoice = null;
    
    // Couleurs du thème
    private static final Color PARCHMENT = new Color(213, 199, 161);
    private static final Color DARK_BORDER = new Color(30, 24, 24);
    private static final Color CARD_BG = new Color(20, 20, 24);
    private static final Color GOLD_BORDER = new Color(177, 160, 106);
    private static final Color ICON_COLOR = new Color(232, 214, 168);
    private static final Color BUTTON_GOLD = new Color(177, 160, 106);
    private static final Color HINT_GRAY = new Color(85, 85, 85);
    
    public LootPanel() {
        setLayout(new BorderLayout());
        setBackground(PARCHMENT);
        setBorder(BorderFactory.createLineBorder(DARK_BORDER, 3));
        
        buildUI();
        setupKeyBindings();
    }
    
    private void buildUI() {
        // Main container with padding
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBackground(PARCHMENT);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        
        // Top section: Left (Relic Card) + Right (Info/Comparison)
        JPanel topSection = new JPanel(new BorderLayout(20, 0));
        topSection.setOpaque(false);
        
        // LEFT: Relic Card (300x380)
        JPanel relicCard = createRelicCard();
        topSection.add(relicCard, BorderLayout.WEST);
        
        // RIGHT: Info Panel
        JPanel infoPanel = createInfoPanel();
        topSection.add(infoPanel, BorderLayout.CENTER);
        
        mainPanel.add(topSection, BorderLayout.CENTER);
        
        // Bottom section: Inventory Summary + Lore + Buttons
        JPanel bottomSection = createBottomSection();
        mainPanel.add(bottomSection, BorderLayout.SOUTH);
        
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private JPanel createRelicCard() {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(CARD_BG);
        card.setPreferredSize(new Dimension(300, 380));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GOLD_BORDER, 2),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        // Icon in center
        relicIconLabel = new JLabel("⚔", SwingConstants.CENTER);
        relicIconLabel.setFont(new Font("Serif", Font.BOLD, 120));
        relicIconLabel.setForeground(ICON_COLOR);
        
        card.add(relicIconLabel, BorderLayout.CENTER);
        
        return card;
    }
    
    private JPanel createInfoPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        // Relic Name
        relicNameLabel = new JLabel("Unknown Relic");
        relicNameLabel.setFont(new Font("Serif", Font.BOLD, 18));
        relicNameLabel.setForeground(DARK_BORDER);
        relicNameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Relic Type
        relicTypeLabel = new JLabel("Type: Unknown");
        relicTypeLabel.setFont(new Font("Serif", Font.PLAIN, 14));
        relicTypeLabel.setForeground(DARK_BORDER);
        relicTypeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        relicTypeLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 15, 0));
        
        // Current Item
        currentItemLabel = new JLabel("Current: None");
        currentItemLabel.setFont(new Font("Serif", Font.PLAIN, 14));
        currentItemLabel.setForeground(new Color(80, 70, 60));
        currentItemLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        currentItemLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        // Comparison Labels
        comparisonAtkLabel = new JLabel("");
        comparisonAtkLabel.setFont(new Font("Serif", Font.PLAIN, 14));
        comparisonAtkLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        comparisonDefLabel = new JLabel("");
        comparisonDefLabel.setFont(new Font("Serif", Font.PLAIN, 14));
        comparisonDefLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        comparisonHpLabel = new JLabel("");
        comparisonHpLabel.setFont(new Font("Serif", Font.PLAIN, 14));
        comparisonHpLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        panel.add(relicNameLabel);
        panel.add(relicTypeLabel);
        panel.add(currentItemLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(comparisonAtkLabel);
        panel.add(comparisonDefLabel);
        panel.add(comparisonHpLabel);
        panel.add(Box.createVerticalGlue());
        
        return panel;
    }
    
    private JPanel createBottomSection() {
        JPanel bottom = new JPanel(new BorderLayout(20, 10));
        bottom.setOpaque(false);
        bottom.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        // LEFT: Inventory Summary
        inventorySummaryLabel = new JLabel("<html>Current Stats:<br>ATK: 0 | DEF: 0 | HP: 0</html>");
        inventorySummaryLabel.setFont(new Font("Serif", Font.PLAIN, 12));
        inventorySummaryLabel.setForeground(new Color(80, 70, 60));
        
        // CENTER: Lore Text
        loreLabel = new JLabel("<html><i>\"A fragment of what you were.\"</i></html>", SwingConstants.CENTER);
        loreLabel.setFont(new Font("Serif", Font.ITALIC, 16));
        loreLabel.setForeground(new Color(60, 50, 40));
        
        // RIGHT: Buttons + Hint
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setOpaque(false);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);
        
        btnClaim = createStyledButton("CLAIM IT");
        btnLeave = createStyledButton("LEAVE IT");
        
        buttonPanel.add(btnClaim);
        buttonPanel.add(btnLeave);
        
        hintLabel = new JLabel("<html><i>hint: If fate refuses, steel will answer.</i></html>", SwingConstants.RIGHT);
        hintLabel.setFont(new Font("Serif", Font.ITALIC, 12));
        hintLabel.setForeground(HINT_GRAY);
        hintLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        hintLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        
        rightPanel.add(buttonPanel);
        rightPanel.add(hintLabel);
        
        bottom.add(inventorySummaryLabel, BorderLayout.WEST);
        bottom.add(loreLabel, BorderLayout.CENTER);
        bottom.add(rightPanel, BorderLayout.EAST);
        
        return bottom;
    }
    
    private JButton createStyledButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Serif", Font.BOLD, 14));
        btn.setBackground(BUTTON_GOLD);
        btn.setForeground(DARK_BORDER);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(DARK_BORDER, 2),
            BorderFactory.createEmptyBorder(10, 25, 10, 25)
        ));
        return btn;
    }
    
    /**
     * Update the panel with new loot information
     */
    public void updateLoot(Hero hero, Artifact newArtifact) {
        this.hero = hero;
        this.artifact = newArtifact;
        
        if (newArtifact == null) {
            return;
        }
        
        // Update relic icon based on type
        String icon = "⚔"; // Default weapon
        if (newArtifact.getType() != null) {
            switch (newArtifact.getType()) {
                case WEAPON:
                    icon = "⚔";
                    break;
                case ARMOR:
                    icon = "⛨";
                    break;
                case HELM:
                    icon = "♕";
                    break;
            }
        }
        relicIconLabel.setText(icon);
        
        // Update relic name and type
        relicNameLabel.setText(newArtifact.getName());
        
        String typeText = "Type: ";
        if (newArtifact.getType() != null) {
            switch (newArtifact.getType()) {
                case WEAPON:
                    typeText += "Weapon";
                    break;
                case ARMOR:
                    typeText += "Armor";
                    break;
                case HELM:
                    typeText += "Helm";
                    break;
            }
        }
        relicTypeLabel.setText(typeText);
        
        // Get current equipped item
        Artifact currentItem = null;
        String currentItemText = "Current: None";
        
        if (hero != null && newArtifact.getType() != null) {
            switch (newArtifact.getType()) {
                case WEAPON:
                    currentItem = hero.getWeapon();
                    if (currentItem != null) {
                        currentItemText = "Current: " + currentItem.getName();
                    }
                    break;
                case ARMOR:
                    currentItem = hero.getArmor();
                    if (currentItem != null) {
                        currentItemText = "Current: " + currentItem.getName();
                    }
                    break;
                case HELM:
                    currentItem = hero.getHelm();
                    if (currentItem != null) {
                        currentItemText = "Current: " + currentItem.getName();
                    }
                    break;
            }
        }
        currentItemLabel.setText(currentItemText);
        
        // Calculate stat changes
        int currentBonus = currentItem != null ? currentItem.getBonusValue() : 0;
        int newBonus = newArtifact.getBonusValue();
        int diff = newBonus - currentBonus;
        
        String diffColor = diff > 0 ? "#228B22" : (diff < 0 ? "#8B0000" : "#555555");
        String diffSign = diff > 0 ? "+" : "";
        
        // Update comparison labels
        if (newArtifact.getType() == ArtifactType.WEAPON) {
            comparisonAtkLabel.setText("<html>ATK: " + currentBonus + " → " + newBonus + 
                " <span style='color:" + diffColor + ";'>(" + diffSign + diff + ")</span></html>");
            comparisonDefLabel.setText("");
            comparisonHpLabel.setText("");
        } else if (newArtifact.getType() == ArtifactType.ARMOR) {
            comparisonAtkLabel.setText("");
            comparisonDefLabel.setText("<html>DEF: " + currentBonus + " → " + newBonus + 
                " <span style='color:" + diffColor + ";'>(" + diffSign + diff + ")</span></html>");
            comparisonHpLabel.setText("");
        } else if (newArtifact.getType() == ArtifactType.HELM) {
            comparisonAtkLabel.setText("");
            comparisonDefLabel.setText("");
            comparisonHpLabel.setText("<html>HP: " + currentBonus + " → " + newBonus + 
                " <span style='color:" + diffColor + ";'>(" + diffSign + diff + ")</span></html>");
        }
        
        // Update inventory summary
        if (hero != null) {
            int totalAtk = hero.getAttack();
            int totalDef = hero.getDefense();
            int totalHp = hero.getMaxHitPoints();
            
            inventorySummaryLabel.setText("<html>Current Stats:<br>ATK: " + totalAtk + 
                " | DEF: " + totalDef + " | HP: " + totalHp + "</html>");
        }
    }
    
    /**
     * Configure le panel avec l'artefact (legacy method)
     */
    public void setArtifact(Artifact artifact) {
        updateLoot(null, artifact);
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
