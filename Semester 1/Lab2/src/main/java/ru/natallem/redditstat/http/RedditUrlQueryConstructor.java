package ru.natallem.redditstat.http;

import ru.natallem.redditstat.interfaces.IRequestQueryConstructor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RedditUrlQueryConstructor implements IRequestQueryConstructor {

    private static final String apiHost = "https://api.pushshift.io/";
    private static final String endpoint = "reddit/search/submission/";
    private static final String size_limit = "&size=500";
    private final Pattern tag_pattern = Pattern.compile("^[\\pL\\pN_]*$");

    @Override
    public String constructQuery(String tag, int hoursAgo) {
        if (hoursAgo < 1 || hoursAgo > 24) {
            throw new IndexOutOfBoundsException("Hours for url query should be in range [1..24]");
        }
        Matcher m = tag_pattern.matcher(tag);
        if (!m.find()) {
            throw new IllegalArgumentException("Tag string '" + tag + "' doesn't match tag regex");
        }
        StringBuilder builder = new StringBuilder(apiHost + endpoint + "?q=" + tag + size_limit + "&after=" + hoursAgo + "h");
        if (hoursAgo != 1) {
            builder.append("&before=").append(hoursAgo - 1).append("h");
        }
        return builder.toString();
    }
}
