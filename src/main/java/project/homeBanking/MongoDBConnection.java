package project.homeBanking;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MongoDBConnection {
    private final Logger mongoLogger;
    private MongoDatabase database;
    private MongoClient mongoClient;

    public MongoDBConnection() {
        mongoLogger = Logger.getLogger( "org.mongodb.driver" );
        mongoLogger.setLevel(Level.SEVERE);
        MongoClientURI uri = new MongoClientURI("mongodb+srv://oleskiy:Adgj1357@cluster0-x1ici.mongodb.net/HomeBanking?retryWrites=true&w=majority");
        mongoClient = new MongoClient(uri);
        database = mongoClient.getDatabase("HomeBanking");
    }

    public MongoDatabase getDatabase() {
        return this.database;
    }

    public MongoClient getMongoClient() {
        return this.mongoClient;
    }

}
