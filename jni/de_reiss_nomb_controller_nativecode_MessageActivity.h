#include <jni.h>
 



#ifndef de_reiss_nomb_controller_nativecode_MessageActivity
#define de_reiss_nomb_controller_nativecode_MessageActivity
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     de_reiss_nomb_controller_nativecode_MessageActivity
 * Method:    sendSmsViaAmCommand
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_de_reiss_nomb_controller_nativecode_MessageActivity_sendSmsViaAmCommand
  (JNIEnv *, jobject, jstring phonenumber, jstring messagebody);



#ifdef __cplusplus
}
#endif
#endif
