#include <de_reiss_nomb_controller_nativecode_PackageManagerInfoActivity.h>

#include <stdlib.h>
#include <string.h>
#include <stdio.h>
#include <time.h>

#include <android/log.h>




#define LOG_TAG "Nomb"
#define LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG,LOG_TAG,__VA_ARGS__)
#define LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)



JNIEXPORT jstring JNICALL Java_de_reiss_nomb_controller_nativecode_PackageManagerInfoActivity_getInstalledApplicationsViaPmCommand(
		JNIEnv* env, jobject thiz) {

    char* str = "did not work";

    int result;
    result = system("pm list packages >> /mnt/sdcard/packageslist.txt");
    if(result <= -1) {
        result = system("pm list packages >> /sdcard/packageslist.txt");
    }
    if(result <= -1) {
        result = system("pm list packages >> /storage/sdcard0/packageslist.txt");
    }
    if(result <= -1) {
        result = system("pm list packages >> /storage/sdcard/packageslist.txt");
    }
    //result = system(wholeCommand);
    LOGD("result code from 'pm list packages' : %d", result);


    if(result > -1) {
        str = "worked";
    }

    return (*env)->NewStringUTF(env, str);
}