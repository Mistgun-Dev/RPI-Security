#ifndef SERVOMOTEUR_H
#define SERVOMOTEUR_H

using namespace std;

#define PIN_SERVO_VERTICAL		18
#define PIN_SERVO_HORIZONTAL	13
#define ROT_MIN					28
#define ROT_MAX					64
#define ROT_MIDDLE				50
#define DIRECTION_RIGHT			1
#define DIRECTION_LEFT			2

class ServoMoteur
{
	public:
		unsigned char u8PinServo;				//Pin de connexion à la Raspberry
		unsigned char u8PositionCourante;		//Position courante du Servomoteur
		unsigned char u8Speed;					//Vitesse de rotation du servoMoteur

		ServoMoteur(unsigned char u8PinServo);	
		~ServoMoteur();
		void vdPositionDemarrage();
		void vdRotation(char direction,  bool servoHorizontal);
		void vdSetRotation(char rot);
};

#endif
