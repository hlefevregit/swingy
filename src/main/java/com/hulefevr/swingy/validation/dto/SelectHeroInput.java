package com.hulefevr.swingy.validation.dto;

public class SelectHeroInput {
    private int heroIndex;

    public SelectHeroInput(int heroIndex) {
        this.heroIndex = heroIndex;
    }

    public int getHeroIndex() {
        return heroIndex;
    }
}
