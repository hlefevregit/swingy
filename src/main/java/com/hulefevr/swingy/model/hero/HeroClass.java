package com.hulefevr.swingy.model.hero;

/**
 * Enumération des classes de héros disponibles.
 * Chaque classe a des stats de base différentes selon son archétype.
 */
public enum HeroClass {
    EXILE("The Exile", "Balanced warrior, cast out but not broken", 10, 8, 100),
    REVENANT("The Revenant", "Tank, returns from death stronger", 8, 12, 120),
    PENITENT("The Penitent", "High damage, seeks redemption through violence", 12, 6, 95),
    WARDEN("The Warden", "Defender, once guarded Heaven's gates", 9, 11, 110),
    SORCERER("The Sorcerer", "Powerful magic, fragile body", 15, 5, 85);

    private final String displayName;
    private final String description;
    private final int baseAttack;
    private final int baseDefense;
    private final int baseHitPoints;

    HeroClass(String displayName, String description, int baseAttack, int baseDefense, int baseHitPoints) {
        this.displayName = displayName;
        this.description = description;
        this.baseAttack = baseAttack;
        this.baseDefense = baseDefense;
        this.baseHitPoints = baseHitPoints;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
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
