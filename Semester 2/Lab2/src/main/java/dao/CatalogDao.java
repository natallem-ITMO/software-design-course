package dao;

import com.mongodb.client.model.Filters;
import com.mongodb.rx.client.MongoClient;
import com.mongodb.rx.client.MongoCollection;
import com.mongodb.rx.client.Success;
import models.Item;
import org.bson.Document;
import rx.Observable;
import util.Converter;
import util.Currency;

public class CatalogDao {
    MongoCollection<Document> itemsCollection;
    static final String COLLECTION_NAME = "items";

    private final Converter converter;

    public CatalogDao(MongoClient client, Converter converter) {
        itemsCollection = client.getDatabase("catalog").getCollection(COLLECTION_NAME);
        this.converter = converter;
    }

    public Observable<Success> addItem(String name, int value, String currencyString) {
        return itemsCollection.find(Filters.eq("name", name))
                .toObservable()
                .isEmpty()
                .flatMap(isEmpty -> {
                    if (isEmpty) {
                        Currency currency = Currency.valueOf(currencyString);
                        int unifiedValue = converter.convertToUnifiedValue(value, currency);
                        return itemsCollection.insertOne(new Item(name, unifiedValue).toDocument());
                    } else {
                        return Observable.error(new IllegalArgumentException("Item with name '" + name + "' is already exists"));
                    }
                });
    }

    public Observable<Item> getItems() {
        return itemsCollection.find()
                .toObservable()
                .map(Item::new);

    }

    public Item convertItemTo(Item item, Currency currency) {
        return new Item(item.getName(), converter.convertToCurrencyValue(item.getValue(), currency));
    }
}
