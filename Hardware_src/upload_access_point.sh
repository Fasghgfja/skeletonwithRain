#!/bin/bash

scp -r Raspberry/ $2@$1:~;
ssh $2@$1