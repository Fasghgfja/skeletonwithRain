# https://github.com/hbldh/bleak
# https://github.com/hbldh/bleak/blob/master/examples/service_explorer.py
import struct
import asyncio
import time

from bleak import BleakClient, BleakScanner
from datetime import datetime
#device_name = "SensorStation GxTy"
device_name = "G4T2"
hygro_alarm_characteristic_uuid = "000019b1-0000-1000-8000-00805f9b34fb"
ligth_alarm_characteristic_uuid = "000019b0-0000-1000-8000-00805f9b34fb"
temp_alarm_characteristic_uuid = "000019b2-0000-1000-8000-00805f9b34fb"
hum_alarm_characteristic_uuid = "000019b3-0000-1000-8000-00805f9b34fb"
press_alarm_characteristic_uuid = "000019b4-0000-1000-8000-00805f9b34fb"
gas_alarm_characteristic_uuid = "000019b5-0000-1000-8000-00805f9b34fb"
# device_name = "A52 von stefan"

# writeValue is used to write the values to the database with sensortype and uuid as identifier
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
async def writeAlarmSignal(uuid):
    device = await BleakScanner.find_device_by_name(device_name)
    if device is None:
        return 1
    else:
        async with BleakClient(device) as client:
            try:
                await client.write_gatt_char(uuid, b'1', response=True)
                file1 = open("logFile.txt", "a")
                file1.write("WARNING: Activated alarm at {0} on uuid: {1}\n\n".format(datetime.now().strftime("%D__%H:%M:%S"), uuid))
                file1.close()
            except Exception as e:
                logException(e, uuid)
def checkBoarderValues():
    # foreach sensor of table Sensor
    # select upperBoared, lowerBoarder, current, alarmCount from sensor
    upper_value = 0
    lower_value = 0
    current_value = 0
    alarm_count = 0
    if current_value < lower_value or current_value > upper_value:
        if alarm_count > 5:
            asyncio.run(writeAlarmSignal(hygro_alarm_characteristic_uuid))
        else:
            alarm_count += 1
    # update alarm_count if changed of table sensor
async def readSensorData():
    # scanning for Sensorstation with name "G4T2"
    device = await BleakScanner.find_device_by_name(device_name) # could also have timeout
    if device is None:
        return 1
    else:
        # the naming convention is not intuitive imho
        async with BleakClient(device) as client:
            print("Connected to device {0}".format(device_name))
            # print all services and all characteristics provided by device

            for service in client.services: # iterate all defined services on peripheral
                print("Serivce: {0}".format(service))
                if service.uuid != "00001801-0000-1000-8000-00805f9b34fb":
                    file1 = open("logFile.txt", "a")
                    file1.write("INFO: Connected to device {0} at {1}\nSerivce uuid:\t{2}\nDescription:\t{3}\n"
                                .format(device_name, datetime.now().strftime("%D__%H:%M:%S"), service.uuid, service.description))
                    file1.close()
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
                            writeValue(value, float_value, type, characteristic)
                        except Exception as e:
                            logException(e, characteristic.uuid)
            file1 = open("logFile.txt", "a")
            file1.write("INFO: Disconnected at {0}\n\n".format(datetime.now().strftime("%D__%H:%M:%S")))
            file1.close()
def logException(e, uuid):
    file1 = open("logFile.txt", "a")
    file1.write("ERROR: write alarm signal to characteristic {0}. Error is {1} at {2}\n".format(uuid, e, datetime.now().strftime("%D__%H:%M:%S")))
    file1.close()
def logConnectionException():
    file1 = open("logFile.txt", "a")
    file1.write("ERROR: Could not find device with name {0} at {1}\n".format(device_name, datetime.now().strftime("%D__%H:%M:%S")))
    file1.close()
repeat = 0
while repeat < 100:
    if asyncio.run(readSensorData()) == 1:
        logConnectionException()
    time.sleep(60)

    repeat += 1
    print(repeat)

