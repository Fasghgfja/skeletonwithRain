# https://github.com/hbldh/bleak
# https://github.com/hbldh/bleak/blob/master/examples/service_explorer.py
import struct
import asyncio
import time
import sqlite3
import array as devices
from bleak import BleakClient, BleakScanner
from datetime import datetime


#device_name = "SensorStation GxTy"
program_state = 0

device_name = "G4T2"
hygro_alarm_characteristic_uuid = "000019b1-0000-1000-8000-00805f9b34fb"
ligth_alarm_characteristic_uuid = "000019b0-0000-1000-8000-00805f9b34fb"
temp_alarm_characteristic_uuid = "000019b2-0000-1000-8000-00805f9b34fb"
hum_alarm_characteristic_uuid = "000019b3-0000-1000-8000-00805f9b34fb"
press_alarm_characteristic_uuid = "000019b4-0000-1000-8000-00805f9b34fb"
gas_alarm_characteristic_uuid = "000019b5-0000-1000-8000-00805f9b34fb"
# device_name = "A52 von stefan"

# writeValue is used to write the values to the database
def writeValue(value, is_float, type, characteristic):
    file1 = open("sensorfile.txt", "a")
    file1.write("\nSensor type:\t{0}\nuuid:\t\t\t{1}\n".format(type.decode(), characteristic.uuid))
    if is_float:
        format = "Value:\t\t\t{float_value:.2f} "
        [extract_float_value] = struct.unpack("f", value)
        file1.write(format.format(float_value = extract_float_value))
    else:
        file1.write("Value:\t\t\t{0}".format(int.from_bytes(value, "little")))
    file1.close()
# send signal to switch on/off alarm ligth on a given uuid
async def writeAlarmSignal(uuid, switch):
    device = await BleakScanner.find_device_by_name(device_name)
    if device is None:
        return 1
    else:
        async with BleakClient(device) as client:
            try:
                await client.write_gatt_char(uuid, b'1', response=True)
                file1 = open("logFile.txt", "a")
                file1.write("WARNING: Send {0} alarm signal at {1} on uuid: {2}\n\n".format(switch, datetime.now().strftime("%D__%H:%M:%S"), uuid))
                file1.close()
            except Exception as e:
                logException(e, uuid)
def checkBoarderValues():
    # TODO
    # select count of sensorstations
    connected_sensor_stations = 0
    while connected_sensor_stations > 0:
        # TODO
        # foreach Sensorstation
        # select alarm_switch from sensorstation where name = "G4T2"
        alarm_switch = "off"
        if alarm_switch == "off":
            # TODO
            # foreach sensor of table Sensor
            # select the tuple of an sensor
            upper_value = 0
            lower_value = 0
            current_value = 0
            alarm_count = 0
            uuid = 0
            if current_value < lower_value or current_value > upper_value:
                if alarm_count > 5:
                    asyncio.run(writeAlarmSignal(uuid, "ON"))
                    alarm_count = 0
                else:
                    alarm_count += 1
            # update alarm_count if it has been changed
        elif alarm_switch == "fixed":
            # TODO
            # select any sensor uuid of the current sensorstation
            uuid = 0
            asyncio.run(writeAlarmSignal(uuid, "OFF"))
        else:
            # TODO
            # check database of webapp via REST if alarm is fixed
            print("check again")
    # end of while
    file1 = open("logFile.txt", "a")
    file1.write("INFO: Boarder values have been checked at {0}\n".format(datetime.now().strftime("%D__%H:%M:%S")))
    file1.close()
async def readSensorData(new_connection, device_list):
    # scanning for Sensorstation with name "G4T2"
    sensor_index = 0
    for deviceName in device_list:
        device = await BleakScanner.find_device_by_name(deviceName) # could also have timeout
        if device is None:
            logConnectionException(deviceName)
        else:
            # the naming convention is not intuitive imho
            async with BleakClient(device) as client:
                print("Connected to device {0}".format(deviceName))
                # print all services and all characteristics provided by device

                for service in client.services: # iterate all defined services on peripheral
                    print("Serivce: {0}".format(service))
                    if service.uuid != "00001801-0000-1000-8000-00805f9b34fb":
                        file1 = open("logFile.txt", "a")
                        file1.write("INFO: Connected to device {0} at {1}\nSerivce uuid:\t{2}\nDescription:\t{3}\n"
                                    .format(deviceName, datetime.now().strftime("%D__%H:%M:%S"), service.uuid, service.description))
                        file1.close()
                        if new_connection:
                            insertNewSensorStationToDatabase(service.description, deviceName)
                    for characteristic in service.characteristics: # print the characteristics of the service
                        float_value = False
                        print("Characteristic: {0} \n\twith properties: {1}".format(characteristic, ", ".join(characteristic.properties)))
                            # if characteristic.uuid != "00002a05-0000-1000-8000-00805f9b34fb":

                        for descriptor in characteristic.descriptors:
                            try:
                                type = await client.read_gatt_descriptor(descriptor.handle)
                                if type == b'TempSensor' or type == b'humiditySensor' or type == b'PressureSensor' or type == b'GasSensor':
                                    float_value = True
                            except Exception as e:
                                logException(e, descriptor.uuid)
                            try:
                                value = await client.read_gatt_char(characteristic.uuid)
                                if new_connection :
                                    insertNewSensorToDatabase(characteristic, deviceName,type, sensor_index)
                                    sensor_index +=1
                                else:
                                    writeValue(value, float_value, type, characteristic)
                            except Exception as e:
                                logException(e, characteristic.uuid)
                file1 = open("logFile.txt", "a")
                file1.write("INFO: Disconnected at {0}\n\n".format(datetime.now().strftime("%D__%H:%M:%S")))
                file1.close()
