// Entrypoint
package com.hulefevr.swingy;

import com.hulefevr.swingy.controller.GameController;
import com.hulefevr.swingy.view.View;
import com.hulefevr.swingy.view.ViewManager;
import com.hulefevr.swingy.view.console.ConsoleView;
import com.hulefevr.swingy.view.gui.GuiView;
import com.hulefevr.swingy.util.InputManager;

import java.awt.GraphicsEnvironment;

public class App {
    public static void main(String[] args) {
        // Demander à l'utilisateur de choisir entre Console et GUI
        View view = chooseViewMode();
        ViewManager.setView(view);

        // Lancer le contrôleur principal du jeu
        GameController controller = new GameController();
        controller.startGame();
        
        // Fermer le Scanner à la fin
        InputManager.close();
    }

    private static View chooseViewMode() {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║   SWINGY - The Book of the Fallen      ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.println();
        System.out.println("Choisissez votre mode d'affichage :");
        System.out.println("  1. Console (Terminal)");
        System.out.println("  2. GUI (Interface graphique)");
        System.out.println();
        System.out.print("Votre choix (1 ou 2) : ");
        
        while (true) {
            String input = InputManager.readLine().trim();

            if (input.equals("1")) {
                System.out.println("\n✓ Mode Console sélectionné\n");
                return new ConsoleView();
            } else if (input.equals("2")) {
                // If running in a headless environment (no DISPLAY), refuse GUI and fallback
                if (GraphicsEnvironment.isHeadless()) {
                    System.out.println("\nGUI requested but no display is available. Falling back to Console mode.\n");
                    System.out.println("If you intended to run the GUI, start the app with an X server or use xvfb-run.\n");
                    return new ConsoleView();
                }

                System.out.println("\n✓ Mode GUI sélectionné\n");
                return new GuiView();
            } else {
                System.out.print("Choix invalide. Veuillez entrer 1 ou 2 : ");
            }
        }
    }
}