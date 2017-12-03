package com.rmatushkin.http;

import com.rmatushkin.entity.SingleFile;
import com.rmatushkin.exception.HttpClientException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

import static com.rmatushkin.util.ThreadUtil.createExecutorService;
import static java.lang.String.format;
import static java.lang.System.currentTimeMillis;
import static java.lang.Thread.sleep;

public class HttpClient {
    private static final int BYTES_BUFFER_SIZE = 1024;
    private int threadsQuantity;
    private Limit limit;

    public HttpClient() {
    }

    public HttpClient(int threadsQuantity) {
        validateThreadsQuantity(threadsQuantity);
        this.threadsQuantity = threadsQuantity;
    }

    public void download(List<SingleFile> singleFiles) {
        List<Callable<Void>> tasks = new ArrayList<>();

        for (SingleFile singleFile : singleFiles) {
            tasks.add(createTask(singleFile));
        }

        runTasks(tasks);
    }

    public void setLimit(Limit limit) {
        this.limit = limit;
    }

    private Callable<Void> createTask(SingleFile singleFile) {
        return () -> {
            try (InputStream inputStream = singleFile.openInputStream();
                 OutputStream outputStream = singleFile.openOutputStream()) {
                if (limit == null) {
                    readWriteWithoutLimit(inputStream, outputStream);
                } else {
                    readWriteWithLimit(inputStream, outputStream);
                }
            } catch (FileNotFoundException e) {
                System.err.println(format("File '%s' not found!", e.getMessage()));
                return null;
            } catch (IOException e) {
                System.err.println(e.getMessage());
                throw new HttpClientException(e);
            }
            return null;
        };
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
        ExecutorService executorService = createExecutorService(threadsQuantity);
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
