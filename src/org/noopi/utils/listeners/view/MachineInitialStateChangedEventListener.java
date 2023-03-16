package org.noopi.utils.listeners.view;

import java.util.EventListener;

import org.noopi.utils.State;

public interface MachineInitialStateChangedEventListener extends EventListener {
  void onInitialStateChanged(State state);
}
