# one user for every role;
# 2 AP, 2SS each,
# User
INSERT INTO userx (ENABLED, FIRST_NAME, LAST_NAME, PASSWORD, USERNAME, CREATE_DATE) VALUES(TRUE, 'Admin', 'Istrator', '$2a$10$Bm27OvKm5FENObph7xPfnO2L1mbKyjpG3LdeixGrJV0xC7TgcFGX2', 'admin', '2016-01-01');
INSERT INTO userx (ENABLED, FIRST_NAME, LAST_NAME, PASSWORD, USERNAME, CREATE_DATE) VALUES(TRUE, 'Susi', 'Kaufgern', '$2a$10$Bm27OvKm5FENObph7xPfnO2L1mbKyjpG3LdeixGrJV0xC7TgcFGX2', 'Susi', '2016-01-01');
INSERT INTO userx (ENABLED, FIRST_NAME, LAST_NAME, PASSWORD, USERNAME, CREATE_DATE) VALUES(TRUE, 'Max', 'Mustermann', '$2a$10$Bm27OvKm5FENObph7xPfnO2L1mbKyjpG3LdeixGrJV0xC7TgcFGX2', 'Max', '2016-01-01');
# Userrole
INSERT INTO userx_user_role (USERX_USERNAME, ROLES) VALUES ('Max', 'USER');
INSERT INTO userx_user_role (USERX_USERNAME, ROLES) VALUES ('admin', 'ADMIN');
INSERT INTO userx_user_role (USERX_USERNAME, ROLES) VALUES ('admin', 'GARDENER');
INSERT INTO userx_user_role (USERX_USERNAME, ROLES) VALUES ('Susi', 'USER');
INSERT INTO userx_user_role (USERX_USERNAME, ROLES) VALUES ('Susi', 'GARDENER');
# AccessPoints
INSERT INTO access_point(ACCESS_POINTID, LOCATION, VALIDATED, CREATE_DATE, UPDATE_DATE) VALUES(1, 'Room1', TRUE, '2023-01-01', '2022-01-01');
INSERT INTO access_point(ACCESS_POINTID, LOCATION, VALIDATED, CREATE_DATE, UPDATE_DATE) VALUES(2, 'Room2', TRUE, '2023-01-01', '2022-01-01');
# Plants
INSERT INTO plant(PLANTID, PLANT_NAME, planted_date) VALUES(1, 'Atomic Northern Lights','2022-01-01');
INSERT INTO plant(PLANTID, PLANT_NAME, planted_date) VALUES(2, 'Charlotte''s Web','2022-01-02');
INSERT INTO plant(PLANTID, PLANT_NAME, planted_date) VALUES(3, 'Alpine Rocket', '2022-01-03');
INSERT INTO plant(PLANTID, PLANT_NAME, planted_date) VALUES(4, 'Blue Dream', '2022-01-04');
INSERT INTO plant(PLANTID, PLANT_NAME, planted_date) VALUES(5, 'Sour Diesel', '2022-01-05');
#SensorStations
INSERT INTO sensor_station(SENSOR_STATION_NAME, CREATE_DATE, UPDATE_DATE, LOCATION ,alarm_switch, plant_plantid, alarm_count_threshold) VALUES('G4T2','2022-01-01','2022-01-05','Room1', 'off',1, 1);
INSERT INTO sensor_station(SENSOR_STATION_NAME, CREATE_DATE, UPDATE_DATE, LOCATION,alarm_switch, plant_plantid, alarm_count_threshold) VALUES('G4T1','2022-01-01','2022-02-05','Room2', 'off',2, 1);
INSERT INTO sensor_station(SENSOR_STATION_NAME, CREATE_DATE, UPDATE_DATE, LOCATION ,alarm_switch, plant_plantid, alarm_count_threshold) VALUES('G4T3','2022-01-01','2022-02-01','Room3', 'off',3, 1);
INSERT INTO sensor_station(SENSOR_STATION_NAME, CREATE_DATE, UPDATE_DATE, LOCATION,alarm_switch, plant_plantid, alarm_count_threshold ) VALUES('G4T4','2022-03-01','2022-02-01','Room4', 'off',4, 1);
#Sensors
insert into sensor (id,uuid,sensor_station_name,type, alarm_count,upper_border, lower_border,create_date) values(1,'000019b1-0000-1000-8000-00805f9b34fb', 'G4T2', 'SOIL_MOISTURE', 0, 0, 0,'2022-01-02');
insert into sensor (id,uuid,sensor_station_name,type, alarm_count,upper_border, lower_border,create_date) values(2,'000019b0-0000-1000-8000-00805f9b34fb', 'G4T2', 'HUMIDITY', 0, 0, 0,'2022-01-02');
insert into sensor (id,uuid,sensor_station_name,type, alarm_count,upper_border, lower_border,create_date) values(3,'000019b2-0000-1000-8000-00805f9b34fb', 'G4T2', 'AIR_PRESSURE', 0, 0, 0,'2022-01-02');
insert into sensor (id,uuid,sensor_station_name,type, alarm_count,upper_border, lower_border,create_date) values(4,'000019b3-0000-1000-8000-00805f9b34fb', 'G4T2', 'TEMPERATURE', 0, 0, 0,'2022-01-02');
insert into sensor (id,uuid,sensor_station_name,type, alarm_count,upper_border, lower_border,create_date) values(5,'010019b5-0000-1000-8000-00805f9b34fb', 'G4T2', 'AIR_QUALITY', 0, 0, 0,'2022-01-02');
insert into sensor (id,uuid,sensor_station_name,type, alarm_count,upper_border, lower_border,create_date) values(6,'100019b5-0000-1000-8000-00805f9b34fb', 'G4T2', 'LIGHT_INTENSITY', 0, 0, 0,'2022-01-02');
insert into sensor (id,uuid,sensor_station_name,type, alarm_count,upper_border, lower_border,create_date) values(7,'000019b6-0000-1000-8000-00805f9b34fb', 'G4T1', 'SOIL_MOISTURE', 0, 0, 0,'2022-01-06');
insert into sensor (id,uuid,sensor_station_name,type, alarm_count,upper_border, lower_border,create_date) values(8,'000019b7-0000-1000-8000-00805f9b34fb', 'G4T1', 'HUMIDITY', 0, 0, 0,'2022-01-07');
insert into sensor (id,uuid,sensor_station_name,type, alarm_count,upper_border, lower_border,create_date) values(9,'000019b8-0000-1000-8000-00805f9b34fb', 'G4T1', 'AIR_PRESSURE', 0, 0, 0,'2022-01-08');
insert into sensor (id,uuid,sensor_station_name,type, alarm_count,upper_border, lower_border,create_date) values(10,'000019b9-0000-1000-8000-00805f9b34fb', 'G4T1', 'TEMPERATURE', 0, 0, 0,'2022-01-09');
insert into sensor (id,uuid,sensor_station_name,type, alarm_count,upper_border, lower_border,create_date) values(11,'010019b10-0000-1000-8000-00805f9b34fb', 'G4T1', 'AIR_QUALITY', 0, 0, 0,'2022-01-10');
insert into sensor (id,uuid,sensor_station_name,type, alarm_count,upper_border, lower_border,create_date) values(12,'100019b10-0000-1000-8000-00805f9b34fb', 'G4T1', 'LIGHT_INTENSITY', 0, 0, 0,'2022-01-10');
insert into sensor (id,uuid,sensor_station_name,type, alarm_count,upper_border, lower_border,create_date) values(13,'00001911-0000-1000-8000-00805f9b34fb', 'G4T3', 'SOIL_MOISTURE', 0, 0, 0,'2022-01-12');
insert into sensor (id,uuid,sensor_station_name,type, alarm_count,upper_border, lower_border,create_date) values(14,'00001912-0000-1000-8000-00805f9b34fb', 'G4T3', 'HUMIDITY', 0, 0, 0,'2022-01-01');
insert into sensor (id,uuid,sensor_station_name,type, alarm_count,upper_border, lower_border,create_date) values(15,'00001913-0000-1000-8000-00805f9b34fb', 'G4T3', 'AIR_PRESSURE', 0, 0, 0,'2022-02-04');
insert into sensor (id,uuid,sensor_station_name,type, alarm_count,upper_border, lower_border,create_date) values(16,'00001914-0000-1000-8000-00805f9b34fb', 'G4T3', 'TEMPERATURE', 0, 0, 0,'2022-02-01');
insert into sensor (id,uuid,sensor_station_name,type, alarm_count,upper_border, lower_border,create_date) values(17,'01001915-0000-1000-8000-00805f9b34fb', 'G4T3', 'AIR_QUALITY', 0, 0, 0,'2022-03-01');
insert into sensor (id,uuid,sensor_station_name,type, alarm_count,upper_border, lower_border,create_date) values(18,'100019b10-0000-1000-8000-00805f9b34fb', 'G4T3', 'LIGHT_INTENSITY', 0, 0, 0,'2022-01-10');
insert into sensor (id,uuid,sensor_station_name,type, alarm_count,upper_border, lower_border,create_date) values(19,'00001911-0000-1000-8000-00805f9b34fb', 'G4T4', 'SOIL_MOISTURE', 0, 0, 0,'2022-01-12');
insert into sensor (id,uuid,sensor_station_name,type, alarm_count,upper_border, lower_border,create_date) values(20,'00001912-0000-1000-8000-00805f9b34fb', 'G4T4', 'HUMIDITY', 0, 0, 0,'2022-01-01');
insert into sensor (id,uuid,sensor_station_name,type, alarm_count,upper_border, lower_border,create_date) values(21,'00001913-0000-1000-8000-00805f9b34fb', 'G4T4', 'AIR_PRESSURE', 0, 0, 0,'2022-02-04');
insert into sensor (id,uuid,sensor_station_name,type, alarm_count,upper_border, lower_border,create_date) values(22,'00001914-0000-1000-8000-00805f9b34fb', 'G4T4', 'TEMPERATURE', 0, 0, 0,'2022-02-01');
insert into sensor (id,uuid,sensor_station_name,type, alarm_count,upper_border, lower_border,create_date) values(23,'01001915-0000-1000-8000-00805f9b34fb', 'G4T4', 'AIR_QUALITY', 0, 0, 0,'2022-03-01');
insert into sensor (id,uuid,sensor_station_name,type, alarm_count,upper_border, lower_border,create_date) values(24,'100019b10-0000-1000-8000-00805f9b34fb', 'G4T4', 'LIGHT_INTENSITY', 0, 0, 0,'2022-01-10');

