# RPI-Security
RPI-Security est un système de caméra embarquée détectrice d'intrusions, tournant sur Raspberry Pi et contrôlable à distance via une application Android.

#
# Fonctionalités 
- Visualisation du flux vidéo de la caméra sur serveur TCP, depuis une application android ou un naviguateur web
- Rotation de la caméra par servomoteurs à distance depuis l'application
- Détection d'intrusion via algorithme de détection de mouvements par flux optique
- Envoi de notifications pour informer d'une intrusion, sur le device android distant via Google Firebase
- Prise d'une vidéo lors de la détection d'une intrusion, et envoi de celle-ci par mail sur la boîte mail de l'utilisateur
- Fonctionalités diverses depuis l'application ...

# Framework et librairies
- OpenCV
- Poco (envoi de mails)
- CURL (communication avec Google Firebase)
- WiringPi (Gestion des entrées/sorties de la Raspberry)
