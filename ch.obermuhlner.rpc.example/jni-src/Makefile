# Define a variable for classpath
CLASS_PATH = ../bin

# Define a virtual path for .class in the bin directory
vpath %.class $(CLASS_PATH)

all : JniHelloService.dll install

install :
	mv JniHelloService.dll ..

# $@ matches the target, $< matches the first dependency
JniHelloService.dll : JniHelloService.o
	x86_64-w64-mingw32-g++ -Wl,--add-stdcall-alias -shared -o $@ $<

# $@ matches the target, $< matches the first dependancy
JniHelloService.o : JniHelloService.cpp JniHelloService.h
	x86_64-w64-mingw32-g++ -I"${JAVA_HOME}\include" -I"${JAVA_HOME}\include\win32" -c $< -o $@

# $* matches the target filename without the extension
JniHelloService.h : JniHelloService.class
	javah -classpath $(CLASS_PATH) $*

clean :
	rm JniHelloService.o

