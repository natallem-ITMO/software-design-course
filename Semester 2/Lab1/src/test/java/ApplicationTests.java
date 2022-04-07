import actors.ApplicationActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import org.junit.*;
import scala.concurrent.duration.Duration;
import akka.testkit.TestKit;
import server.QueryServiceCommunicator;
import server.StubQueryServiceCommunicator;
import utils.ProcessingResult;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class ApplicationTests {
    private static ActorSystem system;

    @Before
    public void setup() {
        system = ActorSystem.create();
    }

    @After
    public void teardown() {
        TestKit.shutdownActorSystem(system, Duration.apply(40, TimeUnit.SECONDS), true);
        system = null;
    }

    @Test
    public void testCorrectServiceResponsesAnswers() {
        List<String> queries = List.of("hello", "java", "111", "");
        for (String query : queries) {
            new TestKit(system) {{
                List<String> companies = List.of("yandex", "google", "bing");
                Set<String> checkResult = new HashSet<>();
                List<StubQueryServiceCommunicator.SearchEngineSettings> searchEngineSettings =
                        companies.stream().map(company -> {
                            List<String> answers = List.of(
                                    "answer " + company + "1",
                                    "answer " + company + "2",
                                    "answer " + company + "3"
                            );
                            checkResult.addAll(answers.stream().map(x -> query + " " + x).collect(Collectors.toSet()));
                            return new StubQueryServiceCommunicator.SearchEngineSettings(0, company,
                                    answers);
                        }).collect(Collectors.toList());

                QueryServiceCommunicator serviceCommunicator = new StubQueryServiceCommunicator(
                        searchEngineSettings
                );
                ActorRef actorRef = system.actorOf(Props.create(ApplicationActor.class, system, serviceCommunicator), "application_actor_test_" + query);
                actorRef.tell(query, testActor());
                ProcessingResult response = expectMsgClass(Duration.apply(30, TimeUnit.SECONDS), ProcessingResult.class);
                assertEquals(response.result.stream().map(x -> x.answer).collect(Collectors.toSet()), checkResult);
            }};
        }
    }

    @Test
    public void testOneServiceDoNotResponse() {
        String query = "hello";
        new TestKit(system) {{
            List<String> companies = List.of("yandex", "google", "bing");
            Set<String> checkResult = new HashSet<>();
            List<StubQueryServiceCommunicator.SearchEngineSettings> searchEngineSettings =
                    companies.stream().map(company -> {
                        List<String> answers = List.of(
                                "answer " + company + "1",
                                "answer " + company + "2",
                                "answer " + company + "3"
                        );
                        if (checkResult.size() <= 3)
                            checkResult.addAll(answers.stream().map(x -> query + " " + x).collect(Collectors.toSet()));
                        return new StubQueryServiceCommunicator.SearchEngineSettings(0, company,
                                answers);
                    }).collect(Collectors.toList());
            searchEngineSettings.get(2).sleepTimeout = 20;
            QueryServiceCommunicator serviceCommunicator = new StubQueryServiceCommunicator(
                    searchEngineSettings
            );
            ActorRef actorRef = system.actorOf(Props.create(ApplicationActor.class, system, serviceCommunicator), "application_actor_test_" + query);
            actorRef.tell(query, testActor());
            ProcessingResult response = expectMsgClass(Duration.apply(30, TimeUnit.SECONDS), ProcessingResult.class);
            assertEquals(response.result.stream().map(x -> x.answer).collect(Collectors.toSet()), checkResult);
        }};
    }

    @Test
    public void testNoServicesResponse() {
        String query = "hello";
        new TestKit(system) {{
            List<String> companies = List.of("yandex", "google", "bing");
            Set<String> checkResult = new HashSet<>();
            List<StubQueryServiceCommunicator.SearchEngineSettings> searchEngineSettings =
                    companies.stream().map(company -> {
                        List<String> answers = List.of(
                                "answer " + company + "1",
                                "answer " + company + "2",
                                "answer " + company + "3"
                        );
                        return new StubQueryServiceCommunicator.SearchEngineSettings(20, company,
                                answers);
                    }).collect(Collectors.toList());
            QueryServiceCommunicator serviceCommunicator = new StubQueryServiceCommunicator(
                    searchEngineSettings
            );
            ActorRef actorRef = system.actorOf(Props.create(ApplicationActor.class, system, serviceCommunicator), "application_actor_test_" + query);
            actorRef.tell(query, testActor());
            ProcessingResult response = expectMsgClass(Duration.apply(30, TimeUnit.SECONDS), ProcessingResult.class);
            assertEquals(response.result.stream().map(x -> x.answer).collect(Collectors.toSet()), checkResult);
        }};
    }
}
