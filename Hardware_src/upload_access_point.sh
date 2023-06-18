#!/bin/bash

scp -r Raspberry/ pi@$1:~;
ssh pi@$1