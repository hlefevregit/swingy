package com.hulefevr.swingy.view.gui.screens;

import com.hulefevr.swingy.validation.dto.CreateHeroInput;
import com.hulefevr.swingy.model.hero.HeroClass;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Panel for creating a hero (name + class). Blocks until user submits or cancels.
 * Redesigned with dark fantasy grimoire aesthetic.
 */
public class CreateHeroPanel extends JPanel {
    // Color palette
    private static final Color PARCHMENT = new Color(213, 199, 161);
    private static final Color DARK_BORDER = new Color(30, 24, 24);
    private static final Color INK_BLACK = new Color(34, 26, 22);
    private static final Color ERROR_RED = new Color(138, 52, 52);
    private static final Color GOLD_BUTTON = new Color(177, 160, 106);
    private static final Color GOLD_HOVER = new Color(140, 120, 70);
    private static final Color SIGIL_BG = new Color(20, 20, 24);
    private static final Color GOLD_BORDER = new Color(177, 160, 106);
    private static final Color PREVIEW_BG = new Color(199, 181, 144);
    
    private JTextField nameField;
    private ButtonGroup classGroup;
    private JLabel errorLabel;
    private JLabel previewLabel;
    private JLabel sigilIconLabel;
    private java.util.concurrent.CountDownLatch latch = new java.util.concurrent.CountDownLatch(1);
    private CreateHeroInput result;

