#!/bin/bash
./mvnw clean install
./banner.sh
java -jar ./target/bmore-analytical-1.0-SNAPSHOT-jar-with-dependencies.jar
