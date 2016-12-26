#include <jni.h>
#include <string>
#include <list>
#include <set>
#include <iostream>
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

void copy_java_string_to_std_string(JNIEnv *env, jstring inJavaString, std::string &outStdString) {
	if (!inJavaString) {
		outStdString.clear();
		return;
	}
	
	const char *s = env->GetStringUTFChars(inJavaString, NULL);
	outStdString = s;
	env->ReleaseStringUTFChars(inJavaString,s);
}

// convert list

void copy_java_string_list_to_std_list(JNIEnv *env, jobject inJavaList, std::list<std::string> &outStdList) {
	if (!inJavaList) {
		outStdList.clear();
		return;
	}
	
	jclass class_List = env->FindClass("java/util/List");
	jmethodID method_List_size = env->GetMethodID(class_List, "size", "()I");
	jmethodID method_List_get = env->GetMethodID(class_List, "get", "(I)Ljava/lang/Object;");
	
	long size = env->CallIntMethod(inJavaList, method_List_size);

	for(jlong i=0; i<size; i++) {
		jobject javaElement = env->CallObjectMethod(inJavaList, method_List_get, i);
		
		std::string element;
		copy_java_string_to_std_string(env, (jstring) javaElement, element);
		outStdList.push_back(element);
	}
}

jobject copy_std_string_list_to_java_list(JNIEnv *env, std::list<std::string> &inStdList) {
	jclass class_ArrayList = env->FindClass("java/util/ArrayList");
	jmethodID method_ArrayList_init = env->GetMethodID(class_ArrayList, "<init>", "()V");
	jmethodID method_ArrayList_add = env->GetMethodID(class_ArrayList, "add", "(Ljava/lang/Object;)Z");

	jobject javaList = env->NewObject(class_ArrayList, method_ArrayList_init);

	for (std::list<std::string>::iterator iter = inStdList.begin(); iter != inStdList.end(); iter++) {
		std::string element = *iter;
		
		jstring javaString = env->NewStringUTF(element.c_str());
		env->CallBooleanMethod(javaList, method_ArrayList_add, javaString);
	}

	return javaList;
}

// convert set

void copy_java_string_set_to_std_set(JNIEnv *env, jobject inJavaSet, std::set<std::string> &outStdSet) {
	if (!inJavaSet) {
		outStdSet.clear();
		return;
	}

	jclass class_Set = env->FindClass("java/util/Set");
	jmethodID method_Set_iterator = env->GetMethodID(class_Set, "iterator", "()Ljava/util/Iterator;");
	
	jclass class_Iterator = env->FindClass("java/util/Iterator");
	jmethodID method_Iterator_hasNext = env->GetMethodID(class_Iterator, "hasNext", "()Z");
	jmethodID method_Iterator_next = env->GetMethodID(class_Iterator, "next", "()Ljava/lang/Object;");

	jobject javaIterator = env->CallObjectMethod(inJavaSet, method_Set_iterator);
	while (env->CallBooleanMethod(javaIterator, method_Iterator_hasNext)) {
		jobject javaElement = env->CallObjectMethod(javaIterator, method_Iterator_next);

		std::string element;
		copy_java_string_to_std_string(env, (jstring) javaElement, element);
		outStdSet.insert(element);
	}
}

jobject copy_std_string_set_to_java_set(JNIEnv *env, std::set<std::string> &inStdSet) {
	jclass class_HashSet = env->FindClass("java/util/HashSet");

	jmethodID method_HashSet_init = env->GetMethodID(class_HashSet, "<init>", "()V");
	jmethodID method_HashSet_add = env->GetMethodID(class_HashSet, "add", "(Ljava/lang/Object;)Z");

	jobject javaSet = env->NewObject(class_HashSet, method_HashSet_init);

	for (std::set<std::string>::iterator iter = inStdSet.begin(); iter != inStdSet.end(); iter++) {
		std::string element = *iter;
		
		jstring javaString = env->NewStringUTF(element.c_str());
		env->CallBooleanMethod(javaSet, method_HashSet_add, javaString);
	}

	return javaSet;
}

// convert map

