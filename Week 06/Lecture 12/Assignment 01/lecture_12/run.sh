#!/bin/bash
echo Building the project with Maven...
mvn clean install && java -jar target/lecture_12-1.0-SNAPSHOT.jar