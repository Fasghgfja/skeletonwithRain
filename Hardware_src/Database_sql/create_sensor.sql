create table Sensor(
sensor_id int not null primary key,
uuid varchar,
station_name varchar(64) not null,
sensor_type varchar,
alarm_count int,
lower_boarder varchar,
upper_boarder varchar,
FOREIGN KEY (station_name) REFERENCES sensorstation(name) ON DELETE CASCADE);