#include <jni.h>
#include <string>
#include "jni_JniHelloService.h"

/*
Type     Chararacter 
boolean      Z 
byte         B 
char         C 
double       D 
float        F 
int          I 
long         J 
object       L 
short        S 
void         V 
array        [ 
*/

class ExampleData {
public:
	bool booleanField;
	long intField;
	long long longField;
	//std::string stringField;
};


/*
void CopyJStringToStdString(JNIEnv *env, jstring inJString, std::string &outStdString) {
	if (!inJString) {
		outStdString.clear();
		return;
	}
	
	const char *s = env->GetStringUTFChars(inJString, NULL);
	outStdString = s;
	env->ReleaseStringUTFChars(inJString,s);
}
*/
 
JNIEXPORT void JNICALL Java_jni_JniHelloService_ping(JNIEnv *env, jobject thisObj) {
	return;
}

JNIEXPORT double JNICALL Java_jni_JniHelloService_calculateSquare(JNIEnv *env, jobject thisObj, jdouble value) {
	return value * value;
}

JNIEXPORT jobject JNICALL Java_jni_JniHelloService_exampleMethod(JNIEnv *env, jobject thisObj, jobject jniExampleData) {
	ExampleData exampleData;
	
	jclass jniExampleDataClass = env->GetObjectClass(jniExampleData);
	jfieldID fieldBooleanField = env->GetFieldID(jniExampleDataClass, "booleanField", "Z");
	jfieldID fieldIntField = env->GetFieldID(jniExampleDataClass, "intField", "I");
	jfieldID fieldLongField = env->GetFieldID(jniExampleDataClass, "longField", "J");
	//jfieldID fieldStringField = env->GetFieldID(jniExampleDataClass, "stringField", "Ljava/lang/String;");

	exampleData.booleanField = env->GetBooleanField(jniExampleData, fieldBooleanField);
	exampleData.intField = env->GetIntField(jniExampleData, fieldIntField);
	exampleData.longField = env->GetLongField(jniExampleData, fieldLongField);
	//jstring exampleDataStringField = (jstring) env->GetObjectField(jniExampleData, fieldStringField);
	//CopyJStringToStdString(env, exampleDataStringField, exampleData.stringField);
	
	exampleData.booleanField = true;
	exampleData.intField += 1111;
	exampleData.longField += 3333;
	
	env->SetBooleanField(jniExampleData, fieldBooleanField, exampleData.booleanField);
	env->SetIntField(jniExampleData, fieldIntField, exampleData.intField);
	env->SetLongField(jniExampleData, fieldLongField, exampleData.longField);
	
	return jniExampleData;
}

JNIEXPORT jobject JNICALL Java_jni_JniHelloService_adapterExampleMethod(JNIEnv *env, jobject thisObj, jobject adapterExampleData) {
	return adapterExampleData;
}

JNIEXPORT void JNICALL Java_jni_JniHelloService_exampleFailure(JNIEnv *env, jobject thisObj) {
}
