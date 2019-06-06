  /*
 * Fichier IntrusionDetection.cpp
 * Date : 04/06/2019
 * Auteurs : Nehari Mohamed et Yassine Tabet
 * Classe permettant la détection d'une intrusion par détection de mouvements via algorithme de flux optique
 */
 

#include "IntrusionDetection.h"
#include "Window.h"
#include "SendNotification.h"
#include "SendMail.h"
#include "Alarm.h"
#include <unistd.h>
#include <assert.h>
#include <sys/stat.h>
#include <sys/types.h>
#include <string>
#include <time.h>

//Constructeur permettant l'initialisation des données de détection, ainsi que l'initialisation du thread 
IntrusionDetection::IntrusionDetection()
{
    this->s32Sensibility = 90000;
    this->bRunThread = false;
    this->bIntrusionDetected = false;
    this->bFirstFrameDetection = true;
    this->threadDetection = thread(&IntrusionDetection::vdDetectIntrusion, this);

}

IntrusionDetection::~IntrusionDetection()
{}

//Fonction permettant l'activation ou la désactivation du thread
void IntrusionDetection::vdSetThreadStatus(bool status)
{
    this->bRunThread = status;
}

// Fonction permettant la réinitialisation du thread
void IntrusionDetection::vdReset()
{
    this->bIntrusionDetected = false;
    this->bFirstFrameDetection = true;
}

/*
     Fonction permetant la détection d'une intrusion paralgorithme de flux optique entre deux frames : N et N-1
     L'algorithme renvoie un integer permettant de quantifier la différence entre les frames N et N-1
     Si cet integer est supérieur au seuil de détection définit dans le constructeur, une intrusion détectée, et non dans le cas contraire.
     Une fois l'intrusion détectée, le serveur envoie une notification sur le smartphone androidde l'utilisateur afin de l'informé de la détection, et 
     une vidéo de celle-ci est enregistrée d'une durée paramétrable, et est envoyée ensuite vers un compte renseigné. 
*/
void IntrusionDetection::vdDetectIntrusion()
{
    static struct timespec startRecord, stopRecord;
    int diffFrame = 0;
    bool recordVideo = false;
    VideoWriter *video = NULL;
    Mat frame;
    string pathVideo;
    bool detectIntrusion = true;
    char dateTimeIntrusion[40];

    while(1)
    { 
        //Si la fonction de detection est activée
        if(this->bRunThread == true)
        {
            Mat cpy, flow, roi, motion2color;
            
            mutexCamera.lock();
            cam >> frame;
            mutexCamera.unlock(); 
            
            if (frame.empty())
                continue;

            resize(frame, cpy, Size(), 0.50, 0.50);
            cvtColor(cpy, roi, CV_BGR2GRAY);

            if(detectIntrusion == true)
            {
                if(this->bFirstFrameDetection == false)
                {
            	    if(this->previousFrame.data)
            	    {
            	        calcOpticalFlowFarneback(this->previousFrame, roi, flow, 0.5, 3, 15, 3, 5, 1.2, 0);
            	        
            	        for( int i=0; i < flow.rows; ++i)
            	        {
            		        Point2f* data = flow.ptr<Point2f>(i);
            	            for( int j = 0; j < flow.cols; ++j)
            	            {
            	                diffFrame += data[j].x * data[j].x + data[j].y * data[j].y;
            	            }
            	        }
            	        
            	        fprintf(stderr,"Différence frame N et N-1 = %d\n", diffFrame);

            	        if(diffFrame >= s32Sensibility && !this->bIntrusionDetected)
                        { 
                            fprintf(stderr, "\nINTRUSION DETECTEE !!!\n");
                            bIntrusionDetected = true;
                            alarmBuzzer->vdSetThreadStatus(true);
                            alarmBuzzer->vdSetAlarmMode(ALARM_INTRUSION);

                            //Récupérer la date et l'heure
                            struct tm * timeinfo;
                            time_t rawtime;
                            time (&rawtime);
                            timeinfo = localtime (&rawtime);
                            strftime(dateTimeIntrusion, sizeof(dateTimeIntrusion), "%A %d %B %Y %H:%M:%S", timeinfo);

                            //Envoi d'une notification au client Android l'informant de la date et de l'heure de l'intrusion
                            SendNotification *notification = new SendNotification();
                            notification->vdSendNotification(dateTimeIntrusion);

                            //Récupération du chemin de la vidéo;
                            string videoName = dateTimeIntrusion ;
                            videoName += ".avi";
                            pathVideo = "videoIntrusions/" + videoName; 

                            int frame_width = cam.get(CV_CAP_PROP_FRAME_WIDTH);
                            int frame_height = cam.get(CV_CAP_PROP_FRAME_HEIGHT);
                
                            video = new VideoWriter(pathVideo.c_str(),CV_FOURCC('M','J','P','G'),10, Size(frame_width,frame_height),true);
                            recordVideo = true;
                            detectIntrusion = false;
                            clock_gettime(CLOCK_REALTIME, &startRecord);

                            delete notification;
                        }
                        else
                            ;
            	    } 
            	}
                else
                {
                    this->bFirstFrameDetection = false;
                    continue;
                }

                roi.copyTo(this->previousFrame); 
                diffFrame = 0;
            }
        }

        if(recordVideo)
        {
            flip(frame, frame, -1);
            video->write(frame);

            //Si le temp d'arrivée - temp de depart >= delai alors on arrête d'enregistré
            clock_gettime( CLOCK_REALTIME, &stopRecord);
            float timeElapsed = stopRecord.tv_sec - startRecord.tv_sec;

            if(timeElapsed >= 7)
            {
                recordVideo = false;
                detectIntrusion = true;
                vdReset(); 
                fprintf(stderr, "enregistrement terminé \n");
        
                video->release();

                SendMail *mail = new SendMail();
                mail->vdSendMail(pathVideo, dateTimeIntrusion);

                delete mail;
                delete video;
            }
        }
        else
            usleep(50000);
    }
}


char* IntrusionDetection::vdSavePicture(Mat frame, char *dateTimeIntrusion)
{
    char *pathImage = NULL;
    pathImage = (char*)malloc(100 * sizeof(char));
    assert(pathImage);

    sprintf(pathImage, "%s%s%s", "picturesIntrusions/", dateTimeIntrusion , ".png");
    fprintf(stderr, "\npath = %s\n", pathImage);
    imwrite(pathImage, frame);

    return pathImage;
}
