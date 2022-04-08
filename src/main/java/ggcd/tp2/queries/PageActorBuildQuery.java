package ggcd.tp2.queries;

import com.beust.jcommander.Parameters;
import com.clearspring.analytics.util.Lists;
import ggcd.tp2.RunnableQuery;
import ggcd.tp2.config.Configuration;
import ggcd.tp2.segments.PageActorSegment;
import org.apache.spark.sql.SparkSession;
import scala.Tuple2;

import java.util.List;

import static ggcd.tp2.config.Configuration.ACTOR_PAGES_PQ;

@Parameters(commandDescription = "Constroi a pagina por ator")
public final class PageActorBuildQuery implements RunnableQuery {
    private static final PageActorBuildQuery SINGLETON = new PageActorBuildQuery();

    private PageActorBuildQuery() {}

    public static RunnableQuery of() {
        return SINGLETON;
    }

    @Override
    public void run(final SparkSession sparkSession) {
        PageActorSegment.of().run(sparkSession)
                .write()
                .insertInto(ACTOR_PAGES_PQ);
    }
}
