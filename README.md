## Loading streaming data into a database. A backpressure story.

> Laboratory work No2 at Real-Time Programming  
> University: Technical University of Moldova  
> Faculty: Software Engineering  
> Teacher: Burlacu Alexandru  
> Group: FAF -182  
> Student: Vizant Beatrice  
> Task: Loading streaming data into a database with Backpressure  

## Table of contents
- [Requirements](#requirements)
- [Output example](#output-example)
- [Explanation](#explanation)
- [Technologies](#technologies)
- [Status](#status)

## Requirements
* Copy the Dynamic Supervisor + Workers that compute the sentiment score and adapt this copy of the system to compute the Engagement Ratio per Tweet. Notice that some tweets are actually retweets and contain a special field retweet_status. You will have to extract it and treat it as a separate tweet. The Engagement Ratio will be computed as: (#favorites + #retweets) / #followers.
* Workers now print sentiment scores, now they will have to send it to a dedicated aggregator actor where the sentiment score, the engagement ratio, and the original tweet will be merged together. Hint: you will need special ids to recombine everything properly because synchronous communication is forbidden.  
* Load everything to database, implement a backpressure mechanism called adaptive batching​​. Adaptive batching means that you write/send data in batches if the maximum batch size is reached, for example 128 elements, or the time is up, for example a window of 200ms is provided, whichever occurs first. This will be the responsibility of the sink actor(s).  
* Then split the tweet JSON into users and tweets and keep them in separate collections/tables in the DB.  
* Of course, don't forget about using actors and supervisors for your system to keep it running.

OPTIONAL:  
* Reactive pull backpressure mechanism between the aggregator actor and the sink.  
* Create an actor that would compute the Engagement Ratio per User.  
* Resumable/pausable transmission between the aggregator and the sink, that is, if sink or DB is unavailable, the aggregator will buffer the messages until they can be sent again.  
* Have a metrics endpoint to monitor the stats of the sink/DB controller service on ingested messages, average execution time, 75th, 90th, 95th percentile execution time, number of crashes per given time window, etc.  
* Anything else interesting and challenging you can think of.  

## Output example
![alt-text](https://github.com/DivineBee/Actor_Backpressure/blob/master/src/main/resources/lab2giff.gif?raw=true)
![alt-text](https://github.com/DivineBee/Actor_Backpressure/blob/master/src/main/resources/lab2.JPG?raw=true)
![alt-text](https://github.com/DivineBee/Actor_Backpressure/blob/master/src/main/resources/lab2tweets.JPG?raw=true)
![alt-text](https://github.com/DivineBee/Actor_Backpressure/blob/master/src/main/resources/lab2users.JPG?raw=true)

## Explanation
First I optimized the previous actor system to work faster and better, by extracting and changing inner loops and rethink the function's behaviour. Then I created 2 classes _DataWithAnalytics_ and _DataWithId_. _DataWithId_ contains primary information for starting processing. Since this type of information is sufficiently typed and its used by multiple actors then we use it for primary transmission. For example to emotion score and ratio. If emotion score and ratio receive only one type of data but aggregator had a problem of receiving ambiguous type of data( data of different kind). From one side he receives data about tweet, from other side he receives data about ratio or emotion score or even about the user. Therefore, we have that the information that we get in the aggregator is heterogeneous.  
Here are 2 possible way of solving this problem. First is creating a "master" class for example _RecordWithId_ and extend from that class multiple children(eg. TweetWithID. NameWithId, UserWithId... etc.) this variant is not bad but it will complicate the structure of the program and can create problems of understanding of the program by some other programmer which will want to work with the code. I think it's unneeded "over-engineering" in this case, when you try to solve a simple problem by some complex or hard approach. So I decided to make a class called _DataAnalytics_ which contains all the information which might be used by aggregator altogether, but admitting that by different actors we can receive different messages which are not full-fledged (classes' object which don't contain all the information) in comparison to other data passed by Json we transmit not fully complete object but only fragments of this object. And in aggregator I combine these fragments into something integral. And I got the simplified version of program where I don't have big accumulation of classes and wherein didn't override big parts of code just for adaptation of specific situations, instead of adapting my view to the program - I adapted the program to my view.  
In _JsonBehaviour_ I had to extract more data by using nodes for this laboratory work which are used in ratio calculations for example. Then, inserted all the data received from Json to dataWithId and sent it to different actors for further process of that data. It is sent to actors which will calculate emotion score for example, tweet engagement ratio, user engagement ratio. And also in this class we have ready to be sent data to aggregator such as tweet and user.  
```java
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
``` 
After this I added _TweetEngagementRatio_ which receives the _DataWithId_ which I talked about earlier, and calculates tweet's engagement ratio by the following formula: favorites+retweets/followers. And after calculation the result is put in the fragment and sent to aggregator as following:  
```java
try {
    // calculate the engagement ratio by formula favorites+retweets/followers
        tweetEngagementRatio = (dataWithId.getFavouritesCount() + dataWithId.getRetweetsCount()) / dataWithId.getRetweetFollowersCount();
    } catch (NullPointerException e) {
        // in case he is some cringe guy with retweets and favourites and nobody want to follow him.
        System.err.println("Can't calculate ratio -> 0 followers");
    }
    // Initialize new fragment and insert the engagement ratio 
    DataWithAnalytics transmittableFragment = new DataWithAnalytics();
    // get the incoming data id
    transmittableFragment.setId(dataWithId.getId());
    // append calculated ratio
    transmittableFragment.setEmotionRatio(tweetEngagementRatio);
    // send the fragment with tweet's ratio to aggregator
    Supervisor.sendMessage("aggregator", transmittableFragment);
    return true;
```
Then I added the _UserEngagementRatio_ which is similar to the principles of tweet engagement ratio, but has other parameters and formula which is the following: followers-friends/statuses. I decided to make this formula like that because we will see the real user ratio by first seeing how many "real" followers user has except friends and then divide it by number of statuses to see the real impact and popularity. After calculations this little fragment with only user's ratio is sent to aggregator.  
Now, about Aggregator, it receives and then combines all the fragments which arrive together by id and merge them in one chunk which will be sent to the sink. Code for this is commented and self-explanatory:  
```java
public boolean onReceive(Actor<DataWithAnalytics> self, DataWithAnalytics dataAnalyticsFragment) throws Exception {
    // check if there is such an entry in the local hashmap. If so, then the execution of the
    // code inside the if starts
    if (localHashMap.get(dataAnalyticsFragment.getId()) != null) {
        // since we have already checked and found such an entry with such an id in the hashmap, we pull it out
        // to perform operations on it
        DataWithAnalytics record = localHashMap.get(dataAnalyticsFragment.getId());
        // we check what data is in the transmitted fragment and transfer it to this record
        checkData(dataAnalyticsFragment, record);
        // then check the data for integrity, if it passes the check then it can be sent
        // to the sink and removed from local map
        if (record.checkForIntegrity()) {
            Supervisor.sendMessage("sink", record);
            localHashMap.remove(record);
        }
    } else {
        // else just create new record and place new incoming data
        DataWithAnalytics newRecord = new DataWithAnalytics();
        newRecord.setId(dataAnalyticsFragment.getId());
        checkData(dataAnalyticsFragment, newRecord);
        localHashMap.put(dataAnalyticsFragment.getId(), newRecord);
        }
```  
Before creating the Sink I had to think first about database connection with MongoDB, so I created a class _MongoUtility_ which establishes all the needed connections to database and its collections. It contains also a method called _insertDataToDb()_ which update elements if are present or insert if there are no such records in db, first it inserts the desired fields to the "tweets" collection and then other fields regarding user data to the "users" collection such as id, user and user ratio. An example can be seen bellow(this method will later be called inside the Sink:  
```java
establishConnectionToCollection("tweets");
for (int i = 0; i < size; i++) {
    DataWithAnalytics currentRecord = dataRecords.get(i);
    Document tweetDoc = new Document();
    tweetDoc.put("id", currentRecord.getId());
    tweetDoc.put("tweet", currentRecord.getTweet());
    tweetDoc.put("emotionRatio", currentRecord.getEmotionRatio());
    tweetDoc.put("score", currentRecord.getEmotionScore());
    collection.insertOne(tweetDoc);
}
```
Now about _Sink_ and the backpressure mechanism. It was required to send data in batches of specific size 128 or if the time is up (200ms), first idea was to put in a while loop the time-checking condition and within it to insert the condition for batch size, but it was very resource demanding and not the best approach. So I decided to insert a flag variable isSent. So now I have 2 separate optimized if-conditions one is checking for how many time has passed and other has just one check which is for if the time is up or the maximum batch size is reached.  
```java
if (isSent) {
    // get current time
    start = System.currentTimeMillis();
    // calculate 200 ms from starting of timer
    end = (long) (start + 0.2 * 1000);
    // set flag to false (the timer will finish)
    isSent = false;
}
// if the time is up or the maximum batch size is reached (whichever occurs first)
if (System.currentTimeMillis() >= end || recordsToDB.size() >= BATCH_SIZE) {
    // insert records to DB
    mongoUtility.insertDataToDB(recordsToDB);
    // create a list to store other records
    recordsToDB = new ArrayList<>();
    // set flag to true
    isSent = true;
}
```
## Technologies
Java 11 and Maven for Jackson and MongoDB dependencies

## Status
Project Status _finished_
