  /*
 * Fichier SendMail.cpp
 * Date : 04/06/2019
 * Auteurs : Nehari Mohamed et Yassine Tabet
 * Classe permettant l'envoi d'un mail vers un compte utilisateur en y joignant une vidéo en pièce jointe
 */

#include <iostream>
#include "Poco/Net/MailMessage.h"
#include "Poco/Net/MailRecipient.h"
#include "Poco/Net/SMTPClientSession.h"
#include "Poco/Net/NetException.h"
#include "Poco/Net/SecureSMTPClientSession.h"
#include "Poco/Net/InvalidCertificateHandler.h"
#include "Poco/Net/AcceptCertificateHandler.h"
#include "Poco/Net/SSLManager.h"
#include "Poco/Net/SecureStreamSocket.h"
#include "Poco/Net/MailRecipient.h"
#include "Poco/Net/MailRecipient.h"
#include "Poco/Net/FilePartSource.h"
#include "Poco/Net/StringPartSource.h"
#include "stdlib.h"
#include "stdio.h"
#include "SendMail.h"
#include <time.h>

using Poco::Net::InvalidCertificateHandler;
using Poco::Net::AcceptCertificateHandler;
using Poco::Net::Context;
using Poco::Net::SSLManager;
using Poco::Net::SecureStreamSocket;
using Poco::Net::SocketAddress;
using Poco::Net::SecureSMTPClientSession;
using Poco::Net::SMTPClientSession;
using Poco::SharedPtr;
using Poco::Net::MailMessage;
using Poco::Net::MailRecipient;
using Poco::Net::FilePartSource;
using Poco::Net::StringPartSource;


SendMail::SendMail()
{}

SendMail::~SendMail()
{}

void SendMail::vdInit()
{}

// Fonction permettant l'envoi d'un mail vers une adresse cible, depuis un compte émetteur, en y joignant une vidéo en pièce jointe de l'intrusion prise par la caméra
void SendMail::vdSendMail(string pathVideo, char *dateTimeIntrusion)
{
  string host = "smtp.gmail.com";
  int port = 465;
  string sUserName = "adresseEmetteur@gmail.com";
  string sPassword = "mdpDuCompteEmetteur";
  string to = "adresseDestinataire@gmail.com";
  string from = "RPI Camera Security";
  string subject = "Intrusion Detected";
  MailMessage message;

  try {
    SharedPtr<InvalidCertificateHandler> pCert = new AcceptCertificateHandler(false);
    Context::Ptr pContext = new Poco::Net::Context(Context::CLIENT_USE, "", "", "", Context::VERIFY_NONE, 9, false, "ALL:!ADH:!LOW:!EXP:!MD5:@STRENGTH");
    SSLManager::instance().initializeClient(0, pCert, pContext);

    SecureStreamSocket pSSLSocket(pContext);
    pSSLSocket.connect(SocketAddress(host, port));
    SecureSMTPClientSession secure(pSSLSocket);

    secure.login();
    secure.startTLS(pContext);
    secure.login(SMTPClientSession::AUTH_LOGIN, sUserName, sPassword);

    message.setSender(from);
    message.addRecipient(MailRecipient(MailRecipient::PRIMARY_RECIPIENT, to));
    message.setSubject(subject);
    message.setContentType("text/plain; charset=UTF-8");

    //Construction du message à envoyer
    char messageContent[256];
    sprintf(messageContent, "An Intrusion has been detected on : %s", dateTimeIntrusion);
    message.addContent(new StringPartSource(messageContent));

    //Insertion de l'image dans le mail
    char* realPathVideo = realpath(pathVideo.c_str(), NULL);
    message.addAttachment("Detection Video", new FilePartSource(realPathVideo));

    secure.sendMessage(message);
    secure.close();
    
  } catch (Poco::Net::SMTPException &e) {
    cout << e.code() << endl;
    cout << e.message() << endl;
    cout << e.what() << endl;
    cout << e.displayText().c_str() << endl;
  }
  catch (Poco::Net::NetException &e) {
    cout << e.code() << endl;
    cout << e.message() << endl;
    cout << e.what() << endl;
    cout << e.displayText().c_str() << endl;
  }
}