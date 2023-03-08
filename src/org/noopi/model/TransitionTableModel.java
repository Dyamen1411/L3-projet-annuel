package org.noopi.model;

import java.util.HashMap;

import javax.swing.event.EventListenerList;

import org.noopi.utils.IDatabase;
import org.noopi.utils.MachineAction;
import org.noopi.utils.State;
import org.noopi.utils.Symbol;
import org.noopi.utils.Transition;
import org.noopi.utils.events.database.DatabaseRegisterEvent;
import org.noopi.utils.events.database.DatabaseUnregisterEvent;
import org.noopi.utils.listeners.TransitionTableUpdatedEventListener;
import org.noopi.utils.listeners.database.DatabaseRegisterEventListener;
import org.noopi.utils.listeners.database.DatabaseUnregisterEventListener;

public class TransitionTableModel {
  
  private HashMap<Transition.Left, Transition.Right> table;
  private IDatabase<String, Symbol> symbols;
  private IDatabase<String, State> states;

  private EventListenerList listenerList;

  public TransitionTableModel(
    IDatabase<String, Symbol> symbols,
    IDatabase<String, State> states
  ) {
    assert symbols != null;
    assert states != null;
    table = new HashMap<>();
    listenerList = new EventListenerList();

    symbols.addDatabaseRegisterEventListener(
      new DatabaseRegisterEventListener<Symbol>() {
        @Override
        public void onRegisterEvent(DatabaseRegisterEvent<Symbol> e) {
          Symbol s = e.getValue();
          if (symbols.contains(s.toString())) {
            // TODO: register error ?
            return;
          }
          for (State state : states.values()) {
            Transition t = new Transition(
              state,
              s,
              MachineAction.MACHINE_STOP,
              state,
              s
            );
            table.put(t.toLeft(), t.toRight());
          }
          fireTableUpdated();
        }
      }
    );

    symbols.addDatabaseUnregisterEventListener(
      new DatabaseUnregisterEventListener<Symbol>() {
        @Override
        public void onUnregisterEvent(DatabaseUnregisterEvent<Symbol> e) {
          Symbol s = e.getValue();
          if (!symbols.contains(s.toString())) {
            // TODO: register error ?
            return;
          }
          for (State state : states.values()) {
            table.remove(new Transition.Left(s, state));
          }
          fireTableUpdated();
        }
      }
    );

    states.addDatabaseRegisterEventListener(
      new DatabaseRegisterEventListener<State>() {
        @Override
        public void onRegisterEvent(DatabaseRegisterEvent<State> e) {
          State s = e.getValue();
          if (states.contains(s.toString())) {
            // TODO: register error ?
            return;
          }
          for (Symbol symbol : symbols.values()) {
            Transition t = new Transition(
              s,
              symbol,
              MachineAction.MACHINE_STOP,
              s,
              symbol
            );
            table.put(t.toLeft(), t.toRight());
          }
          fireTableUpdated();
        }
      }
    );

    states.addDatabaseUnregisterEventListener(
      new DatabaseUnregisterEventListener<State>() {
        @Override
        public void onUnregisterEvent(DatabaseUnregisterEvent<State> e) {
          State s = e.getValue();
          if (!states.contains(s.toString())) {
            // TODO: register error ?
            return;
          }
          for (Symbol symbol : symbols.values()) {
            table.remove(new Transition.Left(symbol, s));
          }
          fireTableUpdated();
        }
      }
    );
  }

  public Transition.Right getTransition(Transition.Left k) {
    return table.get(k);
  }

  public void update(Transition t) {
    table.put(t.toLeft(), t.toRight());
  }

  public void addTableUpdatedEventListener(
    TransitionTableUpdatedEventListener l
  ) {
    assert l != null;
    listenerList.add(TransitionTableUpdatedEventListener.class, l);
  }

  protected void fireTableUpdated() {
    Object[] listeners = listenerList.getListenerList();
    for (int i = listeners.length - 2; i >= 0; i -= 2) {
      if (listeners[i] != TransitionTableUpdatedEventListener.class) {
        continue;
      }
      ((TransitionTableUpdatedEventListener) listeners[i + 1]).onTableUpdated();
    }
  }

}
