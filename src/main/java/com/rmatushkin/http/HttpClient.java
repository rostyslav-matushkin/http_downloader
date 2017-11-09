package com.rmatushkin.http;

import com.rmatushkin.exception.HttpClientException;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;

import static com.rmatushkin.enums.Unit.MEGABYTE;
import static java.net.Proxy.Type.HTTP;

public class HttpClient {

    public void downloadByUrl(String url, String destinationFilePath) {
        Proxy proxy = new Proxy(HTTP, new InetSocketAddress("195.230.131.201", 3128));
        try (BufferedInputStream bufferedInputStream =
                     new BufferedInputStream(new URL(url).openConnection(proxy).getInputStream());
             FileOutputStream fileOutputStream = new FileOutputStream(destinationFilePath)) {
            byte[] buffer = new byte[MEGABYTE.getBytes()];
            int count;
            while ((count = bufferedInputStream.read(buffer, 0, MEGABYTE.getBytes())) != -1) {
                fileOutputStream.write(buffer, 0, count);
            }
        } catch (IOException e) {
            throw new HttpClientException(e.getMessage());
        }
    }
}
