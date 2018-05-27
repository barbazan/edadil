package com.barbazan.edadil.utils.http;

import sun.misc.BASE64Encoder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtil {

    public static String getContent(String path) throws IOException {
        BufferedReader reader = null;
        try {
            String userPassword = "admin" + ":" + "admin123";
            BASE64Encoder encoder = new BASE64Encoder();
            String encoded = encoder.encode(userPassword.getBytes());
            URL url = new URL(path);
            HttpURLConnection c = (HttpURLConnection) url.openConnection();
            c.setRequestProperty("Authorization", "Basic " + encoded);
            c.setRequestMethod("POST");
            c.setReadTimeout(10000);
            c.connect();
            reader = new BufferedReader(new InputStreamReader(c.getInputStream()));
            StringBuilder buf = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                buf.append(line).append("\n");
            }
            return (buf.toString().replaceAll("&quot;", "\""));
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

}
