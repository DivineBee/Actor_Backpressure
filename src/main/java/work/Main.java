package work;

import actor.model.ActorFactory;
import actor.model.DeadException;
import actor.model.Supervisor;
import behaviours.EmotionHandler;
import behaviours.EngagementRatioBehaviour;
import behaviours.SSEClientBehaviour;

import static behaviours.JSONBehaviour.tweet;

/**
 * @author Beatrice V.
 * @created 07.02.2021 - 19:36
 * @project ActorProg1
 */
public class Main {
    public static void main(String[] args) throws DeadException {
        SSEClientBehaviour sseClientBehaviour = new SSEClientBehaviour();
        EmotionHandler emotionHandler = new EmotionHandler();
        EngagementRatioBehaviour ratioBehaviour = new EngagementRatioBehaviour();

        ActorFactory.createActor("tweetEngagementRatio", ratioBehaviour);
        ActorFactory.createActor("emotionScoreCalculator", ratioBehaviour);

        ActorFactory.createActor("firstSSEClient", sseClientBehaviour);
        ActorFactory.createActor("secondSSEClient", sseClientBehaviour);
        ActorFactory.createActor("emotionHandler", emotionHandler);
        //ActorFactory.createActor("ratioBehaviour", ratio);

        Supervisor.sendMessage("firstSSEClient", "http://localhost:4000/tweets/1");
        Supervisor.sendMessage("secondSSEClient", "http://localhost:4000/tweets/2");
        Supervisor.sendMessage("emotionHandler", "B:\\PROGRAMMING\\PROJECTS\\ActorProg1\\src\\main\\resources\\emotions.txt");
       // Supervisor.sendMessage("ratioBehaviour", EmotionHandler.getEmotionScore(tweet));
    }
}
