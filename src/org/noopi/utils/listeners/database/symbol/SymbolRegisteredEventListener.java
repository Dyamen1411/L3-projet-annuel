package org.noopi.utils.listeners.database.symbol;

import java.util.EventListener;

import org.noopi.utils.events.database.symbol.SymbolRegisteredEvent;

public interface SymbolRegisteredEventListener extends EventListener {
  void onSymbolRegistered(SymbolRegisteredEvent e);
}
