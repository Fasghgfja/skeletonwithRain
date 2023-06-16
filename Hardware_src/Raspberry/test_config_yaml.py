import unittest
from unittest.mock import patch, mock_open
import config_yaml

class TestConfigYaml(unittest.TestCase):
    @patch('config_yaml.yaml.load')
    @patch('config_yaml.open', new_callable=mock_open, read_data="data")
    @patch('config_yaml.exception_logging.logException')
    def test_read_webapp_ip(self, mock_log, mock_file, mock_yaml):
        mock_yaml.return_value = {"webapp-params": {"ip": "127.0.0.1"}}
        self.assertEqual(config_yaml.read_wepapp_ip(), "127.0.0.1")
        mock_log.assert_not_called()

    @patch('config_yaml.yaml.load')
    @patch('config_yaml.open', new_callable=mock_open, read_data="data")
    @patch('config_yaml.exception_logging.logException')
    def test_read_accesspoint_id(self, mock_log, mock_file, mock_yaml):
        mock_yaml.return_value = {"accesspoint-params": {"id": "1234"}}
        self.assertEqual(config_yaml.read_accesspoint_id(), "1234")
        mock_log.assert_not_called()

    # similar tests for read_sending_intervals and read_auth_params

    @patch('config_yaml.yaml.load')
    @patch('config_yaml.open', new_callable=mock_open, read_data="data")
    @patch('config_yaml.exception_logging.logException')
    def test_write_sending_intervals(self, mock_log, mock_file, mock_yaml):
        mock_yaml.return_value = {"accesspoint-params": {}}
        config_yaml.write_sending_intervalls("10", "20", "30")
        mock_file.assert_called_with("config.yaml", "w")
        mock_log.assert_not_called()

    # similar tests for read_trashhold_params, read_validation_params and write_validation

    # You can add tests for error conditions:
    @patch('config_yaml.yaml.load')
    @patch('config_yaml.open', new_callable=mock_open, read_data="data")
    @patch('config_yaml.exception_logging.logException')
    def test_read_webapp_ip_error(self, mock_log, mock_file, mock_yaml):
        mock_yaml.side_effect = Exception('Error loading yaml')
        self.assertIsNone(config_yaml.read_wepapp_ip())
        mock_log.assert_called_once()
        call_args = mock_log.call_args[0]  # Get the positional arguments that were called
        self.assertIsInstance(call_args[0], Exception)
        self.assertEqual(str(call_args[0]), 'Error loading yaml')
        self.assertEqual(call_args[1], 'reading config.yaml')


# similar error condition tests for other functions

if __name__ == '__main__':
    unittest.main()
