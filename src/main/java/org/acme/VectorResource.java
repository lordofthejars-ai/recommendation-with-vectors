package org.acme;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.output.Response;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.io.IOException;
import java.util.Arrays;

@Path("/vector")
public class VectorResource {

    @Inject
    EmbeddingModel embeddingModel;

    @GET
    @Path("/calculate")
    @Produces(MediaType.TEXT_PLAIN)
    public void hello() throws IOException {


        final Response<Embedding> old = embeddingModel.embed("How old are you?");
        final Response<Embedding> age = embeddingModel.embed("What is your age?");
        final Response<Embedding> howare = embeddingModel.embed("How are you?");

        System.out.println("******* How Old are you?  *******");
        final Embedding oldContent = old.content();

        System.out.println(Arrays.toString(oldContent.vector()));
        System.out.println("*********************");

        System.out.println("******* What is your age? *******");
        final Embedding ageContent = age.content();

        System.out.println(Arrays.toString(ageContent.vector()));
        System.out.println("*********************");

        System.out.println("**************");
        final Embedding howareContent = howare.content();

        System.out.println(Arrays.toString(howareContent.vector()));
        System.out.println("*********************");

        System.out.println("How old are you? vs What is your age?");
        final double sim1 = cosineSimilarity(oldContent.vector(), ageContent.vector());
        System.out.println(sim1);

        System.out.println("How old are you? vs How are you?");
        final double sim2 = cosineSimilarity(oldContent.vector(), howareContent.vector());
        System.out.println(sim2);

        System.out.println("What is your age? vs How are you?");
        final double sim3 = cosineSimilarity(oldContent.vector(), howareContent.vector());
        System.out.println(sim3);



    }


    public double cosineSimilarity(float[] v1, float[] v2) {
        double dotProd = 0.0;
        double v1SqrSum = 0.0;
        double v2SqrSum = 0.0;
        for (int i = 0; i < v1.length; i++) {
            dotProd += v1[i] * v2[i];
            v1SqrSum += Math.pow(v1[i], 2);
            v2SqrSum += Math.pow(v2[i], 2);
        }
        return dotProd / (Math.sqrt(v1SqrSum) * Math.sqrt(v2SqrSum));
    }

    public double dotProduct(float[] v1, float[] v2) {
        float dotProd = 0f;
        for (int i = 0; i < v1.length; i++) {
            dotProd += v1[i] * v2[i];
        }
        return dotProd;
    }
}
