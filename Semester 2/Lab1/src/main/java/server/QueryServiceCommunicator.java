package server;

import java.util.List;

public interface QueryServiceCommunicator {
    List<String> getQueryResult(String searchEngine, String query) throws InterruptedException;
}
