package ggcd.tp2;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public interface RunnableSegment {
    Dataset<Row> run(SparkSession sparkSession);
}
