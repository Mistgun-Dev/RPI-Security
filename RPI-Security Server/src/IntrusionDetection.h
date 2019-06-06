#ifndef INTRUSION_DETECTION_H
#define INTRUSION_DETECTION_H

#include <iostream>
#include <string>
#include <opencv2/highgui/highgui.hpp>
#include <opencv2/imgproc/imgproc.hpp>
#include <opencv2/core/core.hpp>
#include "opencv2/video/video.hpp"
#include <thread>

#define UNKNOWN_FLOW_THRESH 1e9

using namespace cv;
using namespace std;

class IntrusionDetection
{
	public:
		Mat previousFrame;
		int s32Sensibility;
	    volatile bool bRunThread;
		thread threadDetection;
		bool bIntrusionDetected;
		bool bFirstFrameDetection;

		IntrusionDetection();
		~IntrusionDetection();
		void vdDetectIntrusion();
		void vdSetThreadStatus(bool status);
		char* vdSavePicture(Mat frame, char *dateTimeIntrusion);
		void vdReset();
};

#endif