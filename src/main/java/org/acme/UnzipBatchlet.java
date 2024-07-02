package org.acme;


import jakarta.batch.api.Batchlet;
import jakarta.enterprise.context.Dependent;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import net.lingala.zip4j.ZipFile;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

@Dependent
@Named
public class UnzipBatchlet implements Batchlet {

    @Inject
    Logger logger;

    @ConfigProperty(name = "catalog-file", defaultValue = "src/main/resources/ikea-us-products-kaggle.zip")
    protected String zipFile;

    @Override
    public String process() throws Exception {

        logger.info("Unzipping catalog file " + zipFile);

        try(ZipFile ikea = new ZipFile(zipFile)) {
            ikea.extractAll("/tmp/ikea");
        }
        return zipFile + " extracted.";
    }

    @Override
    public void stop() {

    }
}
