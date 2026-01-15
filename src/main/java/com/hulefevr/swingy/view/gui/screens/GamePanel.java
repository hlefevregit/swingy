package com.hulefevr.swingy.view.gui.screens;

import com.hulefevr.swingy.model.game.GameState;
import com.hulefevr.swingy.model.hero.Hero;
import com.hulefevr.swingy.model.map.GameMap;
import com.hulefevr.swingy.model.map.Position;
import com.hulefevr.swingy.model.game.Encounter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.concurrent.CountDownLatch;

/**
 * Panel de jeu principal - design "The Book of the Fallen"
 * Layout: left decorative panel | hero stats bar | map + log | NSEW controls | action buttons
 */
public class GamePanel extends JPanel {
    private GameState gameState;
    private Image backgroundImage;
    
    private JLabel heroStatsLabel;
    private MapCanvas mapCanvas;
    private JTextArea logArea;
    private JLabel inventoryWeaponLabel;
    private JLabel inventoryArmorLabel;
    private JLabel inventoryHelmLabel;
    private JButton btnStats, btnMenu, btnSwitchView;
    
    private CountDownLatch moveLatch;
    private String moveResult;
    
    private static final Color PARCHMENT = new Color(245, 238, 228);
    private static final Color DARK_STONE = new Color(46, 44, 40);
    private static final Color BORDER_DARK = new Color(80, 76, 70);
    private static final Color MAP_BG = new Color(32, 32, 32);
    private static final Color MAP_VISIBLE = new Color(64, 64, 64);
    
    public GamePanel() {
        setLayout(new BorderLayout());
        setOpaque(false); // Transparent pour voir le background
        setFocusable(true);
        loadBackgroundImage();
        buildUI();
        setupKeyBindings();
    }
    
