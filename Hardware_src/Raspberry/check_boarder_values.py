import DB_connection
from datetime import datetime
import ble_service_connection
import asyncio

def checkBoarderValues():
    # TODO
    # select count of sensorstations
    connected_sensor_stations_list = DB_connection.read_Sensor_Station_Database().fetchall()
    for station in connected_sensor_stations_list:
        alarm_switch = station[2]
        for sensor in DB_connection.read_sensors_database(station[0]).fetchall():
            upper_value = sensor[5]
            lower_value = sensor[6]
            alarm_count = sensor[4]
            uuid = sensor[1]
            current_value_breaks = 0
            current_value_list = DB_connection.read_value_from_database(sensor[0]).fetchall()
            if alarm_count == -1 and alarm_switch == "on":
                # TODO call Webapp via REST value alarm_switch
                alarm_switch = "fixed"
                if alarm_switch == "fixed":
                    alarm_switch = "off"
                    asyncio.run(ble_service_connection.writeAlarmSignal(uuid, "OFF", station[0]))
                    DB_connection.update_sensor_station_database(alarm_switch, station[0])
                    DB_connection.update_sensor_database(0,sensor[0])
            else:
                for value in current_value_list:
                    if value[0] < lower_value or value[0] > upper_value:
                        current_value_breaks += 1

                if (len(current_value_list) - current_value_breaks) < ((len(current_value_list) * 3) / 4):
                    alarm_count += 1

                if alarm_count > 5 and station[2] == "off":
                    asyncio.run(ble_service_connection.writeAlarmSignal(uuid, "ON", station[0]))
                    alarm_count = -1
                    DB_connection.update_sensor_station_database("on", station[0])
                    # TODO update Sensorstation alarm_switch webapp
                if alarm_count != sensor[4]:
                    DB_connection.update_sensor_database(alarm_count, sensor[0])
                    # Todo update Sensor at Webapp

    # end of while
    file1 = open("logFile.txt", "a")
    file1.write("INFO: Boarder values have been checked at {0}\n".format(datetime.now().strftime("%D__%H:%M:%S")))
    file1.close()