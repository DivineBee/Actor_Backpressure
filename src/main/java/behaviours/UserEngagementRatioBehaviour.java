package behaviours;

import actor.model.Actor;
import actor.model.Behaviour;
import actor.model.Supervisor;
import utilities.data.analytics.DataWithAnalytics;
import utilities.data.analytics.DataWithId;

/**
 * @author Beatrice V.
 * @created 14.03.2021 - 11:25
 * @project ActorProg2
 */
public class UserEngagementRatioBehaviour implements Behaviour<DataWithId> {
    @Override
    public boolean onReceive(Actor<DataWithId> self, DataWithId dataWithId) throws Exception {
        double userEngagementRatio = 0;
        try {
            userEngagementRatio = (dataWithId.getUserFollowersCount() - dataWithId.getFriendsCount()) / dataWithId.getStatusesCount();
        }catch (NullPointerException e){
            System.err.println("Can't calculate ratio -> 0 statuses");
        }

        DataWithAnalytics transmittableFragment = new DataWithAnalytics();
        transmittableFragment.setId(dataWithId.getId());
        transmittableFragment.setUserRatio(userEngagementRatio);
        //System.out.println("DDD " + transmittableFragment);

        Supervisor.sendMessage("aggregator", transmittableFragment);
        return true;
    }

    @Override
    public void onException(Actor<DataWithId> self, Exception exc) {
        exc.printStackTrace();
        self.die();
    }
}