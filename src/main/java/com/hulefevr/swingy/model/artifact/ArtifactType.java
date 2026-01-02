package com.hulefevr.swingy.model.artifact;

/**
 * Types d'artefacts disponibles dans le jeu
 */
public enum ArtifactType {
	WEAPON("Weapon", "attack"),
	ARMOR("Armor", "defense"),
	HELM("Helm", "hitPoints");
	
	private final String displayName;
	private final String bonusStat;
	
	ArtifactType(String displayName, String bonusStat) {
		this.displayName = displayName;
		this.bonusStat = bonusStat;
	}
	
	public String getDisplayName() {
		return displayName;
	}
	
	public String getBonusStat() {
		return bonusStat;
	}
}
