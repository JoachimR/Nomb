#include <de_reiss_nomb_controller_nativecode_MessageActivity.h>

#include <stdlib.h>
#include <string.h>
#include <stdio.h>
#include <time.h>

#include <android/log.h>




#define LOG_TAG "Nomb"
#define LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG,LOG_TAG,__VA_ARGS__)
#define LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)



JNIEXPORT jstring JNICALL Java_de_reiss_nomb_controller_nativecode_MessageActivity_sendSmsViaAmCommand(
		JNIEnv* env, jobject thiz, jstring phonenumber, jstring messagebody) {

    char* str = "Starting SENDTO Intent via am command did not work!";

    char* part1 = "am start -a android.intent.action.SENDTO -d sms:";
    const char *part2 = (*env)->GetStringUTFChars(env, phonenumber, 0);
    char* part3 = "--es sms_body \"";
    const char *part4 = (*env)->GetStringUTFChars(env, messagebody, 0);
    char* part5 = "\" --ez exit_on_sent true";

    char* part1and2;
    part1and2 = malloc(strlen(part1)+1+strlen(part2));
    strcpy(part1and2, part1);
    strcat(part1and2, part2);


    char* part3and4;
    part3and4 = malloc(strlen(part3)+1+strlen(part4));
    strcpy(part3and4, part3);
    strcat(part3and4, part4);


    char* first4parts;
    first4parts = malloc(strlen(part1and2)+1+strlen(part3and4));
    strcpy(first4parts, part1and2);
    strcat(first4parts, part3and4);


    char* wholeCommand;
    wholeCommand = malloc(strlen(first4parts)+1+strlen(part5));
    strcpy(wholeCommand, first4parts);
    strcat(wholeCommand, part5);

    LOGD("command : %d", wholeCommand);


    int result;
    //result = system("am start -a android.intent.action.SENDTO -d sms:01511234567 --es sms_body \"test\" --ez exit_on_sent true");
    result = system(wholeCommand);
    LOGD("result from am : %d", result);


    if(result) {
        str = "Starting SENDTO Intent via am command worked!";
    }

    return (*env)->NewStringUTF(env, str);
}