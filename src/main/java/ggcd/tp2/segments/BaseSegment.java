package ggcd.tp2.segments;

import ggcd.tp2.RunnableSegment;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;


public final class BaseSegment implements RunnableSegment {
    private static final BaseSegment SINGLETON = new BaseSegment();

    private BaseSegment() { }

    public static RunnableSegment of() {
        return SINGLETON;
    }

    @Override
    public Dataset<Row> run(SparkSession sparkSession) {
        return sparkSession.sql(
                "select \n" +
                        "    first(nb.nconst) as nconst,\n" +
                        "    first(nb.primaryName) as name, \n" +
                        "    nvl(first(nb.deathYear), year(current_date())) - first(nb.birthYear) as age, \n" +
                        "    count(distinct tp.tconst) as titlesNumber, \n" +
                        "    greatest(max(tb.endYear), max(tb.startYear)) - min(tb.startYear) as activityYears, \n" +
                        "    avg(tr.averageRating) as averageRating \n" +
                        "from name_basics_pq as nb \n" +
                        "inner join title_principals_pq as tp \n" +
                        "on nb.nconst == tp.nconst \n" +
                        "inner join title_basics_pq as tb \n" +
                        "on tb.tconst == tp.tconst \n" +
                        "inner join title_ratings_pq as tr\n" +
                        "on tr.tconst == tb.tconst \n" +
                        "group by nb.nconst"
        );
    }

}