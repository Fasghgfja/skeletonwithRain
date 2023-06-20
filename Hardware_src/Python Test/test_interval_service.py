import unittest
from unittest.mock import patch
from datetime import datetime, timedelta
import interval_service

class TestIntervalService(unittest.TestCase):

    @patch('DB_connection.read_station_interval_Database')
    @patch('exception_logging.logException')
    def test_get_measurement_interval(self, mock_logException, mock_readDB):
        mock_readDB.return_value = [("Station1", 10), ("Station2", 20)]
        result = interval_service.get_measurement_interval()
        self.assertEqual(result[0].name, "Station1")
        self.assertEqual(result[0].interval, timedelta(minutes=10))
        self.assertEqual(result[1].name, "Station2")
        self.assertEqual(result[1].interval, timedelta(minutes=20))

    @patch('DB_connection.read_station_interval_Database')
    @patch('exception_logging.logException')
    def test_get_webapp_interval(self, mock_logException, mock_readDB):
        mock_readDB.return_value = [("Station1", 10, 15), ("Station2", 20, 30)]
        result = interval_service.get_webapp_interval()
        self.assertEqual(result[0].name, "Station1")
        self.assertEqual(result[0].interval, timedelta(minutes=15))
        self.assertEqual(result[1].name, "Station2")
        self.assertEqual(result[1].interval, timedelta(minutes=30))

    @patch('DB_connection.read_station_interval_Database')
    @patch('exception_logging.logException')
    def test_get_start_time(self, mock_logException, mock_readDB):
        mock_readDB.return_value = [("Station1", 10, 15), ("Station2", 20, 30)]
        result = interval_service.get_start_time("Station2")
        self.assertEqual(result.name, "Station2")
        self.assertIsInstance(result.start_time, datetime)

    @patch('exception_logging.logException')
    def test_station_interval_passed(self, mock_logException):
        start_list = [interval_service.start_time_tuple("Station1", datetime.now() - timedelta(minutes=5))]
        delta_list = [interval_service.interval_tuple("Station1", timedelta(minutes=4))]
        result = interval_service.station_interval_passed(start_list, delta_list)
        self.assertIn("Station1", result)

    @patch('DB_connection.read_station_interval_Database')
    @patch('exception_logging.logException')
    def test_get_all_start_times(self, mock_logException, mock_readDB):
        mock_readDB.return_value = [("Station1", 10, 15), ("Station2", 20, 30)]
        result = interval_service.get_all_start_times()
        self.assertEqual(len(result), 2)
        self.assertIsInstance(result[0], interval_service.start_time_tuple)
        self.assertIsInstance(result[1], interval_service.start_time_tuple)

if __name__ == '__main__':
    unittest.main()
