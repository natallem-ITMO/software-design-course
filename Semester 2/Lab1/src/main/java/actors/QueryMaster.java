package actors;

import akka.actor.*;
import scala.Option;
import server.QueryServiceCommunicator;
import utils.ProcessingResult;

import java.util.ArrayList;
import java.util.List;


public class QueryMaster extends UntypedActor {
    ProcessingResult result;
    private final ActorRef callback;

    private final static List<String> ALL_SEARCH_ENGINES = List.of("yandex", "google", "bing");
    private final static int ANSWER_NUMBER = 5;

    public QueryMaster(QueryServiceCommunicator service, String query, ActorRef callback) {
        this.result = new ProcessingResult(query);
        this.callback = callback;

        for (String searchEngine : ALL_SEARCH_ENGINES) {
            ActorRef engineActor = getContext().actorOf(Props.create(QueryExecutor.class, searchEngine, service), searchEngine + "_searcher");
            engineActor.tell(query, getSelf());
        }
        ActorRef timerActor = getContext().actorOf(Props.create(QueryTimer.class), "timer");
        timerActor.tell("start", getSelf());
    }

    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof ProcessingResult) {
            ProcessingResult message_result = (ProcessingResult) message;
            result.result.addAll(message_result.result);
            sendResult(false);
        } else if (message instanceof String && message.equals("timeout")) {
            sendResult(true);
        }
    }

    private void sendResult(boolean forceSending) {
        if (forceSending || gotAllAnswers()) {
            callback.tell(result, getSelf());
            context().stop(self());
        }
    }

    private boolean gotAllAnswers() {
        return result.result.size() == ALL_SEARCH_ENGINES.size() * ANSWER_NUMBER;
    }
}

