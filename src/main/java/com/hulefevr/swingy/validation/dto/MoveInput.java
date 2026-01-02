package com.hulefevr.swingy.validation.dto;

public class MoveInput {
    private String direction;

    public MoveInput(String direction) {
        this.direction = direction;
    }

    public String getDirection() {
        return direction;
    }
}
