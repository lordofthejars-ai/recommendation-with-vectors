package org.acme;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.model.embedding.EmbeddingModel;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;


@ApplicationScoped
public class VectorProcessor {

    @Inject
    Logger logger;

    @Inject
    EmbeddingModel embeddingModel;

    private int maxSize = 0;

    public float[] generateVector(IkeaProduct ikeaProduct) {
        return generateVector(ikeaProduct.productName(), ikeaProduct.description());
    }

    public float[] generateVector(Product product) {
        return generateVector(product.name, product.description);
    }

     protected float[] generateVector(String productName, String description) {

        String sb = productName + " " + description;

        Embedding content = embeddingModel.embed(sb).content();

        int currentDimension = content.dimension();
        if (currentDimension > maxSize) {
            maxSize = currentDimension;
            logger.info("Current maxsize of vector " + maxSize);
        }

        return content.vector();
    }

}
