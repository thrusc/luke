package org.apache.lucene.luke.app.desktop.listeners;

import org.apache.lucene.luke.app.desktop.components.ComponentOperatorRegistry;
import org.apache.lucene.luke.app.desktop.components.DocumentsPanelProvider;
import org.apache.lucene.luke.app.desktop.components.SearchPanelProvider;
import org.apache.lucene.luke.app.desktop.components.TabbedPaneProvider;
import org.apache.lucene.luke.app.desktop.util.MessageUtils;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class DocumentsPanelListeners {

  private final DocumentsPanelProvider.Controller controller;

  private final TabbedPaneProvider.TabSwitcherProxy tabSwitcher;

  private final ComponentOperatorRegistry operatorRegistry;

  public DocumentsPanelListeners(DocumentsPanelProvider.Controller controller,
                                 TabbedPaneProvider.TabSwitcherProxy tabSwitcher,
                                 ComponentOperatorRegistry operatorRegistry) {
    this.controller = controller;
    this.tabSwitcher = tabSwitcher;
    this.operatorRegistry = operatorRegistry;
  }

  public ActionListener getFieldsCBListener() {
    return (ActionEvent e) -> controller.showFirstTerm();
  }

  public ActionListener getFirstTermBtnListener() {
    return (ActionEvent e) -> controller.showFirstTerm();
  }

  public ActionListener getTermTFListener() {
    return (ActionEvent e) -> controller.seekNextTerm();
  }

  public ActionListener getNextTermBtnListener() {
    return (ActionEvent e) -> controller.showNextTerm();
  }

  public ActionListener getFirstTermDocBtnListener() {
    return (ActionEvent e) -> controller.showFirstTermDoc();
  }

  public ActionListener getNextTermDocBtnListener() {
    return (ActionEvent e) -> controller.showNextTermDoc();
  }

  public ActionListener getMltSearchBtnListener() {
    return (ActionEvent e) -> {
      int docNum = controller.getMLTDocNum();
      operatorRegistry.get(SearchPanelProvider.SearchTabOperator.class).ifPresent(operator -> operator.mltSearch(docNum));
      tabSwitcher.switchTab(TabbedPaneProvider.Tab.SEARCH);
    };
  }

  public ChangeListener getDocNumSpnrListener() {
    return (ChangeEvent e) -> controller.showCurrentDoc();
  }

  public ActionListener getAddDocBtnListener() {
    return (ActionEvent e) -> controller.showAddDocumentDialog();
  }

  public MouseListener getDocumentTableListener() {
    return new MouseAdapter() {
      @Override
      public void mousePressed(MouseEvent e) {
        showPopupIfNeeded(e);
      }

      private void showPopupIfNeeded(MouseEvent e) {
        if (e.isPopupTrigger()) {
          createDocumentTablePopup().show(e.getComponent(), e.getX(), e.getY());
        }
      }
    };
  }

  private JPopupMenu createDocumentTablePopup() {
    JPopupMenu popup = new JPopupMenu();

    // show term vector
    JMenuItem item1 = new JMenuItem(MessageUtils.getLocalizedMessage("documents.doctable.menu.item1"));
    item1.addActionListener(e -> controller.showTermVectorDialog());
    popup.add(item1);

    // show doc values
    JMenuItem item2 = new JMenuItem(MessageUtils.getLocalizedMessage("documents.doctable.menu.item2"));
    item2.addActionListener(e -> controller.showDocValuesDialog());
    popup.add(item2);

    // show stored value
    JMenuItem item3 = new JMenuItem(MessageUtils.getLocalizedMessage("documents.doctable.menu.item3"));
    item3.addActionListener(e -> controller.showStoredValueDialog());
    popup.add(item3);

    // copy stored value to clipboard
    JMenuItem item4 = new JMenuItem(MessageUtils.getLocalizedMessage("documents.doctable.menu.item4"));
    item4.addActionListener(e -> controller.copyStoredValue());
    popup.add(item4);

    return popup;
  }
}
