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
    
    private JLabel heroStatsLabel;
    private MapCanvas mapCanvas;
    private JTextArea logArea;
    private JButton btnN, btnE, btnS, btnW;
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
        setBackground(new Color(60, 58, 54));
        setFocusable(true);
        buildUI();
        setupKeyBindings();
    }

    private void buildUI() {
        // Left decorative panel
        JPanel leftDeco = new JPanel();
        leftDeco.setPreferredSize(new Dimension(220, 0));
        leftDeco.setBackground(DARK_STONE);
        add(leftDeco, BorderLayout.WEST);

        // Right content
        JPanel right = new JPanel(new BorderLayout(0, 0));
        right.setOpaque(false);

        // Title: HERO SHEET
        JLabel title = new JLabel("Hero Sheet", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 24f));
        title.setForeground(new Color(48, 44, 40));
        title.setOpaque(true);
        title.setBackground(PARCHMENT);
        title.setBorder(BorderFactory.createEmptyBorder(12, 12, 4, 12));

        // Hero stats bar below title
        heroStatsLabel = new JLabel("", SwingConstants.CENTER);
        heroStatsLabel.setFont(heroStatsLabel.getFont().deriveFont(Font.PLAIN, 14f));
        heroStatsLabel.setForeground(new Color(48, 44, 40));
        heroStatsLabel.setOpaque(true);
        heroStatsLabel.setBackground(PARCHMENT);
        heroStatsLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, BORDER_DARK),
                BorderFactory.createEmptyBorder(4, 12, 12, 12)));

        JPanel topGroup = new JPanel(new BorderLayout());
        topGroup.setOpaque(false);
        topGroup.add(title, BorderLayout.NORTH);
        topGroup.add(heroStatsLabel, BorderLayout.SOUTH);
        right.add(topGroup, BorderLayout.NORTH);

        // Center: map + log
        JPanel centerPanel = new JPanel(new BorderLayout(8, 0));
        centerPanel.setOpaque(false);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));

        // Map
        mapCanvas = new MapCanvas();
        JPanel mapHolder = new JPanel(new BorderLayout());
        mapHolder.setOpaque(false);
        mapHolder.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_DARK, 2),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)));
        mapHolder.add(mapCanvas, BorderLayout.CENTER);
        centerPanel.add(mapHolder, BorderLayout.CENTER);

        // Log
        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setLineWrap(true);
        logArea.setWrapStyleWord(true);
        logArea.setFont(logArea.getFont().deriveFont(13f));
        logArea.setBackground(PARCHMENT);
        logArea.setForeground(new Color(48, 44, 40));
        JScrollPane logScroll = new JScrollPane(logArea);
        logScroll.setPreferredSize(new Dimension(280, 0));
        logScroll.setBorder(BorderFactory.createLineBorder(BORDER_DARK, 2));
        centerPanel.add(logScroll, BorderLayout.EAST);

        right.add(centerPanel, BorderLayout.CENTER);

        // Bottom: directional + actions
        JPanel bottom = new JPanel(new BorderLayout(8, 8));
        bottom.setOpaque(false);
        bottom.setBorder(BorderFactory.createEmptyBorder(8, 12, 12, 12));

        // Directional panel (NSEW layout)
        btnN = createDirButton("N");
        btnE = createDirButton("E");
        btnS = createDirButton("S");
        btnW = createDirButton("W");

        JPanel dirPanel = new JPanel(new GridBagLayout());
        dirPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.gridx = 1; gbc.gridy = 0;
        dirPanel.add(btnN, gbc);
        gbc.gridx = 0; gbc.gridy = 1;
        dirPanel.add(btnW, gbc);
        gbc.gridx = 2; gbc.gridy = 1;
        dirPanel.add(btnE, gbc);
        gbc.gridx = 1; gbc.gridy = 2;
        dirPanel.add(btnS, gbc);

        // Bottom row: action buttons
        btnStats = createActionButton("STATS");
        btnMenu = createActionButton("MENU");
        btnSwitchView = createActionButton("SWITCH VIEW");

        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        actionsPanel.setOpaque(false);
        actionsPanel.add(btnStats);
        actionsPanel.add(btnMenu);
        actionsPanel.add(btnSwitchView);

        JPanel bottomCombined = new JPanel(new BorderLayout());
        bottomCombined.setOpaque(false);
        bottomCombined.add(dirPanel, BorderLayout.WEST);
        bottomCombined.add(actionsPanel, BorderLayout.EAST);

        bottom.add(bottomCombined, BorderLayout.CENTER);
        right.add(bottom, BorderLayout.SOUTH);

        add(right, BorderLayout.CENTER);

        // Wire listeners
        btnN.addActionListener(e -> recordMove("W"));
        btnE.addActionListener(e -> recordMove("D"));
        btnS.addActionListener(e -> recordMove("S"));
        btnW.addActionListener(e -> recordMove("A"));        
        // Menu button quits the game (sends "Q" like console)
        btnMenu.addActionListener(e -> recordMove("Q"));    }

    private JButton createDirButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(btn.getFont().deriveFont(Font.BOLD, 16f));
        btn.setPreferredSize(new Dimension(50, 40));
        btn.setBackground(DARK_STONE);
        btn.setForeground(PARCHMENT);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(BORDER_DARK, 2));
        return btn;
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
        mapCanvas.repaint();
    }

    private void updateHeroStats() {
        if (gameState != null && gameState.getHero() != null) {
            Hero h = gameState.getHero();
            heroStatsLabel.setText(String.format(
                    "%s  LVL %d  XP %d/%d  ATK %d  DEF %d  HP %d/%d",
                    h.getName(), h.getLevel(), h.getXp(), h.getXpForNextLevel(),
                    h.getAttack(), h.getDefense(), h.getHitPoints(), h.getMaxHitPoints()));
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
