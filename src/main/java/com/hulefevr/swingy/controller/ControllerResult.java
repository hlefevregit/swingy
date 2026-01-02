package com.hulefevr.swingy.controller;

import com.hulefevr.swingy.model.game.Encounter;

/**
 * Résultat d'une action du contrôleur (notamment le déplacement)
 */
public class ControllerResult {
	private final boolean encounter;
	private final boolean exitReached;
	private final Encounter encounterData;
	private final String message;
	
	public ControllerResult(boolean encounter, boolean exitReached, Encounter encounterData, String message) {
		this.encounter = encounter;
		this.exitReached = exitReached;
		this.encounterData = encounterData;
		this.message = message;
	}
	
	public boolean hasEncounter() {
		return encounter;
	}
	
	public boolean isExitReached() {
		return exitReached;
	}
	
	public Encounter getEncounter() {
		return encounterData;
	}
	
	public String getMessage() {
		return message;
	}
}
