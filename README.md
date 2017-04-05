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

# Response to questions
- What persistence solution did you choose and why?
  - I chose SQL because the data seems highly relational. And having a relational database also allows for easier reporting for the business (compared to a NoSQL database).
- What are some other ways you might score a driver?
  - Shipper/Receiver feedback review - possibly a review system
  - Timeliness of pickup and delivery
  - Responsiveness to shipper/receiver inquiries
  - and many others
- What do you think are the best features to implement? When and why?
  - **Push notification** (via app, sms, or email): when a shipment becomes available in a region, all of the available drivers in the region will get notified. This will continue to drive user engagement in our service, so I'd do this up front.
  - **Real-time dashboard** would be super awesome (I know Meteor supports this with NoSQL) where users could see updated shipments without refreshing the web browser. But that's more on the front end though.
  - **Bidding platform**: allows drivers to bid on the shipments, and allow shippers to set a most price they're willing to pay (ebay style). 
- What would you add/change for a real-world v1 of this system?
  - Ensure atomic transactions (so we don't offer the same shipment twice, etc)
  - Caching. The logic to sort the drivers by number of previous accepted offers currently always do a SQL query and it is time consuming. The sorting already takes `O(N log N)` time, if we have millions more shipments it's not feasible to query all shipment data for each new shipment. For the caching we want something that can instantly tell how many shipments a driver has had in the past X number of days, and use that value to do sorting.
  - More tests to cover each resource.
  - Convert the business logic to a core processor package, and resources will use the processors to perform required business logic. This will separate handling the http request and the business logic, so that if we want to switch to a queue-based system we don't have to change a whole lot.
  - More logging with better logging solution, probably ELK.
  - Metrics. I have added a simple health check, but we should be able to do better than that.