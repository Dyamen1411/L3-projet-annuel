package org.noopi.utils.listeners.database.state;

import java.util.EventListener;

import org.noopi.utils.events.database.state.StateUnRegisteredEvent;

public interface StateUnRegisteredEventListener extends EventListener {
  void onStateUnRegistered(StateUnRegisteredEvent e);
}
