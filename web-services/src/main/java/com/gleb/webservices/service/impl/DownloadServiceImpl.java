package com.gleb.webservices.service.impl;

import com.gleb.webservices.service.DownloadService;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by gleb on 27.10.15.
 */
public class DownloadServiceImpl implements DownloadService {

    @Override
    public void dowload(String urlAddress) throws IOException {
        URL url = new URL(urlAddress);
        URLConnection connection = url.openConnection();
        InputStream inputStream = connection.getInputStream();
        int next = 0;
        StringBuilder builder = new StringBuilder();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        while ((next = inputStream.read()) != -1) {
            char c = (char) next;
            builder.append(c);
            outputStream.write(next);
        }
        File file = new File("/media/gleb/10585F7C585F5F90/myistylz/web-services/NewFile/123.csv.gz");
        if(file.exists()) {
            file.delete();
        }
        file.createNewFile();
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(outputStream.toByteArray());
        fileOutputStream.flush();
    }

}
