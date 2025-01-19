Words Processor

This repository contains the implementation of a system that utilizes the Lorem Ipsum API to generate dummy text data. The project includes two applications:

1. **Processing Application- words-processing module**
2. **Repository Application- history-reports module**

## Project Overview

### Processing Application

The Processing Application is a Java-based server that exposes a REST API endpoint to process requests for dummy text data. It also integrates with Kafka to produce processed data messages.

#### API Endpoint

- **URL:** `/betvictor/text`
- **Method:** `HTTP GET`
- **Accepted Parameters:**
    - `p` (integer): Indicates the maximum number of paragraphs (must be greater than 0).
    - `l` (string): Indicates the length of each paragraph. Possible values: `short`, `medium`, `long`, `verylong`.
- **Response Format (JSON):**
  ```json
  {
    "freq_word": "<most_frequent_word>",
    "avg_paragraph_size": "<avg_paragraph_size>",
    "avg_paragraph_processing_time": "<avg_paragraph_processing_time>",
    "total_processing_time": "<total_processing_time>"
  }
  ```
    - `<most_frequent_word>`: The most frequent word in the paragraphs.
    - `<avg_paragraph_size>`: Average size of a paragraph.
    - `<avg_paragraph_processing_time>`: Average time spent analyzing a paragraph.
    - `<total_processing_time>`: Total processing time to generate the response.

#### Functionality

1. Processes the request by calling the Lorem Ipsum API.
2. Computes metrics including:
    - Most frequent word.
    - Average paragraph size.
    - Average processing time per paragraph.
    - Total processing time.
3. Produces the computed result to a Kafka topic (`words.processed`).

#### Additional Features

- Kafka messages are produced in the same order as they are sent.
- Configurable Kafka broker and topic settings.

### Repository Application

The Repository Application is a consumer service that listens to the Kafka topic `words.processed` and stores the consumed data in a database. It also provides an endpoint to retrieve the stored results.

#### API Endpoint

- **URL:** `/betvictor/history`
- **Method:** `HTTP GET`
- **Response Format:**
    - Displays the last 10 computation results from the database.

#### Features

1. Consumes messages from Kafka and stores them in a database.
2. Provides configurable Kafka settings, including:
    - Kafka broker.
    - Number of concurrent Kafka consumers.

## Key Components

### Processing Application

- **Controllers:**
    - `WordProcessingController`: Handles API requests and produces Kafka messages.
- **Services:**
    - `WordProcessingServiceImpl`: Processes text and computes metrics.
- **Kafka:**
    - `KafkaMessageProducer`: Produces messages to Kafka.

### Repository Application

- **Controllers:**
    - `ReportController`: Provides the history of processed data.
- **Services:**
    - `ReportServiceImpl`: Manages database operations.
- **Kafka:**
    - `KafkaReportsConsumer`: Consumes messages from Kafka.
    - `KafkaConsumerConfig`: Configures Kafka consumer settings.


## Technology Stack

- **Java 21**
- **Maven**
- **Spring Boot**
- **Kafka**
- **PostgreSQL**
- **Kafdrop** http://localhost:9000
- **Docker**

## Applications

**TL;DR:** All-in-one Docker Compose for demo purposes:

```bash
docker-compose -f docker-compose.yml up
```
**Kafka and Kafdrop**
The application stack includes Kafka, Zookeeper, and Kafdrop, all deployed within Docker .
Using Kafka alongside Zookeeper for message brokering. Additionally, integrated Kafdrop, a web-based UI tool that allows us to visualize the content within Kafka, such as topics and messages.

**Ports**

- **Kafdrop: 9000 (Web UI)**
- **Zookeeper: 2181 (Client Port), 2888, 3888**
- **Kafka: 9092 (External), 29092 (Internal)**


| App Name             | Description                                               | REST Endpoint (with default port settings) |
| -------------------- | --------------------------------------------------------- | ------------------------------------------ |
| **words-processing** | Handles HTTP requests, processes text & generates reports | `http://localhost:8080/betvictor/text`     |
| **reports-history**  | Provides pageable processing reports list                 | `http://localhost:8084/betvictor/history`  |

## Environment Variables

Applications are highly configurable and support many environment variables, such as:

| ENV Variable                             | Description                                      | Default Value                                       |
| ---------------------------------------- | ------------------------------------------------ | --------------------------------------------------- |
| `SERVER_PORT`                            | Application port                                 | `8080` (words-processing), `8084` (reports-history) |
| `KAFKA_BOOTSTRAP_SERVERS`                | Kafka Broker address                             | `localhost:9092`                                    |
| `KAFKA_SECURITY_PROTOCOL`                | Kafka Security Protocol                          | `PLAINTEXT`                                         |
| `KAFKA_TOPIC_WORDS_PROCESSED`            | Topic name                                       | `words.processed`                                   |
| `KAFKA_TOPIC_PARTITIONS_WORDS_PROCESSED` | Topic partitions                                 | `4`                                                 |
| `KAFKA_CONSUMER_THREADS`                 | Consumer threads count, respective to partitions | `4`                                                 |
| `KAFKA_CONSUMERS_GROUP`                  | Consumer group name                              | `consumer-group`                                    |
| `DATASOURCE_URL`                         | Database connection URL                          | `jdbc:postgresql://localhost:5432/lorem_db`         |
| `DATASOURCE_DRIVER`                      | Database driver                                  | `org.postgresql.Driver`                             |



## Installation

## Code Conventions

The code follows standard conventions.

## Pre-Requisites to Run Locally 

1. **Set up Git command line tool:** [Guide](https://help.github.com/articles/set-up-git)
2. **Clone the source code to the local machine:**
   ```bash
   git clone https://github.com/AzizyanAsi/lorem.git
   cd lorem
   ```
3. **Install Docker:** [Guide](https://docs.docker.com/get-docker/)
4**Start Kafka broker, ZooKeeper, and PostgreSQL:** 
   ```bash
   docker-compose -f docker-compose.yml up -d
   ```

5. **Build JAR packages:**
   ```bash
   ./mvnw package
   ```
6. **Run ********`words-processing`******** app:**
   ```bash
   java -jar words-processing/target/*.jar
   ```
7. **Run ********`reports-history`******** app:**
   ```bash
   java -jar reports-history/target/*.jar
   ```


## Usage

### Processing Application

Make a GET request to `/betvictor/text` with the required parameters. Example:

```bash
curl -X GET "http://localhost:8080/betvictor/text?p=3&l=SHORT"
```

### Repository Application

Make a GET request to `/betvictor/history` to retrieve the last 10 computation results:

```bash
curl -X GET "http://localhost:8081/betvictor/history"
```


## Author

[Astghik Azizyan](https://github.com/AzizyanAsi)


