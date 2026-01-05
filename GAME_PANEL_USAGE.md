# GamePanel - Documentation d'utilisation

## Vue d'ensemble

Le `GamePanel` est un composant Swing qui affiche l'interface de jeu principale avec un design inspiré de "The Book of the Fallen". Il suit le design fourni dans l'image de référence.

## Caractéristiques

### Design visuel
- **Cadre décoratif** : Bordures ornementées avec effet parchemin et or
- **Hero Sheet** : Affichage des stats du héros en haut (nom, niveau, XP, ATK, DEF, HP)
- **Carte de jeu** : Visualisation de la carte avec le héros (@), les ennemis (V) et la sortie (X)
- **Panneau de messages** : Zone de texte à droite pour les événements du jeu
- **Boutons de direction** : N, E, S, W pour se déplacer
- **Boutons d'action** : STATS, MENU, SWITCH VIEW

### Palette de couleurs
- `PARCHMENT` : Fond parchemin (220, 205, 175)
- `DARK_BROWN` : Marron foncé pour les boutons (40, 30, 20)
- `BORDER_GOLD` : Bordures dorées (160, 140, 90)
- `MAP_BG` : Fond de carte sombre (30, 30, 30)

## Utilisation dans GuiWindow

Le `GamePanel` est automatiquement ajouté au `GuiWindow` lors de l'initialisation :

```java
// Dans GuiWindow.java
public void showGame(GameState gameState) {
    SwingUtilities.invokeLater(() -> {
        gamePanel.updateGameState(gameState);
        cards.show(root, GAME);
        root.revalidate();
        root.repaint();
    });
}
```

## Méthodes publiques du GamePanel

### updateGameState(GameState state)
Met à jour l'affichage avec l'état actuel du jeu :
```java
gamePanel.updateGameState(gameState);
```

### addMessage(String message)
Ajoute un message dans le panneau de messages :
```java
gamePanel.addMessage("Vous avez bougé vers le Nord");
gamePanel.addMessage("Un ennemi apparaît !");
```

### clearMessages()
Efface tous les messages :
```java
gamePanel.clearMessages();
```

### Définir les actions des boutons

```java
GamePanel panel = window.getGamePanel();

// Boutons de direction
panel.setNorthAction(e -> {
    // Action pour aller au Nord
});

panel.setEastAction(e -> {
    // Action pour aller à l'Est
});

panel.setSouthAction(e -> {
    // Action pour aller au Sud
});

panel.setWestAction(e -> {
    // Action pour aller à l'Ouest
});

// Boutons d'action
panel.setStatsAction(e -> {
    // Afficher les stats du héros
});

panel.setMenuAction(e -> {
    // Retourner au menu
});

panel.setSwitchViewAction(e -> {
    // Basculer entre GUI et Console
});
```

## Exemple complet d'utilisation

```java
// Dans GameLoopController ou GameController

public void startGame(Hero hero) {
    // Créer l'état du jeu
    GameState gameState = new GameState();
    gameState.setHero(hero);
    
    // Générer la carte
    GameMap map = mapGenerator.generateMap(hero.getLevel());
    gameState.setMap(map);
    
    // Afficher le panneau de jeu
    GuiWindow window = ((GuiView) view).getWindow();
    window.showGame(gameState);
    
    // Configurer les actions des boutons
    GamePanel panel = window.getGamePanel();
    
    panel.setNorthAction(e -> moveHero("N", gameState, window));
    panel.setEastAction(e -> moveHero("E", gameState, window));
    panel.setSouthAction(e -> moveHero("S", gameState, window));
    panel.setWestAction(e -> moveHero("W", gameState, window));
    
    panel.setMenuAction(e -> {
        window.showMainMenu();
    });
    
    panel.setSwitchViewAction(e -> {
        // Implémenter le changement de vue
    });
}

private void moveHero(String direction, GameState state, GuiWindow window) {
    // Logique de mouvement
    Hero hero = state.getHero();
    Position newPos = movementService.calculateNewPosition(hero.getPosition(), direction);
    
    if (state.getMap().isValidPosition(newPos)) {
        hero.setPosition(newPos);
        window.addGameMessage("Vous avez bougé vers " + getDirectionName(direction));
        
        // Vérifier les rencontres
        if (state.getMap().hasEncounterAt(newPos)) {
            window.addGameMessage("Un ennemi apparaît !");
            // Gérer le combat
        }
        
        // Mettre à jour l'affichage
        window.updateGameState(state);
    }
}
```

## Affichage de la carte

La carte est rendue automatiquement dans le `MapCanvas` :
- **@** : Position du héros (blanc)
- **V** : Ennemis/Villains (rouge)
- **X** : Sortie de la carte (vert)
- Zone autour du héros éclaircie (rayon de 3 cases)

La taille des cellules est de 12x12 pixels par défaut, ajustable via `CELL_SIZE`.

## Notes techniques

- Tous les appels à l'UI doivent être faits sur l'EDT (Event Dispatch Thread)
- Le GamePanel utilise un `MapCanvas` interne pour le rendu de la carte
- Les messages sont automatiquement scrollés vers le bas
- La police utilisée est `Serif` pour un effet "livre ancien"

## Améliorations futures possibles

1. Ajouter des animations pour les mouvements
2. Implémenter des effets visuels pour les combats
3. Ajouter des icônes pour les artefacts équipés
4. Créer une barre de progression pour l'XP
5. Ajouter des tooltips sur les boutons
6. Implémenter un système de mini-map
