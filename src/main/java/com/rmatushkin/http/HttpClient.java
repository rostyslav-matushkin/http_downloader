package com.rmatushkin.http;

import com.rmatushkin.enums.Unit;
import com.rmatushkin.exception.HttpClientException;
import com.rmatushkin.exception.LimitParseException;

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
    private Limit limit;
    private boolean enabledLimit;

    public HttpClient() {
    }

    public HttpClient(Limit limit) {
        this.limit = limit;
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
            System.err.println(e.getMessage());
            throw new HttpClientException(e.getMessage());
        }
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
            if (string.matches(LIMIT_PATTERN) && string.toLowerCase().endsWith(KILOBYTE.getLetter())) {
                int limitValue = parseInt(string.substring(0, string.length() - 1));
                return new Limit(limitValue, KILOBYTE);
            }
            if (string.matches(LIMIT_PATTERN) && string.toLowerCase().endsWith(MEGABYTE.getLetter())) {
                int limitValue = parseInt(string.substring(0, string.length() - 1));
                return new Limit(limitValue, MEGABYTE);
            }
            throw new LimitParseException(format("String %s can't be parsed!", string));
        }
    }
}
