package com.hulefevr.swingy.model.ennemy;

/**
 * Types d'ennemis selon le lore.txt.
 * Level 1-3: Watcher
 * Level 4-7: Herald, Thurifer
 * Level 8-10: Archon, Virtue, Dominion
 * Level 11-15: Seraph, Principality
 */
public enum VillainType {
    // Level 1-3
    WATCHER("Watcher", "A Watcher descends. It does not speak. It remembers your crime.", 6, 6, 50),
    
    // Level 4-7
    HERALD("Herald", "The Herald calls your name — a name you no longer bear.", 8, 7, 65),
    THURIFER("Thurifer", "The Thurifer's incense burns your soul.", 9, 6, 60),
    
    // Level 8-10
    ARCHON("Archon", "An Archon blocks your path. It knows how you will die.", 11, 10, 85),
    VIRTUE("Virtue", "A Virtue stands before you, emanating divine wrath.", 10, 11, 90),
    DOMINION("Dominion", "The Dominion judges you unworthy.", 12, 9, 80),
    
    // Level 11-15
    SERAPH("Seraph of Judgment", "The air burns. A Seraph descends.", 15, 12, 110),
    PRINCIPALITY("Principality", "The Choir stands united. A Principality bars your way.", 14, 13, 115);
    
    private final String displayName;
    private final String flavorText;
    private final int baseAttack;
    private final int baseDefense;
    private final int baseHitPoints;
    
    VillainType(String displayName, String flavorText, int baseAttack, int baseDefense, int baseHitPoints) {
        this.displayName = displayName;
        this.flavorText = flavorText;
        this.baseAttack = baseAttack;
        this.baseDefense = baseDefense;
        this.baseHitPoints = baseHitPoints;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getFlavorText() {
        return flavorText;
    }
    
    public int getBaseAttack() {
        return baseAttack;
    }
    
    public int getBaseDefense() {
        return baseDefense;
    }
    
    public int getBaseHitPoints() {
        return baseHitPoints;
    }
}
