#ifndef WINDOW_H
#define WINDOW_H

#include <opencv2/highgui/highgui.hpp>
#include <opencv2/imgproc/imgproc.hpp>
#include <mutex>
#include "Alarm.h"

extern VideoCapture cam;
extern mutex mutexCamera;
extern Alarm *alarmBuzzer;

#endif