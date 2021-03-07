package behaviours;

import actor.model.Actor;
import actor.model.Supervisor;

/**
 * @author Beatrice V.
 * @created 25.02.2021 - 21:16
 * @project ActorProg2
 */
public class EngagementRatioBehaviour implements Behaviour<TweetWithId> {
    @Override
    public boolean onReceive(Actor<String> self, TweetWithId s) throws Exception {
        try {
            double engagementRatio = 0;
            engagementRatio = f+s/t;
            System.out.println("f " + s.getFavorites() + "n " + s.getNumberOfRetweets() + "f " + s.getFollowers());
        }catch (NullPointerException e){
            System.err.println("Can't calculate ratio -> 0 followers");
        }
        System.out.println("ENGAGEMENT: " +  String.format("%.2f", engagementRatio));
        IdWithRatio idWithRatio = new IdWithRatio(tweetWithId.getId(), ratio);
        Supervisor.sendMessage("aggregator", idWithRatio);
        return false;
    }

    @Override
    public void onException(Actor<String> self, Exception exc) {
        exc.printStackTrace();
        self.die();
    }
}
