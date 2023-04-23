package cc.phantomhost.core.config;

import cc.phantomhost.core.utils.credential.CredentialKey;
import cc.phantomhost.core.utils.credential.PhantomCredentials;
import com.mongodb.client.*;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.util.Map;

public class MongoDBConfigurationIO implements ConfigurationIO{

    private static final String OBJECT_ID_KEY = "_id";
    private final PhantomCredentials credentials;
    private final String itemId;

    public MongoDBConfigurationIO(PhantomCredentials credentials, String itemId) throws IOException {
        this.credentials = credentials;
        this.itemId = itemId;
    }

    private String getURI(){
        return String.format("mongodb://%s:%s@localhost:27017",credentials.getCredential(CredentialKey.USERNAME),
                credentials.getCredential(CredentialKey.PASSWORD));
    }

    @Override
    public void loadConfiguration(Map<Setting,String> configuration) throws IOException {
        try(MongoClient client = MongoClients.create(getURI())){
            MongoDatabase database = client.getDatabase(credentials.getCredential(CredentialKey.DATABASE));
            MongoCollection<Document> collection = database.getCollection(itemId);
            Document searchQuery = new Document();
            searchQuery.put(OBJECT_ID_KEY,itemId);
            FindIterable<Document> cursor = collection.find(searchQuery);
            Document data = cursor.first();
            if(data == null){
                return;
            }
            for(String key : data.keySet()){
                configuration.put(Setting.getFromKey(key),data.get(key).toString());
            }
        }
    }

    @Override
    public void saveConfiguration(Map<Setting,String> configuration) throws IOException {
        try(MongoClient client = MongoClients.create(getURI())){
            MongoDatabase database = client.getDatabase(credentials.getCredential(CredentialKey.DATABASE));
            MongoCollection<Document> collection = database.getCollection(itemId);
            Document searchQuery = new Document();
            searchQuery.put(OBJECT_ID_KEY,itemId);
            FindIterable<Document> cursor = collection.find(searchQuery);
            Document newDocument = generateDocumentToSave(configuration);
            if(cursor.first() == null){
                newDocument.put(OBJECT_ID_KEY,new ObjectId(itemId));
                collection.insertOne(newDocument);
                return;
            }
            Document updateObject = new Document();
            updateObject.put("$set",newDocument);
            collection.updateOne(searchQuery,updateObject);
        }
    }

    private Document generateDocumentToSave(Map<Setting,String> configuration){
        Document document = new Document();
        configuration.forEach((key,value) -> document.put(key.getKey(),value));
        return document;
    }
}
