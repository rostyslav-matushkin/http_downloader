package com.rmatushkin.http;

import com.rmatushkin.entity.SingleFile;
import com.rmatushkin.enums.Unit;
import com.rmatushkin.exception.LimitParseException;

import java.util.List;

import static com.rmatushkin.constraint.RegexPattern.LIMIT_REGEX;
import static com.rmatushkin.enums.Unit.KILOBYTE;
import static com.rmatushkin.enums.Unit.MEGABYTE;
import static java.lang.Integer.parseInt;
import static java.lang.String.format;

public class HttpClient {
    private static final int BUFFER_SIZE = 4096;
    private Limit limit;
    private boolean enabledLimit;

    public HttpClient() {
    }

    public HttpClient(Limit limit) {
        this.limit = limit;
    }

    public void download(List<SingleFile> singleFiles) {
        if (enabledLimit) {
            downloadWithLimit(singleFiles);
        } else {
            downloadWithoutLimit(singleFiles);
        }
    }

    private void downloadWithLimit(List<SingleFile> singleFiles) {

    }

    private void downloadWithoutLimit(List<SingleFile> singleFiles) {

    }

    public void enableLimit() {
        enabledLimit = true;
    }

    public void disableLimit() {
        enabledLimit = false;
    }

    public static final class Limit {
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
            if (string.matches(LIMIT_REGEX) && string.toLowerCase().endsWith(KILOBYTE.getLetter())) {
                int limitValue = parseInt(string.substring(0, string.length() - 1));
                return new Limit(limitValue, KILOBYTE);
            }
            if (string.matches(LIMIT_REGEX) && string.toLowerCase().endsWith(MEGABYTE.getLetter())) {
                int limitValue = parseInt(string.substring(0, string.length() - 1));
                return new Limit(limitValue, MEGABYTE);
            }
            throw new LimitParseException(format("String %s can't be parsed!", string));
        }
    }
}
