package com.hulefevr.swingy.view.gui.screens;

import com.hulefevr.swingy.model.game.GameState;
import com.hulefevr.swingy.model.hero.Hero;
import com.hulefevr.swingy.model.map.GameMap;
import com.hulefevr.swingy.model.map.Position;
import com.hulefevr.swingy.model.game.Encounter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

/**
 * Panel de jeu principal avec le design inspiré de "The Book of the Fallen"
 */
public class GamePanel extends JPanel {
    private GameState gameState;
    
    // Composants UI
    private JLabel heroInfoLabel;
    private MapCanvas mapCanvas;
    private JTextArea messageArea;
    private JButton btnNorth, btnEast, btnSouth, btnWest;
    private JButton btnStats, btnMenu, btnSwitchView;
    
    // Pour gérer l'attente d'input
    private final Object inputLock = new Object();
    private String pendingMove = null;
    private boolean moveWaitingSetup = false;
    
    // Couleurs du thème
    private static final Color PARCHMENT = new Color(220, 205, 175);
    private static final Color DARK_BROWN = new Color(40, 30, 20);
    private static final Color BORDER_GOLD = new Color(160, 140, 90);
    private static final Color MAP_BG = new Color(30, 30, 30);
    
    public GamePanel() {
        setLayout(new BorderLayout());
        setBackground(DARK_BROWN);
        
        initComponents();
        layoutComponents();
        setupKeyBindings();
    }
    
    private void initComponents() {
        // Hero info label
        heroInfoLabel = new JLabel("HERO SHEET", SwingConstants.CENTER);
        heroInfoLabel.setFont(new Font("Serif", Font.BOLD, 18));
        heroInfoLabel.setForeground(DARK_BROWN);
        heroInfoLabel.setOpaque(true);
        heroInfoLabel.setBackground(PARCHMENT);
        
        // Map canvas
        mapCanvas = new MapCanvas();
        
        // Message area
        messageArea = new JTextArea(10, 20);
        messageArea.setEditable(false);
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        messageArea.setFont(new Font("Serif", Font.PLAIN, 14));
        messageArea.setBackground(PARCHMENT);
        messageArea.setForeground(DARK_BROWN);
        messageArea.setBorder(BorderFactory.createLineBorder(BORDER_GOLD, 2));
        
        // Direction buttons (WASD)
        btnNorth = createDirectionButton("W");
        btnEast = createDirectionButton("D");
        btnSouth = createDirectionButton("S");
        btnWest = createDirectionButton("A");
        
        // Action buttons
        btnStats = createActionButton("STATS");
        btnMenu = createActionButton("MENU");
        btnSwitchView = createActionButton("SWITCH VIEW");
    }
    
