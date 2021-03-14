package utilities;

/**
 * @author Beatrice V.
 * @created 13.03.2021 - 18:14
 * @project ActorProg2
 */

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import utilities.tweet.analytics.TweetWithAnalytics;

import java.util.List;

public class MongoUtility {
    private MongoClient client;
    private MongoDatabase database;
    private MongoCollection<Document> collection;

    // Establish connection to database
    public void establishConnectionToDB(String host, int port, String databaseName) {
        client = new MongoClient(host, port);
        database = client.getDatabase(databaseName);
    }

    // Establish connection to collection of database
    public MongoCollection<Document> establishConnectionToCollection(String collectionName) {
        collection = database.getCollection(collectionName);
        return collection;
    }

    // Update elements if are present or insert if there are no such records in db
    public void insertDataToDB(List<TweetWithAnalytics> tweetRecords) throws Exception {
        int size = tweetRecords.size();

        if(size > 0) {
            establishConnectionToCollection("tweets");
            for(int i = 0; i < size; i++) {
                //  get each element individually
                TweetWithAnalytics currentRecord = tweetRecords.get(i);
                Document currentDoc = new Document();
                currentDoc.put("id", currentRecord.getId());
                currentDoc.put("tweet", currentRecord.getTweet());
                currentDoc.put("ratio", currentRecord.getRatio());
                currentDoc.put("score", currentRecord.getEmotionScore());
                System.out.println("HERE------"+currentDoc);
                collection.insertOne(currentDoc);
            }
            /*establishConnectionToCollection("tweets");
            for(int i = 0; i < size; i++) {
                //  get each element individually
                TweetWithAnalytics currentRecord = tweetRecords.get(i);

                //  insert if new and update if already present element in DB
               *//* collection.updateOne(Filters.eq("name", currentCurrency.getTargetName()),
                        new Document("$set", new Document().
                                append("link", currentCurrency.getLink()).
                                append("value", currentCurrency.getExchangeRate())),
                        new UpdateOptions().upsert(true));
                *//*
                Document currentDoc = new Document();
                currentDoc.put("id", currentRecord.getId());
                currentDoc.put("tweet", currentRecord.getTweet());
                currentDoc.put("ratio", currentRecord.getRatio());
                currentDoc.put("score", currentRecord.getEmotionScore());
                System.out.println("HERE------"+currentDoc);
                collection.insertOne(currentDoc);
            }*/
        } else {
            throw new Exception("empty list sent to upsert");
        }
    }
}
