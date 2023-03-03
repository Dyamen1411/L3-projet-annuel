package org.noopi.utils;

import java.util.HashMap;

import javax.swing.event.EventListenerList;

import org.noopi.utils.events.database.DatabaseRegisterEvent;
import org.noopi.utils.events.database.DatabaseUnregisterEvent;
import org.noopi.utils.exceptions.DatabaseDuplicateException;
import org.noopi.utils.exceptions.DatabaseMissingEntryException;
import org.noopi.utils.listeners.database.DatabaseRegisterEventListener;
import org.noopi.utils.listeners.database.DatabaseUnregisterEventListener;

public class StateDatabase implements IDatabase<String, State> {
  private HashMap<String, State> states;

  private EventListenerList listenerList;
  private DatabaseRegisterEvent<State> registerEvent;
  private DatabaseUnregisterEvent<State> unregisterEvent;

  public StateDatabase() {
    states = new HashMap<>();
    listenerList = new EventListenerList();
  }

  public boolean contains(String name) {
    return states.containsKey(name);
  }

  @Override
  public State registerEntry(String name) throws DatabaseDuplicateException {
    if (states.containsKey(name)) {
      throw new DatabaseDuplicateException();
    }
    State s = new State(name);
    states.put(name, s);
    fireStateRegisteredEvent(s);
    return s;
  }

  @Override
  public void unregisterEntry(String name) throws DatabaseMissingEntryException
  {
    if (!states.containsKey(name)) {
      throw new DatabaseMissingEntryException();
    }
    State s = states.get(name);
    states.remove(name);
    fireStateUnRegisteredEvent(s);
  }

  @Override
  public void addDatabaseRegisterEventListener(
    DatabaseRegisterEventListener<State> l
  ) {
    assert l != null;
    listenerList.add(DatabaseRegisterEventListener.class, l);
  }

  @Override
  public void addDatabaseUnregisterEventListener(
    DatabaseUnregisterEventListener<State> l
  ) {
    assert l != null;
    listenerList.add(DatabaseUnregisterEventListener.class, l);
  }

  protected void fireStateRegisteredEvent(State s) {
    Object[] listeners = listenerList.getListenerList();
    boolean b = false;
    for (int i = listeners.length - 2; i >= 0; i -= 2) {
      if (listeners[i] != DatabaseRegisterEventListener.class) {
        continue;
      }
      if (registerEvent == null || b) {
        registerEvent = new DatabaseRegisterEvent<>(s);
        b = true;
      }
      ((DatabaseRegisterEventListener<State>) listeners[i + 1])
        .onRegisterEvent(registerEvent);
    }
  }

  protected void fireStateUnRegisteredEvent(State s) {
    Object[] listeners = listenerList.getListenerList();
    boolean b = false;
    for (int i = listeners.length - 2; i >= 0; i -= 2) {
      if (listeners[i] != DatabaseUnregisterEventListener.class) {
        continue;
      }
      if (unregisterEvent == null || b) {
        unregisterEvent = new DatabaseUnregisterEvent<State>(s);
        b = true;
      }
      ((DatabaseUnregisterEventListener<State>) listeners[i + 1])
        .onUnregisterEvent(unregisterEvent);
    }
  }

  @Override
  public State[] values() {
    return states.values().toArray(new State[0]);
  }

  @Override
  public String[] entries() {
    return states.entrySet().toArray(new String[0]);
  }
}
