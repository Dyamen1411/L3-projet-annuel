package org.noopi.utils.machine;

import java.util.HashMap;

import javax.swing.event.EventListenerList;

import org.noopi.utils.events.database.state.StateRegisteredEvent;
import org.noopi.utils.events.database.state.StateUnRegisteredEvent;
import org.noopi.utils.exceptions.ExistingStateException;
import org.noopi.utils.exceptions.NonExistingStateException;
import org.noopi.utils.listeners.database.state.StateRegisteredEventListener;
import org.noopi.utils.listeners.database.state.StateUnRegisteredEventListener;

public class StateDatabase {
  private HashMap<String, State> states;

  private EventListenerList listenerList;
  private StateRegisteredEvent registerEvent;
  private StateUnRegisteredEvent unRegisterEvent;

  public StateDatabase() {
    states = new HashMap<>();
    listenerList = new EventListenerList();
  }

  public boolean contains(String name) {
    return states.containsKey(name);
  }

  public State createState(String name) throws ExistingStateException {
    if (states.containsKey(name)) {
      throw new ExistingStateException();
    }
    State s = new State(name);
    states.put(name, s);
    fireStateRegisteredEvent(s);
    return s;
  }

  public void deleteState(String name) throws NonExistingStateException {
    if (!states.containsKey(name)) {
      throw new NonExistingStateException();
    }
    State s = states.get(name);
    states.remove(name);
    fireStateUnRegisteredEvent(s);
  }

  public void addStateRegisteredEventListener(
    StateRegisteredEventListener l
  ) {
    assert l != null;
    listenerList.add(StateRegisteredEventListener.class, l);
  }

  public void addStateUnRegisteredEventListener(
    StateUnRegisteredEventListener l
  ) {
    assert l != null;
    listenerList.add(StateUnRegisteredEventListener.class, l);
  }

  protected void fireStateRegisteredEvent(State s) {
    Object[] listeners = listenerList.getListenerList();
    boolean b = false;
    for (int i = listeners.length - 2; i >= 0; i -= 2) {
      if (listeners[i] != StateRegisteredEventListener.class) {
        continue;
      }
      if (registerEvent == null || b) {
        registerEvent = new StateRegisteredEvent(s);
        b = true;
      }
      ((StateRegisteredEventListener) listeners[i + 1])
        .onStateRegistered(registerEvent);
    }
  }

  protected void fireStateUnRegisteredEvent(State s) {
    Object[] listeners = listenerList.getListenerList();
    boolean b = false;
    for (int i = listeners.length - 2; i >= 0; i -= 2) {
      if (listeners[i] != StateUnRegisteredEventListener.class) {
        continue;
      }
      if (unRegisterEvent == null || b) {
        unRegisterEvent = new StateUnRegisteredEvent(s);
        b = true;
      }
      ((StateUnRegisteredEventListener) listeners[i + 1])
        .onStateUnRegistered(unRegisterEvent);
    }
  }
}
