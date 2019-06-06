  /*
 * Fichier Alarm.cpp
 * Date : 04/06/2019
 * Auteurs : Nehari Mohamed et Yassine Tabet
 * Classe permettant la gestion du Buzzer pour l'alarme
 */
 
#include "Alarm.h"
#include <iostream>
#include <wiringPi.h>
#include <softTone.h>
#include <unistd.h>

// Constructeur initialisant le composant buzzer, et lançant le thread gérant l'alarme 
Alarm::Alarm(unsigned char u8PinBuzzer)
{
	if (wiringPiSetupGpio () == -1)
		return;

	this->u8PinBuzzer = u8PinBuzzer;
	pinMode(this->u8PinBuzzer, OUTPUT);
	runAlarm = false;

	this->threadBuzzer = thread(&Alarm::vdRunAlarm, this);
}

Alarm::~Alarm()
{
	threadBuzzer.join();
}

// Fonction activant ou désactivant le thread de lancement de l'alarme
void Alarm::vdSetThreadStatus(bool status)
{
	runAlarm = status;
	this->alarmMode = -1;
}

//FOnction petmettant de définir le type d'alarme à utilisé (bip rapide, bip lent ...)
void Alarm::vdSetAlarmMode(char mode)
{
	this->alarmMode = mode;
}

//Fonction de gestion de l'alarme permettant l'activation du buzzer
void Alarm::vdRunAlarm()
{
	while(1)
	{
		if(runAlarm == true)
		{
			switch(this->alarmMode)
			{
				case BIP_BEGIN_DETECTION:
					vdBipDetectionEnable();
					break;
				case BIP_END_DETECTION:
					vdBipDetectionDisable();
					break;
				case ALARM_INTRUSION:
					digitalWrite(this->u8PinBuzzer, HIGH);
					delay(500);
					digitalWrite(this->u8PinBuzzer, LOW);
					delay(50);
					break;
				default:
					usleep(10000);
					break;
			}
		}
		else
			usleep(10000);
	}	
}

// Fonction jouant 3 bip lors de l'activation du mode de détection d'intrusion
void Alarm::vdBipDetectionEnable()
{
	digitalWrite(this->u8PinBuzzer, HIGH);
	delay(50);
	digitalWrite(this->u8PinBuzzer, LOW);
	delay(50);

	digitalWrite(this->u8PinBuzzer, HIGH);
	delay(50);
	digitalWrite(this->u8PinBuzzer, LOW);
	delay(50);

	digitalWrite(this->u8PinBuzzer, HIGH);
	delay(1500);
	digitalWrite(this->u8PinBuzzer, LOW);

	this->runAlarm = false;
}

// Fonction jouant 3 bip lors de la désactivation du mode de détection d'intrusion
void Alarm::vdBipDetectionDisable()
{
	digitalWrite(this->u8PinBuzzer, HIGH);
	delay(50);
	digitalWrite(this->u8PinBuzzer, LOW);
	delay(50);

	digitalWrite(this->u8PinBuzzer, HIGH);
	delay(50);
	digitalWrite(this->u8PinBuzzer, LOW);
	delay(50);

	digitalWrite(this->u8PinBuzzer, HIGH);
	delay(1500);
	digitalWrite(this->u8PinBuzzer, LOW);

	this->runAlarm = false;
}

