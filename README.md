# Spring Multithreaded Store
There is a product catalog – a list of objects with price and quantity in stock. Several customers create orders at the same time using multithreading (Runnable or ExecutorService). The warehouse is a shared resource (ConcurrentHashMap<Product, Integer>). Several warehouse workers process orders taken from a BlockingQueue. After all orders are processed, run analytics in parallel (parallelStream) to show: Total number of orders; Total profit; Top 3 best selling products;


## Project Structure
```bash
.
├── pom.xml
├── src
│   ├── main
│   │   ├── java/com/dachi/spring/multithreadedstore
│   │   │   ├── model
│   │   │   │   ├── Order.java
│   │   │   │   ├── Product.java
│   │   │   │   ├── Report.java
│   │   │   │   └── Warehouse.java
│   │   │   ├── service
│   │   │   │   ├── AnalyticsAspect.java
│   │   │   │   ├── OrderGenerator.java
│   │   │   │   └── OrderProcessor.java
│   │   │   ├── SpringMultithreadedStoreApplication.java
│   │   │   └── StoreConfig.java
│   │   └── resources/application.properties
│   └── test/java/com/dachi/spring/multithreadedstore
└── target/
```

## Compile and run
1. Compile:
```bash
mvn clean package
```
2. Run:
```bash
mvn spring-boot:run
```
Or run the built JAR:
```bash
java -jar target/*.jar
```


