  /*
 * Fichier BodyDetection.cpp
 * Date : 04/06/2019
 * Auteurs : Nehari Mohamed et Yassine Tabet
 * Classe permettant la détection des corps et des visages sur une image
 */
 
#include "BodyDetection.h"
#include <iostream>

// Constructeur permettant le chargement des fichier XML Haarcascade servant à la détection de visages et de corps
BodyDetection::BodyDetection()
{
	this->face_haarcascade = new CascadeClassifier("data/lbpcascade_frontalface_improved.xml");
	this->body_haarcascade = new CascadeClassifier("data/haarcascade_fullbody.xml");

	if(this->body_haarcascade == NULL || this->face_haarcascade == NULL)
	{
		fprintf(stderr,"Haarcascade not initialized");
		exit(0);
	}
}


BodyDetection::~BodyDetection()
{
	delete this->body_haarcascade;
}

// Fonction permettant la détection de corps sur une image
void BodyDetection::vdDetectBody(Mat frame)
{
	vector<Rect> body;
	Mat frameGray;
	cvtColor(frame, frameGray, CV_RGB2GRAY);
	//equalizeHist( frameGray, frameGray );

	static const cv::Size minSize(30, 30);
    static const cv::Size maxSize;

  	body_haarcascade->detectMultiScale(frameGray, body, 1.1, 2, cv::CASCADE_SCALE_IMAGE, minSize, maxSize);

  	for (vector<Rect>::iterator fc = body.begin(); fc != body.end(); ++fc)
    {
      rectangle(frame, (*fc).tl(), (*fc).br(), Scalar(0, 255, 0), 2, CV_AA);
    }
}

// Fonction permettant la détection de visages sur une image
void BodyDetection::vdDetectFaces(Mat frame)
{
	vector<Rect> faces;
	Mat frameGray;
	cvtColor(frame, frameGray, CV_RGB2GRAY);
	//equalizeHist( frameGray, frameGray );

  	face_haarcascade->detectMultiScale(frameGray, faces, 1.1, 2, 0|CV_HAAR_SCALE_IMAGE, Size(30, 30));

  	for (vector<Rect>::iterator fc = faces.begin(); fc != faces.end(); ++fc)
    {
      rectangle(frame, (*fc).tl(), (*fc).br(), Scalar(0, 255, 0), 2, CV_AA);
    }
}


