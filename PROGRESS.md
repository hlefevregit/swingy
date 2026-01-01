# SWINGY - Progress Tracker
## The Book of the Fallen - Développement du jeu

---

## ✅ Phase 1 : Configuration & Infrastructure (TERMINÉ)

### 1.1 Configuration Maven
**Objectif** : Créer un `pom.xml` valide et expliquer sa structure
**Détails** :
- Créé `pom.xml` complet avec :
  - Java 17 pour la compilation
  - Dépendances : SQLite JDBC (3.44.1.0), Validation API (2.0.1.Final), JUnit Jupiter (5.10.1)
  - Plugins : maven-compiler-plugin, maven-shade-plugin (pour uber-jar), exec-maven-plugin
- Configuration pour exécution avec `mvn exec:java`
**Fichiers concernés** : `pom.xml`

### 1.2 Correction des erreurs de compilation (51 erreurs)
**Objectif** : Corriger toutes les erreurs de compilation
**Détails** :
- Erreurs corrigées :
  - Classes dans les mauvais fichiers (`Position.java` contenait `GameMap`)
  - Package typo (`hulefevre` → `hulefevr` dans `Artifact.java`)
  - Imports manquants (`java.util.List` dans `View.java`)
  - Constructeurs DTO incorrects (boolean → String)
  - Méthodes manquantes (`Hero.getXp()`, `Encounter.getEnemy()`, `GameState.getHero()`)
- Classes créées :
  - `BattleResult.java` : Résultat de combat (victory, XP, loot)
  - `Input.java` : Interface pour abstraction input utilisateur
  - `StringUtils.java` : Recréée avec méthodes utilitaires
  - `Position.java` : Recréée proprement avec Position(x,y)
**Fichiers concernés** : `Position.java`, `StringUtils.java`, `SelectHeroPanel.java`, `Artifact.java`, `GameState.java`, `Encounter.java`, `Hero.java`, `View.java`, `ConsoleView.java`, `ConsoleRenderer.java`, `GuiView.java`, `ViewManager.java`, `App.java`
**Documentation** : `CORRECTIONS.md` créée avec détails de toutes les corrections

---

## ✅ Phase 2 : Système de Héros (TERMINÉ)

### 2.1 Modèle Hero complet
**Objectif** : Implémenter le modèle Hero avec système de classes, stats, levels et XP
**Détails** :
- Enum `HeroClass` avec 5 classes :
  - `EXILE` : Balanced (10 ATK, 8 DEF, 100 HP)
  - `REVENANT` : Tank (8 ATK, 12 DEF, 120 HP)
  - `PENITENT` : DPS (12 ATK, 6 DEF, 95 HP)
  - `WARDEN` : Defender (9 ATK, 11 DEF, 110 HP)
  - `SORCERER` : Glass cannon (15 ATK, 5 DEF, 85 HP)
- Système de stats dynamiques :
  - `getAttack()` = base + (level-1)*2 + weapon bonus
  - `getDefense()` = base + (level-1)*2 + armor bonus
  - `getMaxHitPoints()` = base + (level-1)*10 + helm bonus
- Système XP :
  - Formule XP pour next level : `level * 1000 + (level-1)² * 450`
  - Méthode `gainExperience(int xp)` avec level-up automatique
- Calcul taille de map :
  - Formule : `(level-1) * 5 + 10 - (level % 2)`
- Slots d'artefacts : weapon, armor, helm
- Setters de persistence (setLevel, setExperience) pour restauration depuis DB
**Fichiers concernés** : `Hero.java`

### 2.2 HeroFactory - Création des héros
**Objectif** : Factory pattern pour créer des héros avec stats selon la classe
**Détails** :
- Méthode `createHero(String name, HeroClass heroClass)`
- Stats de base assignées selon la classe choisie
- Descriptions uniques pour chaque classe (thème fallen angels)
- Initialisation à level 1 avec 0 XP
**Fichiers concernés** : `HeroFactory.java`

