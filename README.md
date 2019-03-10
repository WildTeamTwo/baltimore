# Purpose
BMore Analytical downloads public data, cross references with better data sets, and runs more insightful analytics than the [Baltimore Open Data API](https://data.baltimorecity.gov/) is able to provide.

# Why 
Let's be clear. We want to be an independent source of truth for the hood. We want to report everything from courtroom filings, murder forensics, to social trends such as the release of a new pair of highly desired Jordan brand shoes.  

Knowing this information allows us to predict outcomes (to a reasonable degree), detect potential for bias, and find potential pockets of corruption.

# Build Instructions
The team uses Maven to build the application and handle dependencies. In addition Maven Wrapper is used align the team to a common build tool.

# Run as a Spring Boot application
Navigate to the project home direcorty and run the spring boot plugin.
./mvnw spring-boot:run

# Run as a JAR
Download the source run the commands below:
> cd {project_home_directory}/bmore-analytical
> ./mvnw install 
> java -jar target/bmore-analytical-1.0-SNAPSHOT-jar-with-dependencies.jar

# Setup and Test Instructions
The application requires:
1) Google API account
2) MySQL database 

