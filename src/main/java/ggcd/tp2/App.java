package ggcd.tp2;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import ggcd.tp2.queries.PageActorBuildQuery;
import ggcd.tp2.segments.*;
import ggcd.tp2.queries.SeasonHitsQuery;
import ggcd.tp2.queries.Top10ActorsQuery;
import ggcd.tp2.queries.TopGenresQuery;
import java.util.HashMap;
import java.util.Map;

import org.apache.spark.sql.SparkSession;

public final class App {
    private static final String PROGRAM_NAME = "tp2";

    @Parameter(
            names = {"-h", "--help"},
            help = true,
            description = "Displays help information")
    private boolean help = false;

    @Parameter(
            names = {"--metastore"},
            description = "URI for Hive Metastore")
    private String metastore = "thrift://hive-metastore:9083" ;

    private static final Map<String, RunnableQuery> QUERIES = new HashMap<>();
    private static final Map<String, RunnableSegment> SEGMENTS = new HashMap<>();

    // Add all available queries
    static {
        QUERIES.put("top-genre", TopGenresQuery.of());
        QUERIES.put("season-hits", SeasonHitsQuery.of());
        QUERIES.put("top-actors", Top10ActorsQuery.of());
        QUERIES.put("page-actor-build", PageActorBuildQuery.of());

        SEGMENTS.put("base", BaseSegment.of());
        SEGMENTS.put("hits", HitsSegment.of());
        SEGMENTS.put("generation", GenerationSegment.of());
        SEGMENTS.put("friends", FriendsSegment.of());
        SEGMENTS.put("page-actor", PageActorSegment.of());
    }

    private App() {}

    public static void main(final String[] args) {
        new App().start(args);
    }

    public void start(final String[] args) {
        String command = this.parseArguments(args);

        SparkSession sparkSession = SparkSession.builder()
                .appName("Hive tables and RDD example")
                .master("local")
                .config("hive.metastore.uris", this.metastore)
                .enableHiveSupport()
                .getOrCreate();

        try {
            QUERIES.get(command).run(sparkSession);
        } catch (NullPointerException ex) {
            SEGMENTS.get(command).run(sparkSession).show();
        }
    }

    public String parseArguments(final String[] args) {
        JCommander commands = new JCommander(this);
        commands.setProgramName(PROGRAM_NAME);

        QUERIES.forEach(commands::addCommand);
        SEGMENTS.forEach(commands::addCommand);

        try {
            commands.parse(args);

            if (this.help) {
                commands.usage();
                System.exit(0);
            }

            return commands.getParsedCommand();
        } catch (ParameterException exception) {
            System.err.println(exception.getMessage());
            commands.usage();
            System.exit(1);
        }

        return null;
    }
}
