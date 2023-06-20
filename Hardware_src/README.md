# Accesspoint and Sensor station

This readme should describe the handle of the raspberry pi and the arduino uno LE.
## Arduino
In the directory [arduino](arduino) is the source code for the sensor station [Arduino_programm_v1.0.ino](arduino%2FArduino_programm_v1.0.ino).
This program is prepared to load onto an arduinoUnoLE.
There are two different option to connect to a Sensor Station.
1) Give the Sensor station a name and pair it at sensorstation managemant page.
   The identification will happen via the name.
   The current name is "PlantSensor" but you can change it to another one if you like.
2) Search for sensor station via accesspoint management page and choose one.
   The identification will happen via the mac address of the arduino.


The following service and characteristic are implemented:
```c
BLEService readSensorDataService("181A");

    BLEIntCharacteristic ligthValueCharacteristic("19b0", BLERead | BLEWrite);
    BLEIntCharacteristic hygroValueCharacteristic("19b1", BLERead | BLEWrite);
    BLEFloatCharacteristic tempValueCharacteristic("19b2", BLERead | BLEWrite);
    BLEFloatCharacteristic humidityValueCharacteristic("19b3", BLERead | BLEWrite);
    BLEFloatCharacteristic pressureValueCharacteristic("19b4", BLERead | BLEWrite);
    BLEFloatCharacteristic gasValueCharacteristic("19b5", BLERead | BLEWrite);
    BLEBoolCharacteristic alarmCharacteristic("19b6", BLERead );
```
    

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

| service/characteristic |  uuid  |      description      | properties | alarm interval | alarm light |
|------------------------|:------:|:---------------------:|:----------:|:--------------:|:-----------:|
| readSensorData         | 0x181A | environmental sensing |  service   |       -        |      -      |
| ligthValue             | 0x19b0 |    LIGHT_INTENSITY    | READ/WRITE |      1sec      |    blue     |
| hygroValue             | 0x19b1 |       HUMIDITY        | READ/WRITE |      1sec      |     red     |
| tempValue              | 0x19b2 |      TEMPERATURE      | READ/WRITE |      6sec      |    green    |
| humidityValue          | 0x19b3 |     SOIL_MOISTURE     | READ/WRITE |      6sec      |    blue     |
| pressureValue          | 0x19b4 |     AIR_PRESSURE      | READ/WRITE |      6sec      |     red     |
| gasValue               | 0x19b5 |      AIR_QUALITY      | READ/WRITE |      1sec      |    green    |
| alarm                  | 0x19b6 |     ALARM_STATUS      |    READ    |       -        |      -      |



## Raspberry
The raspberry source code is located in the /Raspberry directory. It is developed in python3.10 to read via BLE the sensor data from the arduino and writes them into a database.
As a next feature the data gets transferred to the webapp via REST POST/GET requests.

### Run accesspoint application
To run the accesspoint application you need to create a new access point via the webapp application.
After creating a new access point you are able to download the config.yaml.
Copy this yaml into the [Raspberry](Raspberry) directory, afterward open a terminal and move into the directory [Hardware_src](Hardware_src) 
Use the following command to upload the application onto the raspberry device. If the password is needed please enter your raspberry password.

```shell
  bash upload_access_point.sh "raspberry-IP"
```


If this is uploaded correct you will be asked to enter your raspberry password again to enter onto your raspberry.
If you are on your raspberry use the following command to start the access point application.
```shell
    bash Raspberry/start_access_point.sh&
```
After SUCCESS startup you should see a log entry of the given access point at webapp.
#### Note 
   If you are using the raspberry that we set up you just need to reboot the raspberry it will start the application automatically.
   If not the following description shows how you can set up a crontab:
   
1) Open new crontab
```shell
   crontap -e
```
2) add the following line on the bottom of the file:
```shell
   @reboot bash /home/pi/Raspberry/start_access_point.sh >/dev/null 2>&1
```

### Class overview


