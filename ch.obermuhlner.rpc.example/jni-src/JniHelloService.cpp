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

void copy_jstring_to_std_string(JNIEnv *env, jstring inJString, std::string &outStdString) {
	if (!inJString) {
		outStdString.clear();
		return;
	}
	
	const char *s = env->GetStringUTFChars(inJString, NULL);
	outStdString = s;
	env->ReleaseStringUTFChars(inJString,s);
}

void copy_jobject_to_ExampleData(JNIEnv *env, jobject jniExampleData, ExampleData &exampleData) {
	jclass class_ExampleData = env->FindClass("ch/obermuhlner/rpc/example/api/ExampleData");
	
	jfieldID field_booleanField = env->GetFieldID(class_ExampleData, "booleanField", "Z");
	jfieldID field_intField = env->GetFieldID(class_ExampleData, "intField", "I");
	jfieldID field_longField = env->GetFieldID(class_ExampleData, "longField", "J");
	jfieldID field_stringField = env->GetFieldID(class_ExampleData, "stringField", "Ljava/lang/String;");
	jfieldID field_planetField = env->GetFieldID(class_ExampleData, "planetField", "Lch/obermuhlner/rpc/example/api/Planet;");

	exampleData.booleanField = env->GetBooleanField(jniExampleData, field_booleanField);
	exampleData.intField = env->GetIntField(jniExampleData, field_intField);
	exampleData.longField = env->GetLongField(jniExampleData, field_longField);
	copy_jstring_to_std_string(env, (jstring) env->GetObjectField(jniExampleData, field_stringField), exampleData.stringField);
}

jobject copy_ExampleData_to_jobject(JNIEnv *env, ExampleData &exampleData) {
	jclass class_ExampleData = env->FindClass("ch/obermuhlner/rpc/example/api/ExampleData");
	
	jmethodID method_init = env->GetMethodID(class_ExampleData, "<init>", "()V");
	jobject jniExampleData = env->NewObject(class_ExampleData, method_init);
	
	jfieldID field_booleanField = env->GetFieldID(class_ExampleData, "booleanField", "Z");
	jfieldID field_intField = env->GetFieldID(class_ExampleData, "intField", "I");
	jfieldID field_longField = env->GetFieldID(class_ExampleData, "longField", "J");
	jfieldID field_stringField = env->GetFieldID(class_ExampleData, "stringField", "Ljava/lang/String;");
	jfieldID field_planetField = env->GetFieldID(class_ExampleData, "planetField", "Lch/obermuhlner/rpc/example/api/Planet;");

	env->SetBooleanField(jniExampleData, field_booleanField, exampleData.booleanField);
	env->SetIntField(jniExampleData, field_intField, exampleData.intField);
	env->SetLongField(jniExampleData, field_longField, exampleData.longField);
	env->SetObjectField(jniExampleData, field_stringField, env->NewStringUTF(exampleData.stringField.c_str()));
	
	return jniExampleData;
}

HelloService helloService = HelloService();

JNIEXPORT void JNICALL Java_jni_JniHelloService_ping(JNIEnv *env, jobject thisObj) {
	helloService.ping();
}

JNIEXPORT double JNICALL Java_jni_JniHelloService_calculateSquare(JNIEnv *env, jobject thisObj, jdouble value) {
	return helloService.calculateSquare(value);
}

JNIEXPORT jobject JNICALL Java_jni_JniHelloService_exampleMethod(JNIEnv *env, jobject thisObj, jobject jniExampleData) {
	ExampleData exampleData;
	copy_jobject_to_ExampleData(env, jniExampleData, exampleData);
	
	ExampleData result = helloService.enrichExample(exampleData);
	
	return copy_ExampleData_to_jobject(env, exampleData);
}

JNIEXPORT jobject JNICALL Java_jni_JniHelloService_adapterExampleMethod(JNIEnv *env, jobject thisObj, jobject adapterExampleData) {
	return adapterExampleData;
}

JNIEXPORT void JNICALL Java_jni_JniHelloService_exampleFailure(JNIEnv *env, jobject thisObj) {
}
