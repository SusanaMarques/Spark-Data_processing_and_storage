FROM bde2020/spark-base:3.1.1-hadoop3.2
COPY target/ggcd.tp2-0.1.0.jar /
ENTRYPOINT ["/spark/bin/spark-submit", "--class", "ggcd.tp2.App", "--master", "spark://spark-master:7077", "/ggcd.tp2-0.1.0.jar"]
