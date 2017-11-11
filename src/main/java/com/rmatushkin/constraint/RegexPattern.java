package com.rmatushkin.constraint;

public class RegexPattern {
    public static String URL_PATTERN =
            "^(http:\\/\\/|https:\\/\\/)?(www.)?([a-zA-Z0-9]+).[a-zA-Z0-9]*.[a-z]{3}.?([a-z]+)?$";
    public static final String UTF8_BOM = "\uFEFF";
    public static final String MANY_WHITESPACES = "\\s+";
    public static final String LIMIT_PATTERN = "^(\\d+)([kK]|[mM])$";
}
