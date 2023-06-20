from datetime import datetime
import sqlite3
import struct
import exception_logging

#----------------------------------------Create Database
def implement_database():
    # TODO check database connection

    conn = sqlite3.connect('AccessPoint')
    c = conn.cursor()
    c.execute('''
            create table if not exists Sensorstation(
            name varchar(64) not null primary key,
            service_description varchar,
            alarm_switch varchar(15),
            measurement_interval int,
            webapp_interval int,
            treshold int)
          ''')

    c.execute('''
            create table if not exists Sensor(
            sensor_id int not null primary key,
            uuid varchar,
            station_name varchar(64) not null,
            sensor_type varchar,
            alarm_count int,
            lower_boarder varchar,
            upper_boarder varchar,
            FOREIGN KEY (station_name) REFERENCES Sensorstation(name) ON DELETE CASCADE);
        ''')

    c.execute('''
            create table if not exists Value(
            value varchar not null,
            time_stamp varchar(32),
            sensor_id int,
            FOREIGN KEY (sensor_id) REFERENCES Sensor(sensor_id) ON DELETE CASCADE);
        ''')
    conn.commit()

#----------------------------------------Insert Database
def insert_values_into_database(value, float_value, type, deviceName):
    if float_value:
        float_val = struct.unpack('f', value)[0]
        string_value = float_val
    else:
        string_value = int.from_bytes(value, "little")
    try:
        conn = sqlite3.connect('AccessPoint')
        c = conn.cursor()
        c.execute('''
                select sensor_id from sensor where sensor_type='{0}' and station_name='{1}'
        '''.format(type.decode(), deviceName))
        sensor_id = c.fetchone()[0]
        c = conn.cursor()
        c.execute('''
                insert into value values('{0}', '{2}', {1})
            '''.format(string_value, sensor_id, datetime.now().strftime("%d-%m-%Y %H:%M:%S")))
        conn.commit()
        file1 = open("logFile.txt", "a")
        file1.write("INFO: Value for type {0} on id {1} saved\n".format(type.decode(), sensor_id))
        file1.close()
    except Exception as e:
        exception_logging.logException(e, type)

def insert_new_sensor_station_to_database(attribute, name):
    try:
        conn = sqlite3.connect('AccessPoint')
        c = conn.cursor()
        c.execute('''
                insert into Sensorstation values('{0}', '{1}', 'off', 1, 30, 5)
            '''.format(name, attribute))
        conn.commit()
        file1 = open("logFile.txt", "a")
        file1.write("INFO: Sensor Station with name {0} has been inserted to the database\n".format(name))
        file1.close()
        print("saved")
    except Exception as e:
        exception_logging.logException(e, attribute)

def insert_new_sensor_to_database(attribute, name, type, sensor_index):
    try:
        conn = sqlite3.connect('AccessPoint')
        c = conn.cursor()
        c.execute('''
                insert into Sensor values( {0}, '{1}', '{2}', '{3}', {4}, {5}, {6})
            '''.format(sensor_index, attribute.uuid, name, type.decode(), 0, 0, 10000))
        conn.commit()
        exception_logging.log_information("INFO: Sensor with uuid {0} and type {1} from Station {2} has been inserted to the database".format(attribute.uuid, type.decode(), name))
    except Exception as e:
        exception_logging.logException(e, attribute.uuid)

#----------------------------------------Update Database
def update_sensor_station_database(alarm_switch, station_name):
    try:
        conn = sqlite3.connect('AccessPoint')
        c = conn.cursor()
        c.execute('''
                update Sensorstation set alarm_switch='{0}' where name='{1}'
            '''.format(alarm_switch, station_name))
        conn.commit()
        file1 = open("logFile.txt", "a")
        file1.write("INFO: SensorStation {0} has been updated alarm_switch to {1} \n".format(station_name, alarm_switch))
        file1.close()
    except Exception as e:
        exception_logging.logException(e, station_name)

def update_sensor_station_interval(station_name, measurement_interval, webapp_interval, treshold):
    try:
        conn = sqlite3.connect('AccessPoint')
        c = conn.cursor()
        c.execute('''
                update Sensorstation set measurement_interval={0}, webapp_interval={1}, treshold={2} where name='{3}'
            '''.format(measurement_interval, webapp_interval, treshold, station_name))
        conn.commit()
        file1 = open("logFile.txt", "a")
        file1.write("INFO: SensorStation {0} has been updated data\n".format(station_name))
        file1.close()
    except Exception as e:
        exception_logging.logException(e, station_name)
