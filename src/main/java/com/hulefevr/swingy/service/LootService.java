package com.hulefevr.swingy.service;

import com.hulefevr.swingy.model.hero.Hero;
import com.hulefevr.swingy.model.artifact.Artifact;
import com.hulefevr.swingy.model.artifact.ArtifactType;
import java.util.Random;

/**
 * Service gérant le loot et les artefacts.
 */
public class LootService {
    private Random random = new Random();
    
    // Noms narratifs d'artefacts selon lore.txt
    private static final String[] WEAPON_NAMES = {
        "Blade of Blasphemy",
        "Spear of the First Fall",
        "Ashen Claw",
        "Instrument of Defiance"
    };
    
    private static final String[] ARMOR_NAMES = {
        "Sigil of Scourged Wings",
        "Mantle of the Cast Out",
        "Carapace of Judgment"
    };
    
    private static final String[] HELM_NAMES = {
        "Crown of the First Choir",
        "Halo of Ash",
        "Diadem of Silence",
        "Crown of Memory"
    };
    
    /**
     * Génère un artefact aléatoire selon le niveau.
     */
    public Artifact generateLoot(int level) {
        ArtifactType type = ArtifactType.values()[random.nextInt(ArtifactType.values().length)];
        String name;
        
        switch (type) {
            case WEAPON:
                name = WEAPON_NAMES[random.nextInt(WEAPON_NAMES.length)];
                break;
            case ARMOR:
                name = ARMOR_NAMES[random.nextInt(ARMOR_NAMES.length)];
                break;
            case HELM:
                name = HELM_NAMES[random.nextInt(HELM_NAMES.length)];
                break;
            default:
                name = "Unknown Relic";
        }
        
        // Bonus basé sur le niveau
        int bonus = 2 + level;
        
        return new Artifact(name, type, bonus);
    }
    
    /**
     * Équipe un artefact sur le héros.
     */
    public void equipArtifact(Hero hero, Artifact artifact) {
        switch (artifact.getType()) {
            case WEAPON:
                hero.setWeapon(artifact);
                break;
            case ARMOR:
                hero.setArmor(artifact);
                break;
            case HELM:
                hero.setHelm(artifact);
                break;
        }
    }
}
