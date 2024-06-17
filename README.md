# Fleet Manager

Tracks the locations of various trucks in the Greater Toronto Area.

## Table of Contents

- [Introduction](#introduction)
- [Prerequisites](#prerequisites)
- [Running the Application](#running-the-application)
- [Usage](#usage)
- [Shutting Down the Application](#shutting-down-the-application)
- [Learn More](#learn-more)

## Introduction

A Spring Boot application that tracks the locations of trucks in the Greater Toronto Area. The trucks send their
locations to an Apache Kafka Topic every 100 milliseconds. Built using a microservice architecture. Provides a React web
application to view the locations of each truck on an interactive map. 

## Prerequisites

Before you begin, ensure you have met the following requirements:
- You have installed Java (version 21 or above).
- You have installed Maven (version 3.9.7 or above).
- You have installed Git.
- You have installed Apache Kafka and ZooKeeper (the default installation of Kafka includes ZooKeeper)
- You have an IDE installed (IntelliJ IDEA, Eclipse, VSCode, etc.).
- You have a GitHub account.

## Running the Application

### 1. Clone the Repository

To clone the repository, run the following command in your terminal:

```bash
git clone https://github.com/WilliamYin2024/Fleet-Manager.git
```

### 2. Package the Java Project

To package the Java Project, navigate to the location of the cloned repository and run the following commands:

```bash
cd Fleet-Manager
mvn clean package
```

### 3. Start Apache ZooKeeper and Kafka

To start ZooKeeper and Kafka, navigate to the folder where you have installed Kafka and run the following commands:

***The microservices in this application are configured to communicate with Kafka by using the URL localhost:9092.
Please ensure that port 9092 is available or modify the "bootstrap.servers" and "spring.kafka.bootstrap-servers"
properties respectively in position-simulator and position-tracker's application.properties files to use a different URL
for Kafka.***

```bash
bin/zookeeper-server-start.sh -daemon config/zookeeper.properties
bin/kafka-server-start.sh config/server.properties
```

### 4. Create the "actions" and "vehicle-positions" Topics

To create the "actions" and "vehicle-positions" topics required by this application, run the following commands:

```bash
bin/kafka-topics.sh --create --topic actions --bootstrap-server localhost:9092
bin/kafka-topics.sh --create --topic vehicle-positions --bootstrap-server localhost:9092
```

### 5. Run the Application

To run the application, navigate back to the ```Fleet-Manager``` directory and run the following commands:

***The microservices run on ports 8080, 8082, 8084, 8085, and 8086. The React web application runs on port 5174. Please
ensure those ports are free before running the application.***

```bash
bin/fleet-manager-startup.sh
```

## Usage

View the application by visiting the URL ```http://localhost:5174```. You should see a map of the Greater Toronto Area
with markers that periodically move and leave behind a line indicating the route they have travelled thus far.

## Shutting Down the Application

To shut down the application, navigate to the ```Fleet-Manager``` directory and run the following command:

```bash
bin/fleet-manager-shutdown.sh
```

## Learn More

To find out how I created this application, please view the post on my Medium blog [here](https://medium.com/@william.yin.2024/using-apache-kafka-spring-boot-and-react-to-create-an-easily-scalable-fleet-manager-application-c9c5cfc374e0).
