#!/bin/bash

# Build the project
mvn clean package
# Start server
java -jar player-communicator-server/target/player-communicator-server-1.0.0.jar 8080
