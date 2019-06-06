#ifndef SOCKET_SERVER_H
#define SOCKET_SERVER_H

#include <sys/socket.h> 
#include <netinet/in.h> 
#include <pthread.h>
#include "EventCmdAndroid.h"

#define MAX_SIZE_BUFFER 	1024
#define NUMBER_CLIENT_MAX   1

using namespace std;

class SocketServer
{
	public:
		sockaddr_in address;
		int serverSocket;
		unsigned short portNumber;
		char nbClientConnected;
		pthread_t threadServer;

		EventCmdAndroid *gestionCommandAndroid;

		SocketServer(unsigned short port);
		~SocketServer();
		void Init();
		void LoopServerCommand();
		void ReceiveData(int socket);

		static void* LoopServer_Helper(void* context)
	    {
	        ((SocketServer *)context)->LoopServerCommand();
	        return NULL;
	    }

	    void Start()
		{
			pthread_create(&threadServer, NULL, this->LoopServer_Helper, this);
		}
};

#endif