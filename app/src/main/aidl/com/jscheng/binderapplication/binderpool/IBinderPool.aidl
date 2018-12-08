// IBinderPool.aidl
package com.jscheng.binderapplication.binderpool;

// Declare any non-default types here with import statements

interface IBinderPool {
    IBinder queryBinder(int binderCode);
}
