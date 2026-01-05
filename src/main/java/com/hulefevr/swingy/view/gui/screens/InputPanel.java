package com.hulefevr.swingy.view.gui.screens;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.CountDownLatch;

/**
 * Simple panel to collect a single-line text input inside the main window.
 * Provides a blocking waitForResult() to integrate with existing calling code.
 */
public class InputPanel extends JPanel {
    private JLabel promptLabel;
    private JTextField field;
    private JPanel buttons;
    private String result;
    private CountDownLatch latch;

    public InputPanel() {
        setLayout(new BorderLayout(8, 8));
        promptLabel = new JLabel();
        field = new JTextField();

        add(promptLabel, BorderLayout.NORTH);
        add(field, BorderLayout.CENTER);

        buttons = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton ok = new JButton("OK");
        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                result = field.getText();
                if (latch != null) latch.countDown();
            }
        });
        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                result = null;
                if (latch != null) latch.countDown();
            }
        });
        buttons.add(ok);
        buttons.add(cancel);
        add(buttons, BorderLayout.SOUTH);
    }

    public void setPrompt(String title, String prompt) {
        if (title != null) setBorder(BorderFactory.createTitledBorder(title)); else setBorder(null);
        promptLabel.setText(prompt == null ? "" : prompt);
        field.setText("");
    }

    public String waitForResult() {
        latch = new CountDownLatch(1);
        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        String r = result;
        result = null;
        latch = null;
        return r;
    }

    public void requestFocusForField() {
        field.requestFocusInWindow();
    }
}
