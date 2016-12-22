# Define a variable for classpath
CLASS_PATH = ../bin

GPLUSPLUS=x86_64-w64-mingw32-g++

# Define a virtual path for .class in the bin directory
vpath %.class $(CLASS_PATH)

all : JniHelloService.dll install

install :
	mv JniHelloService.dll ..

# $@ matches the target, $< matches the first dependency
JniHelloService.dll : JniHelloService.o HelloService.o
	${GPLUSPLUS} -Wl,--add-stdcall-alias -shared -o $@ JniHelloService.o HelloService.o

# $@ matches the target, $< matches the first dependancy
JniHelloService.o : JniHelloService.cpp jni_JniHelloService.h
	${GPLUSPLUS} -I"${JAVA_HOME}\include" -I"${JAVA_HOME}\include\win32" -c $< -o $@

HelloService.o : HelloService.cpp HelloService.h
	${GPLUSPLUS} -c $< -o $@

# $* matches the target filename without the extension
jni_JniHelloService.h : 
	javah -classpath $(CLASS_PATH) jni.JniHelloService

clean :
	rm JniHelloService.o HelloService.o

