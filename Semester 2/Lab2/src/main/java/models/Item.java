package models;

import org.bson.Document;

public class Item {
    private final String name;
    private final int value;

    public Item(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Item(Document document) {
        this(document.getString("name"), document.getInteger("value"));
    }

    @Override
    public String toString() {
        return String.format("item: %s, price: %d", name, value);
    }

    public Document toDocument() {
        return new Document()
                .append("name", name)
                .append("value", value);
    }

    public int getValue() {
        return value;
    }
}
