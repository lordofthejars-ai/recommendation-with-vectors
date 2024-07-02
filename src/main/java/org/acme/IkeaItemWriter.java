package org.acme;

import jakarta.batch.api.chunk.AbstractItemWriter;

import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.transaction.Transactional;
import org.jboss.logging.Logger;

import java.util.List;

@Dependent
@Named
@Transactional
public class IkeaItemWriter extends AbstractItemWriter {

    @Inject
    Logger logger;

    @Override
    public void writeItems(List<Object> items) {

        logger.info("Writing " + items.size() + " elements");

        items.stream()
                .map(Product.class::cast)
                .forEach((Product p) -> p.persist());
    }
}
