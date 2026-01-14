package com.hulefevr.swingy.view.gui.screens;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * Panneau principal du menu GUI - "The Book of the Fallen"
 */
public class MainMenuPanel extends JPanel {
    private String selected;
    private java.util.concurrent.CountDownLatch latch = new java.util.concurrent.CountDownLatch(1);
    private BackgroundPanel backgroundPanel;
    
    private static final Color PARCHMENT = new Color(245, 238, 228);
    private static final Color DARK_STONE = new Color(46, 44, 40);
    private static final Color BORDER_DARK = new Color(80, 76, 70);

    public MainMenuPanel() {
        this(null);
    }

    public MainMenuPanel(JDialog owner) {
        setLayout(new BorderLayout());
        setBackground(DARK_STONE);
        buildUI(owner);
    }

    private void buildUI(JDialog owner) {
        // Background panel avec l'image
        backgroundPanel = new BackgroundPanel();
        backgroundPanel.setLayout(new GridBagLayout());
        
        // Panel central avec le menu
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(true);
        centerPanel.setBackground(PARCHMENT);
        centerPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_DARK, 4),
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(DARK_STONE, 2),
                        BorderFactory.createEmptyBorder(30, 50, 30, 50)
                )
        ));
        
        // // Decorative left panel
        // JPanel leftDeco = new JPanel() {
        //     @Override
        //     protected void paintComponent(Graphics g) {
        //         super.paintComponent(g);
        //         Graphics2D g2 = (Graphics2D) g;
        //         GradientPaint gradient = new GradientPaint(
        //                 0, 0, new Color(60, 55, 50),
        //                 getWidth(), 0, new Color(80, 75, 70)
        //         );
        //         g2.setPaint(gradient);
        //         g2.fillRect(0, 0, getWidth(), getHeight());
        //     }
        // };
        // leftDeco.setPreferredSize(new Dimension(120, 0));
        // leftDeco.setOpaque(false);
        
        // Title
        JLabel titleLine1 = new JLabel("THE BOOK", SwingConstants.CENTER);
        titleLine1.setFont(new Font("Serif", Font.BOLD, 28));
        titleLine1.setForeground(DARK_STONE);
        titleLine1.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel titleLine2 = new JLabel("OF THE FALLEN", SwingConstants.CENTER);
        titleLine2.setFont(new Font("Serif", Font.BOLD, 32));
        titleLine2.setForeground(DARK_STONE);
        titleLine2.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLine2.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, BORDER_DARK));
        
        // Menu items
        JPanel menuItems = new JPanel();
        menuItems.setLayout(new BoxLayout(menuItems, BoxLayout.Y_AXIS));
        menuItems.setOpaque(false);
        menuItems.setBorder(BorderFactory.createEmptyBorder(20, 30, 15, 30));
        
        JButton b1 = createMenuItem("1 · Summon a Fallen Soul", "[1: Enter]");
        JButton b2 = createMenuItem("2 · Resume the Trial", "[2: Enter]");
        JButton b3 = createMenuItem("3 · Scriptures of Condemnation", "[3: Enter]");
        JButton b4 = createMenuItem("4 · Abandon the Descent", "[4: Enter]");
        
        menuItems.add(b1);
        menuItems.add(Box.createVerticalStrut(8));
        menuItems.add(b2);
        menuItems.add(Box.createVerticalStrut(8));
        menuItems.add(b3);
        menuItems.add(Box.createVerticalStrut(8));
        menuItems.add(b4);
        
        // Footer text
        JLabel footer = new JLabel("Turn the page with your choice.", SwingConstants.CENTER);
        footer.setFont(new Font("Serif", Font.ITALIC, 14));
        footer.setForeground(DARK_STONE);
        footer.setAlignmentX(Component.CENTER_ALIGNMENT);
        footer.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        
        // Assemble center panel
        centerPanel.add(Box.createVerticalGlue());
        centerPanel.add(titleLine1);
        centerPanel.add(Box.createVerticalStrut(5));
        centerPanel.add(titleLine2);
        centerPanel.add(Box.createVerticalStrut(15));
        centerPanel.add(menuItems);
        centerPanel.add(footer);
        centerPanel.add(Box.createVerticalGlue());
        
        centerPanel.setMaximumSize(new Dimension(600, 450));
        
        // Combine left deco + center in a horizontal layout
        JPanel mainContent = new JPanel(new BorderLayout());
        mainContent.setOpaque(false);
        // mainContent.add(leftDeco, BorderLayout.WEST);
        mainContent.add(centerPanel, BorderLayout.CENTER);
        
        backgroundPanel.add(mainContent);
        add(backgroundPanel, BorderLayout.CENTER);
        
        // Bottom footer
        JPanel bottomFooter = new JPanel(new BorderLayout());
        bottomFooter.setOpaque(false);
        bottomFooter.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        JLabel versionLeft = new JLabel("Edition of Ash · v1.0 · GUI Mode");
        versionLeft.setFont(new Font("Serif", Font.PLAIN, 12));
        versionLeft.setForeground(new Color(200, 200, 200));
        
        JLabel versionRight = new JLabel("Loading souls...");
        versionRight.setFont(new Font("Serif", Font.ITALIC, 12));
        versionRight.setForeground(new Color(200, 200, 200));
        
        bottomFooter.add(versionLeft, BorderLayout.WEST);
        bottomFooter.add(versionRight, BorderLayout.EAST);
        
        add(bottomFooter, BorderLayout.SOUTH);
        
        // Actions
        b1.addActionListener(e -> choose("1", owner));
        b2.addActionListener(e -> choose("2", owner));
        b3.addActionListener(e -> choose("3", owner));
        b4.addActionListener(e -> choose("4", owner));
        
        // Key bindings
        setupKeyBindings(owner);
        setFocusable(true);
    }
    
    private JButton createMenuItem(String text, String shortcut) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        
        JLabel mainText = new JLabel(text);
        mainText.setFont(new Font("Serif", Font.PLAIN, 16));
        mainText.setForeground(DARK_STONE);
        
        JLabel shortcutText = new JLabel(shortcut);
        shortcutText.setFont(new Font("Serif", Font.PLAIN, 12));
        shortcutText.setForeground(new Color(100, 95, 90));
        
        panel.add(mainText, BorderLayout.WEST);
        panel.add(shortcutText, BorderLayout.EAST);
        
        JButton btn = new JButton();
        btn.setLayout(new BorderLayout());
        btn.add(panel, BorderLayout.CENTER);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(true);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_DARK, 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        btn.setFocusPainted(false);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(500, 50));
        
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                System.out.println("Button clicked: " + text);
            }
        });
        
        return btn;
    }
    
    private void setupKeyBindings(JDialog owner) {
        InputMap im = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = getActionMap();
        
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_1, 0), "choose1");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD1, 0), "choose1");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_2, 0), "choose2");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD2, 0), "choose2");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_3, 0), "choose3");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD3, 0), "choose3");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_4, 0), "choose4");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD4, 0), "choose4");
        
        am.put("choose1", new AbstractAction() { public void actionPerformed(ActionEvent e) { choose("1", owner); } });
        am.put("choose2", new AbstractAction() { public void actionPerformed(ActionEvent e) { choose("2", owner); } });
        am.put("choose3", new AbstractAction() { public void actionPerformed(ActionEvent e) { choose("3", owner); } });
        am.put("choose4", new AbstractAction() { public void actionPerformed(ActionEvent e) { choose("4", owner); } });
    }

    private void choose(String value, JDialog owner) {
        this.selected = value;
        System.out.println("MainMenuPanel.choose() -> " + value);
        latch.countDown();
        if (owner != null) {
            owner.dispose();
        } else {
            Window w = SwingUtilities.getWindowAncestor(this);
            if (w instanceof JDialog) {
                ((JDialog) w).dispose();
            }
        }
    }

    public String getSelected() {
        return selected;
    }

    public String waitForSelection() {
        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return selected;
    }

    public void reset() {
        selected = null;
        latch = new java.util.concurrent.CountDownLatch(1);
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
                System.err.println("Could not load main menu background image: " + e.getMessage());
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
