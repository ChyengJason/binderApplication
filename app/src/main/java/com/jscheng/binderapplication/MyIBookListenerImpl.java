package com.jscheng.binderapplication;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created By Chengjunsen on 2018/12/8
 */
public abstract class MyIBookListenerImpl extends Binder implements MyIBookListener{

    public MyIBookListenerImpl() {
        this.attachInterface(this,  DESTRIPTOR);
    }

    @Override
    protected boolean onTransact(int code, @NonNull Parcel data, @Nullable Parcel reply, int flags) throws RemoteException {
        switch (code) {
            case INTERFACE_TRANSACTION:
                reply.writeString(DESTRIPTOR);
                return true;
            case TRANSACTION_onNewBookArrived:
                if (data.readInt() > 0) {
                    Book book = Book.CREATOR.createFromParcel(data);
                    this.onNewBookArrived(book);
                }
                reply.writeNoException();
                return true;
            default:
                return super.onTransact(code, data, reply, flags);
        }
    }

    @Override
    public IBinder asBinder() {
        return this;
    }

    // 将inder转换为MyIBookListener（根据是否在同一个进程区分）
    public static MyIBookListener asInterface(IBinder obj) {
        if (obj == null) {
            return null;
        }
        IInterface iin = obj.queryLocalInterface(DESTRIPTOR);
        if ((iin != null) && (iin instanceof MyIBookManager)) {
            return (MyIBookListener)iin;
        }
        return new MyIBookListenerImpl.Proxy(obj);
    }

    private static class Proxy implements MyIBookListener {
        private IBinder mRemote;

        public Proxy(IBinder binder) {
            this.mRemote = binder;
        }

        @Override
        public void onNewBookArrived(Book newBook) throws RemoteException {
            Parcel data = Parcel.obtain();
            Parcel reply = Parcel.obtain();
            if (newBook != null) {
                data.writeInt(1);
                newBook.writeToParcel(data, 0);
            } else {
                data.writeInt(0);
            }
            mRemote.transact(TRANSACTION_onNewBookArrived, data, reply, 0);
            reply.readException();
            data.recycle();
            reply.recycle();
        }

        @Override
        public IBinder asBinder() {
            return mRemote;
        }
    }
}
