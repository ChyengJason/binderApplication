package com.jscheng.binderapplication.binderpool;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

/**
 * Created By Chengjunsen on 2018/12/8
 */
public class BinderPool {
    private static BinderPool mInstance;
    private Context mContext;
    private IBinderPool mBinderService;

    private BinderPool(Context context) {
        this.mContext = context.getApplicationContext();
        connectBinderPoolService();
    }

    public static BinderPool getInstance(Context context) {
        if (mInstance == null) {
            synchronized (BinderPool.class) {
                if (mInstance == null) {
                    mInstance = new BinderPool(context);
                }
            }
        }
        return mInstance;
    }

    private synchronized void connectBinderPoolService() {
        Intent service = new Intent(mContext, BinderPoolService.class);
        mContext.bindService(service, mBinderPoolConnection, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection mBinderPoolConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            try {
                mBinderService = IBinderPool.Stub.asInterface(service);
                mBinderService.asBinder().linkToDeath(mBinderPoolDeathRecipient, 0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private IBinder.DeathRecipient mBinderPoolDeathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            if (mBinderService != null && mBinderService.asBinder().isBinderAlive()) {
                mBinderService.asBinder().unlinkToDeath(this, 0);
                mBinderService = null;
                connectBinderPoolService();
            }
        }
    };

    // 运行在客户端
    public IBinder queryBinder(int binderCode) {
        if ( mBinderService != null ){
            try {
                return mBinderService.queryBinder(binderCode);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