| Class                                                                |                                                                                                                                        description                                                                                                                                        |            properties            |
|----------------------------------------------------------------------|:-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------:|:--------------------------------:|
| [accesspoint_application.py](Raspberry%2Faccesspoint_application.py) |                                                                              main application it calls the different classes<br/> in the way that it has been describe in section<br/> Application sequence                                                                               | control the application sequence |
| [ble_service_connection.py](Raspberry%2Fble_service_connection.py)   |    read and write via bluetooth LE.<br/>It reads the Service and the characteristics if a connection is new.<br/>If a connection is existing it reads the values of the sensors.<br/>In addition it writes an alarm and reads the alarm characteristic and search for nearby devices.     |      BLE connection handle       |
| [check_boarder_values.py](Raspberry%2Fcheck_boarder_values.py)       |                                                                        This class evaluates if sensor boarders are broken. Moreover if an alarm is switch on it checks if the alarm has been switched off or not.                                                                         |      boarder value checker       |
| [config_yaml.py](Raspberry%2Fconfig_yaml.py)                         |                                                                                                          In this class the read/write functions for the config.yaml are placed.                                                                                                           |       read/write conf.yaml       |
| [DB_connection.py](Raspberry%2FDB_connection.py)                     | This class is the database connection. There are different insert/update/read functions for the database.<br/>The database is developed as an sqLite.The model is shown in [E-R_sensorstation.pdf](Database_sql%2FE-R_sensorstation.pdf) and will be build up at the start if not exists. |       database connection        |
| [rest_api.py](Raspberry%2Frest_api.py)                               |                                                                                                 This class contains the different POST/GET function to read and write data to the Webapp.                                                                                                 |           REST handle            |
| [program_status.py](Raspberry%2Fprogram_status.py)                   |                                                                                                                             Is a enum for the program states.                                                                                                                             |               enum               |
| [exception_logging.py](Raspberry%2Fexception_logging.py)             |                                                                                   At this class the exceptions get connected and saved to a logfile that gets transfer to the webapp from time to time                                                                                    |         exception handle         |
| [interval_service.py](Raspberry%2Finterval_service.py)               |                                                                                        This class evaluates the time intervals and offers the start times for the webapp and measurement interval.                                                                                        |         interval handle          |
| [application_restarter.py](Raspberry%2Fapplication_restarter.py)     |                                 This class is called inside the start_access_point.sh after the application start. It will evaluate if the application needs to restart. This information gets read out of the  [restart.yaml](Raspberry%2Frestart.yaml)                                  |     restart the main program     |


Additional files in the directory [Raspberry](Raspberry) are:

- logFile.txt: in this file the logs will be stored until they get write to the webapp.
- specialLogs.txt: in this file all the errors they are too long to send to the webapp are stored.

### Application sequence
The following code shows the program states. This states will called depending on the time intervals for the states. The intervals will be stored into the application_properties.txt.
At the beginning the database will be implemented automatically.
        
```python
                if (start_call_new_station_time + call_station_delta) < datetime.now():
                    program_state = program_status.Is.CHECK_WEBAPP_FOR_NEW_SENSORSTATION.value
                    start_call_new_station_time = datetime.now()

                elif (start_search_for_devices_time + search_delta) < datetime.now():
                    program_state = program_status.Is.SEARCH_FOR_DEVICES.value
                    start_search_for_devices_time = datetime.now()

                elif (start_check_webapp_data_time + check_webapp_delta) < datetime.now():
                    program_state = program_status.Is.CALL_SENSOR_STATION_DATA.value
                    start_check_webapp_data_time = datetime.now()

                elif (start_check_alarm_time + check_alarm_delta) < datetime.now():
                    program_state = program_status.Is.CHECK_SENSOR_STATION_ALARM.value
                    start_check_alarm_time = datetime.now()

                else:
                    if (start_log_time + log_delta) < datetime.now():
                        program_state = program_status.Is.SEND_LOG_TO_WEBAPP.value
                        start_log_time = datetime.now()

                    elif len(measurement_station_list) > 0:
                        program_state = program_status.Is.READ_SENSOR_VALUES.value
                        for e in start_measurement_interval_time_list:
                            for s in measurement_station_list:
                                if e.name == s:
                                    e.start_time = datetime.now()

                    elif len(webapp_station_list) > 0:
                        program_state = program_status.Is.WRITE_VALUES_TO_WEBAPP.value
                        for e in start_webapp_interval_time_list:
                            for s in webapp_station_list:
                                if e.name == s:
                                    e.start_time = datetime.now()
```

### Packages
   The Directory [Raspberry](Raspberry) contains all the necessary file to run the access point.
   Be sure that you copy the config.yaml into this directory.
   Furthermore, the function to read the current server port let crash the JUnit tests, so make sure that port 8080 is used at webserver.
   You can change this option in class [AccessPointService.java](..%2Fsrc%2Fmain%2Fjava%2Fat%2Fqe%2Fskeleton%2Fservices%2FAccessPointService.java).
   And here [import_packages.txt](import_packages.txt) you find a list of all the Packages you need to run the application.

### Configuration
   The config.yaml contains all the necessary information for the access point.
   You can download the config.yaml from the access point management page at the webapp.
   
Example:
```yaml
accesspoint-params:
  id: 10
  validation: false
webapp-params:
  ip: localhost:8080
  pswd: passwd
  usnm: admin
```
### Transmitting interval
There are two different transmission intervals:
1) measurement-interval is the interval how often the values of the sensors are read.
2) webapp-interval is the interval how often the measurements get transferred to the webapp.

### Alarm Count
The alarm is developed on the way that if a quarter of the measurements are breaking the boarders the alarm count gets increased by one.
The alarmCountThreshold is set by default to one and is the boarder how high the alarm count can increase until an alarm will be switched on.
You are able to set this at webapp. 

## Run script
The run script is the file [start_access_point.sh](Raspberry%2Fstart_access_point.sh) and it is developed to start raspberry program.
This program has a recursive call to itself to make sure that the program restarts if it crashes.
