
-- $2y$10$P7im4OMw6hsnPWLpJ1nVKup1jlEFsLIek9D3lglmZv.Tq05GDEhMS -> passwd
INSERT INTO USERX (ENABLED, FIRST_NAME, LAST_NAME, PASSWORD, USERNAME, CREATE_USER_USERNAME, CREATE_DATE) VALUES(TRUE, 'Admin', 'Istrator', '$2a$10$Bm27OvKm5FENObph7xPfnO2L1mbKyjpG3LdeixGrJV0xC7TgcFGX2', 'admin', 'admin', '2016-01-01')
INSERT INTO USERX_USER_ROLE (USERX_USERNAME, ROLES) VALUES ('admin', 'ADMIN')
INSERT INTO USERX_USER_ROLE (USERX_USERNAME, ROLES) VALUES ('admin', 'GARDENER')
INSERT INTO USERX (ENABLED, FIRST_NAME, LAST_NAME, PASSWORD, USERNAME, CREATE_USER_USERNAME, CREATE_DATE) VALUES(TRUE, 'Susi', 'Kaufgern', '$2a$10$Bm27OvKm5FENObph7xPfnO2L1mbKyjpG3LdeixGrJV0xC7TgcFGX2', 'user1', 'admin', '2016-01-01')
INSERT INTO USERX_USER_ROLE (USERX_USERNAME, ROLES) VALUES ('user1', 'USER')
INSERT INTO USERX_USER_ROLE (USERX_USERNAME, ROLES) VALUES ('user1', 'GARDENER')
INSERT INTO USERX (ENABLED, FIRST_NAME, LAST_NAME, PASSWORD, USERNAME, CREATE_USER_USERNAME, CREATE_DATE) VALUES(TRUE, 'Max', 'Mustermann', '$2a$10$Bm27OvKm5FENObph7xPfnO2L1mbKyjpG3LdeixGrJV0xC7TgcFGX2', 'user2', 'admin', '2016-01-01')
INSERT INTO USERX_USER_ROLE (USERX_USERNAME, ROLES) VALUES ('user2', 'USER')
INSERT INTO USERX (ENABLED, FIRST_NAME, LAST_NAME, PASSWORD, USERNAME, CREATE_USER_USERNAME, CREATE_DATE) VALUES(TRUE, 'Elvis', 'The King', '$2a$10$Bm27OvKm5FENObph7xPfnO2L1mbKyjpG3LdeixGrJV0xC7TgcFGX2', 'elvis', 'elvis', '2016-01-01')
INSERT INTO USERX_USER_ROLE (USERX_USERNAME, ROLES) VALUES ('elvis', 'ADMIN')
INSERT INTO PLANT(PLANTID, PLANT_NAME, CREATE_DATE) VALUES(0, 'Atomic Northern Lights','2022-01-01')
INSERT INTO PLANT(PLANTID, PLANT_NAME, CREATE_DATE,UPDATE_DATE) VALUES(1, 'Charlotte''s Web','2022-01-01','2022-01-01')
INSERT INTO PLANT(PLANTID, PLANT_NAME, CREATE_DATE,UPDATE_DATE) VALUES(2, 'Alpine Rocket', '2022-01-04','2022-05-01')
INSERT INTO PLANT(PLANTID, PLANT_NAME, CREATE_DATE,UPDATE_DATE) VALUES(3, 'Blue Dream', '2022-01-02','2022-01-01')
INSERT INTO PLANT(PLANTID, PLANT_NAME, CREATE_DATE,UPDATE_DATE) VALUES(4, 'Sour Diesel', '2022-01-01','2022-03-01')
INSERT INTO SENSOR_STATION(SENSOR_STATIONID, CREATE_DATE, UPDATE_DATE, LOCATION) VALUES(1,'2022-01-01','2022-01-05','Room1')
INSERT INTO SENSOR_STATION(SENSOR_STATIONID, CREATE_DATE, UPDATE_DATE, LOCATION) VALUES(2,'2022-01-01','2022-02-05','Room2')
INSERT INTO SENSOR_STATION(SENSOR_STATIONID, CREATE_DATE, UPDATE_DATE, LOCATION) VALUES(3,'2022-01-01','2022-02-01','Room3')
INSERT INTO SENSOR_STATION(SENSOR_STATIONID, CREATE_DATE, UPDATE_DATE, LOCATION) VALUES(4,'2022-03-01','2022-02-01','Room4')
INSERT INTO SENSOR_STATION(SENSOR_STATIONID, CREATE_DATE, UPDATE_DATE, LOCATION) VALUES(5,'2022-03-01','2022-01-01','Room5')
INSERT INTO SENSOR_STATION(SENSOR_STATIONID, CREATE_DATE, UPDATE_DATE, LOCATION) VALUES(6,'2022-03-01','2022-01-01','Room6')
INSERT INTO SENSOR_STATION(SENSOR_STATIONID, CREATE_DATE, UPDATE_DATE, LOCATION) VALUES(7,'2022-01-02','2022-01-01','Room7')
INSERT INTO SENSOR_STATION(SENSOR_STATIONID, CREATE_DATE, UPDATE_DATE, LOCATION) VALUES(8,'2022-01-02','2022-01-05','Room8')
INSERT INTO SENSOR_STATION(SENSOR_STATIONID, CREATE_DATE, UPDATE_DATE, LOCATION) VALUES(9,'2022-01-02','2022-01-05','Room9')
INSERT INTO LOG(ID, DATE, TEXT) VALUES(1,'2022-01-02','HKÖJKLÖKLJÖVGJTGDJDJFGJ')
INSERT INTO LOG(ID, DATE, TEXT) VALUES(2,'2022-01-02','HKÖJKLÖKLJÖVGJTGDJDJFGJ')
INSERT INTO LOG(ID, DATE, TEXT) VALUES(3,'2022-01-02','HKÖJKLÖKLJÖVGJTGDJDJFGJ')
INSERT INTO LOG(ID, DATE, TEXT) VALUES(4,'2022-01-02','HKÖJKLÖKLJÖVGJTGDJDJFGJ')
INSERT INTO MEASUREMENT(ID ,TIMESTAMP,TYPE,UNIT,VALUE_S,PLANT_ID,SENSOR_STATION_ID) VALUES(1,'2022-01-02','HUMIDITY','%','36',1,1)
INSERT INTO MEASUREMENT(ID ,TIMESTAMP,TYPE,UNIT,VALUE_S,PLANT_ID,SENSOR_STATION_ID) VALUES(2,'2022-01-02','TEMPERATURE','C','22',1,1)
INSERT INTO MEASUREMENT(ID ,TIMESTAMP,TYPE,UNIT,VALUE_S,PLANT_ID,SENSOR_STATION_ID) VALUES(3,'2022-01-02','SOIL_MOISTURE','%','12',1,1)
INSERT INTO MEASUREMENT(ID ,TIMESTAMP,TYPE,UNIT,VALUE_S,PLANT_ID,SENSOR_STATION_ID) VALUES(4,'2022-01-02','LIGHT_INTENSITY','lux','2000',1,1)
INSERT INTO MEASUREMENT(ID ,TIMESTAMP,TYPE,UNIT,VALUE_S,PLANT_ID,SENSOR_STATION_ID) VALUES(5,'2022-01-02','AIR_QUALITY','Assessment','350',1,1)
INSERT INTO MEASUREMENT(ID ,TIMESTAMP,TYPE,UNIT,VALUE_S,PLANT_ID,SENSOR_STATION_ID) VALUES(6,'2022-01-02','HUMIDITY','%','10',2,2)
INSERT INTO MEASUREMENT(ID ,TIMESTAMP,TYPE,UNIT,VALUE_S,PLANT_ID,SENSOR_STATION_ID) VALUES(7,'2022-01-02','TEMPERATURE','C','20',2,2)
INSERT INTO MEASUREMENT(ID ,TIMESTAMP,TYPE,UNIT,VALUE_S,PLANT_ID,SENSOR_STATION_ID) VALUES(8,'2022-01-02','SOIL_MOISTURE','%','5',2,2)
INSERT INTO MEASUREMENT(ID ,TIMESTAMP,TYPE,UNIT,VALUE_S,PLANT_ID,SENSOR_STATION_ID) VALUES(9,'2022-01-02','LIGHT_INTENSITY','lux','350',2,2)
INSERT INTO MEASUREMENT(ID ,TIMESTAMP,TYPE,UNIT,VALUE_S,PLANT_ID,SENSOR_STATION_ID) VALUES(10,'2022-01-02','AIR_QUALITY','Assessment','200',2,2)


