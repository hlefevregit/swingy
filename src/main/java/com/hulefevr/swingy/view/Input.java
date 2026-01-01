package com.hulefevr.swingy.view;

/**
 * Interface pour lire les entrées utilisateur
 */
public interface Input {
    /**
     * Lit une entrée utilisateur avec un prompt
     * @param prompt Le message à afficher
     * @return La chaîne saisie par l'utilisateur
     */
    String getInput(String prompt);
}
