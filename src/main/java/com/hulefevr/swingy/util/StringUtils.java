package com.hulefevr.swingy.util;

/**
 * Utilitaires pour manipuler les chaînes de caractères
 */
public class StringUtils {
    
    /**
     * Vérifie si une chaîne est vide ou null
     */
    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    /**
     * Capitalise la première lettre d'une chaîne
     */
    public static String capitalize(String str) {
        if (isEmpty(str)) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }

    /**
     * Répète un caractère n fois
     */
    public static String repeat(char c, int times) {
        StringBuilder sb = new StringBuilder(times);
        for (int i = 0; i < times; i++) {
            sb.append(c);
        }
        return sb.toString();
    }

    /**
     * Centre un texte dans une largeur donnée
     */
    public static String center(String text, int width) {
        if (text.length() >= width) return text;
        int padding = (width - text.length()) / 2;
        return repeat(' ', padding) + text;
    }
}