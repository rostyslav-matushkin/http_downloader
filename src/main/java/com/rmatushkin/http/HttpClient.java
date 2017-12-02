package com.rmatushkin.http;

import com.rmatushkin.entity.SingleFile;
import com.rmatushkin.exception.HttpClientException;
import com.rmatushkin.service.FileService;

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

import static java.util.concurrent.CompletableFuture.runAsync;
import static java.util.concurrent.Executors.newFixedThreadPool;
import static java.util.concurrent.Executors.newWorkStealingPool;

public class HttpClient {
    private static final int BUFFER_SIZE = 4096;
    private FileService fileService;
    private AtomicInteger atomicInteger;
    private int threadsQuantity;
    private Limit limit;

    public HttpClient() {
        fileService = new FileService();
        atomicInteger = new AtomicInteger();
    }

    public HttpClient(int threadsQuantity) {
        fileService = new FileService();
        validateThreadsQuantity(threadsQuantity);
        this.threadsQuantity = threadsQuantity;
        atomicInteger = new AtomicInteger();
    }

    public void download(List<SingleFile> singleFiles) {
        List<Callable<Object>> tasks = new ArrayList<>();

        for (SingleFile singleFile : singleFiles) {
            tasks.add(createTask(singleFile));
        }

        runPercentCalculation(tasks.size());
        runTasks(tasks);
    }

    public void setLimit(Limit limit) {
        this.limit = limit;
    }

    private Callable<Object> createTask(SingleFile singleFile) {
        return () -> {
            URL url = new URL(singleFile.getUrl());
            fileService.createDirectory(singleFile.getDirectoryPath());
            String destinationFilePath = singleFile.getDestinationFilePath();

            try {
                InputStream inputStream = buildInputStream(url);
                FileOutputStream fileOutputStream = new FileOutputStream(destinationFilePath);

                byte[] buffer = new byte[BUFFER_SIZE];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    fileOutputStream.write(buffer, 0, bytesRead);
                }
                fileOutputStream.close();
                inputStream.close();

                atomicInteger.incrementAndGet();
            } catch (IOException e) {
                System.err.println(e.getMessage());
                throw new HttpClientException();
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
            throw new HttpClientException();
        }
    }

    private void runTasks(List<Callable<Object>> tasks) {
        ExecutorService executorService;
        if (threadsQuantity == 0) {
            executorService = newWorkStealingPool();
        } else {
            executorService = newFixedThreadPool(threadsQuantity);
        }

        try {
            executorService.invokeAll(tasks);
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
            throw new HttpClientException();
        }
        executorService.shutdown();
    }

    private void runPercentCalculation(int taskSize) {
        runAsync(() -> {
            int currentPercent = 0;
            int tempPercent = 0;
            System.out.println(currentPercent + "%");
            while ((currentPercent = atomicInteger.get() * 100 / taskSize) <= 100) {
                if (tempPercent != currentPercent) {
                    System.out.println(currentPercent + "%");
                    tempPercent = currentPercent;
                }
            }
        });
    }

    private void validateThreadsQuantity(int threadsQuantity) {
        if (threadsQuantity <= 0) {
            System.err.println("Threads quantity can't be less or equal ZERO!");
            throw new HttpClientException();
        }
    }
}
