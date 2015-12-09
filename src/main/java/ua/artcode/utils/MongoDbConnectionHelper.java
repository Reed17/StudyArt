package ua.artcode.utils;

import com.mongodb.MongoClient;
import org.apache.log4j.Logger;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.springframework.context.ApplicationContext;

/**
 * Created by Razer on 09.11.15.
 */
public class MongoDbConnectionHelper {
    public MongoDbConnectionHelper() {

        context = SpringContext.getContext();
    }

    private final Logger LOG = Logger.getLogger(MongoDbConnectionHelper.class);
    private ApplicationContext context;
    private MongoClient mongoClient;
    private Datastore datastore;
    private Morphia morphia;

    public MongoClient initMongoClient() {
        if (mongoClient == null) {
            LOG.trace("Create mongo client");
            mongoClient = context.getBean(MongoClient.class);
        }
        return mongoClient;
    }

    public <T> Datastore createDatastore(MongoClient mongoClient, Class<T> tClass) {
        if (datastore == null) {
            LOG.trace("Create data store");
            morphia = context.getBean(Morphia.class);
            morphia.map(tClass);
            datastore= context.getBean(Datastore.class);
        }
        return datastore;
    }
}
