package ru.natallem.redditstat.http;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.CoreMatchers.instanceOf;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

public class RedditUrlQueryConstructorTest {
    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void testConstructUrlWithZeroHoursArg() {
        exceptionRule.expect(IndexOutOfBoundsException.class);
        exceptionRule.expectMessage("Hours for url query should be in range [1..24]");
        RedditUrlQueryConstructor constructor = new RedditUrlQueryConstructor();
        constructor.constructQuery("sdf", 0);
    }

    @Test
    public void testConstructUrlWithNegativeHoursArg() {
        exceptionRule.expect(IndexOutOfBoundsException.class);
        exceptionRule.expectMessage("Hours for url query should be in range [1..24]");
        RedditUrlQueryConstructor constructor = new RedditUrlQueryConstructor();
        constructor.constructQuery("sdf", -6);
    }

    @Test
    public void testConstructUrlWithOverflowHoursArg() {
        exceptionRule.expect(IndexOutOfBoundsException.class);
        exceptionRule.expectMessage("Hours for url query should be in range [1..24]");
        RedditUrlQueryConstructor constructor = new RedditUrlQueryConstructor();
        constructor.constructQuery("sdf", 25);
    }

    @Test
    public void testIncorrectTags() {
        RedditUrlQueryConstructor constructor = new RedditUrlQueryConstructor();
        ArrayList<String> wrongTags = new ArrayList<>(
                Arrays.asList(
                        " first_space",
                        "spaces in the middle",
                        "last_space ",
                        "tag_with_incorrect_symbols$"));
        for (String query : wrongTags){
            try {
                constructor.constructQuery(query, 1);
                fail("Should throw an exception, because query string '" + query + "'  is not tag");
            } catch (Exception e) {
                assertThat(e, instanceOf(IllegalArgumentException.class));
                assertEquals("Tag string '" + query + "' doesn't match tag regex", e.getMessage());
            }
        }
    }

    @Test
    public void testCorrectTags() {
        RedditUrlQueryConstructor constructor = new RedditUrlQueryConstructor();
        ArrayList<String> tags = new ArrayList<>(
                Arrays.asList(
                        "",
                        "simple",
                        "_tag",
                        "tag_tag",
                        "tag_",
                        "1",
                        "111111111111111111111111111111111111111111111111111111111111111111111111111111111111"));
        for (String query : tags){
            assertEquals("https://api.pushshift.io/reddit/search/submission/?q="+ query + "&size=500&after=1h", constructor.constructQuery(query, 1));
        }
        for (int i = 2; i <= 24; i++){
            for (String query : tags){
                assertEquals("https://api.pushshift.io/reddit/search/submission/?q="+ query + "&size=500&after=" + i+ "h&before=" + (i-1) + "h", constructor.constructQuery(query, i));
            }
        }
    }
}