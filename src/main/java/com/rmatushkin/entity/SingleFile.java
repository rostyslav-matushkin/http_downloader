package com.rmatushkin.entity;

public class SingleFile {
    private String url;
    private String fileName;
    private String directoryPath;

    public String getDestinationFilePath() {
        if (fileName != null && directoryPath != null) {
            return directoryPath + "\\" + fileName;
        }
        return null;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDirectoryPath() {
        return directoryPath;
    }

    public void setDirectoryPath(String directoryPath) {
        this.directoryPath = directoryPath;
    }
}
