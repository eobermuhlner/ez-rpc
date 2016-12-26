#include <iostream>
#include "HelloService.h"

HelloService::HelloService() {
}

HelloService::~HelloService() {
}

void HelloService::ping() {
	std::cout << "Ping" << std::endl;
}

double HelloService::calculateSquare(double value) {
	return value * value;
}

ExampleData* HelloService::enrichExample(ExampleData *exampleData) {
	if (!exampleData) {
		return NULL;
	}

	exampleData->intField += 111;
	exampleData->longField += 22222222;
	exampleData->stringField += " from C++";
	exampleData->planetField = MARS;
	exampleData->listField.push_back("from C++");
	exampleData->setField.insert("from C++");
	exampleData->mapField[99] = "from C++";

	return exampleData;
}

