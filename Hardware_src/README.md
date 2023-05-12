# Accesspoint and Sensor station

This readme should describe the handle of the raspberry pi and the arduino uno LE.
## Arduino
In the directory arduino is the source code for the sensor station and it only needs to load onto an arduinoUnoLE. 

## Raspberry
The raspberry source code is located in the /Raspberry directory. It is developt to read via BLE the sensor data from the arduino and writes them into a database.

The following code shows the program states. At the prorgam start the database will be implemented automatically.


        
        match program_state:
            case program_status.Is.IMPLEMENT_DATABASE
            case program_status.Is.CHECK_WEBAPP_FOR_NEW_SENSORSTATION
            if measurement_interval > 1:
                    program_state = program_status.Is.READ_SENSOR_VALUES
                else:
                    program_state = program_status.Is.CHECK_SENSOR_STATION_ALARM
            case program_status.Is.READ_SENSOR_VALUES
             if sending_interval >= 1:
                    program_state = program_status.Is.CHECK_BOARDER_VALUER
                else:
                    program_state = program_status.Is.CHECK_SENSOR_STATION_ALARM
            case program_status.Is.CHECK_BOARDER_VALUER
            case program_status.Is.WRITE_VALUES_TO_WEBAPP
            case program_status.Is.CHECK_SENSOR_STATION_ALARM
            case program_status.Is.CHECK_FOR_NEW_BOARDER_VALUES

## Run script
The run script is the file start_access_point.sh and it is developed to start and observe the raspberry programm. 
It also must copyed onto the raspberry to log if the programm is terminated or to start the accespoint programm.
There is no need to use it, it is just for the phase of use to have a nice handle. 