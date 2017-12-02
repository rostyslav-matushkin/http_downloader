package com.rmatushkin.entity;

public class SingleFile {
    private String url;
    private String fileName;
    private String destinationDirectoryPath;

    public String getResultPath() {
        if (fileName != null && destinationDirectoryPath != null) {
            return destinationDirectoryPath + "\\" + fileName;
        }
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

    public String getDestinationDirectoryPath() {
        return destinationDirectoryPath;
    }

    public void setDestinationDirectoryPath(String destinationDirectoryPath) {
        this.destinationDirectoryPath = destinationDirectoryPath;
    }
}