void copy_java_integer_string_map_to_std_map(JNIEnv *env, jobject inJavaMap, std::map<long, std::string> &outStdMap) {
	if (!inJavaMap) {
		outStdMap.clear();
		return;
	}

	jclass class_Map = env->FindClass("java/util/Map");
	jmethodID method_Map_keySet = env->GetMethodID(class_Map, "keySet", "()Ljava/util/Set;");
	jmethodID method_Map_get = env->GetMethodID(class_Map, "get", "(Ljava/lang/Object;)Ljava/lang/Object;");

	jclass class_Set = env->FindClass("java/util/Set");
	jmethodID method_Set_iterator = env->GetMethodID(class_Set, "iterator", "()Ljava/util/Iterator;");
	
	jclass class_Iterator = env->FindClass("java/util/Iterator");
	jmethodID method_Iterator_hasNext = env->GetMethodID(class_Iterator, "hasNext", "()Z");
	jmethodID method_Iterator_next = env->GetMethodID(class_Iterator, "next", "()Ljava/lang/Object;");

	jclass class_Integer = env->FindClass("java/lang/Integer");
	jmethodID method_Integer_intValue = env->GetMethodID(class_Integer, "intValue", "()I");

	jobject javaKeySet = env->CallObjectMethod(inJavaMap, method_Map_keySet);
	jobject javaIterator = env->CallObjectMethod(javaKeySet, method_Set_iterator);
	while (env->CallBooleanMethod(javaIterator, method_Iterator_hasNext)) {
		jobject javaKey = env->CallObjectMethod(javaIterator, method_Iterator_next);
		jobject javaValue = env->CallObjectMethod(inJavaMap, method_Map_get, javaKey);
	
		jint javaIntKey = env->CallIntMethod(javaKey, method_Integer_intValue);
		
		std::string value;
		copy_java_string_to_std_string(env, (jstring) javaValue, value);
		outStdMap[javaIntKey] = value;
	}
}

