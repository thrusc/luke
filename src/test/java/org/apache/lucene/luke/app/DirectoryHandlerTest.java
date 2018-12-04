package org.apache.lucene.luke.app;

import org.apache.lucene.luke.DirectoryObserverMock;
import org.apache.lucene.luke.models.documents.DocumentsTestBase;
import org.junit.Before;
import org.junit.Test;

public class DirectoryHandlerTest extends DocumentsTestBase {
    DirectoryHandler dh;
    DirectoryObserverMock dom;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        dh = new DirectoryHandler();
        dom = new DirectoryObserverMock();
        dh.addObserver(dom);
        dh.open("./","org.apache.lucene.store.FSDirectory");

    }

    @Test
    public void directoryOpened() {
        assertEquals(true,dh.directoryOpened());
        dh.close();
        assertEquals(false,dh.directoryOpened());
    }

    @Test
    public void open() {
        DirectoryHandler tmpdh = new DirectoryHandler();
        DirectoryObserverMock tmpfdo = new DirectoryObserverMock();
        tmpdh.addObserver(tmpfdo);
        tmpdh.open("./","org.apache.lucene.store.FSDirectory");
        assertEquals(tmpdh.directoryOpened(),tmpfdo.isOpened());
        tmpdh.close();
    }

    @Test
    public void close() {
        dh.close();
        assertFalse(dom.isOpened());
        assertFalse(dh.directoryOpened());
    }

    @Test
    public void getState() {
        if(dh.directoryOpened()) {
            assertNotNull(dh.getState());
        } else {
            dh.open("./","org.apache.lucene.store.FSDirectory");
            assertNotNull(dh.getState());
        }
    }
}