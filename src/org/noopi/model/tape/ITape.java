package org.noopi.model.tape;

import org.noopi.utils.machine.Symbol;

public interface ITape {
  void reset(Symbol defaultSymbol);
  void reset(Symbol defaultSymbol, Symbol[] symbols);

  void shiftRight();
  void shiftLeft();

  Symbol readSymbol();
  void writeSymbol(Symbol symbol);
}
