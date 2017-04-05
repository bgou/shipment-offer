# Shipment Offer API
## Prerequisite

- Java 8
- Maven 3
- Docker 1.13

## How to build

#### Ensure Maven 3 and Java 8 are in use

```
$ mvn -version
Apache Maven 3.3.9 (bb52d8502b132ec0a5a3f4c09453c07478323dc5; 2015-11-10T08:41:47-08:00)
Maven home: /usr/local/Cellar/maven/3.3.9/libexec
Java version: 1.8.0_102, vendor: Oracle Corporation
Java home: /Library/Java/JavaVirtualMachines/jdk1.8.0_102.jdk/Contents/Home/jre
```

#### Build the jar file

```shell
# This will run the tests as well
$ mvn package
java -jar target/
```

## How to run

1. Start a docker container that runs and initializes the mariadb with table creation

```
$ docker-compose up -d
```

2. Run the jar file

```
$ java -jar target/shipment-offer-api-1.0-SNAPSHOT.jar server shipment-offer.yml
```

## How to stop

#### Stop the service

press Ctrl+C

#### Stop the SQL container

```
docker-compose down
```

