package ru.natallem.redditstat.parsing;

import org.junit.Assert;
import org.junit.Test;
import ru.natallem.redditstat.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class RedditResultParserTest {

    @Test
    public void testCorrectJsonParsing() {
        RedditResultParser parser = new RedditResultParser();
        ArrayList<String> testRequests = new ArrayList<>(Arrays.asList("flowers", "mock", "trump"));
        ArrayList<Integer> testRequestSizes = new ArrayList<>(Arrays.asList(8, 24, 1));
        ArrayList<ArrayList<Integer>> testExpectedResult = new ArrayList<>(Arrays.asList(
                new ArrayList<>(Arrays.asList(19, 20, 27, 19, 11, 14, 17, 26)),
                new ArrayList<>(Arrays.asList(5, 12, 8, 3, 3, 6, 5, 9, 4, 5, 5, 9, 2, 7, 7, 7, 9, 8, 5, 6, 10, 7, 6, 5)),
                new ArrayList<>(Collections.singletonList(47))
        ));
        for (int testNum = 0; testNum < testRequests.size(); ++testNum) {
            String tag = testRequests.get(testNum);
            int hours = testRequestSizes.get(testNum);
            for (int i = 1; i <= hours; ++i) {
                Assert.assertEquals("compare tag=" + tag + " i=" + i, (int) testExpectedResult.get(testNum).get(i - 1),
                        parser.parseResult(Utils.getJsonFromFileForRequest(tag, i)));
            }
        }
    }

    @Test(expected = NullPointerException.class)
    public void testIncorrectJsonParsing() {
        RedditResultParser parser = new RedditResultParser();

        parser.parseResult(incorrectJsonString);
    }

    private final String incorrectJsonString = "{\n" +
            "    \"dat\": [\n" +
            "        {\n" +
            "            \"all_awardings\": [],\n" +
            "            \"allow_live_comments\": false,\n" +
            "            \"author\": \"-Rustling-Jimmies-\",\n" +
            "            \"treatment_tags\": [],\n" +
            "            \"upvote_ratio\": 1.0,\n" +
            "            \"url\": \"https://i.redd.it/7x5hjmf9bev71.jpg\",\n" +
            "            \"url_overridden_by_dest\": \"https://i.redd.it/7x5hjmf9bev71.jpg\",\n" +
            "            \"whitelist_status\": \"all_ads\",\n" +
            "            \"wls\": 6\n" +
            "        }\n" +
            "    ]\n" +
            "}";
}