package com.jscheng.binderapplication.binderpool;

import android.os.RemoteException;
import android.util.Log;

/**
 * Created By Chengjunsen on 2018/12/8
 */
public class Logger extends ILog.Stub{

    @Override
    public void log(String text) throws RemoteException {
        Log.e("CJS", text);
    }
}
