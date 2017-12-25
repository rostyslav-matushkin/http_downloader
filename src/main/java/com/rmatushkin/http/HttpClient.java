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
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import static com.rmatushkin.util.ThreadUtil.newExecutorService;
import static java.lang.String.format;
import static java.lang.System.currentTimeMillis;
import static java.lang.Thread.sleep;

public class HttpClient {
    private static final int BYTES_BUFFER_SIZE = 1024;
    private static final Object LOCK = new Object();
    private static final AtomicLong TOTAL_BYTES = new AtomicLong();
    private static final AtomicInteger BYTES_COUNT = new AtomicInteger();
    private static volatile long checkTime = currentTimeMillis() + 1000;
    private int threadsQuantity;
    private Limit limit;

    private HttpClient(int threadsQuantity, Limit limit) {
        this.threadsQuantity = threadsQuantity;
        this.limit = limit;
    }

    public void download(List<SingleFile> singleFiles) {
        List<Callable<Void>> tasks = new ArrayList<>();
        for (SingleFile singleFile : singleFiles) {
            tasks.add(createTask(singleFile));
        }
        runTasks(tasks);

        System.out.println(format("Downloaded %s bytes", TOTAL_BYTES.get()));
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
        int readBytes;
        while ((readBytes = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, readBytes);
            TOTAL_BYTES.addAndGet(readBytes);
        }
    }

    private void readWriteWithLimit(InputStream inputStream, OutputStream outputStream) throws IOException {
        int bytesPerSecond = limit.getUnit().getBytes() * limit.getValue();
        byte[] buffer = new byte[BYTES_BUFFER_SIZE];
        int readBytes;
        while (true) {
            synchronized (LOCK) {
                readBytes = inputStream.read(buffer);
                if (readBytes == -1) {
                    break;
                }
                outputStream.write(buffer, 0, readBytes);
                if (BYTES_COUNT.addAndGet(readBytes) < bytesPerSecond) {
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
                BYTES_COUNT.set(0);
                checkTime = currentTimeMillis() + 1000;
            }
        }
    }

    private <T> void runTasks(List<Callable<T>> tasks) {
        ExecutorService executorService = newExecutorService(threadsQuantity);
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

    public static class HttpClientBuilder {
        private int threadsQuantity;
        private Limit limit;

        public HttpClient build() {
            return new HttpClient(threadsQuantity, limit);
        }

        public HttpClientBuilder setThreadsQuantity(int threadsQuantity) {
            validateThreadsQuantity(threadsQuantity);
            this.threadsQuantity = threadsQuantity;
            return this;

        }

        public HttpClientBuilder setLimit(Limit limit) {
            this.limit = limit;
            return this;
        }

        private void validateThreadsQuantity(int threadsQuantity) {
            if (threadsQuantity < 0) {
                throw new HttpClientException("Threads quantity can't be less zero!");
            }
        }
    }
}
