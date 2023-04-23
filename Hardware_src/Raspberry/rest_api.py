import sqlite3

import cursor as cursor
import requests

auth = ("admin", "passwd")

measurements_url = "http://localhost:8080/api/measurements"
sensorStations_url = "http://localhost:8080/model/SensorStation"





class SensorValue(object):
    def __init__(self, sensorStation: str, sensor_id: str, value: str, time_stamp: str):
        self.sensorStation = sensorStation
        self.sensor_id = sensor_id
        self.value = value
        self.time_stamp = time_stamp


def writeValueToWebApp():

    conn = sqlite3.connect('AccessPoint')
    cur = conn.cursor()


    all_sensorstations_sql = "SELECT * FROM Sensorstation;"
    all_sensorstations = cur.execute(all_sensorstations_sql).fetchall()

    for sensorstations in all_sensorstations:

        get_all_sensors = cur.execute('''
                                SELECT * FROM Sensor WHERE station_name = "{0}"
                            '''.format(sensorstations[0]))


        for sensor in get_all_sensors:
            sensor_id = sensor[0]
            print(sensor_id)

            values = cur.execute('''
                        SELECT * FROM Value WHERE sensor_id = {0}
                    '''.format(sensor_id))

            for value in values:

                sensor_id_string = str(value[2])
                time_stamp_string = str(value[1])

                temp_sensor_value = SensorValue(sensorStation=sensor[2], sensor_id=sensor_id_string, value=value[0], time_stamp=time_stamp_string)
                r = requests.post(measurements_url, json=vars(temp_sensor_value), auth=auth)
                print(r.status_code)




    cur.close()
    conn.close()


def getSensorstations():

    temp_sensorstation_names = []

    response = requests.get(sensorStations_url, auth=auth)

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
        added_sensorstation_list.append(sensorstations)

    webapp_sensorstation_names = getSensorstations()

    tempList =[]

    for name in webapp_sensorstation_names:
        if name not in added_sensorstation_list:
            tempList.append(name)


    return tempList