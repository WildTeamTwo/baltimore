# Purpose
BMore Analytical downloads public data, cross references with better data sets, and runs more insightful analytics than the [Baltimore Open Data API](https://data.baltimorecity.gov/) is able to provide.

# Why 
Let's be clear we do this for the people of Baltimore. We want to look at Baltimore through every lens possible.  We want to report everything from courtroom trends to social trends. We are interested in anything that impacts quality of life and impacts a mass of people. Whatever the data set, whatever the trend we want to measure it's impact.

These tools enable us to provide smart insights to residents.  In addition, it allows us to detect political trends, predict outcomes (to a reasonable degree), detect locations of bias, find opportunities of self and community-improvement, and find pockets of corruption.

# Build Instructions
The team uses Maven to build the application and handle dependencies. In addition Maven Wrapper is used align the team to a common build tool.

# Run as a Spring Boot application
Navigate to the project home direcorty and run the spring boot plugin.
```
./mvnw spring-boot:run
```
# Run as a JAR
Download the source run the commands below:
```
> cd {project_home_directory}/bmore-analytical
> ./mvnw install 
> java -jar target/bmore-analytical-1.0-SNAPSHOT-jar-with-dependencies.jar
```
# Setup and Test Instructions
The application requires:
- [x] Google API account
- [x] MySQL database 

