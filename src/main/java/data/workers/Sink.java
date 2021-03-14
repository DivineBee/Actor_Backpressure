package data.workers;

import actor.model.Actor;
import actor.model.Behaviour;
import utilities.MongoUtility;
import utilities.data.analytics.DataWithAnalytics;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Beatrice V.
 * @created 12.03.2021 - 10:20
 * @project ActorProg2
 */
public class Sink implements Behaviour<DataWithAnalytics> {
    private MongoUtility mongoUtility;
    private long start = 0;
    private long end = 0;
    private boolean isSent = true;

    private List<DataWithAnalytics> recordsToDB = new ArrayList<DataWithAnalytics>();
    private static final int BATCH_SIZE = 128;


    public Sink(String host, int port, String databaseName) {
        mongoUtility = new MongoUtility();
        mongoUtility.establishConnectionToDB(host, port, databaseName);
    }

    @Override
    public boolean onReceive(Actor<DataWithAnalytics> self, DataWithAnalytics msg) throws Exception {
        if (isSent) {
            start = System.currentTimeMillis();
            end = (long) (start + 0.2 * 1000);
            isSent = false;
        }

        if (System.currentTimeMillis() >= end || recordsToDB.size() >= BATCH_SIZE) {
            mongoUtility.insertDataToDB(recordsToDB);
            recordsToDB = new ArrayList<>();
            isSent = true;
        }

        recordsToDB.add(msg);
        System.out.println(msg);

        return true;
    }

    @Override
    public void onException(Actor<DataWithAnalytics> self, Exception exc) {
        exc.printStackTrace();
        self.die();
    }
}
