package com.hulefevr.swingy.validation.dto;

public class SelectHeroInput {
    private String choice;

    public SelectHeroInput(String choice) {
        this.choice = choice;
    }

    public String getChoice() {
        return choice;
    }
}