package org.noopi.model.tape;

import org.noopi.utils.machine.Symbol;

public interface ITape {
  void reset();
  void reset(Symbol[] symbols);

  void shiftRight();
  void shiftLeft();

  Symbol getCurrentSymbol();
}