jobject copy_std_long_string_map_to_java_map(JNIEnv *env, std::map<long, std::string> &inStdMap) {
	jclass class_HashMap = env->FindClass("java/util/HashMap");
	jmethodID method_HashMap_init = env->GetMethodID(class_HashMap, "<init>", "()V");
	jmethodID method_HashMap_put = env->GetMethodID(class_HashMap, "put", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;");

	jclass class_Integer = env->FindClass("java/lang/Integer");
	jmethodID method_Integer_init = env->GetMethodID(class_Integer, "<init>", "(I)V");

	jobject javaMap = env->NewObject(class_HashMap, method_HashMap_init);

	for (std::map<long, std::string>::iterator iter = inStdMap.begin(); iter != inStdMap.end(); iter++) {
		long key = iter->first;
		std::string value = iter->second;
		
		jobject javaIntegerKey = env->NewObject(class_Integer, method_Integer_init, key);
		jstring javaStringValue = env->NewStringUTF(value.c_str());
		env->CallObjectMethod(javaMap, method_HashMap_put, javaIntegerKey, javaStringValue);
	}

	return javaMap;
}

// convert ExampleData

ExampleData* convert_jobject_to_ExampleData(JNIEnv *env, jobject jniExampleData) {
	if (!jniExampleData) {
		return NULL;
	}
	
	jclass class_ExampleData = env->FindClass("ch/obermuhlner/rpc/example/api/ExampleData");
	jfieldID field_ExampleData_booleanField = env->GetFieldID(class_ExampleData, "booleanField", "Z");
	jfieldID field_ExampleData_intField = env->GetFieldID(class_ExampleData, "intField", "I");
	jfieldID field_ExampleData_longField = env->GetFieldID(class_ExampleData, "longField", "J");
	jfieldID field_ExampleData_stringField = env->GetFieldID(class_ExampleData, "stringField", "Ljava/lang/String;");
	jfieldID field_ExampleData_planetField = env->GetFieldID(class_ExampleData, "planetField", "Lch/obermuhlner/rpc/example/api/Planet;");
	jfieldID field_ExampleData_nestedExampleData = env->GetFieldID(class_ExampleData, "nestedExampleData", "Lch/obermuhlner/rpc/example/api/ExampleData;");
	jfieldID field_ExampleData_listField = env->GetFieldID(class_ExampleData, "listField", "Ljava/util/List;");
	jfieldID field_ExampleData_setField = env->GetFieldID(class_ExampleData, "setField", "Ljava/util/Set;");
	jfieldID field_ExampleData_mapField = env->GetFieldID(class_ExampleData, "mapField", "Ljava/util/Map;");

	ExampleData *exampleData = new ExampleData();
	exampleData->booleanField = env->GetBooleanField(jniExampleData, field_ExampleData_booleanField);
	exampleData->intField = env->GetIntField(jniExampleData, field_ExampleData_intField);
	exampleData->longField = env->GetLongField(jniExampleData, field_ExampleData_longField);
	copy_java_string_to_std_string(env, (jstring) env->GetObjectField(jniExampleData, field_ExampleData_stringField), exampleData->stringField);
	exampleData->nestedExampleData = convert_jobject_to_ExampleData(env, env->GetObjectField(jniExampleData, field_ExampleData_nestedExampleData));
	copy_java_string_list_to_std_list(env, env->GetObjectField(jniExampleData, field_ExampleData_listField), exampleData->listField);
	copy_java_string_set_to_std_set(env, env->GetObjectField(jniExampleData, field_ExampleData_setField), exampleData->setField);
	copy_java_integer_string_map_to_std_map(env, env->GetObjectField(jniExampleData, field_ExampleData_mapField), exampleData->mapField);
		
	return exampleData;
}

jobject convert_ExampleData_to_jobject(JNIEnv *env, ExampleData *exampleData) {
	if (!exampleData) {
		return NULL;
	}

	jclass class_ExampleData = env->FindClass("ch/obermuhlner/rpc/example/api/ExampleData");
	
	jmethodID method_ExampleData_init = env->GetMethodID(class_ExampleData, "<init>", "()V");
	jobject jniExampleData = env->NewObject(class_ExampleData, method_ExampleData_init);
	
	jfieldID field_ExampleData_booleanField = env->GetFieldID(class_ExampleData, "booleanField", "Z");
	jfieldID field_ExampleData_intField = env->GetFieldID(class_ExampleData, "intField", "I");
	jfieldID field_ExampleData_longField = env->GetFieldID(class_ExampleData, "longField", "J");
	jfieldID field_ExampleData_stringField = env->GetFieldID(class_ExampleData, "stringField", "Ljava/lang/String;");
	jfieldID field_ExampleData_planetField = env->GetFieldID(class_ExampleData, "planetField", "Lch/obermuhlner/rpc/example/api/Planet;");
	jfieldID field_ExampleData_nestedExampleData = env->GetFieldID(class_ExampleData, "nestedExampleData", "Lch/obermuhlner/rpc/example/api/ExampleData;");
	jfieldID field_ExampleData_listField = env->GetFieldID(class_ExampleData, "listField", "Ljava/util/List;");
	jfieldID field_ExampleData_setField = env->GetFieldID(class_ExampleData, "setField", "Ljava/util/Set;");
	jfieldID field_ExampleData_mapField = env->GetFieldID(class_ExampleData, "mapField", "Ljava/util/Map;");

	env->SetBooleanField(jniExampleData, field_ExampleData_booleanField, exampleData->booleanField);
	env->SetIntField(jniExampleData, field_ExampleData_intField, exampleData->intField);
	env->SetLongField(jniExampleData, field_ExampleData_longField, exampleData->longField);
	env->SetObjectField(jniExampleData, field_ExampleData_stringField, env->NewStringUTF(exampleData->stringField.c_str()));
	env->SetObjectField(jniExampleData, field_ExampleData_nestedExampleData, convert_ExampleData_to_jobject(env, exampleData->nestedExampleData));
	env->SetObjectField(jniExampleData, field_ExampleData_listField, copy_std_string_list_to_java_list(env, exampleData->listField));
	env->SetObjectField(jniExampleData, field_ExampleData_setField, copy_std_string_set_to_java_set(env, exampleData->setField));
	env->SetObjectField(jniExampleData, field_ExampleData_mapField, copy_std_long_string_map_to_java_map(env, exampleData->mapField));
	
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
	ExampleData *exampleData = convert_jobject_to_ExampleData(env, jniExampleData);
	
	ExampleData *result = helloService.enrichExample(exampleData);

	if (exampleData) {
		delete exampleData;
	}
	
	jobject jniResult = convert_ExampleData_to_jobject(env, result);
	
	if (result) {
		delete result;
	}
	
	return jniResult;
}

JNIEXPORT jobject JNICALL Java_jni_JniHelloService_adapterExampleMethod(JNIEnv *env, jobject thisObj, jobject adapterExampleData) {
	return adapterExampleData;
}

JNIEXPORT void JNICALL Java_jni_JniHelloService_exampleFailure(JNIEnv *env, jobject thisObj) {
}
