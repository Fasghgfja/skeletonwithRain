import exception_logging
import rest_api
import time
import asyncio
import ble_service_connection
import DB_connection
import check_boarder_values
import program_status
import config_yaml

SECTION_SLEEP = 5
#  cronjop to restart
program_state = 0
if __name__ == '__main__':
    # TODO read config.yaml
    rest_api.send_log_data_to_webapp()

    try:
        with open("../config.yaml") as f:
            cfg = yaml.load(f, Loader=yaml.FullLoader)


    sending_interval_counter = 0
    measurement_interval_counter = 0
    program_state = 0#program_status.Is.READ_SENSOR_VALUES.value
    while True:
        match program_state:

            case program_status.Is.IMPLEMENT_DATABASE.value:
                print("Implement database")
                DB_connection.implement_database()
                time.sleep(SECTION_SLEEP)
                program_state = program_status.Is.CHECK_WEBAPP_FOR_NEW_SENSORSTATION.value
                #program_state = program_status.Is.CHECK_FOR_NEW_BOARDER_VALUES.value

            case program_status.Is.CHECK_WEBAPP_FOR_NEW_SENSORSTATION.value:
                print("Call for new Sensorstation")
                try:
                    new_device_name_list = rest_api.check_if_new_stations()
                    if len(new_device_name_list) > 0:
                        asyncio.run(ble_service_connection.read_sensor_data(True, new_device_name_list))
                        asyncio.run(rest_api.write_sensors_and_station_description(new_device_name_list))
                except Exception as e:
                    exception_logging.logException(e, "rest_api read Station name")
                time.sleep(SECTION_SLEEP)
                measurement_interval_counter += 1
                print("Measurement: " + str(measurement_interval_counter))
                if measurement_interval_counter > config_yaml.read_sending_intervalls()[0]:
                    program_state = program_status.Is.READ_SENSOR_VALUES.value
                    measurement_interval_counter = 0
                else:
                    program_state = program_status.Is.CHECK_SENSOR_STATION_ALARM.value

            case program_status.Is.READ_SENSOR_VALUES.value:# next to prove
                print("Read Sensor data")
                try:
                    device_name = DB_connection.read_Sensor_Stationnames_Database()
                    name_list =[]
                    for device in device_name:
                        name_list.append(device[0])
                    asyncio.run(ble_service_connection.read_sensor_data(False, name_list))
                except Exception as e:
                    exception_logging.logException(e, "call_read_values")
                sending_interval_counter += 1
                if sending_interval_counter >= config_yaml.read_sending_intervalls()[1]:
                    program_state = program_status.Is.CHECK_BOARDER_VALUER.value
                    sending_interval_counter = 0
                else:
                    program_state = program_status.Is.CHECK_SENSOR_STATION_ALARM.value
                time.sleep(SECTION_SLEEP)

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
                time.sleep(SECTION_SLEEP)

            case program_status.Is.CHECK_SENSOR_STATION_ALARM.value:
                print("check sensorstation alarm")
                try:
                    check_boarder_values.check_sensor_station_alarm()
                except Exception as e:
                    exception_logging.logException(e, "check alarm")
                time.sleep(SECTION_SLEEP)
                program_state = program_status.Is.CHECK_FOR_NEW_BOARDER_AND_INTERVAL_VALUES.value

            case program_status.Is.CHECK_FOR_NEW_BOARDER_AND_INTERVAL_VALUES.value:
                print("check webapp for new boarder values")
                try:
                    rest_api.read_sensor_boarder_values()
                    rest_api.read_sending_interval()
                except Exception as e:
                    exception_logging.logException(e, "read boarders")
                time.sleep(SECTION_SLEEP)
                program_state = program_status.Is.CHECK_WEBAPP_FOR_NEW_SENSORSTATION.value
