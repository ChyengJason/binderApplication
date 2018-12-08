// IBookListener.aidl
package com.jscheng.binderapplication;

// Declare any non-default types here with import statements
import com.jscheng.binderapplication.Book;

interface IBookListener {
    void onNewBookArrived(in Book newBook);
}
