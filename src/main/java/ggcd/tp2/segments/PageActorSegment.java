package ggcd.tp2.segments;

import ggcd.tp2.RunnableSegment;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class PageActorSegment implements RunnableSegment {
    private static final PageActorSegment SINGLETON = new PageActorSegment();

    private PageActorSegment() {}

    public static RunnableSegment of() {
        return SINGLETON;
    }

    @Override
    public Dataset<Row> run(SparkSession sparkSession) {
        String commonColumn = "nconst";

        Dataset<Row> base = BaseSegment.of().run(sparkSession);
        Dataset<Row> hits = HitsSegment.of().run(sparkSession);
        Dataset<Row> generation = GenerationSegment.of().run(sparkSession);
        Dataset<Row> friends = FriendsSegment.of().run(sparkSession);

        return base
                .join(hits, commonColumn)
                .join(generation, commonColumn)
                .join(friends, commonColumn);
    }
}
