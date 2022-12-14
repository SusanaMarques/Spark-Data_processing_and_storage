[bruno]: https://github.com/BrunoXBSantos
[bruno-pic]: https://github.com/BrunoXBSantos.png?size=120
[nelson]: https://github.com/nelsonmestevao
[nelson-pic]: https://github.com/nelsonmestevao.png?size=120
[rui]: https://github.com/Syrayse
[rui-pic]: https://github.com/Syrayse.png?size=120
[susana]: https://github.com/SusanaMarques
[susana-pic]: https://github.com/SusanaMarques.png?size=120

<div align="center">
lll
# Data processing and storage

[Geeting Started](#rocket-getting-started)
|
[Development](#hammer-development)
|
[Tools](#hammer_and_wrench-tools)
|
[Team](#busts_in_silhouette-team)

</div>

The practical work consists of carrying out the experimental evaluation of data
storage and processing tasks using Hive Metastore, Avro + Parquet and Spark
using [IMDb public datasets](https://www.imdb.com/interfaces/).

## :rocket: Getting Started

These instructions will get you a copy of the project up and running on your
local machine for development and testing purposes..

### :inbox_tray: Prerequisites

The following software is required to be installed on your system:

- [Java SDK 8+](https://openjdk.java.net/)
- [Maven](https://maven.apache.org/maven-features.html)
- [GCloud CLI](https://cloud.google.com/sdk/docs/install)
- [Docker](https://docs.docker.com/get-docker/)
- [Docker Compose](https://docs.docker.com/compose/install/)

### :gear: Setup

Create instances of hive and spark (using docker containers, based on big-data-europa with resolved incompatibilities).
Configures the spark network by connecting it to Hive and runs the containers (hive and spark) in the background.

```
bin/setup
```

Uploads the .csv files to Hive's HDFS. Creates the tables and converts the data to the parquet format. If the tables or
files already exist there is no problem.

```
docker-hive/setup
```

### :hammer: Development

Run the project.

```
bin/run query <query>
```

Format the code accordingly to common [guide lines](https://github.com/google/google-java-format).

```
bin/format
```

Lint your code with _checkstyle_.

```
bin/lint
```

### :hammer_and_wrench: Tools

The recommended Integrated Development Environment (IDE) is IntelliJ IDEA.

## :busts_in_silhouette: Team

| [![Bruno][bruno-pic]][bruno] | [![Nelson][nelson-pic]][nelson] | [![Rui][rui-pic]][rui] | [![Susana][susana-pic]][susana] |
| :--------------------------: | :-----------------------------: | :--------------------: | :-----------------------------: |
|    [Bruno Santos][bruno]     |    [Nelson Estevão][nelson]     |    [Rui Reis][rui]     |    [Susana Marques][susana]     |
