from datetime import datetime, timedelta

import exception_logging
import rest_api
import time
import asyncio
import ble_service_connection
import DB_connection
import check_boarder_values
import program_status
import config_yaml

SECTION_SLEEP = 15
#  cronjop to restart
program_state = 0
if __name__ == '__main__':
    print("Implement database this will need 15 seconds")
    DB_connection.implement_database()
    time.sleep(SECTION_SLEEP)

    program_state = 0
    start_measurement_interval_time = datetime.now()
    delta_measurement = 0
    start_webapp_interval_time = datetime.now()
    delta_webapp = 0
    start_log_time = datetime.now()
    log_delta = timedelta(seconds=1830)
    start_check_webapp_data_time = datetime.now()
    check_webapp_delta = timedelta(310)
    start_call_new_station_time = datetime.now()
    call_station_delta = timedelta(seconds=200)

    while True:
        try:

            print(datetime.now().strftime("%H:%M:%S"))
            try:
                delta_measurement = timedelta(minutes=config_yaml.read_sending_intervalls()[0])
                delta_webapp = timedelta(minutes=config_yaml.read_sending_intervalls()[1])
            except Exception as e:
                exception_logging.logException(e, "Read intervals from config.yaml")


            # time evaluations for program state
            if (start_call_new_station_time + call_station_delta) < datetime.now():
                program_state = program_status.Is.CHECK_WEBAPP_FOR_NEW_SENSORSTATION.value
            elif (start_check_webapp_data_time + check_webapp_delta) < datetime.now():
                program_state = program_status.Is.CHECK_FOR_NEW_BOARDER_AND_INTERVAL_VALUES.value
            else:
                if (start_log_time + log_delta) < datetime.now():
                    program_state = program_status.Is.SEND_LOG_TO_WEBAPP.value
                elif (start_measurement_interval_time + delta_measurement) < datetime.now():
                    program_state = program_status.Is.READ_SENSOR_VALUES.value
                elif (start_webapp_interval_time + delta_webapp) < datetime.now():
                    program_state = program_status.Is.WRITE_VALUES_TO_WEBAPP.value
                else:
                    program_state = program_status.Is.CHECK_SENSOR_STATION_ALARM.value




            match program_state:

                # call new SensorStations all 3min:20sec
                case program_status.Is.CHECK_WEBAPP_FOR_NEW_SENSORSTATION.value:
                    print("Call for new Sensorstation")
                    new_device_name_list = []

                    try:
                        new_device_name_list = rest_api.check_if_new_stations()
                    except Exception as e:
                        exception_logging.logException(e, "Call webapp for new sensorstations")

                    if len(new_device_name_list) > 0:
                        print("New device found: " + new_device_name_list)
                        # search for new SensorStation
                        try:
                            asyncio.run(ble_service_connection.read_sensor_data(True, new_device_name_list))
                        except Exception as e:
                            exception_logging.logException(e, "Search for new SensorStation")
                        # write found stations to webapp
                        try:
                            asyncio.run(rest_api.write_sensors_and_station_description(new_device_name_list))
                        except Exception as e:
                            exception_logging.logException(e, "Write new SensorStation to Webapp")

                    start_call_new_station_time = datetime.now()

                # set by measurment intervall
                case program_status.Is.READ_SENSOR_VALUES.value:
                    print("Read Sensor data")
                    try:
                        device_name = DB_connection.read_Sensor_Stationnames_Database()
                        name_list =[]
                        for device in device_name:
                            name_list.append(device[0])
                        asyncio.run(ble_service_connection.read_sensor_data(False, name_list))
                        print("check Boarders")
                        try:
                            check_boarder_values.checkBoarderValues()
                        except Exception as e:
                            exception_logging.logException(e, "While checking boarder breaks")
                    except Exception as e:
                        exception_logging.logException(e, "call_read_values")
                    start_measurement_interval_time = datetime.now()

                # set by webapp interval
                case program_status.Is.WRITE_VALUES_TO_WEBAPP.value:
                    print("write values to Webapp")
                    try:
                        rest_api.write_value_to_web_app()
                    except Exception as e:
                        exception_logging.logException(e, "rest_api write values")
                    start_webapp_interval_time = datetime.now()

                # send log to webapp with interval 30min:30sec
                case program_status.Is.SEND_LOG_TO_WEBAPP.value:
                    print("Send log to webapp")
                    try:
                        rest_api.send_log_data_to_webapp()
                    except Exception as e:
                        exception_logging.logException(e, "send Log to Webapp")

                    start_log_time = datetime.now()

                # if no other case is true than the alarm status of the sensorstations get checked
                case program_status.Is.CHECK_SENSOR_STATION_ALARM.value:
                    print("check sensorstation alarm")
                    try:
                        check_boarder_values.check_sensor_station_alarm()
                    except Exception as e:
                        exception_logging.logException(e, "check alarm")

                # called every 5min:10sec
                case program_status.Is.CHECK_FOR_NEW_BOARDER_AND_INTERVAL_VALUES.value:
                    print("check webapp for new boarder values")
                    try:
                        rest_api.read_sensor_boarder_values()
                        rest_api.read_sending_interval()
                    except Exception as e:
                        exception_logging.logException(e, "read boarders")

        except Exception as e:
            exception_logging.logException(e, " Programm breakdown")