/// PID GAINS
float gainP = 2.5;
float gainD = 550.0;
float gainIpitch = 0.04;
float gainIroll = 0.04;

float gainPyaw = 1.0;
float gainIyaw = 0.0;
float gainDyaw = 0.0;


/// other PID variables
long lastTime = 0;
float elapsedTime = 0.0;
float maxPID = 400;
float desiredAngleRoll = 0.0;
float desiredAnglePitch = 0.0;
float desiredAngleYaw = 0.0;
float pitchSum = 0.0;
float rollSum = 0.0;


///  Pitch
float pitchP = 0.0;
float pitchI = 0.0;
float pitchD = 0.0;
float pitchLastE = 0.0;
float pitchPID = 0.0;


/// Roll
float rollP = 0.0;
float rollI = 0.0;
float rollD = 0.0;
float rollLastE = 0.0;
float rollPID = 0.0;


/// Yaw
float yawP = 0.0;
float yawI = 0.0;
float yawD = 0.0;
float yawLastE = 0.0;
float yawPID = 0.0;

bool first = true;

void setDesiredAngles(int side, int forward, int rotation) {
  desiredAngleRoll = ((float)side) / 5.0;
  desiredAnglePitch = ((float)forward) / 10.0;
  desiredAngleYaw += (float)rotation / 200.0;
  if (desiredAngleYaw >= 360) {
    desiredAngleYaw -= 360;
  } else if (desiredAngleYaw <= -360) {
    desiredAngleYaw += 360;
  }
}

void calculatePid() {

  // vypocet aktualni odchylky od pozadovaneho nakloneni
  float rollError = roll - desiredAngleRoll;
  float pitchError = pitch - desiredAnglePitch;
  if (first) {
    desiredAngleYaw = yaw;
    first = false;
  }
  float yawError = yaw - desiredAngleYaw;

  pitchSum += pitchError;
  rollSum += rollError;
  // vypocet Proporcionalnich hodnot pro dane nakloneni
  pitchP = pitchError * gainP;
  rollP = rollError * gainP;
  yawP = yawError * gainPyaw;

  // vypocet integralnich hodnot pro dane nakloneni
  //pitchI = gainIpitch * pitchSum;
  //

  //rollI = gainIroll * rollSum;

  // vypocet ubehleho casu od posledniho mereni
  elapsedTime = ( micros() - lastTime);
  lastTime = micros();

  elapsedTime /= (float) 1000.0;
  /*Serial.println(elapsedTime,5);
    Serial.println(pitchError);
    Serial.println(pitchLastE);*/
  //Serial.println(1100*((pitchError - pitchLastE) /elapsedTime),8);
  // vypocet derivacnich hodnot pro dane nakloneni
  pitchD = gainD * ((pitchError - pitchLastE) / elapsedTime);
  rollD = gainD * ((rollError - rollLastE) / elapsedTime );
  yawD = gainDyaw * ((yawError - yawLastE) / elapsedTime);

  pitchPID = pitchP + pitchI + pitchD;
  rollPID = rollP + rollI + rollD;
  yawPID = yawP + yawI + yawD;
  /* Serial.println(rollPID);
    Serial.println(pitchPID);*/
  // omezeni PID controleru
  if (pitchPID > maxPID) {
    pitchPID = maxPID;
  } else if (pitchPID < -maxPID) {
    pitchPID = -maxPID;
  }

  if (rollPID > maxPID) {
    rollPID = maxPID;
  } else  if (rollPID < -maxPID) {
    rollPID = -maxPID;
  }
  if (yawPID > maxPID) {
    yawPID = maxPID;
  } else  if (yawPID < -maxPID) {
    yawPID = -maxPID;
  }
  //Serial.println(rollPID);
  //Serial.println(pitchPID);
  // ukladani posledni odchylky
  pitchLastE = pitchError;
  rollLastE = rollError;
  yawLastE = yawError;
}
