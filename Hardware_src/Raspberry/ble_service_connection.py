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
create_file = False
def createFile():
    file1 = open("sensorfile.txt", "w")
    file1.writelines(datetime.now().strftime("%D__%H:%M:%S"))
    file1.close()
# writeValue is used to write the values to the database with sensortype and uuid as identifier
def writeValue(value, is_float, type, characteristic):
    file1 = open("sensorfile.txt", "a")
    file1.write("\n")
    file1.write("Sensor type:\t{0}\nuuid:\t\t\t{1}".format(type.decode(), characteristic.uuid))
    file1.write("\n")
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
        print("ERROR: Could not find device with name {0}".format(device_name))
        return
    else:
        async with BleakClient(device) as client:
            try:
                await client.write_gatt_char(uuid, b'1', response=True)
                file1 = open("sensorfile.txt", "a")
                file1.write("Activated alarm at {0} on uuid: {1}\n\n".format(datetime.now().strftime("%D__%H:%M:%S"), uuid))
                file1.close()
            except Exception as e:
                print("ERROR: write alarm signal to characteristic {0}. Error is {1}".format(uuid, e))

async def readSensorData():
    # scanning for Sensorstation with name "G4T2"
    device = await BleakScanner.find_device_by_name(device_name) # could also have timeout
    if device is None:
        print("ERROR: Could not find device with name {0}".format(device_name))
        return
    else:
        # the naming convention is not intuitive imho
        async with BleakClient(device) as client:
            print("Connected to device {0}".format(device_name))
            # print all services and all characteristics provided by device

            for service in client.services: # iterate all defined services on peripheral
                print("Serivce: {0}".format(service))
                if service.uuid != "00001801-0000-1000-8000-00805f9b34fb":
                    file1 = open("sensorfile.txt", "a")
                    file1.write("Connected to device {0} at {1}\nSerivce uuid:\t{2}\nDescription:\t{3}\n"
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
                            print("ERROR: reading descriptor {0}. Error is {1}".format(descriptor, e))
                        try:
                            value = await client.read_gatt_char(characteristic.uuid)
                            writeValue(value, float_value, type, characteristic)
                        except Exception as e:
                            print("ERROR: reading characteristic {0}. Error is {1}".format(characteristic, e))

            print("================\n")
            file1 = open("sensorfile.txt", "a")
            file1.write("\n")
            file1.write("Disconnected at {0}".format(datetime.now().strftime("%D__%H:%M:%S")))
            file1.write("\n\n")
            file1.close()

repeat = 0
while repeat < 100:
    asyncio.run(readSensorData())
    time.sleep(10)
    if repeat == 2 or repeat == 3:
        asyncio.run(writeAlarmSignal(hygro_alarm_characteristic_uuid))
    elif repeat == 4 or repeat == 5:
            asyncio.run(writeAlarmSignal(ligth_alarm_characteristic_uuid))
    elif repeat == 6 or repeat == 7:
        asyncio.run(writeAlarmSignal(temp_alarm_characteristic_uuid))
    elif repeat == 8 or repeat == 9:
        asyncio.run(writeAlarmSignal(hum_alarm_characteristic_uuid))
    elif repeat == 10 or repeat == 11:
        asyncio.run(writeAlarmSignal(press_alarm_characteristic_uuid))
    elif repeat == 12 or repeat == 13:
        asyncio.run(writeAlarmSignal(gas_alarm_characteristic_uuid))
    repeat += 1
    print(repeat)

