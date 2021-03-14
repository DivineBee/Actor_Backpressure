package utilities.tweet.analytics;

import java.util.UUID;

/**
 * @author Beatrice V.
 * @created 12.03.2021 - 11:41
 * @project ActorProg2
 */
public class TweetWithAnalytics {
    private UUID id;
    private String tweet;
    private Double ratio;
    private Integer emotionScore;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTweet() {
        return tweet;
    }

    public void setTweet(String tweet) {
        this.tweet = tweet;
    }

    public Double getRatio() {
        return ratio;
    }

    public void setRatio(Double ratio) {
        this.ratio = ratio;
    }

    public Integer getEmotionScore() {
        return emotionScore;
    }

    public void setEmotionScore(Integer emotionScore) {
        this.emotionScore = emotionScore;
    }

    @Override
    public String toString() {
        return "TweetWithAnalytics{" +
                "id=" + id +
                ", tweet='" + tweet + '\'' +
                ", ratio=" + ratio +
                ", emotionScore=" + emotionScore +
                '}';
    }

    //  check if record is full and if it can be transmitted further
    public boolean checkForIntegrity() {
        if (this.tweet != null && ratio != null && emotionScore != null) {
            return true;
        } else {
            return false;
        }
    }
}