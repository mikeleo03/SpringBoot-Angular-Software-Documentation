#!/bin/bash
echo Building the project with Maven...
mvn clean install && java -jar target/lecture_9_2-1.0-SNAPSHOT.jar