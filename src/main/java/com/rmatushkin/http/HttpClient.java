package com.rmatushkin.http;

import com.rmatushkin.exception.HttpClientException;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

public class HttpClient {

    public void downloadByUrl(String url, String destinationFilePath, File destinationDirectory) {
        String destinationPath = destinationDirectory.getPath() + "\\" + destinationFilePath;
        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(new URL(url).openStream());
             FileOutputStream fileOutputStream = new FileOutputStream(destinationPath)) {
            byte[] buffer = new byte[1024];
            int count;
            while ((count = bufferedInputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, count);
            }
        } catch (IOException e) {
            throw new HttpClientException(e.getMessage());
        }
    }
}
