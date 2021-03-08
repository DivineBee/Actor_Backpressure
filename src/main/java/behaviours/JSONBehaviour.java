package behaviours;

import actor.model.Actor;
import actor.model.Behaviour;
import actor.model.Supervisor;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import utilities.TweetWithId;

/**
 * @author Beatrice V.
 * @created 10.02.2021 - 14:14
 * @project ActorProg1
 */
public class JSONBehaviour implements Behaviour<String> {
    public static String tweet = null;
    public static String user = null;
    public static int favorites = 0;
    public static int followers = 0;
    public static int numberOfRetweets = 0;

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
            //System.out.println("DATA" + data + favorites);
            // print beautifully the output

            //1st field id, tweet
            TweetWithId tweetWithId = new TweetWithId(tweet, favorites, followers, numberOfRetweets);

//            Supervisor.sendMessage("aggregator", tweetWithId);
            Supervisor.sendMessage("emotionScoreCalculator", tweetWithId);
            Supervisor.sendMessage("tweetEngagementRatio", tweetWithId);
//            System.out.println("USER: " + user + " | " + "TWEET: " + tweet + " | " + "SCORE: " + EmotionHandler.getEmotionScore(tweet));

            System.out.println(tweetWithId);
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