### 2.3 MenuController - Interface création
**Objectif** : Gérer la création et l'affichage des héros
**Détails** :
- `createHero()` : Workflow complet de création
  - Affiche les 5 classes disponibles avec descriptions
  - Demande nom et classe via View
  - Validation des inputs
  - Appelle HeroFactory
  - Retourne le héros créé
- `displayHeroStats(Hero hero)` : Affichage formaté
  - Box Unicode avec caractères de dessin (━, ║, etc.)
  - Stats complètes : nom, classe, level, XP, ATK, DEF, HP
  - Calcul XP nécessaire pour next level
  - Taille de map affichée
**Fichiers concernés** : `MenuController.java`

### 2.4 Validation de création
**Objectif** : Validation robuste des inputs utilisateur
**Détails** :
- Nom non vide
- Classe valide (doit exister dans HeroClass enum)
- Gestion des erreurs avec messages clairs
- DTO `CreateHeroInput` pour encapsulation
**Fichiers concernés** : `MenuController.java`, `CreateHeroInput.java`

### 2.5 Affichage des stats
**Objectif** : Afficher les stats d'un héros de manière claire
**Détails** :
- Formatage avec box Unicode
- Affichage de tous les attributs pertinents
- Support des artefacts équipés (slots weapon/armor/helm)
- Formule XP affichée (current / needed)
**Fichiers concernés** : `MenuController.java`

---

## ✅ Phase 3 : Persistence SQLite (TERMINÉ)

### 3.1 Schéma de base de données
**Objectif** : Créer le schéma SQLite pour stocker les héros
**Détails** :
- Table `heroes` :
  - `name` TEXT PRIMARY KEY (unique)
  - `hero_class` TEXT NOT NULL
  - `level` INTEGER NOT NULL
  - `experience` INTEGER NOT NULL
  - `base_attack` INTEGER NOT NULL
  - `base_defense` INTEGER NOT NULL
  - `base_hit_points` INTEGER NOT NULL
  - Colonnes FK pour artefacts (weapon_id, armor_id, helm_id) - préparées pour futur
- Table `artifacts` (préparée pour futur)
- Méthode `initializeSchema()` avec CREATE TABLE IF NOT EXISTS
**Fichiers concernés** : `DbSchema.java`

### 3.2 Repository Pattern
**Objectif** : Interface pour abstraction de la persistence
**Détails** :
- Interface `HeroRepository` avec méthodes :
  - `void save(Hero hero)` : Sauvegarder/mettre à jour
  - `Hero findByName(String name)` : Charger par nom
  - `List<Hero> findAll()` : Charger tous les héros
  - `void delete(String name)` : Supprimer un héros
  - `boolean exists(String name)` : Vérifier existence
- Permet de changer facilement l'implémentation (fichier, DB, etc.)
**Fichiers concernés** : `HeroRepository.java`

### 3.3 Implémentation JDBC
**Objectif** : Implémentation concrète avec SQLite
**Détails** :
- Classe `JdbcHeroRepository implements HeroRepository`
- Connection SQLite via `jdbc:sqlite:swingy.db`
- `save()` utilise ON CONFLICT DO UPDATE (upsert)
- Utilise `HeroRowMapper` pour conversion ResultSet → Hero
- Méthode `close()` pour fermer la connexion proprement
- Initialisation du schéma dans le constructeur
**Fichiers concernés** : `JdbcHeroRepository.java`

### 3.4 Mapping ResultSet → Hero
**Objectif** : Convertir les données SQL en objets Hero
**Détails** :
- Classe `HeroRowMapper`
- Méthode `mapRow(ResultSet rs)` :
  - Récupère toutes les colonnes
  - Crée un Hero avec le constructeur de persistence
  - Utilise les setters de persistence pour restaurer l'état
  - Reconstruit les stats à partir des valeurs de base
**Fichiers concernés** : `HeroRowMapper.java`

### 3.5 Intégration dans GameController
**Objectif** : Utiliser la persistence dans le jeu
**Détails** :
- Ajout du champ `HeroRepository heroRepository`
- Initialisation dans constructeur : `new JdbcHeroRepository()`
- Sauvegarde après création de héros
- Chargement pour sélection de héros
- Fermeture de connexion dans cleanup
**Fichiers concernés** : `GameController.java`

