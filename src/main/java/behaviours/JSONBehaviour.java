package behaviours;

import actor.model.Actor;
import actor.model.Behaviour;
import actor.model.Supervisor;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import utilities.data.analytics.DataWithAnalytics;
import utilities.data.analytics.DataWithId;

/**
 * @author Beatrice V.
 * @created 10.02.2021 - 14:14
 * @project ActorProg1
 */
public class JSONBehaviour implements Behaviour<String> {
    public static String tweet = null;
    public static String user = null;
    public static int userFollowers = 0;
    public static int userFriends = 0;
    public static int favorites = 0;
    public static int followers = 0;
    public static int numberOfRetweets = 0;
    public static int numberOfStatuses = 0;

    @Override
    public boolean onReceive(Actor<String> self, String data) throws Exception {
        // create ObjectMapper which will be used to parse the json file
        ObjectMapper jsonMapper = new ObjectMapper();

        // if the data contains panic message then the actor is killing itself via die()
        if (data.contains("{\"message\": panic}")) {
            System.out.println("Actor died x_x");
            self.die();
            return false;
        }

        // if the data is not null and is not localhost then parse and extract needed fields
        if (!data.contains("localhost") && data != null && !data.isEmpty()) {
            // read data and assign to jsonNode
            JsonNode jsonNode = jsonMapper.readValue(data, JsonNode.class);

            // extract tweet and save to variable
            JsonNode tweetNode = jsonNode.get("message").get("tweet").get("text");
            tweet = tweetNode.asText() + " ";

            // extract user and save to variable
            JsonNode userNode = jsonNode.get("message").get("tweet").get("user").get("screen_name");
            user = userNode.asText();

            JsonNode userFollowersNode = jsonNode.get("message").get("tweet").get("user").get("followers_count");
            userFollowers = userFollowersNode.asInt();

            JsonNode userFriendsNode = jsonNode.get("message").get("tweet").get("user").get("friends_count");
            userFriends = userFriendsNode.asInt();

            JsonNode statusesCountNode = jsonNode.get("message").get("tweet").get("user").get("statuses_count");
            numberOfStatuses = statusesCountNode.asInt();

            if (data.contains("retweeted_status")) {
                // extract retweet and save to variable
                JsonNode retweetFavoritesNode = jsonNode.get("message").get("tweet").get("retweeted_status").get("user").get("favourites_count");
                favorites = retweetFavoritesNode.asInt();

                // extract folllowers and save to variable
                JsonNode retweetFollowersNode = jsonNode.get("message").get("tweet").get("retweeted_status").get("user").get("followers_count");
                followers = retweetFollowersNode.asInt();

                // extract retweets and save to variable
                JsonNode retweetCountNode = jsonNode.get("message").get("tweet").get("retweeted_status").get("retweet_count");
                numberOfRetweets = retweetCountNode.asInt();
            }
            // Here a chunk of data is initialized with all the desired fields parsed from stream
            DataWithId dataWithId = new DataWithId(tweet, user, favorites, numberOfRetweets, followers, userFriends, userFollowers, numberOfStatuses);

            // Here 76-79 supervisor is sending to other actors this data so each actor
            // will process it and send further to aggregator
            Supervisor.sendMessage("emotionScoreCalculator", dataWithId);
            Supervisor.sendMessage("tweetEngagementRatio", dataWithId);
            Supervisor.sendMessage("userEngagementRatio", dataWithId);

            // from this class we are already ready to transmit 2 fragments of data
            // regarding tweet and user directly to aggregator because they don't need
            // any additional processing
            DataWithAnalytics transmittableFragment = new DataWithAnalytics();
            transmittableFragment.setId(dataWithId.getId());
            // set the tweet which will be transmitted
            transmittableFragment.setTweet(dataWithId.getTweet());
            // set the user which will be transmitted
            transmittableFragment.setUser(dataWithId.getUser());

            // send the composed fragment to aggregator
            Supervisor.sendMessage("aggregator", transmittableFragment);
        }
        return true;
    }

    // if exception encountered then actor will die
    @Override
    public void onException(Actor<String> self, Exception exc) {
        exc.printStackTrace();
        self.die();
    }
}
