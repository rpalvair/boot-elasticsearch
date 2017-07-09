package com.palvair.elasticsearch.application;

public enum TypeName {

    USER("user");

    private final String name;

    TypeName(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
