package utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProcessingResult {

    public final String query;
    public List<QueryResult> result = new ArrayList<>();

    public ProcessingResult(String query) {
        this.query = query;
    }

    @Override
    public String toString() {
        return "Result for query \"" + query + "\":\n"
                + result.stream().map(
                        queryResult
                                -> "\t" + queryResult.searchEngineName + " -> " + queryResult.answer + "\n")
                .collect(Collectors.joining());
    }
}