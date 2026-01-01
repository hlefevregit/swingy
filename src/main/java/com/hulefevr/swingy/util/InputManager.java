package com.hulefevr.swingy.util;

import java.util.Scanner;

/**
 * Classe singleton pour gérer l'entrée utilisateur de manière centralisée.
 * Évite les problèmes de fermeture de System.in avec plusieurs Scanner.
 */
public class InputManager {
    private static Scanner scanner = null;

    private InputManager() {
        // Constructeur privé pour pattern singleton
    }

    /**
     * Retourne l'instance unique du Scanner.
     * Crée le Scanner seulement lors du premier appel.
     */
    public static Scanner getScanner() {
        if (scanner == null) {
            scanner = new Scanner(System.in);
        }
        return scanner;
    }

    /**
     * Lit une ligne de l'entrée standard.
     */
    public static String readLine() {
        return getScanner().nextLine();
    }

    /**
     * Ferme le Scanner (à appeler seulement à la fin du programme).
     */
    public static void close() {
        if (scanner != null) {
            scanner.close();
            scanner = null;
        }
    }
}
