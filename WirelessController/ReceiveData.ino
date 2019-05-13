long xAngle = 0;
long yAngle = 0;
long zAngle = 0;
long batteryVoltage = 0;
const char charNumbers[] = "0123456789ABCDEF";

void readDataFromDrone() {
  int readedData[18];
  for (int i = 0; Serial1.available() != 0; i++) {
    char c = (char)Serial1.read();
    readedData[i] = charToInt(c);
  }

  xAngle = hexToDec(&readedData[0], 1);
  yAngle = hexToDec(&readedData[5], 1);
  zAngle = hexToDec(&readedData[10], 1);
  batteryVoltage = hexToDec(&readedData[15], 0);
}

long hexToDec(int *pointer, bool angle) {
  long number ;
  if (angle) number = -300000;
  else number = -1000;


  int maxI;
  if (angle) maxI = 5;
  else maxI = 3;
  for (int i = 0; i < maxI; i++) {
    number += *pointer * pow(16, i);
    pointer++;
  }
  return number;
}
int charToInt(char number) {
  for (int i = 0; i < 16; i++) {
    if (number == charNumbers[i]) return i;
  }
  return -1;
}
