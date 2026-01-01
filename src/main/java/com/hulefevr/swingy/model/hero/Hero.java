package com.hulefevr.swingy.model.hero;

import com.hulefevr.swingy.model.artifact.Artifact;

/**
 * Représente un héros dans le jeu
 * Les stats finales = stats de base + bonus de level + bonus d'artefacts
 */
public class Hero {
	private String name;
	private HeroClass heroClass;
	private int level;
	private int experience;
	
	// Stats de base (définies par la classe du héros)
	private int baseAttack;
	private int baseDefense;
	private int baseHitPoints;
	
	// HP actuels (peut diminuer en combat)
	private int currentHitPoints;
	
	// Artefacts équipés
	private Artifact weapon;
	private Artifact armor;
	private Artifact helm;

	public Hero(String name, HeroClass heroClass, int baseAttack, int baseDefense, int baseHP) {
		this.name = name;
		this.heroClass = heroClass;
		this.level = 1;
		this.experience = 0;
		this.baseAttack = baseAttack;
		this.baseDefense = baseDefense;
		this.baseHitPoints = baseHP;
		this.currentHitPoints = getMaxHitPoints(); // HP pleins au départ
	}

	// Getters

	public String getName() {
		return name;
	}

	public HeroClass getHeroClass() {
		return heroClass;
	}
	
	public String getHeroClassName() {
		return heroClass.toString();
	}
	
	public int getLevel() {
		return level;
	}
	
	public int getExperience() {
		return experience;
	}
	
	// Alias pour compatibilité
	public int getXp() {
		return experience;
	}
	
	/**
	 * Calcule l'attaque totale = base + bonus level + bonus weapon
	 */
	public int getAttack() {
		int attack = baseAttack + (level - 1) * 2; // +2 ATK par niveau
		if (weapon != null) {
			// TODO: ajouter weapon.getAttackBonus() quand implémenté
		}
		return attack;
	}
	
	/**
	 * Calcule la défense totale = base + bonus level + bonus armor
	 */
	public int getDefense() {
		int defense = baseDefense + (level - 1) * 2; // +2 DEF par niveau
		if (armor != null) {
			// TODO: ajouter armor.getDefenseBonus() quand implémenté
		}
		return defense;
	}
	
	/**
	 * Retourne les HP maximum = base + bonus level + bonus helm
	 */
	public int getMaxHitPoints() {
		int maxHP = baseHitPoints + (level - 1) * 10; // +10 HP par niveau
		if (helm != null) {
			// TODO: ajouter helm.getHPBonus() quand implémenté
		}
		return maxHP;
	}
	
	/**
	 * Retourne les HP actuels
	 */
	public int getHitPoints() {
		return currentHitPoints;
	}
	
	/**
	 * XP nécessaire pour passer au niveau suivant
	 * Formule: level * 1000 + (level-1)^2 * 450
	 */
	public int getXpForNextLevel() {
		return level * 1000 + (level - 1) * (level - 1) * 450;
	}
	
	// Setters et méthodes utilitaires
	
	public void setCurrentHitPoints(int hp) {
		this.currentHitPoints = Math.max(0, Math.min(hp, getMaxHitPoints()));
	}
	
	public void heal(int amount) {
		this.currentHitPoints = Math.min(currentHitPoints + amount, getMaxHitPoints());
	}
	
	public void takeDamage(int damage) {
		this.currentHitPoints = Math.max(0, currentHitPoints - damage);
	}
	
	public boolean isAlive() {
		return currentHitPoints > 0;
	}
	
	/**
	 * Gagne de l'XP et vérifie si le héros monte de niveau
	 * @return true si le héros a level up
	 */
	public boolean gainExperience(int xp) {
		this.experience += xp;
		
		// Vérifier level up
		if (this.experience >= getXpForNextLevel()) {
			levelUp();
			return true;
		}
		return false;
	}
	
	private void levelUp() {
		this.level++;
		// Restaurer les HP au maximum lors du level up
		this.currentHitPoints = getMaxHitPoints();
	}
	
	// Gestion des artefacts
	
	public Artifact getWeapon() {
		return weapon;
	}
	
	public void setWeapon(Artifact weapon) {
		this.weapon = weapon;
	}
	
	public Artifact getArmor() {
		return armor;
	}
	
	public void setArmor(Artifact armor) {
		this.armor = armor;
	}
	
	public Artifact getHelm() {
		return helm;
	}
	
	public void setHelm(Artifact helm) {
		this.helm = helm;
	}
	
	// Setters pour la persistence (restoration depuis DB)
	
	/**
	 * ATTENTION: À utiliser uniquement pour la persistence !
	 * Définit directement le niveau sans recalculer les stats
	 */
	public void setLevel(int level) {
		this.level = level;
	}
	
	/**
	 * ATTENTION: À utiliser uniquement pour la persistence !
	 * Définit directement l'XP
	 */
	public void setExperience(int experience) {
		this.experience = experience;
	}
	
	/**
	 * Calcule la taille de la map pour ce héros
	 * Formule: (level-1)*5+10-(level%2)
	 */
	public int getMapSize() {
		return (level - 1) * 5 + 10 - (level % 2);
	}
}