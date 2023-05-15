# Accesspoint and Sensor station

This readme should describe the handle of the raspberry pi and the arduino uno LE.
## Arduino
In the directory */Hardware_src/arduino* is the source code for the sensor station.
This program is prepared to load onto an arduinoUnoLE.
The following service and characteristic are implemented:

    BLEService readSensorDataService("181A");

    BLEIntCharacteristic ligthValueCharacteristic("19b0", BLERead | BLEWrite);
    BLEIntCharacteristic hygroValueCharacteristic("19b1", BLERead | BLEWrite);
    BLEFloatCharacteristic tempValueCharacteristic("19b2", BLERead | BLEWrite);
    BLEFloatCharacteristic humidityValueCharacteristic("19b3", BLERead | BLEWrite);
    BLEFloatCharacteristic pressureValueCharacteristic("19b4", BLERead | BLEWrite);
    BLEFloatCharacteristic gasValueCharacteristic("19b5", BLERead | BLEWrite);
    BLEBoolCharacteristic alarmCharacteristic("19b6", BLERead );

The connection plan is stored in the directory  */Arduino*.
The program *Arduino_programm_v1.0.ino* has the following functionalities: 
- It offers 6 different sensor values.
- One button for three functionalities.
- Warnlight system

1) The sensors are represented by the characteristics point out above.
   - "LIGHT_INTENSITY"
   - "SOIL_MOISTURE"
   - "TEMPERATURE"
   - "HUMIDITY"
   - "AIR_PRESSURE"
   - "AIR_QUALITY"
   The characteristic to read if the alarm has been switch off with the button is:
   - "ALARM_STATUS"  
   
2) The button has the following sequences
    - If the arduino gets switch on it is in standby mode. The first push starts the pairing tone.
      In this case it is ready for pairing. If then 5 minutes are passing without connection 
      the arduino goes back into standby.
    - If there is a connection and the arduino operates as usual there is the option to but the arduino back to standby.
      This is like a reset and can be done with a button push while operating time.
    - If an alarm is on, with a push on the button it can switched of. This can also done via the webapp.
   
3) The warning ligth system switches depending on the alarm different colours on and off.
   - AIR_QUALITY ......1sec intervall in green
   - AIR_PRESSURE......6sec intervall in red
   - SOIL_MOISTURE.....6sec intervall in blue
   - TEMPERATURE.......6sec intervall in green
   - LIGHT_INTENSITY...1sec intervall in blue
   - HUMIDITY..........1sec intervall in red

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
            case program_status.Is.CHECK_FOR_NEW_BOARDER_AND_INTERVAL_VALUES

## Run script
The run script is the file start_access_point.sh and it is developed to start and observe the raspberry programm. 
It also must copyed onto the raspberry to log if the programm is terminated or to start the accespoint programm.
There is no need to use it, it is just for the phase of use to have a nice handle. 