package ws.rlns.glowstone.database;

import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import lombok.Getter;
import org.bson.Document;
import ws.rlns.glowstone.database.utils.Credentials;

import java.util.concurrent.CompletableFuture;

import static java.util.concurrent.CompletableFuture.supplyAsync;

public final class GlowstoneMongo {

    @Getter
    private final Credentials credentials;

    @Getter
    private final MongoClient client;

    @Getter
    private MongoDatabase database;

    public GlowstoneMongo(Credentials credentials, String databaseName) {
        this.credentials = credentials;

        if (credentials.getUri() != null) {
            client = new MongoClient(new MongoClientURI(credentials.getUri()));
        } else {
            if (!credentials.getPassword().isEmpty()) {
                ServerAddress serverAddress = new ServerAddress(credentials.getHost(),
                        credentials.getPort());

                MongoCredential credential = MongoCredential.createCredential(
                        credentials.getUser(), "admin",
                        credentials.getPassword().toCharArray());

                client = new MongoClient(serverAddress, credential, new MongoClientOptions.Builder().build());
            } else {
                client = new MongoClient(credentials.getHost(), credentials.getPort());
            }
        }
        try {
            database = client.getDatabase(databaseName.isBlank() ? "glowstone_default" : databaseName);
        } catch (Exception ignored) {
        }
    }

    private CompletableFuture<Document> getDocument(MongoCollection<Document> collection, String key, String value) {
        return supplyAsync(() -> collection.find(Filters.eq(key, value)).first());

    }

    private CompletableFuture<Boolean> UpdateDocument(MongoCollection<Document> collection, Document oldDocu, Document newDocu){
        return supplyAsync(()-> collection.replaceOne(oldDocu, newDocu, new ReplaceOptions().upsert(true)).wasAcknowledged());
    }

}
