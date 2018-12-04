package org.apache.lucene.luke.app.desktop.components.dialog.documents;

import org.apache.lucene.luke.app.desktop.dto.documents.NewField;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;


public class FieldsTableModelTest {
    FieldsTableModel ftm;
    NewField tmpField, tmpFiled2;

    @Before
    public void setUp() throws Exception {
        List<NewField> newFieldList = new ArrayList<>();
        tmpField = NewField.newInstance();
        tmpField.setName("testName");
        tmpField.setType(String.class);
        tmpField.setValue("testValue");
        tmpField.setStored(true);
        newFieldList.add(tmpField);

        tmpFiled2 = NewField.newInstance();
        tmpFiled2.setName("testName2");
        tmpFiled2.setType(String.class);
        tmpFiled2.setValue("testValue2");
        tmpFiled2.setStored(true);
        newFieldList.add(tmpFiled2);

        ftm = new FieldsTableModel(newFieldList);
    }

    @Test
    public void getRowCount() {
        assertEquals(2,ftm.getRowCount());
    }

    @Test
    public void getColumnCount() {
        assertEquals(5,ftm.getColumnCount());
    }

    @Test
    public void getColumnName() {
        assertEquals("Del",ftm.getColumnName(0));
        assertEquals("Name",ftm.getColumnName(1));
        assertEquals("Type",ftm.getColumnName(2));
        assertEquals("Options",ftm.getColumnName(3));
        assertEquals("Value",ftm.getColumnName(4));

        assertEquals("",ftm.getColumnName(100));
    }

    @Test
    public void getColumnClass() {
        assertEquals(Boolean.class,ftm.getColumnClass(0));
        assertEquals(String.class,ftm.getColumnClass(1));
        assertEquals(Class.class,ftm.getColumnClass(2));
        assertEquals(String.class,ftm.getColumnClass(3));
        assertEquals(String.class,ftm.getColumnClass(4));
        assertEquals(Object.class,ftm.getColumnClass(100));
    }

    @Test
    public void getValueAt() {
        assertEquals("",ftm.getValueAt(0,3));
        assertEquals(null,ftm.getValueAt(0,4));
    }

    @Test
    public void isCellEditable() {
        assertEquals(true,ftm.isCellEditable(1,0));
        assertEquals(true,ftm.isCellEditable(1,1));
        assertEquals(true,ftm.isCellEditable(1,2));
        assertEquals(false,ftm.isCellEditable(1,3));
        assertEquals(true,ftm.isCellEditable(1,4));
    }

    @Test
    public void setValueAt() {
        ftm.setValueAt(true,1,0);
        assertEquals(true,ftm.getValueAt(1,0));

        ftm.setValueAt("NewEditedName",1,1);
        assertEquals("NewEditedName",ftm.getValueAt(1,1));

        ftm.setValueAt(Float.class,1,2);
        assertEquals(Float.class,ftm.getValueAt(1,2));

        ftm.setValueAt("NewEditedValue",1,4);
        assertEquals("NewEditedValue",ftm.getValueAt(1,4));
    }
}