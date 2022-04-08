package ggcd.tp2.segments;

import ggcd.tp2.RunnableQuery;
import ggcd.tp2.RunnableSegment;
import ggcd.tp2.queries.SeasonHitsQuery;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class HitsSegment implements RunnableSegment {
    private static final HitsSegment SINGLETON = new HitsSegment();

    private HitsSegment() {}

    public static RunnableSegment of() {
        return SINGLETON;
    }

    @Override
    public Dataset<Row> run(SparkSession sparkSession) {
        return sparkSession.sql(
                "select\n" +
                        "    t2.nconst, collect_list(t2.tconst) as top10titles\n" +
                        "from (\n" +
                        "    select \n" +
                        "        nconst, tconst, averageRating, dense_rank()\n" +
                        "            over(partition by nconst order by averageRating desc) as rank\n" +
                        "    from (\n" +
                        "        select tp.nconst, tr.tconst, tr.averageRating\n" +
                        "        from title_principals_pq as tp \n" +
                        "        inner join title_ratings_pq as tr \n" +
                        "            on tr.tconst == tp.tconst\n" +
                        "    ) as t1\n" +
                        ") t2\n" +
                        "where \n" +
                        "    t2.rank < 10 \n" +
                        "group by t2.nconst"
        );
    }
}
