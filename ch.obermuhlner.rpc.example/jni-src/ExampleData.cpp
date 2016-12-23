#include "ExampleData.h"

ExampleData::ExampleData() {
}

ExampleData::~ExampleData() {
	if (nestedExampleData) {
		delete nestedExampleData;
	}
}
