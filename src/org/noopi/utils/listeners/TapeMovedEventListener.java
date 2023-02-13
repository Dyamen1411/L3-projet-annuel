package org.noopi.utils.listeners;

import java.util.EventListener;

import org.noopi.utils.events.TapeMovedEvent;

public interface TapeMovedEventListener extends EventListener {
  void onTapeMoved(TapeMovedEvent e);
}
