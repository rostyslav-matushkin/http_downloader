package com.rmatushkin.entity;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SingleFile {
    private String url;
    private String fileName;
    private String directoryPath;

    public InputStream openInputStream() throws IOException {
        HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(url).openConnection();
        return httpURLConnection.getInputStream();
    }

    public OutputStream openOutputStream() throws IOException {
        return new FileOutputStream(directoryPath + "\\" + fileName);
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setDirectoryPath(String directoryPath) {
        this.directoryPath = directoryPath;
    }
}
