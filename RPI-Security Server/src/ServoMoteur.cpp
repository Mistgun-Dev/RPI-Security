  /*
 * Fichier ServoMoteur.cpp
 * Date : 04/06/2019
 * Auteurs : Nehari Mohamed et Yassine Tabet
 * Classe permettant la gestion des servoMoteurs.
 */

#include "ServoMoteur.h"
#include <iostream>
#include <wiringPi.h>

// Constructeur initialisant les entrées des servomoteurs 
ServoMoteur::ServoMoteur(unsigned char u8PinServo)
{
	if (wiringPiSetupGpio () == -1)
		return;

	this->u8PinServo = u8PinServo;
	pinMode(this->u8PinServo, PWM_OUTPUT);
	this->u8Speed = 3;
	this->u8PositionCourante = ROT_MIDDLE;
}

ServoMoteur::~ServoMoteur()
{

}

// Fonction placant le sermomoteur à une position de démarrage
void ServoMoteur::vdPositionDemarrage()
{
	this->u8PositionCourante = ROT_MIDDLE;

	pwmSetMode(PWM_MODE_MS);
	pwmSetClock(375);
	pwmSetRange(1024);

	pwmWrite(this->u8PinServo, this->u8PositionCourante);
}

// Fonction permettant la rotation du servomoteur à droite ou à gauche en fonction de la direction passée en paramètre
void ServoMoteur::vdRotation(char direction, bool servoHorizontal)
{	
	pwmSetMode(PWM_MODE_MS); 
	pwmSetClock(375); 
	pwmSetRange(1024);

	if(direction == DIRECTION_RIGHT)
	{
		if(servoHorizontal == true)
			this->u8PositionCourante = 5;
		else if((this->u8PositionCourante - u8Speed) <= ROT_MIN+3)
			this->u8PositionCourante = ROT_MIN+3;
		else
			this->u8PositionCourante -= u8Speed;
	}
	else if(direction == DIRECTION_LEFT)
	{
		if(servoHorizontal == true)
			this->u8PositionCourante = -5;
		else if((this->u8PositionCourante + u8Speed) >= ROT_MAX-3)
			this->u8PositionCourante = ROT_MAX-3;
		else
			this->u8PositionCourante += u8Speed;
	}
	else
		return;

	if(servoHorizontal == true)
	{
		pwmWrite(this->u8PinServo, this->u8PositionCourante);
		delay(100);
		pwmWrite(this->u8PinServo,0);
	}
	else
		pwmWrite(this->u8PinServo, this->u8PositionCourante);
}

// Fonction permettant la rotation du servomoteur à un angle définit
void ServoMoteur::vdSetRotation(char rot)
{
	pwmSetMode(PWM_MODE_MS); 
	pwmSetClock(375); 
	pwmSetRange(1024);
	
	pwmWrite(this->u8PinServo , rot);
}





