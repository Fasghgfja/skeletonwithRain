create table Value(
value_id int not null,
value varchar(64) not null,
time_stamp timestamp,
sensor_id int,
PRIMARY KEY (value_id),
FOREIGN KEY (sensor_id) REFERENCES sensor(sensor_id) ON DELETE CASCADE);