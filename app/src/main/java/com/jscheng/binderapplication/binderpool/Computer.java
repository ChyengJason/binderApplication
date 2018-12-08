package com.jscheng.binderapplication.binderpool;

import android.os.RemoteException;

/**
 * Created By Chengjunsen on 2018/12/8
 */
public class Computer extends ICompute.Stub {
    @Override
    public int add(int a, int b) throws RemoteException {
        return a + b;
    }

    @Override
    public int sub(int a, int b) throws RemoteException {
        return a - b;
    }
}
