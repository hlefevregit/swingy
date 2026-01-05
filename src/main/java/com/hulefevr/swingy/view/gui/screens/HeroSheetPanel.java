package com.hulefevr.swingy.view.gui.screens;

import com.hulefevr.swingy.model.hero.Hero;

import javax.swing.*;
import java.awt.*;

/**
 * Panel displaying a full hero sheet inside the main window.
 */
public class HeroSheetPanel extends JPanel {
    private JLabel title;
    private JTextArea body;

    public HeroSheetPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(32, 28, 24));

        title = new JLabel("HERO SHEET", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 24));
        title.setForeground(new Color(230, 220, 200));
        add(title, BorderLayout.NORTH);

        body = new JTextArea();
        body.setEditable(false);
        body.setBackground(new Color(48, 44, 40));
        body.setForeground(new Color(230, 220, 200));
        body.setRows(10);
        body.setFont(new Font("Monospaced", Font.PLAIN, 12));
        add(new JScrollPane(body), BorderLayout.CENTER);
    }

    public void showHero(Hero h) {
        StringBuilder sb = new StringBuilder();
        sb.append("Name: ").append(h.getName()).append("    Class: ").append(h.getHeroClass()).append("\n");
        sb.append("Level: ").append(h.getLevel()).append("   XP: ").append(h.getXp()).append("/ ").append(h.getXpForNextLevel()).append("\n\n");
        sb.append("Attack: ").append(h.getAttack()).append("   Defense: ").append(h.getDefense()).append("   HP: ").append(h.getHitPoints()).append("/").append(h.getMaxHitPoints()).append("\n\n");
        sb.append("Relics Equipped:\n");
        sb.append(" Weapon: ").append(h.getWeapon() != null ? h.getWeapon().getName() + " (" + h.getWeapon().getBonusValue() + ")" : "None").append("\n");
        sb.append(" Armor : ").append(h.getArmor() != null ? h.getArmor().getName() + " (" + h.getArmor().getBonusValue() + ")" : "None").append("\n");
        sb.append(" Helm  : ").append(h.getHelm() != null ? h.getHelm().getName() + " (" + h.getHelm().getBonusValue() + ")" : "None").append("\n");

        body.setText(sb.toString());
    }
}
