# https://github.com/hbldh/bleak
# https://github.com/hbldh/bleak/blob/master/examples/service_explorer.py

import asyncio
from bleak import BleakClient, BleakScanner

#device_name = "SensorStation GxTy"
device_name = "G4T2"
# device_name = "A52 von stefan"

async def main():
    # scanning for Sensorstation with name "G4T2"
    device = await BleakScanner.find_device_by_name(device_name) # could also have timeout
    if device is None:
        print("ERROR: Could not find device with name {0}".format(device_name))
        return
    
    # the naming convention is not intuitive imho
    async with BleakClient(device) as client:
        print("Connected to device {0}".format(device_name))

        # print all services and all characteristics provided by device
        for service in client.services: # iterate all defined services on peripheral
            print("Serivce: {0}".format(service))

            for characteristic in service.characteristics: # print the characteristics of the service
                # Output example
                # Serivce: 0000180a-0000-1000-8000-00805f9b34fb (Handle: 14): Device Information
                # Characteristic: 000019b1-0000-1000-8000-00805f9b34fb (Handle: 15): Vendor specific
                # with properties: read
                # Value is: 234
                # Descriptor 00002901-0000-1000-8000-00805f9b34fb (Handle: 17): Characteristic User Description says: bytearray(b'HygroSensor')
                print("Characteristic: {0} \n\twith properties: {1}".format(characteristic, ", ".join(characteristic.properties)))
                try:
                    value = await client.read_gatt_char(characteristic.uuid)
                    print("Value is: {0}".format(int.from_bytes(value, "little")))

                except Exception as e:
                    print("ERROR: reading characteristic {0}. Error is {1}".format(characteristic, e))

                for descriptor in characteristic.descriptors:
                    try:
                        value = await client.read_gatt_descriptor(descriptor.handle)
                        print("Descriptor {0} says: {1}".format(descriptor, value))
                    except Exception as e:
                        print("ERROR: reading descriptor {0}. Error is {1}".format(descriptor, e))
                print()
            print("================\n")

        print("INFO: Disconnecting from device {0} ...".format(device_name))
    print("INFO: Disconnected from device {0}".format(device_name))


asyncio.run(main())
