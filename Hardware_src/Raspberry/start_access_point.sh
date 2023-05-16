#!/bin/bash
# shellcheck disable=SC2164
while true
 do

    # shellcheck disable=SC2164
    echo "WARNING: Started at $(date +"%T")" &>> logFile.txt
    python3 accesspoint_application.py;
    echo "WARNING: Stopped at $(date +"%T")" &>> logFile.txt
    sleep 3
    # shellcheck disable=SC2164

done