// bonus switch runtime
package com.hulefevr.swingy.view;

import com.hulefevr.swingy.config.GameMode;

public class ViewManager {
    private static View current;

    public static void setView(View v) {
        current = v;
    }
    public static View getView() {
        return current;
    }
    public static boolean isConsoleMode() {
        return current instanceof com.hulefevr.swingy.view.console.ConsoleView;
    }
    public static boolean isGuiMode() {
        return current instanceof com.hulefevr.swingy.view.gui.GuiView;
    }
}