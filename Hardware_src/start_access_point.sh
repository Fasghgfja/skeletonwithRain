#!/bin/bash
# shellcheck disable=SC2164
while true
 do

    # shellcheck disable=SC2164
    echo "INFO: Started at $(date +"%T")" &>> logFile.txt
    python3 Raspberry/ble_service_connection.py;
    echo "INFO: Stopped at $(date +"%T")" &>> logFile.txt
    sleep 3
    # shellcheck disable=SC2164

done