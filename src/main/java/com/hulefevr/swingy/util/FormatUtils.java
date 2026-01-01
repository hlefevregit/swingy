package com.hulefevr.swingy.util;

public class FormatUtils {
    public static String formatHeroListEntry(int index, String name, String type, int level) {
        return String.format("%d) %s the %s (Level %d)", index, name, type, level);
    }
}