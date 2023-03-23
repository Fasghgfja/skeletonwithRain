//dev: means development and all function and attributes that marked with that are just for the development phase
//Define the pins to identify
const int button_state = D12;
const int ligth_sensor = A0;
const int green = A1;
const int blue = A2;
const int red = A3;
const int hygro_sensor = A6;
const int piezo = A7;
const byte ligth_on = 254;                               //idea to define different clors depending on the alarm only defined for waterAlarm

int readButton = 0;
bool Alarm = false;
//Timer variablen für Zeitmessung
unsigned long readSensor_timer_start;
unsigned long readLigth_timer_delta = 3000;               //to test it is set to 3s, need to change to z-mins?
unsigned long readHygro_timer_delta = 6000;               //to test it is set to 6s, need to change to y-mins?
unsigned long readAir_condition_timer_delta = 9000;       //to test it is set to 9s, need to change to x-mins?
unsigned long Alarm_timer_start;
unsigned long timer_current;
unsigned long Alarm_time_delta = 1000;
unsigned long dev_variable_alarm_ligth_length = 15000;
unsigned long dev_variable_alarm_ligth_start = 0;
byte alarm_ligth_state = 0;
byte read_sensor_state = 0;
byte alarm_ligth_type = 0;                               //Tihs attribute defines the alarm type e.g. 0:water_alarm, 1:airQuality_alarm and 2:photonIntense_alarm
void setup() {
  Serial.begin(9600); //dev, to print out some information on the serial monitor
  pinMode(green, OUTPUT);
  pinMode(blue, OUTPUT);
  pinMode(red, OUTPUT);
  pinMode(button_state, INPUT);
  pinMode(hygro_sensor, INPUT);
  pinMode(piezo, OUTPUT);
  pinMode(ligth_sensor, INPUT);
}


void loop(){
  readButton = digitalRead(button_state);
  timer_current = millis();

  //dev to test the sequense by button push afterwards we can use for peering function
  if(readButton == HIGH){
    Alarm = true;
    Alarm_timer_start = millis();
    dev_variable_alarm_ligth_start = millis();
  }


  //Is called to read sensor data
  if( ((timer_current - readSensor_timer_start) >= readLigth_timer_delta && read_sensor_state == 0) ||
      ((timer_current - readSensor_timer_start) >= readHygro_timer_delta && read_sensor_state == 1) ||
      ((timer_current - readSensor_timer_start) >= readAir_condition_timer_delta && read_sensor_state == 2)){

    switch(read_sensor_state){
    case 0: readLigth_sensor(); read_sensor_state++; break;             //fist sensor to read
    case 1: readHygro_sensor(); read_sensor_state++; break;        //second sensor to read
    case 2: readAir_condition_sensor(); read_sensor_state = 0; readSensor_timer_start = millis(); break;   //last sensor to read, set state to 0 and readSensor_timer_start to current time
    }
  }
  //Is called if an alarm signal is dedected
  if(Alarm && (timer_current - Alarm_timer_start) >= Alarm_time_delta){

    switch(alarm_ligth_type){
    case 0: sequence_water_alarm(); break;
    case 1: sequence_airQuality_alarm(); break;
    case 2: sequence_photonIntense_alarm(); break;
  }
    Alarm_timer_start = millis();

    //dev if to switch off alarmligth
    if((timer_current - dev_variable_alarm_ligth_start) >= dev_variable_alarm_ligth_length){
      Alarm = false;
      lightOff();
    }
  }

}

void readLigth_sensor(){
  byte ligth_state = analogRead(ligth_sensor);
  //TODO calculate ligth value and write to BLE_desk
  //dev
  lightOff();
  lightOn(0, 0, ligth_on);
}
void readHygro_sensor(){
  byte hygro_state = analogRead(hygro_sensor);
  //TODO calculate hygro value and write to BLE_desk
  //dev
  lightOff();
  lightOn(0, ligth_on, 0);
}
void readAir_condition_sensor(){
  //TODO Aircondition sensor code

  //dev
  lightOff();
  lightOn(ligth_on, 0, 0);
}
void sequence_water_alarm(){
  switch(alarm_ligth_state){
    case 0: lightOn(ligth_on, 0, 0); alarm_ligth_state++; break;
    case 1: lightOn(0, ligth_on, 0); alarm_ligth_state++; break;
    case 2: lightOn(0, 0, ligth_on); alarm_ligth_state = 0; break;
  }
}

void sequence_airQuality_alarm(){
  switch(alarm_ligth_state){
    case 1: lightOn(0, ligth_on, 0); alarm_ligth_state++; break;
    case 0: lightOn(ligth_on, 0, 0); alarm_ligth_state++; break;
    case 2: lightOn(0, 0, ligth_on); alarm_ligth_state = 0; break;
  }
}
void sequence_photonIntense_alarm(){
  switch(alarm_ligth_state){
    case 2: lightOn(0, 0, ligth_on); alarm_ligth_state++; break;
    case 0: lightOn(ligth_on, 0, 0); alarm_ligth_state++; break;
    case 1: lightOn(0, ligth_on, 0); alarm_ligth_state = 0; break;
  }
}
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
