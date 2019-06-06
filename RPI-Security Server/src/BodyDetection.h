#ifndef BODY_DETECT_H
#define BODY_DETECT_H

#include <opencv2/highgui/highgui.hpp>
#include <opencv2/imgproc/imgproc.hpp>
#include <opencv2/core/core.hpp>
#include <opencv2/objdetect/objdetect.hpp>

using namespace std;
using namespace cv;

class BodyDetection
{
	private:
		CascadeClassifier *body_haarcascade;
		CascadeClassifier *face_haarcascade;

	public:
		BodyDetection();	
		~BodyDetection();
		void vdDetectFaces(Mat frame);
		void vdDetectBody(Mat frame);
};

#endif
