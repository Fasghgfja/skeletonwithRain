import struct

import define as define

import DB_connection
from datetime import datetime
import ble_service_connection
import asyncio
import rest_api
import exception_logging
NAME = 0

def checkBoarderValues():
    connected_sensor_stations_list = DB_connection.read_Sensor_Stationnames_Database()
    update_alarm_count_list = []
    for station in connected_sensor_stations_list:
        alarm_switch = station[2]
        sensor_list = DB_connection.read_sensors_database(station[NAME])
        for sensor in sensor_list.fetchall():
            if sensor[3] != "ALARM_STATUS":
                upper_value = sensor[5]
                lower_value = sensor[6]
                alarm_count = sensor[4]
                uuid = sensor[1]
                current_value_breaks = 0
                current_value_list = DB_connection.read_value_from_database(sensor[NAME]).fetchall()
                if alarm_count == -1 and alarm_switch == "on":
                    #webapp_alarm_switch = rest_api.get_sensorstations(False, station[NAME])
                    #if webapp_alarm_switch == "fixed":
                    alarm_switch = update_alarm_switch(station[NAME], uuid, station[1], sensor[0])
                else:
                    for value in current_value_list:
                        upper = num_check(upper_value)
                        lower = num_check(lower_value)
                        current_value = num_check(value[0])
                        if current_value < lower or current_value > upper:
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
                        sensor_value = rest_api.Sensor(sensor_id=sensor[0], uuid="",station_name="", type="", alarm_count=alarm_count, upper_boarder="",lower_boarder="")
                        update_alarm_count_list.append(vars(sensor_value))


    # end of while
    if len(update_alarm_count_list) > 0:
        rest_api.update_Sensor(update_alarm_count_list)
    exception_logging.log_information("INFO: Boarder values have been checked")

def check_sensor_station_alarm():
    connected_sensor_stations_list = DB_connection.read_Sensor_Stationnames_Database()
    for station in connected_sensor_stations_list:
        alarm_switch = station[2]
        if alarm_switch == "on":
            sensor = DB_connection.read_sensors_alarm_count(station[NAME]).fetchone()
            update_alarm_switch(station[NAME], sensor[1], station[1], sensor[0])

def update_alarm_switch(station_name, uuid, description, sensor_id):
    webapp_alarm_switch =""
    alarm_switch_uuid = DB_connection.read_sensors_alarm_characteristic(station_name)[0]
    sensor_station_alarm_switch = asyncio.run(ble_service_connection.read_alarm_status(alarm_switch_uuid, station_name))
    string_value = int.from_bytes(sensor_station_alarm_switch, "little")
    if string_value != 1:
        webapp_alarm_switch = rest_api.get_sensorstations(False, station_name)
    if webapp_alarm_switch == "fixed" or string_value == 1:
        alarm_switch = "off"
        if string_value != 1:
            asyncio.run(ble_service_connection.writeAlarmSignal(uuid, "OFF", station_name))
        DB_connection.update_sensor_station_database(alarm_switch, station_name)
        DB_connection.update_sensor_database(0,sensor_id)
        rest_api.write_alarm_switch(station_name, alarm_switch, description)
        # rest_api.update_Sensor(sensor_id, 0)

def num_check(num):
    if str(num).isalnum():
        return int(num)
    else:
        return float(num)