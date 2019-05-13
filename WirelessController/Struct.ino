void calculatePercentage(struct Lever lever, uint16_t actualValue, int leverType) {
  double onePercent = 0;
  int percent = 0;
  if (leverType < 3) {
    if (actualValue > lever.middleValue + 50) {
      onePercent = (lever.maxValue - lever.middleValue - 50) / 100;
      percent = 100 - (lever.maxValue - actualValue) / onePercent;
      if (percent > 100) percent = 100;
    } else if (actualValue < lever.middleValue - 50) {
      onePercent = (lever.middleValue - 50 - lever.maxValue) / 100;
      percent = (lever.middleValue - actualValue) / onePercent;
      if (percent < -100) percent = -100;
    }
  } else {
    onePercent = (lever.maxValue - lever.minValue) / 100;
    percent = 100 - (lever.maxValue - actualValue) / onePercent;
    if (percent < 0)percent = 0;
  }
  switch (leverType) {
    case 0:
      actualLeverValues.rotateLever = percent+200;
      break;
    case 1:
      actualLeverValues.sideLever = percent*-1+200;
      break;
    case 2:
      actualLeverValues.forwardLever = percent*-1+200;
      break;
    case 3:
      actualLeverValues.powerLever = percent+200;
      break;
  }
}



