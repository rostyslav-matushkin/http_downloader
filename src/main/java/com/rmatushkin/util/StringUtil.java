package com.rmatushkin.util;

import static com.rmatushkin.constraint.RegexPattern.MANY_WHITESPACES_REGEX;
import static com.rmatushkin.constraint.RegexPattern.UTF8_BOM;

public class StringUtil {

    public static String removeUtf8Bom(String string) {
        return string.replaceAll(UTF8_BOM, "");
    }

    public static String removeExcessWhitespaces(String string) {
        return string.replaceAll(MANY_WHITESPACES_REGEX, " ");
    }

    public static String removeAllWhitespaces(String string) {
        return string.replaceAll(MANY_WHITESPACES_REGEX, "");
    }
}
