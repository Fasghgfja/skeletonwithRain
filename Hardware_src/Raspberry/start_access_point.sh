#!/bin/bash
sleep 10
cd $HOME/Raspberry;
python3 accesspoint_application.py
python3 application_restarter.py