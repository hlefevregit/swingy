package com.hulefevr.swingy.view.gui.screens;

import com.hulefevr.swingy.validation.dto.CreateHeroInput;
import com.hulefevr.swingy.model.hero.HeroClass;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Panel for creating a hero (name + class). Blocks until user submits or cancels.
 */
public class CreateHeroPanel extends JPanel {
    private JTextField nameField;
    private ButtonGroup classGroup;
    private JLabel errorLabel;
    private java.util.concurrent.CountDownLatch latch = new java.util.concurrent.CountDownLatch(1);
    private CreateHeroInput result;

    public CreateHeroPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(32, 28, 24));

        JPanel center = new JPanel(new GridBagLayout());
        center.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(8, 12, 8, 12);

        JLabel title = new JLabel("SUMMON A FALLEN SOUL");
        title.setFont(new Font("Serif", Font.BOLD, 28));
        title.setForeground(new Color(230, 220, 200));
        gbc.gridwidth = 2;
        center.add(title, gbc);

        gbc.gridy++;
        gbc.gridwidth = 1;
        JLabel nameLabel = new JLabel("Name (ink it):");
        nameLabel.setForeground(new Color(200, 190, 170));
        center.add(nameLabel, gbc);

        nameField = new JTextField(24);
        gbc.gridx = 1;
        center.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        JLabel calling = new JLabel("Calling:");
        calling.setForeground(new Color(200, 190, 170));
        center.add(calling, gbc);

        JPanel radios = new JPanel(new GridLayout(0, 1));
        radios.setOpaque(false);
        classGroup = new ButtonGroup();
        for (HeroClass hc : HeroClass.values()) {
            JRadioButton rb = new JRadioButton(hc.name().charAt(0) + hc.name().substring(1).toLowerCase() + " — " + (hc == HeroClass.REVENANT ? "wrath / attack" : hc == HeroClass.EXILE ? "balanced" : hc == HeroClass.PENITENT ? "endurance" : ""));
            rb.setActionCommand(hc.name());
            rb.setForeground(new Color(230, 220, 200));
            rb.setOpaque(false);
            if (classGroup.getButtonCount() == 0) rb.setSelected(true);
            classGroup.add(rb);
            radios.add(rb);
        }

        gbc.gridy++;
        center.add(radios, gbc);

        // Error label
        errorLabel = new JLabel("");
        errorLabel.setForeground(new Color(180, 80, 60));
        gbc.gridy++;
        center.add(errorLabel, gbc);

        add(center, BorderLayout.CENTER);

        // Footer with buttons
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.setOpaque(false);
        JButton summon = new JButton("SUMMON");
        JButton back = new JButton("BACK");
        footer.add(summon);
        footer.add(back);
        add(footer, BorderLayout.SOUTH);

        summon.addActionListener((ActionEvent e) -> onSummon());
        back.addActionListener((ActionEvent e) -> onBack());
    }

    private void onSummon() {
        String name = nameField.getText();
        if (name == null || name.trim().isEmpty()) {
            errorLabel.setText("Name must not be blank.");
            return;
        }
        String selected = null;
        for (AbstractButton b : java.util.Collections.list(classGroup.getElements())) {
            if (b.isSelected()) {
                selected = b.getActionCommand();
                break;
            }
        }
        result = new CreateHeroInput(name.trim(), selected != null ? selected : HeroClass.EXILE.name());
        latch.countDown();
    }

    private void onBack() {
        result = null;
        latch.countDown();
    }

    /**
     * Blocks until user selects Summon or Back and returns a CreateHeroInput or null if cancelled.
     * Caller MUST NOT be on the EDT.
     */
    public CreateHeroInput waitForResult() {
        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return result;
    }

    public void reset() {
        nameField.setText("");
        errorLabel.setText("");
        classGroup.clearSelection();
        // select first
        java.util.Enumeration<AbstractButton> en = classGroup.getElements();
        if (en.hasMoreElements()) en.nextElement().setSelected(true);
        latch = new java.util.concurrent.CountDownLatch(1);
        result = null;
    }
}
