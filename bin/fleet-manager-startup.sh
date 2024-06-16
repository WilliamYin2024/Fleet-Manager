#!/bin/bash

echo "Starting Position Tracker..."
java -jar position-tracker/target/position-tracker-1.0.0-SNAPSHOT.jar 2> logs/position-tracker.err 1> logs/position-tracker.log &
sleep 1

echo "Starting Position Simulator..."
java -jar position-simulator/target/position-simulator-1.0.0-SNAPSHOT.jar 2>logs/position-simulator.err 1> logs/position-simulator.log &
sleep 1

echo "Starting Staff Service..."
java -jar staff-service/target/staff-service-1.0.0-SNAPSHOT.jar 2>logs/staff-service.err 1> logs/staff-service.log &
sleep 1

echo "Starting Vehicle Telemetry..."
java -jar vehicle-telemetry/target/vehicle-telemetry-1.0.0-SNAPSHOT.jar 2>logs/vehicle-telemetry.err 1> logs/vehicle-telemetry.log &
sleep 1

echo "Starting API Gateway..."
java -jar api-gateway/target/api-gateway-1.0.0-SNAPSHOT.jar 2>logs/api-gateway.err 1> logs/api-gateway.log &
sleep 1

echo "Starting React Web App..."
java -jar webapp-react/target/webapp-react-1.0.0-SNAPSHOT.jar 2>logs/webapp-react.err 1> logs/webapp-react.log &
