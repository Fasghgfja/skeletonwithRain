create table Value(
value varchar(64) not null,
time_stamp timestamp,
sensor_id int
FOREIGN KEY (sensor_id) REFERENCES sensor(sensor_id) ON DELETE CASCADE);