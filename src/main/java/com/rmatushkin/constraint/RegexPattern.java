package com.rmatushkin.constraint;

public class RegexPattern {
    public static String URL_REGEX =
            "^((http[s]?|ftp):\\/)?\\/?([^:\\/\\s]+)((\\/\\w+)*\\/)([\\w\\-\\.]+[^#?\\s]+)(.*)?(#[\\w\\-]+)?$";
    public static final String UTF8_BOM = "\uFEFF";
    public static final String MANY_WHITESPACES_REGEX = "\\s+";
    public static final String LIMIT_REGEX = "^(\\d+)([kK]|[mM])$";
}
