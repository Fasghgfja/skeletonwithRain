import sqlite3

import cursor as cursor
import requests
import DB_connection
import exception_logging

# local
#auth = ("admin", "passwd")

#measurements_url = "http://localhost:8080/api/measurements"
#get_sensorStations_url = "http://localhost:8080/api/sensorstations"
#post_sensorStations_url = "http://localhost:8080/api/sensorstations"
#post_sensor_url = "http://localhost:8080/api/sensors"
#get_sensor_boarder_value_url = "http://localhost:8080/api/sensorsboardervalue"
#get_Station_alarm_switch_url = "http://localhost:8080/api/getsensorstations"
#post_update_sensor_url = "http://localhost:8080/api/updatesensors"

# server
auth = ("SHAdmin", "gsecret4passwordt2")
measurements_url = "http://srh-softwaresolutions.com/api/measurements"
get_sensorStations_url = "http://srh-softwaresolutions.com/api/sensorstations"
post_sensorStations_url = "http://srh-softwaresolutions.com/api/sensorstations"
post_sensor_url = "http://srh-softwaresolutions.com/api/sensors"
get_Station_alarm_switch_url = "http://srh-softwaresolutions.com/api/getsensorstations"
post_update_sensor_url = "http://srh-softwaresolutions.com/api/updatesensors"
get_sensor_boarder_value_url = "http://srh-softwaresolutions.com/api/sensorsboardervalue"

id = 50100

class SensorValue(object):
    def __init__(self, sensorStation: str, sensor_id: str, value: str, time_stamp: str, type:str):
        self.sensorStation = sensorStation
        self.sensor_id = sensor_id
        self.value = value
        self.time_stamp = time_stamp
        self.type = type

class StationValue(object):
    def __init__(self, name: str, service_description: str, alarm_switch: str):
        self.name = name
        self.service_description = service_description
        self.alarm_switch = alarm_switch

class Sensor(object):
    def __init__(self, sensor_id: int, uuid: str, station_name: str, type: str, alarm_count: int, upper_boarder: str, lower_boarder: str):
        self.sensor_id = sensor_id
        self.uuid = uuid
        self.station_name = station_name
        self.type = type
        self.alarm_count = alarm_count
        self.upperBoarder = upper_boarder
        self.lowerBoarder = lower_boarder

def writeValueToWebApp():

    conn = sqlite3.connect('AccessPoint')
    cur = conn.cursor()

    # TODO call database querys via DB_connection.xxx
    all_sensorstations_sql = "SELECT * FROM Sensorstation;"
    all_sensorstations = cur.execute(all_sensorstations_sql).fetchall()

    for sensorstations in all_sensorstations:
        # TODO call database querys via DB_connection.xxx
        get_all_sensors = cur.execute('''
                                SELECT * FROM Sensor WHERE station_name = "{0}"
                            '''.format(sensorstations[0])).fetchall()

        for sensor in get_all_sensors:
            # TODO call database querys via DB_connection.xxx
            values = cur.execute('''
                        SELECT * FROM Value WHERE sensor_id = {0}
                    '''.format(sensor[0])).fetchall()
            for value in values:

                sensor_id_string = str(value[2])
                time_stamp_string = str(value[1])

                temp_sensor_value = SensorValue(sensorStation=sensor[2], sensor_id=sensor_id_string, value=value[0], time_stamp=time_stamp_string, type=sensor[3])
                r = requests.post(measurements_url, json=vars(temp_sensor_value), auth=auth)
                if r.status_code == 200:
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
                    sensor_values = Sensor(sensor_id=sensor[0], uuid=sensor[1], station_name=sensor[2], type=sensor[3], alarm_count=sensor[4], upper_boarder="10000", lower_boarder="0" )
                    requests.post(post_sensor_url, json=vars(sensor_values), auth=auth)
            except Exception as e:
                exception_logging.logException(e, station[0])
    # TODO check if all new stations are added

def read_sensor_boarder_values():
    sensor_station_list = DB_connection.read_Sensor_Stationnames_Database().fetchall()
    for station in sensor_station_list:
        sensor_list = DB_connection.read_sensors_database(station[0]).fetchall()
        for sensor in sensor_list:
            sensor_id = sensor[0]
            sensor_value = Sensor(sensor_id=sensor_id, uuid="",station_name="", type="", alarm_count=0, upper_boarder="", lower_boarder="")
            response = requests.get(get_sensor_boarder_value_url, json=vars(sensor_value), auth=auth)
            if response.json()["lowerBoarder"] != sensor[5] or response.json()["upperBoarder"] != sensor[6]:
                DB_connection.update_boarder_value(sensor[0], response.json()["lowerBoarder"], response.json()["upperBoarder"])
            #if (sensor[4] != response.json()["alarm_count"]):
                #DB_connection.update_sensor_database(response.json()["alarm_count"], sensor[0])
            print("read_sensor_boarder")




def write_alarm_switch(name, alarm_switch, description):
    try:
        station_values = StationValue(name=name, service_description=description, alarm_switch=alarm_switch)
        requests.post(post_sensorStations_url, json=vars(station_values), auth=auth)
    except Exception as e:
        exception_logging.logException(e, "write alarm_switch")
def getSensorstations(getName, name):


    if getName:

        url = "{0}/{1}".format(get_sensorStations_url, id)
        response = requests.get(url, auth=auth)
        if response.status_code == 200:
            data = response.json()
            return data
        else:
            print("Fehler beim Abrufen der Daten. Status Code:", response.status_code)
    else:
        try:
            station_values = StationValue(name=name, service_description="", alarm_switch="")
            response = requests.get(get_Station_alarm_switch_url,json=vars(station_values), auth=auth)
            switch = response.content.decode()
            return switch # get alarm_switch

        except Exception as e:
            exception_logging.logException(e, "read alarm_switch from webapp")



def checkIfNewStations():

    conn = sqlite3.connect('AccessPoint')
    cur = conn.cursor()
    # TODO call database querys via DB_connection.xxx
    already_added_sensorstation_list = []
    already_added_SensorStations = cur.execute('''
                                    SELECT name FROM Sensorstation
                                ''')

    for sensorstations in already_added_SensorStations.fetchall():
        already_added_sensorstation_list.append(sensorstations[0])

    webapp_sensorstation_names = getSensorstations(True,"")

    for name in already_added_sensorstation_list:
        try:
            webapp_sensorstation_names.remove(name)
        except Exception as e:
            exception_logging.logException(e, "Filter New Stations")

    return webapp_sensorstation_names

def update_Sensor(sensor_id, alarm_count):
    try:
        sensor_value = Sensor(sensor_id=sensor_id, uuid="",station_name="", type="", alarm_count=alarm_count, upper_boarder="",lower_boarder="")
        requests.post(post_update_sensor_url, json=vars(sensor_value), auth=auth)
    except Exception as e:
        exception_logging.logException(e, "write alarm_count to webapp")