package behaviours;

import actor.model.Actor;
import actor.model.Behaviour;
import actor.model.Supervisor;
import utilities.user.analytics.UserWithAnalytics;
import utilities.user.analytics.UserWithId;

/**
 * @author Beatrice V.
 * @created 14.03.2021 - 11:25
 * @project ActorProg2
 */
public class UserEngagementRatioBehaviour implements Behaviour<UserWithId> {
    @Override
    public boolean onReceive(Actor<UserWithId> self, UserWithId userWithId) throws Exception {
        double engagementRatio = 0;
        try {
            engagementRatio = (userWithId.getFollowersCount() - userWithId.getFriendsCount()) / userWithId.getStatusesCount();
        }catch (NullPointerException e){
            System.err.println("Can't calculate ratio -> 0 statuses");
        }

        UserWithAnalytics transmittableFragment = new UserWithAnalytics();
        transmittableFragment.setId(userWithId.getId());
        transmittableFragment.setRatio(engagementRatio);

       // Supervisor.sendMessage("aggregator", transmittableFragment);
        return true;
    }

    @Override
    public void onException(Actor<UserWithId> self, Exception exc) {
        exc.printStackTrace();
        self.die();
    }
}