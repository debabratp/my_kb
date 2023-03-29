package com.my.kb.vo;

public enum Type {
    KNOWLEDGE("KNOWLEDGE"),
    ISSUE("ISSUE");

    public static final Type[] ALL = {KNOWLEDGE, ISSUE};


    private final String name;


    public static Type forName(final String name) {
        if (name == null) {
            throw new IllegalArgumentException("Name cannot be null for type");
        }
        if (name.toUpperCase().equals("KNOWLEDGE")) {
            return KNOWLEDGE;
        } else if (name.toUpperCase().equals("ISSUE")) {
            return ISSUE;
        }
        throw new IllegalArgumentException("Name \"" + name + "\" does not correspond to any Type");
    }


    private Type(final String name) {
        this.name = name;
    }


    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return getName();
    }
}
