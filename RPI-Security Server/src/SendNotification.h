#ifndef SEND_NOTIF_H
#define SEND_NOTIF_H

#include <iostream>

using namespace std;

class SendNotification
{
	private:
		char deviceToken[100];
		char bufferJson[1024];

	public:
		SendNotification();
		~SendNotification();
		void vdSendNotification(char* dateTimeIntrusion);
};

#endif