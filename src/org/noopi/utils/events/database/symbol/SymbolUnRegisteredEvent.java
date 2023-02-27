package org.noopi.utils.events.database.symbol;

import org.noopi.utils.machine.Symbol;

public class SymbolUnRegisteredEvent {
  private final Symbol s;
  
  public SymbolUnRegisteredEvent(Symbol s) {
    this.s = s;
  }

  public Symbol getSymbol() {
    return s;
  }
}