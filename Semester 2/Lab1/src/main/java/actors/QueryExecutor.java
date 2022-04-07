package actors;

import akka.actor.UntypedActor;
import scala.Option;
import server.QueryServiceCommunicator;
import utils.ProcessingResult;
import utils.QueryResult;

import java.util.List;
import java.util.stream.Collectors;

public class QueryExecutor extends UntypedActor {
    final private String searchEngineName;
    private QueryServiceCommunicator service;

    public QueryExecutor(String searchEngineName, QueryServiceCommunicator service) {
        this.searchEngineName = searchEngineName;
        this.service = service;
    }

    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof String) {
            List<QueryResult> answer = service.getQueryResult(searchEngineName, (String) message).stream()
                    .map(x -> new QueryResult(x, searchEngineName)).collect(Collectors.toList());
            ProcessingResult processingResult = new ProcessingResult((String) message);
            processingResult.result = answer;
            context().parent().tell(processingResult, getSelf());
        }
    }

}

