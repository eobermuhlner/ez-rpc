#include <jni.h>
#include <string>
#include "jni_JniHelloService.h"

#include "ExampleData.h"
#include "Planet.h"
#include "HelloService.h"

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

void CopyJStringToStdString(JNIEnv *env, jstring inJString, std::string &outStdString) {
	if (!inJString) {
		outStdString.clear();
		return;
	}
	
	const char *s = env->GetStringUTFChars(inJString, NULL);
	outStdString = s;
	env->ReleaseStringUTFChars(inJString,s);
}

HelloService helloService = HelloService();

JNIEXPORT void JNICALL Java_jni_JniHelloService_ping(JNIEnv *env, jobject thisObj) {
	helloService.ping();
}

JNIEXPORT double JNICALL Java_jni_JniHelloService_calculateSquare(JNIEnv *env, jobject thisObj, jdouble value) {
	return helloService.calculateSquare(value);
}

JNIEXPORT jobject JNICALL Java_jni_JniHelloService_exampleMethod(JNIEnv *env, jobject thisObj, jobject jniExampleData) {
	jclass class_jniExampleData = env->GetObjectClass(jniExampleData);
	
	jfieldID field_booleanField = env->GetFieldID(class_jniExampleData, "booleanField", "Z");
	jfieldID field_intField = env->GetFieldID(class_jniExampleData, "intField", "I");
	jfieldID field_longField = env->GetFieldID(class_jniExampleData, "longField", "J");
	jfieldID field_stringField = env->GetFieldID(class_jniExampleData, "stringField", "Ljava/lang/String;");
	jfieldID field_planetField = env->GetFieldID(class_jniExampleData, "planetField", "Lch/obermuhlner/rpc/example/api/Planet;");

	ExampleData exampleData;
	exampleData.booleanField = env->GetBooleanField(jniExampleData, field_booleanField);
	exampleData.intField = env->GetIntField(jniExampleData, field_intField);
	exampleData.longField = env->GetLongField(jniExampleData, field_longField);
	CopyJStringToStdString(env, (jstring) env->GetObjectField(jniExampleData, field_stringField), exampleData.stringField);
	
	ExampleData result = helloService.enrichExample(exampleData);
	
	env->SetBooleanField(jniExampleData, field_booleanField, result.booleanField);
	env->SetIntField(jniExampleData, field_intField, result.intField);
	env->SetLongField(jniExampleData, field_longField, result.longField);
	env->SetObjectField(jniExampleData, field_stringField, env->NewStringUTF(result.stringField.c_str()));
	
	return jniExampleData;
}

JNIEXPORT jobject JNICALL Java_jni_JniHelloService_adapterExampleMethod(JNIEnv *env, jobject thisObj, jobject adapterExampleData) {
	return adapterExampleData;
}

JNIEXPORT void JNICALL Java_jni_JniHelloService_exampleFailure(JNIEnv *env, jobject thisObj) {
}
