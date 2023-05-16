import yaml
import exception_logging

def read_wepapp_ip():

    try:
        with open("../config.yaml") as f:
            cfg = yaml.load(f, Loader=yaml.FullLoader)

        return cfg["webapp-params"]["ip"]
    except Exception as e:
        exception_logging.logException(e, "reading config.yaml")

def read_accesspoint_id():

    try:
        with open("../config.yaml") as f:
            cfg = yaml.load(f, Loader=yaml.FullLoader)

        return cfg["accesspoint-params"]["id"]
    except Exception as e:
        exception_logging.logException(e, "reading config.yaml")

def read_sending_intervalls():

    try:
        with open("../config.yaml") as f:
            cfg = yaml.load(f, Loader=yaml.FullLoader)
        intervall_list = [cfg["accesspoint-params"]["measurement-intervall"], cfg["accesspoint-params"]["webapp-intervall"]]
        return intervall_list
    except Exception as e:
        exception_logging.logException(e, "reading config.yaml")

def write_sending_intervalls(measurment_intervall, webapp_intervall):

    try:
        with open("../config.yaml","r") as r_f:
            cfg = yaml.load(r_f, Loader=yaml.FullLoader)
        cfg["accesspoint-params"]["measurement-intervall"] = measurment_intervall
        cfg["accesspoint-params"]["webapp-intervall"] = webapp_intervall
        with open("../config.yaml","w") as w_f:
            yaml.dump(cfg, w_f)
    except Exception as e:
        exception_logging.logException(e, "reading config.yaml")

def read_auth_params():

    try:
        with open("../config.yaml") as f:
            cfg = yaml.load(f, Loader=yaml.FullLoader)
        auth_list = [cfg["webapp-params"]["pswd"], cfg["webapp-params"]["usnm"]]
        return auth_list
    except Exception as e:
        exception_logging.logException(e, "reading config.yaml")

#webapp-params:
#ip: localhost:8080
#usnm: admin
#pswd: passwd

#accesspoint-params:
#measurement-intervall: 10
#webapp-intervall: 10
#id: 50100