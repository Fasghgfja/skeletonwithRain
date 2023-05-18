import math
import asyncio
import requests
import DB_connection
import exception_logging
import datetime
import json

import config_yaml
# local
auth = ("admin", "passwd")
log_id = 0

post_log_url = "http://localhost:8080/"
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

def url_builder(link):
    url = "http://{0}/api/{1}".format(config_yaml.read_wepapp_ip(),link, config_yaml.read_accesspoint_id())
    return url
def get_auth():
    auth = (config_yaml.read_auth_params()[1], config_yaml.read_auth_params()[0])
    return auth

def check_validation():
    url = "{0}/{1}".format(url_builder("validated"), config_yaml.read_accesspoint_id())
    valid = requests.get(url, auth=get_auth())
    if valid.json():
        config_yaml.write_valitation()
def write_value_to_web_app():
    if config_yaml.read_validation_params():
        measurements_url = url_builder("measurements")
        send_value_list = []
        for sensorstations in DB_connection.read_Sensor_Stationnames_Database():
            for sensor in DB_connection.read_sensors_database(sensorstations[0]).fetchall():
                if sensor[3] != "ALARM_STATUS":
                    for value in DB_connection.read_value_from_database(sensor[0]):
                        sensor_id_string = str(value[2])
                        time_stamp_string = str(value[1])
                        temp_sensor_value = SensorValue(sensorStation=sensor[2], sensor_id=sensor_id_string, value=value[0], time_stamp=time_stamp_string, type=sensor[3])
                        send_value_list.append(vars(temp_sensor_value))
        r = requests.post(measurements_url, json=send_value_list, auth=get_auth())
        if r.status_code == 200:
            DB_connection.delete_values()
    else:
        check_validation()

async def write_sensors_and_station_description(station_names):
    if config_yaml.read_validation_params():
        sensor_stationnames = DB_connection.read_Sensor_Stationnames_Database()
        post_sensorStations_url = url_builder("sensorstations")
        post_sensor_url = url_builder("sensors")
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
                    r = requests.post(post_sensor_url, json=json_list,auth=get_auth())
                    if r.status_code != 200:
                        exception_logging.log_connection_exception("Did not write Sensors to webapp")
                except Exception as e:
                    exception_logging.logException(e, station[0])
    else:
        check_validation()

def read_sensor_boarder_values():
    if config_yaml.read_validation_params():
        url = "{0}/{1}".format(url_builder("sensorsboardervalue"), config_yaml.read_accesspoint_id())
        data = requests.get(url,auth=get_auth())
        sensor_list = data.json()
        for sensor in sensor_list:
            to_update_sensor = DB_connection.read_sensors_by_id(sensor["sensor_id"])
            if to_update_sensor[5] != sensor["lowerBoarder"] or to_update_sensor[6] != sensor["upperBoarder"]:
                DB_connection.update_boarder_value(sensor["sensor_id"], sensor["upperBoarder"], sensor["lowerBoarder"])

        print("read_sensor_boarder")
    else:
        check_validation()




def write_alarm_switch(name, alarm_switch, description):
    if config_yaml.read_validation_params():
        post_sensorStations_url = url_builder("sensorstations")
        try:
            station_values = StationValue(name=name, service_description=description, alarm_switch=alarm_switch)
            requests.post(post_sensorStations_url, json=vars(station_values), auth=get_auth())
        except Exception as e:
            exception_logging.logException(e, "write alarm_switch")
    else:
        check_validation()
async def get_sensorstations(getName, name):

    if getName:
        url = "{0}/{1}".format(url_builder("sensorstations"), config_yaml.read_accesspoint_id())
        response = requests.get(url, auth=get_auth())
        if response.status_code == 200:
            data = response.json()
            return data
        else:
            print("Fehler beim Abrufen der Daten. Status Code:", response.status_code)
    else:
        get_Station_alarm_switch_url = url_builder("getsensorstations")
        try:
            station_values = StationValue(name=name, service_description="", alarm_switch="")
            response = requests.get(get_Station_alarm_switch_url,json=vars(station_values), auth=get_auth())
            switch = response.content.decode()
            return switch # get alarm_switch

        except Exception as e:
            exception_logging.logException(e, "read alarm_switch from webapp")