### 3.6 Tests de persistence
**Résultats** :
- 3 héros créés et sauvegardés : Azrael (REVENANT), Seraph (SORCERER), Morningstar (WARDEN)
- Base de données `swingy.db` : 20KB
- Requête SQL confirmée : toutes les données correctes
- Chargement testé : héros restaurés avec stats correctes

---

## ✅ Phase 4 : Boucle du jeu et Menu (TERMINÉ)

### 4.1 GameController - Boucle principale
**Objectif** : Implémenter la boucle principale du jeu
**Détails** :
- Méthode `startGame()` avec loop while(running)
- Menu avec 4 options :
  1. Create Hero → `createHero()`
  2. Select Hero → `selectExistingHero()`
  3. Show Lore → `showLore()` (lit `lore.txt`)
  4. Exit → quit proprement
- Gestion des choix invalides
- Cleanup de la DB à la sortie
**Fichiers concernés** : `GameController.java`

### 4.2 Affichage du lore
**Objectif** : Lire et afficher le fichier lore.txt
**Détails** :
- Méthode `showLore(View view)`
- Lit `lore.txt` avec `Files.readAllLines()`
- Affiche ligne par ligne via View
- Gestion d'erreur si fichier manquant
**Fichiers concernés** : `GameController.java`

### 4.3 Intégration des Views
**Objectif** : Connecter la boucle aux vues Console/GUI
**Détails** :
- ViewManager pattern singleton
- Abstraction View interface
- Toutes les interactions passent par View
- Support Console et GUI (GUI en stubs pour futur)
**Fichiers concernés** : `ViewManager.java`, `View.java`, `ConsoleView.java`, `GuiView.java`

---

## ✅ Phase 5 : Gestion des Entrées (TERMINÉ)

### 5.1 InputManager Singleton
**Objectif** : Gérer une seule instance de Scanner pour tout le programme
**Problème résolu** : Éviter les conflits de fermeture de System.in avec plusieurs Scanner
**Détails** :
- Pattern Singleton
- Méthode `getScanner()` : retourne instance unique
- Méthode `readLine()` : wrapper pour faciliter l'usage
- Méthode `close()` : fermeture propre à la fin du programme
- Tous les Scanner locaux remplacés par InputManager
**Fichiers concernés** : `InputManager.java` (CRÉÉ), `App.java`, `ConsoleView.java`

---

## ✅ Phase 6 : Choix Console/GUI (TERMINÉ)

### 6.1 Écran de sélection au démarrage
**Objectif** : Permettre de choisir entre mode Console et GUI au lancement
**Détails** :
- Méthode `chooseViewMode()` dans `App.java`
- Affichage d'un menu stylisé au démarrage :
  ```
  ╔════════════════════════════════════════╗
  ║   SWINGY - The Book of the Fallen      ║
  ╚════════════════════════════════════════╝
  
  Choisissez votre mode d'affichage :
    1. Console (Terminal)
    2. GUI (Interface graphique)
  ```
- Validation de l'input (1 ou 2)
- Boucle jusqu'à choix valide
- Initialise ViewManager avec la vue choisie
- Message de confirmation
**Fichiers concernés** : `App.java`

### 6.2 Focus sur Console
**Statut** : Mode Console entièrement implémenté
**Statut GUI** : Stubs préparés avec JOptionPane pour implémentation future

---

## ✅ Phase 7 : Sélection de Héros (TERMINÉ)

### 7.1 Liste des héros sauvegardés
**Objectif** : Afficher tous les héros depuis la DB
**Détails** :
- Méthode `selectExistingHero()` dans GameController
- Appel `heroRepository.findAll()`
- Affichage formaté : `N. NOM (CLASSE) - Level X - Y XP`
- Option 0 pour retourner au menu
- Message si aucun héros trouvé
**Fichiers concernés** : `GameController.java`

