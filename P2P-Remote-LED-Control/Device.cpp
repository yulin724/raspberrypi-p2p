#include <unistd.h>
#include <stdio.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <string.h>

#include <wiringPi.h>

#include "PPPP/PPPP_Type.h"
#include "PPPP/PPPP_API.h"
#include "PPPP/PPPP_Error.h"

#define LED 7

INT32 session_handle;

void execute_cmd_turn_on_led()
{
    printf("ON\n");
    digitalWrite (LED, 0);
}

void execute_cmd_turn_off_led()
{
    printf("OFF\n");
     digitalWrite (LED, 1);
}

void enter_service_loop() {
    char cmd = -1;

    wiringPiSetup();
    pinMode (LED, OUTPUT) ;

    int ret = -1;

    while(1) {
        UINT32 ReadSize;

        ret = PPPP_Check_Buffer(session_handle, 0, NULL, &ReadSize);

        INT32 DataSize = ReadSize;
        if(ret == ERROR_PPPP_SESSION_CLOSED_TIMEOUT)
        {
            printf("Session TimeOUT!!\n");
            break;
        }
        else if(ret == ERROR_PPPP_SESSION_CLOSED_REMOTE)
        {
            printf("Session Remote Close!!\n");
            break;
        }

        if(ReadSize > 0) {
            ret = PPPP_Read(session_handle, 0, &cmd, &DataSize, 0xFFFFFFFF);

            if(cmd==0) {
                execute_cmd_turn_off_led();
            }

            if(cmd==1) {
                execute_cmd_turn_on_led();
            }

            if(cmd==2) {
		printf("EXIT\n");
                return;
            }
        }
        usleep(500*1000);
    }
}

int main(int argc, char **argv) {
    CHAR *DID = argv[1];

    // 4 STEPs in total

    // STEP 1: Init String
    INT32 init_ret = PPPP_Initialize((CHAR*)"EFGBFFBJKFJOGCJNFHHCFHEMGENHHBMHHLFGBKDFAMJLLDKHDHACDEPBGCLAIALDADMPKDDIODMEBOCNJLNDJJ");

    // STEP 2: Login to P2P Server
    UINT32 timeout = 60;
    UINT16 udp_port = 10000;
    CHAR enable_Internet = 1; // true

    while(1) {
        printf("Device %s, Start to Listen ...\n", DID);
        // STEP 3: Start to Listen, waitting for client
        INT32 ret = PPPP_Listen(DID, timeout, udp_port, enable_Internet);
        if(ret < 0) {
            printf("PPPP_Listen failed : %d\n", ret);
	    continue;
        } else {
            session_handle = ret;
            // STEP 4: Check session status
            st_PPPP_Session Sinfo;
            if(PPPP_Check(session_handle, &Sinfo) == ERROR_PPPP_SUCCESSFUL)
            {
                printf("\n-------Session Ready (%d): %s Mode ------------------\n", session_handle, (Sinfo.bMode ==0)? "P2P":"RLY");
                printf("Socket FD: %d\n", Sinfo.Skt); //Sockfd
                printf("Remote Client Addr : %s:%d\n", inet_ntoa(Sinfo.RemoteAddr.sin_addr),ntohs(Sinfo.RemoteAddr.sin_port));
                printf("I am %s\n", (Sinfo.bCorD ==0)? "Client":"Device");
                printf("Connection mode: %s\n", (Sinfo.bMode ==0)? "P2P":"RLY");
                printf("------------End of Session info ---------------\n");

                enter_service_loop();

                PPPP_Close(session_handle);
            }
        }
    }

    PPPP_DeInitialize();

    printf("....Job Done!! press any key to exit\n");
    getchar();
    return 0;
}
