package com.hulefevr.swingy.validation.dto;

public class CreateHeroInput {
    private String name;
    private String heroClass;

    public CreateHeroInput(String name, String heroClass) {
        this.name = name;
        this.heroClass = heroClass;
    }

    public String getName() {
        return name;
    }

    public String getHeroClass() {
        return heroClass;
    }
}
