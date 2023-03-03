package org.noopi.utils.events.tape;

import org.noopi.utils.Symbol;

public class TapeWriteEvent {
  private final Symbol s;

  public TapeWriteEvent(Symbol s) {
    assert s != null;
    this.s = s;
  }

  public Symbol getSymbol() {
    return s;
  }
}