    private void loadBackgroundImage() {
        try {
            java.net.URL imageURL = getClass().getResource("/images/SorrowfulAngelsOnARock.png");
            if (imageURL != null) {
                backgroundImage = new ImageIcon(imageURL).getImage();
            }
        } catch (Exception e) {
            System.err.println("Could not load SorrowfulAngelsOnARock background: " + e.getMessage());
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        
        if (backgroundImage != null) {
            int panelWidth = getWidth();
            int panelHeight = getHeight();
            int imgWidth = backgroundImage.getWidth(this);
            int imgHeight = backgroundImage.getHeight(this);
            
            if (imgWidth > 0 && imgHeight > 0) {
                // Calculer le ratio pour garder les proportions
                double ratio = Math.max((double) panelWidth / imgWidth, (double) panelHeight / imgHeight);
                int destWidth = (int) (imgWidth * ratio);
                int destHeight = (int) (imgHeight * ratio);
                
                // Centrer l'image
                int x = (panelWidth - destWidth) / 2;
                int y = (panelHeight - destHeight) / 2;
                
                // Dessiner l'image entière
                g2.drawImage(backgroundImage, x, y, destWidth, destHeight, this);
            }
        } else {
            // Fallback: fond sombre
            g2.setColor(new Color(60, 58, 54));
            g2.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    private void buildUI() {
        // Main container
        JPanel mainContainer = new JPanel(new BorderLayout(0, 0));
        mainContainer.setOpaque(false);
        mainContainer.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // TOP SECTION: Inventory | Heaven vs Abyss + Mini-Map | Lore/Log
        JPanel topSection = new JPanel(new BorderLayout(15, 0));
        topSection.setOpaque(false);

        // LEFT: Inventory panel (papier parchemin)
        JPanel inventoryPanel = createInventoryPanel();
        inventoryPanel.setPreferredSize(new Dimension(280, 0));
        topSection.add(inventoryPanel, BorderLayout.WEST);

        // CENTER: Title + Mini-Map
        JPanel centerSection = new JPanel(new BorderLayout(0, 10));
        centerSection.setOpaque(false);

        // Title: HEAVEN vs ABYSS
        JLabel titleLabel = new JLabel("HEAVEN vs ABYSS", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 32));
        titleLabel.setForeground(DARK_STONE);
        titleLabel.setOpaque(false);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        centerSection.add(titleLabel, BorderLayout.NORTH);

        // Mini-Map (trou noir dans la page)
        mapCanvas = new MapCanvas();
        JPanel mapHolder = new JPanel(new BorderLayout());
        mapHolder.setOpaque(true);
        mapHolder.setBackground(Color.BLACK); // Fond noir pour effet nocturne
        mapHolder.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(DARK_STONE, 3),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        mapHolder.add(mapCanvas, BorderLayout.CENTER);
        centerSection.add(mapHolder, BorderLayout.CENTER);

        topSection.add(centerSection, BorderLayout.CENTER);

        // RIGHT: Lore/Log panel (papier parchemin)
        JPanel lorePanel = createLorePanel();
        lorePanel.setPreferredSize(new Dimension(280, 0));
        topSection.add(lorePanel, BorderLayout.EAST);

        mainContainer.add(topSection, BorderLayout.CENTER);

        // FOOTER: Buttons
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        footer.setOpaque(false);
        footer.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

        btnMenu = createFooterButton("MENU");
        btnSwitchView = createFooterButton("SWITCH");
        btnStats = createFooterButton("HELP");

        footer.add(btnMenu);
        footer.add(btnSwitchView);
        footer.add(btnStats);

        mainContainer.add(footer, BorderLayout.SOUTH);

        add(mainContainer, BorderLayout.CENTER);

        // Wire listeners
        btnMenu.addActionListener(e -> recordMove("Q"));
    }

    private JButton createActionButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(btn.getFont().deriveFont(Font.BOLD, 13f));
        btn.setBackground(DARK_STONE);
        btn.setForeground(PARCHMENT);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_DARK, 2),
                BorderFactory.createEmptyBorder(8, 16, 8, 16)));
        return btn;
    }
    
    private JButton createFooterButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Serif", Font.BOLD, 16));
        btn.setBackground(PARCHMENT);
        btn.setForeground(DARK_STONE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(DARK_STONE, 3),
                BorderFactory.createEmptyBorder(10, 30, 10, 30)));
        return btn;
    }
    
    private JPanel createLorePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(true);
        panel.setBackground(PARCHMENT);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(DARK_STONE, 3),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)));
        
        // Title
        JLabel title = new JLabel("LORE & EVENTS", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 18));
        title.setForeground(DARK_STONE);
        
        // Hero stats (déplacé ici)
        heroStatsLabel = new JLabel("", SwingConstants.CENTER);
        heroStatsLabel.setFont(new Font("Serif", Font.PLAIN, 10));
        heroStatsLabel.setForeground(new Color(48, 44, 40));
        
        // Log area
        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setLineWrap(true);
        logArea.setWrapStyleWord(true);
        logArea.setFont(new Font("Serif", Font.PLAIN, 12));
        logArea.setBackground(PARCHMENT);
        logArea.setForeground(new Color(48, 44, 40));
        JScrollPane logScroll = new JScrollPane(logArea);
        logScroll.setBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, BORDER_DARK));
        
        JPanel topPanel = new JPanel(new BorderLayout(0, 5));
        topPanel.setOpaque(false);
        topPanel.add(title, BorderLayout.NORTH);
        topPanel.add(heroStatsLabel, BorderLayout.SOUTH);
        
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(logScroll, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createInventoryPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(true);
        panel.setBackground(PARCHMENT);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_DARK, 2),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)));
        
        // Title
        JLabel title = new JLabel("INVENTORY");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 16f));
        title.setForeground(DARK_STONE);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Equipment labels with HTML for word wrapping
        inventoryWeaponLabel = new JLabel("<html>⚔ Weapon:<br/>None</html>");
        inventoryWeaponLabel.setFont(inventoryWeaponLabel.getFont().deriveFont(12f));
        inventoryWeaponLabel.setForeground(new Color(48, 44, 40));
        inventoryWeaponLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        inventoryArmorLabel = new JLabel("<html>🛡 Armor:<br/>None</html>");
        inventoryArmorLabel.setFont(inventoryArmorLabel.getFont().deriveFont(12f));
        inventoryArmorLabel.setForeground(new Color(48, 44, 40));
        inventoryArmorLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        inventoryHelmLabel = new JLabel("<html>⛑ Helm:<br/>None</html>");
        inventoryHelmLabel.setFont(inventoryHelmLabel.getFont().deriveFont(12f));
        inventoryHelmLabel.setForeground(new Color(48, 44, 40));
        inventoryHelmLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        panel.add(title);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(inventoryWeaponLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(inventoryArmorLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(inventoryHelmLabel);
        panel.add(Box.createVerticalGlue());
        
        return panel;
    }

    private void recordMove(String dir) {
        moveResult = dir;
        if (moveLatch != null) moveLatch.countDown();
    }

    private void setupKeyBindings() {
        // WASD key bindings
        InputMap inputMap = getInputMap(WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getActionMap();

        // W = North
        inputMap.put(KeyStroke.getKeyStroke('w'), "moveNorth");
        inputMap.put(KeyStroke.getKeyStroke('W'), "moveNorth");
        actionMap.put("moveNorth", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                recordMove("W");
            }
        });

        // A = West
        inputMap.put(KeyStroke.getKeyStroke('a'), "moveWest");
        inputMap.put(KeyStroke.getKeyStroke('A'), "moveWest");
        actionMap.put("moveWest", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                recordMove("A");
            }
        });

        // S = South
        inputMap.put(KeyStroke.getKeyStroke('s'), "moveSouth");
        inputMap.put(KeyStroke.getKeyStroke('S'), "moveSouth");
        actionMap.put("moveSouth", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                recordMove("S");
            }
        });

        // D = East
        inputMap.put(KeyStroke.getKeyStroke('d'), "moveEast");
        inputMap.put(KeyStroke.getKeyStroke('D'), "moveEast");
        actionMap.put("moveEast", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                recordMove("D");
            }
        });
    }

    public void updateGameState(GameState state) {
        this.gameState = state;
        updateHeroStats();
        updateInventory();
        mapCanvas.repaint();
    }

    private void updateHeroStats() {
        if (gameState != null && gameState.getHero() != null) {
            Hero h = gameState.getHero();
            heroStatsLabel.setText(String.format(
                    "<html><center>%s  LVL %d  XP %d/%d<br>ATK %d  DEF %d  HP %d/%d</center></html>",
                    h.getName(), h.getLevel(), h.getXp(), h.getXpForNextLevel(),
                    h.getAttack(), h.getDefense(), h.getHitPoints(), h.getMaxHitPoints()));
        }
    }
    
    private void updateInventory() {
        if (gameState != null && gameState.getHero() != null) {
            Hero h = gameState.getHero();
            
            // Weapon - format HTML sur 2 lignes
            if (h.getWeapon() != null) {
                inventoryWeaponLabel.setText(String.format("<html>⚔ Weapon:<br/>%s (+%d ATK)</html>", 
                    h.getWeapon().getName(), h.getWeapon().getBonusValue()));
            } else {
                inventoryWeaponLabel.setText("<html>⚔ Weapon:<br/>None</html>");
            }
            
            // Armor - format HTML sur 2 lignes
            if (h.getArmor() != null) {
                inventoryArmorLabel.setText(String.format("<html>🛡 Armor:<br/>%s (+%d DEF)</html>", 
                    h.getArmor().getName(), h.getArmor().getBonusValue()));
            } else {
                inventoryArmorLabel.setText("<html>🛡 Armor:<br/>None</html>");
            }
            
            // Helm - format HTML sur 2 lignes
            if (h.getHelm() != null) {
                inventoryHelmLabel.setText(String.format("<html>⛑ Helm:<br/>%s (+%d HP)</html>", 
                    h.getHelm().getName(), h.getHelm().getBonusValue()));
            } else {
                inventoryHelmLabel.setText("<html>⛑ Helm:<br/>None</html>");
            }
        }
    }

    public void appendLog(String msg) {
        logArea.append("- " + msg + "\n");
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }

    public void clearLog() {
        logArea.setText("");
    }

    public String waitForMove() {
        moveLatch = new CountDownLatch(1);
        try {
            moveLatch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        String res = moveResult;
        moveResult = null;
        moveLatch = null;
        return res;
    }

    // Legacy methods for compatibility
    public void addMessage(String msg) { appendLog(msg); }
    public void setupMoveWaiting() { /* no-op, new API uses waitForMove() */ }
    public void setNorthAction(ActionListener l) { /* deprecated, use waitForMove() */ }
    public void setEastAction(ActionListener l) { /* deprecated, use waitForMove() */ }
    public void setSouthAction(ActionListener l) { /* deprecated, use waitForMove() */ }
    public void setWestAction(ActionListener l) { /* deprecated, use waitForMove() */ }

    public void setStatsAction(ActionListener l) { btnStats.addActionListener(l); }
    public void setMenuAction(ActionListener l) { btnMenu.addActionListener(l); }
    public void setSwitchViewAction(ActionListener l) { btnSwitchView.addActionListener(l); }

    private class MapCanvas extends JPanel {
        private static final int CELL_SIZE = 14;

        public MapCanvas() {
            setBackground(MAP_BG);
            setPreferredSize(new Dimension(450, 380));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (gameState == null || gameState.getHero() == null || gameState.getMap() == null) {
                return;
            }
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            GameMap map = gameState.getMap();
            Hero hero = gameState.getHero();
            Position heroPos = hero.getPosition();
            int mapSize = map.getSize();
            
            // Calculate camera offset to keep hero centered
            // If map is smaller than canvas, center the map
            // If map is larger, center on hero
            int canvasWidth = getWidth();
            int canvasHeight = getHeight();
            int totalMapW = mapSize * CELL_SIZE;
            int totalMapH = mapSize * CELL_SIZE;
            
            int cameraOffsetX, cameraOffsetY;
            
            if (totalMapW <= canvasWidth) {
                // Map fits horizontally - center it
                cameraOffsetX = (canvasWidth - totalMapW) / 2;
            } else {
                // Map is larger - center on hero
                cameraOffsetX = canvasWidth / 2 - (heroPos.getX() * CELL_SIZE + CELL_SIZE / 2);
                // Clamp camera so we don't show area outside the map
                int maxOffsetX = 0;
                int minOffsetX = canvasWidth - totalMapW;
                cameraOffsetX = Math.max(minOffsetX, Math.min(maxOffsetX, cameraOffsetX));
            }
            
            if (totalMapH <= canvasHeight) {
                // Map fits vertically - center it
                cameraOffsetY = (canvasHeight - totalMapH) / 2;
            } else {
                // Map is larger - center on hero
                cameraOffsetY = canvasHeight / 2 - (heroPos.getY() * CELL_SIZE + CELL_SIZE / 2);
                // Clamp camera so we don't show area outside the map
                int maxOffsetY = 0;
                int minOffsetY = canvasHeight - totalMapH;
                cameraOffsetY = Math.max(minOffsetY, Math.min(maxOffsetY, cameraOffsetY));
            }

            // Calculate visible cell range for optimization
            int startX = Math.max(0, -cameraOffsetX / CELL_SIZE - 1);
            int endX = Math.min(mapSize, (-cameraOffsetX + canvasWidth) / CELL_SIZE + 1);
            int startY = Math.max(0, -cameraOffsetY / CELL_SIZE - 1);
            int endY = Math.min(mapSize, (-cameraOffsetY + canvasHeight) / CELL_SIZE + 1);

            // Draw dark grid (only visible cells)
            g2.setColor(MAP_BG);
            for (int y = startY; y < endY; y++) {
                for (int x = startX; x < endX; x++) {
                    int px = cameraOffsetX + x * CELL_SIZE;
                    int py = cameraOffsetY + y * CELL_SIZE;
                    g2.fillRect(px, py, CELL_SIZE - 1, CELL_SIZE - 1);
                }
            }

            // View radius lighter
            int viewRadius = 3;
            g2.setColor(MAP_VISIBLE);
            for (int dy = -viewRadius; dy <= viewRadius; dy++) {
                for (int dx = -viewRadius; dx <= viewRadius; dx++) {
                    int x = heroPos.getX() + dx;
                    int y = heroPos.getY() + dy;
                    if (x >= 0 && x < mapSize && y >= 0 && y < mapSize) {
                        int px = cameraOffsetX + x * CELL_SIZE;
                        int py = cameraOffsetY + y * CELL_SIZE;
                        g2.fillRect(px, py, CELL_SIZE - 1, CELL_SIZE - 1);
                    }
                }
            }

            // Draw encounters (V for villains) - only visible ones
            g2.setFont(new Font("Monospaced", Font.BOLD, CELL_SIZE - 2));
            g2.setColor(new Color(200, 60, 60));
            for (int y = startY; y < endY; y++) {
                for (int x = startX; x < endX; x++) {
                    Encounter enc = map.getEncounterAt(new Position(x, y));
                    if (enc != null && !enc.isResolved()) {
                        int px = cameraOffsetX + x * CELL_SIZE;
                        int py = cameraOffsetY + y * CELL_SIZE;
                        g2.drawString("V", px + 2, py + CELL_SIZE - 2);
                    }
                }
            }

            // Draw exit (#)
            Position exitPos = map.getExitPosition();
            if (exitPos != null) {
                g2.setColor(new Color(100, 200, 100)); // Green for exit
                int ex = cameraOffsetX + exitPos.getX() * CELL_SIZE;
                int ey = cameraOffsetY + exitPos.getY() * CELL_SIZE;
                g2.drawString("#", ex + 2, ey + CELL_SIZE - 2);
            }

            // Draw hero (@)
            g2.setColor(new Color(220, 220, 180));
            int hx = cameraOffsetX + heroPos.getX() * CELL_SIZE;
            int hy = cameraOffsetY + heroPos.getY() * CELL_SIZE;
            g2.drawString("@", hx + 2, hy + CELL_SIZE - 2);
        }
    }
}
