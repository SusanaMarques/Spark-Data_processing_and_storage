package ggcd.tp2.segments;

import ggcd.tp2.RunnableSegment;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class GenerationSegment implements RunnableSegment {
    private static final GenerationSegment SINGLETON = new GenerationSegment();

    private GenerationSegment() {}

    public static RunnableSegment of() {
        return SINGLETON;
    }

    @Override
    public Dataset<Row> run(SparkSession sparkSession) {
        return sparkSession.sql(
                "select\n" +
                        "    perdecade.nconst, topgen.top10generation\n" +
                        "from (\n" +
                        "    select\n" +
                        "        nb2.nconst, floor(nb2.birthYear / 10) as decada\n" +
                        "    from name_basics_pq as nb2\n" +
                        ") perdecade\n" +
                        "inner join (\n" +
                        "    select\n" +
                        "        t2.decada, collect_list(t2.nconst) as top10generation\n" +
                        "    from (\n" +
                        "        select\n" +
                        "            nconst, decada, numFilmes, dense_rank()\n" +
                        "                over(partition by decada order by numFilmes desc) as rank\n" +
                        "        from (\n" +
                        "            select\n" +
                        "                tfilmes.*, floor(nb.birthYear / 10) as decada\n" +
                        "            from (\n" +
                        "                select\n" +
                        "                    tp.nconst, count(*) as numFilmes\n" +
                        "                from title_principals_pq as tp\n" +
                        "                group by tp.nconst\n" +
                        "            ) as tfilmes\n" +
                        "            inner join\n" +
                        "                name_basics_pq as nb\n" +
                        "            on nb.nconst = tfilmes.nconst\n" +
                        "            where\n" +
                        "                nb.birthYear is not null\n" +
                        "        ) \n" +
                        "    ) t2\n" +
                        "    where\n" +
                        "        t2.rank < 10\n" +
                        "    group by t2.decada\n" +
                        ") topgen\n" +
                        "on \n" +
                        "    perdecade.decada = topgen.decada"
        );
    }
}
