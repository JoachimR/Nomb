#include <jni.h>
 



#ifndef _Included_de_reiss_nomb_controller_nativecode_FileHandlingActivity
#define _Included_de_reiss_nomb_controller_nativecode_FileHandlingActivity
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     de_reiss_nomb_controller_nativecode_FileHandlingActivity
 * Method:    performfopenwplus
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_de_reiss_nomb_controller_nativecode_FileHandlingActivity_performfopenwplus
  (JNIEnv *, jobject, jstring path);

/*
* Class:     de_reiss_nomb_controller_nativecode_FileHandlingActivity
* Method:    performfopenr
* Signature: ()Ljava/lang/String;
*/
JNIEXPORT jstring JNICALL Java_de_reiss_nomb_controller_nativecode_FileHandlingActivity_performfopenr
(JNIEnv *, jobject, jstring path);



#ifdef __cplusplus
}
#endif
#endif
