import os
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

SECTION_SLEEP = 15
#  cronjop to restart
program_state = 0
if __name__ == '__main__':
    exception_logging.log_success("Application startup")
    file1 = open("application_properties.txt", "w")


    print("{0} --- Application startup".format(datetime.now().strftime("%D %H:%M:%S")))
    print("                      Implement database this will need 15 seconds")
    DB_connection.implement_database()
    #time.sleep(SECTION_SLEEP)
    print("{0} --- SUCCESS: Implement database".format(datetime.now().strftime("%D %H:%M:%S")))
    print("{0} --- Initialize intervals".format(datetime.now().strftime("%D %H:%M:%S")))
    program_state = 0
    bleak_bug_counter = 0
    program = 0
    run = True
    # list that contains all start timer for measurement of the sensor stations
    start_measurement_interval_time_list = interval_service.get_all_start_times()
    # list that contains all measurement intervals
    delta_measurement_list =[]
    # list that contains all station they need to measure values
    measurement_station_list = []
    # list that contains all start timer for webapp interval of the sensor stations
    start_webapp_interval_time_list = interval_service.get_all_start_times()
    # list that contains all webapp intervals
    delta_webapp_list = []
    # list that contains all stations they need to send their values to the webapp
    webapp_station_list = []
    # send logs timer
    start_log_time = datetime.now()
    log_delta = timedelta(seconds=1830) #30:30
    print("                      Send logs to webapp interval set to:                           {0} min".format(log_delta))
    file1.write("Send logs to webapp interval set to:                           {0} min\n".format(log_delta))
    # call boarder values and sending intervals from webapp timer
    start_check_webapp_data_time = datetime.now()
    check_webapp_delta = timedelta(seconds=310) # 5:10
    print("                      Call sensor station data from webapp interval set to:          {0} min".format(check_webapp_delta))
    file1.write("Call sensor station data from webapp interval set to:          {0} min\n".format(check_webapp_delta))
    # call webapp for new connected Sensor Stations timer (must be under pair time of 5 minutes)
    start_call_new_station_time = datetime.now()
    call_station_delta = timedelta(seconds=100) # 3:20
    print("                      Call sensor station to connect from webapp interval set to:    {0} min".format(call_station_delta))
    file1.write("Call sensor station to connect from webapp interval set to:    {0} min\n".format(call_station_delta))
    # check if an alarm of a Sensor Station is on timer
    start_check_alarm_time = datetime.now()
    check_alarm_delta = timedelta(seconds=260) # 4:20
    print("                      Prove sensor station alarm interval set to:                    {0} min".format(check_alarm_delta))
    file1.write("Prove sensor station alarm interval set to:                    {0} min\n".format(check_alarm_delta))
    # searching for devices nearby
    start_search_for_devices_time = datetime.now()
    search_delta = timedelta(seconds=60) # 0:15
    print("                      Search for new sensor stations via BLE interval set to:        {0} min".format(search_delta))
    file1.write("Search for new sensor stations via BLE interval set to:        {0} min\n".format(search_delta))
    #time.sleep(SECTION_SLEEP)
    found_devices = []
    file1.close()
    print("                      Interval properties added to application_proprties.txt")
    print("                      Intervals initialized program loop starts -> ok")
    while run:
        if bleak_bug_counter > 5:
            exception_logging.log_success("Shutdown caused by Bleak ERROR")
            #os.system("bash start_access_point.sh")
            break
        try:
            try:
                if len(DB_connection.read_station_interval_Database()) != len(start_measurement_interval_time_list):
                    start_measurement_interval_time_list = interval_service.get_all_start_times()
                elif len(DB_connection.read_station_interval_Database()) != len(start_webapp_interval_time_list):
                    delta_measurement_list = interval_service.get_measurement_interval()
                delta_webapp_list = interval_service.get_webapp_interval()
                measurement_station_list = interval_service.station_interval_passed(start_measurement_interval_time_list, delta_measurement_list)
                webapp_station_list = interval_service.station_interval_passed(start_webapp_interval_time_list,delta_webapp_list)
            except Exception as e:
                exception_logging.logException(e, "Read intervals from database")
            #-------------------------------------------------------------------------------------------------
            try:
                if measurement_station_list is None:
                    measurement_station_list = []
                if webapp_station_list is None:
                    webapp_station_list = []
                # time evaluations for program state
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

                    else:
                        time.sleep(5)
                        # program_state = program_status.Is.READ_SENSOR_VALUES.value
                        program_state = -1
            except Exception as e:
                exception_logging.logException(e, "Time evaluation")
                program_state = -1
            #-------------------------------------------------------------------------------------------------



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
                        if new_device_name_list[0] == "AP deleted stop program":
                            program = "AP deleted stop program"
                        print("                      New device found: {0}".format(new_device_name_list[0]))
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
                            program = asyncio.run(rest_api.write_sensors_and_station_description(new_device_name_list))
                            print("                      Write new station to Webapp -> ok")
                        except Exception as e:
                            exception_logging.logException(e, "Write new SensorStation to Webapp")
                    else:
                        print("                      No new stations to connect")
                #-------------------------------------------------------------------------------------------------

                # call time every 1:30 minutes
                case program_status.Is.SEARCH_FOR_DEVICES.value:
                    print("{0} --- Search for sensor stations".format(datetime.now().strftime("%D %H:%M:%S")))
                    found_devices = asyncio.run(ble_service_connection.search())
                    if len(found_devices) > 0:
                        if found_devices[0] == "error":
                            bleak_bug_counter += 1
                        else:
                            rest_api.send_possible_devices_to_webapp(found_devices)

                #-------------------------------------------------------------------------------------------------

                # call time set by measurment intervall
                case program_status.Is.READ_SENSOR_VALUES.value:
                    print("{0} --- Read Sensor data".format(datetime.now().strftime("%D %H:%M:%S")))
                    try:
                        asyncio.run(ble_service_connection.read_sensor_data(False, measurement_station_list))
                        print("                      Read measurements -> ok")
                    except Exception as e:
                        print("                      Read measurements -> fail")
                        exception_logging.logException(e, "call_read_values")
                #-------------------------------------------------------------------------------------------------
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
                        program = rest_api.write_value_to_web_app(webapp_station_list)
                        print("                      Write values to Webapp -> ok")

                    except Exception as e:
                        exception_logging.logException(e, "rest_api write values")
                #-------------------------------------------------------------------------------------------------
                # send log to webapp with interval 30min:30sec
                case program_status.Is.SEND_LOG_TO_WEBAPP.value:
                    print("{0} --- Send log to webapp".format(datetime.now().strftime("%D %H:%M:%S")))
                    try:
                        program = rest_api.send_log_data_to_webapp(False)
                        print("                      Write log to Webapp -> ok")
                    except Exception as e:
                        exception_logging.logException(e, "send Log to Webapp")
                #-------------------------------------------------------------------------------------------------


                # if no other case is true than the alarm status of the sensor stations get checked
                case program_status.Is.CHECK_SENSOR_STATION_ALARM.value:
                    print("{0} --- check sensor station alarm".format(datetime.now().strftime("%D %H:%M:%S")))
                    try:
                        check_boarder_values.check_sensor_station_alarm()
                        print("                      Check sensor station alarm -> ok")
                    except Exception as e:
                        exception_logging.logException(e, "check alarm")
                #--------------------------------------------------------------------------------------------------

                # called every 5min:10sec
                case program_status.Is.CALL_SENSOR_STATION_DATA.value:
                    print("{0} --- check webapp for new data".format(datetime.now().strftime("%D %H:%M:%S")))
                    try:
                        program = rest_api.read_sensor_station_data()
                        print("                      Call sensor station data -> ok")
                    except Exception as e:
                        exception_logging.logException(e, "read sensor station data")
                #-------------------------------------------------------------------------------------------------

        except Exception as e:
            exception_logging.logException(e, " Major exception caught")
        if program != 0 and program is not None:
            os.system("rm AccessPoint")
            run = False
    rest_api.send_log_data_to_webapp(True)
