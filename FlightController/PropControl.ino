#include <Servo.h>

Servo S1 , S2 , S3 , S4;
int FR = 1000;
int FL = 1000;
int RR = 1000;
int RL = 1000;

int throtle = 1000;

void propInit() {
  S1.attach(PB9); // Front - Right
  S2.attach(PB8); // Front - Left
  S3.attach(PB7); // Rear - Right
  S4.attach(PB6); // Rear - Left

  S1.writeMicroseconds(FR);
  S2.writeMicroseconds(FL);
  S3.writeMicroseconds(RR);
  S4.writeMicroseconds(RL);
  delay(5000);
}

void setThrotle(int power) {
  throtle = (power * 10) + 1000;
}

void propControl() {
  //Serial.println(throtle);
  if (throtle != 1000) {
    FR = throtle - pitchPID + rollPID - yawPID;
    FL = throtle - pitchPID - rollPID + yawPID;
    RR = throtle + pitchPID + rollPID + yawPID;
    RL = throtle + pitchPID - rollPID - yawPID;

    //omezeni rychlosti -- 2000 max 1000min
    if (FR > 2000) {
      FR = 2000;
    }
    if (FL > 2000) {
      FL = 2000;
    }
    if (RR > 2000) {
      RR = 2000;
    }
    if (RL > 2000) {
      RL = 2000;
    }

    if (FR < 1030) {
      FR = 1030;
    }
    if (FL < 1030) {
      FL = 1030;
    }
    if (RR < 1030) {
      RR = 1030;
    }
    if (RL < 1030) {
      RL = 1030;
    }
    /*Serial.print("FR ");
      Serial.print(FR);
      Serial.print("FL ");
      Serial.print(FL);
      Serial.print("RR ");
      Serial.print(RR);
      Serial.print("RL ");
      Serial.println(RL);*/

    S1.writeMicroseconds(FR);
    S2.writeMicroseconds(FL);
    S3.writeMicroseconds(RR);
    S4.writeMicroseconds(RL);
  } else {
    S1.writeMicroseconds(1000);
    S2.writeMicroseconds(1000);
    S3.writeMicroseconds(1000);
    S4.writeMicroseconds(1000);

  }
}
