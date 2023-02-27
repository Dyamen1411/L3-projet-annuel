package org.noopi.utils.listeners.database.symbol;

import java.util.EventListener;

import org.noopi.utils.events.database.symbol.SymbolUnRegisteredEvent;

public interface SymbolUnRegisteredEventListener extends EventListener {
  void onSymbolUnRegistered(SymbolUnRegisteredEvent e);
}
