package org.noopi.utils.listeners;

import java.util.EventListener;

import org.noopi.utils.events.TapeResetEvent;

public interface TapeResetEventListener extends EventListener {
  void onTapeReset(TapeResetEvent e);
}
