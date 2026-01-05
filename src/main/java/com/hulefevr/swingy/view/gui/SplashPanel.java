package com.hulefevr.swingy.view.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Panneau de splash minimal : titre, citation et bouton "OPEN THE BOOK".
 */
public class SplashPanel extends JPanel {
    private final GuiWindow parent;

    public SplashPanel(GuiWindow parent) {
        this.parent = parent;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        // Title area
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        JLabel title = new JLabel("THE BOOK OF THE FALLEN", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 36));
        title.setForeground(new Color(220, 180, 80));
        titlePanel.add(title, BorderLayout.CENTER);

        JLabel quote = new JLabel("\u201CLet the ink bleed.\u201D", SwingConstants.CENTER);
        quote.setFont(new Font("Serif", Font.ITALIC, 18));
        quote.setForeground(new Color(200, 200, 200));
        titlePanel.add(quote, BorderLayout.SOUTH);

        add(titlePanel, BorderLayout.CENTER);

        // Footer / button
        JPanel bottom = new JPanel();
        bottom.setOpaque(false);
        JButton openBtn = new JButton("OPEN THE BOOK");
        openBtn.setFont(new Font("SansSerif", Font.BOLD, 16));
        openBtn.setBackground(new Color(60, 60, 60));
        openBtn.setForeground(Color.WHITE);
        openBtn.setFocusPainted(false);
        openBtn.setPreferredSize(new Dimension(220, 44));

        openBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Switch to main menu
                parent.showMainMenu();
            }
        });

        bottom.add(openBtn);
        add(bottom, BorderLayout.SOUTH);

        // Background color
        setBackground(new Color(20, 18, 16));
        setBorder(BorderFactory.createEmptyBorder(80, 60, 80, 60));
    }
}
