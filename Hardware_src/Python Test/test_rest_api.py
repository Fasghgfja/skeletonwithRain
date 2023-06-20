import unittest
from unittest.mock import patch, Mock
import rest_api


class TestRestApi(unittest.TestCase):

    @patch('config_yaml.read_wepapp_ip')
    @patch('config_yaml.read_accesspoint_id')
    def test_url_builder(self, mock_read_accesspoint_id, mock_read_wepapp_ip):
        mock_read_wepapp_ip.return_value = "192.168.1.1"
        mock_read_accesspoint_id.return_value = "accesspoint1"
        url = rest_api.url_builder("endpoint")
        self.assertEqual(url, "http://192.168.1.1/api/endpoint")

    @patch('requests.get')
    @patch('rest_api.url_builder')
    @patch('rest_api.get_auth')
    def test_check_validation(self, mock_get_auth, mock_url_builder, mock_get):
        mock_get_auth.return_value = ('user', 'pass')
        mock_url_builder.return_value = 'http://192.168.1.1/api/validated'
        mock_get.return_value.status_code = 200
        mock_get.return_value.json.return_value = True
        result = rest_api.check_validation()
        self.assertIsNone(result)

    @patch('DB_connection.read_Sensor_Stationnames_Database')
    def test_get_current_station_data(self, mock_read_Sensor_Stationnames_Database):
        mock_read_Sensor_Stationnames_Database.return_value = [('station1',), ('station2',)]
        station_list = ['station1', 'station3']
        result = rest_api.get_current_station_data(station_list)
        self.assertEqual(result, [('station1',)])

    def test_handle_special_values(self):
        self.assertEqual(rest_api.handle_special_values(float('inf')), 'inf')
        self.assertEqual(rest_api.handle_special_values(float('nan')), 'nan')
        self.assertEqual(rest_api.handle_special_values(123), None)


if __name__ == '__main__':
    unittest.main()
