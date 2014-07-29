#include <de_reiss_nomb_controller_nativecode_RunCommandActivity.h>

#include <stdlib.h>
#include <string.h>
#include <stdio.h>
#include <time.h>

#include <android/log.h>



#define LOG_TAG "Nomb"



JNIEXPORT jstring JNICALL Java_de_reiss_nomb_controller_nativecode_RunCommandActivity_fetchdiskinfo(
		JNIEnv* env, jobject thiz) {

    char* str = "did not work";

    int result = system("/system/bin/df >> /data/data/de.reiss.nomb/fetchdiskinfo.out.txt");


//    int result;
//    result = system("/system/bin/df >> /mnt/sdcard/fetchdiskinfo.out.txt");
//    if(result <= -1) {
//        result = system("/system/bin/df >> /sdcard/fetchdiskinfo.out.txt");
//    }
//    if(result <= -1) {
//        result = system("/system/bin/df >> /storage/sdcard0/fetchdiskinfo.out.txt");
//    }
//    if(result <= -1) {
//        result = system("/system/bin/df >> /storage/sdcard/fetchdiskinfo.out.txt");
//    }

    if(result > -1) {
        str = "worked";
    }

    return (*env)->NewStringUTF(env, str);
}






JNIEXPORT jstring JNICALL Java_de_reiss_nomb_controller_nativecode_RunCommandActivity_fetchnetstatinfo(
		JNIEnv* env, jobject thiz) {

    char* str = "did not work";

    int result = system("/system/bin/netstat >> /data/data/de.reiss.nomb/fetchnetstatinfo.out.txt");

//    int result;
//    result = system("/system/bin/netstat >> /mnt/sdcard/fetchnetstatinfo.out.txt");
//    if(result <= -1) {
//        result = system("/system/bin/netstat >> /sdcard/fetchnetstatinfo.out.txt");
//    }
//    if(result <= -1) {
//        result = system("/system/bin/netstat >> /storage/sdcard0/fetchnetstatinfo.out.txt");
//    }
//    if(result <= -1) {
//        result = system("/system/bin/netstat >> /storage/sdcard/fetchnetstatinfo.out.txt");
//    }
//
//    if(result > -1) {
//        str = "worked";
//    }

    return (*env)->NewStringUTF(env, str);
}



JNIEXPORT jstring JNICALL Java_de_reiss_nomb_controller_nativecode_RunCommandActivity_fetchprocessinfo(
		JNIEnv* env, jobject thiz) {

    char* str = "did not work";

    int result = system("/system/bin/top -n 1 >> /data/data/de.reiss.nomb/fetchprocessinfo.out.txt");

//    int result;
//    result = system("/system/bin/top -n 1 >> /mnt/sdcard/fetchprocessinfo.out.txt");
//    if(result <= -1) {
//        result = system("/system/bin/top -n 1 >> /sdcard/fetchprocessinfo.out.txt");
//    }
//    if(result <= -1) {
//        result = system("/system/bin/top -n 1 >> /storage/sdcard0/fetchprocessinfo.out.txt");
//    }
//    if(result <= -1) {
//        result = system("/system/bin/top -n 1 >> /storage/sdcard/fetchprocessinfo.out.txt");
//    }

    if(result > -1) {
        str = "worked";
    }

    return (*env)->NewStringUTF(env, str);
}




JNIEXPORT jstring JNICALL Java_de_reiss_nomb_controller_nativecode_RunCommandActivity_getsystemproperty(
		JNIEnv* env, jobject thiz) {

    char* str = "did not work";

    int result = system("/system/bin/getprop >> /data/data/de.reiss.nomb/getsystemproperty.out.txt");

//    int result;
//    result = system("/system/bin/getprop >> /mnt/sdcard/getsystemproperty.out.txt");
//    if(result <= -1) {
//        result = system("/system/bin/getprop >> /sdcard/getsystemproperty.out.txt");
//    }
//    if(result <= -1) {
//        result = system("/system/bin/getprop >> /storage/sdcard0/getsystemproperty.out.txt");
//    }
//    if(result <= -1) {
//        result = system("/system/bin/getprop >> /storage/sdcard/getsystemproperty.out.txt");
//    }

    if(result > -1) {
        str = "worked";
    }

    return (*env)->NewStringUTF(env, str);
}







