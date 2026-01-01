package com.hulefevr.swingy.model.artifact;

/**
 * Classe abstraite pour les artefacts (armes, armures, casques)
 */
public abstract class Artifact {
    private String name;

    public Artifact(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}