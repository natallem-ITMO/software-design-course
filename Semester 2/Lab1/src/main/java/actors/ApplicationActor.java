package actors;

import akka.actor.*;
import server.QueryServiceCommunicator;
import server.StubQueryServiceCommunicator;
import utils.ProcessingResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApplicationActor extends UntypedActor {
    final private ActorSystem system;
    private final QueryServiceCommunicator service;
    private final Map<String, ActorRef> callback = new HashMap<>();
    private int masterCounter = 0;

    public ApplicationActor(ActorSystem system, QueryServiceCommunicator service) {
        this.system = system;
        this.service = service;
    }

    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof String) {
            callback.put((String) message, getSender());
            system.actorOf(
                    Props.create(QueryMaster.class, service, (String) message, getSelf()), "master_" + masterCounter++);
        } else if (message instanceof ProcessingResult) {
            ProcessingResult result = (ProcessingResult) message;
            String query = result.query;
            showResult((ProcessingResult) message);
            if (callback.get(query) != null) {
                callback.get(query).tell(message, getSelf());
            }
        }
    }

    private void showResult(ProcessingResult result) {
        System.out.println(result.toString());
    }

    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("MySystem");
        QueryServiceCommunicator serviceCommunicator = new StubQueryServiceCommunicator(List.of(
                new StubQueryServiceCommunicator.SearchEngineSettings(0, "yandex", List.of(
                        "answer yandex 1",
                        "answer yandex 2",
                        "answer yandex 3"
                )),
                new StubQueryServiceCommunicator.SearchEngineSettings(0, "google", List.of(
                        "answer google 1",
                        "answer google 2",
                        "answer google 3"
                )),
                new StubQueryServiceCommunicator.SearchEngineSettings(0, "bing", List.of(
                        "answer bing 1",
                        "answer bing 2",
                        "answer bing 3"
                ))
        ));
        ActorRef actor = system.actorOf(
                Props.create(ApplicationActor.class, system, serviceCommunicator), "main_actor");
        actor.tell("hello!", ActorRef.noSender());
    }

}