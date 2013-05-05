package com.p2p.pppp_api;

public class st_PPPP_Session {
	int Skt=-1;
	byte[] 	RemoteIP	=new byte[16];
	int		RemotePort	=0;
	
	byte[] 	MyLocalIP	=new byte[16];
	int		MyLocalPort	=0;
	
	byte[]  MyWanIP		=new byte[16];
	int		MyWanPort	=0;
	
	int		ConnectTime	=0;
	byte[]	DID=new byte[24];
	byte	bCorD=0;
	byte	bMode=0;
	
	public static String bytes2Str(byte[] byts)
	{
    	String str="";
		int iLen=0;
		for(iLen=0; iLen<byts.length; iLen++){
			if(byts[iLen]==(byte)0) break;
		}		
		if(iLen==0) str="";
		else str=new String(byts,0,iLen);
		return str;
	}
	
	public int    getSkt()			{ return Skt;					}
	public String getRemoteIP()		{ return bytes2Str(RemoteIP); 	}
	public int	  getRemotePort()	{ return RemotePort;			}
	public String getMyLocalIP()	{ return bytes2Str(MyLocalIP); 	}
	public int	  getMyLocalPort()	{ return MyLocalPort;			}
	public String getMyWanIP()		{ return bytes2Str(MyWanIP); 	}
	public int	  getMyWanPort()	{ return MyWanPort;				}
	
	public int 	  getConnectTime()	{ return ConnectTime;			}
	public String getDID()			{ return bytes2Str(DID);		}
	public int 	  getCorD()			{ return (int)(bCorD&0xFF);		}
	public int	  getMode()			{ return (int)(bMode&0xFF);		}
	
	
	
}
