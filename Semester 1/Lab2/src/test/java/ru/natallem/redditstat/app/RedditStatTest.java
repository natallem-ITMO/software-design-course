package ru.natallem.redditstat.app;

import com.xebialabs.restito.server.StubServer;
import org.glassfish.grizzly.http.Method;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.natallem.redditstat.http.RedditUrlQueryConstructor;
import ru.natallem.redditstat.http.UrlReader;
import ru.natallem.redditstat.parsing.RedditResultParser;
import ru.natallem.redditstat.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import static com.xebialabs.restito.semantics.Condition.endsWithUri;
import static com.xebialabs.restito.builder.stub.StubHttp.whenHttp;
import static com.xebialabs.restito.semantics.Action.stringContent;
import static com.xebialabs.restito.semantics.Condition.method;
import static org.mockito.Mockito.when;

public class RedditStatTest {
    private static final int PORT = 32453;

    @Mock
    private RedditUrlQueryConstructor mockQueryConstructor;

    private RedditStat testRedditStat;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        testRedditStat = new RedditStat(mockQueryConstructor, new UrlReader(), new RedditResultParser());
    }

    @Test
    public void testHttpsGetStatisticByHours() {
        RedditStat redditStat = new RedditStat(new RedditUrlQueryConstructor(), new UrlReader(), new RedditResultParser());
        for (int h = 1; h <= 24; h += 4) {
            System.out.println("Checking request for " + h + " hours");
            List<Integer> stats = redditStat.getStatisticByHours("mock", h);
            for (int i = 0; i < h; i++) {
                Assert.assertTrue(stats.get(i) > 0);
            }
        }
    }

    @Test
    public void testRedditStat() {
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
                when(mockQueryConstructor.constructQuery(tag, i))
                        .thenReturn(createMockQuery(tag, i));
            }
            int finalTestNum = testNum;
            withStubServer(s -> {
                for (int i = 1; i <= hours; ++i) {
                    whenHttp(s)
                            .match(method(Method.GET), endsWithUri("/" + tag + "/" + i + "h"))
                            .then(stringContent(Utils.getJsonFromFileForRequest(tag, i)));
                }
                List<Integer> statisticByHours = testRedditStat.getStatisticByHours(tag, hours);
                Assert.assertEquals(testExpectedResult.get(finalTestNum), statisticByHours);
            });
        }
    }

    private String createMockQuery(String tag, int i) {
        return "http://localhost:" + PORT + "/reddit/" + tag + "/" + i + "h";
    }

    private void withStubServer(Consumer<StubServer> callback) {
        StubServer stubServer = null;
        try {
            stubServer = new StubServer(RedditStatTest.PORT).run();
            callback.accept(stubServer);
        } finally {
            if (stubServer != null) {
                stubServer.stop();
            }
        }
    }
}