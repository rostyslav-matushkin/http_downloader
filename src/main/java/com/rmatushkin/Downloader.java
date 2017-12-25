package com.rmatushkin;

import com.rmatushkin.entity.SingleFile;
import com.rmatushkin.http.HttpClient;
import com.rmatushkin.http.HttpClient.HttpClientBuilder;
import com.rmatushkin.http.Limit;
import com.rmatushkin.util.ProgramArgumentUtil;

import java.util.List;
import java.util.Map;

import static com.rmatushkin.http.Limit.parseLimit;
import static com.rmatushkin.util.FileUtil.createDirectory;
import static com.rmatushkin.util.FileUtil.newSingleFiles;
import static com.rmatushkin.util.TimeUtil.formatMilliseconds;
import static java.lang.Integer.parseInt;
import static java.lang.String.format;
import static java.lang.System.currentTimeMillis;

public class Downloader {
    private static final String[] PARAMETERS = {"n", "l", "f", "o"};
    private Map<String, String> parametersToValues;
    private List<SingleFile> singleFiles;

    public Downloader(String[] args) {
        init(args);
    }

    public void start() {
        long startTime = currentTimeMillis();

        int threadsQuantity = 0;
        String threadsQuantityValue = parametersToValues.get(PARAMETERS[0]);
        if (threadsQuantityValue != null) {
            threadsQuantity = parseInt(threadsQuantityValue);
        }
        Limit limit = parseLimit(parametersToValues.get(PARAMETERS[1]));

        HttpClient httpClient = new HttpClientBuilder()
                .setThreadsQuantity(threadsQuantity)
                .setLimit(limit)
                .build();

        httpClient.download(singleFiles);

        long finishTime = currentTimeMillis();

        System.out.println(format("Spent time: %s", formatMilliseconds((finishTime - startTime))));
    }

    private void init(String[] args) {
        parametersToValues = ProgramArgumentUtil.parse(args, PARAMETERS);

        String sourceFilePath = parametersToValues.get(PARAMETERS[2]);
        singleFiles = newSingleFiles(sourceFilePath);

        String destinationDirectoryPath = parametersToValues.get(PARAMETERS[3]);
        createDirectory(destinationDirectoryPath);
        for (SingleFile singleFile : singleFiles) {
            singleFile.setDirectoryPath(destinationDirectoryPath);
        }
    }
}
