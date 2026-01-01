// formules XP, taille map, taux fuite...
package com.hulefevr.swingy.config;

public class GameRule {


    public static int mapSize(int level) {
        return (level - 1) * 5 + 10 - (level % 2);
    }

    public static int xpToLevelUp(int level) {
        return level * 1000 + (level - 1) * (level - 1) * 450;
    }

    public static double RUN_SUCCES_CHANCE = 0.5;

}