def update_sensor_database(alarm_count, sensor_id):
    try:
        conn = sqlite3.connect('AccessPoint')
        c = conn.cursor()
        c.execute('''
                update sensor set alarm_count={0} where sensor_id={1}
            '''.format(alarm_count, sensor_id))
        conn.commit()
        file1 = open("logFile.txt", "a")
        file1.write("INFO: Sensor with sensor_id {0} has been updated alarm_count to {1} \n".format(sensor_id, alarm_count))
        file1.close()
    except Exception as e:
        exception_logging.logException(e, sensor_id)

def update_boarder_value(sensor_id, upper_boarder, lower_boarder):
    try:
        conn = sqlite3.connect('AccessPoint')
        c = conn.cursor()
        c.execute('''
                update sensor set lower_boarder={0}, upper_boarder={1} where sensor_id={2}
            '''.format(lower_boarder, upper_boarder, sensor_id))
        conn.commit()
        file1 = open("logFile.txt", "a")
        file1.write("INFO: Sensor with sensor_id {0} has been updated upper_boarder to {1} and lower_boarder to {2} \n".format(sensor_id, upper_boarder, lower_boarder))
        file1.close()
    except Exception as e:
        exception_logging.logException(e, sensor_id)

#----------------------------------------Read Database
def read_Sensor_Stationnames_Database():
    try:
        conn = sqlite3.connect('AccessPoint')
        c = conn.cursor()
        c.execute('''
            select * from Sensorstation
        ''')
        return c.fetchall()
    except Exception as e:
        exception_logging.logException(e, "SensorStation")

def read_station_interval_Database():
    try:
        conn = sqlite3.connect('AccessPoint')
        c = conn.cursor()
        c.execute('''
            select name, measurement_interval, webapp_interval from Sensorstation
        ''')
        return c.fetchall()
    except Exception as e:
        exception_logging.logException(e, "SensorStation")
def read_value_from_database(sensor_id):
    try:
        conn = sqlite3.connect('AccessPoint')
        c = conn.cursor()
        c.execute('''
            select * from value where sensor_id={0}
        '''.format(sensor_id))
        return c
    except Exception as e:
        exception_logging.logException(e, "Value")
def read_sensors_database(name):
    try:
        conn = sqlite3.connect('AccessPoint')
        c = conn.cursor()
        c.execute('''
            select * from Sensor where station_name='{0}'
        '''.format(name))
        return c
    except Exception as e:
        exception_logging.logException(e, "Sensor")

def read_sensors_by_id(station, sensor_type):
    try:
        conn = sqlite3.connect('AccessPoint')
        c = conn.cursor()
        c.execute('''
            select * from Sensor where station_name='{0}' and sensor_type='{1}'
        '''.format(station, sensor_type))
        return c.fetchone()
    except Exception as e:
        exception_logging.logException(e, "Sensor by station and type")

def read_sensors_alarm_count(name):
    try:
        conn = sqlite3.connect('AccessPoint')
        c = conn.cursor()
        c.execute('''
            select * from Sensor where station_name='{0}' and alarm_count=-1
        '''.format(name))
        return c
    except Exception as e:
        exception_logging.logException(e, "Sensor")
def read_sensors_alarm_characteristic(name):
    try:
        count = 0
        conn = sqlite3.connect('AccessPoint')
        c = conn.cursor()
        c.execute('''
            select uuid from Sensor where station_name='{0}' and sensor_type='ALARM_STATUS'
        '''.format(name))
        # sensor count to get max index
        return c.fetchone()
    except Exception as e:
        exception_logging.logException(e, "Sensor")

def read_sensors():
    try:
        conn = sqlite3.connect('AccessPoint')
        c = conn.cursor()
        c.execute('''
            select sensor_id from Sensor 
        ''')
        return c.fetchall()
    except Exception as e:
        exception_logging.logException(e, "Sensor")

#----------------------------------------delete Database
def delete_values(id):
    try:
        conn = sqlite3.connect('AccessPoint')
        c = conn.cursor()
        c.execute('''
            delete from Value where sensor_id ='{0}'
        '''.format(id))
        conn.commit()
    except Exception as e:
        exception_logging.logException(e, "delete values")

def delete_sensor_station(name):
    try:
        conn = sqlite3.connect('AccessPoint')
        c = conn.cursor()
        c.execute('''
            delete from Sensorstation where name='{0}'
        '''.format(name))
        c.execute('''
            delete from Sensor where station_name='{0}'
        '''.format(name))
        conn.commit()
        exception_logging.log_information("Sensor Station {0} has been deleted".format(name))
    except Exception as e:
        exception_logging.logException(e, "delete Station")


