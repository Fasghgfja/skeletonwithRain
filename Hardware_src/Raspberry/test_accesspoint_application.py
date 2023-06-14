
import unittest
import DB_connection
from datetime import datetime, timedelta
from unittest.mock import MagicMock, patch

import asyncio


import interval_service
import exception_logging
import rest_api
import time
import ble_service_connection
import check_boarder_values
import program_status

class accesspoint_application_test(unittest.TestCase):
    def test_implement_database(self):
        with patch('DB_connection.implement_database') as mock_implement_database:
            DB_connection.implement_database()
            mock_implement_database.assert_called_once()

    def test_get_all_start_times(self):
        with patch('interval_service.get_all_start_times') as mock_get_all_start_times:
            mock_return_value = ['start_time1', 'start_time2']
            mock_get_all_start_times.return_value = mock_return_value
            result = interval_service.get_all_start_times()
            self.assertEqual(result, mock_return_value)

    def test_get_measurement_interval(self):
        with patch('interval_service.get_measurement_interval') as mock_get_measurement_interval:
            mock_return_value = ['interval1', 'interval2']
            mock_get_measurement_interval.return_value = mock_return_value
            result = interval_service.get_measurement_interval()
            self.assertEqual(result, mock_return_value)

    def test_get_webapp_interval(self):
        with patch('interval_service.get_webapp_interval') as mock_get_webapp_interval:
            mock_return_value = ['webapp_interval1', 'webapp_interval2']
            mock_get_webapp_interval.return_value = mock_return_value
            result = interval_service.get_webapp_interval()
            self.assertEqual(result, mock_return_value)

    def test_station_interval_passed(self):
        start_measurement_interval_time_list = ['start_time1', 'start_time2']
        delta_measurement_list = ['delta1', 'delta2']
        with patch('interval_service.get_start_time') as mock_get_start_time:
            mock_get_start_time.side_effect = ['start_time1', 'start_time2']
            result = interval_service.station_interval_passed(start_measurement_interval_time_list, delta_measurement_list)
            self.assertEqual(result, [])

    def test_check_if_new_stations(self):
        with patch('rest_api.check_if_new_stations') as mock_check_if_new_stations:
            mock_return_value = ['new_station1', 'new_station2']
            mock_check_if_new_stations.return_value = mock_return_value
            result = rest_api.check_if_new_stations()
            self.assertEqual(result, mock_return_value)

    # Continue writing tests for the other functions...

if __name__ == '__main__':
    unittest.main()