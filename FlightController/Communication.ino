#define HC12 Serial1
const char startString[] = "START";
const char numbers[] = "0123456789";
const char hex[] = "0123456789ABCDEF";
byte safetyCounter = 0;
int data[13];
uint32_t lastReqMillis = 0;
long lastMillisCut = 0;
struct ControllerData {
  int rotation;
  int sideMovement;
  int forwardMovement;
  int power;
};
ControllerData actualData = {200, 200, 200, 200};


void initCommunication() {
  HC12.begin(115200);
  //Serial.println(1);

  HC12.print("READY");
  //Serial.println(2);
  delay(20);
}
void requestForData() {
  if (millis() - lastReqMillis >= 20) {
    lastReqMillis = millis();
    HC12.print("R");
    parseData();
    HC12.print("P");
    sendDataToController();
  }


}
void parseData() {
  if (HC12.available() == 0) {
    safetyCounter++;
    if (safetyCounter == 10) safeLanding();
  }
  else safetyCounter = 0;
  for (int i = 0; HC12.available() != 0; i++) {
    data[i] = parseIntFromChar( HC12.read());
    //Serial.println(data[i]);
  }
  actualData.rotation = getParsedNumber(data[0], data[1], data[2]);
  actualData.sideMovement =  getParsedNumber(data[3], data[4], data[5]);
  actualData.forwardMovement = getParsedNumber(data[6], data[7], data[8]);
  actualData.power = getParsedNumber(data[9], data[10], data[11]);

}
void sendDataToController() {



  sendInHex(floatToLong(pitch));
  sendInHex(floatToLong(roll));
  sendInHex(floatToLong(yaw));
  sendBatteryInHex(getBattery());
  //HC12.print("4A8");

}


void sendBatteryInHex(float battery){

  battery *= 100;
  int voltage = battery;
  while(voltage!=0){
     HC12.print(hex[voltage%16]);
     voltage /= 16;
  }
}
long floatToLong(float f) {
  return (long)(f * 1000 + 300000);
}
void sendInHex(long number) {
  while (number != 0) {
    HC12.print(hex[number % 16]);
    number /= 16;
  }

}

void safeLanding() {
  if (millis() - lastMillisCut >= 100) {
    lastMillisCut = millis();
    actualData.rotation = 0;
    actualData.sideMovement = 0;
    actualData.forwardMovement = 0;
    if (getPower() >= 10) {
      actualData.power = actualData.power - 2;
    } else {
      actualData.power = 0;
      initCommunication();
    }
  }
}



int getParsedNumber(int first, int sec, int third) {

  return first * 100 + sec * 10 + third - 200;
}

int parseIntFromChar(char number) {
  for (int i = 0; i < 10; i++)if (number == numbers[i])return i;
}

//gettery -> nobody cares
int getRotation() {
  return actualData.rotation;
}
int getSideMovement() {
  return actualData.sideMovement;
}
int getForwardMovement() {
  return actualData.forwardMovement;
}
int getPower() {
  return actualData.power;
}
