 /*
 * Fichier SocketServer.cpp
 * Date : 04/06/2019
 * Auteurs : Nehari Mohamed et Yassine Tabet
 * Classe permettant la création d'un serveur TCP pour la gestion et l'écoute des commandes reçu de la part du client TCP depuis l'application android.
 */

#include <iostream>
#include <string>
#include "SocketServer.h" 
#include <string.h>

SocketServer::SocketServer(unsigned short port)
{
	this->portNumber = port;
	this->nbClientConnected = 0;
	this->gestionCommandAndroid = new EventCmdAndroid();
}

SocketServer::~SocketServer()
{
	pthread_join(threadServer, NULL);
}

// Fonction permettant la création et le lancement du serveur TCP pour lécoute des commandes envoyées par l'application android
void SocketServer::Init()
{
	int opt = 1; 

	//Creation description socket
	if ((serverSocket = socket(AF_INET, SOCK_STREAM, 0)) == 0) 
	{ 
		fprintf(stderr,"Socket échoué \n"); 
		exit(-1); 
	} 
	
	//Forcage pour réutilisation de l'addresse
	if (setsockopt(serverSocket, SOL_SOCKET, SO_REUSEADDR, &opt, sizeof(opt)))
	{ 
		fprintf(stderr,"Erreur setSockOpt \n"); 
		exit(1); 
	}

	address.sin_family = AF_INET; 
	address.sin_addr.s_addr = INADDR_ANY; 
	address.sin_port = htons(portNumber); 
	
	//Attachement de la socket au port 
	if (bind(serverSocket, (struct sockaddr *)&address, sizeof(address)) != 0) 
	{ 
		fprintf(stderr,"Bind échoué \n"); 
		exit(-1); 
	} 

	//Ecoute du serveur
	if (listen(serverSocket, NUMBER_CLIENT_MAX) < 0) 
	{ 
		fprintf(stderr,"Erreur listen \n"); 
		exit(-1); 
	} 
}

// Fonction d'écoute du serveur tournant sur un thread indépendant. 
void SocketServer::LoopServerCommand()
{
	int newClient = -1;

	for (;;)
    {
    	sockaddr_in from = { 0 };
		socklen_t addrlen = sizeof(address);
		if(nbClientConnected < NUMBER_CLIENT_MAX)
		{
			newClient = accept(serverSocket, (struct sockaddr *)(&from), (socklen_t*)&addrlen);
			if (newClient)
			{
				//std::cout << "\nNouvelle connexion sur le port : " << address.sin_port << std::endl;
				nbClientConnected++;
			}
			else
				break;
		}

		//RcvData n'est pas optimisé pour plusieurs clients mais juste pour un seul pour l"instant
		if(nbClientConnected > 0)
			ReceiveData(newClient);
	}
}

// Fonction de réception des données socket reçus
void SocketServer::ReceiveData(int socket)
{
	int buffer;

	int res = recv(socket, &buffer, 4, 0); //Récupération d'un seul octet sur les 4 envoyés car valeurs parfois fausse, soit 256 commande possible

	//Déconnexion du client
	if(res == 0)
	{	
		//fprintf(stderr, "Client déconnécté \n");
		nbClientConnected--;
	}
	//Data recue de la part du client
	else if(res)
	{
		gestionCommandAndroid->vdInterpreterCmdAndroid(buffer);
	}
}