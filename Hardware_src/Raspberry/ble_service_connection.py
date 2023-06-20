import DB_connection, exception_logging
from bleak import BleakClient, BleakScanner
class device_tuple(object):
    def __init__(self, name: str, mac: str, uuid: str):
        self.name = name
        self.mac = mac
        self.uuid = uuid
async def search():
    try:
        devices = await BleakScanner.discover(timeout=10.0, return_adv=True)
        device_list = []
        for k, v in devices.items():
            if v[1].local_name == "PlantSensor" and not_present(k):
                d_tuple = device_tuple(name=v[1].local_name, mac=k, uuid=v[1].service_uuids[0])
                print("                      {0}, {1}".format(d_tuple.mac, d_tuple.name))
                device_list.append(vars(d_tuple))
        print("                      searching for stations -> ok")
        return device_list
    except Exception as e:
        exception_logging.logException(e, "read devices")
        print("                      searching for stations -> fail")
        return ["error"]
# send signal to switch on/off alarm ligth on a given uuid
async def write_alarm_signal(uuid, switch, station_name, sensor_type):
    if is_mac(station_name):
        device = await BleakScanner.find_device_by_address(station_name)
    else:
        device = await BleakScanner.find_device_by_name(station_name)
    if device is None:
        return 1
    else:
        async with BleakClient(device) as client:
            try:
                await client.write_gatt_char(uuid, b'1', response=True)
                exception_logging.log_information("WARNING: Send {0} alarm signal on station {1} to type: {2}".format(switch, station_name, sensor_type))
            except Exception as e:
                exception_logging.logException(e, uuid)
async def read_alarm_status(uuid, station_name):
    if is_mac(station_name):
        device = await BleakScanner.find_device_by_address(station_name)
    else:
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


async def read_sensor_data(new_connection, device_list):
    sensor_index = 0
    if new_connection:
        sensor_index = get_sensor_id()
    for station_name in device_list:
        if station_name is None:
            continue
        elif is_mac(station_name):
            device = await BleakScanner.find_device_by_address(station_name)
        else:
            device = await BleakScanner.find_device_by_name(station_name)
        if device is None:
            exception_logging.log_connection_exception(station_name)
        else:
            async with BleakClient(device) as client:
                print("                      Connected to device: {0}".format(station_name))
                for service in client.services:
                    if service.uuid != "00001801-0000-1000-8000-00805f9b34fb":
                        exception_logging.log_information("Connected to device {0}, Serivce uuid:\t{1}, Description:\t{2}"
                                                          .format(station_name, service.uuid, service.description))
                        if new_connection:
                            DB_connection.insert_new_sensor_station_to_database(service.description, station_name)
                    for characteristic in service.characteristics:
                        for descriptor in characteristic.descriptors:
                            try:
                                type = await client.read_gatt_descriptor(descriptor.handle)
                                float_value = is_float(type)
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
                exception_logging.log_success("Values from device {0}, Serivce uuid:\t{1}, Description:\t{2} have been read"
                                              .format(station_name, service.uuid, service.description))
        device = None
def not_present(name):
    current_names = DB_connection.read_station_interval_Database()
    for s in current_names:
        if s[0] == name:
            return False
    return True
def get_sensor_id():
    id_list = DB_connection.read_sensors()
    temp_id = 0
    for id in id_list:
        if temp_id == (id[0] - 1):
            temp_id = id[0]
        else:
            return temp_id + 1
    return temp_id + 1
def is_float(type):
    if type == b'SOIL_MOISTURE' or type == b'TEMPERATURE' or type == b'HUMIDITY' or type == b'AIR_PRESSURE' or type == b'AIR_QUALITY':
        return True
    else:
        return False
def is_mac(name):
    semicol = 1
    for c in name:
        if (semicol % 3) == 0 and c ==':':
            semicol += 1
            continue
        if str(c).isnumeric():
            semicol += 1
            continue
        elif c == 'A' or c == 'B' or c == 'C' or c == 'D' or c == 'E' or c == 'F':
            semicol += 1
            continue
        else:
            return False

    return True