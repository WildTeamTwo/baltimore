# Purpose
BMore Analytical downloads public data, cross references with better data sets, and runs more insightful analytics than the [Baltimore Open Data API](https://data.baltimorecity.gov/) is able to provide.

# System Design
![System Design](/bmore-analytical/system-design.jpg)

# Why 
Because we do this for the culture. These tools will provide smart data into bias, corruption, misappropriation of funds, and political trends. We want to provide first class intelligence into state and local politics.

# Build Instructions
The team uses Maven to build the application and manage dependencies. 

# Things to note about the app.
The app is a suite of tools. Tools generally fall into 3 categories: retrieve raw data, cross referencing with other private and public data stores to make the data more intelligible, and analyze the data. 

Such a data intensive app requires hard drive space to store raw baltimore data.  I would say that the largest data sets (parking, arrest, etc) could contain rouhgly roughly 1 GB of data each. The smaller data sets (liquor, property, etc) may contain ~500 MB.

When we develop further we will put this data in a shared cloud environment.   

# Setup Instructions
1. Install Mysql 8 installed with __root__ access on __localhost__
2. Run __./setup.sh__ to create dependencies such as data tables, db user, and file system resources.

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

