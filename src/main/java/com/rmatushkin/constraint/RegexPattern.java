package com.rmatushkin.constraint;

public class RegexPattern {
    public static final String UTF8_BOM = "\uFEFF";
    public static final String MANY_WHITESPACES_REGEX = "\\s+";
    public static final String LIMIT_REGEX = "^([kK]|[mM])=(\\d)$";
    public static final String URL_REGEX = "https?.+";
    public static final String URL_AND_FILE_NAME_REGEX = "^" + URL_REGEX + MANY_WHITESPACES_REGEX + ".+$";
    public static final String FILE_EXTENSION = "\\.\\w+$";

}
