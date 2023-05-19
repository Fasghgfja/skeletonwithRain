from datetime import datetime

def logException(e, uuid):
    file1 = open("logFile.txt", "a")
    file1.write("ERROR: On characteristic {0}. Error is {1} Date {2}\n".format(uuid, e, datetime.now().strftime("%D__%H:%M:%S")))
    file1.close()
def log_connection_exception(name):
    file1 = open("logFile.txt", "a")
    file1.write("ERROR: Could not find device with name {0} Date {1}\n".format(name, datetime.now().strftime("%D__%H:%M:%S")))
    file1.close()

def log_information(text):
    file1 = open("logFile.txt", "a")
    file1.write("{0} Date {1}\n".format(text, datetime.now().strftime("%D__%H:%M:%S")))
    file1.close()