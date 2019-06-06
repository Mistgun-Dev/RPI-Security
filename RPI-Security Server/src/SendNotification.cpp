 /*
 * Fichier SendNotification.cpp
 * Date : 04/06/2019
 * Auteurs : Nehari Mohamed et Yassine Tabet
 * Classe permettant l'envoi d'une notification par le serveur de Google Firebase
 */


#include <curl/curl.h>
#include "SendNotification.h"
#include <cstdlib>
#include <string.h>

SendNotification::SendNotification()
{
}

SendNotification::~SendNotification()
{}

/* Fonction permettant l'envoi d'une notification via CURL en passant par le service Google Firebase, ver un device android identifiable par une clé d'identification unique
   générée par Firebase */
void SendNotification::vdSendNotification(char* dateTimeIntrusion)
{
	char as8BufferJSON[1024];
	CURL* curl = curl_easy_init();
	struct curl_slist* headers = NULL;
	
	memset(as8BufferJSON, 0, sizeof as8BufferJSON);
	sprintf(as8BufferJSON, "%s%s%s",
					"{\"to\": \"Remplacer par l'id fournie par Firebase sur ledevice cible\", \
					\"data\": 													\
						{														\
							\"title\": \"Intrusion Détectée !\",	\
							\"message\": \"Intrusion detéctéé : \",			\
							\"img_url\": \"https://img.purch.com/w/660/aHR0cDovL3d3dy5saXZlc2NpZW5jZS5jb20vaW1hZ2VzL2kvMDAwLzA5OS8zMjkvb3JpZ2luYWwvY2hpbXBzLWVhdC1tb25rZXktYnJhaW5zLTAx\", \
							\"date\": \"", dateTimeIntrusion, "\" 					\
						}														\
					}" );

    if(!curl) {
        cerr << "Erreur lors de l'initialisation de CURL" << endl;
        return;
    }

    curl_easy_setopt(curl, CURLOPT_CUSTOMREQUEST, "POST");
    curl_easy_setopt(curl, CURLOPT_URL, "https://fcm.googleapis.com/fcm/send");
	headers = curl_slist_append(headers, "Content-Type: application/json");
	headers = curl_slist_append(headers, "Authorization : key=Remplacer par la key API du serveur Google Firebase");
	curl_easy_setopt(curl, CURLOPT_POSTFIELDS, as8BufferJSON);

	curl_easy_setopt(curl, CURLOPT_HTTPHEADER, headers);
	curl_easy_perform(curl);
}