package org.apache.lucene.luke.models.tools;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.AnalyzerWrapper;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.SortedSetDocValuesField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.luke.app.desktop.components.AnalysisPanelProvider;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.BytesRef;
import org.junit.Before;
import org.junit.Test;
import org.apache.lucene.luke.models.documents.DocumentsTestBase;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.junit.Assert.*;

public class IndexToolsImplTest extends DocumentsTestBase {
    IndexToolsImpl imp;
    PrintStream ps;
    Document doc1, doc2;
    Analyzer analyzer = new StandardAnalyzer();
    Directory directory;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        imp = new IndexToolsImpl(reader, true, true);
        //ps = new PrintStream(System.out);

        FieldType titleType = new FieldType();
        titleType.setIndexOptions(IndexOptions.DOCS_AND_FREQS);
        titleType.setStored(true);
        titleType.setTokenized(true);
        titleType.setOmitNorms(true);

        FieldType authorType = new FieldType();
        authorType.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS);
        authorType.setStored(true);
        authorType.setTokenized(true);
        authorType.setOmitNorms(false);

        FieldType textType = new FieldType();
        textType.setIndexOptions(IndexOptions.DOCS_AND_FREQS);
        textType.setStored(false);
        textType.setTokenized(true);
        textType.setStoreTermVectors(true);
        textType.setOmitNorms(false);

        FieldType downloadsType = new FieldType();
        downloadsType.setDimensions(1, Integer.BYTES);
        downloadsType.setStored(true);

        doc1 = new Document();
        doc1.add(newField("title", "Pride and Prejudice", titleType));
        doc1.add(newField("author", "Jane Austen", authorType));
        doc1.add(newField("text",
                "It is a truth universally acknowledged, that a single man in possession of a good fortune, must be in want of a wife.",
                textType));
        doc1.add(new SortedSetDocValuesField("subject", new BytesRef("Fiction")));
        doc1.add(new SortedSetDocValuesField("subject", new BytesRef("Love stories")));
        doc1.add(new Field("downloads", packInt(28533), downloadsType));

        doc2 = new Document();
        doc2.add(newField("testAdd", "Test add", titleType));
        doc2.add(newField("title", "Pride and Prejudice", titleType));
        doc2.add(newField("author", "Jane Austen", authorType));
        doc2.add(newField("text",
                "It is a truth universally acknowledged, that a single man in possession of a good fortune, must be in want of a wife.",
                textType));
        doc2.add(new SortedSetDocValuesField("subject", new BytesRef("Fiction")));
        doc2.add(new SortedSetDocValuesField("subject", new BytesRef("Love stories")));
        doc2.add(new Field("downloads", packInt(28533), downloadsType));


        directory = ((DirectoryReader) reader).directory();
    }

    @Test
    public void checkIndex() {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ps = new PrintStream(baos, true, "UTF-8");
            imp.checkIndex(ps);
            String data = new String(baos.toByteArray(), StandardCharsets.UTF_8);
            assertTrue(data.contains("open reader.........OK"));
            assertTrue(data.contains("check integrity.....OK"));
            assertTrue(data.contains("check live docs.....OK"));
            assertTrue(data.contains("field infos.........OK"));
            assertTrue(data.contains("field norms.........OK"));
            assertTrue(data.contains("stored fields.......OK"));
            assertTrue(data.contains("term vectors........OK"));
        } catch (UnsupportedEncodingException e) {
            fail();
        }

    }

    @Test
    public void addDocument() {
        imp.addDocument(doc1, analyzer);
        boolean added = false;
        try {
            for (String s : directory.listAll()) {
                if (s.equals("segments_2")) {
                    added = true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertTrue(added);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addDocumentFailed() {
        imp.addDocument(null, analyzer);
    }
}
