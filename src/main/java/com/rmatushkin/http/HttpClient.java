package com.rmatushkin.http;

import com.rmatushkin.enums.Unit;
import com.rmatushkin.exception.HttpClientException;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

import static com.rmatushkin.enums.Unit.MEGABYTE;

public class HttpClient {
    private static final int DEFAULT_LIMIT_VALUE = 10;
    private static final Unit DEFAULT_LIMIT_UNIT = MEGABYTE;
    private Limit limit;

    public HttpClient() {
        limit = getDefaultLimit();
    }

    private void download(String url, String destinationFile) {
        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(new URL(url).openStream());
             FileOutputStream fileOutputStream = new FileOutputStream(destinationFile)) {
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

    public void enableLimit(int value, Unit unit) {
        limit = new Limit(value, unit);
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
    }

    private Limit getDefaultLimit() {
        return new Limit(DEFAULT_LIMIT_VALUE, DEFAULT_LIMIT_UNIT);
    }
}
