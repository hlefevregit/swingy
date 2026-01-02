package com.hulefevr.swingy.model.artifact;

/**
 * Classe pour les artefacts (armes, armures, casques)
 */
public class Artifact {
    private String name;
    private ArtifactType type;
    private int bonusValue;

    public Artifact(String name, ArtifactType type, int bonusValue) {
        this.name = name;
        this.type = type;
        this.bonusValue = bonusValue;
    }

    public String getName() {
        return name;
    }
    
    public ArtifactType getType() {
        return type;
    }
    
    public int getBonusValue() {
        return bonusValue;
    }
    
    @Override
    public String toString() {
        return name + " (" + type.getDisplayName() + " +" + bonusValue + " " + type.getBonusStat() + ")";
    }
}
