# https://github.com/hbldh/bleak
# https://github.com/hbldh/bleak/blob/master/examples/service_explorer.py
import DB_connection
import exception_logging

import sqlite3
import array as devices
from bleak import BleakClient, BleakScanner
from datetime import datetime




# writeValue is used to write the values to the database

        # added
# send signal to switch on/off alarm ligth on a given uuid
async def writeAlarmSignal(uuid, switch, station_name):
    device = await BleakScanner.find_device_by_name(station_name)
    if device is None:
        return 1
    else:
        async with BleakClient(device) as client:
            try:
                await client.write_gatt_char(uuid, b'1', response=True)
                exception_logging.log_information("WARNING: Send {0} alarm signal on uuid: {1}".format(switch, uuid))
            except Exception as e:
                exception_logging.logException(e, uuid)
async def read_alarm_status(uuid, station_name):
    device = await BleakScanner.find_device_by_name(station_name)
    if device is None:
        return 1
    else:
        async with BleakClient(device) as client:
            try:
                alarm_switch = await client.read_gatt_char(uuid)
                exception_logging.log_information("WARNING: Call alarm_switch from Station {0}".format(station_name))
                return alarm_switch
            except Exception as e:
                exception_logging.logException(e, uuid)

def get_sensor_id():
    id_list = DB_connection.read_sensors()
    temp_id = 0
    for id in id_list:
        if temp_id == (id - 1):
            temp_id = id
        else:
            return temp_id + 1
    return temp_id + 1
async def read_sensor_data(new_connection, device_list):
    # scanning for Sensorstation with name "G4T2"
    sensor_index = 0
    if new_connection:
        sensor_index = get_sensor_id()
    for station_name in device_list:
        device = await BleakScanner.find_device_by_name(station_name) # could also have timeout
        if device is None:
            exception_logging.log_connection_exception(station_name)
        else:
            # the naming convention is not intuitive imho
            async with BleakClient(device) as client:
                print("Connected to device {0}".format(station_name))
                # print all services and all characteristics provided by device

                for service in client.services: # iterate all defined services on peripheral
                    if service.uuid != "00001801-0000-1000-8000-00805f9b34fb":
                        exception_logging.log_information("INFO: Connected to device {0}, Serivce uuid:\t{1}, Description:\t{2}"
                                                          .format(station_name, service.uuid, service.description))
                        if new_connection:
                            DB_connection.insert_new_sensor_station_to_database(service.description, station_name)
                    for characteristic in service.characteristics: # print the characteristics of the service
                        float_value = False
                        for descriptor in characteristic.descriptors:
                            try:
                                type = await client.read_gatt_descriptor(descriptor.handle)
                                if type == b'TEMPERATURE' or type == b'HUMIDITY' or type == b'AIR_PRESSURE' or type == b'AIR_QUALITY':
                                    float_value = True
                            except Exception as e:
                                exception_logging.logException(e, descriptor.uuid)
                            try:
                                value = await client.read_gatt_char(characteristic.uuid)
                                if new_connection :

                                    DB_connection.insert_new_sensor_to_database(characteristic, station_name, type, sensor_index)
                                    sensor_index += 1
                                elif type != b'ALARM_STATUS':
                                    DB_connection.insert_values_into_database(value, float_value, type, station_name)
                            except Exception as e:
                                exception_logging.logException(e, characteristic.uuid)
                exception_logging.log_information("INFO: Disconnected")