INSERT INTO measurement(ID ,TIMESTAMP,TYPE,UNIT,VALUE_S,SENSOR_STATION_NAME) VALUES(1,'2022-02-02 21:27:31','HUMIDITY','%','36','G4T4');
INSERT INTO measurement(ID ,TIMESTAMP,TYPE,UNIT,VALUE_S,SENSOR_STATION_NAME) VALUES(2,'2022-02-03 12:26:32','HUMIDITY','%','20','G4T4');
INSERT INTO measurement(ID ,TIMESTAMP,TYPE,UNIT,VALUE_S,SENSOR_STATION_NAME) VALUES(3,'2022-02-04 21:25:33','HUMIDITY','%','50','G4T4');
INSERT INTO measurement(ID ,TIMESTAMP,TYPE,UNIT,VALUE_S,SENSOR_STATION_NAME) VALUES(4,'2022-02-05 15:24:34','HUMIDITY','%','40','G4T4');
INSERT INTO measurement(ID ,TIMESTAMP,TYPE,UNIT,VALUE_S,SENSOR_STATION_NAME) VALUES(5,'2022-02-06 11:23:35','HUMIDITY','%','50','G4T4');
INSERT INTO measurement(ID ,TIMESTAMP,TYPE,UNIT,VALUE_S,SENSOR_STATION_NAME) VALUES(6,'2022-02-07 18:22:36','HUMIDITY','%','40','G4T4');
INSERT INTO measurement(ID ,TIMESTAMP,TYPE,UNIT,VALUE_S,SENSOR_STATION_NAME) VALUES(7,'2022-02-08 11:21:37','TEMPERATURE','C','32','G4T4');
INSERT INTO measurement(ID ,TIMESTAMP,TYPE,UNIT,VALUE_S,SENSOR_STATION_NAME) VALUES(8,'2022-02-09 12:11:38','TEMPERATURE','C','34','G4T4');
INSERT INTO measurement(ID ,TIMESTAMP,TYPE,UNIT,VALUE_S,SENSOR_STATION_NAME) VALUES(9,'2022-02-12 13:10:40','TEMPERATURE','C','23','G4T4');
INSERT INTO measurement(ID ,TIMESTAMP,TYPE,UNIT,VALUE_S,SENSOR_STATION_NAME) VALUES(10,'2022-02-22 14:09:40','TEMPERATURE','C','34','G4T4');
INSERT INTO measurement(ID ,TIMESTAMP,TYPE,UNIT,VALUE_S,SENSOR_STATION_NAME) VALUES(11,'2022-02-12 15:08:47','TEMPERATURE','C','28','G4T4');
INSERT INTO measurement(ID ,TIMESTAMP,TYPE,UNIT,VALUE_S,SENSOR_STATION_NAME) VALUES(12,'2022-02-12 16:07:46','SOIL_MOISTURE','%','11','G4T4');
INSERT INTO measurement(ID ,TIMESTAMP,TYPE,UNIT,VALUE_S,SENSOR_STATION_NAME) VALUES(13,'2022-02-02 17:06:45','SOIL_MOISTURE','%','17','G4T4');
INSERT INTO measurement(ID ,TIMESTAMP,TYPE,UNIT,VALUE_S,SENSOR_STATION_NAME) VALUES(14,'2022-02-12 18:05:44','SOIL_MOISTURE','%','99','G4T4');
INSERT INTO measurement(ID ,TIMESTAMP,TYPE,UNIT,VALUE_S,SENSOR_STATION_NAME) VALUES(15,'2022-01-23 19:04:43','SOIL_MOISTURE','%','62','G4T4');
INSERT INTO measurement(ID ,TIMESTAMP,TYPE,UNIT,VALUE_S,SENSOR_STATION_NAME) VALUES(16,'2022-02-26 20:03:42','SOIL_MOISTURE','%','47','G4T4');
INSERT INTO measurement(ID ,TIMESTAMP,TYPE,UNIT,VALUE_S,SENSOR_STATION_NAME) VALUES(17,'2022-03-21 21:02:41','SOIL_MOISTURE','%','35','G4T4');
INSERT INTO measurement(ID ,TIMESTAMP,TYPE,UNIT,VALUE_S,SENSOR_STATION_NAME) VALUES(18,'2022-04-10 23:01:30','LIGHT_INTENSITY','lux','1002','G4T4');
INSERT INTO measurement(ID ,TIMESTAMP,TYPE,UNIT,VALUE_S,SENSOR_STATION_NAME) VALUES(19,'2022-05-19 02:00:30','LIGHT_INTENSITY','lux','1000','G4T4');
INSERT INTO measurement(ID ,TIMESTAMP,TYPE,UNIT,VALUE_S,SENSOR_STATION_NAME) VALUES(20,'2022-06-18 03:59:30','LIGHT_INTENSITY','lux','1000','G4T4');
INSERT INTO measurement(ID ,TIMESTAMP,TYPE,UNIT,VALUE_S,SENSOR_STATION_NAME) VALUES(21,'2022-01-17 04:58:30','AIR_QUALITY','Aqi','350','G4T4');


