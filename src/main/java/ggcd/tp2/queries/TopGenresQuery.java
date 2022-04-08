package ggcd.tp2.queries;

import com.beust.jcommander.Parameters;
import com.clearspring.analytics.util.Lists;
import ggcd.tp2.RunnableQuery;
import ggcd.tp2.config.Configuration;
import java.util.List;
import org.apache.spark.sql.SparkSession;
import scala.Tuple2;

@Parameters(commandDescription = "Género mais comum em cada década")
public final class TopGenresQuery implements RunnableQuery {
    private static final TopGenresQuery SINGLETON = new TopGenresQuery();

    private TopGenresQuery() {}

    public static RunnableQuery of() {
        return SINGLETON;
    }

    @Override
    public void run(final SparkSession sparkSession) {
        List<Tuple2<Integer, Tuple2<String, Integer>>> c =
                sparkSession
                        .table(Configuration.TITLE_BASICS_PQ)
                        .toJavaRDD()
                        .filter(r -> !r.isNullAt(5) && !r.isNullAt(8))
                        .flatMapToPair(
                                r ->
                                        r.getList(8).stream()
                                                .map(
                                                        p ->
                                                                new Tuple2<>(
                                                                        new Tuple2<>(
                                                                                r.getInt(5) / 10,
                                                                                (String) p),
                                                                        1))
                                                .iterator())
                        .reduceByKey(Integer::sum)
                        .mapToPair(a -> new Tuple2<>(a._1._1, new Tuple2<>(a._1._2, a._2)))
                        .groupByKey()
                        .mapToPair(
                                a -> {
                                    List<Tuple2<String, Integer>> ls = Lists.newArrayList(a._2);
                                    ls.sort((b, dc) -> (-1) * Long.compare(b._2, dc._2));
                                    return new Tuple2<>(a._1, ls.get(0));
                                })
                        .sortByKey()
                        .collect();

        for (Tuple2<Integer, Tuple2<String, Integer>> v : c) {
            System.out.println("ENTRY: decade= " + v._1 + ", topGenre= " + v._2);
        }
    }
}
