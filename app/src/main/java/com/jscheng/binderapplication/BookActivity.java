package com.jscheng.binderapplication;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

public class BookActivity extends AppCompatActivity {
    private MyIBookManager mProxy;

    private MyIBookListener myIBookListener = new MyIBookListenerImpl() {
        @Override
        public void onNewBookArrived(Book book) throws RemoteException {
            Log.e("CJS", Process.myPid() + "client onNewBookArrived: "  + book.getId() + " : " + book.getName()  );
        }
    };

    private IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            new IBinder.DeathRecipient() {
                @Override
                public void binderDied() {
                    if(mProxy == null) {
                        return;
                    }
                    mProxy.asBinder().unlinkToDeath(this,0);
                    mProxy = null;
                    // 重新连接service
                    Intent intent = new Intent(BookActivity.this, MyBookManagerService.class);
                    bindService(intent, mServiceConn, Context.BIND_AUTO_CREATE);
                }
            };
        }
    };

    private ServiceConnection mServiceConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mProxy = MyBookManagerImpl.asInterface(service);
            try {
                mProxy.asBinder().linkToDeath(mDeathRecipient, 0);
                mProxy.registerListener(myIBookListener);
                mProxy.addBook(new Book(100, "myBook"));
                List<Book> books = mProxy.getBookList();
                for (Book book : books) {
                    Log.e("CJS", "client " + Process.myPid() + " onServiceConnected: " + book.getId() + " : " + book.getName() );
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mProxy = null;
        }

        @Override
        public void onBindingDied(ComponentName name) {

        }

        @Override
        public void onNullBinding(ComponentName name) {

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        Intent intent = new Intent(this, MyBookManagerService.class);
        bindService(intent, mServiceConn, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        if (mProxy != null && mProxy.asBinder().isBinderAlive()) {
            Log.e("CJS", "onDestroy: unregister");
            try {
                mProxy.unRegisterListener(myIBookListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        unbindService(mServiceConn);
        super.onDestroy();
    }
}
