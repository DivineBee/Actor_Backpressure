package data.workers;

import actor.model.Actor;
import actor.model.Behaviour;
import actor.model.Supervisor;
import org.bson.io.BsonOutput;
import utilities.data.analytics.DataWithAnalytics;

import java.util.HashMap;
import java.util.UUID;

/**
 * @author Beatrice V.
 * @created 12.03.2021 - 10:20
 * @project ActorProg2
 */
public class Aggregator implements Behaviour<DataWithAnalytics> {
    //DataWithId tweetWithId = new DataWithId();
    HashMap<UUID, DataWithAnalytics> localHashMap;

    public Aggregator() throws Exception {
        localHashMap = new HashMap<>();
        DataWithAnalytics.class.getClass();
    }

    @Override
    public boolean onReceive(Actor<DataWithAnalytics> self, DataWithAnalytics dataAnalyticsFragment) throws Exception {
        if (dataAnalyticsFragment.getUser() != null) {
            System.out.println(dataAnalyticsFragment);
        }
        if (localHashMap.get(dataAnalyticsFragment.getId()) != null) {
            DataWithAnalytics record = localHashMap.get(dataAnalyticsFragment.getId());
            if (dataAnalyticsFragment.getTweet() != null) {
                record.setTweet(dataAnalyticsFragment.getTweet());
            }
            if (dataAnalyticsFragment.getEmotionScore() != null) {
                record.setEmotionScore(dataAnalyticsFragment.getEmotionScore());
            }
            if (dataAnalyticsFragment.getEmotionRatio() != null) {
                record.setEmotionRatio(dataAnalyticsFragment.getEmotionRatio());
            }
            if (dataAnalyticsFragment.getUser() != null) {
                record.setUser(dataAnalyticsFragment.getUser());
            }
            if (dataAnalyticsFragment.getUserRatio() != null) {
                record.setUserRatio(dataAnalyticsFragment.getUserRatio());
            }

            if (record.checkForIntegrity()) {
                Supervisor.sendMessage("sink", record);
                localHashMap.remove(record);
                System.out.println("RECORD " + record);
            }
        } else {
            DataWithAnalytics newRecord = new DataWithAnalytics();
            newRecord.setId(dataAnalyticsFragment.getId());
            if (dataAnalyticsFragment.getTweet() != null) {
                newRecord.setTweet(dataAnalyticsFragment.getTweet());
            }
            if (dataAnalyticsFragment.getEmotionScore() != null) {
                newRecord.setEmotionScore(dataAnalyticsFragment.getEmotionScore());
            }
            if (dataAnalyticsFragment.getEmotionRatio() != null) {
                newRecord.setEmotionRatio(dataAnalyticsFragment.getEmotionRatio());
            }
            if (dataAnalyticsFragment.getUser() != null) {
                newRecord.setUser(dataAnalyticsFragment.getUser());
            }
            if (dataAnalyticsFragment.getUserRatio() == null) {
                newRecord.setUserRatio(dataAnalyticsFragment.getUserRatio());
            }
            localHashMap.put(dataAnalyticsFragment.getId(), newRecord);
        }
        return true;
    }

    public void checkData(DataWithAnalytics dataAnalyticsFragment, DataWithAnalytics record) {
        if (dataAnalyticsFragment.getTweet() != null) {
            record.setTweet(dataAnalyticsFragment.getTweet());
        } else if (dataAnalyticsFragment.getEmotionScore() != null) {
            record.setEmotionScore(dataAnalyticsFragment.getEmotionScore());
        } else if (dataAnalyticsFragment.getEmotionRatio() != null) {
            record.setEmotionRatio(dataAnalyticsFragment.getEmotionRatio());
        } else if (dataAnalyticsFragment.getUser() != null) {
            record.setUser(dataAnalyticsFragment.getUser());
        } else if (dataAnalyticsFragment.getUserRatio() != null) {
            record.setUserRatio(dataAnalyticsFragment.getUserRatio());
        }
    }

    @Override
    public void onException(Actor<DataWithAnalytics> self, Exception exc) {
        exc.printStackTrace();
        self.die();
    }
}