    public CreateHeroPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(11, 11, 11));
        
        buildUI();
    }
    
    private void buildUI() {
        // Main card panel (parchment background)
        JPanel card = new JPanel(new BorderLayout(0, 15));
        card.setBackground(PARCHMENT);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(DARK_BORDER, 3),
            BorderFactory.createEmptyBorder(20, 30, 20, 30)
        ));
        
        // Title at top
        JLabel titleLabel = new JLabel("SUMMON A FALLEN SOUL", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 26));
        titleLabel.setForeground(INK_BLACK);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        card.add(titleLabel, BorderLayout.NORTH);
        
        // Center: Sigil (left) + Form (right)
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 0, 20);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        gbc.weighty = 1;
        
        // LEFT: Sigil Panel
        JPanel sigilPanel = createSigilPanel();
        centerPanel.add(sigilPanel, gbc);
        
        // RIGHT: Form Panel
        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.insets = new Insets(0, 0, 0, 0);
        JPanel formPanel = createFormPanel();
        centerPanel.add(formPanel, gbc);
        
        card.add(centerPanel, BorderLayout.CENTER);
        
        // Bottom: Buttons
        JPanel buttonPanel = createButtonPanel();
        card.add(buttonPanel, BorderLayout.SOUTH);
        
        // Add card to main panel with padding
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setOpaque(false);
        GridBagConstraints wrapperGbc = new GridBagConstraints();
        wrapperGbc.insets = new Insets(40, 60, 40, 60);
        wrapper.add(card, wrapperGbc);
        
        add(wrapper, BorderLayout.CENTER);
    }
    
    private JPanel createSigilPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(260, 320));
        panel.setBackground(SIGIL_BG);
        panel.setBorder(BorderFactory.createLineBorder(GOLD_BORDER, 2));
        
        // Icon/Symbol in center
        sigilIconLabel = new JLabel("☩", SwingConstants.CENTER);
        sigilIconLabel.setFont(new Font("Serif", Font.BOLD, 100));
        sigilIconLabel.setForeground(GOLD_BORDER);
        
        panel.add(sigilIconLabel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.insets = new Insets(0, 0, 8, 0);
        
        // Name label
        JLabel nameLabel = new JLabel("Name (ink it):");
        nameLabel.setFont(new Font("Serif", Font.PLAIN, 14));
        nameLabel.setForeground(INK_BLACK);
        panel.add(nameLabel, gbc);
        
        // Name field
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 5, 0);
        nameField = new JTextField(20);
        nameField.setFont(new Font("Serif", Font.PLAIN, 14));
        nameField.setBackground(new Color(245, 240, 230));
        nameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(DARK_BORDER, 1),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        panel.add(nameField, gbc);
        
        // Error label
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 15, 0);
        errorLabel = new JLabel("");
        errorLabel.setFont(new Font("Serif", Font.ITALIC, 12));
        errorLabel.setForeground(ERROR_RED);
        panel.add(errorLabel, gbc);
        
        // Calling label
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 8, 0);
        JLabel callingLabel = new JLabel("Calling:");
        callingLabel.setFont(new Font("Serif", Font.BOLD, 14));
        callingLabel.setForeground(INK_BLACK);
        panel.add(callingLabel, gbc);
        
        // Preview panel (create BEFORE radios so previewLabel exists)
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 15, 0);
        JPanel previewPanel = createPreviewPanel();
        
        // Radio buttons for classes (this will call updateClassPreview)
        JPanel radiosPanel = createClassRadiosPanel();
        
        // Add radios first, then preview
        panel.add(radiosPanel, gbc);
        
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 0, 0);
        panel.add(previewPanel, gbc);
        
        return panel;
    }
    
    private JPanel createClassRadiosPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        
        classGroup = new ButtonGroup();
        boolean first = true;
        
        for (HeroClass hc : HeroClass.values()) {
            JRadioButton rb = new JRadioButton(hc.getDisplayName());
            rb.setActionCommand(hc.name());
            rb.setFont(new Font("Serif", Font.PLAIN, 13));
            rb.setForeground(INK_BLACK);
            rb.setOpaque(false);
            rb.setFocusPainted(false);
            
            if (first) {
                rb.setSelected(true);
                updateClassPreview(hc);
                first = false;
            }
            
            // Update preview when class changes
            rb.addActionListener(e -> {
                if (rb.isSelected()) {
                    updateClassPreview(hc);
                }
            });
            
            classGroup.add(rb);
            panel.add(rb);
            panel.add(Box.createVerticalStrut(5));
        }
        
        return panel;
    }
    
    private JPanel createPreviewPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(PREVIEW_BG);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(DARK_BORDER, 1),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        
        JLabel titleLabel = new JLabel("Class Preview:");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 12));
        titleLabel.setForeground(INK_BLACK);
        
        previewLabel = new JLabel("ATK 10   DEF 8   HP 100");
        previewLabel.setFont(new Font("Serif", Font.PLAIN, 13));
        previewLabel.setForeground(INK_BLACK);
        
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);
        content.add(titleLabel);
        content.add(Box.createVerticalStrut(5));
        content.add(previewLabel);
        
        panel.add(content, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        
        // Left: Debug button (small, discreet)
        JButton debugBtn = new JButton("DEBUG LV14");
        debugBtn.setFont(new Font("Serif", Font.PLAIN, 10));
        debugBtn.setForeground(new Color(100, 100, 100));
        debugBtn.setBackground(new Color(180, 180, 180));
        debugBtn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(120, 120, 120), 1),
            BorderFactory.createEmptyBorder(3, 8, 3, 8)
        ));
        debugBtn.setFocusPainted(false);
        debugBtn.addActionListener(e -> onDebugHero());
        
        // Right: Main buttons
        JPanel rightButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightButtons.setOpaque(false);
        
        JButton backBtn = createStyledButton("BACK");
        JButton summonBtn = createStyledButton("SUMMON");
        
        backBtn.addActionListener(e -> onBack());
        summonBtn.addActionListener(e -> onSummon());
        
        rightButtons.add(backBtn);
        rightButtons.add(summonBtn);
        
        panel.add(debugBtn, BorderLayout.WEST);
        panel.add(rightButtons, BorderLayout.EAST);
        
        return panel;
    }
    
    private JButton createStyledButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Serif", Font.BOLD, 14));
        btn.setForeground(new Color(16, 16, 16));
        btn.setBackground(GOLD_BUTTON);
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(DARK_BORDER, 1),
            BorderFactory.createEmptyBorder(6, 16, 6, 16)
        ));
        btn.setFocusPainted(false);
        
        // Hover effect
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(GOLD_HOVER);
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(GOLD_BUTTON);
            }
        });
        
        return btn;
    }
    
    private void updateClassPreview(HeroClass heroClass) {
        previewLabel.setText(String.format("ATK %d   DEF %d   HP %d",
            heroClass.getBaseAttack(),
            heroClass.getBaseDefense(),
            heroClass.getBaseHitPoints()));
        
        // Update sigil icon based on class
        String icon = "☩"; // Default
        switch (heroClass) {
            case EXILE:
                icon = "⚔";
                break;
            case REVENANT:
                icon = "☠";
                break;
            case PENITENT:
                icon = "✞";
                break;
            case WARDEN:
                icon = "⛨";
                break;
            case SORCERER:
                icon = "✦";
                break;
        }
        sigilIconLabel.setText(icon);
    }

    private void onSummon() {
        String name = nameField.getText();
        if (name == null || name.trim().isEmpty()) {
            errorLabel.setText("Name must not be blank.");
            return;
        }
        String selected = null;
        for (AbstractButton b : java.util.Collections.list(classGroup.getElements())) {
            if (b.isSelected()) {
                selected = b.getActionCommand();
                break;
            }
        }
        result = new CreateHeroInput(name.trim(), selected != null ? selected : HeroClass.EXILE.name());
        latch.countDown();
    }
    
    private void onDebugHero() {
        // Créer un héros debug niveau 14 avec le nom du champ ou un nom par défaut
        String name = nameField.getText();
        if (name == null || name.trim().isEmpty()) {
            name = "DebugHero";
        }
        // Utiliser une notation spéciale pour indiquer que c'est un héros debug
        result = new CreateHeroInput("DEBUG:" + name.trim(), HeroClass.REVENANT.name());
        latch.countDown();
    }

    private void onBack() {
        result = null;
        latch.countDown();
    }

    /**
     * Blocks until user selects Summon or Back and returns a CreateHeroInput or null if cancelled.
     * Caller MUST NOT be on the EDT.
     */
    public CreateHeroInput waitForResult() {
        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return result;
    }

    public void reset() {
        nameField.setText("");
        errorLabel.setText("");
        classGroup.clearSelection();
        // select first
        java.util.Enumeration<AbstractButton> en = classGroup.getElements();
        if (en.hasMoreElements()) en.nextElement().setSelected(true);
        latch = new java.util.concurrent.CountDownLatch(1);
        result = null;
    }
}
