from datetime import datetime, timedelta
import DB_connection

class measurement_tuple(object):
    def __init__(self, name: str, measurement_interval: timedelta):
        self.name = name
        self.measurement_interval: measurement_interval

class webapp_tuple(object):
    def __init__(self, name: str, webapp_interval: timedelta):
        self.name = name
        self.webapp_interval: webapp_interval

class start_time_tuple(object):
    def __init__(self, name: str, start_time: datetime):
        self.name = name
        self.start_time: start_time

def get_measurement_interval():
    station_list = DB_connection.read_station_interval_Database()
    measurement_tuple_list = []
    for station in station_list:
        m_tuple = measurement_tuple(name=station[0], measurement_interval=timedelta(station[1] * 60))
        measurement_tuple_list.append(m_tuple)

    return measurement_tuple_list

def get_webapp_interval():
    station_list = DB_connection.read_station_interval_Database()
    webapp_tuple_list = []
    for station in station_list:
        w_tuple = webapp_tuple(name=station[0], webapp_interval=timedelta(station[2] * 60))
        webapp_tuple_list.append(w_tuple)

    return webapp_tuple_list

def get_start_time(name):
    station_list = DB_connection.read_station_interval_Database()
    for station in station_list:
        if name == station[0]:
            return start_time_tuple(name=station[0], start_time=datetime.now())
