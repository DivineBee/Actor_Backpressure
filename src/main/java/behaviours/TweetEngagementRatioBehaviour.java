package behaviours;

import actor.model.Actor;
import actor.model.Behaviour;
import actor.model.Supervisor;
import utilities.data.analytics.DataWithAnalytics;
import utilities.data.analytics.DataWithId;

/**
 * @author Beatrice V.
 * @created 25.02.2021 - 21:16
 * @project ActorProg2
 */
public class TweetEngagementRatioBehaviour implements Behaviour<DataWithId> {
    @Override
    public boolean onReceive(Actor<DataWithId> self, DataWithId dataWithId) throws Exception {
        double engagementRatio = 0;
        try {
            engagementRatio = (dataWithId.getFavouritesCount() + dataWithId.getRetweetsCount()) / dataWithId.getRetweetFollowersCount();
        }catch (NullPointerException e){
            System.err.println("Can't calculate ratio -> 0 followers");
        }

        DataWithAnalytics transmittableFragment = new DataWithAnalytics();
        transmittableFragment.setId(dataWithId.getId());
        transmittableFragment.setEmotionRatio(engagementRatio);

        Supervisor.sendMessage("aggregator", transmittableFragment);
        return true;
    }

    @Override
    public void onException(Actor<DataWithId> self, Exception exc) {
        exc.printStackTrace();
        self.die();
    }
}
