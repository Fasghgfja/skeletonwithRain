import define as define

import DB_connection
from datetime import datetime
import ble_service_connection
import asyncio
import rest_api
import exception_logging
NAME = 0

def checkBoarderValues():
    # select count of sensorstations
    connected_sensor_stations_list = DB_connection.read_Sensor_Station_Database().fetchall()
    for station in connected_sensor_stations_list:
        alarm_switch = station[2]
        for sensor in DB_connection.read_sensors_database(station[NAME]).fetchall():
            upper_value = sensor[5]
            lower_value = sensor[6]
            alarm_count = sensor[4]
            uuid = sensor[1]
            current_value_breaks = 0
            current_value_list = DB_connection.read_value_from_database(sensor[NAME]).fetchall()
            if alarm_count == -1 and alarm_switch == "on":
                webapp_alarm_switch = rest_api.getSensorstations(False, station[NAME])
                if webapp_alarm_switch == "fixed":
                    alarm_switch = update_alarm_switch(station[NAME], uuid, station[1], sensor[0])
            else:
                for value in current_value_list:
                    if value[0] < lower_value or value[0] > upper_value:
                        current_value_breaks += 1

                if alarm_count != -1 and ((len(current_value_list) - current_value_breaks) < ((len(current_value_list) * 3) / 4)):
                    alarm_count += 1

                if alarm_count > 5 and alarm_switch == "off":
                    asyncio.run(ble_service_connection.writeAlarmSignal(uuid, "ON", station[NAME]))
                    alarm_count = -1
                    alarm_switch = "on"
                    DB_connection.update_sensor_station_database(alarm_switch, station[NAME])
                    rest_api.write_alarm_switch(station[NAME], alarm_switch, station[1])
                if alarm_count != sensor[4]:
                    DB_connection.update_sensor_database(alarm_count, sensor[0])
                    rest_api.update_Sensor(sensor[0], alarm_count)

    # end of while
    exception_logging.log_information("INFO: Boarder values have been checked")

def check_sensor_station_alarm():
    connected_sensor_stations_list = DB_connection.read_Sensor_Station_Database().fetchall()
    for station in connected_sensor_stations_list:
        alarm_switch = station[2]
        if alarm_switch == "on":
            for sensor in DB_connection.read_sensors_database(station[NAME]).fetchall():
                alarm_count = sensor[4]
                uuid = sensor[1]
                if alarm_count == -1:
                    update_alarm_switch(station[NAME], uuid, station[1], sensor[0])

def update_alarm_switch(station_name, uuid, description, sensor_id):
    webapp_alarm_switch = rest_api.getSensorstations(False, station_name)
    if webapp_alarm_switch == "fixed":
        alarm_switch = "off"
        asyncio.run(ble_service_connection.writeAlarmSignal(uuid, "OFF", station_name))
        DB_connection.update_sensor_station_database(alarm_switch, station_name)
        DB_connection.update_sensor_database(0,sensor_id)
        rest_api.write_alarm_switch(station_name, alarm_switch, description)
        rest_api.update_Sensor(sensor_id, 0)