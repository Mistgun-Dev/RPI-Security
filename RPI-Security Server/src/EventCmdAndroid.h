#ifndef EVENT_ANDROID_H
#define EVENT_ANDROID_H

#include "ServoMoteur.h"
#include "IntrusionDetection.h"
#include "Alarm.h"

#define ROTATION_CAM_UP 		1
#define	ROTATION_CAM_DOWN  	    2
#define ROTATION_CAM_RIGHT  	3
#define ROTATION_CAM_LEFT 	 	4
#define ACTIVATE_ALARM		 	5
#define DESACTIVATE_ALARM	 	6
#define ACTIVATE_DETECTION	 	7
#define DESACTIVATE_DETECTION	8

using namespace std;

class EventCmdAndroid
{
	public:

		ServoMoteur *servoMoteurVertical, *servoMoteurHorizontal;
		
		IntrusionDetection *intrusionDetection;

		EventCmdAndroid();
		~EventCmdAndroid();
		void vdInterpreterCmdAndroid(int cmd);
};

#endif