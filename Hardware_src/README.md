# Accesspoint and Sensor station

This readme should describe the handle of the raspberry pi and the arduino uno LE.
## Arduino
In the directory [arduino](arduino) is the source code for the sensor station [Arduino_programm_v1.0.ino](arduino%2FArduino_programm_v1.0.ino).
This program is prepared to load onto an arduinoUnoLE.
The current name is "G4T2" but you can change it to another one if you like.
The following service and characteristic are implemented:

    BLEService readSensorDataService("181A");

    BLEIntCharacteristic ligthValueCharacteristic("19b0", BLERead | BLEWrite);
    BLEIntCharacteristic hygroValueCharacteristic("19b1", BLERead | BLEWrite);
    BLEFloatCharacteristic tempValueCharacteristic("19b2", BLERead | BLEWrite);
    BLEFloatCharacteristic humidityValueCharacteristic("19b3", BLERead | BLEWrite);
    BLEFloatCharacteristic pressureValueCharacteristic("19b4", BLERead | BLEWrite);
    BLEFloatCharacteristic gasValueCharacteristic("19b5", BLERead | BLEWrite);
    BLEBoolCharacteristic alarmCharacteristic("19b6", BLERead );

### Arduino connection Plan
[Arduino_Schaltplan_aktuell.png](..%2FArduino%2FArduino_Schaltplan_aktuell.png) 

There are additional plans in directory [Arduino](..%2FArduino)
### Arduino functionalities

The program has the following functionalities: 
- It offers 6 different sensor values.
- One button for three functionalities.
- Warnlight system

The characteristic to read if the alarm has been switch off with the button is:
- "ALARM_STATUS"
1) The sensors are represented by the characteristics point out above.
   - "LIGHT_INTENSITY"
   - "SOIL_MOISTURE"
   - "TEMPERATURE"
   - "HUMIDITY"
   - "AIR_PRESSURE"
   - "AIR_QUALITY"
   

2) The button has the following sequences
    - If the arduino gets switch on it is in standby mode. The first push starts the pairing tone.
      In this case it is ready for pairing. If then 5 minutes are passing without connection 
      the arduino goes back into standby.
    - If there is a connection and the arduino operates as usual there is the option to but the arduino back to standby.
      This is like a reset and can be done with a button push while operating time.
    - If an alarm is on, with a push on the button it can switch of. This can also be done via the webapp.
   

3) The warning ligth system switches depending on the alarm different colours on and off.
   - AIR_QUALITY ......1sec intervall in green
   - AIR_PRESSURE......6sec intervall in red
   - SOIL_MOISTURE.....6sec intervall in blue
   - TEMPERATURE.......6sec intervall in green
   - LIGHT_INTENSITY...1sec intervall in blue
   - HUMIDITY..........1sec intervall in red

### Arduino properties

| service/characteristic |  uuid   |      description      | properties | alarm interval | alarm light |
|------------------------|:-------:|:---------------------:|:----------:|:--------------:|:-----------:|
| readSensorData         |  181A   | environmental sensing |  service   |       -        |      -      |
| ligthValue             |  19b0   |    LIGHT_INTENSITY    | READ/WRITE |      1sec      |    blue     |
| hygroValue             |  19b1   |       HUMIDITY        | READ/WRITE |      1sec      |     red     |
| tempValue              |  19b2   |      TEMPERATURE      | READ/WRITE |      6sec      |    green    |
| humidityValue          |  19b3   |     SOIL_MOISTURE     | READ/WRITE |      6sec      |    blue     |
| pressureValue          |  19b4   |     AIR_PRESSURE      | READ/WRITE |      6sec      |     red     |
| gasValue               |  19b5   |      AIR_QUALITY      | READ/WRITE |      1sec      |    green    |
| alarm                  |  19b6   |     ALARM_STATUS      |    READ    |       -        |      -      |



## Raspberry
The raspberry source code is located in the /Raspberry directory. It is developed in python3.10 to read via BLE the sensor data from the arduino and writes them into a database.
As a next feature the data gets transferred to the webapp via REST POST/GET requests.

### Run accesspoint application
To run the accesspoint application you need to create a new accesspoint via the webapp application.
Then move to the [Raspberry](Raspberry) directory and use the command:

    scp -r . "pi@raspberryIP":~

If this is uploaded correct go onto your raspberry and move inside the Raspberry directory and start:

    bash start_access_point.sh

### Class overview


