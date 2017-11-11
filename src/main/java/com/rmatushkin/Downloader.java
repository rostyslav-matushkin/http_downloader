package com.rmatushkin;

import com.rmatushkin.enums.Unit;
import com.rmatushkin.exception.DownloaderException;
import com.rmatushkin.http.HttpClient;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

import static com.rmatushkin.constraint.RegexPattern.URL_PATTERN;
import static com.rmatushkin.util.FileSystemUtil.createDirectory;
import static com.rmatushkin.util.FileSystemUtil.findFile;
import static com.rmatushkin.util.ProgramArgumentUtil.parse;
import static com.rmatushkin.util.StringUtil.removeExcessWhitespaces;
import static com.rmatushkin.util.StringUtil.removeUtf8Bom;
import static java.lang.Integer.parseInt;
import static java.lang.String.format;
import static java.util.concurrent.Executors.newFixedThreadPool;
import static java.util.concurrent.Executors.newWorkStealingPool;

public class Downloader {
    private static final String[] PARAMETERS = {"n", "l", "f", "o"};
    private String[] args;
    private Map<String, String> valuesByParameters;
    private int threadsQuantity;
    private Map<String, String> fileNamesByUrls;
    private File destinationDirectory;

    public Downloader(String[] args) {
        this.args = args;
        init();
    }

    public void start() {

    }

    private void init() {
        valuesByParameters = parse(args, PARAMETERS);
        String threadsQuantity = valuesByParameters.get(PARAMETERS[0]);
        if (threadsQuantity != null) {
            this.threadsQuantity = parseInt(valuesByParameters.get(PARAMETERS[0]));
        }
        String fileWithUrlsPath = valuesByParameters.get(PARAMETERS[2]);
        if (fileWithUrlsPath == null) {
            throw new DownloaderException(format("Parameter %s can't be null!", "-" + PARAMETERS[2]));
        }
        File fileWithUrls = findFile(fileWithUrlsPath);
        fileNamesByUrls = getFileNamesByUrls(fileWithUrls);
        String destinationDirectoryPath = valuesByParameters.get(PARAMETERS[3]);
        if (destinationDirectoryPath == null) {
            throw new DownloaderException(format("Parameter %s can't be null!", "-" + PARAMETERS[3]));
        }
        destinationDirectory = createDirectory(destinationDirectoryPath);
    }

    private Map<String, String> getFileNamesByUrls(File file) {
        Map<String, String> fileNamesByUrls = new HashMap<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            while (bufferedReader.ready()) {
                String readLine = bufferedReader.readLine();
                String processedLine = removeExcessWhitespaces(removeUtf8Bom(readLine));
                String[] subLines = processedLine.split(" ");
                String url = subLines[0];
                if (!url.matches(URL_PATTERN)) {
                    throw new DownloaderException("The line '%s' isn't URL!");
                }
                String fileName = subLines[1];
                fileNamesByUrls.put(fileName, url);
            }
            return fileNamesByUrls;
        } catch (IOException e) {
            throw new DownloaderException(e.getMessage());
        }
    }

    private void downloadFiles() {
        List<Callable<Object>> tasks = new ArrayList<>();
        HttpClient httpClient = new HttpClient();
        String limitValue = valuesByParameters.get(PARAMETERS[1]);
        if (limitValue != null) {

        }
        int limit = 0;
        for (Entry<String, String> pair : fileNamesByUrls.entrySet()) {
            tasks.add(() -> {
                String url = pair.getKey();
                String fileName = pair.getValue();
                httpClient.enableLimit();
                return null;
            });
        }
        runTasks(tasks);
    }



    private <T> void runTasks(List<Callable<T>> tasks) {
        ExecutorService executorService = newWorkStealingPool();
        if (threadsQuantity != 0)
        executorService = newFixedThreadPool(threadsQuantity);
        try {
            executorService.invokeAll(tasks);
        } catch (InterruptedException e) {
            throw new DownloaderException(e.getMessage());
        }
        executorService.shutdown();
    }
}
