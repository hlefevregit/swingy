package com.hulefevr.swingy.view.gui.screens;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.CountDownLatch;

/**
 * Panel épique de montée de niveau - style "The Book of the Fallen"
 */
public class LevelUpPanel extends JPanel {
    private JLabel titleLabel;
    private JTextArea messageArea;
    private JLabel levelLabel;
    private JButton continueButton;
    
    private CountDownLatch latch;
    
    private static final Color PARCHMENT = new Color(245, 238, 228);
    private static final Color DARK_STONE = new Color(46, 44, 40);
    private static final Color BORDER_DARK = new Color(80, 76, 70);
    
    public LevelUpPanel() {
        setLayout(new BorderLayout());
        setBackground(DARK_STONE);
        buildUI();
    }
    
    private void buildUI() {
        // Main content panel with borders
        JPanel mainPanel = new JPanel(new BorderLayout(0, 20));
        mainPanel.setBackground(PARCHMENT);
        mainPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_DARK, 8),
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(DARK_STONE, 3),
                        BorderFactory.createCompoundBorder(
                                BorderFactory.createLineBorder(BORDER_DARK, 2),
                                BorderFactory.createEmptyBorder(40, 60, 40, 60)
                        )
                )
        ));
        
        // Title: LEVEL ASCENDED
        titleLabel = new JLabel("LEVEL ASCENDED", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 42));
        titleLabel.setForeground(DARK_STONE);
        titleLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(2, 0, 2, 0, BORDER_DARK),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        
        // Center panel with decorative image area and message
        JPanel centerPanel = new JPanel(new BorderLayout(0, 30));
        centerPanel.setOpaque(false);
        
        // Decorative image area (placeholder for engraving-style art)
        JPanel imagePanel = new JPanel() {
            private Image backgroundImage;
            
            {
                // Charger l'image de fond
                try {
                    java.net.URL imageURL = getClass().getResource("/images/levelupraw.png");
                    if (imageURL != null) {
                        backgroundImage = new ImageIcon(imageURL).getImage();
                    }
                } catch (Exception e) {
                    System.err.println("Could not load level up background image: " + e.getMessage());
                }
            }
            
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                
                int w = getWidth();
                int h = getHeight();
                
                if (backgroundImage != null) {
                    // Dessiner l'image en la redimensionnant pour remplir le panel
                    g2.drawImage(backgroundImage, 0, 0, w, h, this);
                } else {
                    // Fallback: gradient si l'image n'est pas trouvée
                    GradientPaint gradient = new GradientPaint(
                            0, 0, new Color(235, 228, 218),
                            0, h, new Color(220, 210, 195)
                    );
                    g2.setPaint(gradient);
                    g2.fillRect(0, 0, w, h);
                    
                    // Draw subtle radial light effect
                    int centerX = w / 2;
                    int centerY = h / 2;
                    RadialGradientPaint radial = new RadialGradientPaint(
                            centerX, centerY - 50,
                            Math.min(w, h) / 2.5f,
                            new float[]{0f, 0.5f, 1f},
                            new Color[]{
                                    new Color(255, 255, 240, 120),
                                    new Color(255, 255, 240, 40),
                                    new Color(255, 255, 240, 0)
                            }
                    );
                    g2.setPaint(radial);
                    g2.fillRect(0, 0, w, h);
                }
                
                // Draw border
                g2.setColor(BORDER_DARK);
                g2.setStroke(new BasicStroke(2));
                g2.drawRect(1, 1, w - 2, h - 2);
            }
        };
        imagePanel.setPreferredSize(new Dimension(700, 300));
        imagePanel.setOpaque(false);
        
        // Message area
        messageArea = new JTextArea();
        messageArea.setEditable(false);
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        messageArea.setFont(new Font("Serif", Font.PLAIN, 18));
        messageArea.setForeground(DARK_STONE);
        messageArea.setBackground(PARCHMENT);
        messageArea.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        messageArea.setOpaque(false);
        
        // Level label
        levelLabel = new JLabel("New Level: 2", SwingConstants.CENTER);
        levelLabel.setFont(new Font("Serif", Font.BOLD, 24));
        levelLabel.setForeground(DARK_STONE);
        
        // Combine message and level
        JPanel textPanel = new JPanel(new BorderLayout(0, 15));
        textPanel.setOpaque(false);
        textPanel.add(messageArea, BorderLayout.CENTER);
        textPanel.add(levelLabel, BorderLayout.SOUTH);
        
        centerPanel.add(imagePanel, BorderLayout.NORTH);
        centerPanel.add(textPanel, BorderLayout.CENTER);
        
        // Continue button
        continueButton = new JButton("CONTINUE");
        continueButton.setFont(new Font("Serif", Font.BOLD, 18));
        continueButton.setForeground(PARCHMENT);
        continueButton.setBackground(DARK_STONE);
        continueButton.setFocusPainted(false);
        continueButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_DARK, 3),
                BorderFactory.createEmptyBorder(12, 50, 12, 50)
        ));
        continueButton.addActionListener(e -> {
            if (latch != null) {
                latch.countDown();
            }
        });
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);
        buttonPanel.add(continueButton);
        
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Add main panel with some margin
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(DARK_STONE);
        wrapper.add(mainPanel);
        
        add(wrapper, BorderLayout.CENTER);
    }
    
    /**
     * Configure et affiche l'écran de level up
     */
    public void setLevelUp(int newLevel, String message) {
        levelLabel.setText("New Level: " + newLevel);
        messageArea.setText(message);
        messageArea.setCaretPosition(0);
    }
    
    /**
     * Attend que l'utilisateur clique sur CONTINUE
     * Doit être appelé depuis un thread non-EDT
     */
    public void waitForContinue() {
        latch = new CountDownLatch(1);
        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        latch = null;
    }
}