| Class                                                                |                                                                                                                                        description                                                                                                                                        |            properties            |
|----------------------------------------------------------------------|:-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------:|:--------------------------------:|
| [accesspoint_application.py](Raspberry%2Faccesspoint_application.py) |                                                                              main application it calls the different classes<br/> in the way that it has been describe in section<br/> Application sequence                                                                               | control the application sequence |
| [ble_service_connection.py](Raspberry%2Fble_service_connection.py)   |                   read and write via bluetooth LE.<br/>It reads the Service and the characteristics if a connection is new.<br/>If a connection is existing it reads the values of the sensors.<br/>In addition it writes an alarm and reads the alarm characteristic.                    |      BLE connection handle       |
| [check_boarder_values.py](Raspberry%2Fcheck_boarder_values.py)       |                                                                        This class evaluates if sensor boarders are broken. Moreover if an alarm is switch on it checks if the alarm has been switched off or not.                                                                         |      boarder value checker       |
| [config_yaml.py](Raspberry%2Fconfig_yaml.py)                         |                                                                                                          In this class the read/write functions for the config.yaml are placed.                                                                                                           |       read/write conf.yaml       |
| [DB_connection.py](Raspberry%2FDB_connection.py)                     | This class is the database connection. There are different insert/update/read functions for the database.<br/>The database is developed as an sqLite.The model is shown in [E-R_sensorstation.pdf](Database_sql%2FE-R_sensorstation.pdf) and will be build up at the start if not exists. |       database connection        |
| [rest_api.py](Raspberry%2Frest_api.py)                               |                                                                                                 This class contains the different POST/GET function to read and write data to the Webapp.                                                                                                 |           REST handle            |
| [program_status.py](Raspberry%2Fprogram_status.py)                   |                                                                                                                             Is a enum for the program states.                                                                                                                             |               enum               |
| [exception_logging.py](Raspberry%2Fexception_logging.py)             |                                                                                   At this class the exceptions get connected and saved to a logfile that gets transfer to the webapp from time to time                                                                                    |         exception handle         |


### Application sequence
The following code shows the program states. At the beginning the database will be implemented automatically.
        
        match program_state:
            case program_status.Is.IMPLEMENT_DATABASE
            case program_status.Is.CHECK_WEBAPP_FOR_NEW_SENSORSTATION
               if measurement_interval > "is set by the Gardener at Webapp":
                       program_state = program_status.Is.READ_SENSOR_VALUES
                   else:
                       program_state = program_status.Is.CHECK_SENSOR_STATION_ALARM
            case program_status.Is.READ_SENSOR_VALUES
                if sending_interval >= "Is set by the Gardener at Webapp":
                       program_state = program_status.Is.CHECK_BOARDER_VALUER
                   else:
                       program_state = program_status.Is.CHECK_SENSOR_STATION_ALARM
            case program_status.Is.CHECK_BOARDER_VALUER
            case program_status.Is.WRITE_VALUES_TO_WEBAPP
            case program_status.Is.CHECK_SENSOR_STATION_ALARM
            case program_status.Is.CHECK_FOR_NEW_BOARDER_AND_INTERVAL_VALUES
            case program_status.Is.SEND_LOG_TO_WEBAPP

### Packages
   The Directory [Raspberry](Raspberry) contains all the necessary file to run the access point.
   Be sure that you copy the config.yaml into this directory.
   And here [import_packages.txt](import_packages.txt) you find a list of all the Packages you need to run the application.

### Transmitting interval
There are two different transmission intervals:
1) measurement-interval is the interval how often the values of the sensors are read.
2) webapp-interval is the interval how often the measurments get transfered to the webapp.

The webapp interval is called after the measurement-interval how it is mentioned Application sequence.
Therefor it is a multiple of the measurement-interval so the webapp interval shoul be greater than the measurement interval.
The following table shows the relation between the two interval with the set values at webapp:

| webapp interval | measurement-interval | send to webapp | read values |
|:---------------:|:--------------------:|:--------------:|:-----------:|
|        1        |          1           |     1 min      |    1 min    |
|        1        |          2           |     2 min      |    2 min    |
|        2        |          2           |     4 min      |    2 min    |

### Alarm Count
The alarm is developed on the way that if a quarter of the measurements are breaking the boarders the alarm count gets increased by one.
The alarmCountThreshold is set by default to one and is the boarder how high the alarm count can increase until an alarm will be switched on.
But this feature is an additional option and it is still in the test phase so you cant change it. 
## Run script
The run script is the file [start_access_point.sh](Raspberry%2Fstart_access_point.sh) and it is developed to start raspberry programm. 