INSERT INTO measurement(ID ,TIMESTAMP,TYPE,UNIT,VALUE_S,SENSOR_STATION_NAME) VALUES(22,'2022-01-02 05:57:30','HUMIDITY','%','10','G4T3');
INSERT INTO measurement(ID ,TIMESTAMP,TYPE,UNIT,VALUE_S,SENSOR_STATION_NAME) VALUES(23,'2022-01-03 06:56:30','TEMPERATURE','C','24','G4T3');
INSERT INTO measurement(ID ,TIMESTAMP,TYPE,UNIT,VALUE_S,SENSOR_STATION_NAME) VALUES(24,'2022-01-04 07:55:30','TEMPERATURE','C','24','G4T3');
INSERT INTO measurement(ID ,TIMESTAMP,TYPE,UNIT,VALUE_S,SENSOR_STATION_NAME) VALUES(25,'2022-01-05 08:54:30','TEMPERATURE','C','34','G4T3');
INSERT INTO measurement(ID ,TIMESTAMP,TYPE,UNIT,VALUE_S,SENSOR_STATION_NAME) VALUES(26,'2022-01-06 09:53:30','TEMPERATURE','C','12','G4T3');
INSERT INTO measurement(ID ,TIMESTAMP,TYPE,UNIT,VALUE_S,SENSOR_STATION_NAME) VALUES(27,'2022-01-07 10:52:30','TEMPERATURE','C','11','G4T3');
INSERT INTO measurement(ID ,TIMESTAMP,TYPE,UNIT,VALUE_S,SENSOR_STATION_NAME) VALUES(28,'2022-01-08 11:51:30','SOIL_MOISTURE','%','51','G4T3');
INSERT INTO measurement(ID ,TIMESTAMP,TYPE,UNIT,VALUE_S,SENSOR_STATION_NAME) VALUES(29,'2022-01-09 12:50:30','SOIL_MOISTURE','%','55','G4T3');
INSERT INTO measurement(ID ,TIMESTAMP,TYPE,UNIT,VALUE_S,SENSOR_STATION_NAME) VALUES(30,'2022-01-10 13:49:30','SOIL_MOISTURE','%','75','G4T3');
INSERT INTO measurement(ID ,TIMESTAMP,TYPE,UNIT,VALUE_S,SENSOR_STATION_NAME) VALUES(31,'2022-01-11 14:48:30','SOIL_MOISTURE','%','15','G4T3');
INSERT INTO measurement(ID ,TIMESTAMP,TYPE,UNIT,VALUE_S,SENSOR_STATION_NAME) VALUES(32,'2022-01-12 15:47:30','LIGHT_INTENSITY','lux','350','G4T3');
INSERT INTO measurement(ID ,TIMESTAMP,TYPE,UNIT,VALUE_S,SENSOR_STATION_NAME) VALUES(33,'2022-01-13 16:46:30','LIGHT_INTENSITY','lux','550','G4T3');
INSERT INTO measurement(ID ,TIMESTAMP,TYPE,UNIT,VALUE_S,SENSOR_STATION_NAME) VALUES(34,'2022-01-14 17:45:30','AIR_QUALITY','Assessment','200','G4T3');
INSERT INTO measurement(ID ,TIMESTAMP,TYPE,UNIT,VALUE_S,SENSOR_STATION_NAME) VALUES(35,'2022-01-15 18:44:30','HUMIDITY','%','10','G4T3');
INSERT INTO measurement(ID ,TIMESTAMP,TYPE,UNIT,VALUE_S,SENSOR_STATION_NAME) VALUES(36,'2022-01-16 19:43:30','HUMIDITY','%','20','G4T3');

