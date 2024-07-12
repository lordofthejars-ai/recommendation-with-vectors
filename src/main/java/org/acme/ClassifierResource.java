package org.acme;

import dev.langchain4j.classification.TextClassifier;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

import jakarta.ws.rs.QueryParam;

import java.util.List;

@Path("/classify")
public class ClassifierResource {


    @Inject
    TextClassifier<EmbeddingModelCreator.SentimentCategory> classifier;


    @GET
    public String emotion(@QueryParam("sentence") String sentence) {
        List<EmbeddingModelCreator.SentimentCategory> classify = classifier.classify(sentence);
        return classify.getFirst().name();
    }
}
