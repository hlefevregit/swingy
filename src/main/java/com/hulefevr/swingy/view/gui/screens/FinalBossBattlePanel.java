package com.hulefevr.swingy.view.gui.screens;

import com.hulefevr.swingy.model.hero.Hero;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.CountDownLatch;

/**
 * Panel pour afficher le combat final "Heaven vs Abyss"
 * Combat épique entre l'Archange et le héros
 */
public class FinalBossBattlePanel extends JPanel {
    private Hero hero;
    
    private JLabel archangelNameLabel;
    private JLabel archangelStatsLabel;
    private JLabel heroNameLabel;
    private JProgressBar heroHpBar;
    private JTextArea messageArea;
    private JButton btnNextTurn;
    private BackgroundPanel backgroundPanel;
    
    private CountDownLatch actionLatch;
    
    // Couleurs du thème
    private static final Color PARCHMENT = new Color(245, 238, 228);
    private static final Color DARK_STONE = new Color(46, 44, 40);
    private static final Color BORDER_DARK = new Color(80, 76, 70);
    private static final Color HP_RED = new Color(139, 0, 0);
    private static final Color HP_BG = new Color(60, 60, 60);
    
    public FinalBossBattlePanel() {
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
        
        // Background panel avec l'image AzraelVersus
        backgroundPanel = new BackgroundPanel();
        backgroundPanel.setLayout(new BorderLayout());
        
        // Top: Title sections
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        
        JPanel titlesRow = new JPanel(new BorderLayout());
        titlesRow.setOpaque(true);
        titlesRow.setBackground(PARCHMENT);
        titlesRow.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, BORDER_DARK));
        
        // Heaven label (left)
        JLabel heavenLabel = new JLabel("HEAVEN", SwingConstants.CENTER);
        heavenLabel.setFont(new Font("Serif", Font.BOLD, 28));
        heavenLabel.setForeground(DARK_STONE);
        heavenLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        // Abyss label (right)
        JLabel abyssLabel = new JLabel("ABYSS", SwingConstants.CENTER);
        abyssLabel.setFont(new Font("Serif", Font.BOLD, 28));
        abyssLabel.setForeground(DARK_STONE);
        abyssLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        titlesRow.add(heavenLabel, BorderLayout.WEST);
        titlesRow.add(abyssLabel, BorderLayout.EAST);
        topPanel.add(titlesRow, BorderLayout.NORTH);
        
        // Hero panel en haut à droite
        JPanel topRightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 40, 15));
        topRightPanel.setOpaque(false);
        
        JPanel heroPanel = createStatPanel();
        JPanel heroContent = new JPanel(new BorderLayout(3, 3));
        heroContent.setOpaque(false);
        
        heroNameLabel = new JLabel("Azrael (Revenant)");
        heroNameLabel.setFont(new Font("Serif", Font.BOLD, 13));
        heroNameLabel.setForeground(DARK_STONE);
        
        heroHpBar = createHpBar();
        
        heroContent.add(heroNameLabel, BorderLayout.NORTH);
        heroContent.add(heroHpBar, BorderLayout.CENTER);
        heroPanel.add(heroContent, BorderLayout.CENTER);
        
        topRightPanel.add(heroPanel);
        topPanel.add(topRightPanel, BorderLayout.CENTER);
        
        backgroundPanel.add(topPanel, BorderLayout.NORTH);
        
        // Center: Spacer pour laisser voir l'image
        JPanel centerSpacer = new JPanel();
        centerSpacer.setOpaque(false);
        backgroundPanel.add(centerSpacer, BorderLayout.CENTER);
        
        // Bottom: Archangel panel + Message + Button
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 40, 15, 40));
        
        // Archangel panel en bas à gauche
        JPanel bottomLeftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        bottomLeftPanel.setOpaque(false);
        
        JPanel archangelPanel = createStatPanel();
        JPanel archangelContent = new JPanel();
        archangelContent.setLayout(new BoxLayout(archangelContent, BoxLayout.Y_AXIS));
        archangelContent.setOpaque(false);
        
        archangelNameLabel = new JLabel("ARCHANGEL (Judgment)");
        archangelNameLabel.setFont(new Font("Serif", Font.BOLD, 13));
        archangelNameLabel.setForeground(DARK_STONE);
        archangelNameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        archangelStatsLabel = new JLabel("<html>ATK: Absolute<br/>DEF: Absolute<br/>HP: Unmeasurable</html>");
        archangelStatsLabel.setFont(new Font("Serif", Font.PLAIN, 11));
        archangelStatsLabel.setForeground(DARK_STONE);
        archangelStatsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        archangelContent.add(archangelNameLabel);
        archangelContent.add(Box.createRigidArea(new Dimension(0, 3)));
        archangelContent.add(archangelStatsLabel);
        archangelPanel.add(archangelContent);
        
        bottomLeftPanel.add(archangelPanel);
        bottomPanel.add(bottomLeftPanel, BorderLayout.NORTH);
        
        // Message area + button
        JPanel messagePanel = new JPanel(new BorderLayout());
        messagePanel.setOpaque(false);
        messagePanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        messageArea = new JTextArea();
        messageArea.setText("\"The radiant sword descends—a judgment total.\n\nThe Archangel obliterates you, leaving ash and silence.\"");
        messageArea.setEditable(false);
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        messageArea.setFont(new Font("Serif", Font.ITALIC, 15));
        messageArea.setForeground(DARK_STONE);
        messageArea.setOpaque(true);
        messageArea.setBackground(new Color(245, 238, 228, 220));
        messageArea.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        messageArea.setRows(3);
        
        messagePanel.add(messageArea, BorderLayout.CENTER);
        
        // Button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
        buttonPanel.setOpaque(false);
        
        btnNextTurn = createButton("NEXT TURN");
        btnNextTurn.addActionListener(e -> recordAction());
        
        buttonPanel.add(btnNextTurn);
        messagePanel.add(buttonPanel, BorderLayout.SOUTH);
        
        bottomPanel.add(messagePanel, BorderLayout.CENTER);
        backgroundPanel.add(bottomPanel, BorderLayout.SOUTH);
        mainPanel.add(backgroundPanel, BorderLayout.CENTER);
        
        add(mainPanel, BorderLayout.CENTER);
        
        setupKeyBindings();
    }
    
    private JPanel createStatPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(true);
        panel.setBackground(PARCHMENT);
        panel.setPreferredSize(new Dimension(320, 105));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_DARK, 3),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
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
                BorderFactory.createEmptyBorder(12, 50, 12, 50)
        ));
        return btn;
    }
    
    private void recordAction() {
        if (actionLatch != null) {
            actionLatch.countDown();
        }
    }
    
    public void setHero(Hero hero) {
        this.hero = hero;
        if (hero != null) {
            heroNameLabel.setText(hero.getName() + " (" + hero.getHeroClass().getDisplayName() + ")");
            updateHpBar();
        }
    }
    
    public void updateHpBar() {
        if (hero != null) {
            int hpPercent = (int) ((double) hero.getHitPoints() / hero.getMaxHitPoints() * 100);
            heroHpBar.setValue(hpPercent);
            heroHpBar.setString("HP: " + hero.getHitPoints() + "/" + hero.getMaxHitPoints());
        }
    }
    
    public void setMessage(String message) {
        messageArea.setText(message);
    }
    
    public void waitForNextTurn() {
        actionLatch = new CountDownLatch(1);
        try {
            actionLatch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        actionLatch = null;
    }
    
    private void setupKeyBindings() {
        InputMap inputMap = getInputMap(WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getActionMap();
        
        inputMap.put(KeyStroke.getKeyStroke("ENTER"), "nextTurn");
        inputMap.put(KeyStroke.getKeyStroke("SPACE"), "nextTurn");
        actionMap.put("nextTurn", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                btnNextTurn.doClick();
            }
        });
    }
    
    /**
     * Panel avec image de fond AzraelVersus
     */
    private class BackgroundPanel extends JPanel {
        private Image backgroundImage;
        
        public BackgroundPanel() {
            try {
                java.net.URL imageURL = getClass().getResource("/images/AzraelVersus.png");
                if (imageURL != null) {
                    backgroundImage = new ImageIcon(imageURL).getImage();
                }
            } catch (Exception e) {
                System.err.println("Could not load AzraelVersus background image: " + e.getMessage());
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
