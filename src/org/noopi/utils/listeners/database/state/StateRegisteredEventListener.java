package org.noopi.utils.listeners.database.state;

import java.util.EventListener;

import org.noopi.utils.events.database.state.StateRegisteredEvent;

public interface StateRegisteredEventListener extends EventListener {
  void onStateRegistered(StateRegisteredEvent e);
}
