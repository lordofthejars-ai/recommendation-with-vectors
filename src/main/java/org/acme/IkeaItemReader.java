package org.acme;

import jakarta.batch.api.chunk.ItemReader;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Named;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.stream.JsonParser;
import jakarta.transaction.Transactional;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;

@Dependent
@Named
@Transactional
public class IkeaItemReader implements ItemReader {

    private JsonParser parser;
    private InputStream in;

    @Override
    public void open(Serializable checkpoint) throws Exception {
        in = new FileInputStream("/tmp/ikea/ikea_sample_file.json");
        parser = Json.createParser(in);
    }

    @Override
    public void close() throws Exception {
        if (in != null) {
            in.close();
        }
    }

    @Override
    public Object readItem() throws Exception {
        JsonParser.Event event = parser.next();
        if (event == JsonParser.Event.START_OBJECT) {
            final JsonObject object = parser.getObject();
        }
        return null;
    }

    @Override
    public Serializable checkpointInfo() throws Exception {
        return null;
    }
}
