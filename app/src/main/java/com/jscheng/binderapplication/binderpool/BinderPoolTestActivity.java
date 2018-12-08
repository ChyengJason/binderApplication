package com.jscheng.binderapplication.binderpool;

import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.jscheng.binderapplication.R;

public class BinderPoolTestActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binder_pool_test);
        findViewById(R.id.btn).setOnClickListener(this);
        BinderPool.getInstance(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn) {
            try {
                test();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    private void test() throws RemoteException {
        IBinder compterBinder = BinderPool.getInstance(this).queryBinder(BinderPoolService.BINDER_COMPUTE);
        ICompute mComputer = ICompute.Stub.asInterface(compterBinder);
        int result = mComputer.add(1, 3);

        IBinder logBinder = BinderPool.getInstance(this).queryBinder(BinderPoolService.BINDER_LOG);
        ILog mLogger = ILog.Stub.asInterface(logBinder);
        mLogger.log(String.valueOf(result));
    }
}
