package org.acme;

import ai.djl.Application;
import ai.djl.MalformedModelException;
import ai.djl.inference.Predictor;
import ai.djl.ndarray.NDArrays;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.NDManager;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelNotFoundException;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.training.util.ProgressBar;
import ai.djl.translate.NoBatchifyTranslator;
import ai.djl.translate.TranslateException;
import ai.djl.translate.TranslatorContext;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.io.IOException;
import java.util.Arrays;

@Path("/hello")
public class GreetingResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public void hello() throws ModelNotFoundException, MalformedModelException, IOException, TranslateException {
        String modelUrl =
                "https://storage.googleapis.com/tfhub-modules/google/universal-sentence-encoder/4.tar.gz";

        Criteria<String, float[]> criteria =
                Criteria.builder()
                        .optApplication(Application.NLP.TEXT_EMBEDDING)
                        .setTypes(String.class, float[].class)
                        .optModelUrls(modelUrl)
                        .optTranslator(new UniversalSentenceEncoderTranslator())
                        .optEngine("TensorFlow")
                        .optProgress(new ProgressBar())
                        .build();
        try (ZooModel<String, float[]> model = criteria.loadModel();
             Predictor<String, float[]> predictor = model.newPredictor()) {
            float[] f1 =  predictor.predict("How old are you?");
            float[] f2 =  predictor.predict("What is your age?");
            float[] f3 =  predictor.predict("How are you?");

            System.out.println("num " + f1.length + "  " + Arrays.toString(f1));
            System.out.println(Arrays.toString(f2));
            System.out.println(Arrays.toString(f3));
        }
    }

    private static final class UniversalSentenceEncoderTranslator implements NoBatchifyTranslator<String, float[]> {

        @Override
        public float[] processOutput(TranslatorContext ctx, NDList ndList) {
            // Since we set a single String, it returns a single float[]
            return ndList.getFirst().toFloatArray();
        }

        @Override
        public NDList processInput(TranslatorContext ctx, String s) {

            NDManager manager = ctx.getNDManager();
            NDList inputsList = new NDList();
            inputsList.add(manager.create(s));

            return new NDList(NDArrays.stack(inputsList));
        }
    }
}
