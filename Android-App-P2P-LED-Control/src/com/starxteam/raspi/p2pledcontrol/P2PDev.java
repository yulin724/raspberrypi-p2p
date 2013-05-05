package com.starxteam.raspi.p2pledcontrol;

import android.os.Handler;
import android.os.Message;

import com.p2p.pppp_api.PPPP_APIs;
import com.p2p.pppp_api.st_PPPP_Session;

public class P2PDev {
	public static final int CODE_INFO_CONNECTING = 1;
	public static final int CODE_INFO_CONNECT_FAIL = 2;
	public static final int CODE_INFO_PPPP_CHECK_OK = 3;
	public static final int CODE_INFO_AV_ONLINENUM = 4;

	public static final int MAX_SIZE_BUF = 65536; // 64*1024;

	public static final byte CHANNEL_DATA = 1;
	public static final byte CHANNEL_IOCTRL = 2;

	String mDevUID = "";

	int m_handleSession = -1;
	volatile boolean m_bRunning = false;
	private static int m_nInitH264Decoder = -1;
	private static boolean m_bInitAudio = false;
	
	private Handler parentHandler;

	public P2PDev(Handler handler) {
		parentHandler = handler;
	}

	private boolean isNullField(String str) {
		if (str == null || str.length() == 0)
			return true;
		else
			return false;
	}

	public static final byte[] intToByteArray_Little(int value) {
		return new byte[] { (byte) value, (byte) (value >>> 8),
				(byte) (value >>> 16), (byte) (value >>> 24) };
	}

	public static int initAll() {
		String strPara = "EFGBFFBJKFJOGCJNFHHCFHEMGENHHBMHHLFGBKDFAMJLLDKHDHACDEPBGCLAIALDADMPKDDIODMEBOCNJLNDJJ";

		System.out.println("=====> Start to PPPP_Initalize()");
		int nRet = PPPP_APIs.PPPP_Initialize(strPara.getBytes());
		System.out.println("=====> PPPP_APIs.PPPP_Initialize ret=" + nRet);

		return nRet;
	}

	public static int deinitAll() {
		int nRet = PPPP_APIs.PPPP_DeInitialize();

		return nRet;
	}

	public boolean isConnected() {
		return (m_handleSession >= 0);
	}

	public int connectDev(String did) {
		mDevUID = did;

		if (isNullField(mDevUID))
			return -5000;
		if (m_handleSession < 0) {
			m_handleSession = PPPP_APIs.PPPP_Connect(mDevUID, (byte) 1, 0);
			if (m_handleSession < 0) {
				System.out.println("Session Connected!!!!"); 
				return m_handleSession;
			}
		}

		st_PPPP_Session SInfo = new st_PPPP_Session();
		if (PPPP_APIs.PPPP_Check(m_handleSession, SInfo) == PPPP_APIs.ERROR_PPPP_SUCCESSFUL) {
			String str;
			str = String.format("  ----Session Ready: -%s----",
					(SInfo.getMode() == 0) ? "P2P" : "RLY");
			System.out.println(str);
			str = String.format("  Socket: %d", SInfo.getSkt());
			System.out.println(str);
			str = String.format("  Remote Addr: %s:%d", SInfo.getRemoteIP(),
					SInfo.getRemotePort());
			System.out.println(str);
			str = String.format("  My Lan Addr: %s:%d", SInfo.getMyLocalIP(),
					SInfo.getMyLocalPort());
			System.out.println(str);
			str = String.format("  My Wan Addr: %s:%d", SInfo.getMyWanIP(),
					SInfo.getMyWanPort());
			System.out.println(str);
			str = String
					.format("  Connection time: %d", SInfo.getConnectTime());
			System.out.println(str);
			str = String.format("  DID: %s", SInfo.getDID());
			System.out.println(str);
			str = String.format("  I am : %s",
					(SInfo.getCorD() == 0) ? "Client" : "Device");
			System.out.println(str);

			Message msg = new Message();
			msg.what = P2PLedControlActivity.DEVICE_CONNECTED;
			msg.obj = (SInfo.getMode() == 0) ? "P2P" : "RLY";
			parentHandler.sendMessage(msg);
			
			return 0;

		} else {
			
			parentHandler.sendEmptyMessage(P2PLedControlActivity.DEVICE_CONNECT_FAILED);

			return -1;
		}
	}

	public int disconnDev() {
		int nRet = PPPP_APIs.ER_ANDROID_NULL;
		m_bRunning = false;

		if (m_handleSession >= 0) {
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			nRet = PPPP_APIs.PPPP_Close(m_handleSession);
			m_handleSession = -1;
		}

		return nRet;
	}

	public int write(byte[] DataBuf, int DataSizeToWrite) {

		PPPP_APIs.PPPP_Write(m_handleSession, (byte) 0, DataBuf,
				DataSizeToWrite);
		return DataSizeToWrite;
	}

}
