import exception_logging
import rest_api
import time
import asyncio
import ble_service_connection
import DB_connection
import check_boarder_values
import program_status
import yaml
from pprint import pprint


#  cronjop to restart
program_state = 0
if __name__ == '__main__':
    # TODO read config.yaml
    try:
        with open("../config.yaml") as f:
            cfg = yaml.load(f, Loader=yaml.FullLoader)

        pprint(cfg["webapp-params"])
    except Exception as e:
        exception_logging.logException(e, "reading config.yaml")
    ip = cfg["webapp-params"]["ip"]
    print(ip)
    sending_interval = 0
    measurement_interval = 0
    loop = 10
    while loop > 0:
        DB_connection.insert_values_into_database(b'25',False, b'GasSensor', "G4T2" )
        loop -= 1
    # new_SensorStation = True
    program_state = program_status.Is.READ_SENSOR_VALUES.value
    while True:
        match program_state:

            case program_status.Is.IMPLEMENT_DATABASE.value:
                print("Implement database")
                DB_connection.implement_database()
                time.sleep(1)
                program_state = program_status.Is.CHECK_WEBAPP_FOR_NEW_SENSORSTATION.value
                #program_state = program_status.Is.CHECK_FOR_NEW_BOARDER_VALUES.value

            case program_status.Is.CHECK_WEBAPP_FOR_NEW_SENSORSTATION.value:
                print("Call for new Sensorstation")
                try:
                    new_device_name_list = rest_api.check_if_new_stations()
                    print(new_device_name_list)
                    if len(new_device_name_list) > 0:
                        asyncio.run(ble_service_connection.read_sensor_data(True, new_device_name_list))
                    rest_api.write_sensors_and_station_description(new_device_name_list)
                except Exception as e:
                    exception_logging.logException(e, "rest_api read Station name")
                time.sleep(1)
                measurement_interval += 1
                print("Measurement: " + str(measurement_interval))
                if measurement_interval > 1:
                    program_state = program_status.Is.READ_SENSOR_VALUES.value
                    measurement_interval = 0
                else:
                    program_state = program_status.Is.CHECK_SENSOR_STATION_ALARM.value

            case program_status.Is.READ_SENSOR_VALUES.value:
                print("Read Sensor data")
                try:
                    device_name = DB_connection.read_Sensor_Stationnames_Database() # extract from [('G4T2',)]
                    name_list =[]
                    for device in device_name:
                        name_list.append(device[0])
                    print(name_list)
                    asyncio.run(ble_service_connection.read_sensor_data(False, name_list))
                except Exception as e:
                    exception_logging.logException(e, "call_read_values")
                sending_interval += 1
                if sending_interval >= 1:
                    program_state = program_status.Is.CHECK_BOARDER_VALUER.value
                    sending_interval = 0
                else:
                    program_state = program_status.Is.CHECK_SENSOR_STATION_ALARM.value
                time.sleep(1)

            case program_status.Is.CHECK_BOARDER_VALUER.value:
                print("check Boarders")
                check_boarder_values.checkBoarderValues()
                time.sleep(1)
                program_state = program_status.Is.WRITE_VALUES_TO_WEBAPP.value

            case program_status.Is.WRITE_VALUES_TO_WEBAPP.value:
                print("write values to Webapp")
                try:
                    rest_api.write_value_to_web_app()
                except Exception as e:
                    exception_logging.logException(e, "rest_api write values")
                program_state = program_status.Is.CHECK_SENSOR_STATION_ALARM.value
                time.sleep(1)

            case program_status.Is.CHECK_SENSOR_STATION_ALARM.value:
                print("check sensorstation alarm")
                try:
                    check_boarder_values.check_sensor_station_alarm()
                except Exception as e:
                    exception_logging.logException(e, "check alarm")
                time.sleep(1)
                program_state = program_status.Is.CHECK_FOR_NEW_BOARDER_VALUES.value

            case program_status.Is.CHECK_FOR_NEW_BOARDER_VALUES.value:
                print("check webapp for new boarder values")
                try:
                    rest_api.read_sensor_boarder_values()
                except Exception as e:
                    exception_logging.logException(e, "read boarders")
                time.sleep(1)
                program_state = program_status.Is.CHECK_WEBAPP_FOR_NEW_SENSORSTATION.value
