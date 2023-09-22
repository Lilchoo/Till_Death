package org.example;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.conversions.Bson;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;

public class UserData {

 protected Window window;

 private String username;

 private int highscore;

 private int recentscore;

 public UserData(Window window) {
  this.window = window;
 }

 public String getName() {
  return this.username;
 }

 public int getHighScore() {
  return this.highscore;
 }

 public int getRecentScore() {
  return this.recentscore;
 }


 public void setUsername(String name) {
  this.username = name;
 }

 public void setScore(int recentscore) {
  this.recentscore = recentscore;
  sendData();

 }

 public void sendData() {
  new Thread(new Runnable() {
   @Override
   public void run() {
    asyncUpdateScore();
   }

  }).start();
 }

 public void asyncUpdateScore() {
  MongoClientURI uri = new MongoClientURI(
   "Cluster_URI");
  try (MongoClient mongoClient = new MongoClient(uri)) {
   MongoDatabase database = mongoClient.getDatabase("databaseName");
   MongoCollection<Document> collection = database.getCollection("tillDeath");
   Bson results = eq("username", username);
   if (collection.countDocuments(results) == 0) {
    Document newPlayer = new Document("username", username);
    newPlayer.append("score", this.recentscore);
    collection.insertOne(newPlayer);
    this.highscore = recentscore;
   } else {
    Document data = collection.find(results).first();
    int score = data.getInteger("score");
    if (recentscore > score) {
     collection.updateOne(eq("username", username), set("score", this.recentscore));
     this.highscore = recentscore;
    } else {
     this.highscore = score;
    }

   }

  }

 }

}
