package data.workers;

import actor.model.Actor;
import actor.model.Behaviour;
import utilities.TweetWithAnalytics;
import java.util.HashMap;
import java.util.UUID;

/**
 * @author Beatrice V.
 * @created 12.03.2021 - 10:20
 * @project ActorProg2
 */
public class Aggregator implements Behaviour<TweetWithAnalytics> {
    //TweetWithId tweetWithId = new TweetWithId();
    HashMap<UUID, TweetWithAnalytics> localHashMap;

    public Aggregator() throws Exception {
        localHashMap = new HashMap<>();
        TweetWithAnalytics.class.getClass();
    }

    @Override
    public boolean onReceive(Actor<TweetWithAnalytics> self, TweetWithAnalytics tweetAnalyticsFragment) throws Exception {
        if (localHashMap.get(tweetAnalyticsFragment.getId()) != null) {
            TweetWithAnalytics record = localHashMap.get(tweetAnalyticsFragment.getId());
            if (tweetAnalyticsFragment.getTweet() != null) {
                record.setTweet(tweetAnalyticsFragment.getTweet());
            } else if (tweetAnalyticsFragment.getEmotionScore() != null) {
                record.setEmotionScore(tweetAnalyticsFragment.getEmotionScore());
            } else if (tweetAnalyticsFragment.getRatio() != null) {
                record.setRatio(tweetAnalyticsFragment.getRatio());
            }

            if (record.checkForIntegrity()) {
               // Supervisor.sendMessage("sink", record);
               // localHashMap.remove(record);
                System.out.println(record);
            }
        } else {
            TweetWithAnalytics newRecord = new TweetWithAnalytics();
            newRecord.setId(tweetAnalyticsFragment.getId());
            if (tweetAnalyticsFragment.getTweet() != null) {
                newRecord.setTweet(tweetAnalyticsFragment.getTweet());
            } else if (tweetAnalyticsFragment.getEmotionScore() != null) {
                newRecord.setEmotionScore(tweetAnalyticsFragment.getEmotionScore());
            } else if (tweetAnalyticsFragment.getRatio() != null) {
                newRecord.setRatio(tweetAnalyticsFragment.getRatio());
            }
            localHashMap.put(tweetAnalyticsFragment.getId(), newRecord);
        }

        return true;
    }

    @Override
    public void onException(Actor<TweetWithAnalytics> self, Exception exc) {
        exc.printStackTrace();
        self.die();
    }
}
