@echo off
echo Building the project with Maven...
mvn clean install && java -jar target/lecture_7-1.0-SNAPSHOT.jar