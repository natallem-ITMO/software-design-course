package actors;

import akka.actor.UntypedActor;
import scala.Option;

public class QueryTimer extends UntypedActor {

    final static long timout_sec = 5;

    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof String && message.equals("start")) {
            Thread.sleep(timout_sec * 1000);
            getContext().parent().tell("timeout", getSelf());
        }
    }
}

