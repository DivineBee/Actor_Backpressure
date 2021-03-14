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
import utilities.data.analytics.DataWithAnalytics;

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
    public void insertDataToDB(List<DataWithAnalytics> dataRecords) throws Exception {
        int size = dataRecords.size();

        if (size > 0) {
            establishConnectionToCollection("tweets");
            for (int i = 0; i < size; i++) {
                DataWithAnalytics currentRecord = dataRecords.get(i);
                Document tweetDoc = new Document();
                tweetDoc.put("id", currentRecord.getId());
                tweetDoc.put("tweet", currentRecord.getTweet());
                tweetDoc.put("emotionRatio", currentRecord.getEmotionRatio());
                tweetDoc.put("score", currentRecord.getEmotionScore());
                System.out.println("TWEET------" + tweetDoc);
                collection.insertOne(tweetDoc);
            }

            establishConnectionToCollection("users");
            for (int i = 0; i < size; i++) {
                DataWithAnalytics currentRecord = dataRecords.get(i);
                Document userDoc = new Document();
                userDoc.put("id", currentRecord.getId());
                userDoc.put("user", currentRecord.getUser());
                userDoc.put("userRatio", currentRecord.getUserRatio());
                System.out.println("USER------" + userDoc);
                collection.insertOne(userDoc);
            }
        } else {
            throw new Exception("empty list sent to collection");
        }
    }
}
