package behaviours;

import actor.model.Actor;
import actor.model.Behaviour;
import actor.model.Supervisor;
import utilities.RatioWithId;
import utilities.TweetWithAnalytics;
import utilities.TweetWithId;

/**
 * @author Beatrice V.
 * @created 25.02.2021 - 21:16
 * @project ActorProg2
 */
public class EngagementRatioBehaviour implements Behaviour<TweetWithId> {
    @Override
    public boolean onReceive(Actor<TweetWithId> self, TweetWithId tweetWithId) throws Exception {
        double engagementRatio = 0;
        try {
            engagementRatio = (tweetWithId.getFavouritesCount() + tweetWithId.getRetweetsCount()) / tweetWithId.getFollowersCount();
        }catch (NullPointerException e){
            System.err.println("Can't calculate ratio -> 0 followers");
        }
//        RatioWithId ratioWithId = new RatioWithId(tweetWithId.getId(), engagementRatio);
//        System.out.println(ratioWithId);

        TweetWithAnalytics transmittableFragment = new TweetWithAnalytics();
        transmittableFragment.setId(tweetWithId.getId());
        transmittableFragment.setRatio(engagementRatio);

        Supervisor.sendMessage("aggregator", transmittableFragment);
        return true;
    }

    @Override
    public void onException(Actor<TweetWithId> self, Exception exc) {
        exc.printStackTrace();
        self.die();
    }
}
