package com.example.BankAPIWithMongoDB.util;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.springframework.data.util.Pair;

import java.util.HashMap;
import java.util.Map;

public class MongoDataCopy {

    private MongoClient mongoClient;
    private MongoDatabase sourceDB;
    private MongoDatabase targetDB;
    private MongoCollection<Document> sourceCollection;
    private MongoCollection<Document> targetCollection;
    private final Map<Integer, Pair<String, String>> quarters = new HashMap<>();

    public MongoDataCopy(String url, String sourceDB, String targetDB, String sourceCollection, String targetCollection) {

        try {
            mongoClient = MongoClients.create(url);

            this.sourceDB = mongoClient.getDatabase(sourceDB);
            this.sourceCollection = this.sourceDB.getCollection(sourceCollection);

            this.targetDB = mongoClient.getDatabase(targetDB);
            this.targetCollection = this.targetDB.getCollection(targetCollection);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


        quarters.put(0, Pair.of("-01-01", "-12-31"));
        quarters.put(1, Pair.of("-01-01", "-03-31"));
        quarters.put(2, Pair.of("-04-01", "-06-30"));
        quarters.put(3, Pair.of("-07-01", "-09-30"));
        quarters.put(4, Pair.of("-10-01", "-12-31"));
    }

    public void copyQuarter(Integer year, Integer quarter) {

        if (quarter > 0) {
            String startOfQuarter = (year + quarters.get(quarter).getFirst());
            String endOfQuarter = (year + quarters.get(quarter).getSecond());



            FindIterable<Document> docs = sourceCollection.find(
                    Filters.and(
                            Filters.gte("creation_date", startOfQuarter),
                            Filters.lte("creation_date", endOfQuarter)
                    )
            );

            for (Document currentDocument : docs) {

                Document doc = new Document("accountNumber",currentDocument.getString("accountNumber"));
                MongoCursor<Document> targetCursor = targetCollection.find(doc).iterator();

                if(!targetCursor.hasNext()) targetCollection.insertOne(currentDocument);
            }

        } else {
            copyQuarter(year, 1);

            copyQuarter(year, 2);

            copyQuarter(year, 3);

            copyQuarter(year, 4);
        }
    }
}
