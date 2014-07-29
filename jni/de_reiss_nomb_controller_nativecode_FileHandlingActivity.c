#include <de_reiss_nomb_controller_nativecode_FileHandlingActivity.h>

#include <stdlib.h>
#include <string.h>
#include <stdio.h>
#include <time.h>

#include <android/log.h>


/*
for fprintf():

Get stdout to LogCat
http://developer.android.com/tools/debugging/debugging-log.html#viewingStd
adb shell stop
adb shell setprop log.redirect-stdio true
adb shell start
*/



#define LOG_TAG "Nomb"



JNIEXPORT jstring JNICALL Java_de_reiss_nomb_controller_nativecode_FileHandlingActivity_performfopenwplus(
		JNIEnv* env, jobject thiz, jstring path) {

    __android_log_write(ANDROID_LOG_ERROR, LOG_TAG, "<NATIVE ACCESS FOPEN>");

    char* str = "Native Code! fopen(path, \"w+\") did not work!";


    const char *nativePath = (*env)->GetStringUTFChars(env, path, 0);
    __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, "path:%s",nativePath);

    FILE* file = fopen(nativePath,"w+");

//    FILE* file = fopen("/sdcard/hello.txt","w+"); // Android 2.1
//    if (file == NULL) {
//        file = fopen("/mnt/sdcard/hello.txt","w+");
//        if (file == NULL) {
//            file = fopen("/storage/sdcard/hello.txt","w+"); // Android 4.4+
//        }
//    }

    if (file) {
        __android_log_write(ANDROID_LOG_ERROR, LOG_TAG, "file exists!");

        // write current time to file
        time_t rawtime;
        struct tm * timeinfo;
        time ( &rawtime );
        timeinfo = localtime ( &rawtime );
        //printf("accessed and wrote this to the file at: %s", asctime (timeinfo));


        fprintf(file, "accessed and wrote this to the file via NDK at: %s", asctime(timeinfo) );

        fflush(file);
        fclose(file);

         str = "Native Code! fopen(path, \"w+\") worked!";
    } else {
        __android_log_write(ANDROID_LOG_ERROR, LOG_TAG, "file null!");
    }
    __android_log_write(ANDROID_LOG_ERROR, LOG_TAG, "</NATIVE ACCESS FOPEN>");

    return (*env)->NewStringUTF(env, str);
}








JNIEXPORT jstring JNICALL Java_de_reiss_nomb_controller_nativecode_FileHandlingActivity_performfopenr(
		JNIEnv* env, jobject thiz, jstring path) {

    __android_log_write(ANDROID_LOG_ERROR, LOG_TAG, "<NATIVE ACCESS FOPEN>");

    char* str = "Native Code! fopen(path, \"r\") did not work!";


    const char *nativePath = (*env)->GetStringUTFChars(env, path, 0);
    __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, "path:%s",nativePath);

    FILE* file = fopen(nativePath,"r");



    if (file) {
        __android_log_write(ANDROID_LOG_ERROR, LOG_TAG, "file exists!");
        char readFromFile[924];
        fgets(readFromFile,924,file); // TODO read more lines?
        fflush(file);
        fclose(file);
        char result[1024];
        strcpy (result, "Native Code! fopen(path, \"r\"') worked!\n\n");
        strcat (result, readFromFile);

        __android_log_write(ANDROID_LOG_ERROR, LOG_TAG, "</NATIVE ACCESS FOPEN>");
        return (*env)->NewStringUTF(env, result);
    } else {
        __android_log_write(ANDROID_LOG_ERROR, LOG_TAG, "file null!");
        __android_log_write(ANDROID_LOG_ERROR, LOG_TAG, "</NATIVE ACCESS FOPEN>");
        return (*env)->NewStringUTF(env, str);
    }



}


