int reqCounter = 0;
uint32_t lastsendMillis = 0;
char readyForFlight[] = "READY";

void sendDataToDrone() {
  bool isItReq = Serial1.available() != 0;
  if (!isItReq) return;


  for (int i = 0; Serial1.available() != 0; i++) {
    char c = Serial1.read();
    if (c == 'R') {
      isItReq = true;
      break;
    } else if (c == 'P') {
      isItReq = false;
      delay(1);
      readDataFromDrone();
      sendDataToPhone();
      break;
    }
  }
  if (isItReq) {
    Serial1.print(actualLeverValues.rotateLever);
    Serial1.print(actualLeverValues.sideLever);
    Serial1.print(actualLeverValues.forwardLever);
    Serial1.print(actualLeverValues.powerLever);
    digitalWrite(blinkLedPin, 1);
    lastsendMillis = millis();
  }
}
void checkRequestedData() {
  while (Serial1.available() != 0) {
    Serial2.print((char)Serial1.read());
  }
}

bool initCommunication() {
  bool rdy = true;
  while (Serial1.available() == 0)blink();
  delay(10);
  for (int i = 0; i < 5; i++) {
    char c = Serial1.read();
    if (c != readyForFlight[i])rdy = false;
    Serial2.print(c);
  }
  Serial2.print(rdy);
  if (rdy) digitalWrite(blinkLedPin, 1);
  return rdy;
}
void sendDataToPhone() {
  Serial2.print("x = ");
  Serial2.println(xAngle);
  Serial2.print("y = ");
  Serial2.println(yAngle);
  Serial2.print("z = ");
  Serial2.println(zAngle);
  Serial2.print("v = ");
  Serial2.println(batteryVoltage);
}
