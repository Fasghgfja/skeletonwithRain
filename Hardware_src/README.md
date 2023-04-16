# Accesspoint and Sensor station

This readme should describe the handle of the raspberry pi and the arduino uno LE.
## Arduino
In the directory arduino is the source code for the sensor station and it only needs to load onto an arduinoUnoLE. 

## Raspberry
The raspberry source code is located in the /Raspberry directory. It is developt to read via BLE the sensor data from the arduino and writes them into a database.

## Run script
The run script is the file start_access_point.sh and it is developed to start and observe the raspberry programm. 
It also must copyed onto the raspberry to log if the programm is terminated or to start the accespoint programm.
There is no need to use it, it is just for the phase of use to have a nice handle. 