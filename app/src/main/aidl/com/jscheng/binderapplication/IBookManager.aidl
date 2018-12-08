// IBokManager.aidl
package com.jscheng.binderapplication;

// Declare any non-default types here with import statements
import com.jscheng.binderapplication.Book;
import com.jscheng.binderapplication.IBookListener;

interface IBookManager {
    List<Book> getBookList();
    void addBook(in Book book);
    void registerBookListener(IBookListener listener);
    void unRegisterBookListener(IBookListener listener);
}
