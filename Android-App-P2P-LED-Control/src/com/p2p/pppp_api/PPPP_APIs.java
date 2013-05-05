package com.p2p.pppp_api;

public class PPPP_APIs {
	public static int ms_verAPI=0;
	
	public static final int ERROR_PPPP_SUCCESSFUL						=  0;
	
	public static final int ERROR_PPPP_NOT_INITIALIZED					= -1;
	public static final int ERROR_PPPP_ALREADY_INITIALIZED				= -2;
	public static final int ERROR_PPPP_TIME_OUT							= -3;
	public static final int ERROR_PPPP_INVALID_ID						= -4;
	public static final int ERROR_PPPP_INVALID_PARAMETER				= -5;
	public static final int ERROR_PPPP_DEVICE_NOT_ONLINE				= -6;
	public static final int ERROR_PPPP_FAIL_TO_RESOLVE_NAME				= -7;
	public static final int ERROR_PPPP_INVALID_PREFIX					= -8;
	public static final int ERROR_PPPP_ID_OUT_OF_DATE					= -9;
	public static final int ERROR_PPPP_NO_RELAY_SERVER_AVAILABLE		= -10;
	public static final int ERROR_PPPP_INVALID_SESSION_HANDLE			= -11;
	public static final int ERROR_PPPP_SESSION_CLOSED_REMOTE			= -12;
	public static final int ERROR_PPPP_SESSION_CLOSED_TIMEOUT			= -13;
	public static final int ERROR_PPPP_SESSION_CLOSED_CALLED			= -14;
	public static final int ERROR_PPPP_REMOTE_SITE_BUFFER_FULL			= -15;
	public static final int ERROR_PPPP_USER_LISTEN_BREAK				= -16;
	public static final int ERROR_PPPP_MAX_SESSION						= -17;
	public static final int ERROR_PPPP_UDP_PORT_BIND_FAILED				= -18;
	public static final int ERROR_PPPP_USER_CONNECT_BREAK				= -19;
	public static final int ERROR_PPPP_SESSION_CLOSED_INSUFFICIENT_MEMORY=-20;
	
	public static final int ER_ANDROID_NULL								=-5000;
	
	public native static int PPPP_GetAPIVersion();
	public native static int PPPP_Initialize(byte[] Parameter);
	public native static int PPPP_DeInitialize();
	public native static int PPPP_NetworkDetect(st_PPPP_NetInfo NetInfo, int UDP_Port);
	public native static int PPPP_NetworkDetectByServer(st_PPPP_NetInfo NetInfo, int UDP_Port, String ServerString);
	public native static int PPPP_Share_Bandwidth(byte bOnOff);
	public native static int PPPP_Listen(String MyID, int TimeOut_sec, int UDP_Port, byte bEnableInternet);
	public native static int PPPP_Listen_Break();
	public native static int PPPP_LoginStatus_Check(byte[] bLoginStatus);
	public native static int PPPP_Connect(String TargetID, byte bEnableLanSearch, int UDP_Port);
	public native static int PPPP_ConnectByServer(String TargetID, byte bEnableLanSearch, int UDP_Port, String ServerString);
	public native static int PPPP_Connect_Break();
	public native static int PPPP_Check(int SessionHandle, st_PPPP_Session SInfo);
	public native static int PPPP_Close(int SessionHandle);
	public native static int PPPP_ForceClose(int SessionHandle);
	public native static int PPPP_Write(int SessionHandle, byte Channel, byte[] DataBuf, int DataSizeToWrite);
	public native static int PPPP_Read(int SessionHandle, byte Channel, byte[] DataBuf, int[] DataSize, int TimeOut_ms);
	public native static int PPPP_Check_Buffer(int SessionHandle, byte Channel, int[] WriteSize, int[] ReadSize);
	
	static { try {	System.loadLibrary("PPPP_API");
					ms_verAPI=PPPP_GetAPIVersion();
				}catch(UnsatisfiedLinkError ule){ System.out.println("loadLibrary PPPP_API lib,"+ule.getMessage()); }
			}
}
