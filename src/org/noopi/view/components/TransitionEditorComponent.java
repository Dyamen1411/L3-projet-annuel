package org.noopi.view.components;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.EventListenerList;

import org.noopi.utils.events.view.TransitionModifiedEvent;
import org.noopi.utils.listeners.view.TransitionModifiedEventListener;
import org.noopi.utils.machine.MachineAction;
import org.noopi.utils.machine.State;
import org.noopi.utils.machine.Symbol;
import org.noopi.utils.machine.Transition;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TransitionEditorComponent extends JPanel {

  // ATTRIBUTS

  private JButton confirmButton;
  private JComboBox<String> oldSymbolList;
  private JComboBox<String> oldStateList;
  private JComboBox<String> newSymbolList;
  private JComboBox<String> newStateList;
  private JComboBox<MachineAction> direction;
  
  private EventListenerList listenerList;
  private TransitionModifiedEvent event;

  // Constructeurs

  public TransitionEditorComponent(String actionName) {
    assert actionName != null;
    assert !actionName.equals("");
    initialize(actionName);
    createController();
    placeComponents();
    listenerList = new EventListenerList();
    event = null;
  }

  // Outils

  private void placeComponents() {
    // TODO: Use "borders" instread of spaces in both JLabels (~4 & ~7)
    setLayout(new GridLayout(1, 5));
    { //-
      add(confirmButton);
      add(new JLabel("          :"));
      add(oldSymbolList);
      add(oldStateList);
      add(new JLabel("       =>"));
      add(newSymbolList);
      add(newStateList);
      add(direction);
    } //-
  }

  private void initialize(String actionName) {
    confirmButton = new JButton(actionName);
    oldSymbolList = new JComboBox<>();
    oldStateList = new JComboBox<>();
    newSymbolList = new JComboBox<>();
    newStateList = new JComboBox<>();

    direction = new JComboBox<MachineAction>(MachineAction.values());
  }

  private void createController() {
    confirmButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        fireTransitionModifiedEvent(getTransition());
      }
    });
  }

  public void registerSymbol(String s) {
    assert s != null;
    oldSymbolList.addItem(s);
    newSymbolList.addItem(s);
  }

  public void unregisterSymbol(String s) {
    assert s != null;
    oldSymbolList.removeItem(s);
    newSymbolList.removeItem(s);
  }

  public void registerState(String s) {
    assert s != null;
    oldStateList.addItem(s);
    newStateList.addItem(s);
  }

  public void unregisterState(String s) {
    assert s != null;
    oldStateList.removeItem(s);
    newStateList.removeItem(s);
  }

  public void addTransitionModifiedEventListener(
    TransitionModifiedEventListener l
  ) {
    assert l != null;
    listenerList.add(TransitionModifiedEventListener.class, l);
  }

  private Transition getTransition() {
    State ost = new State((String) oldStateList.getSelectedItem());
    Symbol osy = new Symbol((String) oldSymbolList.getSelectedItem());
    MachineAction d = (MachineAction) direction.getSelectedItem();
    State nst = new State((String) newStateList.getSelectedItem());
    Symbol nsy = new Symbol((String) newSymbolList.getSelectedItem());
    return new Transition(ost, osy, d, nst, nsy);
  }

  protected void fireTransitionModifiedEvent(Transition t) {
    Object[] listeners = listenerList.getListenerList();
    boolean b = false;
    for (int i = listeners.length - 2; i >= 0; i -= 2) {
      if (listeners[i] == TransitionModifiedEventListener.class) {
        if (event == null || !b) {
          event = new TransitionModifiedEvent(t);
          b = true;
        }
        ((TransitionModifiedEventListener) listeners[i + 1])
          .onTransitionModified(event);
      }
    }
  }
}