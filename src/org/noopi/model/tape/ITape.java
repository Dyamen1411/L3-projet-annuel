package org.noopi.model.tape;

import org.noopi.utils.listeners.tape.TapeMovedEventListener;
import org.noopi.utils.listeners.tape.TapeResetEventListener;
import org.noopi.utils.listeners.tape.TapeWriteEventListener;
import org.noopi.utils.machine.Symbol;

public interface ITape {
  void reset(Symbol defaultSymbol);
  void reset(Symbol defaultSymbol, Symbol[] symbols);

  void shiftRight();
  void shiftLeft();

  Symbol readSymbol();
  void writeSymbol(Symbol symbol);

  void addTapeResetEventListener(TapeResetEventListener l);
  void addTapeMovedEventListener(TapeMovedEventListener l);
  void addTapeWriteEventListener(TapeWriteEventListener l);

  void removeTapeResetEventListener(TapeResetEventListener l);
  void removeTapeMovedEventListener(TapeMovedEventListener l);
  void removeTapeWriteEventListener(TapeWriteEventListener l);
}
