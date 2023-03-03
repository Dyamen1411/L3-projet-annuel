package org.noopi.view.components;

import javax.naming.OperationNotSupportedException;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.EventListenerList;

import org.noopi.utils.events.view.TransitionModifiedEvent;
import org.noopi.utils.listeners.view.TransitionModifiedEventListener;
import org.noopi.utils.MachineAction;
import org.noopi.utils.State;
import org.noopi.utils.Symbol;
import org.noopi.utils.Transition;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TransitionEditorComponent extends JPanel {

  // ATTRIBUTS

  private JButton confirmButton;
  private JTextField oldSymbolTextField;
  private JTextField oldStateTextField;
  private JTextField newSymbolTextField;
  private JTextField newStateTextField;
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
      add(oldSymbolTextField);
      add(oldStateTextField);
      add(new JLabel("       =>"));
      add(newSymbolTextField);
      add(newStateTextField);
      add(direction);
    } //-
  }

  private void initialize(String actionName) {
    confirmButton = new JButton(actionName);
    oldSymbolTextField = new HintableTextField("", "Symbole");
    oldStateTextField = new HintableTextField("", "Etat");
    newSymbolTextField = new HintableTextField("", "Symbole");
    newStateTextField = new HintableTextField("", "Etat");
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

  public void addTransitionModifiedEventListener(
    TransitionModifiedEventListener l
  ) {
    assert l != null;
    listenerList.add(TransitionModifiedEventListener.class, l);
  }

  private Transition getTransition() {
    // State ost = new State(oldStateTextField.getText());
    // Symbol osy = new Symbol(oldSymbolTextField.getText());
    // MachineAction d = (MachineAction) direction.getSelectedItem();
    // State nst = new State(newStateTextField.getText());
    // Symbol nsy = new Symbol(newSymbolTextField.getText());
    // return new Transition(ost, osy, d, nst, nsy);
    throw new UnsupportedOperationException("not implemented yet");
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