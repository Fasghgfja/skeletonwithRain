import sqlite3

import cursor as cursor
import requests

listOfSensorStations = []

auth = ("admin", "passwd")

measurements_url = "http://localhost:8080/api/measurements"
sensorStations_url = "http://localhost:8080/model/SensorStation"





class SensorValue(object):
    def __init__(self, sensorStationName: str, uuid: str, value: str):
        self.sensorStationName = sensorStationName
        self.uuid = uuid
        self.value = value


def writeValueToWebApp():

    conn = sqlite3.connect('AccessPoint')
    sql = "SELECT sensorstationname, uuid, value FROM meine_tabelle LIMIT 10;"
    temp_sql_values = cursor.execute(sql).fetchall()

    for entry in temp_sql_values:
        temp_sensor_value = SensorValue(sensorStationName=entry[0], uuid=entry[1], value=entry[2])
        r = requests.post(measurements_url, json=vars(temp_sensor_value), auth=auth)
        print(r.status_code)


def getSensorstations():

    temp_sensor_names = []

    response = requests.get(sensorStations_url, auth=auth)

    if response.status_code == 200:
        data = response.json()
        for sensor in data:
            if sensor["sensorStationID"] == "station1":
                temp_sensor_names.append(sensor["sensorStationID"])
    else:
        print("Fehler beim Abrufen der Daten. Status Code:", response.status_code)

    print(temp_sensor_names)
    return temp_sensor_names


def checkIfNewStations():

    sensor_names = getSensorstations()
    temp_flag = 0

    for name in sensor_names:
        if name in listOfSensorStations:
            temp_flag = 1


