package org.acme;

import jakarta.batch.api.chunk.ItemReader;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.stream.JsonParser;
import jakarta.transaction.Transactional;
import org.jboss.logging.Logger;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;

@Dependent
@Named
public class IkeaItemReader implements ItemReader {

    @Inject
    Logger logger;

    private JsonParser parser;
    private InputStream in;

    @Override
    public void open(Serializable checkpoint) throws Exception {
        in = new FileInputStream("/tmp/ikea/ikea_sample_file.json");
        parser = Json.createParser(in);
        searchItems();
    }

    @Override
    public void close() throws Exception {
        if (in != null) {
            in.close();
        }
    }

    @Override
    public Object readItem() {
        JsonParser.Event event = parser.next();
        if (event == JsonParser.Event.START_OBJECT) {
            final JsonObject object = parser.getObject();

            String productName = object.getString("product_title");

            logger.info("Reading item " + productName);

            String rawDescription = object.getString("raw_product_details");
            double price = Double.parseDouble(object.getString("product_price"));
            return new IkeaProduct(productName, rawDescription, price, new float[0]);
        }
        return null;
    }

    private void searchItems() {
        parser.next();
    }

    @Override
    public Serializable checkpointInfo() throws Exception {
        return null;
    }
}
