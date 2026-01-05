package com.hulefevr.swingy.view.gui.screens;

import com.hulefevr.swingy.model.hero.Hero;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

/**
 * Panel to select/delete a hero from a list. Blocks until user selects or cancels.
 */
public class SelectHeroPanel extends JPanel {
    private JList<String> list;
    private DefaultListModel<String> model;
    private java.util.concurrent.CountDownLatch latch = new java.util.concurrent.CountDownLatch(1);
    private String result; // selected index as string, or null

    public SelectHeroPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(60, 58, 54));

        // Left decorative panel (placeholder for illustration)
        JPanel left = new JPanel();
        left.setPreferredSize(new Dimension(220, 0));
        left.setBackground(new Color(46, 44, 40));
        add(left, BorderLayout.WEST);

        // Right content: title + list + buttons
        JPanel right = new JPanel(new BorderLayout(8, 8));
        right.setOpaque(false);

        JLabel title = new JLabel("Resume the Trial");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 24f));
        title.setForeground(new Color(230, 220, 200));
        title.setBorder(BorderFactory.createEmptyBorder(12, 12, 0, 12));
        right.add(title, BorderLayout.NORTH);

        // List inside a framed area
        model = new DefaultListModel<>();
        list = new JList<>(model);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setBackground(new Color(245, 238, 228));
        list.setForeground(new Color(48, 44, 40));
    // Increase the fixed cell height so name and meta don't overlap
    list.setFixedCellHeight(72);
    list.setFont(list.getFont().deriveFont(16f));
        list.setCellRenderer(new ListCellRenderer<String>() {
            @Override
            public Component getListCellRendererComponent(JList<? extends String> list, String value, int index, boolean isSelected, boolean cellHasFocus) {
                JPanel p = new JPanel();
                p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
                p.setOpaque(true);
                p.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));

                JLabel name = new JLabel();
                name.setFont(name.getFont().deriveFont(Font.BOLD, 18f));
                name.setForeground(new Color(48, 44, 40));

                JLabel meta = new JLabel();
                meta.setFont(meta.getFont().deriveFont(Font.PLAIN, 12f));
                meta.setForeground(new Color(100, 90, 80));
                meta.setBorder(BorderFactory.createEmptyBorder(6, 0, 0, 0));

                // Value format: "Name — CLASS — LVL n — XP x"
                String[] parts = value.split(" — ");
                name.setText(parts.length > 0 ? parts[0] : value);
                if (parts.length > 1) {
                    meta.setText(String.join(" ", java.util.Arrays.copyOfRange(parts, 1, parts.length)));
                } else {
                    meta.setText("");
                }

                p.add(name);
                p.add(meta);
                p.setPreferredSize(new Dimension(p.getPreferredSize().width, 72));

                if (isSelected) {
                    p.setBackground(new Color(200, 180, 140));
                    name.setForeground(new Color(20, 18, 16));
                    meta.setForeground(new Color(30, 24, 20));
                } else {
                    p.setBackground(new Color(245, 238, 228));
                }
                return p;
            }
        });

        JPanel listHolder = new JPanel(new BorderLayout());
        listHolder.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 170, 150), 2),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)));
        listHolder.add(new JScrollPane(list), BorderLayout.CENTER);
        right.add(listHolder, BorderLayout.CENTER);

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 12));
        footer.setOpaque(false);
        JButton select = new JButton("SELECT");
        JButton del = new JButton("DELETE");
        JButton back = new JButton("BACK");
        select.setPreferredSize(new Dimension(120, 36));
        del.setPreferredSize(new Dimension(120, 36));
        back.setPreferredSize(new Dimension(120, 36));
        footer.add(select);
        footer.add(del);
        footer.add(back);
        right.add(footer, BorderLayout.SOUTH);

        add(right, BorderLayout.CENTER);

        select.addActionListener((ActionEvent e) -> onSelect());
        del.addActionListener((ActionEvent e) -> onDelete());
        back.addActionListener((ActionEvent e) -> onBack());
    }

    public void setHeroes(List<Hero> heroes) {
        model.clear();
        for (Hero h : heroes) {
            model.addElement(h.getName() + " — " + h.getHeroClass() + " — LVL " + h.getLevel() + " — XP " + h.getXp());
        }
        if (!model.isEmpty()) list.setSelectedIndex(0);
    }

    private void onSelect() {
        int idx = list.getSelectedIndex();
        if (idx >= 0) result = String.valueOf(idx + 1);
        else result = null;
        latch.countDown();
    }

    private void onDelete() {
        int idx = list.getSelectedIndex();
        if (idx >= 0) result = "D" + (idx + 1);
        else result = null;
        latch.countDown();
    }

    private void onBack() {
        result = "0";
        latch.countDown();
    }

    public String waitForResult() {
        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return result;
    }

    public void reset() {
        model.clear();
        result = null;
        latch = new java.util.concurrent.CountDownLatch(1);
    }
}