def logException(e, uuid):
    file1 = open("logFile.txt", "a")
    file1.write("ERROR: On characteristic {0}. Error is {1} at {2}\n".format(uuid, e, datetime.now().strftime("%D__%H:%M:%S")))
    file1.close()
def logConnectionException(name):
    file1 = open("logFile.txt", "a")
    file1.write("ERROR: Could not find device with name {0} at {1}\n".format(name, datetime.now().strftime("%D__%H:%M:%S")))
    file1.close()
def implement_database():
    # TODO check database connection

    conn = sqlite3.connect('AccessPoint')
    c = conn.cursor()
    c.execute('''
            create table if not exists Sensorstation(
            name varchar(64) not null primary key,
            service_uuid varchar,
            alarm_switch varchar(15) )
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
            value_id int not null,
            value varchar(64) not null,
            time_stamp timestamp,
            sensor_id int,
            PRIMARY KEY (value_id),
            FOREIGN KEY (sensor_id) REFERENCES Sensor(sensor_id) ON DELETE CASCADE);
        ''')
    conn.commit()
def insertNewSensorStationToDatabase(attribute, name):
    # TODO get number of max index of sensor
    try:
        conn = sqlite3.connect('AccessPoint')
        c = conn.cursor()
        c.execute('''
                insert into Sensorstation values('{0}', '{1}', 'off')
            '''.format(name, attribute))
        conn.commit()
        print("saved")
    except Exception as e:
        logException(e, attribute)
def insertNewSensorToDatabase(attribute, name, type, sensor_index):
    try:
        conn = sqlite3.connect('AccessPoint')
        c = conn.cursor()
        c.execute('''
                insert into Sensor values( {0}, '{1}', '{2}', '{3}', {4}, {5}, {6})
            '''.format(sensor_index, attribute.uuid, name, type.decode(), 0, 0, 0))
        conn.commit()
        print("ok-----------------InsertSensor")
    except Exception as e:
        logException(e, attribute.uuid)
def readStationNamesDatabase():
    # TODO call in loob all sensorstation name
    try:
        conn = sqlite3.connect('AccessPoint')
        c = conn.cursor()
        c.execute('''
            select name from Sensorstation
        '''.format(device_name))
        return c
    except Exception as e:
        logException(e, "SensorStation")

def readSensorsDatabase(name):
    # TODO call in loob all sensorstation name
    try:
        conn = sqlite3.connect('AccessPoint')
        c = conn.cursor()
        c.execute('''
            select * from Sensor where station_name='{0}'
        '''.format(name))
        return c
    except Exception as e:
        logException(e, "Sensor")

if __name__ == '__main__':
    # TODO read config.yaml
    repeat = 0
    value_count = 0
    new_SensorStation = True
    while repeat < 100:
        match program_state:
            case 0:
                implement_database()
                print("Implement database")
                time.sleep(5)
                program_state = 1
            case 1:
                # TODO call web app for Sensorstations if a new is added return True e.g. new_SensorStation = readSensorstation() and set device_name to the new one
                print("Call for new Sensorstation")
                if new_SensorStation:
                    asyncio.run(readSensorData(new_SensorStation, [device_name]))
                new_SensorStation = False
                print(readStationNamesDatabase().fetchall())
                print(readSensorsDatabase(device_name).fetchall())
                time.sleep(10)
                program_state = 2
            case 2:
                device_name = readStationNamesDatabase().fetchone()[0] # extract from [('G4T2',)]
                print(" its the name {0}".format(device_name))
                asyncio.run(readSensorData(new_SensorStation, [device_name]))
                print("Read Sensor data")
                value_count += 1
                # TODO if value_count > 10 call sendValuesTo Rest
                time.sleep(5)
                program_state = 3
            case 3:
                checkBoarderValues()
                print("check Boarders")
                time.sleep(5)
                program_state = 1
        repeat += 1

#  cronjop to restart