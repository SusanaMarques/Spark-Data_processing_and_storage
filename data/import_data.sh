#!/usr/bin/env bash

curl https://datasets.imdbws.com/name.basics.tsv.gz | gunzip | hdfs dfs -put - /name_basics/name.basics.tsv
curl https://datasets.imdbws.com/title.basics.tsv.gz | gunzip | hdfs dfs -put - /title_basics/title.basics.tsv
curl https://datasets.imdbws.com/title.principals.tsv.gz | gunzip | hdfs dfs -put - /title_principals/title.principals.tsv
curl https://datasets.imdbws.com/title.ratings.tsv.gz | gunzip | hdfs dfs -put - /title_ratings/title.ratings.tsv
