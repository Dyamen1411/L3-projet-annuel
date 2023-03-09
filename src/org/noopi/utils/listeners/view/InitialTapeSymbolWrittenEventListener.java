package org.noopi.utils.listeners.view;

import java.util.EventListener;

import org.noopi.utils.Symbol;

public interface InitialTapeSymbolWrittenEventListener extends EventListener {
  void onTapeWritten(Symbol s);
}
