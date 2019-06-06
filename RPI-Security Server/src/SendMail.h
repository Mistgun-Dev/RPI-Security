#ifndef SEND_MAIL_H
#define SEND_MAIL_H

using namespace std;

class SendMail
{
	private:

	public:
		SendMail();
		~SendMail();
		void vdInit();
		void vdSendMail(string pathVideo, char *dateTimeIntrusion);
};

#endif