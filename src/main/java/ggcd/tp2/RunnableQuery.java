package ggcd.tp2;

import org.apache.spark.sql.SparkSession;

public interface RunnableQuery {
    void run(SparkSession sparkSession);
}
