#ifndef ExampleData_h
#define ExampleData_h

#include <string>
#include <list>
#include <set>
#include <map>
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
	std::set<std::string> setField;
	std::map<long, std::string> mapField;
	
	ExampleData *nestedExampleData;
};

#endif
