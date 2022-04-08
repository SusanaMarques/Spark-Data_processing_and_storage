#!/usr/bin/env bash

BASE_PATH='hdfs://'

beeline -u jdbc:hive2://localhost:10000 -e "
    create external table name_basics(
        nconst string,
        primaryName string,
        birthYear integer,
        deathYear integer,
        primaryProfession array<string>,
        knowForTitles array<string>)
        row format delimited
        fields terminated by '\t'
        collection items terminated by ','
        lines terminated by '\n'
        stored as textfile
        location '$BASE_PATH/name_basics'
        tblproperties (\"skip.header.line.count\"=\"1\");
"

beeline -u jdbc:hive2://localhost:10000 -e "
    create table name_basics_pq(
        nconst string,
        primaryName string,
        birthYear integer,
        deathYear integer,
        primaryProfession array<string>,
        knowForTitles array<string>)
        stored as parquet;
"

beeline -u jdbc:hive2://localhost:10000 -e "
    insert overwrite table name_basics_pq select * from name_basics;
"

beeline -u jdbc:hive2://localhost:10000 -e "
    create external table title_basics (
        tconst string,
        titleType string,
        primaryTitle string,
        originalTitle string,
        isAdult boolean,
        startYear integer,
        endYear integer,
        runtimeMinutes integer,
        genres array<string>)
        row format delimited
        fields terminated by '\t'
        collection items terminated by ','
        lines terminated by '\n'
        stored as textfile
        location '$BASE_PATH/title_basics'
        tblproperties (\"skip.header.line.count\"=\"1\");
"

beeline -u jdbc:hive2://localhost:10000 -e "
    create table title_basics_pq(
        tconst string,
        titleType string,
        primaryTitle string,
        originalTitle string,
        isAdult boolean,
        startYear integer,
        endYear integer,
        runtimeMinutes integer,
        genres array<string>)
        stored as parquet;
"

beeline -u jdbc:hive2://localhost:10000 -e "
    insert overwrite table title_basics_pq select * from title_basics;
"

beeline -u jdbc:hive2://localhost:10000 -e "
    create external table title_ratings (
        tconst string,
        averageRating string,
        numVotes integer)
        row format delimited
        fields terminated by '\t'
        collection items terminated by ','
        lines terminated by '\n'
        stored as textfile
        location '$BASE_PATH/title_ratings'
        tblproperties (\"skip.header.line.count\"=\"1\");
"

beeline -u jdbc:hive2://localhost:10000 -e "
    create table title_ratings_pq(
        tconst string,
        averageRating string,
        numVotes integer)
        stored as parquet;
"

beeline -u jdbc:hive2://localhost:10000 -e "
    insert overwrite table title_ratings_pq select * from title_ratings;
"

beeline -u jdbc:hive2://localhost:10000 -e "
    create external table title_principals (
        tconst string,
        ordering integer,
        nconst string,
        category string,
        job string,
        characters string)
        row format delimited
        fields terminated by '\t'
        collection items terminated by ','
        lines terminated by '\n'
        stored as textfile
        location '$BASE_PATH/title_principals'
        tblproperties (\"skip.header.line.count\"=\"1\");
"

beeline -u jdbc:hive2://localhost:10000 -e "
    create table title_principals_pq(
        tconst string,
        ordering integer,
        nconst string,
        category string,
        job string,
        characters string)
        stored as parquet;
"

beeline -u jdbc:hive2://localhost:10000 -e "
    insert overwrite table title_principals_pq select * from title_principals;
"

beeline -u jdbc:hive2://localhost:10000 -e "
    create table actor_pages_pq (
        nconst string,
        name string,
        age integer,
        titlesNumber integer,
        activityYears integer,
        averageRating float,
        top10titles array<string>,
        top10generation array<string>,
        friends array<string>)
        stored as parquet;
"

# List parquet files
hdfs dfs -ls /user/hive/warehouse
