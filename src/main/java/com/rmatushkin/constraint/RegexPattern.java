package com.rmatushkin.constraint;

public class RegexPattern {
    public static String URL_PATTERN =
            "^((http[s]?|ftp):\\/)?\\/?([^:\\/\\s]+)((\\/\\w+)*\\/)([\\w\\-\\.]+[^#?\\s]+)(.*)?(#[\\w\\-]+)?$";
    public static final String UTF8_BOM = "\uFEFF";
    public static final String MANY_WHITESPACES = "\\s+";
    public static final String LIMIT_PATTERN = "^(\\d+)([kK]|[mM])$";
}
