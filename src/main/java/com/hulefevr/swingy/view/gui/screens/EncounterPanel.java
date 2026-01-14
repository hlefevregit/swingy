package com.hulefevr.swingy.view.gui.screens;

import com.hulefevr.swingy.model.hero.Hero;
import com.hulefevr.swingy.model.ennemy.Villain;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.CountDownLatch;

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
    private JTextArea messageArea;
    private JButton btnFight;
    private JButton btnRun;
    private BackgroundPanel backgroundPanel;
    
    private CountDownLatch choiceLatch;
    private String choiceResult;
    
    // Couleurs du thème
    private static final Color PARCHMENT = new Color(245, 238, 228);
    private static final Color DARK_STONE = new Color(46, 44, 40);
    private static final Color BORDER_DARK = new Color(80, 76, 70);
    private static final Color HP_RED = new Color(139, 0, 0);
    private static final Color HP_BG = new Color(60, 60, 60);
    
    public EncounterPanel() {
        setLayout(new BorderLayout());
        setBackground(DARK_STONE);
        buildUI();
    }
    
    private void buildUI() {
        // Main content avec bordures épaisses
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setOpaque(false);
        mainPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(DARK_STONE, 12),
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDER_DARK, 4),
                        BorderFactory.createLineBorder(DARK_STONE, 3)
                )
        ));
        
        // Background panel avec l'image
        backgroundPanel = new BackgroundPanel();
        backgroundPanel.setLayout(new BorderLayout());
        
        // Title at top
        titleLabel = new JLabel("HEAVEN VS ABYSS", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 28));
        titleLabel.setForeground(DARK_STONE);
        titleLabel.setOpaque(true);
        titleLabel.setBackground(PARCHMENT);
        titleLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 3, 0, BORDER_DARK),
                BorderFactory.createEmptyBorder(12, 20, 12, 20)
        ));
        backgroundPanel.add(titleLabel, BorderLayout.NORTH);
        
        // Center overlay pour les infos de combat
        JPanel centerOverlay = new JPanel(new BorderLayout());
        centerOverlay.setOpaque(false);
        
        // Top row: Hero et Villain stats
        JPanel statsRow = new JPanel(new BorderLayout(20, 0));
        statsRow.setOpaque(false);
        statsRow.setBorder(BorderFactory.createEmptyBorder(20, 40, 0, 40));
        
        // Villain panel (left)
        JPanel villainPanel = createStatPanel(true);
        villainNameLabel = new JLabel("Watcher", SwingConstants.CENTER);
        villainNameLabel.setFont(new Font("Serif", Font.BOLD, 16));
        villainNameLabel.setForeground(DARK_STONE);
        villainHpBar = createHpBar();
        
        JPanel villainContent = new JPanel(new BorderLayout(5, 5));
        villainContent.setOpaque(false);
        villainContent.add(villainNameLabel, BorderLayout.NORTH);
        villainContent.add(villainHpBar, BorderLayout.CENTER);
        villainPanel.add(villainContent, BorderLayout.CENTER);
        
        // Hero panel (right)
        JPanel heroPanel = createStatPanel(false);
        heroNameLabel = new JLabel("Azrael (Revenant)", SwingConstants.CENTER);
        heroNameLabel.setFont(new Font("Serif", Font.BOLD, 16));
        heroNameLabel.setForeground(DARK_STONE);
        heroHpBar = createHpBar();
        
        JPanel heroContent = new JPanel(new BorderLayout(5, 5));
        heroContent.setOpaque(false);
        heroContent.add(heroNameLabel, BorderLayout.NORTH);
        heroContent.add(heroHpBar, BorderLayout.CENTER);
        heroPanel.add(heroContent, BorderLayout.CENTER);
        
        statsRow.add(villainPanel, BorderLayout.WEST);
        statsRow.add(heroPanel, BorderLayout.EAST);
        centerOverlay.add(statsRow, BorderLayout.NORTH);
        
        // Message area in the center-bottom
        JPanel messagePanel = new JPanel(new BorderLayout());
        messagePanel.setOpaque(false);
        messagePanel.setBorder(BorderFactory.createEmptyBorder(250, 60, 20, 60));
        
        messageArea = new JTextArea("You strike... The light recoils.");
        messageArea.setEditable(false);
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        messageArea.setFont(new Font("Serif", Font.ITALIC, 18));
        messageArea.setForeground(DARK_STONE);
        messageArea.setOpaque(true);
        messageArea.setBackground(new Color(245, 238, 228, 200));
        messageArea.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        messagePanel.add(messageArea, BorderLayout.CENTER);
        
        centerOverlay.add(messagePanel, BorderLayout.CENTER);
        
        // Bottom: Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 15));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        btnFight = createButton("FIGHT");
        btnRun = createButton("RUN (50%)");
        
        btnFight.addActionListener(e -> recordChoice("F"));
        btnRun.addActionListener(e -> recordChoice("R"));
        
        buttonPanel.add(btnFight);
        buttonPanel.add(btnRun);
        centerOverlay.add(buttonPanel, BorderLayout.SOUTH);
        
        backgroundPanel.add(centerOverlay, BorderLayout.CENTER);
        mainPanel.add(backgroundPanel, BorderLayout.CENTER);
        
        add(mainPanel, BorderLayout.CENTER);
        
        setupKeyBindings();
    }
    
    private JPanel createStatPanel(boolean isVillain) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(true);
        panel.setBackground(PARCHMENT);
        panel.setPreferredSize(new Dimension(280, 70));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_DARK, 3),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        return panel;
    }
    
    private JProgressBar createHpBar() {
        JProgressBar bar = new JProgressBar(0, 100);
        bar.setValue(100);
        bar.setStringPainted(true);
        bar.setForeground(HP_RED);
        bar.setBackground(HP_BG);
        bar.setString("HP");
        bar.setPreferredSize(new Dimension(0, 25));
        return bar;
    }
    
    private JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Serif", Font.BOLD, 18));
        btn.setForeground(PARCHMENT);
        btn.setBackground(DARK_STONE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_DARK, 3),
                BorderFactory.createEmptyBorder(12, 40, 12, 40)
        ));
        return btn;
    }
    
    private void recordChoice(String choice) {
        choiceResult = choice;
        if (choiceLatch != null) {
            choiceLatch.countDown();
        }
    }
    
    public void setEncounter(Hero hero, Villain villain) {
        this.hero = hero;
        this.villain = villain;
        
        heroNameLabel.setText(hero.getName() + " (" + hero.getHeroClass().getDisplayName() + ")");
        villainNameLabel.setText(villain.getName());
        
        updateHpBars();
        messageArea.setText("You strike... The light recoils.");
    }
    
    public void updateHpBars() {
        if (hero != null) {
            int hpPercent = (int) ((double) hero.getHitPoints() / hero.getMaxHitPoints() * 100);
            heroHpBar.setValue(hpPercent);
            heroHpBar.setString("HP");
        }
        
        if (villain != null) {
            int hpPercent = 100; // Villain HP is not tracked
            villainHpBar.setValue(hpPercent);
            villainHpBar.setString("HP");
        }
    }
    
    public void setMessage(String message) {
        messageArea.setText(message);
    }
    
    public String waitForChoice() {
        choiceLatch = new CountDownLatch(1);
        try {
            choiceLatch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        String result = choiceResult;
        choiceResult = null;
        choiceLatch = null;
        return result != null ? result : "R";
    }
    
    public void setupChoiceWaiting() {
        // Reset choice state
        choiceResult = null;
    }
    
    private void setupKeyBindings() {
        InputMap inputMap = getInputMap(WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getActionMap();
        
        inputMap.put(KeyStroke.getKeyStroke('f'), "fight");
        inputMap.put(KeyStroke.getKeyStroke('F'), "fight");
        actionMap.put("fight", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                btnFight.doClick();
            }
        });
        
        inputMap.put(KeyStroke.getKeyStroke('r'), "run");
        inputMap.put(KeyStroke.getKeyStroke('R'), "run");
        actionMap.put("run", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                btnRun.doClick();
            }
        });
    }
    
    /**
     * Panel avec image de fond
     */
    private class BackgroundPanel extends JPanel {
        private Image backgroundImage;
        
        public BackgroundPanel() {
            try {
                java.net.URL imageURL = getClass().getResource("/images/encounter.png");
                if (imageURL != null) {
                    backgroundImage = new ImageIcon(imageURL).getImage();
                }
            } catch (Exception e) {
                System.err.println("Could not load encounter background image: " + e.getMessage());
            }
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            
            if (backgroundImage != null) {
                g2.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            } else {
                // Fallback gradient
                GradientPaint gradient = new GradientPaint(
                        0, 0, new Color(60, 60, 60),
                        0, getHeight(), new Color(30, 30, 30)
                );
                g2.setPaint(gradient);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        }
    }
}
