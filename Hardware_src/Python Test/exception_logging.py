from datetime import datetime

def logException(e, system):
    file1 = open("logFile.txt", "a")
    file1.write("ERROR: {0}. Error is {1} Date {2}\n".format(system, e, datetime.now().strftime("%D__%H:%M:%S")))
    file1.close()
def log_connection_exception(name):
    file1 = open("logFile.txt", "a")
    file1.write("ERROR: Could not connect to {0} Date {1}\n".format(name, datetime.now().strftime("%D__%H:%M:%S")))
    file1.close()

def log_information(text):
    file1 = open("logFile.txt", "a")
    file1.write("{0} Date {1}\n".format(text, datetime.now().strftime("%D__%H:%M:%S")))
    file1.close()

def log_success(text):
    file1 = open("logFile.txt", "a")
    file1.write("SUCCESS: {0} Date {1}\n".format(text, datetime.now().strftime("%D__%H:%M:%S")))
    file1.close()

def catch_to_long_error_msg(text):
    file1 = open("specialLogs.txt", "a")
    file1.write("{0}\n".format(text))
    file1.close()