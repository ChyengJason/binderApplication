package com.jscheng.binderapplication;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created By Chengjunsen on 2018/12/8
 */
public class MyBookManagerService extends Service {
    private CopyOnWriteArrayList<Book> mBooks = new CopyOnWriteArrayList<>();
    private RemoteCallbackList<MyIBookListener> mListeners = new RemoteCallbackList<>();

    private Binder mBinder = new MyBookManagerImpl() {
        @Override
        public List<Book> getBookList() throws RemoteException {
            Log.e("CJS", Process.myPid() + " service getBookList: " + mBooks.size());
            return mBooks;
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            Log.e("CJS", Process.myPid() + " service addBook: " + mBooks.size());
            mBooks.add(book);
            int size = mListeners.beginBroadcast();
            for (int i = 0; i < size; i++) {
                mListeners.getBroadcastItem(i).onNewBookArrived(book);
            }
            mListeners.finishBroadcast();
        }

        @Override
        public void registerListener(MyIBookListener listener) throws RemoteException {
            mListeners.register(listener);
           int size = mListeners.beginBroadcast();
            mListeners.finishBroadcast();
            Log.e("CJS", Process.myPid() + " registerListener: " + size);
        }

        @Override
        public void unRegisterListener(MyIBookListener listener) throws RemoteException {
            mListeners.unregister(listener);
            int size = mListeners.beginBroadcast();
            mListeners.finishBroadcast();
            Log.e("CJS", Process.myPid() + " unregisterListener: " + size);
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mBooks.add(new Book(1, "book1"));
        mBooks.add(new Book(2, "book2"));
        mBooks.add(new Book(3, "book3"));
    }
}
