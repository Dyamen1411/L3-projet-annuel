package org.noopi.utils.events;

import org.noopi.utils.machine.Symbol;

public class TapeWriteEvent implements ILayoutEvent {
  private final Symbol s;

  public TapeWriteEvent(Symbol s) {
    assert s != null;
    this.s = s;
  }

  public Symbol getSymbol() {
    return s;
  }
}
