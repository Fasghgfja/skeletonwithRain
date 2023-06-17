import DB_connection, ble_service_connection, rest_api, exception_logging
import asyncio
NAME = 0
ID = 0
DESCRIPTION = 1
ALARM_SWITCH = 2
ALARM_COUNT = 4
UPPER = 6
LOWER = 5
TYPE = 3
UUID = 1
def check_boarder_values(station_list):
    connected_sensor_stations_list = DB_connection.read_Sensor_Stationnames_Database()
    sensor_station_list = []
    for s in connected_sensor_stations_list:
        for e in station_list:
            if e == s[NAME]:
                sensor_station_list.append(s)

    update_alarm_count_list = []
    for station in sensor_station_list:
        alarm_switch = station[ALARM_SWITCH]#2
        sensor_list = DB_connection.read_sensors_database(station[NAME])
        for sensor in sensor_list.fetchall():
            if sensor[TYPE] != "ALARM_STATUS":#3
                upper_value = num_check(sensor[UPPER])#6
                lower_value = num_check(sensor[LOWER])#5
                alarm_count = sensor[ALARM_COUNT]#4
                uuid = sensor[1]#1
                current_value_breaks = 0
                current_value_list = DB_connection.read_value_from_database(sensor[NAME]).fetchall()
                if alarm_count == -1 and alarm_switch == "on":
                    update_alarm_switch(station[NAME], uuid, station[DESCRIPTION], sensor[ID], sensor[TYPE])
                else:
                    for value in current_value_list:
                        current_value = num_check(value[0])
                        if current_value < lower_value or current_value > upper_value:
                            current_value_breaks += 1

                    if alarm_count != -1 and ((len(current_value_list) - current_value_breaks) < ((len(current_value_list) * 3) / 4)):
                        alarm_count += 1

                    if alarm_count >= station[5] and alarm_switch == "off":
                        asyncio.run(ble_service_connection.write_alarm_signal(uuid, "ON", station[NAME]))
                        alarm_count = -1
                        alarm_switch = "on"
                        DB_connection.update_sensor_station_database(alarm_switch, station[NAME])
                        rest_api.write_alarm_switch(station[NAME], alarm_switch, station[DESCRIPTION])
                    if alarm_count != sensor[ALARM_COUNT]:
                        DB_connection.update_sensor_database(alarm_count, sensor[ID])
                        sensor_value = rest_api.Sensor(sensor_id=sensor[ID], uuid="",station_name=station[NAME], type=sensor[TYPE], alarm_count=alarm_count, upper_boarder="",lower_boarder="")
                        update_alarm_count_list.append(vars(sensor_value))


    # end of while
    if len(update_alarm_count_list) > 0:
        rest_api.update_Sensor(update_alarm_count_list)
    exception_logging.log_information("INFO: Boarder values have been checked")

def check_sensor_station_alarm():
    connected_sensor_stations_list = DB_connection.read_Sensor_Stationnames_Database()
    for station in connected_sensor_stations_list:
        alarm_switch = station[ALARM_SWITCH]
        if alarm_switch == "on":
            sensor = DB_connection.read_sensors_alarm_count(station[NAME]).fetchone()
            update_alarm_switch(station[NAME], sensor[UUID], station[DESCRIPTION], sensor[ID], sensor[TYPE])

def update_alarm_switch(station_name, uuid, description, sensor_id, sensor_type):
    webapp_alarm_switch =""
    alarm_switch_uuid = DB_connection.read_sensors_alarm_characteristic(station_name)[0]
    sensor_station_alarm_switch = asyncio.run(ble_service_connection.read_alarm_status(alarm_switch_uuid, station_name))
    value = int.from_bytes(sensor_station_alarm_switch, "little")
    print("alarm value: " + str(value))
    if value != 1:
        webapp_alarm_switch = asyncio.run(rest_api.get_sensorstations(False, station_name))
    if webapp_alarm_switch == "fixed" or value == 1:
        update_alarm_count_list = []
        alarm_switch = "off"
        if value != 1:
            asyncio.run(ble_service_connection.write_alarm_signal(uuid, "OFF", station_name))
        DB_connection.update_sensor_station_database(alarm_switch, station_name)
        DB_connection.update_sensor_database(0,sensor_id)
        rest_api.write_alarm_switch(station_name, alarm_switch, description)
        sensor_value = rest_api.Sensor(sensor_id=sensor_id, uuid="",station_name=station_name, type=sensor_type, alarm_count=0, upper_boarder="",lower_boarder="")
        update_alarm_count_list.append(vars(sensor_value))
        rest_api.update_Sensor(update_alarm_count_list)

def num_check(num):
    if str(num).isalnum():
        return int(num)
    else:
        return float(num)