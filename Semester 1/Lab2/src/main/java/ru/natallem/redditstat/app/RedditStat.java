package ru.natallem.redditstat.app;

import ru.natallem.redditstat.interfaces.IResultParser;
import ru.natallem.redditstat.interfaces.ISocialNetworkStatistic;
import ru.natallem.redditstat.interfaces.IRequestQueryConstructor;
import ru.natallem.redditstat.interfaces.IRequestReader;

import java.util.ArrayList;
import java.util.List;

public class RedditStat implements ISocialNetworkStatistic {

    private final IRequestQueryConstructor requestQueryConstructor;
    private final IRequestReader requestReader;
    private final IResultParser parser;

    public RedditStat(IRequestQueryConstructor requestQueryConstructor, IRequestReader requestReader, IResultParser parser) {
        this.requestQueryConstructor = requestQueryConstructor;
        this.parser = parser;
        this.requestReader = requestReader;
    }

    @Override
    public List<Integer> getStatisticByHours(String tag, int hours) {
        List<Integer> resultArray = new ArrayList<>();
        for (int i = 1; i <= hours; i++) {
            String query = requestQueryConstructor.constructQuery(tag, i);
            String result = requestReader.readFrom(query);
            resultArray.add(parser.parseResult(result));
        }
        return resultArray;
    }
}
