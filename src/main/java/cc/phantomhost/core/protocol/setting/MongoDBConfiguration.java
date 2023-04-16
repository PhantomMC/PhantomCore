package cc.phantomhost.core.protocol.setting;

import cc.phantomhost.core.utils.credential.CredentialKey;
import cc.phantomhost.core.utils.credential.PhantomCredentials;
import com.mongodb.client.*;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.yaml.snakeyaml.tokens.Token;

import java.io.IOException;
import java.io.OutputStream;

public class MongoDBConfiguration implements Configuration {

    private static final String OBJECT_ID_KEY = "_id";
    private final PhantomCredentials credentials;
    private final String itemId;

    public MongoDBConfiguration(PhantomCredentials credentials, String itemId) throws IOException {
        this.credentials = credentials;
        this.itemId = itemId;
    }

    @Override
    public String getSetting(Setting key) {
        try(MongoClient client = MongoClients.create(getURI())){
            MongoDatabase database = client.getDatabase(credentials.getCredential(CredentialKey.DATABASE));
            MongoCollection<Document> collection = database.getCollection(itemId);
            Document searchQuery = new Document();
            searchQuery.put(OBJECT_ID_KEY,itemId);
            FindIterable<Document> cursor = collection.find(searchQuery);
            Document data = cursor.first();
            if(data == null){
                return null;
            }
            return data.get(key.getKey()).toString();
        }
    }

    private String getURI(){
        return String.format("mongodb://%s:%s@localhost:27017",credentials.getCredential(CredentialKey.USERNAME),
                credentials.getCredential(CredentialKey.PASSWORD));
    }

    @Override
    public void setSetting(Setting key, Object value) {
        try(MongoClient client = MongoClients.create(getURI())){
            MongoDatabase database = client.getDatabase(credentials.getCredential(CredentialKey.DATABASE));
            MongoCollection<Document> collection = database.getCollection(itemId);
            Document searchQuery = new Document();
            searchQuery.put(OBJECT_ID_KEY,itemId);
            FindIterable<Document> cursor = collection.find(searchQuery);
            if(cursor.first() == null){
                Document newData = new Document();
                newData.put(OBJECT_ID_KEY,new ObjectId(itemId));
                newData.put(key.getKey(),value);
                collection.insertOne(newData);
                return;
            }
            Document newDocument = new Document();
            newDocument.put(key.getKey(),value);
            Document updateObject = new Document();
            updateObject.put("$set",newDocument);
            collection.updateOne(searchQuery,updateObject);
        }
    }

    @Override
    public void saveToStream(OutputStream stream) throws IOException {
        throw new UnsupportedOperationException("Can not save to stream, as this is linked to a mongodb");
    }
}
