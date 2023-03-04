package org.noopi.utils;

import java.util.HashMap;
import java.util.Set;
import java.util.Map.Entry;

import javax.swing.event.EventListenerList;

import org.noopi.utils.events.database.DatabaseRegisterEvent;
import org.noopi.utils.events.database.DatabaseUnregisterEvent;
import org.noopi.utils.exceptions.DatabaseDuplicateException;
import org.noopi.utils.exceptions.DatabaseMissingEntryException;
import org.noopi.utils.listeners.database.DatabaseRegisterEventListener;
import org.noopi.utils.listeners.database.DatabaseUnregisterEventListener;

public class SymbolDatabase implements IDatabase<String, Symbol> {
  private HashMap<String, Symbol> symbols;

  private EventListenerList listenerList;
  private DatabaseRegisterEvent<Symbol> registerEvent;
  private DatabaseUnregisterEvent<Symbol> unregisterEvent;

  public SymbolDatabase() {
    symbols = new HashMap<>();
    listenerList = new EventListenerList();
  }

  public boolean contains(String name) {
    return symbols.containsKey(name);
  }

  @Override
  public Symbol registerEntry(String name) throws DatabaseDuplicateException {
    if (symbols.containsKey(name)) {
      throw new DatabaseDuplicateException();
    }
    Symbol s = new Symbol(name);
    symbols.put(name, s);
    fireSymbolRegisteredEvent(s);
    return s;
  }

  @Override
  public void unregisterEntry(String name) throws DatabaseMissingEntryException
  {
    if (!symbols.containsKey(name)) {
      throw new DatabaseMissingEntryException();
    }
    Symbol s = symbols.get(name);
    symbols.remove(name);
    fireSymbolUnRegisteredEvent(s);
  }

  @Override
  public void addDatabaseRegisterEventListener(
    DatabaseRegisterEventListener<Symbol> l
  ) {
    assert l != null;
    listenerList.add(DatabaseRegisterEventListener.class, l);
  }

  @Override
  public void addDatabaseUnregisterEventListener(
    DatabaseUnregisterEventListener<Symbol> l
  ) {
    assert l != null;
    listenerList.add(DatabaseUnregisterEventListener.class, l);
  }

  protected void fireSymbolRegisteredEvent(Symbol s) {
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
      ((DatabaseRegisterEventListener<Symbol>) listeners[i + 1])
        .onRegisterEvent(registerEvent);
    }
  }

  protected void fireSymbolUnRegisteredEvent(Symbol s) {
    Object[] listeners = listenerList.getListenerList();
    boolean b = false;
    for (int i = listeners.length - 2; i >= 0; i -= 2) {
      if (listeners[i] != DatabaseUnregisterEventListener.class) {
        continue;
      }
      if (unregisterEvent == null || b) {
        unregisterEvent = new DatabaseUnregisterEvent<Symbol>(s);
        b = true;
      }
      ((DatabaseUnregisterEventListener<Symbol>) listeners[i + 1])
        .onUnregisterEvent(unregisterEvent);
    }
  }

  @Override
  public Symbol[] values() {
    return symbols.values().toArray(new Symbol[0]);
  }

  @Override
  public String[] entries() {
    return symbols.keySet().toArray(new String[0]);
  }

  public IReadableDatabase<String, Symbol> toReadable() {
    return (IReadableDatabase<String, Symbol>) this;
  }

  @Override
  public int size() {
    return symbols.size();
  }
}
