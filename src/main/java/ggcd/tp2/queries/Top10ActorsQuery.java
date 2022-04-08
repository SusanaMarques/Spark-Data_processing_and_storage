package ggcd.tp2.queries;

import com.beust.jcommander.Parameters;
import ggcd.tp2.RunnableQuery;
import ggcd.tp2.config.Configuration;
import java.util.List;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import scala.Tuple2;

@Parameters(commandDescription = "Top 10 dos atores que participaram em mais títulos diferentes")
public final class Top10ActorsQuery implements RunnableQuery {
    private static final Top10ActorsQuery SINGLETON = new Top10ActorsQuery();

    private Top10ActorsQuery() {}

    public static RunnableQuery of() {
        return SINGLETON;
    }

    public void run(final SparkSession sparkSession) {
        JavaRDD<Row> mainData = sparkSession.table(Configuration.TITLE_PRINCIPALS_PQ).toJavaRDD();
        List<Tuple2<String, Integer>> c =
                mainData.filter(r -> !r.isNullAt(0) && !r.isNullAt(2) && !r.isNullAt(3))
                        // only maintain actors or actresses
                        .filter(
                                r ->
                                        r.getString(3).equals("actor")
                                                || r.getString(3).equals("actress"))
                        .mapToPair(
                                r -> new Tuple2<>(new Tuple2<>(r.getString(2), r.getString(0)), 1))
                        .distinct()
                        .mapToPair(p -> new Tuple2<>(p._1._1, p._2))
                        .reduceByKey(Integer::sum)
                        .map(t -> t)
                        .sortBy(a -> a._2, false, mainData.partitions().size())
                        .take(10);

        for (Tuple2<String, Integer> v : c) {
            System.out.println("ENTRY: actor= " + v._1 + ", participations= " + v._2);
        }
    }
}
