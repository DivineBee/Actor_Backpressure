package work;

import actor.model.ActorFactory;
import actor.model.Supervisor;
import behaviours.EmotionHandler;
import behaviours.TweetEngagementRatioBehaviour;
import behaviours.SSEClientBehaviour;
import behaviours.UserEngagementRatioBehaviour;
import data.workers.Aggregator;
import data.workers.Sink;

/**
 * @author Beatrice V.
 * @created 07.02.2021 - 19:36
 * @project ActorProg1
 */
public class Main {
    public static void main(String[] args) throws Exception {
        SSEClientBehaviour sseClientBehaviour = new SSEClientBehaviour();
        EmotionHandler emotionHandler = new EmotionHandler("B:\\\\PROGRAMMING\\\\PROJECTS\\\\ActorProg1\\\\src\\\\main\\\\resources\\\\emotions.txt");
        TweetEngagementRatioBehaviour ratioBehaviour = new TweetEngagementRatioBehaviour();
        UserEngagementRatioBehaviour userRatioBehaviour = new UserEngagementRatioBehaviour();
        Aggregator aggregator = new Aggregator();
        Sink sink = new Sink("localhost", 27017, "Tweets");

        ActorFactory.createActor("tweetEngagementRatio", ratioBehaviour);
        ActorFactory.createActor("userEngagementRatio", userRatioBehaviour);
        ActorFactory.createActor("emotionScoreCalculator", emotionHandler);

        ActorFactory.createActor("firstSSEClient", sseClientBehaviour);
        ActorFactory.createActor("secondSSEClient", sseClientBehaviour);
        ActorFactory.createActor("emotionHandler", emotionHandler);

        ActorFactory.createActor("aggregator", aggregator);
        ActorFactory.createActor("sink", sink);
        //ActorFactory.createActor("ratioBehaviour", ratio);

        Supervisor.sendMessage("firstSSEClient", "http://localhost:4000/tweets/1");
        Supervisor.sendMessage("secondSSEClient", "http://localhost:4000/tweets/2");
//        Supervisor.sendMessage("emotionHandler", "B:\\PROGRAMMING\\PROJECTS\\ActorProg1\\src\\main\\resources\\emotions.txt");
       // Supervisor.sendMessage("ratioBehaviour", EmotionHandler.getEmotionScore(tweet));
    }
}
