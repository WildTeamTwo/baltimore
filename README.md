# Purpose
BMore Analytical downloads public data, cross references with better data sets, and runs more insightful analytics than the [Baltimore Open Data API](https://data.baltimorecity.gov/) is able to provide.

# System Design
![System Design](/bmore-analytical/system-design.jpg)

# Why 
Let's be clear we do this for the culture. These tools will provide smart data into bias, corruption, misappropriation of funds, and political trends. We want to provide the low and middle income class first class intelligence into their local politics.

# Build Instructions
The team uses Maven to build the application and handle dependencies. In addition Maven Wrapper is used align the team to a common build tool.

# Run as a Spring Boot application
Navigate to the project home direcorty and run the spring boot plugin.
```
> cd /{project_home}/bmore-analytical
> ./mvnw spring-boot:run
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

