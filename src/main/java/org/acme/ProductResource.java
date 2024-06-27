package org.acme;


import com.fasterxml.jackson.databind.ObjectMapper;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.model.embedding.AllMiniLmL6V2QuantizedEmbeddingModelFactory;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.output.Response;
import eu.hansolo.fx.charts.BubbleGridChart;
import eu.hansolo.fx.charts.BubbleGridChartBuilder;
import eu.hansolo.fx.charts.ChartType;
import eu.hansolo.fx.charts.MatrixPane;
import eu.hansolo.fx.charts.data.BubbleGridChartItem;
import eu.hansolo.fx.charts.data.BubbleGridChartItemBuilder;
import eu.hansolo.fx.charts.data.ChartItem;
import eu.hansolo.fx.charts.data.ChartItemBuilder;
import eu.hansolo.fx.charts.data.MatrixChartItem;
import eu.hansolo.fx.charts.series.MatrixItemSeries;
import eu.hansolo.fx.charts.tools.Helper;
import io.quarkiverse.fx.RunOnFxThread;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import javafx.embed.swing.SwingFXUtils;
import javax.imageio.ImageIO;

@Path("/vector")
public class ProductResource {



    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String generateImportFile() {
        //mapper.readTree()

        return null;
    }


    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public void hello() throws IOException {

        AllMiniLmL6V2QuantizedEmbeddingModelFactory f = new AllMiniLmL6V2QuantizedEmbeddingModelFactory();
        final EmbeddingModel embeddingModel = f.create();

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

        System.out.println("******* Welcome *******");
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

        createHeatMap(sim1, sim2, sim3);

    }

    @RunOnFxThread
    void createHeatMap(double oldVsAge, double oldVsAre, double ageVsAre) throws IOException {
        ChartItem howOldY = ChartItemBuilder.create().name("How Old Are you?").index(0).fill(Color.BLUE).build();
        ChartItem whaAgeY = ChartItemBuilder.create().name("What is your Age?").index(1).fill(Color.ORANGE).build();
        ChartItem howAreY = ChartItemBuilder.create().name("How are you?").index(2).fill(Color.GREEN).build();

        ChartItem howOldX = ChartItemBuilder.create().name("How Old Are you?").index(0).fill(Color.BLUE).build();
        ChartItem whaAgeX = ChartItemBuilder.create().name("What is your Age?").index(1).fill(Color.ORANGE).build();
        ChartItem howAreX = ChartItemBuilder.create().name("How are you?").index(2).fill(Color.GREEN).build();

        BubbleGridChartItem howOldItem1  = BubbleGridChartItemBuilder.create().categoryXItem(howOldX).categoryYItem(howOldY).value(1d).fill(Color.BLUE).build();
        BubbleGridChartItem howOldItem2  = BubbleGridChartItemBuilder.create().categoryXItem(howOldX).categoryYItem(whaAgeY).value(oldVsAge).fill(Color.BLUE).build();
        BubbleGridChartItem howOldItem3  = BubbleGridChartItemBuilder.create().categoryXItem(howOldX).categoryYItem(howAreY).value(oldVsAre).fill(Color.BLUE).build();

        BubbleGridChartItem whatAgeItem1  = BubbleGridChartItemBuilder.create().categoryXItem(whaAgeX).categoryYItem(howOldY).value(oldVsAge).fill(Color.ORANGE).build();
        BubbleGridChartItem whatAgeItem2  = BubbleGridChartItemBuilder.create().categoryXItem(whaAgeX).categoryYItem(whaAgeY).value(1d).fill(Color.ORANGE).build();
        BubbleGridChartItem whatAgeItem3  = BubbleGridChartItemBuilder.create().categoryXItem(whaAgeX).categoryYItem(howOldY).value(ageVsAre).fill(Color.ORANGE).build();

        BubbleGridChartItem howAreItem1  = BubbleGridChartItemBuilder.create().categoryXItem(howAreX).categoryYItem(howOldY).value(oldVsAre).fill(Color.GREEN).build();
        BubbleGridChartItem howAreItem2  = BubbleGridChartItemBuilder.create().categoryXItem(howAreX).categoryYItem(whaAgeY).value(ageVsAre).fill(Color.GREEN).build();
        BubbleGridChartItem howAreItem3  = BubbleGridChartItemBuilder.create().categoryXItem(howAreX).categoryYItem(howAreY).value(1d).fill(Color.GREEN).build();

        List<BubbleGridChartItem> chartItems = List.of(howOldItem1, howOldItem2, howOldItem3, whatAgeItem1, whatAgeItem2, whatAgeItem3, howAreItem1, howAreItem2, howAreItem3);

        BubbleGridChart bubbleGridChart = BubbleGridChartBuilder.create()
            .chartBackground(Color.WHITE)
            .textColor(Color.BLACK)
            .gridColor(Color.rgb(0, 0, 0, 0.1))
            .showGrid(true)
            .showValues(true)
            .showPercentage(false)
            .items(chartItems)
            .useXCategoryFill()
            .autoBubbleTextColor(true)
            .useGradientFill(false)
            .build();


        StackPane pane = new StackPane(bubbleGridChart);
        pane.setPadding(new Insets(10));

        Scene scene = new Scene(pane);

        WritableImage writableImage = new WritableImage(800, 640);
        scene.snapshot(writableImage);
        RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
        ImageIO.write(renderedImage, "png", new File("/tmp/heat.png"));


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
