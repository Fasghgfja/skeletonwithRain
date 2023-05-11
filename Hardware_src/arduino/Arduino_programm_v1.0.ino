#include <Arduino.h>
#include <ArduinoBLE.h>
#include <SPI.h>
#include <Adafruit_Sensor.h>
#include "Adafruit_BME680.h"
//----------------------------------------------------------------------------------------------------init BLE -----------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
Adafruit_BME680 bme;

BLEService readSensorDataService("181A");
BLEIntCharacteristic ligthValueCharacteristic("19b0", BLERead | BLEWrite);
BLEIntCharacteristic hygroValueCharacteristic("19b1", BLERead | BLEWrite);
BLEFloatCharacteristic tempValueCharacteristic("19b2", BLERead | BLEWrite);
BLEFloatCharacteristic humidityValueCharacteristic("19b3", BLERead | BLEWrite);
BLEFloatCharacteristic pressureValueCharacteristic("19b4", BLERead | BLEWrite);
BLEFloatCharacteristic gasValueCharacteristic("19b5", BLERead | BLEWrite);
//----------------------------------------------------------------------------------------------------init pins--------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//dev: means development and all function and attributes that marked with that are just for the development phase
//Define the pins to identify
const int button_state = D12;
const int ligth_sensor = A0;
const int green = A1;
const int blue = A2;
const int red = A3;
const int hygro_sensor = A6;
const int piezo = A7;
//----------------------------------------------------------------------------------------------------init helpers----------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
const byte ligth_on = 254;                               //idea to define different clors depending on the alarm only defined for waterAlarm
int readButton = 0;                                         
bool Alarm = false;
byte alarm_ligth_state = 0;
byte read_sensor_state = 0;
byte alarm_ligth_type = 6;
bool connection_on = false;
bool piezo_on = false;
bool piep = false;
//----------------------------------------------------------------------------------------------------init timer attributes----------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//Timer variablen für Zeitmessung
unsigned long readSensor_timer_start;
unsigned long piezo_timer_start;
unsigned long readAir_condition_timer_delta = 3000;       //to test it is set to 9s, need to change to x-mins?
unsigned long Alarm_timer_start;
unsigned long timer_current;           
unsigned long Alarm_time_delta = 0;
//----------------------------------------------------------------------------------------------------init BLE functions----------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
void readAirValues(BLEDevice central, BLECharacteristic characteristic);
void readHygroValue(BLEDevice central, BLECharacteristic characteristic);
void readLigthValue(BLEDevice central, BLECharacteristic characteristic);
void blePeripheralConnectHandler(BLEDevice central);
void blePeripheralDisconnectHandler(BLEDevice central);

