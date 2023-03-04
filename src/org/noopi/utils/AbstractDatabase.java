package org.noopi.utils;

import java.util.HashMap;

import javax.swing.event.EventListenerList;

import org.noopi.utils.events.database.DatabaseRegisterEvent;
import org.noopi.utils.events.database.DatabaseUnregisterEvent;
import org.noopi.utils.exceptions.DatabaseDuplicateException;
import org.noopi.utils.exceptions.DatabaseMissingEntryException;
import org.noopi.utils.listeners.database.DatabaseRegisterEventListener;
import org.noopi.utils.listeners.database.DatabaseUnregisterEventListener;

public abstract class AbstractDatabase<R, T> implements IMutableDatabase<R, T> {
  private HashMap<R, T> database;

  private EventListenerList listenerList;
  private DatabaseRegisterEvent<T> registerEvent;
  private DatabaseUnregisterEvent<T> unregisterEvent;

  public AbstractDatabase() {
    database = new HashMap<>();
    listenerList = new EventListenerList();
  }

  @Override
  public boolean contains(R name) {
    return database.containsKey(name);
  }

  @Override
  public T get(R name) {
    return database.get(name);
  }

  @Override
  public final T registerEntry(R name) throws DatabaseDuplicateException {
    if (database.containsKey(name)) {
      throw new DatabaseDuplicateException();
    }
    T t = createEntry(name);
    database.put(name, t);
    fireStateRegisteredEvent(t);
    return t;
  }

  @Override
  public void unregisterEntry(R name) throws DatabaseMissingEntryException
  {
    if (!database.containsKey(name)) {
      throw new DatabaseMissingEntryException();
    }
    T s = database.get(name);
    database.remove(name);
    fireStateUnRegisteredEvent(s);
  }

  @Override
  public void addDatabaseRegisterEventListener(
    DatabaseRegisterEventListener<T> l
  ) {
    assert l != null;
    listenerList.add(DatabaseRegisterEventListener.class, l);
  }

  @Override
  public void addDatabaseUnregisterEventListener(
    DatabaseUnregisterEventListener<T> l
  ) {
    assert l != null;
    listenerList.add(DatabaseUnregisterEventListener.class, l);
  }

  @Override
  @SuppressWarnings("unchecked")
  public T[] values() {
    return (T[]) database.values().toArray();
  }

  @Override
  @SuppressWarnings("unchecked")
  public R[] entries() {
    return (R[]) database.keySet().toArray();
  }

  public IDatabase<R, T> toReadable() {
    return (IDatabase<R, T>) this;
  }

  @SuppressWarnings("unchecked")
  protected void fireStateRegisteredEvent(T s) {
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
      ((DatabaseRegisterEventListener<T>) listeners[i + 1])
        .onRegisterEvent(registerEvent);
    }
  }

  @SuppressWarnings("unchecked")
  protected void fireStateUnRegisteredEvent(T s) {
    Object[] listeners = listenerList.getListenerList();
    boolean b = false;
    for (int i = listeners.length - 2; i >= 0; i -= 2) {
      if (listeners[i] != DatabaseUnregisterEventListener.class) {
        continue;
      }
      if (unregisterEvent == null || b) {
        unregisterEvent = new DatabaseUnregisterEvent<T>(s);
        b = true;
      }
      ((DatabaseUnregisterEventListener<T>) listeners[i + 1])
        .onUnregisterEvent(unregisterEvent);
    }
  }

  public int size() {
    return database.size();
  }

  protected abstract T createEntry(R name);
}
