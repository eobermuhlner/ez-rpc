#ifndef HelloService_h
#define HelloService_h

#include "ExampleData.h"

class HelloService {
public:
	HelloService();
	virtual ~HelloService();

	void ping();
	
	double calculateSquare(double value);
	
	ExampleData* enrichExample(ExampleData *exampleData);
};

#endif
