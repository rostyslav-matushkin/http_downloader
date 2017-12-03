package com.rmatushkin.http;

import com.rmatushkin.entity.SingleFile;
import com.rmatushkin.exception.HttpClientException;
import com.rmatushkin.service.FileService;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

import static java.lang.System.currentTimeMillis;
import static java.lang.Thread.sleep;
import static java.util.concurrent.Executors.newFixedThreadPool;
import static java.util.concurrent.Executors.newWorkStealingPool;

public class HttpClient {
    private static final int BYTES_BUFFER_SIZE = 1024;
    private FileService fileService;
    private int threadsQuantity;
    private Limit limit;

    public HttpClient() {
        fileService = new FileService();
    }

    public HttpClient(int threadsQuantity) {
        fileService = new FileService();
        validateThreadsQuantity(threadsQuantity);
        this.threadsQuantity = threadsQuantity;
    }

    public void download(List<SingleFile> singleFiles) {
        List<Callable<Object>> tasks = new ArrayList<>();

        for (SingleFile singleFile : singleFiles) {
            tasks.add(createTask(singleFile));
        }

        runTasks(tasks);
    }

    public void setLimit(Limit limit) {
        this.limit = limit;
    }

    private <T> Callable<T> createTask(SingleFile singleFile) {
        return () -> {
            URL url = new URL(singleFile.getUrl());
            fileService.createDirectory(singleFile.getDirectoryPath());
            String destinationFilePath = singleFile.getDestinationFilePath();

            try {
                InputStream inputStream = buildInputStream(url);
                OutputStream outputStream = new FileOutputStream(destinationFilePath);

                if (limit == null) {
                    readWriteWithoutLimit(inputStream, outputStream);
                } else {
                    readWriteWithLimit(inputStream, outputStream);
                }

                outputStream.close();
                inputStream.close();
            } catch (IOException e) {
                System.err.println(e.getMessage());
                throw new HttpClientException(e);
            }

            return null;
        };
    }

    private InputStream buildInputStream(URL url) {
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            return httpURLConnection.getInputStream();
        } catch (IOException e) {
            System.err.println(e.getMessage());
            throw new HttpClientException(e);
        }
    }

    private void readWriteWithoutLimit(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] buffer = new byte[BYTES_BUFFER_SIZE];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
    }

    private void readWriteWithLimit(InputStream inputStream, OutputStream outputStream) throws IOException {
        int bytesPerSecond = limit.getUnit().getBytes() * limit.getValue();
        byte[] buffer = new byte[BYTES_BUFFER_SIZE];
        int bytesCounter = 0;
        long checkTime = currentTimeMillis() + 1000;
        int bytesRead;
        while (true) {
            bytesRead = inputStream.read(buffer);
            if (bytesRead == -1) {
                break;
            }
            outputStream.write(buffer, 0, bytesRead);
            bytesCounter += bytesRead;
            if (bytesCounter < bytesPerSecond) {
                continue;
            }
            try {
                long sleepTime = checkTime - currentTimeMillis();
                if (sleepTime > 0) {
                    sleep(sleepTime);
                }
            } catch (InterruptedException e) {
                System.err.println(e.getMessage());
                throw new HttpClientException(e);
            }
            bytesCounter = 0;
            checkTime = currentTimeMillis() + 1000;
        }
    }

    private <T> void runTasks(List<Callable<T>> tasks) {
        ExecutorService executorService;
        if (threadsQuantity == 0) {
            executorService = newWorkStealingPool();
        } else {
            executorService = newFixedThreadPool(threadsQuantity);
        }

        try {
            System.out.println("In progress...");
            executorService.invokeAll(tasks);
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
            throw new HttpClientException(e);
        }
        executorService.shutdown();
        System.out.println("Done!");
    }

    private void validateThreadsQuantity(int threadsQuantity) {
        if (threadsQuantity <= 0) {
            throw new HttpClientException("Threads quantity can't be less or equal ZERO!");
        }
    }
}
