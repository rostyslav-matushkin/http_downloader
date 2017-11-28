package com.rmatushkin.http;

import com.rmatushkin.entity.SingleFile;
import com.rmatushkin.enums.Unit;
import com.rmatushkin.exception.HttpClientException;
import com.rmatushkin.exception.LimitParseException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

import static com.rmatushkin.constraint.RegexPattern.LIMIT_REGEX;
import static com.rmatushkin.enums.Unit.KILOBYTE;
import static com.rmatushkin.enums.Unit.MEGABYTE;
import static java.lang.Integer.parseInt;
import static java.lang.String.format;
import static java.util.concurrent.CompletableFuture.runAsync;
import static java.util.concurrent.Executors.newWorkStealingPool;

public class HttpClient {
    private static volatile AtomicInteger ATOMIC_INTEGER = new AtomicInteger();
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
        List<Callable<Object>> tasks = new ArrayList<>(singleFiles.size());

        for (SingleFile singleFile : singleFiles) {
            tasks.add(() -> {
                URL url = singleFile.getUrl();
                String fileName = singleFile.getFileName();
                File destinationDirectory = singleFile.getDestinationDirectory();
                String destinationFilePath = destinationDirectory + "\\" + fileName;

                try {
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    FileOutputStream fileOutputStream = new FileOutputStream(destinationFilePath);

                    byte[] buffer = new byte[BUFFER_SIZE];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        fileOutputStream.write(buffer, 0, bytesRead);
                    }
                    fileOutputStream.close();
                    inputStream.close();

                    ATOMIC_INTEGER.incrementAndGet();
                } catch (IOException e) {
                    throw new HttpClientException(e.getMessage());
                }

                return null;
            });
        }

        runTasks(tasks);
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

    private void runTasks(List<Callable<Object>> tasks) {
        runAsync(() -> {
            float currentPercent = 0;
            float tempPercent = 0;
            System.out.println((int) currentPercent + "%");
            while ((currentPercent = ATOMIC_INTEGER.floatValue() * 100 / tasks.size()) < 100) {
                if (tempPercent != currentPercent) {
                    System.out.println((int) currentPercent + "%");
                    tempPercent = currentPercent;
                }
            }
            System.out.println(100 + "%");
        });

        ExecutorService executorService = newWorkStealingPool();
        try {
            executorService.invokeAll(tasks);
        } catch (InterruptedException e) {
            throw new HttpClientException(e.getMessage());
        }
        executorService.shutdown();
    }
}
