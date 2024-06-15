#!/bin/bash

webapp_pid=$(ps -aef | grep java | grep webapp-react/ | awk '{ print $2 }')
api_gateway_pid=$(ps -aef | grep java | grep api-gateway/ | awk '{ print $2 }')
vehicle_telemetry_pid=$(ps -aef | grep java | grep vehicle-telemetry/ | awk '{ print $2 }')
staff_service_pid=$(ps -aef | grep java | grep staff-service/ | awk '{ print $2 }')
position_simulator_pid=$(ps -aef | grep java | grep position-simulator/ | awk '{ print $2 }')
position_tracker_pid=$(ps -aef | grep java | grep position-tracker/ | awk '{ print $2 }')

echo "Shutting down React Web App with pid: $webapp_pid..."
kill -15 $webapp_pid
sleep 3

echo "Shutting down API Gateway with pid: $api_gateway_pid..."
kill -15 $api_gateway_pid
sleep 3

echo "Shutting down Vehicle Telemetry with pid: $vehicle_telemetry_pid..."
kill -15 $vehicle_telemetry_pid
sleep 3

echo "Shutting down Staff Service with pid: $staff_service_pid..."
kill -15 $staff_service_pid
sleep 3

echo "Shutting down Position Simulator with pid: $position_simulator_pid..."
kill -15 $position_simulator_pid
sleep 3

echo "Shutting down Position Tracker with pid: $position_tracker_pid..."
kill -15 $position_tracker_pid
