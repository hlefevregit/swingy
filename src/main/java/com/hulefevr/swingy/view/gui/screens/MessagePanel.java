package com.hulefevr.swingy.view.gui.screens;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.CountDownLatch;

/**
 * Simple panel to display a message and optional buttons inside the main window.
 * Provides a blocking waitForResult() to integrate with the existing "AndWait" APIs.
 */
public class MessagePanel extends JPanel {
    private JTextArea area;
    private JPanel buttons;
    private String result;
    private CountDownLatch latch;

    public MessagePanel() {
        setLayout(new BorderLayout(8, 8));
        area = new JTextArea();
        area.setEditable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setFont(area.getFont().deriveFont(14f));
        JScrollPane sp = new JScrollPane(area);
        add(sp, BorderLayout.CENTER);

        buttons = new JPanel(new FlowLayout(FlowLayout.CENTER));
        add(buttons, BorderLayout.SOUTH);
    }

    /**
     * Configure the panel with a message and button labels.
     * Use options == null or empty to show a single OK button.
     */
    public void setMessage(String title, String message, String[] options) {
        if (title != null) {
            // optionally set as a border title
            setBorder(BorderFactory.createTitledBorder(title));
        } else {
            setBorder(null);
        }
        area.setText(message == null ? "" : message);

        buttons.removeAll();
        if (options == null || options.length == 0) {
            options = new String[]{"OK"};
        }
        for (String opt : options) {
            JButton b = new JButton(opt);
            b.setActionCommand(opt);
            b.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    result = e.getActionCommand();
                    if (latch != null) {
                        latch.countDown();
                    }
                }
            });
            buttons.add(b);
        }
        revalidate();
        repaint();
    }

    /**
     * Show and block until the user presses a button. Caller must NOT be on the EDT.
     */
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

    /**
     * Non-blocking helper: just set the message and show it.
     */
    public void showMessageNonBlocking(String title, String message) {
        setMessage(title, message, new String[]{"OK"});
    }
}
