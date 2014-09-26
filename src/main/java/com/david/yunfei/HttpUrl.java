package com.david.yunfei;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.EditText;

/**
 *  网络常量
 */
public class HttpUrl {

    public static final String SHARE_FILE="url_file";


    public static final  String DEFAULT_IPAddress="192.168.1.103";
    public static final String DEFAULT_IPPort="8080";
    public static final String DEFAULT_ServiceName="Yunfei";

    public static   String IPAddress="192.168.1.103";
    public static String IPPort="8080";
    public static String ServiceName="Yunfei";

    private static Context mContext;

    public static void init(Context context)
    {
        mContext=context;
       SharedPreferences sf= context.getSharedPreferences(SHARE_FILE, Context.MODE_PRIVATE);
        String ip=sf.getString(DEFAULT_IPAddress,""  );
        if(ip=="")
        {
           SharedPreferences.Editor text= sf.edit();
            text.putString(DEFAULT_IPAddress,IPAddress);
            text.putString(DEFAULT_IPPort,IPPort);
            text.putString(DEFAULT_ServiceName,ServiceName);
            text.commit();

        }


        IPAddress=sf.getString(DEFAULT_IPAddress,"");
        IPPort=sf.getString(DEFAULT_IPPort,"");
        ServiceName=sf.getString(DEFAULT_ServiceName,"");


    }


    public static void reset(String ip, String port, String service) {


        IPAddress =ip;
        IPPort=port;
        ServiceName=service;
        SharedPreferences sf= mContext.getSharedPreferences(SHARE_FILE, Context.MODE_PRIVATE);


            SharedPreferences.Editor text= sf.edit();
            text.putString(DEFAULT_IPAddress,IPAddress);
            text.putString(DEFAULT_IPPort,IPPort);
            text.putString(DEFAULT_ServiceName,ServiceName);
            text.commit();


    }
}
