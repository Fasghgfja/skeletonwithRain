import unittest
from unittest import mock
from unittest.mock import patch, MagicMock
import DB_connection as db

class TestDBConnection(unittest.TestCase):

    @mock.patch('DB_connection.sqlite3.connect')
    def test_implement_database(self, mock_conn):
        db.implement_database()
        self.assertEqual(mock_conn.call_count, 1)
        self.assertEqual(mock_conn().cursor().execute.call_count, 3)


    @patch('sqlite3.connect')
    @patch('DB_connection.exception_logging.logException')
    def test_insert_values_into_database(self, mock_log, mock_connect):
        mock_conn = MagicMock()
        mock_connect.return_value = mock_conn
        db.insert_values_into_database(b'\x00\x00\x80\x3f', True, b'type', 'deviceName')
        mock_conn.cursor.assert_called()
        mock_conn.commit.assert_called()
        mock_log.assert_not_called()

    @patch('sqlite3.connect')
    @patch('DB_connection.exception_logging.logException')
    def test_insert_new_sensor_station_to_database(self, mock_log, mock_connect):
        mock_conn = MagicMock()
        mock_connect.return_value = mock_conn
        db.insert_new_sensor_station_to_database('attribute', 'name')
        mock_conn.cursor.assert_called()
        mock_conn.commit.assert_called()
        mock_log.assert_not_called()

    @patch('sqlite3.connect')
    @patch('DB_connection.exception_logging.logException')
    def test_insert_new_sensor_to_database(self, mock_log, mock_connect):
        mock_conn = MagicMock()
        mock_connect.return_value = mock_conn
        attribute = MagicMock()
        attribute.uuid = 'uuid'
        db.insert_new_sensor_to_database(attribute, 'name', b'type', 0)
        mock_conn.cursor.assert_called()
        mock_conn.commit.assert_called()
        mock_log.assert_not_called()

    @patch('sqlite3.connect')
    @patch('DB_connection.exception_logging.logException')
    def test_update_sensor_station_database(self, mock_log, mock_connect):
        mock_conn = MagicMock()
        mock_connect.return_value = mock_conn
        db.update_sensor_station_database('on', 'station_name')
        mock_conn.cursor.assert_called()
        mock_conn.commit.assert_called()
        mock_log.assert_not_called()

    @patch('sqlite3.connect')
    @patch('DB_connection.exception_logging.logException')
    def test_update_sensor_station_interval(self, mock_log, mock_connect):
        mock_conn = MagicMock()
        mock_connect.return_value = mock_conn
        db.update_sensor_station_interval('station_name', 0, 1, 2)
        mock_conn.cursor.assert_called()
        mock_conn.commit.assert_called()
        mock_log.assert_not_called()

    @patch('sqlite3.connect')
    @patch('DB_connection.exception_logging.logException')
    def test_update_sensor_database(self, mock_log, mock_connect):
        mock_conn = MagicMock()
        mock_connect.return_value = mock_conn
        db.update_sensor_database(0, 1)
        mock_conn.cursor.assert_called()
        mock_conn.commit.assert_called()
        mock_log.assert_not_called()

    @patch('sqlite3.connect')
    @patch('DB_connection.exception_logging.logException')
    def test_update_boarder_value(self, mock_log, mock_connect):
        mock_conn = MagicMock()
        mock_connect.return_value = mock_conn
        db.update_boarder_value(0, 1, 2)
        mock_conn.cursor.assert_called()
        mock_conn.commit.assert_called()
        mock_log.assert_not_called()


if __name__ == '__main__':
    unittest.main()
