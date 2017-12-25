package com.rmatushkin.http;

import com.rmatushkin.exception.LimitParseException;

import static com.rmatushkin.constraint.RegexPattern.LIMIT_REGEX;
import static com.rmatushkin.http.Unit.KILOBYTE;
import static com.rmatushkin.http.Unit.MEGABYTE;
import static java.lang.Integer.parseInt;
import static java.lang.String.format;

public class Limit {
    private final int value;
    private final Unit unit;

    public Limit(int value, Unit unit) {
        this.value = value;
        this.unit = unit;
    }

    public int getValue() {
        return value;
    }

    public Unit getUnit() {
        return unit;
    }

    public static Limit parseLimit(String string) {
        if (string == null) {
            return null;
        }
        if (string.matches(LIMIT_REGEX)) {
            int limitValue = parseInt(string.substring(0, string.length() - 1));
            if (string.endsWith(KILOBYTE.getLetter())) {
                return new Limit(limitValue, KILOBYTE);
            }
            if (string.endsWith(MEGABYTE.getLetter())) {
                return new Limit(limitValue, MEGABYTE);
            }
        }
        throw new LimitParseException(format("String '%s' can't be parsed!", string));
    }
}
