package org.noopi.utils.machine;

import java.util.HashMap;

import javax.swing.event.EventListenerList;

import org.noopi.utils.events.database.symbol.SymbolRegisteredEvent;
import org.noopi.utils.events.database.symbol.SymbolUnRegisteredEvent;
import org.noopi.utils.exceptions.ExistingSymbolException;
import org.noopi.utils.exceptions.NonExistingSymbolException;
import org.noopi.utils.listeners.database.symbol.SymbolRegisteredEventListener;
import org.noopi.utils.listeners.database.symbol.SymbolUnRegisteredEventListener;

public class SymbolDatabase {
  private HashMap<String, Symbol> symbols;

  private EventListenerList listenerList;
  private SymbolRegisteredEvent registerEvent;
  private SymbolUnRegisteredEvent unRegisterEvent;

  public SymbolDatabase() {
    symbols = new HashMap<>();
    listenerList = new EventListenerList();
  }

  public boolean contains(String name) {
    return symbols.containsKey(name);
  }

  public Symbol createSymbol(String name) throws ExistingSymbolException {
    if (symbols.containsKey(name)) {
      throw new ExistingSymbolException();
    }
    Symbol s = new Symbol(name);
    symbols.put(name, s);
    fireSymbolRegisteredEvent(s);
    return s;
  }

  public void deleteSymbol(String name) throws NonExistingSymbolException {
    if (!symbols.containsKey(name)) {
      throw new NonExistingSymbolException();
    }
    Symbol s = symbols.get(name);
    symbols.remove(name);
    fireSymbolUnRegisteredEvent(s);
  }

  public void addSymbolRegisteredEventListener(
    SymbolRegisteredEventListener l
  ) {
    assert l != null;
    listenerList.add(SymbolRegisteredEventListener.class, l);
  }

  public void addSymbolUnRegisteredEventListener(
    SymbolUnRegisteredEventListener l
  ) {
    assert l != null;
    listenerList.add(SymbolUnRegisteredEventListener.class, l);
  }

  protected void fireSymbolRegisteredEvent(Symbol s) {
    Object[] listeners = listenerList.getListenerList();
    boolean b = false;
    for (int i = listeners.length - 2; i >= 0; i -= 2) {
      if (listeners[i] != SymbolRegisteredEventListener.class) {
        continue;
      }
      if (registerEvent == null || b) {
        registerEvent = new SymbolRegisteredEvent(s);
        b = true;
      }
      ((SymbolRegisteredEventListener) listeners[i + 1])
        .onSymbolRegistered(registerEvent);
    }
  }

  protected void fireSymbolUnRegisteredEvent(Symbol s) {
    Object[] listeners = listenerList.getListenerList();
    boolean b = false;
    for (int i = listeners.length - 2; i >= 0; i -= 2) {
      if (listeners[i] != SymbolUnRegisteredEventListener.class) {
        continue;
      }
      if (unRegisterEvent == null || b) {
        unRegisterEvent = new SymbolUnRegisteredEvent(s);
        b = true;
      }
      ((SymbolUnRegisteredEventListener) listeners[i + 1])
        .onSymbolUnRegistered(unRegisterEvent);
    }
  }
}
