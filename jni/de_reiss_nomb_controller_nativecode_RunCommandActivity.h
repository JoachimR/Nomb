#include <jni.h>
 



#ifndef _Included_de_reiss_nomb_controller_nativecode_RunCommandActivity
#define _Included_de_reiss_nomb_controller_nativecode_RunCommandActivity
#ifdef __cplusplus
extern "C" {
#endif
/*
* Class:     de_reiss_nomb_controller_nativecode_RunCommandActivity
* Method:    fetchdiskinfo
* Signature: ()Ljava/lang/String;
*/
JNIEXPORT jstring JNICALL Java_de_reiss_nomb_controller_nativecode_RunCommandActivity_fetchdiskinfo
(JNIEnv *, jobject);

/*
* Class:     de_reiss_nomb_controller_nativecode_RunCommandActivity
* Method:    fetchnetstatinfo
* Signature: ()Ljava/lang/String;
*/
JNIEXPORT jstring JNICALL Java_de_reiss_nomb_controller_nativecode_RunCommandActivity_fetchnetstatinfo
(JNIEnv *, jobject);

/*
* Class:     de_reiss_nomb_controller_nativecode_RunCommandActivity
* Method:    fetchprocessinfo
* Signature: ()Ljava/lang/String;
*/
JNIEXPORT jstring JNICALL Java_de_reiss_nomb_controller_nativecode_RunCommandActivity_fetchprocessinfo
(JNIEnv *, jobject);

/*
* Class:     de_reiss_nomb_controller_nativecode_RunCommandActivity
* Method:    getsystemproperty
* Signature: ()Ljava/lang/String;
*/
JNIEXPORT jstring JNICALL Java_de_reiss_nomb_controller_nativecode_RunCommandActivity_getsystemproperty
(JNIEnv *, jobject);



#ifdef __cplusplus
}
#endif
#endif
