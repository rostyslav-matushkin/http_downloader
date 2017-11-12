package com.rmatushkin.http;

import com.rmatushkin.enums.Unit;
import com.rmatushkin.exception.HttpClientException;
import com.rmatushkin.exception.LimitException;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

import static com.rmatushkin.constraint.RegexPattern.LIMIT_PATTERN;
import static com.rmatushkin.enums.Unit.KILOBYTE;
import static com.rmatushkin.enums.Unit.MEGABYTE;
import static java.lang.Integer.parseInt;
import static java.lang.String.format;

public class HttpClient {
    private static final int DEFAULT_LIMIT_VALUE = 10;
    private static final Unit DEFAULT_LIMIT_UNIT = MEGABYTE;
    private Limit limit;

    public HttpClient() {
        limit = getDefaultLimit();
    }

    public void download(String url, String destinationFilePath) {
        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(new URL(url).openStream());
             FileOutputStream fileOutputStream = new FileOutputStream(destinationFilePath)) {
            int length = limit.getValue() * limit.getUnit().getBytes();
            byte[] buffer = new byte[length];
            int count;
            while ((count = bufferedInputStream.read(buffer, 0, length)) != -1) {
                fileOutputStream.write(buffer, 0, count);
            }
        } catch (IOException e) {
            throw new HttpClientException(e.getMessage());
        }
    }

    public void enableLimit(Limit limit) {
        this.limit = limit;
    }

    public void disableLimit() {
        limit = getDefaultLimit();
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
            if (string.matches(LIMIT_PATTERN) && string.toLowerCase().endsWith(KILOBYTE.getLetter())) {
                int limitValue = parseInt(string.substring(0, string.length() - 1));
                return new Limit(limitValue, KILOBYTE);
            }
            if (string.matches(LIMIT_PATTERN) && string.toLowerCase().endsWith(MEGABYTE.getLetter())) {
                int limitValue = parseInt(string.substring(0, string.length() - 1));
                return new Limit(limitValue, MEGABYTE);
            }
            throw new LimitException(format("String %s can't be parsed!", string));
        }
    }

    private Limit getDefaultLimit() {
        return new Limit(DEFAULT_LIMIT_VALUE, DEFAULT_LIMIT_UNIT);
    }
}
