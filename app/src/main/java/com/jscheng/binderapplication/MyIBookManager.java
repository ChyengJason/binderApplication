package com.jscheng.binderapplication;

import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;

import java.util.List;

/**
 * Created By Chengjunsen on 2018/12/8
 */
public interface MyIBookManager extends IInterface {

   String DESTRIPTOR = "com.jscheng.binderapplication.MyIBookManager";

   int TRANSACTION_getBookList = IBinder.FIRST_CALL_TRANSACTION + 0;

   int TRANSACTION_addBook = IBinder.FIRST_CALL_TRANSACTION + 1;

    int TRANSACTION_registerListener = IBinder.FIRST_CALL_TRANSACTION + 2;

    int TRANSACTION_unRegisterListener = IBinder.FIRST_CALL_TRANSACTION + 3;

   List<Book> getBookList() throws RemoteException;

   void addBook(Book book) throws RemoteException;

   void registerListener(MyIBookListener listener) throws RemoteException;

   void unRegisterListener(MyIBookListener listener) throws RemoteException;

}
