package utilities;

import java.util.UUID;

/**
 * @author Beatrice V.
 * @created 08.03.2021 - 12:02
 * @project ActorProg2
 */
public class TweetWithId {
    private UUID id;
    private String tweet;
    private int favouritesCount;
    private int retweetsCount;
    private int followersCount;

    public TweetWithId(String tweet, int favouritesCount, int retweetsCount, int followersCount) throws Exception {
        if (!tweet.isEmpty()) {
            this.tweet = tweet;
            this.id = UUID.randomUUID();
            this.favouritesCount = favouritesCount;
            this.retweetsCount = retweetsCount;
            this.followersCount = followersCount;
        } else {
            throw new Exception("Tweet is empty or no tweet attached");
        }
    }

    public TweetWithId() {

    }

    public UUID getId() {
        return id;
    }

    public String getTweet() {
        return tweet;
    }

    public int getFavouritesCount() {
        return favouritesCount;
    }

    public int getRetweetsCount() {
        return retweetsCount;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    @Override
    public String toString() {
        return "TweetWithId{" +
                "id=" + id +
                ", tweet='" + tweet + '\'' +
                ", favouritesCount=" + favouritesCount +
                ", retweetsCount=" + retweetsCount +
                ", followersCount=" + followersCount +
                '}';
    }
}
