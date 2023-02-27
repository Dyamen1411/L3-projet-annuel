package org.noopi.utils.events.database.symbol;

import org.noopi.utils.machine.Symbol;

public class SymbolRegisteredEvent {
  private final Symbol s;
  
  public SymbolRegisteredEvent(Symbol s) {
    this.s = s;
  }

  public Symbol getSymbol() {
    return s;
  }
}