//--------------------------------------------------------------------------------SETUP-----------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
void setup() {
  pinMode(green, OUTPUT);
  pinMode(blue, OUTPUT);
  pinMode(red, OUTPUT);
  pinMode(button_state, INPUT);
  pinMode(hygro_sensor, INPUT);
  pinMode(piezo, OUTPUT);
  pinMode(ligth_sensor, INPUT);
  //BluetoothLE setup
  //while (!Serial);
  if (!BLE.begin()) {
    while(1);
  }

  BLE.setLocalName("G4T2");
  BLE.setDeviceName("G4T2");
  //---------------------------------------------------------------
  BLE.setAdvertisedService(readSensorDataService);
  //---------------------------------------------------------------
  BLEDescriptor ligthValueDescriptor("2901", "LIGHT_INTENSITY");
  BLEDescriptor hygroValueDescriptor("2901", "SOIL_MOISTURE");
  BLEDescriptor tempValueDescriptor("2901", "TEMPERATURE");
  BLEDescriptor humidityValueDescriptor("2901", "HUMIDITY");
  BLEDescriptor pressureValueDescriptor("2901", "AIR_PRESSURE");
  BLEDescriptor gasValueDescriptor("2901", "AIR_QUALITY");

  //---------------------------------------------------------------
  ligthValueCharacteristic.addDescriptor(ligthValueDescriptor);
  hygroValueCharacteristic.addDescriptor(hygroValueDescriptor);
  tempValueCharacteristic.addDescriptor(tempValueDescriptor);
  humidityValueCharacteristic.addDescriptor(humidityValueDescriptor);
  pressureValueCharacteristic.addDescriptor(pressureValueDescriptor);
  gasValueCharacteristic.addDescriptor(gasValueDescriptor);
  //---------------------------------------------------------------
  ligthValueCharacteristic.writeValue(0);
  hygroValueCharacteristic.writeValue(0);
  tempValueCharacteristic.writeValue(0);
  humidityValueCharacteristic.writeValue(0);
  pressureValueCharacteristic.writeValue(0);
  gasValueCharacteristic.writeValue(0);
  //---------------------------------------------------------------
  ligthValueCharacteristic.setEventHandler(BLERead, readLigthValue);
  hygroValueCharacteristic.setEventHandler(BLERead, readHygroValue);
  tempValueCharacteristic.setEventHandler(BLERead, readAirValues); 
  //---------------------------------------------------------------
  readSensorDataService.addCharacteristic(ligthValueCharacteristic);
  readSensorDataService.addCharacteristic(hygroValueCharacteristic); 
  readSensorDataService.addCharacteristic(tempValueCharacteristic);
  readSensorDataService.addCharacteristic(humidityValueCharacteristic); 
  readSensorDataService.addCharacteristic(pressureValueCharacteristic);
  readSensorDataService.addCharacteristic(gasValueCharacteristic);  
  //---------------------------------------------------------------
  BLE.addService(readSensorDataService);
  //---------------------------------------------------------------
  BLE.setEventHandler(BLEConnected, blePeripheralConnectHandler);
  BLE.setEventHandler(BLEDisconnected, blePeripheralDisconnectHandler);
  //---------------------------------------------------------------
  BLE.advertise();   
//BME688 setup
  //while (!Serial);
  if (!bme.begin()) {
    while (1);
  }

  // Set up oversampling and filter initialization
  bme.setTemperatureOversampling(BME680_OS_8X);
  bme.setHumidityOversampling(BME680_OS_2X);
  bme.setPressureOversampling(BME680_OS_4X);
  bme.setGasHeater(320, 150); // 320*C for 150 ms  
}
//--------------------------------------------------------------------------------------LOOP-----------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
void loop(){
  readButton = digitalRead(button_state);
  timer_current = millis();  
  if(connection_on){
    BLE.poll();  
  }

  if(piezo_on && (timer_current - piezo_timer_start) >= 1000){
    if(piep){
      noTone(piezo);
      piep = false;
    }
    else{
      tone(piezo, 100);
      piep = true;
    }
    piezo_timer_start = millis();
  }
  //-------------------------------------------------------------------------------------check alarm signal----------------------------------------------------------------------------------------------
  if(hygroValueCharacteristic.written()){
    hygroAlarm();
  }
  if(ligthValueCharacteristic.written()){
    ligthAlarm();
  }
  if(tempValueCharacteristic.written()){
    tempAlarm();
  }
  if(humidityValueCharacteristic.written()){
    humAlarm();
  }
  if(pressureValueCharacteristic.written()){
    pessAlarm();
  }
  if(gasValueCharacteristic.written()){
    gasAlarm();    
  }  
  if(readButton == HIGH){
    connection_on = true;
    piezo_on = true;
    piezo_timer_start = millis();
  }
  //Is called to evaluate if air cond is reat successfull
  if( (timer_current - readSensor_timer_start) >= readAir_condition_timer_delta && read_sensor_state == 1){
    readAir_condition_sensor();    
    readSensor_timer_start = millis();
    read_sensor_state = 0;
  }

  //Is called if an alarm signal is dedected
  if(Alarm && (timer_current - Alarm_timer_start) >= Alarm_time_delta){
    
    switch(alarm_ligth_type){
    case 0: sequence_hygro_alarm(); break;
    case 1: sequence_ligth_alarm(); break;
    case 2: sequence_temp_alarm(); break;
    case 3: sequence_humidy_alarm(); break;
    case 4: sequence_pressure_alarm(); break;
    case 5: sequence_gas_alarm(); break;
    }
    Alarm_timer_start = millis();   
  }

}
//--------------------------------------------------------------------------------------------------Alarm functions-----------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
void gasAlarm(){
  alarm_ligth_type = 5;
  Alarm_time_delta = 1000;
  alarm_controller(); //uncomment for ligth sequence //test ok
}
void pessAlarm(){
  alarm_ligth_type = 4;
  Alarm_time_delta = 3000;
  alarm_controller(); //uncomment for ligth sequence
}
void humAlarm(){
  alarm_ligth_type = 3;
  Alarm_time_delta = 3000;
  alarm_controller(); //uncomment for ligth sequence // test ok
}
void tempAlarm(){
  alarm_ligth_type = 2;
  Alarm_time_delta = 3000;
  alarm_controller(); //uncomment for ligth sequence
}
void ligthAlarm(){
  alarm_ligth_type = 1;
  Alarm_time_delta = 1000;
  alarm_controller(); //uncomment for ligth sequence
}
void hygroAlarm(){
  alarm_ligth_type = 0;
  Alarm_time_delta = 1000; 
  alarm_controller(); //uncomment for ligth sequence    
}
void alarm_controller(){
  if(Alarm){
      Alarm = false;  
      alarm_ligth_type = 6;
      lightOff();          
    }
    else{
      Alarm = true;
    }
    Alarm_timer_start = millis();
}
//--------------------------------------------------------------------------------------Air quality sensor check if successfull--------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
void readAir_condition_sensor(){
  
  if (!bme.endReading()) {
    //Serial.println(F("Failed to complete reading :("));
    return;
  }

}
//---------------------------------------------------------------------------------------sequence functions for alarmligth-------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//green ligth with delay 3sec +
void sequence_temp_alarm(){
  switch(alarm_ligth_state){
    case 0: lightOn(0, 0, ligth_on); alarm_ligth_state++; break;
    case 1: lightOn(0, 0, 0); alarm_ligth_state = 0; break;
  }  
}
//blue ligth with delay 3sec +
void sequence_humidy_alarm(){
  switch(alarm_ligth_state){
    case 0: lightOn(0, ligth_on, 0); alarm_ligth_state++; break;
    case 1: lightOn(0, 0, 0); alarm_ligth_state = 0; break;
  }  
}
// red ligth with delay 3sec +
void sequence_pressure_alarm(){
  switch(alarm_ligth_state){
    case 0: lightOn(ligth_on, 0, 0); alarm_ligth_state++; break;
    case 1: lightOn(0, 0, 0); alarm_ligth_state = 0; break;
  }  
}
// green ligth with delay 1sec +
void sequence_gas_alarm(){
  switch(alarm_ligth_state){
    case 0: lightOn(0, 0, ligth_on); alarm_ligth_state++; break;
    case 1: lightOn(0, 0, 0); alarm_ligth_state = 0; break;
  }  
}
// red ligth with delay 1sec +
void sequence_hygro_alarm(){
  switch(alarm_ligth_state){
    case 0: lightOn(ligth_on, 0, 0); alarm_ligth_state++; break;
    case 1: lightOn(0, 0, 0); alarm_ligth_state = 0; break;
  }  
}
// blue ligth with delay 1sec +
void sequence_ligth_alarm(){
  switch(alarm_ligth_state){
    case 0: lightOn(0, ligth_on, 0); alarm_ligth_state++; break;
    case 1: lightOn(0, 0, 0); alarm_ligth_state = 0; break;
  }  
}
//--------------------------------------------------------------------------------------------------Functions to switch ligth on/off-----------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
void lightOff(){
  analogWrite(A1, 0);
  analogWrite(A2, 0);
  analogWrite(A3, 0);
  if(alarm_ligth_state != 0){  
    alarm_ligth_state = 0;  //if the state isnt zero when the alarm get stoped
  }
}

