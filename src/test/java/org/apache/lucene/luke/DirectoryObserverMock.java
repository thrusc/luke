package org.apache.lucene.luke;

import org.apache.lucene.luke.app.DirectoryObserver;
import org.apache.lucene.luke.app.LukeState;

public class DirectoryObserverMock implements DirectoryObserver {
    boolean isOpened = false;
    Exception throwExeption = null;

    @Override
    public void openDirectory(LukeState state) {
        if(isOpened) {
            throwExeption =  new Exception();
        } else {
            isOpened = true;
        }
    }

    @Override
    public void closeDirectory() {
        if(isOpened) {
            isOpened = false;
        } else {
            throwExeption = new Exception();
        }
    }

    public boolean isOpened(){
        return isOpened;

    }

    public boolean hasException() {
        return (null != throwExeption);
    }

}
