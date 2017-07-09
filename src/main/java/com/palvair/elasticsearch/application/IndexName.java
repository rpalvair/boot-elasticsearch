package com.palvair.elasticsearch.application;


public enum IndexName {

    USER("user");

    private final String name;

    IndexName(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
