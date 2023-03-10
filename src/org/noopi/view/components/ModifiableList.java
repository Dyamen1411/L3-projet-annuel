package org.noopi.view.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.VetoableChangeListener;
import java.beans.VetoableChangeSupport;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.EventListenerList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.noopi.utils.events.view.ElementAddedEvent;
import org.noopi.utils.events.view.ElementRemovedEvent;
import org.noopi.utils.listeners.view.ElementAddedEventListener;
import org.noopi.utils.listeners.view.ElementRemovedEventListener;

public class ModifiableList extends JPanel {

  private static final int FIELD_DISPLAYABLE_WIDTH = 15;
  private static final String PROPERTY_ADD_EVENT = "A";
  private static final String PROPERTY_REM_EVENT = "R";

  private HintableTextField field;
  private JButton addButton;
  private JButton removeButton;
  
  private DefaultListModel<String> model;
  private JList<String> list;

  private VetoableChangeSupport vcs;

  private EventListenerList listenerList;
  private ElementAddedEvent addEvent;
  private ElementRemovedEvent removeEvent;

  public ModifiableList(String hint, String addButtonText, String removeButtonText) {
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

    field = new HintableTextField("", hint, FIELD_DISPLAYABLE_WIDTH);
    addButton = new JButton(addButtonText);
    removeButton = new JButton(removeButtonText);
    model = new DefaultListModel<>();
    list = new JList<>();
    list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    listenerList = new EventListenerList();
    vcs = new VetoableChangeSupport(this);

    list.setModel(model);
    setButtonsEnabled(false);

    JPanel header = new JPanel();
    header.add(field);
    header.add(addButton);
    header.add(removeButton);

    add(header);
    add(new JScrollPane(list));

    // TODO: bug. if too quick, text field does not update
    list.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
      @Override
      public void valueChanged(ListSelectionEvent e) {
        if (((ListSelectionModel) e.getSource()).isSelectionEmpty()) {
          return;
        }
        field.setText(model.get(e.getFirstIndex()));
        setButtonsEnabled(true);
      }
    });

    field.getDocument().addDocumentListener(new DocumentListener() {
      @Override
      public void changedUpdate(DocumentEvent e) {
      }
      @Override
      public void insertUpdate(DocumentEvent e) {
        setButtonsEnabled(true);
      }
      @Override
      public void removeUpdate(DocumentEvent e) {
        setButtonsEnabled(e.getLength() != 0);
      }
    });

    field.addKeyListener(new KeyListener() {

      @Override
      public void keyPressed(KeyEvent e) {
      }

      @Override
      public void keyReleased(KeyEvent e) {
      }

      @Override
      public void keyTyped(KeyEvent e) {
        if(e.getKeyChar() == '\n'){
          addRule();
        }
      }
      
    });

    addButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        addRule();
      }
    });

    removeButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String element = field.getText();
        if (model.removeElement(element)) {
          try {
            vcs.fireVetoableChange(
              new PropertyChangeEvent(this, PROPERTY_REM_EVENT, element, "")
            );
            fireElementRemovedEvent(element);
          } catch (Exception ex) {}
        }
      }
    });
  }

  public void pushElement(String e) {
    model.add(0, e);
  }

  public void addElements(String[] l) {
    // TODO: maybe can do better ?
    if (l == null) {
      return;
    }
    for (String e : l) {
      pushElement(e);
    }
  }

  public void setElements(String[] l) {
    clear();
    addElements(l);
  }

  public void clear() {
    model.clear();
  }

  public void addElementAddedEventListener(ElementAddedEventListener l) {
    assert l != null;
    listenerList.add(ElementAddedEventListener.class, l);
  }

  public void addElementRemovedEventListener(ElementRemovedEventListener l) {
    assert l != null;
    listenerList.add(ElementRemovedEventListener.class, l);
  }

  public void addElementAddedVetoableChangeListener(VetoableChangeListener l) {
    assert l != null;
    vcs.addVetoableChangeListener(PROPERTY_ADD_EVENT, l);
  }

  public void addElementRemovedVetoableChangeListener(VetoableChangeListener l) {
    assert l != null;
    vcs.addVetoableChangeListener(PROPERTY_REM_EVENT, l);
  }

  protected void fireElementAddedEvent(String s) {
    Object[] list = listenerList.getListenerList();
    boolean b = false;
    for (int i = list.length - 2; i >= 0; i -= 2) {
      if (list[i] != ElementAddedEventListener.class) {
        continue;
      }
      if (addEvent == null || !b) {
        addEvent = new ElementAddedEvent(s);
      }
      ((ElementAddedEventListener) list[i + 1]).onElementAdded(addEvent);
    }
  }

  protected void fireElementRemovedEvent(String s) {
    Object[] list = listenerList.getListenerList();
    boolean b = false;
    for (int i = list.length - 2; i >= 0; i -= 2) {
      if (list[i] != ElementRemovedEventListener.class) {
        continue;
      }
      if (removeEvent == null || !b) {
        removeEvent = new ElementRemovedEvent(s);
      }
      ((ElementRemovedEventListener) list[i + 1]).onElementRemoved(removeEvent);
    }
  }

  private void setButtonsEnabled(boolean enabled) {
    addButton.setEnabled(enabled);
    removeButton.setEnabled(enabled);
  }

  private void addRule(){
    String element = field.getText();
          if (model.contains(element) || element.equals("")) {
            return;
          }
          model.add(0, element);
          try {
            vcs.fireVetoableChange(
              new PropertyChangeEvent(this, PROPERTY_ADD_EVENT, "", element)
            );
            fireElementAddedEvent(field.getText());
          } catch (Exception ex) {
            
          }
  }
}
