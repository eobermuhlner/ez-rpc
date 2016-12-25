#ifndef ExampleData_h
#define ExampleData_h

#include <string>
#include <list>
#include "Planet.h"

class ExampleData {
public:
	ExampleData();
	virtual ~ExampleData();

	bool booleanField;
	long intField;
	long long longField;
	std::string stringField;
	Planet planetField;
	std::list<std::string> listField;
	
	ExampleData *nestedExampleData;
};

#endif
