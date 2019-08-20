# Purpose
BMore Analytical downloads public data, cross references with better data sets, and runs more insightful analytics than the [Baltimore Open Data API](https://data.baltimorecity.gov/) is able to provide.

# System Design
![System Design](/bmore-analytical/system-design.jpg)

# Why 
Because we do this for the culture. These tools will provide smart data into bias, corruption, misappropriation of funds, and political trends. We want to provide first class intelligence into state and local politics.

# Build Instructions
The team uses Maven to build the application and manage dependencies. 

# Things to note about the app.
The app is a suite of tools. A Tool generally fall into 3 categories: retrieve raw data, cross referencing with other private and public resources to make the data more intelligible, and analyze the data. 

The app requires hard drive space to store lots of city data.  I would say that the largest data sets (parking, arrest, etc) contains roughly 1 GB of data each. The smaller data sets (liquor, property, etc) may contain ~500 MB.

Hopefully future developments will move this data from local disk to shared cloud environment but that is not currently scheduled for development.   

# Setup Instructions
1. Install Mysql 8 installed with __root__ access on __localhost__
2. Run __./setup.sh__ to create dependencies such as data tables, db user, and file system resources.
3. Request to be added to the Google Cloud API Project.   

# Build instructions
./mvnw clean install

# Run as a Spring Boot app
Navigate to the project home direcorty and run the spring boot plugin.
```
> cd /{project_home}/bmore-analytical
> ./mvnw spring-boot:run
```

# Run as a JAR
Download the source run the commands below:
```
> cd {project_home_directory}/bmore-analytical
> ./start.sh
```
# First time running
The first execution of the app will prompt you to create working directories. This is where raw data will be stored. 
