package server;

import scala.concurrent.duration.Duration;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class StubQueryServiceCommunicator implements QueryServiceCommunicator {
    public static class SearchEngineSettings {
        public long sleepTimeout;
        public String searchEngineName;
        public List<String> results;

        public SearchEngineSettings(long sleepTimeout, String searchEngineName, List<String> results) {
            this.sleepTimeout = sleepTimeout;
            this.searchEngineName = searchEngineName;
            this.results = results;
        }
    }

    private final List<SearchEngineSettings> searchEngineSettings;

    public StubQueryServiceCommunicator(List<SearchEngineSettings> searchEngineSettings) {
        this.searchEngineSettings = searchEngineSettings;
    }

    @Override
    public List<String> getQueryResult(String searchEngine, String query) throws InterruptedException {
        for (SearchEngineSettings settings : searchEngineSettings) {
            if (!settings.searchEngineName.equals(searchEngine)) {
                continue;
            }
            if (settings.sleepTimeout > 0) {
                Thread.sleep(Duration.apply(settings.sleepTimeout, TimeUnit.SECONDS).toMillis());
            }
            return settings.results.stream().map(x -> query + " " + x).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
