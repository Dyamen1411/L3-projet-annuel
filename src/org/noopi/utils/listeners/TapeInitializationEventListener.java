package org.noopi.utils.listeners;

import org.noopi.utils.events.TapeInitializationEvent;

public interface TapeInitializationEventListener {
  void onTapeInitialized(TapeInitializationEvent e);
}
