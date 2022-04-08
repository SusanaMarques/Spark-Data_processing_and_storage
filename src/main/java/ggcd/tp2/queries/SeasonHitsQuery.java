package ggcd.tp2.queries;

import com.beust.jcommander.Parameters;
import com.clearspring.analytics.util.Lists;
import ggcd.tp2.RunnableQuery;
import ggcd.tp2.config.Configuration;
import java.util.List;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.sql.SparkSession;
import scala.Tuple2;

@Parameters(commandDescription = "Título melhor classificado em cada ano")
public final class SeasonHitsQuery implements RunnableQuery {
    private static final SeasonHitsQuery SINGLETON = new SeasonHitsQuery();

    private SeasonHitsQuery() {}

    public static RunnableQuery of() {
        return SINGLETON;
    }

    @Override
    public void run(final SparkSession sparkSession) {
        // Get the ratings in RDD, for simplicity
        JavaPairRDD<String, Double> ratings =
                sparkSession
                        .table(Configuration.TITLE_RATINGS_PQ)
                        .toJavaRDD()
                        .filter(r -> !r.isNullAt(0) && !r.isNullAt(1))
                        .mapToPair(
                                r ->
                                        new Tuple2<>(
                                                r.getString(0),
                                                Double.parseDouble(r.getString(1))));

        List<Tuple2<Integer, Tuple2<String, Double>>> c =
                sparkSession
                        .table(Configuration.TITLE_BASICS_PQ)
                        .toJavaRDD()
                        // TCONST (0) and Startyear (5)
                        .filter(r -> !r.isNullAt(0) && !r.isNullAt(5))
                        .mapToPair(p -> new Tuple2<>(p.getString(0), p.getInt(5)))
                        .join(ratings)
                        .mapToPair(p -> new Tuple2<>(p._2._1, new Tuple2<>(p._1, p._2._2)))
                        .groupByKey()
                        .mapToPair(
                                a -> {
                                    List<Tuple2<String, Double>> ls = Lists.newArrayList(a._2);
                                    ls.sort((b, dc) -> (-1) * Double.compare(b._2, dc._2));
                                    return new Tuple2<>(a._1, ls.get(0));
                                })
                        .sortByKey()
                        .collect();

        for (Tuple2<Integer, Tuple2<String, Double>> v : c) {
            System.out.println("ENTRY: year= " + v._1 + ", title= " + v._2);
        }
    }
}
