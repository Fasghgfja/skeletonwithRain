from datetime import datetime, timedelta
import interval_service
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
    exception_logging.log_success("Application startup")
    print("{0} --- Application startup".format(datetime.now().strftime("%D %H:%M:%S")))
    print("                      Implement database this will need 15 seconds")
    DB_connection.implement_database()
    #time.sleep(SECTION_SLEEP)
    print("{0} --- SUCCESS: Implement database".format(datetime.now().strftime("%D %H:%M:%S")))
    print("{0} --- Initialize intervals".format(datetime.now().strftime("%D %H:%M:%S")))
    program_state = 0
    # list that contains all start timer for measurement of the sensor stations
    start_measurement_interval_time_list = interval_service.get_all_start_times()
    # list that contains all measurement intervals
    delta_measurement_list =[]
    # list that contains all station they need to measure values
    measurement_station_list = []

    start_webapp_interval_time = datetime.now()
    delta_webapp = 0
    # list that contains all start timer for webapp interval of the sensor stations
    start_webapp_interval_time_list = interval_service.get_all_start_times()
    # list that contains all webapp intervals
    delta_webapp_list = []
    # list that contains all stations they need to send their values to the webapp
    webapp_station_list = []
    # send logs timer
    start_log_time = datetime.now()
    log_delta = timedelta(seconds=1830) #30:30
    # call boarder values and sending intervals from webapp timer
    start_check_webapp_data_time = datetime.now()
    check_webapp_delta = timedelta(seconds=310) # 5:10
    # call webapp for new connected Sensor Stations timer (must be under pair time of 5 minutes)
    start_call_new_station_time = datetime.now()
    call_station_delta = timedelta(seconds=200) # 3:20
    # check if an alarm of a Sensor Station is on timer
    start_check_alarm_time = datetime.now()
    check_alarm_delta = timedelta(seconds=260) # 4:20
    #time.sleep(SECTION_SLEEP)
    print("                      Intervals initialized program loop starts -> ok")
    while True:
        try:


            try:
                delta_measurement_list = interval_service.get_measurement_interval()
                delta_webapp_list = interval_service.get_webapp_interval()
                #delta_measurement = timedelta(minutes=config_yaml.read_sending_intervalls()[0])
                #delta_webapp = timedelta(minutes=2)
                measurement_station_list = interval_service.station_interval_passed(start_measurement_interval_time_list, delta_measurement_list)
                webapp_station_list = interval_service.station_interval_passed(start_webapp_interval_time_list,delta_webapp_list)
            except Exception as e:
                exception_logging.logException(e, "Read intervals from database")


            # time evaluations for program state
            if (start_call_new_station_time + call_station_delta) < datetime.now():
                program_state = program_status.Is.CHECK_WEBAPP_FOR_NEW_SENSORSTATION.value

            elif (start_check_webapp_data_time + check_webapp_delta) < datetime.now():
                program_state = program_status.Is.CALL_SENSOR_STATION_DATA.value

            elif (start_check_alarm_time + check_alarm_delta) < datetime.now():
                program_state = program_status.Is.CHECK_SENSOR_STATION_ALARM.value

            else:
                if (start_log_time + log_delta) < datetime.now():
                    program_state = program_status.Is.SEND_LOG_TO_WEBAPP.value

                elif len(measurement_station_list) > 0:
                    program_state = program_status.Is.READ_SENSOR_VALUES.value

                elif len(webapp_station_list) > 0:
                    program_state = program_status.Is.WRITE_VALUES_TO_WEBAPP.value

                else:
                    time.sleep(5)
                    # program_state = program_status.Is.CHECK_FOR_NEW_BOARDER_AND_INTERVAL_VALUES.value
                    program_state = -1




            match program_state:

                # call new SensorStations all 3min:20sec
                case program_status.Is.CHECK_WEBAPP_FOR_NEW_SENSORSTATION.value:
                    print("{0} --- Call for new Sensor station".format(datetime.now().strftime("%D %H:%M:%S")))
                    new_device_name_list = []
                    try:
                        new_device_name_list = rest_api.check_if_new_stations()
                        print("                      Call new names -> ok")
                    except Exception as e:
                        exception_logging.logException(e, "Call webapp for new sensorstations")

                    if len(new_device_name_list) > 0:
                        print("                      New device found: {0}".format(new_device_name_list[0]))
                        print()
                        # search for new SensorStation
                        try:
                            asyncio.run(ble_service_connection.read_sensor_data(True, new_device_name_list))
                            start_measurement_interval_time_list.append(interval_service.get_start_time(new_device_name_list[0]))
                            start_webapp_interval_time_list.append(interval_service.get_start_time(new_device_name_list[0]))
                            print("                      Read new Sensor Station and initialize start timers -> ok")
                        except Exception as e:
                            exception_logging.logException(e, "Search for new SensorStation")
                        # write found stations to webapp
                        try:
                            asyncio.run(rest_api.write_sensors_and_station_description(new_device_name_list))
                            print("                      Write new station to Webapp -> ok")
                        except Exception as e:
                            exception_logging.logException(e, "Write new SensorStation to Webapp")
                    else:
                        print("                      No new stations to search")
                    start_call_new_station_time = datetime.now()

                # call time set by measurment intervall
                case program_status.Is.READ_SENSOR_VALUES.value:
                    print("{0} --- Read Sensor data".format(datetime.now().strftime("%D %H:%M:%S")))
                    try:
                        asyncio.run(ble_service_connection.read_sensor_data(False, measurement_station_list))
                        print("                      Read measurements -> ok")
                        for e in start_measurement_interval_time_list:
                            for s in measurement_station_list:
                                if e.name == s:
                                    e.start_time = datetime.now()
                    except Exception as e:
                        exception_logging.logException(e, "call_read_values")

                # call time is set by webapp interval
                # first the evaluation if boarder values are broken before sending values to webapp and delete successful send values
                case program_status.Is.WRITE_VALUES_TO_WEBAPP.value:
                    print("{0} --- write values to Webapp".format(datetime.now().strftime("%D %H:%M:%S")))
                    try:
                        check_boarder_values.check_boarder_values(webapp_station_list)
                        print("                      Check Boarders before sending values -> ok")
                    except Exception as e:
                        exception_logging.logException(e, "While checking boarder breaks")
                    try:
                        rest_api.write_value_to_web_app(webapp_station_list)
                        print("                      Write values to Webapp -> ok")
                        for e in start_webapp_interval_time_list:
                            for s in webapp_station_list:
                                if e.name == s:
                                    e.start_time = datetime.now()
                    except Exception as e:
                        exception_logging.logException(e, "rest_api write values")

                # send log to webapp with interval 30min:30sec
                case program_status.Is.SEND_LOG_TO_WEBAPP.value:
                    print("{0} --- Send log to webapp".format(datetime.now().strftime("%D %H:%M:%S")))
                    try:
                        rest_api.send_log_data_to_webapp()
                        print("                      Write log to Webapp -> ok")
                    except Exception as e:
                        exception_logging.logException(e, "send Log to Webapp")

                    start_log_time = datetime.now()

                # if no other case is true than the alarm status of the sensor stations get checked
                case program_status.Is.CHECK_SENSOR_STATION_ALARM.value:
                    print("{0} --- check sensor station alarm".format(datetime.now().strftime("%D %H:%M:%S")))
                    try:
                        check_boarder_values.check_sensor_station_alarm()
                        print("                      Check sensor station alarm -> ok")
                    except Exception as e:
                        exception_logging.logException(e, "check alarm")
                    start_check_alarm_time = datetime.now()

                # called every 5min:10sec
                case program_status.Is.CALL_SENSOR_STATION_DATA.value:
                    print("{0} --- check webapp for new data".format(datetime.now().strftime("%D %H:%M:%S")))
                    try:
                        rest_api.read_sensor_station_data()
                        print("                      Call sensor station data -> ok")
                    except Exception as e:
                        exception_logging.logException(e, "read sensor station data")
                    start_check_webapp_data_time = datetime.now()

        except Exception as e:
            exception_logging.logException(e, " Major exception caught")