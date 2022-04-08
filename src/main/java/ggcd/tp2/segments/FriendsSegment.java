package ggcd.tp2.segments;

import ggcd.tp2.RunnableSegment;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class FriendsSegment implements RunnableSegment {
    private static final FriendsSegment SINGLETON = new FriendsSegment();

    private FriendsSegment() {}

    public static RunnableSegment of() {
        return SINGLETON;
    }

    @Override
    public Dataset<Row> run(SparkSession sparkSession) {
        return sparkSession.sql(
                "select\n" +
                        "    tp2.nconst, collect_set(tf.nconst) as friends\n" +
                        "from \n" +
                        "    title_principals_pq as tp2\n" +
                        "inner join (\n" +
                        "    select\n" +
                        "        tp.tconst, tp.nconst\n" +
                        "    from title_principals_pq as tp\n" +
                        ") tf\n" +
                        "on tp2.tconst = tf.tconst\n" +
                        "where\n" +
                        "    tp2.nconst != tf.nconst\n" +
                        "group by tp2.nconst"
        );
    }
}
