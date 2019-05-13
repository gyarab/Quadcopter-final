#define rotateLeverPin PB1//min 213 max 3912 mid 1800 - 1900
#define sideLeverPin PB0//min 268 max 3775 mid 1700 - 1800
#define forwardLeverPin PA7//min 236 max 3756 1703 - 1803 
#define powerLeverPin PA6//min 255 max 3747 1694 - 1794
#define blinkLedPin PA0
#define blinkLedGroundPin PC15
uint32_t lastMillis = millis();
bool blinkActive = false;
int counterRequest = 0;
struct Lever {
  uint16_t middleValue;
  uint16_t minValue;
  uint16_t maxValue;
};
struct LeverValues {
  int rotateLever;
  int sideLever;
  int forwardLever;
  int powerLever;
};
Lever rotateLever = {2120, 250, 3912};
Lever sideLever = {2100, 394, 3888};
Lever forwardLever = {2032, 236, 3756};
Lever powerLever = {0, 240, 3727};
LeverValues actualLeverValues = {200, 200, 200, 200};
bool firstFlight = true;
void setup() {
  pinMode(rotateLeverPin, INPUT);
  pinMode(sideLeverPin, INPUT);
  pinMode(forwardLeverPin, INPUT);
  pinMode(powerLeverPin, INPUT);
  pinMode(blinkLedPin, OUTPUT);
  pinMode(blinkLedGroundPin, OUTPUT);
  digitalWrite(blinkLedGroundPin, LOW);
  Serial2.begin(115200);
  Serial1.begin(115200);
  initCommunication();
}
void loop() {
  if(firstFlight){
    calculatePercentage(powerLever, analogRead(powerLeverPin), 3);
    if(actualLeverValues.powerLever<=205){
      firstFlight = false;
      actualLeverValues.powerLever = 200;
    }
  }else{
  calculatePercentage(rotateLever, analogRead(rotateLeverPin), 0);
  calculatePercentage(sideLever, analogRead(sideLeverPin), 1);
  calculatePercentage(forwardLever, analogRead(forwardLeverPin), 2 );
  calculatePercentage(powerLever, analogRead(powerLeverPin), 3);
  }
  sendDataToDrone();
  delay(1);
}
void blink() {
  if (millis() - lastMillis >= 500) {
    blinkActive = !blinkActive;
    digitalWrite(blinkLedPin, blinkActive);
    lastMillis = millis();
  }
}