void lightOn(byte intense_red, byte intense_green, byte intense_blue){
  analogWrite(A1, intense_blue);
  analogWrite(A2, intense_green);
  analogWrite(A3, intense_red);
}
//-------------------------------------------------------------------------------------------------------BLE functions called by accesspoint------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
void readAirValues(BLEDevice central, BLECharacteristic characteristic){
  read_sensor_state = 1;
  unsigned long endTime = bme.beginReading();
  if (endTime == 0) {
    return;
  }
  
  float pressure = (bme.pressure / 100.0);
  float gas = (bme.gas_resistance / 1000.0);
  tempValueCharacteristic.writeValue(bme.temperature);
  humidityValueCharacteristic.writeValue(bme.humidity);
  pressureValueCharacteristic.writeValue(pressure);
  gasValueCharacteristic.writeValue(gas);
}

void readLigthValue(BLEDevice central, BLECharacteristic characteristic){
  int ligth_value = analogRead(ligth_sensor);
  ligthValueCharacteristic.setValue(ligth_value);
}

void readHygroValue(BLEDevice central, BLECharacteristic characteristic){
  int hygro_value = analogRead(hygro_sensor);
  hygroValueCharacteristic.setValue(hygro_value);
}

void blePeripheralConnectHandler(BLEDevice central) {
  if(piezo_on){
    piezo_on = false;
    noTone(piezo);
  }
}

void blePeripheralDisconnectHandler(BLEDevice central) {
}
