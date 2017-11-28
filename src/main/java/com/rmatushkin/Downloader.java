package com.rmatushkin;

import com.rmatushkin.entity.SingleFile;
import com.rmatushkin.service.ProgramArgumentService;

import java.util.List;
import java.util.Map;

public class Downloader {
    private static final String[] PARAMETERS = {"n", "l", "f", "o"};
    private List<SingleFile> singleFiles;

    public Downloader(String[] args) {
        init(args);
    }

    public void start() {
        downloadFiles();
    }

    private void downloadFiles() {

    }

    private void init(String[] args) {
        ProgramArgumentService programArgumentService = new ProgramArgumentService();
        Map<String, String> valuesByParameters = programArgumentService.parse(args, PARAMETERS);


    }
}