    private JButton createDirectionButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Serif", Font.BOLD, 14));
        btn.setBackground(DARK_BROWN);
        btn.setForeground(PARCHMENT);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_GOLD, 2),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        return btn;
    }
    
    private JButton createActionButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Serif", Font.BOLD, 12));
        btn.setBackground(DARK_BROWN);
        btn.setForeground(PARCHMENT);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_GOLD, 2),
            BorderFactory.createEmptyBorder(8, 20, 8, 20)
        ));
        return btn;
    }
    
    private void layoutComponents() {
        // Main panel with borders
        JPanel mainContent = new JPanel(new BorderLayout(10, 10));
        mainContent.setBackground(PARCHMENT);
        mainContent.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(DARK_BROWN, 15),
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_GOLD, 3),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
            )
        ));
        
        // Top: Hero info
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(heroInfoLabel, BorderLayout.CENTER);
        topPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        // Center: Map and messages side by side
        JPanel centerPanel = new JPanel(new BorderLayout(10, 0));
        centerPanel.setOpaque(false);
        
        // Map section
        JPanel mapSection = new JPanel(new BorderLayout(5, 5));
        mapSection.setOpaque(false);
        mapSection.add(mapCanvas, BorderLayout.CENTER);
        
        // Direction buttons panel (WASD layout)
        JPanel directionPanel = new JPanel(new GridBagLayout());
        directionPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 2, 2, 2);
        
        // W en haut (North)
        gbc.gridx = 1; gbc.gridy = 0;
        directionPanel.add(btnNorth, gbc);
        
        // A à gauche (West)
        gbc.gridx = 0; gbc.gridy = 1;
        directionPanel.add(btnWest, gbc);
        
        // D à droite (East)
        gbc.gridx = 2; gbc.gridy = 1;
        directionPanel.add(btnEast, gbc);
        
        // S en bas (South)
        gbc.gridx = 1; gbc.gridy = 2;
        directionPanel.add(btnSouth, gbc);
        
        mapSection.add(directionPanel, BorderLayout.SOUTH);
        
        centerPanel.add(mapSection, BorderLayout.CENTER);
        
        // Message panel on the right
        JScrollPane messageScroll = new JScrollPane(messageArea);
        messageScroll.setPreferredSize(new Dimension(250, 0));
        messageScroll.setOpaque(false);
        messageScroll.getViewport().setOpaque(false);
        messageScroll.setBorder(BorderFactory.createEmptyBorder());
        centerPanel.add(messageScroll, BorderLayout.EAST);
        
        // Bottom: Two rows
        JPanel bottomContainer = new JPanel(new BorderLayout());
        bottomContainer.setOpaque(false);
        
        // Top row: Keyboard indicators [W] [A] [S] [D]
        JPanel keyboardRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        keyboardRow.setOpaque(false);
        keyboardRow.add(createKeyLabel("[W]"));
        keyboardRow.add(createKeyLabel("[A]"));
        keyboardRow.add(createKeyLabel("[S]"));
        keyboardRow.add(createKeyLabel("[D]"));
        
        // Bottom row: Action buttons
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        bottomPanel.setOpaque(false);
        bottomPanel.add(btnStats);
        bottomPanel.add(btnMenu);
        bottomPanel.add(btnSwitchView);
        
        bottomContainer.add(keyboardRow, BorderLayout.NORTH);
        bottomContainer.add(bottomPanel, BorderLayout.SOUTH);
        
        mainContent.add(topPanel, BorderLayout.NORTH);
        mainContent.add(centerPanel, BorderLayout.CENTER);
        mainContent.add(bottomContainer, BorderLayout.SOUTH);
        
        add(mainContent, BorderLayout.CENTER);
    }
    
    private JLabel createKeyLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Serif", Font.BOLD, 14));
        label.setForeground(PARCHMENT);
        label.setOpaque(true);
        label.setBackground(DARK_BROWN);
        label.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_GOLD, 2),
            BorderFactory.createEmptyBorder(5, 12, 5, 12)
        ));
        return label;
    }
    
    /**
     * Configure les raccourcis clavier WASD pour les mouvements
     */
    private void setupKeyBindings() {
        // Récupérer les InputMap et ActionMap du panel
        InputMap inputMap = getInputMap(WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getActionMap();
        
        // W = North (haut)
        inputMap.put(KeyStroke.getKeyStroke('w'), "move-north");
        inputMap.put(KeyStroke.getKeyStroke('W'), "move-north");
        actionMap.put("move-north", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                btnNorth.doClick();
            }
        });
        
        // A = West (gauche)
        inputMap.put(KeyStroke.getKeyStroke('a'), "move-west");
        inputMap.put(KeyStroke.getKeyStroke('A'), "move-west");
        actionMap.put("move-west", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                btnWest.doClick();
            }
        });
        
        // S = South (bas)
        inputMap.put(KeyStroke.getKeyStroke('s'), "move-south");
        inputMap.put(KeyStroke.getKeyStroke('S'), "move-south");
        actionMap.put("move-south", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                btnSouth.doClick();
            }
        });
        
        // D = East (droite)
        inputMap.put(KeyStroke.getKeyStroke('d'), "move-east");
        inputMap.put(KeyStroke.getKeyStroke('D'), "move-east");
        actionMap.put("move-east", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                btnEast.doClick();
            }
        });
        
        // Bonus: Q pour quitter (menu)
        inputMap.put(KeyStroke.getKeyStroke('q'), "quit-game");
        inputMap.put(KeyStroke.getKeyStroke('Q'), "quit-game");
        actionMap.put("quit-game", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                btnMenu.doClick();
            }
        });
    }
    
    /**
     * Update the panel with current game state
     */
    public void updateGameState(GameState state) {
        this.gameState = state;
        updateHeroInfo();
        mapCanvas.repaint();
    }
    
    private void updateHeroInfo() {
        if (gameState != null && gameState.getHero() != null) {
            Hero hero = gameState.getHero();
            String info = String.format(
                "<html><div style='text-align: center; padding: 5px;'>" +
                "<span style='font-size: 16px; font-weight: bold;'>HERO SHEET</span><br>" +
                "<span style='font-size: 14px;'>%s  LVL %d  XP %d/%d  ATK %d  DEF %d  HP %d/%d</span>" +
                "</div></html>",
                hero.getName(),
                hero.getLevel(),
                hero.getXp(),
                hero.getXpForNextLevel(),
                hero.getAttack(),
                hero.getDefense(),
                hero.getHitPoints(),
                hero.getMaxHitPoints()
            );
            heroInfoLabel.setText(info);
        }
    }
    
    public void addMessage(String message) {
        messageArea.append("- " + message + "\n");
        messageArea.setCaretPosition(messageArea.getDocument().getLength());
    }
    
    public void clearMessages() {
        messageArea.setText("");
    }
    
    // Action listeners
    public void setNorthAction(ActionListener listener) {
        btnNorth.addActionListener(listener);
    }
    
    public void setEastAction(ActionListener listener) {
        btnEast.addActionListener(listener);
    }
    
    public void setSouthAction(ActionListener listener) {
        btnSouth.addActionListener(listener);
    }
    
    public void setWestAction(ActionListener listener) {
        btnWest.addActionListener(listener);
    }
    
    public void setStatsAction(ActionListener listener) {
        btnStats.addActionListener(listener);
    }
    
    public void setMenuAction(ActionListener listener) {
        btnMenu.addActionListener(listener);
    }
    
    public void setSwitchViewAction(ActionListener listener) {
        btnSwitchView.addActionListener(listener);
    }
    
    /**
     * Canvas pour afficher la carte du jeu
     */
    private class MapCanvas extends JPanel {
        private static final int CELL_SIZE = 12;
        
        public MapCanvas() {
            setBackground(MAP_BG);
            setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_GOLD, 3),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
            ));
            setPreferredSize(new Dimension(500, 400));
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            
            if (gameState == null || gameState.getHero() == null || gameState.getMap() == null) {
                return;
            }
            
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            GameMap map = gameState.getMap();
            Hero hero = gameState.getHero();
            Position heroPos = hero.getPosition();
            
            int mapSize = map.getSize();
            int totalWidth = mapSize * CELL_SIZE;
            int totalHeight = mapSize * CELL_SIZE;
            
            int offsetX = (getWidth() - totalWidth) / 2;
            int offsetY = (getHeight() - totalHeight) / 2;
            
            // Draw grid
            g2.setColor(new Color(60, 60, 60));
            for (int y = 0; y < mapSize; y++) {
                for (int x = 0; x < mapSize; x++) {
                    int px = offsetX + x * CELL_SIZE;
                    int py = offsetY + y * CELL_SIZE;
                    g2.fillRect(px, py, CELL_SIZE - 1, CELL_SIZE - 1);
                }
            }
            
            // Draw view area around hero (lighter)
            int viewRadius = 3;
            g2.setColor(new Color(80, 80, 80));
            for (int dy = -viewRadius; dy <= viewRadius; dy++) {
                for (int dx = -viewRadius; dx <= viewRadius; dx++) {
                    int x = heroPos.getX() + dx;
                    int y = heroPos.getY() + dy;
                    if (x >= 0 && x < mapSize && y >= 0 && y < mapSize) {
                        int px = offsetX + x * CELL_SIZE;
                        int py = offsetY + y * CELL_SIZE;
                        g2.fillRect(px, py, CELL_SIZE - 1, CELL_SIZE - 1);
                    }
                }
            }
            
            // Draw encounters
            g2.setFont(new Font("Monospaced", Font.BOLD, CELL_SIZE));
            for (int y = 0; y < mapSize; y++) {
                for (int x = 0; x < mapSize; x++) {
                    Position pos = new Position(x, y);
                    Encounter encounter = map.getEncounterAt(pos);
                    
                    // N'afficher que les ennemis non résolus
                    if (encounter != null && encounter.getEnemy() != null && !encounter.isResolved()) {
                        int px = offsetX + x * CELL_SIZE;
                        int py = offsetY + y * CELL_SIZE + CELL_SIZE - 2;
                        g2.setColor(Color.RED);
                        g2.drawString("V", px + 1, py);
                    }
                }
            }
            
            // Draw hero
            int heroPx = offsetX + heroPos.getX() * CELL_SIZE;
            int heroPy = offsetY + heroPos.getY() * CELL_SIZE + CELL_SIZE - 2;
            g2.setColor(Color.WHITE);
            g2.drawString("@", heroPx + 1, heroPy);
            
            // Draw exit if visible
            Position exit = map.getExitPosition();
            if (exit != null) {
                int exitPx = offsetX + exit.getX() * CELL_SIZE;
                int exitPy = offsetY + exit.getY() * CELL_SIZE + CELL_SIZE - 2;
                g2.setColor(Color.GREEN);
                g2.drawString("X", exitPx + 1, exitPy);
            }
        }
    }
    
    /**
     * Attend qu'un mouvement soit sélectionné via les boutons.
     * Cette méthode bloque jusqu'à ce qu'un bouton soit cliqué.
     * IMPORTANT: Ne doit PAS être appelée depuis l'EDT !
     */
    public String waitForMove() {
        synchronized (inputLock) {
            pendingMove = null;
            try {
                inputLock.wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return "Q"; // Quit si interrompu
            }
            return pendingMove != null ? pendingMove : "Q";
        }
    }
    
    /**
     * Signale qu'un mouvement a été sélectionné.
     * Appelé depuis l'EDT par les boutons.
     */
    private void notifyMove(String direction) {
        synchronized (inputLock) {
            pendingMove = direction;
            inputLock.notifyAll();
        }
    }
    
    /**
     * Réinitialise les actions des boutons pour utiliser le système d'attente
     */
    public void setupMoveWaiting() {
        // Ne configurer qu'une seule fois
        if (moveWaitingSetup) {
            return;
        }
        moveWaitingSetup = true;
        
        // Supprimer les anciens listeners
        for (ActionListener al : btnNorth.getActionListeners()) {
            btnNorth.removeActionListener(al);
        }
        for (ActionListener al : btnEast.getActionListeners()) {
            btnEast.removeActionListener(al);
        }
        for (ActionListener al : btnSouth.getActionListeners()) {
            btnSouth.removeActionListener(al);
        }
        for (ActionListener al : btnWest.getActionListeners()) {
            btnWest.removeActionListener(al);
        }
        
        // Ajouter les nouveaux listeners qui notifient
        btnNorth.addActionListener(e -> notifyMove("W"));
        btnEast.addActionListener(e -> notifyMove("D"));
        btnSouth.addActionListener(e -> notifyMove("S"));
        btnWest.addActionListener(e -> notifyMove("A"));
        
        // Le bouton menu peut servir à quitter
        for (ActionListener al : btnMenu.getActionListeners()) {
            btnMenu.removeActionListener(al);
        }
        btnMenu.addActionListener(e -> notifyMove("Q"));
    }
}