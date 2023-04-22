import requests

auth = ("admin", "passwd")

api_url = "http://localhost:8080/api/measurements"

class SensorValue(object):
    def __init__(self, sensorStationName: str, uuid: str, value: str):
        self.sensorStationName = sensorStationName
        self.uuid = uuid
        self.value = value


def writeValueToWebApp():
    tempSensorValue = SensorValue(sensorStationName="123", uuid="TEMPERATURE", value="56")
    r = requests.post(api_url, json=vars(tempSensorValue), auth=auth)
    print(r.status_code)
