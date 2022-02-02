package ru.natallem.redditstat.http;

import ru.natallem.redditstat.interfaces.IRequestReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.net.MalformedURLException;
import java.net.URL;

public class UrlReader implements IRequestReader {

    @Override
    public String readFrom(String sourceRequestQuery) {
        return readFromImpl(sourceRequestQuery, true);
    }

    private URL toUrl(String url) {
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Malformed url: " + url);
        }
    }

    private String readFromImpl(String sourceRequestQuery, boolean firstCall) {
        URL url = toUrl(sourceRequestQuery);
        try (BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()))) {
            StringBuilder buffer = new StringBuilder();
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                buffer.append(inputLine);
                buffer.append("\n");
            }
            return buffer.toString();
        } catch (IOException e) {
            if (firstCall){
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ex) {
                    throw new UncheckedIOException(e);
                }
                return readFromImpl(sourceRequestQuery, false);
            }
            throw new UncheckedIOException(e);
        }
    }
}
