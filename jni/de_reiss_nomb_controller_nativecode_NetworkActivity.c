#include <de_reiss_nomb_controller_nativecode_NetworkActivity.h>


#include <android/log.h>



#include <string.h>
#include <jni.h>
#include <stdio.h>      /* for printf() and fprintf() */
#include <sys/socket.h> /* for socket(), connect(), sendto(), and
recvfrom() */
#include <arpa/inet.h>  /* for sockaddr_in and inet_addr() */
#include <stdlib.h>     /* for atoi() and exit() */
#include <unistd.h>     /* for close() */
#include <errno.h>


#include <sys/types.h>
#include <netdb.h>

//#define SERVERADRES    "192.168.178.68"//"37.24.147.223" // "127.0.0.1"




#define LOG_TAG "Nomb"





/*  Read a line from a socket  */

ssize_t Readline(int sockd, void *vptr, size_t maxlen) {
    ssize_t n, rc;
    char    c, *buffer;

    buffer = vptr;

    for ( n = 1; n < maxlen; n++ ) {

	if ( (rc = read(sockd, &c, 1)) == 1 ) {
	    *buffer++ = c;
	    if ( c == '\n' )
		break;
	}
	else if ( rc == 0 ) {
	    if ( n == 1 )
		return 0;
	    else
		break;
	}
	else {
	    if ( errno == EINTR )
		continue;
	    return -1;
	}
    }

    *buffer = 0;
    return n;
}







/*  Write a line to a socket  */

ssize_t Writeline(int sockd, const void *vptr, size_t n) {
    size_t      nleft;
    ssize_t     nwritten;
    const char *buffer;

    buffer = vptr;
    nleft  = n;

    while ( nleft > 0 ) {
	if ( (nwritten = write(sockd, buffer, nleft)) <= 0 ) {
	    if ( errno == EINTR )
		nwritten = 0;
	    else
		return -1;
	}
	nleft  -= nwritten;
	buffer += nwritten;
    }

    return n;
}




JNIEXPORT jstring JNICALL Java_de_reiss_nomb_controller_nativecode_NetworkActivity_connecttoserver(
		JNIEnv* env, jobject thiz, jstring serverip, jstring port) {

    __android_log_write(ANDROID_LOG_INFO, LOG_TAG, "<NATIVE SOCKET>");

    const char *nativeServerip = (*env)->GetStringUTFChars(env, serverip, 0);
    __android_log_print(ANDROID_LOG_INFO, LOG_TAG, "Server IP: %s",nativeServerip);
    const char *nativePort = (*env)->GetStringUTFChars(env, port, 0);
    __android_log_print(ANDROID_LOG_INFO, LOG_TAG, "Port: %s",nativePort);



    int sock;
    struct sockaddr_in serv_addr;
    char c;

    sock = (socket(AF_INET, SOCK_STREAM, 0));

    serv_addr.sin_family = AF_INET;
    serv_addr.sin_port = htons(atoi(nativePort));
    serv_addr.sin_addr.s_addr = inet_addr(nativeServerip);

    // connect to echo server
    if(connect(sock, (struct sockaddr *) &serv_addr, sizeof(serv_addr)) < 0) {
        close(sock);
        char* str = "Connecting to server did not work!\n";
         __android_log_write(ANDROID_LOG_INFO, LOG_TAG, "</NATIVE SOCKET>");
        return (*env)->NewStringUTF(env, str);
    }


    else {

        char* res = "";

        char buffer[100]= "hello from c\n"; // "\n" needed !!!! otherwise server hangs in readLine() ........
        if(Writeline(sock, buffer, strlen(buffer)) < 1) {
            res = "Writing to server did not work!\n";

            close(sock);

            __android_log_write(ANDROID_LOG_INFO, LOG_TAG, "</NATIVE SOCKET>");
            return (*env)->NewStringUTF(env, res);

        } else {
            res = "Writing to server worked! Sent: hello from c\n";

            if(Readline(sock, buffer, 100-1) < 0) {
                char str[100];
                strcpy(str,res);
                strcat(str," Reading from server did not work!\n");
                strcpy(str,buffer);

                close(sock);

                __android_log_write(ANDROID_LOG_INFO, LOG_TAG, "</NATIVE SOCKET>");
                return (*env)->NewStringUTF(env, str);
            }
            else {
                __android_log_print(ANDROID_LOG_INFO, LOG_TAG, "Echo response: %s", buffer);


                char str[256];
                snprintf(str, sizeof str, "%s%s%s", res, " Received: \n", buffer);




                close(sock);

                __android_log_write(ANDROID_LOG_INFO, LOG_TAG, "</NATIVE SOCKET>");
                return (*env)->NewStringUTF(env, str);
            }
        }



    }


}