  /*
 * Fichier EventCmdAndroid.cpp
 * Date : 04/06/2019
 * Auteurs : Nehari Mohamed et Yassine Tabet
 * Classe permettant la gestion des fonctionalités de la caméra en fonction des commandes envoyées depuis l'application android
 */
 

#include "EventCmdAndroid.h"
#include "Window.h"

// Constructeur initialisant les fonctionnalités du système 
EventCmdAndroid::EventCmdAndroid()
{
	this->servoMoteurVertical = new ServoMoteur(PIN_SERVO_VERTICAL);
	this->servoMoteurHorizontal = new ServoMoteur(PIN_SERVO_HORIZONTAL);
	this->intrusionDetection = new IntrusionDetection();
}

EventCmdAndroid::~EventCmdAndroid()
{
	delete this->servoMoteurHorizontal;
	delete this->servoMoteurVertical;
	delete this->intrusionDetection;
}

/*  Fonction permettant le lancement d'une fonctionnalité du système en fonction du numéro de commande passé en paramètre, et correspondant a un numéro envoyé depuis 
	l'application android lors de l'appui d'une touche pour l'application d'une fonction donnée
*/
void EventCmdAndroid::vdInterpreterCmdAndroid(int cmd)
{
	switch(cmd)
	{
		case ROTATION_CAM_RIGHT :
			 this->servoMoteurHorizontal->vdRotation(DIRECTION_RIGHT, true); 
			break;
		case ROTATION_CAM_LEFT :
			 this->servoMoteurHorizontal->vdRotation(DIRECTION_LEFT, true);
			break;
		case ROTATION_CAM_UP :
			 this->servoMoteurVertical->vdRotation(DIRECTION_RIGHT, false);
			break;
		case ROTATION_CAM_DOWN :
			this->servoMoteurVertical->vdRotation(DIRECTION_LEFT, false);
			break;
		case ACTIVATE_ALARM :
			alarmBuzzer->vdSetThreadStatus(true);
			alarmBuzzer->vdSetAlarmMode(ALARM_INTRUSION);
			break;
		case DESACTIVATE_ALARM :
			alarmBuzzer->vdSetThreadStatus(false);
			break;
		case ACTIVATE_DETECTION:
			alarmBuzzer->vdSetThreadStatus(true);
			alarmBuzzer->vdSetAlarmMode(BIP_BEGIN_DETECTION);
			this->intrusionDetection->vdSetThreadStatus(true);
			break;
		case DESACTIVATE_DETECTION:
			alarmBuzzer->vdSetThreadStatus(true);
			alarmBuzzer->vdSetAlarmMode(BIP_END_DETECTION);
			this->intrusionDetection->vdSetThreadStatus(false);
			this->intrusionDetection->vdReset();
		default:
			break;
	}
}
