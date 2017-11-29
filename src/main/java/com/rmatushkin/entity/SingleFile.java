package com.rmatushkin.entity;

import java.io.File;
import java.net.URL;

public class SingleFile {
    private URL url;
    private String fileName;
    private File destinationDirectory;

    public String getDestinationFilePath() {
        if (fileName != null && destinationDirectory != null) {
            return destinationDirectory.getPath() + "\\" + fileName;
        }
        return null;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setDestinationDirectory(File destinationDirectory) {
        this.destinationDirectory = destinationDirectory;
    }
}
