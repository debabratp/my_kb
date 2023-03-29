package com.my.kb.util;

public enum MarkdownEnum {
    BOLD_1("**"),//will not support
    BOLD_2("__"),
    ITALIC_1("*"), // will not support
    ITALIC_2("_"),
    LIST("* "), // will not support
    HEADERS_1("#"),
    HEADERS_2("##"),
    HEADERS_3("###");

    private String syntax = "";

    MarkdownEnum(String s) {
        syntax = s;
    }


    public String getSyntax() {
        return this.syntax;
    }
}
