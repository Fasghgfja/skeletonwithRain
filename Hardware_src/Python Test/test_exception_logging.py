import unittest
import os
import exception_logging
class TestLoggingFunctions(unittest.TestCase):

    def setUp(self):
        self.log_file = "logFile.txt"
        self.special_log_file = "specialLogs.txt"

    def tearDown(self):
        if os.path.exists(self.log_file):
            os.remove(self.log_file)
        if os.path.exists(self.special_log_file):
            os.remove(self.special_log_file)

    def test_logException(self):
        e = Exception('Test exception')
        system = 'TestSystem'
        exception_logging.logException(e, system)
        with open(self.log_file, 'r') as file:
            content = file.read()
            self.assertIn('ERROR: TestSystem. Error is Test exception', content)

    def test_log_connection_exception(self):
        name = 'TestServer'
        exception_logging.log_connection_exception(name)
        with open(self.log_file, 'r') as file:
            content = file.read()
            self.assertIn('ERROR: Could not connect to TestServer', content)

    def test_log_information(self):
        text = 'Test information'
        exception_logging.log_information(text)
        with open(self.log_file, 'r') as file:
            content = file.read()
            self.assertIn('Test information', content)

    def test_log_success(self):
        text = 'Test success'
        exception_logging.log_success(text)
        with open(self.log_file, 'r') as file:
            content = file.read()
            self.assertIn('SUCCESS: Test success', content)

    def test_catch_to_long_error_msg(self):
        text = 'Test long error message'
        exception_logging.catch_to_long_error_msg(text)
        with open(self.special_log_file, 'r') as file:
            content = file.read()
            self.assertIn('Test long error message', content)

if __name__ == '__main__':
    unittest.main()


class TestLoggingFunctions(unittest.TestCase):

    def setUp(self):
        self.log_file = "logFile.txt"
        self.special_log_file = "specialLogs.txt"

    def tearDown(self):
        if os.path.exists(self.log_file):
            os.remove(self.log_file)
        if os.path.exists(self.special_log_file):
            os.remove(self.special_log_file)

    def test_logException(self):
        e = Exception('Test exception')
        system = 'TestSystem'
        exception_logging.logException(e, system)
        with open(self.log_file, 'r') as file:
            content = file.read()
            self.assertIn('ERROR: TestSystem. Error is Test exception', content)

    def test_log_connection_exception(self):
        name = 'TestServer'
        exception_logging.log_connection_exception(name)
        with open(self.log_file, 'r') as file:
            content = file.read()
            self.assertIn('ERROR: Could not connect to TestServer', content)

    def test_log_information(self):
        text = 'Test information'
        exception_logging.log_information(text)
        with open(self.log_file, 'r') as file:
            content = file.read()
            self.assertIn('Test information', content)

    def test_log_success(self):
        text = 'Test success'
        exception_logging.log_success(text)
        with open(self.log_file, 'r') as file:
            content = file.read()
            self.assertIn('SUCCESS: Test success', content)

    def test_catch_to_long_error_msg(self):
        text = 'Test long error message'
        exception_logging.catch_to_long_error_msg(text)
        with open(self.special_log_file, 'r') as file:
            content = file.read()
            self.assertIn('Test long error message', content)

if __name__ == '__main__':
    unittest.main()
