#include <jni.h>
 



#ifndef _Included_de_reiss_nomb_controller_nativecode_NetworkActivityNetworkActivity
#define _Included_de_reiss_nomb_controller_nativecode_NetworkActivity
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     de_reiss_nomb_controller_nativecode_NetworkActivity
 * Method:    connecttoserver
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_de_reiss_nomb_controller_nativecode_NetworkActivity_connecttoserver
  (JNIEnv *, jobject, jstring serverip, jstring port);
 


#ifdef __cplusplus
}
#endif
#endif
