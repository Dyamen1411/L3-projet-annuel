package org.noopi.model.transition;

import java.util.HashMap;

import javax.swing.event.EventListenerList;

import org.noopi.model.database.IDatabase;
import org.noopi.model.machine.MachineAction;
import org.noopi.model.state.State;
import org.noopi.model.state.Symbol;
import org.noopi.utils.events.database.DatabaseRegisterEvent;
import org.noopi.utils.events.database.DatabaseUnregisterEvent;
import org.noopi.utils.listeners.TransitionTableUpdatedEventListener;
import org.noopi.utils.listeners.database.DatabaseRegisterEventListener;
import org.noopi.utils.listeners.database.DatabaseUnregisterEventListener;

public class TransitionTableModel {
  
  private HashMap<Transition.Left, Transition.Right> table;

  private EventListenerList listenerList;

  private final IDatabase<String, Symbol> symbols;
  private final IDatabase<String, State> states;

  public TransitionTableModel(
    IDatabase<String, Symbol> symbols,
    IDatabase<String, State> states
  ) {
    assert symbols != null;
    assert states != null;
    this.symbols = symbols;
    this.states = states;
    table = new HashMap<>();
    listenerList = new EventListenerList();

    symbols.addDatabaseRegisterEventListener(
      new DatabaseRegisterEventListener<Symbol>() {
        @Override
        public void onRegisterEvent(DatabaseRegisterEvent<Symbol> e) {
          Symbol s = e.getValue();
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

  public Transition.Right getTransition(Symbol symbol, State state) {
    return table.get(new Transition.Left(symbol, state));
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

  public IDatabase<String, Symbol> getSymbolDatabase() {
    return symbols;
  }

  public IDatabase<String, State> getStatesDatabase() {
    return states;
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
