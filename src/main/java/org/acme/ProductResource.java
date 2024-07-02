package org.acme;

import jakarta.batch.operations.JobOperator;
import jakarta.batch.runtime.BatchRuntime;
import jakarta.batch.runtime.JobExecution;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;

import java.net.URI;
import java.util.List;
import java.util.Properties;

import org.jboss.logging.Logger;

@Path("/product")
public class ProductResource {

    @Inject
    Logger log;

    @Inject
    VectorProcessor vectorProcessor;

    @POST
    @Transactional
    public jakarta.ws.rs.core.Response addProduct(Product product) {
        product.embedded = vectorProcessor.generateVector(product);

        product.persist();
        return jakarta.ws.rs.core.Response.created(URI.create("/vector/" + product.id)).build();
    }

    @GET
    @Path("/{id}")
    public Product findProduct(@PathParam("id") Long id) {
        return Product.findById(id);
    }

    @GET
    @Path("/recommend/{id}")
    public List<Product> recommendProducts(@PathParam("id") Long id) {
        Product referenceProduct = Product.findById(id);
        return Product.suggestProducts(referenceProduct.embedded);
    }

    @GET
    @Path("/generate")
    public jakarta.ws.rs.core.Response generateImportFile() {
        JobOperator jobOperator = BatchRuntime.getJobOperator();
        Properties jobParameters = new Properties();
        long executionId = jobOperator.start("ikea", jobParameters);

        JobExecution jobExecution = jobOperator.getJobExecution(executionId);
        return jakarta.ws.rs.core.Response.ok(new JobData(executionId, jobExecution.getBatchStatus().toString())).build();
    }

    public record JobData(long executionId, String status) {}

}
