#include <unistd.h>
#include <stdio.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <string.h>

#include "PPPP/PPPP_Type.h"
#include "PPPP/PPPP_API.h"
#include "PPPP/PPPP_Error.h"

int main(int argc, char **argv) {
    CHAR *DID = argv[1];

    // 3 STEPs in total
    // STEP 1: Init String
    INT32 init_ret = PPPP_Initialize((CHAR*)"EFGBFFBJKFJOGCJNFHHCFHEMGENHHBMHHLFGBKDFAMJLLDKHDHACDEPBGCLAIALDADMPKDDIODMEBOCNJLNDJJ");

    printf("Client, Connecting to Device %s\n", DID);
    CHAR enable_lan_search = 0; // false
    UINT16 udp_port = 0;
    // STEP 2: Connect to Device
    INT32 ret = PPPP_Connect(DID, enable_lan_search, udp_port);
    if(ret < 0) {
        printf("PPPP_Listen failed : %d\n", ret);
        return ret;
    } else {
        INT32 session_handle = ret;

        // STEP 3: Check session status
        st_PPPP_Session Sinfo;
        if(PPPP_Check(session_handle, &Sinfo) == ERROR_PPPP_SUCCESSFUL)
        {
            printf("\n-------Session Ready (%d): %s Mode ------------------\n", session_handle, (Sinfo.bMode ==0)? "P2P":"RLY");
            printf("Socket FD: %d\n", Sinfo.Skt); //Sockfd
            printf("Remote Client Addr : %s:%d\n", inet_ntoa(Sinfo.RemoteAddr.sin_addr),ntohs(Sinfo.RemoteAddr.sin_port));
            printf("I am %s\n", (Sinfo.bCorD ==0)? "Client":"Device");
            printf("Connection mode: %s\n", (Sinfo.bMode ==0)? "P2P":"RLY");
            printf("------------End of Session info ---------------\n");
#ifdef WIN32DLL
            Sleep(50);
#endif

            char cmd_on = 1;
            char cmd_off = 0;

            while(1) {

                PPPP_Write(session_handle, 0, &cmd_on, 1);
                sleep(1);

                PPPP_Write(session_handle, 0, &cmd_off, 1);
                sleep(1);
            }

            PPPP_Close(session_handle);
        }
    }

    printf("....Job Done!! press any key to exit\n");
    getchar();

    return 0;
}
