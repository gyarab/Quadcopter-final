float napeti = 0;

void setup() {
  // put your setup code here, to run once:
  Serial.begin(115200);
  //pinMode(PB12, INPUT);
  Serial.println("Prop init...");
  propInit();
  pinMode(PA15, OUTPUT);
  digitalWrite(PA15, HIGH);
  Serial.println("Com init...");
  initCommunication();
  pinMode(PB13, OUTPUT);
  digitalWrite(PB13, HIGH);
  Serial.println("MPU init...");
  mpuInit();
  pinMode(PA11, OUTPUT);
  digitalWrite(PA11, HIGH);
  delay(2000);

  digitalWrite(PA15, LOW);
  digitalWrite(PB13, LOW);
  digitalWrite(PA11, LOW);

}

void loop() {
  // put your main code here, to run repeatedly:
  napeti = 0;
  napeti = analogRead(PB12);
  napeti -=1050;
  napeti/=3.7;
  requestForData();

  setThrotle(getPower());
  
  //Serial.print(napeti);
  /*Serial.print("   ");
  Serial.println(getRotation());*/
  setDesiredAngles(-getSideMovement(), getForwardMovement(), getRotation());

  mpuGetData();

  calculatePid();

  propControl();
  resetFif();
  
}

float getBattery(){
  return napeti;}
