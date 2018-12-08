package com.jscheng.binderapplication.binderpool;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

/**
 * Created By Chengjunsen on 2018/12/8
 */
public class BinderPoolService extends Service {
    public static final int BINDER_COMPUTE = 1;
    public static final int BINDER_LOG = 2;

    private Binder mBinderPool = new BinderPoolImpl();
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinderPool;
    }

    public static class BinderPoolImpl extends IBinderPool.Stub {

        private Binder mComputer = new Computer();
        private Binder mLogger = new Logger();

        // 运行在服务端
        @Override
        public IBinder queryBinder(int binderCode) throws RemoteException {
            switch (binderCode) {
                case BINDER_COMPUTE:
                    return mComputer;
                case BINDER_LOG:
                    return mLogger;
                default:
                    return null;
            }
        }
    }
}
