package com.rmatushkin.entity;

import java.io.File;
import java.net.URL;

public class SingleFile {
    private URL url;
    private String fileName;
    private File destinationDirectory;

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public File getDestinationDirectory() {
        return destinationDirectory;
    }

    public void setDestinationDirectory(File destinationDirectory) {
        this.destinationDirectory = destinationDirectory;
    }
}
