package com.jscheng.binderapplication;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

/**
 * Created By Chengjunsen on 2018/12/8
 */
public abstract class MyBookManagerImpl extends Binder implements MyIBookManager {

    public MyBookManagerImpl() {
        this.attachInterface(this, DESTRIPTOR);
    }

    @Override
    public IBinder asBinder() {
        return this;
    }

    public static MyIBookManager asInterface(IBinder obj) {
        if (obj == null) {
            return null;
        }
        IInterface iin = obj.queryLocalInterface(DESTRIPTOR);
        if ((iin != null) && (iin instanceof MyIBookManager)) {
            return (MyIBookManager)iin;
        }
        return new MyBookManagerImpl.Proxy(obj);
    }

    @Override
    protected boolean onTransact(int code, @NonNull Parcel data, @Nullable Parcel reply, int flags) throws RemoteException {
        switch (code) {
            case INTERFACE_TRANSACTION:
                reply.writeString(DESTRIPTOR);
                return true;
            case TRANSACTION_addBook:
                data.enforceInterface(DESTRIPTOR);// 某种校验
                if (data.readInt() != 0) {// 判断参数
                    Book book = Book.CREATOR.createFromParcel(data);
                    this.addBook(book);
                }
                reply.writeNoException();
                return true;
            case TRANSACTION_getBookList:
                data.enforceInterface(DESTRIPTOR);
                List<Book> books = this.getBookList();
                reply.writeNoException();
                reply.writeTypedList(books);
                return true;
            case TRANSACTION_registerListener:
                data.enforceInterface(DESTRIPTOR);
                if (data.readInt() != 0) {
                    MyIBookListener listener = MyIBookListenerImpl.asInterface(data.readStrongBinder());
                    this.registerListener(listener);
                }
                return true;
            case TRANSACTION_unRegisterListener:
                data.enforceInterface(DESTRIPTOR);
                if (data.readInt() != 0) {
                    MyIBookListener listener = MyIBookListenerImpl.asInterface(data.readStrongBinder());
                    this.unRegisterListener(listener);
                }
                return true;
            default:
                return super.onTransact(code, data, reply, flags);
        }
    }

    private static class Proxy implements MyIBookManager {
        private IBinder mRemote;
        public Proxy(IBinder binder) {
            this.mRemote = binder;
        }

        @Override
        public List<Book> getBookList() throws RemoteException {
            Parcel data = Parcel.obtain();
            Parcel reply = Parcel.obtain();
            List<Book> list;
            data.writeInterfaceToken(DESTRIPTOR);// 校验，跟enforceInterface相对
            mRemote.transact(TRANSACTION_getBookList, data, reply, 0);
            reply.readException();
            list = reply.createTypedArrayList(Book.CREATOR);
            reply.recycle();
            data.recycle();
            return list;
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            Parcel data = Parcel.obtain();
            Parcel reply = Parcel.obtain();
            data.writeInterfaceToken(DESTRIPTOR);
            if (book != null) {
                data.writeInt(1);
                book.writeToParcel(data, 0);
            } else {
                data.writeInt(0);
            }
            mRemote.transact(TRANSACTION_addBook, data, reply, 0);
            reply.readException();
            data.recycle();
            reply.recycle();
        }

        @Override
        public void registerListener(MyIBookListener listener) throws RemoteException {
            Parcel data = Parcel.obtain();
            Parcel reply = Parcel.obtain();
            data.writeInterfaceToken(DESTRIPTOR);
            if (listener != null) {
                data.writeInt(1);
                IBinder listenerBinder = listener.asBinder();
                data.writeStrongBinder(listenerBinder);
            } else {
                data.writeInt(0);
            }
            mRemote.transact(TRANSACTION_registerListener, data, reply, 0);
            reply.readException();
            data.recycle();
            reply.recycle();
        }

        @Override
        public void unRegisterListener(MyIBookListener listener) throws RemoteException {
            Parcel data = Parcel.obtain();
            Parcel reply = Parcel.obtain();
            data.writeInterfaceToken(DESTRIPTOR);
            if (listener != null) {
                data.writeInt(1);
                IBinder listenerBinder = listener.asBinder();
                data.writeStrongBinder(listenerBinder);
            } else {
                data.writeInt(0);
            }
            mRemote.transact(TRANSACTION_unRegisterListener, data, reply, 0);
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
