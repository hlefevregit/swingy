package com.hulefevr.swingy.view.gui.screens;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Panneau principal du menu GUI.
 * Fournit quatre options cliquables et keybindings (1..4).
 * Conçu pour être utilisé dans un JDialog modal.
 */
public class MainMenuPanel extends JPanel {
    private String selected;
    private java.util.concurrent.CountDownLatch latch = new java.util.concurrent.CountDownLatch(1);

    public MainMenuPanel() {
        this(null);
    }

    /**
     * Constructor which accepts an optional owner dialog to close when a choice is made.
     */
    public MainMenuPanel(JDialog owner) {
        setLayout(new BorderLayout());
        setBackground(new Color(32, 28, 24));

        // Center card
        JPanel card = new JPanel(new GridBagLayout());
        card.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 20, 8, 20);

        JLabel title = new JLabel("THE BOOK OF THE FALLEN", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 32));
        title.setForeground(new Color(230, 220, 200));
        gbc.gridy = 0;
        card.add(title, gbc);

        JLabel subtitle = new JLabel("Turn the page with your choice.", SwingConstants.CENTER);
        subtitle.setFont(new Font("Serif", Font.ITALIC, 16));
        subtitle.setForeground(new Color(200, 190, 170));
        gbc.gridy = 1;
        card.add(subtitle, gbc);

        JPanel buttons = new JPanel(new GridLayout(4, 1, 8, 8));
        buttons.setOpaque(false);
        buttons.setBorder(BorderFactory.createEmptyBorder(20, 80, 20, 80));

        JButton b1 = styledButton("1 · Summon a Fallen Soul");
        JButton b2 = styledButton("2 · Resume the Trial");
        JButton b3 = styledButton("3 · Scriptures of Condemnation");
        JButton b4 = styledButton("4 · Abandon the Descent");

        buttons.add(b1);
        buttons.add(b2);
        buttons.add(b3);
        buttons.add(b4);

        gbc.gridy = 2;
        card.add(buttons, gbc);

        add(card, BorderLayout.CENTER);

        // Footer
        JLabel footer = new JLabel("Edition of Ash · v1.0 · GUI Mode", SwingConstants.CENTER);
        footer.setFont(new Font("SansSerif", Font.PLAIN, 12));
        footer.setForeground(new Color(170, 160, 150));
        footer.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        add(footer, BorderLayout.SOUTH);

        // Actions: set selection and close owner dialog if provided
        b1.addActionListener((ActionEvent e) -> choose("1", owner));
        b2.addActionListener((ActionEvent e) -> choose("2", owner));
        b3.addActionListener((ActionEvent e) -> choose("3", owner));
        b4.addActionListener((ActionEvent e) -> choose("4", owner));

        // Key bindings (1-4 and numpad) using InputMap/ActionMap so they work without focus
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

        // Ensure focusable for other components
        setFocusable(true);
    }

    private JButton styledButton(String text) {
        JButton b = new JButton(text);
        b.setFont(new Font("Serif", Font.PLAIN, 18));
        b.setBackground(new Color(60, 50, 40));
        b.setForeground(new Color(230, 220, 200));
        b.setFocusPainted(false);
        b.setEnabled(true);
        b.setOpaque(true);
        b.setBorderPainted(false);
        // add a small debug mouse listener to verify clicks
        b.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                System.out.println("Button clicked: " + text);
            }
        });
        return b;
    }

    private void choose(String value, JDialog owner) {
        this.selected = value;
        // debug
        System.out.println("MainMenuPanel.choose() -> " + value);
        // signal waiting thread
        latch.countDown();
        if (owner != null) {
            owner.dispose();
        } else {
            // If no owner, try to hide top-level window
            Window w = SwingUtilities.getWindowAncestor(this);
            if (w instanceof JDialog) {
                ((JDialog) w).dispose();
            }
        }
    }

    /**
     * Returns the selected option ("1".."4") or null if none.
     */
    public String getSelected() {
        return selected;
    }

    /**
     * Blocks until the user selects an option and returns it.
     * Callers should ensure this is not invoked on the EDT.
     */
    public String waitForSelection() {
        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return selected;
    }

    /**
     * Reset the selection state so the panel can be reused.
     */
    public void reset() {
        selected = null;
        latch = new java.util.concurrent.CountDownLatch(1);
    }
}