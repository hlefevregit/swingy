package com.hulefevr.swingy.view.gui;

import javax.swing.*;
import java.awt.*;

/**
 * Panneau de splash - "The Book of the Fallen"
 */
public class SplashPanel extends JPanel {
    private final GuiWindow parent;
    private BackgroundPanel backgroundPanel;
    
    private static final Color PARCHMENT = new Color(245, 238, 228);
    private static final Color DARK_STONE = new Color(46, 44, 40);
    private static final Color BORDER_DARK = new Color(80, 76, 70);

    public SplashPanel(GuiWindow parent) {
        this.parent = parent;
        setLayout(new BorderLayout());
        setBackground(DARK_STONE);
        initUI();
    }

    private void initUI() {
        // Background panel avec l'image
        backgroundPanel = new BackgroundPanel();
        backgroundPanel.setLayout(new GridBagLayout());
        
        // Panel central avec le titre et le bouton
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(true);
        centerPanel.setBackground(PARCHMENT);
        centerPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_DARK, 4),
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(DARK_STONE, 2),
                        BorderFactory.createEmptyBorder(40, 60, 40, 60)
                )
        ));
        
        // Title: THE BOOK
        JLabel titleLine1 = new JLabel("THE BOOK", SwingConstants.CENTER);
        titleLine1.setFont(new Font("Serif", Font.BOLD, 32));
        titleLine1.setForeground(DARK_STONE);
        titleLine1.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // OF THE FALLEN
        JLabel titleLine2 = new JLabel("OF THE FALLEN", SwingConstants.CENTER);
        titleLine2.setFont(new Font("Serif", Font.BOLD, 38));
        titleLine2.setForeground(DARK_STONE);
        titleLine2.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLine2.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, BORDER_DARK));
        
        // Quote
        JLabel quote = new JLabel("— Let the ink bleed. —", SwingConstants.CENTER);
        quote.setFont(new Font("Serif", Font.ITALIC, 16));
        quote.setForeground(DARK_STONE);
        quote.setAlignmentX(Component.CENTER_ALIGNMENT);
        quote.setBorder(BorderFactory.createEmptyBorder(15, 0, 25, 0));
        
        // Button
        JButton openBtn = new JButton("OPEN THE BOOK");
        openBtn.setFont(new Font("Serif", Font.BOLD, 16));
        openBtn.setForeground(PARCHMENT);
        openBtn.setBackground(DARK_STONE);
        openBtn.setFocusPainted(false);
        openBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        openBtn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_DARK, 3),
                BorderFactory.createEmptyBorder(12, 40, 12, 40)
        ));
        openBtn.addActionListener(e -> parent.showMainMenu());
        
        // Assemble title panel
        centerPanel.add(Box.createVerticalGlue());
        centerPanel.add(titleLine1);
        centerPanel.add(Box.createVerticalStrut(5));
        centerPanel.add(titleLine2);
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(quote);
        centerPanel.add(openBtn);
        centerPanel.add(Box.createVerticalGlue());
        
        // Set max size to prevent stretching
        centerPanel.setMaximumSize(new Dimension(500, 300));
        
        backgroundPanel.add(centerPanel);
        add(backgroundPanel, BorderLayout.CENTER);
        
        // Footer info
        JPanel footer = new JPanel(new BorderLayout());
        footer.setOpaque(false);
        footer.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        JLabel versionLeft = new JLabel("Edition of Ash • v1.0 • GUI Mode");
        versionLeft.setFont(new Font("Serif", Font.PLAIN, 12));
        versionLeft.setForeground(new Color(200, 200, 200));
        
        JLabel versionRight = new JLabel("Loading souls...");
        versionRight.setFont(new Font("Serif", Font.ITALIC, 12));
        versionRight.setForeground(new Color(200, 200, 200));
        
        footer.add(versionLeft, BorderLayout.WEST);
        footer.add(versionRight, BorderLayout.EAST);
        
        add(footer, BorderLayout.SOUTH);
    }
    
    /**
     * Panel avec image de fond
     */
    private class BackgroundPanel extends JPanel {
        private Image backgroundImage;
        
        public BackgroundPanel() {
            try {
                java.net.URL imageURL = getClass().getResource("/images/SorrowfulAngelsOnARock.png");
                if (imageURL != null) {
                    backgroundImage = new ImageIcon(imageURL).getImage();
                }
            } catch (Exception e) {
                System.err.println("Could not load splash background image: " + e.getMessage());
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
                        0, 0, new Color(40, 40, 40),
                        0, getHeight(), new Color(20, 20, 20)
                );
                g2.setPaint(gradient);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        }
    }
}
