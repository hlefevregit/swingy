package com.hulefevr.swingy.view.gui.screens;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.CountDownLatch;

/**
 * Panel pour afficher la fin épique "The Fall is Eternal"
 * Écran final avec la chute du héros
 */
public class EndingPanel extends JPanel {
    
    private JButton btnReturnToCover;
    private JButton btnQuit;
    private BackgroundPanel backgroundPanel;
    
    private CountDownLatch actionLatch;
    private String actionResult;
    
    // Couleurs du thème
    private static final Color PARCHMENT = new Color(245, 238, 228);
    private static final Color DARK_STONE = new Color(46, 44, 40);
    private static final Color BORDER_DARK = new Color(80, 76, 70);
    
    public EndingPanel() {
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
        
        // Background panel avec l'image Fall
        backgroundPanel = new BackgroundPanel();
        backgroundPanel.setLayout(new BorderLayout());
        
        // Title at top
        JLabel titleLabel = new JLabel("THE FALL IS ETERNAL", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 38));
        titleLabel.setForeground(DARK_STONE);
        titleLabel.setOpaque(true);
        titleLabel.setBackground(PARCHMENT);
        titleLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 3, 0, BORDER_DARK),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        backgroundPanel.add(titleLabel, BorderLayout.NORTH);
        
        // Center overlay pour le texte et les boutons
        JPanel centerOverlay = new JPanel(new BorderLayout());
        centerOverlay.setOpaque(false);
        centerOverlay.setBorder(BorderFactory.createEmptyBorder(200, 60, 20, 60));
        
        // Text area pour le message final
        JTextArea messageArea = new JTextArea();
        messageArea.setText("\"This is not punishment. This is correction.\"");
        messageArea.setEditable(false);
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        messageArea.setFont(new Font("Serif", Font.ITALIC, 22));
        messageArea.setForeground(DARK_STONE);
        messageArea.setOpaque(true);
        messageArea.setBackground(new Color(245, 238, 228, 230));
        messageArea.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        // Panel pour centrer le texte
        JPanel messagePanel = new JPanel();
        messagePanel.setOpaque(false);
        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));
        messagePanel.add(Box.createVerticalStrut(100));
        messagePanel.add(messageArea);
        messagePanel.add(Box.createRigidArea(new Dimension(0, 30)));
        
        centerOverlay.add(messagePanel, BorderLayout.CENTER);
        
        // Bottom: Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 15));
        buttonPanel.setOpaque(false);
        
        btnReturnToCover = createButton("RETURN TO COVER");
        btnReturnToCover.addActionListener(e -> recordAction("MENU"));
        
        btnQuit = createButton("QUIT");
        btnQuit.addActionListener(e -> recordAction("QUIT"));
        
        buttonPanel.add(btnReturnToCover);
        buttonPanel.add(btnQuit);
        centerOverlay.add(buttonPanel, BorderLayout.SOUTH);
        
        backgroundPanel.add(centerOverlay, BorderLayout.CENTER);
        mainPanel.add(backgroundPanel, BorderLayout.CENTER);
        
        add(mainPanel, BorderLayout.CENTER);
        
        setupKeyBindings();
    }
    
    private JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Serif", Font.BOLD, 18));
        btn.setForeground(PARCHMENT);
        btn.setBackground(DARK_STONE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_DARK, 4),
                BorderFactory.createEmptyBorder(12, 35, 12, 35)
        ));
        return btn;
    }
    
    private void recordAction(String action) {
        actionResult = action;
        if (actionLatch != null) {
            actionLatch.countDown();
        }
    }
    
    public String waitForAction() {
        actionLatch = new CountDownLatch(1);
        try {
            actionLatch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        String result = actionResult;
        actionResult = null;
        actionLatch = null;
        return result != null ? result : "MENU";
    }
    
    private void setupKeyBindings() {
        InputMap inputMap = getInputMap(WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getActionMap();
        
        inputMap.put(KeyStroke.getKeyStroke("ENTER"), "returnToCover");
        inputMap.put(KeyStroke.getKeyStroke('m'), "returnToCover");
        inputMap.put(KeyStroke.getKeyStroke('M'), "returnToCover");
        actionMap.put("returnToCover", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                btnReturnToCover.doClick();
            }
        });
        
        inputMap.put(KeyStroke.getKeyStroke('q'), "quit");
        inputMap.put(KeyStroke.getKeyStroke('Q'), "quit");
        inputMap.put(KeyStroke.getKeyStroke("ESCAPE"), "quit");
        actionMap.put("quit", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                btnQuit.doClick();
            }
        });
    }
    
    /**
     * Panel avec image de fond Fall
     */
    private class BackgroundPanel extends JPanel {
        private Image backgroundImage;
        
        public BackgroundPanel() {
            try {
                java.net.URL imageURL = getClass().getResource("/images/Fall.png");
                if (imageURL != null) {
                    backgroundImage = new ImageIcon(imageURL).getImage();
                }
            } catch (Exception e) {
                System.err.println("Could not load Fall background image: " + e.getMessage());
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
                // Fallback gradient sombre
                GradientPaint gradient = new GradientPaint(
                        0, 0, new Color(40, 40, 40),
                        0, getHeight(), new Color(20, 20, 20)
                );
                g2.setPaint(gradient);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        }
    }
}
