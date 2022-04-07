package ru.natallem.redditstat.parsing;

import ru.natallem.redditstat.interfaces.IResultParser;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class RedditResultParser implements IResultParser {
    @Override
    public int parseResult(String result) {
        JsonObject jsonObject = new JsonParser().parse(result).getAsJsonObject();
        return jsonObject.get("data").getAsJsonArray().size();
    }
}
