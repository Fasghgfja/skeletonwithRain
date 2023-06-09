import asyncio
from bleak import BleakScanner
from bleak.backends.scanner import AdvertisementData

async def search():
    devices = await BleakScanner.discover(timeout=5.0, return_adv=True)
    # print(devices)
    name_list =[]
    for k, v in devices.items():
        name_list.append(v[1].local_name)
        # print("Device id: {0}\nLocal name: {1}\n\n".format(k, v[1].local_name))
    return name_list
