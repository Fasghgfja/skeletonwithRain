import os
import config_yaml

if config_yaml.read_restart_params():
    os.system("bash start_access_point.sh")
else:
    print("NO restart")
    config_yaml.write_restart(True)