import unittest
from unittest.mock import MagicMock, patch, AsyncMock
from asyncio import Future

import DB_connection
import exception_logging
import ble_service_connection
import asyncio

from bleak import BleakClient, BleakScanner


class tests(unittest.TestCase):


    @patch.object(BleakScanner, 'find_device_by_address')
    @patch.object(BleakScanner, 'find_device_by_name')
    async def test_write_alarm_signal(self, mock_find_by_name, mock_find_by_address):
        mock_find_by_name.return_value = None
        mock_find_by_address.return_value = None
        result = await ble_service_connection.write_alarm_signal('uuid1', 'switch1', 'device1')
        self.assertEqual(result, 1)

        device_mock = MagicMock()
        client_mock = MagicMock()
        client_mock.write_gatt_char = AsyncMock()
        device_mock.__aenter__.return_value = client_mock
        mock_find_by_name.return_value = device_mock

        result = await ble_service_connection.write_alarm_signal('uuid1', 'switch1', 'device1')
        self.assertIsNone(result)
        client_mock.write_gatt_char.assert_called_once_with('uuid1', b'1', response=True)

    @patch.object(BleakScanner, 'find_device_by_address')
    @patch.object(BleakScanner, 'find_device_by_name')
    async def test_read_alarm_status(self, mock_find_by_name, mock_find_by_address):
        mock_find_by_name.return_value = None
        mock_find_by_address.return_value = None
        result = await ble_service_connection.read_alarm_status('uuid1', 'device1')
        self.assertEqual(result, 1)

        device_mock = MagicMock()
        client_mock = MagicMock()
        client_mock.read_gatt_char = AsyncMock()
        device_mock.__aenter__.return_value = client_mock
        mock_find_by_name.return_value = device_mock

        result = await ble_service_connection.read_alarm_status('uuid1', 'device1')
        self.assertIsNone(result)
        client_mock.read_gatt_char.assert_called_once_with('uuid1')

    @patch.object(BleakScanner, 'find_device_by_address')
    @patch.object(BleakScanner, 'find_device_by_name')
    async def test_read_sensor_data(self, mock_find_by_name, mock_find_by_address):
        mock_find_by_name.return_value = None
        mock_find_by_address.return_value = None
        result = asyncio.run(ble_service_connection.read_sensor_data(True, ['device1', 'device2']))
        mock_find_by_name.assert_called_with('device1')
        mock_find_by_address.assert_called_with('device1')
        self.assertIsNone(result)

        device_mock = MagicMock()
        client_mock = MagicMock()
        device_mock.__aenter__.return_value = client_mock
        client_mock.services = [
            MagicMock(uuid='service_uuid1', description='service_desc1'),
            MagicMock(uuid='service_uuid2', description='service_desc2'),
            MagicMock(uuid='00001801-0000-1000-8000-00805f9b34fb', description='other_service')
        ]
        client_mock.services[0].characteristics = [
            MagicMock(uuid='characteristic_uuid1'),
            MagicMock(uuid='characteristic_uuid2')
        ]
        client_mock.services[1].characteristics = [
            MagicMock(uuid='characteristic_uuid3')
        ]
        client_mock.services[0].characteristics[0].descriptors = [
            MagicMock(handle=1)
        ]
        client_mock.services[0].characteristics[1].descriptors = [
            MagicMock(handle=2)
        ]
        client_mock.services[1].characteristics[0].descriptors = [
            MagicMock(handle=3)
        ]

        async def read_gatt_descriptor(handle):
            if handle == 1:
                return b'TYPE1'
            elif handle == 2:
                return b'TYPE2'

        async def read_gatt_char(uuid):
            if uuid == 'characteristic_uuid1':
                return b'VALUE1'
            elif uuid == 'characteristic_uuid2':
                return b'VALUE2'

        client_mock.read_gatt_descriptor = AsyncMock(side_effect=read_gatt_descriptor)
        client_mock.read_gatt_char = AsyncMock(side_effect=read_gatt_char)

        result = asyncio.run(ble_service_connection.read_sensor_data(True, ['device1', 'device2']))
        self.assertIsNone(result)
        DB_connection.insert_new_sensor_station_to_database.assert_called_with('service_desc1', 'device1')
        DB_connection.insert_new_sensor_to_database.assert_called_with(
            client_mock.services[0].characteristics[0],
            'device1',
            b'TYPE1',
            0
        )
        DB_connection.insert_values_into_database.assert_called_with(
            b'VALUE1',
            True,
            b'TYPE1',
            'device1'
        )



    def test_is_float(self):
        result = ble_service_connection.is_float(b'SOIL_MOISTURE')
        self.assertTrue(result)
        result = ble_service_connection.is_float(b'OTHER_TYPE')
        self.assertFalse(result)



if __name__ == '__main__':
    unittest.main()