### 7.2 Choix du héros
**Objectif** : Permettre de sélectionner un héros pour jouer
**Détails** :
- Nouvelle méthode View : `promptSelectHeroChoice()`
- Validation du choix :
  - Doit être un nombre
  - Doit être dans la plage [0, nb_heroes]
  - 0 = retour au menu
- Messages d'erreur clairs pour choix invalides
**Fichiers concernés** : `View.java`, `ConsoleView.java`, `ConsoleRenderer.java`, `GuiView.java`

### 7.3 Affichage stats du héros sélectionné
**Objectif** : Confirmer le choix avec stats complètes
**Détails** :
- Récupération du héros depuis la liste
- Appel `menuController.displayHeroStats(selectedHero)`
- Affichage du box Unicode avec toutes les stats
- Message de confirmation : "✓ Selected hero:"
**Fichiers concernés** : `GameController.java`

### 7.4 Démarrage du jeu avec le héros
**Objectif** : Préparer le lancement du jeu
**Détails** :
- Nouvelle méthode `startGameWithHero(Hero hero, View view)`
- Affiche : "=== Starting game with [NOM] ==="
- Calcule et affiche la taille de map
- Placeholder : "[Game loop will be implemented next]"
- Attend input utilisateur (Press Enter to continue...)
- Retourne au menu principal après
**Fichiers concernés** : `GameController.java`

