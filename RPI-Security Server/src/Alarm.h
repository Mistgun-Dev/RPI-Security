#ifndef ALARM_H
#define ALARM_H
#include <thread>

#define PIN_BUZZER 	17
#define BIP_BEGIN_DETECTION 	0
#define BIP_END_DETECTION		1
#define ALARM_INTRUSION			2

using namespace std;

class Alarm
{
	public:
		unsigned char u8PinBuzzer;
		char alarmMode;	// Mode de l'alarme
		bool runAlarm;	//Alarme activée ou désactivée

		Alarm(unsigned char u8PinBuzzer);	
		~Alarm();
		void vdRunAlarm();
		void vdBipDetectionEnable();
		void vdBipDetectionDisable();
		void vdSetThreadStatus(bool status);
		void vdSetAlarmMode(char mode);

	private:
		thread threadBuzzer;
};

#endif
