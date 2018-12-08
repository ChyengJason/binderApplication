package com.jscheng.binderapplication;

import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;

/**
 * Created By Chengjunsen on 2018/12/8
 */
public interface MyIBookListener extends IInterface{
    String DESTRIPTOR = "com.jscheng.binderapplication.MyIBookManager";

    int TRANSACTION_onNewBookArrived = IBinder.FIRST_CALL_TRANSACTION + 0;

    void onNewBookArrived(Book newBook) throws RemoteException;
}
