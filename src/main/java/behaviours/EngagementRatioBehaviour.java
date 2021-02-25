package behaviours;

import actor.model.Actor;
import actor.model.Behaviour;

import static behaviours.JSONBehaviour.*;

/**
 * @author Beatrice V.
 * @created 25.02.2021 - 21:16
 * @project ActorProg2
 */
public class EngagementRatioBehaviour implements Behaviour<String> {
    public static double engagementRatio = 0;

    @Override
    public boolean onReceive(Actor<String> self, String s) throws Exception {
        try {
            System.out.println("f "+favorites+ "n " + numberOfRetweets + "f " + followers);
        }catch (NullPointerException e){
            System.err.println("Can't calculate ratio -> 0 followers");
        }
        System.out.println("ENGAGEMENT: " +  String.format("%.2f", engagementRatio));
        return false;
    }

    @Override
    public void onException(Actor<String> self, Exception exc) {
        exc.printStackTrace();
        self.die();
    }
}
