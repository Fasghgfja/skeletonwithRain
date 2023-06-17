import unittest
from unittest.mock import patch, MagicMock
import check_boarder_values

class tests(unittest.TestCase):
    @patch('check_boarder_values.DB_connection.read_Sensor_Stationnames_Database')
    @patch('check_boarder_values.DB_connection.read_sensors_database')
    @patch('check_boarder_values.DB_connection.read_value_from_database')
    @patch('check_boarder_values.DB_connection.update_sensor_station_database')
    @patch('check_boarder_values.DB_connection.update_sensor_database')
    @patch('check_boarder_values.ble_service_connection.write_alarm_signal')
    @patch('check_boarder_values.rest_api.write_alarm_switch')
    @patch('check_boarder_values.rest_api.update_Sensor')
    @patch('check_boarder_values.exception_logging.log_information')
    def test_check_boarder_values(self, mock_log_info, mock_update_sensor, mock_write_alarm, mock_write_alarm_signal, mock_update_sensor_db, mock_update_station_db, mock_read_value_db, mock_read_sensors_db, mock_read_station_db):
        mock_read_station_db.return_value = MagicMock()
        mock_read_sensors_db.return_value = MagicMock()
        mock_read_value_db.return_value = MagicMock()
        station_list = ["Station1", "Station2"]
        check_boarder_values.check_boarder_values(station_list)

    @patch('check_boarder_values.DB_connection.read_Sensor_Stationnames_Database')
    @patch('check_boarder_values.DB_connection.read_sensors_alarm_count')
    def test_check_sensor_station_alarm(self, mock_read_alarm_count, mock_read_station_db):
        mock_read_station_db.return_value = MagicMock()
        mock_read_alarm_count.return_value = MagicMock()
        check_boarder_values.check_sensor_station_alarm()

    @patch('check_boarder_values.DB_connection.read_sensors_alarm_characteristic')
    @patch('check_boarder_values.ble_service_connection.read_alarm_status')
    @patch('check_boarder_values.ble_service_connection.write_alarm_signal')
    @patch('check_boarder_values.DB_connection.update_sensor_station_database')
    @patch('check_boarder_values.DB_connection.update_sensor_database')
    @patch('check_boarder_values.rest_api.write_alarm_switch')
    @patch('check_boarder_values.rest_api.update_Sensor')
    def test_update_alarm_switch(self, mock_update_sensor, mock_write_alarm, mock_update_sensor_db, mock_update_station_db, mock_write_alarm_signal, mock_read_alarm_status, mock_read_alarm_characteristic):
        mock_read_alarm_characteristic.return_value = MagicMock()
        mock_read_alarm_status.return_value = MagicMock()
        station_name = "Station1"
        uuid = "123"
        description = "Station Description"
        sensor_id = 1
        sensor_type = "Temperature"
        check_boarder_values.update_alarm_switch(station_name, uuid, description, sensor_id, sensor_type)

    def test_num_check(self):
        self.assertEqual(check_boarder_values.num_check(123), 123)
        self.assertEqual(check_boarder_values.num_check("123"), 123)
        self.assertRaises(ValueError, check_boarder_values.num_check, "abc")


if __name__ == '__main__':
    unittest.main()
