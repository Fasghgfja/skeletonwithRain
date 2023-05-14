import sqlite3
import asyncio
import cursor as cursor
import requests
import DB_connection
import exception_logging
import config_yaml
# local
auth = ("admin", "passwd")
# config_yaml.read_wepapp_ip()
measurements_url = "http://localhost:8080/api/measurements"
get_sensorStations_url = "http://localhost:8080/api/sensorstations"
post_sensorStations_url = "http://localhost:8080/api/sensorstations"
post_sensor_url = "http://localhost:8080/api/sensors"
get_sensor_boarder_value_url = "http://localhost:8080/api/sensorsboardervalue"
get_Station_alarm_switch_url = "http://localhost:8080/api/getsensorstations"
post_update_sensor_url = "http://localhost:8080/api/updatesensors"
get_sendinterval_url = "http://localhost:8080/api/sendinterval"
# server
#auth = ("SHAdmin", "gsecret4passwordt2")
#measurements_url = "http://srh-softwaresolutions.com/api/measurements"
#get_sensorStations_url = "http://srh-softwaresolutions.com/api/sensorstations"
#post_sensorStations_url = "http://srh-softwaresolutions.com/api/sensorstations"
#post_sensor_url = "http://srh-softwaresolutions.com/api/sensors"
#get_Station_alarm_switch_url = "http://srh-softwaresolutions.com/api/getsensorstations"
#post_update_sensor_url = "http://srh-softwaresolutions.com/api/updatesensors"
#get_sensor_boarder_value_url = "http://srh-softwaresolutions.com/api/sensorsboardervalue"

id = 50100

class SensorValue(object):
    def __init__(self, sensorStation: str, sensor_id: str, value: str, time_stamp: str, type:str):
        self.sensorStation = sensorStation
        self.sensor_id = sensor_id
        self.value = value
        self.time_stamp = time_stamp
        self.type = type
class Value_List(object):
    def __int__(self, sensor_value : SensorValue):
        self.sensor_values = sensor_value
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

def write_value_to_web_app():

    conn = sqlite3.connect('AccessPoint')
    cur = conn.cursor()

    # TODO call database querys via DB_connection.xxx
    all_sensorstations_sql = "SELECT * FROM Sensorstation;"
    all_sensorstations = cur.execute(all_sensorstations_sql).fetchall()
    send_value_list = []
    for sensorstations in all_sensorstations:
        # TODO call database querys via DB_connection.xxx
        get_all_sensors = cur.execute('''
                                SELECT * FROM Sensor WHERE station_name = "{0}"
                            '''.format(sensorstations[0])).fetchall()

        for sensor in get_all_sensors:
            # TODO call database querys via DB_connection.xxx
            if sensor[3] != "ALARM_STATUS":
                values = cur.execute('''
                            SELECT * FROM Value WHERE sensor_id = {0}
                        '''.format(sensor[0])).fetchall()
                for value in values:

                    sensor_id_string = str(value[2])
                    time_stamp_string = str(value[1])

                    temp_sensor_value = SensorValue(sensorStation=sensor[2], sensor_id=sensor_id_string, value=value[0], time_stamp=time_stamp_string, type=sensor[3])
                    send_value_list.append(vars(temp_sensor_value))
    r = requests.post(measurements_url, json=send_value_list, auth=auth)
    if r.status_code == 200:
        DB_connection.delete_values()
    cur.close()
    conn.close()

async def write_sensors_and_station_description(station_names):
    sensor_stationnames = DB_connection.read_Sensor_Stationnames_Database()

    for station in sensor_stationnames:
        if station[0] in station_names:
            try:
                station_values = StationValue(name=station[0], service_description=station[1], alarm_switch=station[2])
                requests.post(post_sensorStations_url, json=vars(station_values), auth=auth)
                sensor_list = DB_connection.read_sensors_database(station[0]).fetchall()
                json_list =[]
                for sensor in sensor_list:
                    if sensor[3] != "ALARM_STATUS":
                        sensor_values = Sensor(sensor_id=sensor[0], uuid=sensor[1], station_name=sensor[2], type=sensor[3], alarm_count=sensor[4], upper_boarder="10000", lower_boarder="0" )
                        json_list.append(vars(sensor_values))
                r = requests.post(post_sensor_url, json=json_list,auth=auth)
                if r.status_code != 200:
                    exception_logging.log_connection_exception("Did not write Sensors to webapp")
            except Exception as e:
                exception_logging.logException(e, station[0])
    # TODO check if all new stations are added

def read_sensor_boarder_values():
    url = "{0}/{1}".format(get_sensor_boarder_value_url, config_yaml.read_accesspoint_id())
    data = requests.get(url,auth=auth)
    sensor_list = data.json()
    for sensor in sensor_list:
        DB_connection.update_boarder_value(sensor["sensor_id"], sensor["upperBoarder"], sensor["lowerBoarder"])
    # config_yaml.write_sending_intervalls(5,2)
    print("read_sensor_boarder")




def write_alarm_switch(name, alarm_switch, description):
    try:
        station_values = StationValue(name=name, service_description=description, alarm_switch=alarm_switch)
        requests.post(post_sensorStations_url, json=vars(station_values), auth=auth)
    except Exception as e:
        exception_logging.logException(e, "write alarm_switch")
async def get_sensorstations(getName, name):


    if getName:

        url = "{0}/{1}".format(get_sensorStations_url, config_yaml.read_accesspoint_id())
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



def check_if_new_stations():

    already_added_sensorstation_list = []
    for sensor_stations in DB_connection.read_Sensor_Stationnames_Database():
        already_added_sensorstation_list.append(sensor_stations[0])

    webapp_sensorstation_names = asyncio.run(get_sensorstations(True, ""))

    for name in already_added_sensorstation_list:
        try:
            webapp_sensorstation_names.remove(name)
        except Exception as e:
            DB_connection.delete_sensor_station(name)
            exception_logging.logException(e, "Filter New Stations")

    return webapp_sensorstation_names

def update_Sensor(alarm_count_list):
    try:
        r = requests.post(post_update_sensor_url, json=alarm_count_list, auth=auth)
    except Exception as e:
        exception_logging.logException(e, "write alarm_count to webapp")

def read_sending_interval():
    url = "{0}/{1}".format(get_sendinterval_url, config_yaml.read_accesspoint_id())
    try:
        r = requests.get(url, auth=auth)
        mes_int = r.json()["measurementInterval"]
        web_int = r.json()["webappSendInterval"]
        config_yaml.write_sending_intervalls(mes_int, web_int)
    except Exception as e:
        exception_logging.logException(e, "call sending interval")