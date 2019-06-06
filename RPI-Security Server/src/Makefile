SHELL = /bin/sh
# définition des commandes utilisées
BIN = bin
CPPC = g++
ECHO = echo
RM = rm -f
TAR = tar
SUDO = sudo
MKDIR = mkdir
CHMOD = chmod
CP = rsync -R
# déclaration des options du compilateur
SDL_CFLAGS =
SDL_LDFLAGS = 
PG_FLAGS =
CPPFLAGS = -I. $(SDL_CFLAGS) $(PYTHON_CFLAGS)
CFLAGS = -Wall -O3 
OPCVFLAGS = 
POCO_LDFLAGS = -lPocoFoundation -lPocoNet  -lPocoNetSSL -lPocoUtil -lPocoXML
LDFLAGS = -lm $(POCO_LDFLAGS)  -lopencv_highgui -lopencv_imgproc -lopencv_core -lopencv_objdetect -lpthread -lwiringPi -lopencv_imgcodecs -lopencv_videoio -lcurl -lopencv_video

UNAME := $(shell uname)
ifeq ($(UNAME),Darwin)
	MACOSX_DEPLOYMENT_TARGET = 10.9
        CFLAGS += -I/opt/local/include/ -I/opt/local/include/opencv2 -mmacosx-version-min=$(MACOSX_DEPLOYMENT_TARGET) \
        -I/usr/local/include/opencv2
        LDFLAGS += -L/opt/local/lib/ -mmacosx-version-min=$(MACOSX_DEPLOYMENT_TARGET) -L/usr/lib -lc++ -lopencv_imgcodecs 
else
        CFLAGS += -I/usr/local/include/
        LDFLAGS += -lstdc++ -L/usr/local/lib -L/usr/X11R6/lib
endif

#définition des fichiers et dossiers
PROGNAME = RPIsecurity
PACKAGE=$(PROGNAME)
VERSION = 1.0
distdir = $(PACKAGE)-$(VERSION)
HEADERS = src/MJPEGWriter.h \
	src/ServoMoteur.h \
	src/EventCmdAndroid.h \
	src/socketServer.h \
	src/Alarm.h \
	src/SendMail.h \
	src/BodyDetection.h \
	src/SendNotification.h \
	src/IntrusionDetection.h \
	src/Window.h
SOURCES = src/Main.cpp \
	src/MJPEGWriter.cpp \
	src/ServoMoteur.cpp \
	src/SocketServer.cpp \
	src/EventCmdAndroid.cpp \
	src/Alarm.cpp \
	src/SendMail.cpp \
	src/BodyDetection.cpp \
	src/SendNotification.cpp \
	src/IntrusionDetection.cpp
OBJ = $(SOURCES:.cpp=.o)
DOXYFILE = documentation/Doxyfile
EXTRAFILES = 
DISTFILES = $(SOURCES) Makefile $(HEADERS) $(DOXYFILE) $(EXTRAFILES)

all: $(PROGNAME)

$(PROGNAME): $(OBJ)
	$(CC) $(OBJ) $(LDFLAGS) -o $(BIN)/$(PROGNAME)

%.o: %.cpp
	$(CPPC) $(CPPFLAGS) $(CFLAGS) -c $< -o $@

dist: distdir
	$(CHMOD) -R a+r $(distdir)
	$(TAR) zcvf $(distdir).tgz $(distdir)
	$(RM) -r $(distdir)

distdir: $(DISTFILES)
	$(RM) -r $(distdir)
	$(MKDIR) $(distdir)
	$(CHMOD) 777 $(distdir)
	$(CP) $(DISTFILES) $(distdir)

doc: $(DOXYFILE)
	cat $< | sed -e "s/PROJECT_NAME *=.*/PROJECT_NAME = $(PROGNAME)/" | sed -e "s/PROJECT_NUMBER *=.*/PROJECT_NUMBER = $(VERSION)/" >> $<.new
	mv -f $<.new $<
	cd documentation && doxygen && cd ..

clean:
	@$(RM) -r $(BIN)/$(PROGNAME) $(OBJ) *~ $(distdir).tgz gmon.out core.* documentation/*~ shaders/*~ documentation/html
