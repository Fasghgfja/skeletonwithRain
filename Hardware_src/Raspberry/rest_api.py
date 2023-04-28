import sqlite3

import cursor as cursor
import requests
import DB_connection
import exception_logging

auth = ("admin", "passwd")

measurements_url = "http://localhost:8080/api/measurements"
get_sensorStations_url = "http://localhost:8080/api/sensorstations"
post_sensorStations_url = "http://localhost:8080/api/sensorstations"
post_sensor_url = "http://localhost:8080/api/sensors"





class SensorValue(object):
    def __init__(self, sensorStation: str, sensor_id: str, value: str, time_stamp: str):
        self.sensorStation = sensorStation
        self.sensor_id = sensor_id
        self.value = value
        self.time_stamp = time_stamp

class StationValue(object):
    def __init__(self, name: str, service_description: str, alarm_switch: str):
        self.name = name
        self.service_description = service_description
        self.alarm_switch = alarm_switch

class Sensor(object):
    def __init__(self, sensor_id: int, uuid: str, station_name: str, type: str, alarm_count: int):
        self.sensor_id = sensor_id
        self.uuid = uuid
        self.station_name = station_name
        self.type = type
        self.alarm_count = alarm_count

def writeValueToWebApp():

    conn = sqlite3.connect('AccessPoint')
    cur = conn.cursor()

    # TODO call database querys via DB_connection.xxx
    all_sensorstations_sql = "SELECT * FROM Sensorstation;"
    all_sensorstations = cur.execute(all_sensorstations_sql).fetchall()

    for sensorstations in all_sensorstations:

        get_all_sensors = cur.execute('''
                                SELECT * FROM Sensor WHERE station_name = "{0}"
                            '''.format(sensorstations[0])).fetchall()

        for sensor in get_all_sensors:

            values = cur.execute('''
                        SELECT * FROM Value WHERE sensor_id = {0}
                    '''.format(sensor[0])).fetchall()
            for value in values:

                sensor_id_string = str(value[2])
                time_stamp_string = str(value[1])

                temp_sensor_value = SensorValue(sensorStation=sensor[2], sensor_id=sensor_id_string, value=value[0], time_stamp=time_stamp_string)
                r = requests.post(measurements_url, json=vars(temp_sensor_value), auth=auth)
                if r.status_code == 500:
                    DB_connection.delete_values(sensor[0], value[1])
    cur.close()
    conn.close()

def write_sensors_and_station_description(station_names):
    sensor_stationnames = DB_connection.read_Sensor_Station_Database().fetchall()

    for station in sensor_stationnames:
        if station[0] in station_names:
            try:
                station_values = StationValue(name=station[0], service_description=station[1], alarm_switch=station[2])
                requests.post(post_sensorStations_url, json=vars(station_values), auth=auth)
                sensor_list = DB_connection.read_sensors_database(station[0]).fetchall()
                for sensor in sensor_list:
                    sensor_values = Sensor(sensor_id=sensor[0], uuid=sensor[1], station_name=sensor[2], type=sensor[3], alarm_count=sensor[4])
                    requests.post(post_sensor_url, json=vars(sensor_values), auth=auth)
            except Exception as e:
                exception_logging.logException(e, station[0])
    # TODO check if all new stations are added

def write_alarm_switch(name, alarm_switch):
    try:
        station_values = StationValue(name=name, service_description="", alarm_switch=alarm_switch)
        requests.post(post_sensorStations_url, json=vars(station_values), auth=auth)
    except Exception as e:
        exception_logging.logException(e, "write alarm_switch")
def getSensorstations():

    temp_sensorstation_names = []

    response = requests.get(get_sensorStations_url, auth=auth)

    if response.status_code == 200:
        data = response.json()
        for sensor in data:
            temp_sensorstation_names.append(sensor["sensorStationID"])
    else:
        print("Fehler beim Abrufen der Daten. Status Code:", response.status_code)

    print(temp_sensorstation_names)
    return temp_sensorstation_names


def checkIfNewStations():

    conn = sqlite3.connect('AccessPoint')
    cur = conn.cursor()

    already_added_sensorstation_list = []
    already_added_SensorStations = cur.execute('''
                                    SELECT name FROM Sensorstation
                                ''')

    for sensorstations in already_added_SensorStations:
        already_added_sensorstation_list.append(sensorstations)

    webapp_sensorstation_names = getSensorstations()

    tempList =[]

    for name in webapp_sensorstation_names:
        if name not in already_added_sensorstation_list:
            tempList.append(name)


    return tempList