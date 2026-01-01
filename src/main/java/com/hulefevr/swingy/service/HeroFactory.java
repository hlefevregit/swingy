// crée un hero avec stats initiales

package com.hulefevr.swingy.service;

import com.hulefevr.swingy.model.hero.Hero;
import com.hulefevr.swingy.model.hero.HeroClass;

/**
 * Factory pour créer des héros avec des stats de base selon leur classe
 */
public class HeroFactory {
    
    /**
     * Crée un nouveau héros de niveau 1
     * @param name Nom du héros
     * @param heroClass Classe du héros
     * @return Le héros créé
     */
    public static Hero createHero(String name, HeroClass heroClass) {
        int baseAttack = 0;
        int baseDefense = 0;
        int baseHP = 0;
        
        // Stats de base selon la classe (inspiré du lore)
        switch (heroClass) {
            case EXILE:
                // Équilibré - Le banni, versatile
                baseAttack = 10;
                baseDefense = 8;
                baseHP = 100;
                break;
                
            case REVENANT:
                // Tank - Celui qui revient, résistant
                baseAttack = 8;
                baseDefense = 12;
                baseHP = 120;
                break;
                
            case PENITENT:
                // DPS - Le pénitent, agressif
                baseAttack = 14;
                baseDefense = 6;
                baseHP = 90;
                break;
                
            case WARDEN:
                // Défensif - Le gardien déchu
                baseAttack = 9;
                baseDefense = 11;
                baseHP = 110;
                break;
                
            case SORCERER:
                // Magique - Haut dégâts, fragile
                baseAttack = 15;
                baseDefense = 5;
                baseHP = 85;
                break;
                
            default:
                // Fallback
                baseAttack = 10;
                baseDefense = 8;
                baseHP = 100;
        }
        
        return new Hero(name, heroClass, baseAttack, baseDefense, baseHP);
    }
    
    /**
     * Retourne la description d'une classe de héros
     */
    public static String getClassDescription(HeroClass heroClass) {
        switch (heroClass) {
            case EXILE:
                return "The Exile - Balanced warrior, cast out but not broken";
            case REVENANT:
                return "The Revenant - Tank, returns from death stronger";
            case PENITENT:
                return "The Penitent - High damage, seeks redemption through violence";
            case WARDEN:
                return "The Warden - Defender, once guarded Heaven's gates";
            case SORCERER:
                return "The Sorcerer - Powerful magic, fragile body";
            default:
                return "Unknown class";
        }
    }
}