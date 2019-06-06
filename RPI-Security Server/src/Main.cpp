#include "MJPEGWriter.h"
#include "SocketServer.h"
#include "ServoMoteur.h"
#include "SendMail.h"
#include "BodyDetection.h"
#include "SendNotification.h"
#include "IntrusionDetection.h"
#include "Window.h"

VideoCapture cam(0);
mutex mutexCamera;
Alarm *alarmBuzzer;

int main(int argc, char *argv[])
{
    MJPEGWriter connWebServer(atoi(argv[1]));
    Mat frameCamera;
    unsigned char u8firstConnexion = 1;

    alarmBuzzer = new Alarm(PIN_BUZZER);
    ServoMoteur *servoVertical = new ServoMoteur(PIN_SERVO_VERTICAL);
    SocketServer *networkServer = new SocketServer(atoi(argv[1])+1);
    networkServer->Init();
    networkServer->Start();

    if (!cam.isOpened())
    {
        cerr << "ERROR: Problème lors de l'ouverture de la Caméra" << endl;
        return 0;
    }
    
    while(1) 
    {    
        //Capture de la frame courante
        mutexCamera.lock();     
        cam >> frameCamera;
        mutexCamera.unlock(); 
        
        if (frameCamera.empty()) 
            continue;
        
        flip(frameCamera, frameCamera, -1);
        connWebServer.write(frameCamera);
        
        if(u8firstConnexion)
        {
            connWebServer.start();
            u8firstConnexion = 0;
            servoVertical->vdPositionDemarrage();
        }

        char key = cv::waitKey(5);
        if(key == 27)
            break;

        usleep(10000);
    }
    
    delete networkServer;
    delete servoVertical;
    delete alarmBuzzer;
    connWebServer.stop();
    cam.release();
    destroyAllWindows();
    
    return 0;
}
