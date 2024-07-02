package org.acme;

import jakarta.batch.api.chunk.ItemProcessor;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.jboss.logging.Logger;

@Dependent
@Named
public class IkeaItemProcessor implements ItemProcessor {

    @Inject
    VectorProcessor vectorProcessor;

    @Inject
    HtmlCleaner htmlCleaner;

    @Inject
    Logger logger;

    @Override
    public Object processItem(Object item) {

        IkeaProduct ikeaProduct = (IkeaProduct) item;

        logger.info("Processing Item " + ikeaProduct.productName());
        String cleanDescription = htmlCleaner.clean(ikeaProduct.description());
        float[] vector = vectorProcessor.generateVector(ikeaProduct.productName(), cleanDescription);
        return new Product(ikeaProduct.productName(), cleanDescription, ikeaProduct.price(), vector);
    }

}