INSERT INTO measurement(ID ,TIMESTAMP,TYPE,UNIT,VALUE_S,SENSOR_STATION_NAME) VALUES(37,'2022-02-02 20:21:01','HUMIDITY','%','36','G4T1');
INSERT INTO measurement(ID ,TIMESTAMP,TYPE,UNIT,VALUE_S,SENSOR_STATION_NAME) VALUES(38,'2022-02-03 21:22:02','HUMIDITY','%','23','G4T1');
INSERT INTO measurement(ID ,TIMESTAMP,TYPE,UNIT,VALUE_S,SENSOR_STATION_NAME) VALUES(39,'2022-02-04 22:23:03','HUMIDITY','%','50','G4T1');
INSERT INTO measurement(ID ,TIMESTAMP,TYPE,UNIT,VALUE_S,SENSOR_STATION_NAME) VALUES(40,'2022-02-05 23:24:18','HUMIDITY','%','40','G4T1');
INSERT INTO measurement(ID ,TIMESTAMP,TYPE,UNIT,VALUE_S,SENSOR_STATION_NAME) VALUES(41,'2022-02-04 00:25:17','HUMIDITY','%','50','G4T1');
INSERT INTO measurement(ID ,TIMESTAMP,TYPE,UNIT,VALUE_S,SENSOR_STATION_NAME) VALUES(42,'2022-02-05 02:26:16','HUMIDITY','%','40','G4T1');
INSERT INTO measurement(ID ,TIMESTAMP,TYPE,UNIT,VALUE_S,SENSOR_STATION_NAME) VALUES(43,'2022-02-02 01:27:15','TEMPERATURE','C','22','G4T1');
INSERT INTO measurement(ID ,TIMESTAMP,TYPE,UNIT,VALUE_S,SENSOR_STATION_NAME) VALUES(44,'2022-02-02 03:28:14','TEMPERATURE','C','24','G4T1');
INSERT INTO measurement(ID ,TIMESTAMP,TYPE,UNIT,VALUE_S,SENSOR_STATION_NAME) VALUES(45,'2022-02-02 04:29:13','TEMPERATURE','C','21','G4T1');
INSERT INTO measurement(ID ,TIMESTAMP,TYPE,UNIT,VALUE_S,SENSOR_STATION_NAME) VALUES(46,'2022-02-02 05:30:12','TEMPERATURE','C','24','G4T1');
INSERT INTO measurement(ID ,TIMESTAMP,TYPE,UNIT,VALUE_S,SENSOR_STATION_NAME) VALUES(47,'2022-02-02 06:31:11','TEMPERATURE','C','29','G4T1');
INSERT INTO measurement(ID ,TIMESTAMP,TYPE,UNIT,VALUE_S,SENSOR_STATION_NAME) VALUES(48,'2022-02-02 07:32:10','SOIL_MOISTURE','%','12','G4T1');
INSERT INTO measurement(ID ,TIMESTAMP,TYPE,UNIT,VALUE_S,SENSOR_STATION_NAME) VALUES(49,'2022-02-02 08:33:09','SOIL_MOISTURE','%','24','G4T1');
INSERT INTO measurement(ID ,TIMESTAMP,TYPE,UNIT,VALUE_S,SENSOR_STATION_NAME) VALUES(50,'2022-02-02 09:34:08','SOIL_MOISTURE','%','46','G4T1');
INSERT INTO measurement(ID ,TIMESTAMP,TYPE,UNIT,VALUE_S,SENSOR_STATION_NAME) VALUES(51,'2022-01-23 20:35:07','SOIL_MOISTURE','%','68','G4T1');
INSERT INTO measurement(ID ,TIMESTAMP,TYPE,UNIT,VALUE_S,SENSOR_STATION_NAME) VALUES(52,'2022-01-22 21:36:06','SOIL_MOISTURE','%','47','G4T1');
INSERT INTO measurement(ID ,TIMESTAMP,TYPE,UNIT,VALUE_S,SENSOR_STATION_NAME) VALUES(53,'2022-01-21 22:37:05','SOIL_MOISTURE','%','34','G4T1');
INSERT INTO measurement(ID ,TIMESTAMP,TYPE,UNIT,VALUE_S,SENSOR_STATION_NAME) VALUES(54,'2022-01-20 23:38:04','LIGHT_INTENSITY','lux','2000','G4T1');
INSERT INTO measurement(ID ,TIMESTAMP,TYPE,UNIT,VALUE_S,SENSOR_STATION_NAME) VALUES(55,'2022-01-19 00:39:03','LIGHT_INTENSITY','lux','1200','G4T1');
INSERT INTO measurement(ID ,TIMESTAMP,TYPE,UNIT,VALUE_S,SENSOR_STATION_NAME) VALUES(56,'2022-01-18 01:40:02','LIGHT_INTENSITY','lux','900','G4T1');
INSERT INTO measurement(ID ,TIMESTAMP,TYPE,UNIT,VALUE_S,SENSOR_STATION_NAME) VALUES(57,'2022-01-17 02:41:01','AIR_QUALITY','Assessment','350','G4T1');
# Sending interval
insert into ssinterval(ID, web_app_interval, measurement_interval, access_point_access_pointid)values (1, '1', '1',1)
insert into ssinterval(ID, web_app_interval, measurement_interval, access_point_access_pointid)values (2, '1', '1',2)