### 7.5 Tests réalisés
**Scénarios testés** :
- ✅ Sélection Azrael (REVENANT) - Stats correctes : 8/12/120
- ✅ Sélection Seraph (SORCERER) - Stats correctes : 15/5/85
- ✅ Sélection Morningstar (WARDEN) - Stats correctes : 9/11/110
- ✅ Retour au menu (choix 0)
- ✅ Validation choix invalide (99 → message d'erreur)
- ✅ Calcul taille map correcte (9x9 pour level 1)

---

## 📊 État Actuel du Projet

### Fichiers Créés (Total : 9)
1. `pom.xml` - Configuration Maven complète
2. `BattleResult.java` - Modèle résultat de combat
3. `Input.java` - Interface abstraction input
4. `CORRECTIONS.md` - Documentation des 51 corrections
5. `InputManager.java` - Singleton gestion Scanner
6. `DbSchema.java` - Schéma SQLite
7. `HeroRepository.java` - Interface repository
8. `HeroRowMapper.java` - Mapping SQL → Hero
9. `JdbcHeroRepository.java` - Implémentation JDBC

### Fichiers Modifiés (Total : 16+)
1. `Hero.java` - Réécriture complète avec HeroClass, stats, XP, artifacts
2. `HeroFactory.java` - Implémentation createHero() avec 5 classes
3. `MenuController.java` - createHero() workflow + displayHeroStats()
4. `GameController.java` - Boucle principale + persistence + sélection héros
5. `App.java` - Choix Console/GUI + InputManager
6. `View.java` - Ajout imports + promptSelectHeroChoice()
7. `ConsoleView.java` - InputManager + promptSelectHeroChoice()
8. `ConsoleRenderer.java` - Corrections + promptSelectHeroChoice()
9. `GuiView.java` - Corrections + promptSelectHeroChoice()
10. `ViewManager.java` - Corrections références vues
11. `Position.java` - Recréée proprement
12. `StringUtils.java` - Recréée avec méthodes
13. `Artifact.java` - Fix package typo
14. `GameState.java` - Ajout Hero hero + getters/setters
15. `Encounter.java` - Ajout Villain enemy + méthodes
16. `SelectHeroPanel.java` - Fix duplicate class

### Base de Données
- **Fichier** : `swingy.db` (20KB)
- **Héros stockés** : 3 (Azrael, Seraph, Morningstar)
- **Tables** : heroes (complète), artifacts (préparée)

---

## 🎯 Prochaines Étapes Suggérées

### Priorité 1 : Gameplay Core
1. **Génération de Map**
   - Implémenter `MapGenerator.generate(int size)`
   - Créer grille 2D avec formule taille
   - Placer héros au centre
   - Placer sortie sur bord
   - Placer ennemis aléatoires

2. **Système de Mouvement**
   - Implémenter `MovementService.move(Hero, Direction, GameMap)`
   - Validation déplacement (bords de map)
   - Détection collisions
   - Mise à jour position
   - Vérification victoire (atteint sortie)

3. **Système de Rencontres**
   - Génération aléatoire d'ennemis selon level
   - Types d'ennemis selon lore.txt :
     - Level 1-3: Watcher
     - Level 4-7: Herald, Thurifer
     - Level 8-10: Archon, Virtue, Dominion
     - Level 11-15: Seraph, Principality
   - Stats ennemis selon type

### Priorité 2 : Combat & Progression
4. **Système de Combat**
   - Implémenter `BattleService.fight(Hero, Villain)`
   - Calculs attaque/défense
   - Gain XP en cas de victoire
   - Option fuite (run)
   - Game over si défaite

5. **Système de Loot**
   - Génération artefacts aléatoires
   - 3 types : weapon (+ATK), armor (+DEF), helm (+HP)
   - Choix équiper/ignorer
   - Mise à jour stats héros

6. **Level-Up**
   - Recalcul stats automatique
   - Nouvelle map si level-up
   - Messages de lore selon level

### Priorité 3 : Polish & Features
7. **Sauvegarde Persistence Artefacts**
   - Sauvegarder artefacts équipés dans DB
   - Restaurer à la sélection

8. **Interface GUI**
   - Implémenter vraies fenêtres Swing
   - Remplacer JOptionPane stubs
   - Panels pour game, map, encounters

9. **Boss Final & Narratif**
   - Boss de fin selon lore
   - Séquence narrative de victoire

---

## 📈 Formules & Constantes du Jeu

### Stats de Base par Classe
| Classe    | ATK | DEF | HP  | Style         |
|-----------|-----|-----|-----|---------------|
| EXILE     | 10  | 8   | 100 | Balanced      |
| REVENANT  | 8   | 12  | 120 | Tank          |
| PENITENT  | 12  | 6   | 95  | DPS           |
| WARDEN    | 9   | 11  | 110 | Defender      |
| SORCERER  | 15  | 5   | 85  | Glass Cannon  |

### Formules de Calcul
- **Taille Map** : `(level - 1) * 5 + 10 - (level % 2)`
- **XP Next Level** : `level * 1000 + (level - 1)² * 450`
- **Attack** : `base_attack + (level - 1) * 2 + weapon_bonus`
- **Defense** : `base_defense + (level - 1) * 2 + armor_bonus`
- **Max HP** : `base_hp + (level - 1) * 10 + helm_bonus`

### Exemples Tailles de Map
- Level 1 → 9x9
- Level 2 → 14x14
- Level 3 → 19x19
- Level 4 → 24x24
- Level 5 → 29x29

---

## 🔧 Commandes Utiles

### Compilation
```bash
mvn clean compile
```

### Exécution
```bash
mvn exec:java
```

### Exécution silencieuse (sans logs Maven)
```bash
mvn exec:java -q
```

### Tests automatisés (exemple)
```bash
echo -e "1\n1\nTestHero\nEXILE\n4" | mvn exec:java -q
```

### Vérifier base de données
```bash
sqlite3 swingy.db "SELECT * FROM heroes;"
```

---

## 📝 Notes Techniques

### Architecture MVC
- **Model** : Hero, Artifact, Villain, Encounter, GameState
- **View** : Interface View + ConsoleView + GuiView
- **Controller** : GameController, MenuController

### Patterns Utilisés
- **Singleton** : InputManager, ViewManager
- **Factory** : HeroFactory
- **Repository** : HeroRepository (interface + JdbcHeroRepository)
- **DTO** : CreateHeroInput, MoveInput, FightRunInput, etc.

### Dépendances
- SQLite JDBC 3.44.1.0
- Jakarta Validation API 2.0.1.Final
- JUnit Jupiter 5.10.1

---

**Dernière mise à jour** : 1 janvier 2026
**Status** : Infrastructure complète ✅ | Gameplay en cours ⏳
