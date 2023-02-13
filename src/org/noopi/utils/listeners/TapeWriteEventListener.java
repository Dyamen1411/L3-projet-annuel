package org.noopi.utils.listeners;

import java.util.EventListener;

import org.noopi.utils.events.TapeWriteEvent;

public interface TapeWriteEventListener extends EventListener {
  void onTapeWrite(TapeWriteEvent e);
}