def check_if_new_stations():
    if config_yaml.read_validation_params():
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
    else:
        check_validation()

def update_Sensor(alarm_count_list):
    if config_yaml.read_validation_params():
        post_update_sensor_url = url_builder("updatesensors")
        try:
            r = requests.post(post_update_sensor_url, json=alarm_count_list, auth=get_auth())
        except Exception as e:
            exception_logging.logException(e, "write alarm_count to webapp")
    else:
        check_validation()

def read_sending_interval():
    if config_yaml.read_validation_params():
        url = "{0}/{1}".format(url_builder("sendinterval"), config_yaml.read_accesspoint_id())
        try:
            r = requests.get(url, auth=get_auth())
            mes_int = r.json()["measurementInterval"]
            web_int = r.json()["webappSendInterval"]
            tr_ho = r.json()["alarmCountThreshold"]
            config_yaml.write_sending_intervalls(mes_int, web_int, tr_ho)


        except Exception as e:
            exception_logging.logException(e, "call sending interval")
    else:
        check_validation()



class Log_data(object):
    def __init__(self, text: str, subject: str, author: str, time_stamp: str, type: str):
        self.text = text
        self.subject = subject
        self.author = author
        self.time_stamp = time_stamp
        self.type = type


def handle_special_values(obj):
    if isinstance(obj, float) and (math.isnan(obj) or math.isinf(obj)):
        return str(obj)
    else:
        return None

def send_log_data_to_webapp():
    if config_yaml.read_validation_params():
        log_send_list = []
        id = str(config_yaml.read_accesspoint_id())
        url = url_builder("auditLog")
        with open('logFile.txt', 'r') as file:
            for line in file:

                if line.startswith('ERROR: On characteristic'):
                    error_msg = line.split('ERROR:', 1)[1].split('Date', 1)[0].strip()
                    datetime_str = line.rsplit('Date', 1)[-1].strip().replace('__', ' ')
                    time_stamp_string = str(datetime.datetime.strptime(datetime_str, '%m/%d/%y %H:%M:%S'))
                    if len(error_msg) > 100:
                        error_msg = "Could not connect to Server address"
                    temp_log_data = Log_data(text=error_msg, subject="Characteristics", author=id, time_stamp=time_stamp_string, type="ERROR")
                    log_send_list.append(vars(temp_log_data))

                elif line.startswith('ERROR: Could not'):
                    error_msg = line.split('ERROR:', 1)[1].split('Date', 1)[0].strip()
                    datetime_str = line.rsplit('Date', 1)[-1].strip().replace('__', ' ')
                    time_stamp_string = str(datetime.datetime.strptime(datetime_str, '%m/%d/%y %H:%M:%S'))

                    temp_log_data = Log_data(text=error_msg, subject="DEVICE", author=id, time_stamp=time_stamp_string, type="ERROR")
                    log_send_list.append(vars(temp_log_data))


                elif line.startswith('WARNING'):
                    error_msg = line.split('WARNING', 1)[1].split('Date', 1)[0].strip()
                    datetime_str = line.rsplit('Date', 1)[-1].strip().replace('__', ' ')
                    time_stamp_string = str(datetime.datetime.strptime(datetime_str, '%m/%d/%y %H:%M:%S'))

                    temp_log_data = Log_data(text=error_msg, subject="Characteristics", author=id, time_stamp=time_stamp_string, type="WARNING")
                    log_send_list.append(vars(temp_log_data))



        response = requests.post(url, json=log_send_list, auth=auth)
        if response.status_code == 200:
             with open('logFile.txt', 'w') as file:
                file.write("New File")
                file.close()
    else:
        check_validation()