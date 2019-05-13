#include <Wire.h>
#include <I2Cdev.h>
#include <MPU6050_6Axis_MotionApps20.h>

TwoWire WIRE2(2, I2C_FAST_MODE);
#define Wire WIRE2

MPU6050 mpu;

bool dmpReady = false;
uint8_t devStatus;
uint8_t mpuIntStatus;
uint16_t packetSize;
uint16_t fifoCount;
uint8_t fifoBuffer[64];

Quaternion q;
VectorFloat gravity;
float ypr[3];

float pitch = 0;
float roll = 0;
float yaw = 0;

void mpuInit() {
  Wire.begin();
  Wire.setClock(400000);
  mpu.initialize();
  devStatus = mpu.dmpInitialize();
  mpu.setXGyroOffset(-12);
  mpu.setYGyroOffset(-16);
  mpu.setZGyroOffset(16);
  mpu.setXAccelOffset(14);
  mpu.setYAccelOffset(-2104);
  mpu.setZAccelOffset(556);
  if (devStatus == 0 ) {
    mpu.setDMPEnabled(true);
    mpuIntStatus = mpu.getIntStatus();
    dmpReady = true;

    packetSize = mpu.dmpGetFIFOPacketSize();
  } else {
    /* while(true){

      }*/
  }


}
void resetFif() {
  mpu.resetFIFO();
}
void mpuGetData() {
  while (false && fifoCount < packetSize) {

  }
  mpuIntStatus = mpu.getIntStatus();
  fifoCount = mpu.getFIFOCount();

  if ((mpuIntStatus & 0x10) || fifoCount == 1024) {
    mpu.resetFIFO();
  } else if (mpuIntStatus & 0x02) {
    while (fifoCount < packetSize) fifoCount = mpu.getFIFOCount();
    mpu.getFIFOBytes(fifoBuffer, packetSize);
    fifoCount -= packetSize;

    mpu.dmpGetQuaternion(&q, fifoBuffer);
    mpu.dmpGetGravity(&gravity, &q);
    mpu.dmpGetYawPitchRoll(ypr, &q, &gravity);
  }
  pitch = (float)((ypr[1] * 180) / M_PI);
  roll = (float)((ypr[2] * 180) / M_PI);
  yaw = (float)((ypr[0] * 180) / M_PI);
  /*Serial.print(pitch);
    Serial.print(roll);
    Serial.println(yaw);*/



}
