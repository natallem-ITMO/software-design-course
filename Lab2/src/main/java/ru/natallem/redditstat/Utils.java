package ru.natallem.redditstat;

import ru.natallem.redditstat.http.UrlReader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Utils {
    public static String getJsonFromFileForRequest(String tag, int hour) {
        Path resourseFile = Paths.get("src", "test", "resources", tag, hour + "h", "result.json");
        try {
            List<String> strings = Files.readAllLines(resourseFile);
            return String.join("\n", strings);
        } catch (IOException e) {
            return "";
        }
    }

    public static void writeToResources() throws IOException {
        String tag = "mock";
        int hourAfter = 24;
        UrlReader reader = new UrlReader();
        for (int i = 1; i <= hourAfter; ++i) {
            String url = "https://api.pushshift.io/reddit/search/submission/?size=500&q=" + tag + "&after=" + i + "h";
            if (i != 1) {
                url += "&before=" + (i - 1) + "h";
            }
            String fileName = "A:\\ITMO\\Software Disign\\hw2\\RedditStat\\src\\test\\resources\\" + tag + "\\" + i + "h\\result.json";
            File file = new File(fileName);
            File parent = file.getParentFile();
            if (parent != null && !parent.exists() && !parent.mkdirs()) {
                throw new IllegalStateException("Couldn't create dir: " + parent);
            }
            String res = reader.readFrom(url);
            BufferedWriter writer = new BufferedWriter(new FileWriter(file, false));
            writer.append(res);
            writer.close();
        }
    }
}
